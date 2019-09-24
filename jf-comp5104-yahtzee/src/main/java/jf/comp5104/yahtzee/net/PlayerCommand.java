package jf.comp5104.yahtzee.net;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jf.comp5104.yahtzee.Yahtzee;

public class PlayerCommand {

	static enum Command {
		// inactive commands
		INVALID("invalid", "not a valid command"), HELP("help", "print this list"), SAY("say",
				"share your thoughts with other players"), NAME("name", "change your name with name <whatever>"), START(
						"start", "start a game"), STOP("stop", "end the game"), WHO("who", "display list of players"),
		// active commands (in game)
		DISPLAY("display", "prints out the current state of the game"), ENTER("", "<<ENTER>> Roll!"), REROLLSOME("1",
				"Reroll some"), REROLLALL("2", "Reroll all"), SCORE("3", "Score");

		String cmd;
		String desc;
		static List<Integer> rerollIndicies = new ArrayList<>();

		Command(String cmd, String desc) {
			this.cmd = cmd;
			this.desc = desc;

		}

		String getCmd() {
			return cmd;
		}

		String getDesc() {
			return desc;
		}

		List<Integer> getNumericValues() {
			return rerollIndicies;
		}

		public String toString() {
			return getCmd();
		}

		public static Command getCommandFromString(String str) {
			String[] split = str.split(" ");
			try {
				rerollIndicies = Arrays.stream(split).map(Integer::parseInt).collect(Collectors.toList());

			} catch (NumberFormatException e) {
				// do not care about inputs which are not valid index set
			}

			return Arrays.stream(Command.values()).filter(c -> c.getCmd().equalsIgnoreCase(split[0])).findFirst()
					.orElse(Command.INVALID);

		}

		public static String getCommands() {
			StringBuilder sb = new StringBuilder();
			for (Command c : Command.values()) {
				if (c == INVALID)
					continue;
				sb.append(c.getCmd());
				sb.append("\t");
				sb.append(c.getDesc());
				sb.append(Yahtzee.EOL);
			}
			return sb.toString();
		}
	};

}
