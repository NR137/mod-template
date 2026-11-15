package de.nr137.examplemod.neoforge;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import de.nr137.examplemod.ExampleMod;

@Mod(ExampleMod.MOD_ID)
public class ExampleModNeoForge {
	public ExampleModNeoForge() {
		ExampleMod.init(new NeoForgePlatform());
		NeoForge.EVENT_BUS.addListener(ServerStartedEvent.class, event -> ExampleMod.onServerStarted(event.getServer()));
		NeoForge.EVENT_BUS.addListener(ServerStoppingEvent.class, event -> ExampleMod.onServerStopping());
	}
}
