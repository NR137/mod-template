package de.nr137.examplemod.gametest;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.gametest.framework.GameTestHelper;
import de.nr137.examplemod.ExampleMod;

/**
 * Game tests on a real server, shared by both loaders: fabric/src/gametest and neoforge/src/gametest register each
 * entry. Run with ./tasks.sh gametest.
 */
public final class GameTests {
	private GameTests() {
	}

	/** Test name -> test, in the order they run. */
	public static Map<String, Consumer<GameTestHelper>> all() {
		Map<String, Consumer<GameTestHelper>> tests = new LinkedHashMap<>();
		tests.put("mod_is_running", GameTests::modIsRunning);
		return tests;
	}

	private static void modIsRunning(GameTestHelper helper) {
		helper.assertTrue(ExampleMod.isRunning(), "the mod did not get the server's start event");
		helper.succeed();
	}
}
