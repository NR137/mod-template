package de.nr137.examplemod.neoforge.gametest;

import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import de.nr137.examplemod.ExampleMod;
import de.nr137.examplemod.gametest.GameTests;

/** Registers the shared {@link GameTests} with NeoForge: each test function, then a test instance for it. */
@EventBusSubscriber(modid = ExampleMod.MOD_ID)
public final class NeoForgeGameTests {
	private static final int MAX_TICKS = 200;

	private NeoForgeGameTests() {
	}

	@SubscribeEvent
	static void registerFunctions(RegisterEvent event) {
		event.register(Registries.TEST_FUNCTION, helper ->
			GameTests.all().forEach((name, test) -> helper.register(ExampleMod.id(name), test)));
	}

	@SubscribeEvent
	static void registerTests(RegisterGameTestsEvent event) {
		Holder<TestEnvironmentDefinition<?>> environment = event.registerEnvironment(ExampleMod.id("default"));
		for (String name : GameTests.all().keySet()) {
			ResourceKey<Consumer<GameTestHelper>> function = ResourceKey.create(Registries.TEST_FUNCTION, ExampleMod.id(name));
			TestData<Holder<TestEnvironmentDefinition<?>>> data =
				new TestData<>(environment, Identifier.withDefaultNamespace("empty"), MAX_TICKS, 1, true);
			event.registerTest(ExampleMod.id(name), new FunctionGameTestInstance(function, data));
		}
	}
}
