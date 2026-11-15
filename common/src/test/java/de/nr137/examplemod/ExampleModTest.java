package de.nr137.examplemod;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Unit tests of the loader-independent code: ./gradlew :common:test (./tasks.sh test-java). */
class ExampleModTest {
	@Test
	void idsUseTheModNamespace() {
		assertEquals("example_mod:thing", ExampleMod.id("thing").toString());
	}
}
