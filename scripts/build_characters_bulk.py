# -*- coding: utf-8 -*-
"""从 Make Me A Hanzi 的 graphics.txt 与常用字表生成 characters.json（约 3500 字头）。

准备：
  1) pip install -r requirements.txt
  2) 将 makemeahanzi 仓库中的 graphics.txt 放到 scripts/vendor/graphics.txt
     （或 git clone https://github.com/skishore/makemeahanzi.git 后使用其中的 graphics.txt）
  3) python scripts/generate_common3500_txt.py   # 若尚无 scripts/data/common3500.txt
  4) python scripts/build_characters_bulk.py --graphics scripts/vendor/graphics.txt

meta_rich.py 中的字头仍合并完整释义与本地 hanzi_raw 笔画。"""
from __future__ import annotations

import argparse
import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
if str(ROOT / "scripts") not in sys.path:
    sys.path.insert(0, str(ROOT / "scripts"))

from character_build_util import materialize_meta_entry
from meta_rich import META

RAW_DIR = ROOT / "scripts" / "hanzi_raw"
DEFAULT_LIST = ROOT / "scripts" / "data" / "common3500.txt"
OUT = ROOT / "app" / "src" / "main" / "assets" / "dictionary" / "characters.json"


def load_ordered_chars(path: Path) -> list[str]:
    text = path.read_text(encoding="utf-8")
    lines = [ln.strip() for ln in text.splitlines() if ln.strip()]
    return [ln[0] for ln in lines if ln]


def pinyin_for_char(c: str) -> str:
    from pypinyin import Style, pinyin as py_pinyin

    groups = py_pinyin(c, heteronym=True, style=Style.TONE)
    flat: list[str] = []
    for g in groups:
        for x in g:
            if x not in flat:
                flat.append(x)
    return " / ".join(flat) if flat else ""


def minimal_entry(char: str, strokes: list[str]) -> dict:
    return {
        "char": char,
        "pinyin": pinyin_for_char(char),
        "radical": "",
        "shuowen": "",
        "pinyin_word_examples": [],
        "radical_strokes": 0,
        "definitions": [],
        "words": [],
        "idioms": [],
        "strokes": strokes,
        "evolution": [],
        "stroke_hints": [f"第 {i + 1} 笔" for i in range(len(strokes))],
    }


def index_graphics(graphics_path: Path, need: frozenset[str]) -> dict[str, list[str]]:
    out: dict[str, list[str]] = {}
    n = len(need)
    with graphics_path.open(encoding="utf-8") as f:
        for line in f:
            if len(out) >= n:
                break
            line = line.strip()
            if not line:
                continue
            try:
                o = json.loads(line)
            except json.JSONDecodeError:
                continue
            c = o.get("character")
            if c not in need or c in out:
                continue
            strokes = o.get("strokes") or []
            if strokes:
                out[c] = strokes
    return out


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument(
        "--graphics",
        type=Path,
        required=True,
        help="makemeahanzi graphics.txt 路径",
    )
    ap.add_argument("--list", type=Path, default=DEFAULT_LIST, help="常用字表，每行一字")
    ap.add_argument("--out", type=Path, default=OUT)
    args = ap.parse_args()

    if not args.graphics.is_file():
        print("找不到 graphics 文件:", args.graphics, file=sys.stderr)
        sys.exit(1)
    if not args.list.is_file():
        print("找不到字表:", args.list, "请先运行 scripts/generate_common3500_txt.py", file=sys.stderr)
        sys.exit(1)

    ordered = load_ordered_chars(args.list)
    meta_by_char = {m["char"]: m for m in META}
    need_gfx = frozenset(c for c in ordered if c not in meta_by_char)

    print("Indexing graphics for", len(need_gfx), "chars (streaming)...")
    strokes_by = index_graphics(args.graphics, need_gfx)
    missing = sorted(need_gfx - set(strokes_by.keys()))
    if missing:
        print(
            "警告：下列字头在 graphics.txt 中无笔画数据（将跳过，详情页会缺字）：",
            len(missing),
            file=sys.stderr,
        )
        for c in missing[:30]:
            print(" ", repr(c), file=sys.stderr)
        if len(missing) > 30:
            print("  ...", file=sys.stderr)

    chars_out: list[dict] = []
    skipped = 0
    for c in ordered:
        if c in meta_by_char:
            chars_out.append(materialize_meta_entry(meta_by_char[c], RAW_DIR))
        elif c in strokes_by:
            chars_out.append(minimal_entry(c, strokes_by[c]))
        else:
            skipped += 1

    args.out.parent.mkdir(parents=True, exist_ok=True)
    args.out.write_text(
        json.dumps({"characters": chars_out}, ensure_ascii=False, indent=2),
        encoding="utf-8",
    )
    print("Wrote", args.out, "entries:", len(chars_out), "skipped:", skipped)


if __name__ == "__main__":
    main()
