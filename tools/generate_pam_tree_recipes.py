"""Generate 49 optional Pam recipes and two independent vanilla log recipes."""
import argparse
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CANONICAL = ROOT / "tools/data/pam_trees_1_0_2_harvests.json"
OUTPUT = ROOT / "src/main/resources/data/foodhealing/recipes/pam_tree_duplication"
VANILLA_OUTPUT = ROOT / "src/main/resources/data/foodhealing/recipes/log_duplication"
VANILLA_HARVESTS = ("minecraft:apple", "minecraft:cocoa_beans")


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--check", action="store_true")
    args = parser.parse_args()
    data = json.loads(CANONICAL.read_text(encoding="utf-8"))
    rows = data["mappings"]
    assert len(rows) == len({r["harvest"] for r in rows}) == 50
    assert len({r["block"] for r in rows}) == 50
    assert "minecraft:apple" in {r["harvest"] for r in rows}
    expected = {}
    for row in rows:
        if row["harvest"] == "minecraft:apple":
            continue  # Still a Pam direct harvest; the unconditional recipe owns this output.
        namespace, name = row["harvest"].split(":")
        recipe = {
            "type": "minecraft:crafting_shapeless",
            "category": "misc",
            "conditions": [{"type": "forge:mod_loaded", "modid": "pamhc2trees"}],
            "ingredients": [{"item": row["harvest"]}, {"tag": "minecraft:logs"}],
            "result": {"item": row["harvest"], "count": 2},
        }
        expected[f"{namespace}_{name}.json"] = json.dumps(recipe, indent=2) + "\n"
    vanilla = {}
    for harvest in VANILLA_HARVESTS:
        recipe = {
            "type": "minecraft:crafting_shapeless",
            "category": "misc",
            "ingredients": [{"item": harvest}, {"tag": "minecraft:logs"}],
            "result": {"item": harvest, "count": 2},
        }
        vanilla[harvest.split(":")[1] + ".json"] = json.dumps(recipe, indent=2) + "\n"
    assert len(expected) == 49 and len(vanilla) == 2
    for directory, files in ((OUTPUT, expected), (VANILLA_OUTPUT, vanilla)):
        existing = {p.name for p in directory.glob("*.json")}
        assert not (existing - set(files)), f"Stale generated recipe in {directory}"
        if args.check:
            assert existing == set(files)
            for name, content in files.items():
                assert (directory / name).read_text(encoding="utf-8") == content, name
        else:
            directory.mkdir(parents=True, exist_ok=True)
            for name, content in files.items():
                (directory / name).write_text(content, encoding="utf-8", newline="\n")
    print(f"Pam canonical50 / conditional49 / vanilla2 / total51 {'verified' if args.check else 'generated'}")


if __name__ == "__main__":
    main()
