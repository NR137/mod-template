package de.nr137.examplemod.fabric;

import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;
import de.nr137.examplemod.ExampleMod;
import de.nr137.examplemod.Platform;

final class FabricPlatform implements Platform {
	@Override
	public Path configDir() {
		return FabricLoader.getInstance().getConfigDir();
	}

	@Override
	public String modVersion() {
		return version(ExampleMod.MOD_ID);
	}

	@Override
	public String minecraftVersion() {
		return version("minecraft");
	}

	private static String version(String modId) {
		return FabricLoader.getInstance()
			.getModContainer(modId)
			.map(container -> container.getMetadata().getVersion().getFriendlyString())
			.orElse("unknown");
	}
}
