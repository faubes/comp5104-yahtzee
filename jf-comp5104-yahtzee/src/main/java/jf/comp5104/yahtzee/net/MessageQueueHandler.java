package jf.comp5104.yahtzee.net;

import java.util.Arrays;

import jf.comp5104.yahtzee.Game;
import jf.comp5104.yahtzee.Player;
import jf.comp5104.yahtzee.Roll;
import jf.comp5104.yahtzee.Yahtzee;
import jf.comp5104.yahtzee.Game.InputGameState;
import jf.comp5104.yahtzee.exceptions.AlreadyScoredThereException;
import jf.comp5104.yahtzee.net.PlayerCommand.Command;
import jf.comp5104.yahtzee.net.Message;

// MessageQueueHandler consumes messages from Blocking Queue messageQueue
// Responds to commands and changes game state.

class MessageQueueHandler implements Runnable {

	private volatile boolean shutdown;
	YahtzeeServer server;
	Game g;

	MessageQueueHandler(YahtzeeServer server) {
		this.server = server;
		this.shutdown = false;
	}

	void setShutdown(boolean b) {
		this.shutdown = b;
	}

	public void run() {
		System.out.println("Listening for messages");
		while (!shutdown) {
			try {
				if (server.isQueueEmpty()) {
					// for debug
					// System.out.println("No messages");
					Thread.sleep(500);
				} else {
					// System.out.println("take message from q");
					processMessage(server.getMessage());
					// System.out.println(msg);
				}
			} catch (InterruptedException e) {
				System.err.println("Listener thread interrupted, ending");
				shutdown = true;
			}
		}
	}

	private void processMessage(Message msg) {
		if (msg.getText() == null) {
			return;
		}
		Command cmd = Command.getCommandFromString(msg.getText());
		Player p = server.sessionPlayerMap.get(msg.getSender());

		switch (cmd) {
		case SAY:
			if (msg.getText().length() >= 4) {
				System.out.println(p.getName() + " says: " + msg.getText().substring(4));
				server.broadcast(new Message(msg.getSender(), msg.getText().substring(4)));
			}
			break;
		case DISPLAY:
			if (hasGameStarted()) {
				server.respond(msg, g.toString());
				if (p == g.getCurrentPlayer()) {
					server.sendToPlayer(p, g.promptPlayer(p));
				}
			}
			break;
		case SERVER:
			server.respond(msg, server.toString());
			break;
		case WHO:
			server.respond(msg, server.playerSessionMap.keySet().toString());
			break;
		case HELP:
			server.respond(msg, PlayerCommand.Command.getCommands());
			break;
		case NAME:
			String[] split = msg.getText().split(" ");
			if (split.length != 2) {
				server.respond(msg, "Syntax: name <newname>");
				break;
			}
			System.out.println(p.getName() + " changed name to " + split[1]);
			server.respond(msg, "Changed your name to " + split[1]);
			server.sendToAllExceptSender(msg, p.getName() + " has changed their name to " + split[1]);
			p.setName(split[1]);
			break;
		case START:
			if (hasGameStarted()) {
				server.respond(msg, "Game has already started.");
				break;
			} else {
				System.out.println("Game started by " + p.getName());
				g = new Game(server.playerSessionMap.keySet()).start();
				server.respond(msg, "You start a game");
				server.sendToAllExceptSender(msg, p.getName() + " starts a game");
				server.broadcast(g.toString());
				// need to prompt current player - who may not be one who starts
				// game
				server.sendToPlayer(g.getCurrentPlayer(), g.promptPlayer(g.getCurrentPlayer()));
				server.sendToAllExceptPlayer(g.getCurrentPlayer(),
						g.getCurrentPlayer().getName() + " is deciding what to do.");
			}
			break;
		case STOP:
			if (!hasGameStarted()) {
				server.respond(msg, "No game in progress.");
			} else {
				System.out.println("Game ended by " + p.getName());
				server.broadcast("Game has been ended by " + p.getName());
				g.stop();
			}
			break;
		case QUIT:
			if (hasGameStarted()) {
				server.broadcast(p.getName() + " leaves. Game ends.");
				g.removePlayer(p);
				g.stop();
			}
			server.respond(msg, "See ya later");
			server.disconnect(msg.getSender());
			break;
		default:
			if (hasGameStarted() && g.isCurrentPlayer(p)) {
				// process commands available to current player
				processCommandFromCurrentPlayer(p, cmd, msg);
				// server.sendToPlayer(p, g.promptPlayer(p));
			}

			break;
		}
	}

