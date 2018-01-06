package cards.generate;

import com.betterbe.memorydb.generate.Generate;
import com.betterbe.memorydb.generate.Project;
import com.betterbe.memorydb.generate.Record;
import com.betterbe.memorydb.generate.Type;

public class CardsStructure {
	public static void main(String[] args) {
		Generate.project(getProject());
		System.out.println("Refresh the project to allow eclipse to see the last changes");
	}

	public static Project getProject() {
		Project project = new Project("Cards", "cards.record", "src/main/java/cards/record");

		Record statistic = project.table("Statistic");
		statistic.field("name", Type.STRING);
		statistic.field("nr", Type.INTEGER);
		statistic.index("statisticName", "name");
		statistic.index("statisticNr", "nr");
		// MIGHT, TINKER, SOCIAL, PERCEPTION, SPEED
		// POINT_BLANK, SHORT, LONG, AREA
		// SMOKE, COVER, FULL, FLANKED
		// DODGE, RECOIL
		// DAMAGE, PIERCE, ARMOR
		// ALERTNESS, PANIC, IMPRESS, RECOVER
		// CONCENTRATE, MOVE, FLY
		// SLOW, BACK, SHOVE, PRONE, PIN
		// BLIND, BURN, BLEED, KNOCK, CHOKE, UNARM

		// LEFT, RIGHT, FORWARDS, BACKWARDS
		// NUMBER

		Record cardStatistic = project.record("CardStatistic");
		cardStatistic.field("statistic", Type.RELATION, statistic);
		cardStatistic.field("value", Type.BYTE);

		Record card = project.table("Card");
		card.field("name", Type.STRING);
		card.field("set", Type.ENUMERATE, "RACIAL", "BACKGROUND", "MOVE", "WEAPON", "WEARING", "IMPLANT", "ENCOUNTER", "ROOM", "OPPONENT", "COMBAT");
		card.field("stats", Type.ARRAY, cardStatistic);

		Record cards = project.record("Cards");
		cards.field("card", Type.RELATION, card);

		Record race = project.table("Race");
		race.field("name", Type.STRING);
		race.field("cards", Type.ARRAY, cards);

		Record rules = project.table("Rules");
		rules.field("name", Type.STRING);
		rules.field("races", race, "name");
		rules.field("cards", card, "name");
		rules.index("rulesName", "name");

		Record connect = project.table("Connect");
		connect.field("nr", Type.INTEGER);
		connect.field("type", Type.ENUMERATE, "DOOR", "CLIMB", "LINE");
		connect.field("checks", Type.ARRAY, cards);

		Record room = project.table("Room");
		room.field("name", Type.STRING);
		room.field("opponent", Type.ARRAY, cards);
		room.field("items", Type.ARRAY, cards);
		room.field("connection", connect, "nr");
		connect.field("to", Type.RELATION, room);

		Record goal = project.table("Goal");
		goal.field("name", Type.STRING);
		goal.field("type", Type.ENUMERATE, "KNOWLEDGE", "WEAPON", "WEARABLE", "STATUS", "IMPLANT");
		goal.field("XP", Type.INTEGER);
		goal.field("gained", Type.ENUMERATE, "STASH", "OVERHEAR", "REWARD");

		Record material = project.table("Material"); // TODO allow textures.. bump-map/color .. sizes
		material.field("name", Type.STRING);
		material.field("color", Type.INTEGER);

		Record item = project.table("Item"); // TODO allow size & drawing & state
		// PERSON/TABLE/BENCH/CHAIR/BED/BEDROLL/LAMP/ROOM_NR/TREE_TRUNC
		item.field("name", Type.STRING);
		item.field("material", Type.RELATION, material);

		Record wall = project.record("Wall");
		// OPEN/COMBINED/BRICKS/ROUNDED_BRICKS/CONCRETE/ROUNDED_CONCRETE/METAL/WOOD/STONE/ROUNDED_STONE/ROCK/FENCE/LOW_WALL/LOW_FENCE/TREE_TOP
		wall.field("name", Type.STRING);
		wall.field("thickness", Type.BYTE);
		wall.field("sloped", Type.BOOLEAN);
		wall.field("combineLevel", Type.BYTE);
		wall.field("material", Type.RELATION, material);
		wall.field("item", Type.RELATION, item);
		wall.field("inwards", Type.BOOLEAN);

		Record floor = project.record("Floor");
		// OPEN/FILLED/DOOR/METAL_DOOR/WINDOW/WOOD/CONCRETE/ASPHALT/GRAVEL/GRASS/DIRT/ROCK/STAIRS/METAL/METAL_STAIRS/TILES/TILED/TREE_TOP
		floor.field("name", Type.STRING);
		floor.field("type", Type.ENUMERATE, "OPEN", "FILLED", "INSIDE");
		floor.field("sloped", Type.BOOLEAN);
		floor.field("material", Type.RELATION, material);

		Record element = project.record("Element");
		element.field("l", Type.BYTE);
		element.field("t", Type.BYTE);
		element.field("r", Type.BYTE);
		element.field("f", Type.BYTE);
		element.field("i", Type.BYTE);
		element.field("d", Type.BYTE); // per 5 degrees 72 max
		element.field("h", Type.SHORT); // per 10cm

		Record map = project.table("Map");
		map.field("x", Type.INTEGER); // per m
		map.field("y", Type.INTEGER); // per m
		map.field("z", Type.INTEGER); // no measurement .. just ordering
		map.field("l", Type.INTEGER); // length in Y elements
		map.field("d", Type.ARRAY, element);

		Record area = project.table("Area");
		area.field("name", Type.STRING);
		area.field("rooms", room, "name");
		area.field("encounter", Type.ARRAY, cards);
		area.field("goal", goal, "name");
		area.field("maps", map, "x", "y", "z");

		Record skill = project.table("Skill");
		skill.field("card", Type.RELATION, card);
		skill.field("state", Type.ENUMERATE, "STASHED", "PARTY", "ACTIVE");

		Record character = project.table("Character");
		character.field("name", Type.STRING);
		character.field("skills", skill, "card.name");

		Record game = project.table("Game");
		game.field("name", Type.STRING);
		game.field("areas", area, "name");
		game.field("rules", Type.RELATION, rules);
		game.field("characters", character, "name");
		game.field("walls", Type.ARRAY, wall); // max 256
		game.field("floors", Type.ARRAY, floor); // max 256
		game.field("items", item, "name");
		game.field("materials", material, "name");
		game.index("gameName", "name");

		Record member = project.table("Member");
		member.field("game", Type.RELATION, game);
		member.field("role", Type.ENUMERATE, "PLAYER", "MASTER", "OBSERVER");
		member.field("xp", Type.INTEGER);

		Record player = project.table("Player");
		player.field("creation", Type.DATE);
		player.field("last", Type.DATE);
		player.field("name", Type.STRING);
		player.field("member", member, "game.name");
		player.index("playerName", "name");
		return project;
	}
}
