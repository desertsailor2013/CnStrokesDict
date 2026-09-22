# -*- coding: utf-8 -*-
"""合并笔画原始 JSON 与释义元数据，生成 app/src/main/assets/dictionary/characters.json（仅示范字头）。"""
import json
import sys
from pathlib import Path

HERE = Path(__file__).resolve().parent
if str(HERE) not in sys.path:
    sys.path.insert(0, str(HERE))

from character_build_util import materialize_meta_entry
from meta_rich import META

ROOT = Path(__file__).resolve().parents[1]
RAW_DIR = ROOT / "scripts" / "hanzi_raw"
OUT = ROOT / "app" / "src" / "main" / "assets" / "dictionary" / "characters.json"


def main() -> None:
    chars = [materialize_meta_entry(m, RAW_DIR) for m in META]
    OUT.write_text(
        json.dumps({"characters": chars}, ensure_ascii=False, indent=2),
        encoding="utf-8",
    )
    print("Wrote", OUT, "chars:", len(chars))


if __name__ == "__main__":
    main()
