package de.nr137.examplemod.neoforge;

import java.nio.file.Path;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import de.nr137.examplemod.ExampleMod;
import de.nr137.examplemod.Platform;

final class NeoForgePlatform implements Platform {
	@Override
	public Path configDir() {
		return FMLPaths.CONFIGDIR.get();
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
		return ModList.get()
			.getModContainerById(modId)
			.map(container -> container.getModInfo().getVersion().toString())
			.orElse("unknown");
	}
}
