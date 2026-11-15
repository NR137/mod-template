package de.nr137.examplemod.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import de.nr137.examplemod.ExampleMod;

public class ExampleModFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		ExampleMod.init(new FabricPlatform());
		ServerLifecycleEvents.SERVER_STARTED.register(ExampleMod::onServerStarted);
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> ExampleMod.onServerStopping());
	}
}
