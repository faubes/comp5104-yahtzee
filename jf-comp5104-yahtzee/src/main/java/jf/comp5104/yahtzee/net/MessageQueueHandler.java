package jf.comp5104.yahtzee.net;

import java.util.Arrays;

import jf.comp5104.yahtzee.AlreadyScoredThereException;
import jf.comp5104.yahtzee.Game;
import jf.comp5104.yahtzee.NotYourTurnException;
import jf.comp5104.yahtzee.Player;
import jf.comp5104.yahtzee.Game.InputGameState;
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
				System.err.println("Error in receive Msg?");
			}
		}
	}

	private void processMessage(Message msg) {
		if (msg.getText() == null) {
			return;
		}

		Command cmd = Command.getCommandFromString(msg.getText());
		Player p = server.sessionPlayerMap.get(msg.getSender());

		try {
			if (hasGameStarted() && g.isCurrentPlayer(p) && g.isAwaitingIndexSet()) {
				int[] rerollIndex = cmd.getRerollIndicies().stream().mapToInt(Integer::intValue).toArray();
				g.reroll(p, rerollIndex);
				server.broadcast(p.getName() + " rerolls " + Arrays.toString(rerollIndex));
				server.broadcast(g.getCurrentPlayer().getRoll().toString());
				g.setInputState(InputGameState.NEEDCOMMAND);
				sendToCurrentPlayer(g.promptPlayer(p));
				return;
			}
			if (hasGameStarted() && g.isCurrentPlayer(p) && g.isAwaitingCategory()) {
				int[] rerollIndex = cmd.getRerollIndicies().stream().mapToInt(Integer::intValue).toArray();
				g.score(p, rerollIndex[0]);
				server.broadcast(p.getName() + " scores in category " + rerollIndex[0]);
				g.setInputState(InputGameState.NEEDCOMMAND);
				server.broadcast(g.toString());
				sendToCurrentPlayer(g.promptPlayer(g.getCurrentPlayer()));
				return;
			}

			switch (cmd) {
			case SAY:
				server.broadcast(new Message(msg.getSender(), msg.getText().substring(4)));
				break;
			case NAME:
				String[] split = msg.getText().split(" ");
				if (split.length != 2) {
					respond(msg, "Syntax: name <newname>");
					break;
				}
				p.setName(split[1]);
				break;
			case START:
				if (hasGameStarted()) {
					respond(msg, "Game has already started.");
					break;
				} else {
					g = new Game(server.playerSessionMap.keySet()).start();
					server.broadcast("Game has begun!");
					server.broadcast(g.toString());
					sendToCurrentPlayer(g.promptPlayer(g.getCurrentPlayer()));
				}
				break;
			case STOP:
				if (!hasGameStarted()) {
					respond(msg, "No game in progress.");
				}
				else {
					server.broadcast("Game has been stopped.");
					g.stop();
				}
				break;
			case ENTER:
				if (hasGameStarted() && g.isCurrentPlayer(p) && g.isFirstRoll()) {
					g.roll(p);
					server.broadcast(p.getName() + " rolls!");
					server.broadcast(g.getCurrentPlayer().getRoll().toString());
					sendToCurrentPlayer(g.promptPlayer(p));
				}
				break;
			case REROLLALL:
				if (hasGameStarted() && g.isCurrentPlayer(p) && !g.isFirstRoll()) {
					g.reroll(p, 1, 2, 3, 4, 5);
					server.broadcast(p.getName() + " rerolls everything!");
					server.broadcast(g.getCurrentPlayer().getRoll().toString());
					sendToCurrentPlayer(g.promptPlayer(p));
				}
				break;
			case REROLLSOME:
				if (hasGameStarted() && g.isCurrentPlayer(p) && !g.isFirstRoll()) {
					g.setInputState(InputGameState.NEEDINDEXSET);
					sendToCurrentPlayer(g.promptPlayer(p));
				}
				break;
			case SCORE:
				if (hasGameStarted() && g.isCurrentPlayer(p) && !g.isFirstRoll()) {
					g.setInputState(InputGameState.NEEDCATEGORY);
					sendToCurrentPlayer(p.getScoresheet().toString());
					sendToCurrentPlayer(g.promptPlayer(p));
				}
				break;
			default:
				// do nothing
				break;
			}
		} catch (NotYourTurnException e) {
			respond(msg, "It's not your turn.");
		} catch (AlreadyScoredThereException e) {
			respond(msg, "You've already scored in that category.");
			g.setInputState(InputGameState.NEEDCOMMAND);
			sendToCurrentPlayer(g.promptPlayer(p));
		} catch (IndexOutOfBoundsException e) {
			respond(msg, "That is not a valid category. Choose 1-13.");
			sendToCurrentPlayer(g.promptPlayer(p));
		}
	}

	private boolean hasGameStarted() {
		return g != null && g.hasStarted();
	}

	// respond to a message from sender with string
	private void respond(Message msg, String string) {
		msg.getSender().send(string);
	}

	private void sendToCurrentPlayer(String string) {
		Player cur = g.getCurrentPlayer();
		server.playerSessionMap.get(cur).send(string);
	}
}
