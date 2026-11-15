# Example Mod

**One sentence on what the mod does.**

A **server-side mod for Fabric and NeoForge** (Minecraft 26.3). Players join with an unmodded client, and the world
stays a valid vanilla world: remove the mod, and at worst the vanilla behaviour comes back.

**NOT AN OFFICIAL MINECRAFT PRODUCT. NOT APPROVED BY OR ASSOCIATED WITH MOJANG OR MICROSOFT.**

> [!NOTE]
> **Written with AI.** Most of this project's code, texts and docs were written by an AI coding assistant (Claude by
> Anthropic), directed and partly tested by NR137. Not every feature has been tested in depth, so some things may not
> work as expected.

> [!IMPORTANT]
> **This is the template** for NR137's server-side mods. To start a new mod:
> 1. Create the new repo from this one (GitHub: **Use this template**), clone it.
> 2. `python3 scripts/new_mod.py <repo-name> "<Mod Name>"` renames the example mod everywhere (mod id, package,
>    classes, JAR names, compose image) and deletes itself.
> 3. Replace this note and the example texts in this README and `CHANGELOG.md`, and fill in
>    `scripts/smoke_test/scenario.py` with what the mod saves in a world.
>
> Fixes to the build, CI or tests go into this template first and are then carried over to the mods.

## ✨ Features

- …

## 🔒 Permissions

Every command and gated action has a permission node, which a permission mod such as LuckPerms can manage. Without
one, player features are allowed for everyone and admin features for operators.

| Node | Default | What |
| --- | --- | --- |
| … | … | … |

## 🧹 Removing the mod

What stays in the world when the mod is removed, and what becomes vanilla again.

## 🛠️ Development

