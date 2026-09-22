# -*- coding: utf-8 -*-
import copy
import json
from pathlib import Path


def materialize_meta_entry(m: dict, raw_dir: Path) -> dict:
    """将 meta_rich 中一条记录与 hanzi_raw 合并为可写入 characters.json 的字典。"""
    m = copy.deepcopy(m)
    raw_name = m.pop("raw")
    raw_path = raw_dir / raw_name
    raw = json.loads(raw_path.read_text(encoding="utf-8"))
    strokes = raw.get("strokes", [])
    hints = m.get("stroke_hints") or []
    if len(hints) != len(strokes):
        m["stroke_hints"] = [f"第 {i+1} 笔" for i in range(len(strokes))]
    m["strokes"] = strokes
    return m
