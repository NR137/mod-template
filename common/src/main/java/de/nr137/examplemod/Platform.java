package de.nr137.examplemod;

import java.nio.file.Path;

/** What only the loader knows; fabric/ and neoforge/ each implement it. */
public interface Platform {
	Path configDir();

	String modVersion();

	String minecraftVersion();
}
