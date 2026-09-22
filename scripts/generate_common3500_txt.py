# -*- coding: utf-8 -*-
"""生成 GB2312 一级汉字前 3500 个（编码序），写入 scripts/data/common3500.txt。

说明：与教育部《义务教育语文课程常用字表》字序不完全一致；可替换为官方字表 UTF-8 文本。"""
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "scripts" / "data" / "common3500.txt"
COUNT = 3500


def gb2312_level1_ordered() -> list[str]:
    out: list[str] = []
    for i in range(0xB0A1, 0xD7F9 + 1):
        hi, lo = i >> 8, i & 0xFF
        if lo < 0xA1 or lo > 0xFE:
            continue
        try:
            s = bytes([hi, lo]).decode("gb2312")
        except UnicodeDecodeError:
            continue
        if len(s) == 1:
            out.append(s)
    return out


def main() -> None:
    all_lv1 = gb2312_level1_ordered()
    if len(all_lv1) < COUNT:
        raise SystemExit(f"expected at least {COUNT} chars, got {len(all_lv1)}")
    chosen = all_lv1[:COUNT]
    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text("\n".join(chosen) + "\n", encoding="utf-8")
    print("Wrote", OUT, "lines:", len(chosen))


if __name__ == "__main__":
    main()