	private boolean processCommandFromCurrentPlayer(Player p, Command cmd, Message msg)
			throws IllegalArgumentException {
		// we know p is current player
		if (p != g.getCurrentPlayer()) {
			throw new IllegalArgumentException(
					"Attempting to process command for current player, but player not active.");
		}
		try {
			switch (g.getInputState()) {
			// waiting for dice to reroll
			case NEEDHOLDSET:
				int[] holdIndex = Command.getRerollIndiciesFromString(msg.getText()).stream()
						.mapToInt(Integer::intValue).toArray();

				g.reroll(p, Roll.indexComplement(holdIndex));
				server.respond(msg, "You hold " + Arrays.toString(holdIndex));
				server.sendToAllExceptSender(msg, p.getName() + " holds " + Arrays.toString(holdIndex));

				server.respond(msg, "You get " + Yahtzee.EOL + p.getRoll().toString());
				server.sendToAllExceptSender(msg, p.getName() + " gets " + Yahtzee.EOL + p.getRoll().toString());

				g.setInputState(InputGameState.NEEDCOMMAND);
				server.sendToPlayer(p, g.promptPlayer(p));
				return true;
			case NEEDINDEXSET:
				int[] rerollIndex = Command.getRerollIndiciesFromString(msg.getText()).stream()
						.mapToInt(Integer::intValue).toArray();

				g.reroll(p, rerollIndex);
				server.respond(msg, "You reroll " + Arrays.toString(rerollIndex));
				server.sendToAllExceptSender(msg, p.getName() + " rerolls " + Arrays.toString(rerollIndex));

				server.respond(msg, "You get " + Yahtzee.EOL + p.getRoll().toString());
				server.sendToAllExceptSender(msg, p.getName() + " gets " + Yahtzee.EOL + p.getRoll().toString());

				g.setInputState(InputGameState.NEEDCOMMAND);
				server.sendToPlayer(p, g.promptPlayer(p));
				return true;

			// waiting for category to score
			case NEEDCATEGORY:
				int categoryIndex = Command.getRerollIndiciesFromString(msg.getText()).stream()
						.mapToInt(Integer::intValue).findFirst().orElse(0);
				g.score(p, categoryIndex); // score calls endTurn, which changes
											// current player
				server.broadcast(
						p.getName() + " scores in category " + categoryIndex + " for " + p.getScore(categoryIndex));
				if (g.hasEnded()) {
					server.broadcast("The game has ended!");
					server.broadcast(g.toString());
					System.out.println("Game has ended.");
					g.setInputState(InputGameState.NEEDCOMMAND);
					return true;
				}

				if (g.newRound()) {
					server.broadcast("Starting round " + g.getRound());
					System.out.println("Starting round " + g.getRound());
				}
				g.setInputState(InputGameState.NEEDCOMMAND);

				server.broadcast("It is now " + g.getCurrentPlayer().getName() + "'s turn.");
				server.broadcast(g.toString());
				// score ends the turn, so need to send to current player
				server.sendToPlayer(g.getCurrentPlayer(), g.promptPlayer(g.getCurrentPlayer()));
				return true;
			// usual player commands
			case NEEDCOMMAND:
				switch (cmd) {
				case ENTER:
					if (g.isFirstRoll()) {
						g.roll(p);
						server.respond(msg, "You roll:\n " + p.getRoll().toString());
						server.sendToAllExceptSender(msg, p.getName() + " rolls: \n" + p.getRoll().toString());
						server.sendToPlayer(p, g.promptPlayer(p));
						server.sendToAllExceptSender(msg, p.getName() + " is deciding what to do next.");
						return true;
					}
					break;
				case REROLLALL:
					if (!g.isFirstRoll()) {
						g.reroll(p);
						server.respond(msg, "You reroll everything.");
						server.sendToAllExceptSender(msg, p.getName() + " rerolls everything.");
						server.broadcast(p.getRoll().toString());
						server.sendToPlayer(p, g.promptPlayer(p));
						server.sendToAllExceptSender(msg, p.getName() + " is deciding what to do next.");
						return true;
					}
					break;
				case REROLLSOMEBYINDEX:
					if (!g.isFirstRoll()) {
						g.setInputState(InputGameState.NEEDINDEXSET);
						server.sendToPlayer(p, g.promptPlayer(p));
						return true;
					}
					break;
				case REROLLSOMEBYHOLDINDEX:
					if (!g.isFirstRoll()) {
						g.setInputState(InputGameState.NEEDHOLDSET);
						server.sendToPlayer(p, g.promptPlayer(p));
						return true;
					}
					break;
				case SCORE:
					if (!g.isFirstRoll()) {
						g.setInputState(InputGameState.NEEDCATEGORY);
						server.sendToPlayer(p, p.getScoresheet().toString());
						server.sendToPlayer(p, p.getRoll().toString());
						server.sendToPlayer(p, g.promptPlayer(p));
						return true;
					}
					break;
				default:
					break;
				}
				break; // end of NEEDCOMMAND state
			default:
				break;
			}
		} catch (AlreadyScoredThereException e) {
			server.respond(msg, "You've already scored in that category.");
			g.setInputState(InputGameState.NEEDCOMMAND);
			server.sendToPlayer(p, g.promptPlayer(p));
		} catch (IndexOutOfBoundsException e) {
			server.respond(msg, "That is not a valid category. Choose 1-13.");
			server.sendToPlayer(p, g.promptPlayer(p));
		} catch (IllegalStateException e) {
			server.respond(msg, "That is not a valid index for rerolling");
			g.setInputState(InputGameState.NEEDCOMMAND);
			server.sendToPlayer(p, g.promptPlayer(p));
		} catch (NumberFormatException e) {
			server.respond(msg, "That is not a valid index for rerolling");
			g.setInputState(InputGameState.NEEDCOMMAND);
			server.sendToPlayer(p, g.promptPlayer(p));			
		}
		return false;
	}

	private boolean hasGameStarted() {
		return g != null && g.hasStarted();
	}

	public void removePlayer(Player p) {
		if (hasGameStarted()) {
			server.broadcast("Removing player " + p.getName());
			g.removePlayer(p);
		}
	}

	public void stopGame() {
		if (hasGameStarted()) {
			server.broadcast("Game stopped.");
			g.stop();
		}

	}

}
