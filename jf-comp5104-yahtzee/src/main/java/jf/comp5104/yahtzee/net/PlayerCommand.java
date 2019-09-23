package jf.comp5104.yahtzee.net;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerCommand {

	

	static enum Command {
		// inactive commands
		INVALID("invalid"), ENTER(""), SAY("say"), NAME("name"),
		START("start"), STOP("stop"), DISPLAY("display"),
		// active commands
		REROLLALL("1"), REROLLSOME("2"), SCORE("3");

		String desc;
		static List<Integer> rerollIndicies = new ArrayList<>();
		
		Command(String str) {
			this.desc = str;
		}

		String getDesc() {
			return desc;
		}
		
		List<Integer> getNumericValues() {
			return rerollIndicies;
		}

		public String toString() {
			return getDesc();
		}
		
		public static Command getCommandFromString(String str) {
			String[] split = str.split(" ");
			try {
			rerollIndicies = Arrays.stream(split)
					.map(Integer::parseInt)
					.collect(Collectors.toList());
					
			}
			catch (NumberFormatException e) {
				// do not care about inputs which are not valid index set
			}
			
			
			return Arrays.stream(Command.values())
					.filter(c -> c.getDesc().equalsIgnoreCase(split[0]))
					.findFirst()
					.orElse(Command.INVALID);

		}
	};

}