**Needs:** [Docker](https://docs.docker.com/get-docker/) (with Compose) · Minecraft 26.3 to play on the dev server

Everything else runs in one dev image (`docker/Dockerfile`): Java and Python in the versions of
[`.tool-versions`](.tool-versions) (the only place they are set), Gradle from its wrapper, a virtual screen for the
client tests. [`compose.yaml`](compose.yaml) runs it with your checkout mounted and the Gradle cache in a volume.
Compose rebuilds the image by itself when `docker/Dockerfile` or `.tool-versions` change.

**VS Code:** open the folder and run **Dev Containers: Reopen in Container** (extension *Dev Containers*). Terminal,
Gradle and Java tooling then run in the container, the commands below work without `docker compose run --rm dev`, and
the pre-commit hook is turned on.

The build and test commands live in [`tasks.sh`](tasks.sh):

```bash
docker compose run --rm dev ./tasks.sh build      # JARs in fabric/build/libs/ and neoforge/build/libs/
docker compose run --rm dev ./tasks.sh test       # the quick tests
docker compose run --rm dev ./tasks.sh all        # everything CI runs
docker compose run --rm dev                       # a shell in the dev image
docker compose up server                          # Fabric dev server (fabric/runs/server/); LOADER=neoforge for NeoForge
```

Connect with Minecraft to `localhost:25565`. The first start of a dev server stops until you accept Mojang's EULA in
`<loader>/runs/server/eula.txt`.

### Tests

| Task | What |
| --- | --- |
| `test` | The quick tests (`test-java`) |
| `test-java` | JUnit tests of `common/` (`common/src/test`) |
| `gametest` | Game tests on each loader's game test server, with Lithium (`common/src/gametest`, registered in `fabric/src/gametest` and `neoforge/src/gametest`) |
| `smoke` | The built JARs on the official Fabric and NeoForge servers; a **vanilla client** (the official client, no mods) joins and plays; then each world opens on the **official vanilla server without the mod** and must load, keep what it should and save without errors (needs `build` first; see [`scripts/README.md`](scripts/README.md)) |
| `client` | Singleplayer: each loader's dev client opens a copy of the smoke test's world (needs `smoke` first) |
| `changelog` | `CHANGELOG.md` is ready for a release: its top section is `mod_version` with a date, and there is no Unreleased section. With a base ref (`./tasks.sh changelog origin/main`), a change to the mod JAR must also raise `mod_version` |
| `changes` | What the changes since a base ref touch: `jar`, `tools` or `docs` (see Releasing) |
| `versions` | Every version the project uses against the newest one (Minecraft, loaders, Mixin, Java, Gradle and its plugins, JUnit, the dev image's tools, the workflows' actions and runners); fails when something can be updated |

The `tests` workflow (`.github/workflows/tests.yml`) runs them in the same image: on draft pull requests only the quick
tests, on other pull requests, pushes to `main` and before every release all of them. A pull request that only changes
texts skips them. The `versions` workflow runs `versions` on every pull request into `main` and by hand; it is not
required to merge.

A pre-commit hook runs the Java tests when a commit touches the code, in the dev container or through Docker. The dev
container turns it on; in another clone, run `git config core.hooksPath .githooks` once. `git commit --no-verify`
skips it.

### Releasing

The `release` workflow (`.github/workflows/release.yml`) only runs when started by hand: **Actions → release → Run
workflow**, or `gh workflow run release.yml`. It runs the tests first; if one fails, nothing is built or released.

Every change that changes the mod JAR is a release; the rest goes into `main` without one. `./tasks.sh changes` sorts
a pull request by its files:

| Kind | Files | Tests | Release |
| --- | --- | --- | --- |
| `jar` | `common/`, `fabric/`, `neoforge/`, `build-logic/`, the Gradle files, `LICENSE`, `.tool-versions` (but not the tests in them) | all | yes: `mod_version` must be raised |
| `tools` | the tests in those folders, `scripts/`, `docker/`, `.github/`, `compose.yaml`, `tasks.sh`, … | all | no |
| `docs` | `*.md` outside those folders, `docs/` | skipped (the checks pass) | no |

1. For a release, set `mod_version` in `gradle.properties` and give its `CHANGELOG.md` section a date
   (`## [x.y.z] - YYYY-MM-DD`), on the pull request's branch before merging.
2. Run the workflow without "draft release" to only build: the two JARs and the release notes are attached to the run
   for a day.
3. Run it with "draft release" to create a draft GitHub release `v<mod_version>+<minecraft_version>` with both JARs and
   that version's `CHANGELOG.md` section as notes; check it, then publish it on GitHub.

### Project layout

One codebase builds both loaders (based on [MultiLoader-Template](https://github.com/jaredlll08/MultiLoader-Template)):

| Folder | What |
| --- | --- |
| `common/` | Everything shared, including the mixins. Compiled against plain Minecraft, so it cannot use Fabric or NeoForge APIs. |
| `fabric/` | Fabric entry point, `FabricPlatform`, `fabric.mod.json` |
| `neoforge/` | NeoForge entry point, `NeoForgePlatform`, `neoforge.mods.toml` |
| `build-logic/` | Gradle scripts shared by the three projects |
| `docker/`, `compose.yaml`, `.devcontainer/` | The dev image, its tasks and the VS Code dev container |
| `scripts/` | Smoke, vanilla and singleplayer tests, version check ([README](scripts/README.md)) |

Loader-specific needs go through the small `Platform` interface in `common/`; each loader passes its implementation to
`ExampleMod.init` at startup.

## 🧰 Built with

- **Mod:** Java with [Gradle](https://gradle.org), [Fabric Loom](https://github.com/FabricMC/fabric-loom) and
  [ModDevGradle](https://github.com/neoforged/ModDevGradle), in the project layout of
  [MultiLoader-Template](https://github.com/jaredlll08/MultiLoader-Template) (CC0)
- **Tests:** [JUnit](https://junit.org), Minecraft's game tests, [Python](https://www.python.org); the tests run with
  [Lithium](https://modrinth.com/mod/lithium) (LGPL-3.0, downloaded for testing, not part of the mod)
- **Dev environment:** [Docker](https://www.docker.com)

## 📄 License

MIT License, see [LICENSE](LICENSE). © NR137. The JAR contains no files of the game. Minecraft and the game's names
belong to Mojang and Microsoft.
