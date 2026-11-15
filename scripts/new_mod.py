"""Turns a copy of the template into a new mod: renames the example mod's names in every file and path.

Usage: python3 scripts/new_mod.py <repo-name> "<Mod Name>"
  e.g. python3 scripts/new_mod.py synced-shulker-boxes "Synced Shulker Boxes"
gives the repo/JAR name synced-shulker-boxes, the mod id synced_shulker_boxes, the package
de.nr137.syncedshulkerboxes and the class SyncedShulkerBoxes (with SyncedShulkerBoxesFabric, ...NeoForge).
Run it once, in the new repo, right after copying the template; it deletes itself afterwards.
"""

import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SKIPPED = {'.git', '.gradle', 'build', 'runs', '.cache', '__pycache__'}
BINARY = {'.jar', '.png', '.class'}


def files():
	for path in sorted(ROOT.rglob('*')):
		if (path.is_file() and not SKIPPED & set(path.relative_to(ROOT).parts) and path.suffix not in BINARY
				and path != Path(__file__).resolve()):
			yield path


def main() -> None:
	if len(sys.argv) != 3 or not re.fullmatch(r'[a-z][a-z0-9]*(-[a-z0-9]+)*', sys.argv[1]):
		sys.exit(__doc__)
	repo, name = sys.argv[1], sys.argv[2]
	words = repo.split('-')
	replacements = [
		('de.nr137.examplemod', 'de.nr137.' + ''.join(words)),
		('de/nr137/examplemod', 'de/nr137/' + ''.join(words)),
		('ExampleMod', ''.join(word.capitalize() for word in words)),
		('example_mod', '_'.join(words)),
		('example-mod', repo),
		('Example Mod', name),
	]
	if not (ROOT / 'common/src/main/java/de/nr137/examplemod').is_dir():
		sys.exit('The example mod is already renamed.')
	for path in list(files()):
		text = path.read_text(encoding='utf-8')
		new = text
		for old, value in replacements:
			new = new.replace(old, value)
		if new != text:
			path.write_text(new, encoding='utf-8')
	# Paths: deepest first, so renaming a folder doesn't move what is still to be renamed.
	for path in sorted(ROOT.rglob('*'), key=lambda p: len(p.parts), reverse=True):
		if SKIPPED & set(path.relative_to(ROOT).parts):
			continue
		new_name = path.name
		for old, value in replacements:
			new_name = new_name.replace(old, value)
		if path.name == 'examplemod':
			new_name = ''.join(words)
		if new_name != path.name:
			path.rename(path.with_name(new_name))
	Path(__file__).unlink()
	print(f'Renamed the example mod to {name} ({repo}). Next: README.md, CHANGELOG.md and scripts/smoke_test/scenario.py.')


if __name__ == '__main__':
	main()
