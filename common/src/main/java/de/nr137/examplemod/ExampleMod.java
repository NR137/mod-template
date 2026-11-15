package de.nr137.examplemod;

import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The loader-independent entry point: each loader's entry point (fabric/, neoforge/) calls these methods. */
public final class ExampleMod {
	public static final String MOD_ID = "example_mod";
	public static final String MOD_NAME = "Example Mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	private static Platform platform;
	private static volatile boolean running;

	private ExampleMod() {
	}

	public static void init(Platform loaderPlatform) {
		platform = loaderPlatform;
	}

	public static Platform platform() {
		return platform;
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void onServerStarted(MinecraftServer server) {
		running = true;
		// The smoke test waits for this line (scripts/smoke_test/smoke_test.py).
		LOGGER.info("{} {} is running on Minecraft {}", MOD_NAME, platform.modVersion(), platform.minecraftVersion());
	}

	public static void onServerStopping() {
		running = false;
	}

	public static boolean isRunning() {
		return running;
	}
}
