package jf.comp5104.yahtzee.net;

import java.util.Arrays;

public class PlayerCommand {

	static enum Command {
		// inactive commands
		INVALID("invalid"), ENTER(""), SAY("say"), START("start"), STOP("stop"),
		// active commands
		ROLL("roll"), REROLL("reroll"), SCORE("score");

		String desc;

		Command(String str) {
			this.desc = str;
		}

		String getDesc() {
			return desc;
		}

		public String toString() {
			return getDesc();
		}

		public static Command getCommandFromString(String str) {
			String split = str.split(" ")[0];
			return Arrays.stream(Command.values()).filter(c -> c.getDesc().equalsIgnoreCase(split)).findFirst()
					.orElse(Command.INVALID);

		}
	};

}
