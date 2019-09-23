package jf.comp5104.yahtzee.net;

import java.util.Arrays;

import jf.comp5104.yahtzee.AlreadyScoredThereException;
import jf.comp5104.yahtzee.Game;
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

		if (hasGameStarted() && g.isCurrentPlayer(p)) {
			// process commands available to current player
			if (processCommandFromCurrentPlayer(p, cmd, msg)) {
				// do not continue if game action from current player successful
				return;
			}
		} // commands available to all players
		switch (cmd) {
		case SAY:
			server.broadcast(new Message(msg.getSender(), msg.getText().substring(4)));
			break;
		case NAME:
			String[] split = msg.getText().split(" ");
			if (split.length != 2) {
				server.respond(msg, "Syntax: name <newname>");
				break;
			}
			p.setName(split[1]);
			break;
		case START:
			if (hasGameStarted()) {
				server.respond(msg, "Game has already started.");
				break;
			} else {
				g = new Game(server.playerSessionMap.keySet()).start();
				server.broadcast("Game has begun!");
				server.broadcast(g.toString());
				// need to prompt current player - who may not be one who starts game
				server.sendToPlayer(p, g.promptPlayer(g.getCurrentPlayer()));
			}
			break;
		case STOP:
			if (!hasGameStarted()) {
				server.respond(msg, "No game in progress.");
			} else {
				server.broadcast("Game has been ended by " + p.getName());
				g.stop();
			}
			break;
		default:
			// catches INVALID command
			// does nothing
			break;
		}
	}

	private boolean processCommandFromCurrentPlayer(Player p, Command cmd, Message msg) throws IllegalArgumentException {
		// we know p is current player
		if (p != g.getCurrentPlayer()) {
			throw new IllegalArgumentException("Attempting to process command for current player, but player not active.");
		}
		try {
			switch (g.getInputState()) {
			// waiting for dice to reroll
			case NEEDINDEXSET:
				int[] rerollIndex = cmd.getNumericValues().stream().mapToInt(Integer::intValue).toArray();
				g.reroll(p, rerollIndex);
				server.broadcast(p.getName() + " rerolls " + Arrays.toString(rerollIndex));
				server.broadcast(g.getCurrentPlayer().getRoll().toString());
				g.setInputState(InputGameState.NEEDCOMMAND);
				server.sendToPlayer(p, g.promptPlayer(p));
				return true;
			// waiting for category to score
			case NEEDCATEGORY:
				int categoryIndex = cmd.getNumericValues().stream().mapToInt(Integer::intValue).findFirst().orElse(0);
				g.score(p, categoryIndex);
				server.broadcast(p.getName() + " scores in category " + categoryIndex);
				g.setInputState(InputGameState.NEEDCOMMAND);
				server.broadcast(g.toString());
				server.sendToPlayer(p, g.promptPlayer(g.getCurrentPlayer()));
				return true;
			// usual player commands
			case NEEDCOMMAND:
				switch (cmd) {
				case ENTER:
					if (g.isFirstRoll()) {
						g.roll(p);
						server.broadcast(p.getName() + " rolls!");
						server.broadcast(g.getCurrentPlayer().getRoll().toString());
						server.sendToPlayer(p, g.promptPlayer(p));
						return true;
					}
					break;
				case REROLLALL:
					if (!g.isFirstRoll()) {
						g.reroll(p, 1, 2, 3, 4, 5);
						server.broadcast(p.getName() + " rerolls everything!");
						server.broadcast(g.getCurrentPlayer().getRoll().toString());
						server.sendToPlayer(p, g.promptPlayer(p));
						return true;
					}
					break;
				case REROLLSOME:
					if (!g.isFirstRoll()) {
						g.setInputState(InputGameState.NEEDINDEXSET);
						server.sendToPlayer(p, g.promptPlayer(p));
						return true;
					}
					break;
				case SCORE:
					if (!g.isFirstRoll()) {
						g.setInputState(InputGameState.NEEDCATEGORY);
						server.sendToPlayer(p, p.getScoresheet().toString());
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
		}
		return false;
	}

	private boolean hasGameStarted() {
		return g != null && g.hasStarted();
	}


}
