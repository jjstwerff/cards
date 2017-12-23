package cards.generate;

import com.betterbe.memorydb.generate.Generate;
import com.betterbe.memorydb.generate.Project;
import com.betterbe.memorydb.generate.Record;
import com.betterbe.memorydb.generate.Type;

public class Structure {
	public static void main(String[] args) {
		Project project = new Project("Cards", "cards.record", "src/main/java/cards/record");

		Record statistic = project.table("Statistic");
		statistic.field("name", Type.STRING);
		statistic.index("statisticName", "name");
		// STRENGTH, TINKER, SOCIAL, PERCEPTION, SPEED
		// POINT_BLANK, SHORT, LONG
		// SMOKE, COVER, FULL, FLANKED
		// DODGE, RECOIL
		// DAMAGE, PIERCE, ARMOR
		// ALERTNESS, PANIC, IMPRESS, RECOVER
		// CONCENTRATE, MOVE
		// SLOW, BACK, SHOVE, PRONE, PIN
		// BLIND, BURN, BLEED, KNOCK, CHOKE

		Record cardStatistic = project.table("CardStatistic");
		cardStatistic.field("statistic", Type.RELATION, statistic);
		cardStatistic.field("value", Type.BYTE);

		Record card = project.table("Card");
		card.field("name", Type.STRING);
		card.field("set", Type.ENUMERATE).setValues("RACIAL", "BACKGROUND", "MOVE", "WEAPON", "WEARING", "IMPLANT", "ENCOUNTER", "ROOM", "OPPONENT", "COMBAT");
		card.field("stats", Type.ARRAY, cardStatistic);

		Record racial = project.table("Racial");
		racial.field("card", Type.RELATION, card);

		Record race = project.table("Race");
		race.field("name", Type.STRING);
		race.field("cards", racial, "card.name");

		Record rules = project.table("Rules");
		rules.field("name", Type.STRING);
		rules.field("races", race, "name");
		rules.field("cards", card, "name");
		rules.index("rulesName", "name");

		Record game = project.table("Game");
		game.field("name", Type.STRING);
		game.field("rules", Type.RELATION, rules);
		game.index("gameName", "name");

		Record member = project.table("Member");
		member.field("game", Type.RELATION, game);
		member.field("role", Type.ENUMERATE).setValues("PLAYER", "MASTER", "OBSERVER");
		member.field("xp", Type.INTEGER);

		Record player = project.table("Player");
		player.field("creation", Type.DATE);
		player.field("last", Type.DATE);
		player.field("name", Type.STRING);
		player.field("member", member, "game.name");
		player.index("playerName", "name");

		Generate.project(project);
		System.out.println("Refresh the project to allow eclipse to see the last changes");
	}
}
