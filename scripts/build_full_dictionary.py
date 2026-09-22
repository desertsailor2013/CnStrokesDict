# -*- coding: utf-8 -*-
"""
构建完整的单字JSON文件
合并笔画数据和释义数据，生成最终的 characters.json
"""
import json
from pathlib import Path

# 路径配置
SCRIPTS_DIR = Path(__file__).parent
RAW_DIR = SCRIPTS_DIR / "hanzi_raw"
DATA_DIR = SCRIPTS_DIR / "dict_data"
OUTPUT_DIR = Path(__file__).parent.parent / "app" / "src" / "main" / "assets" / "dictionary"
OUTPUT_DIR.mkdir(parents=True, exist_ok=True)


def load_json(file_path: Path) -> dict | list | None:
    """加载JSON文件"""
    if not file_path.exists():
        return None
    try:
        with open(file_path, "r", encoding="utf-8") as f:
            return json.load(f)
    except Exception as e:
        print(f"加载失败: {file_path} - {e}")
        return None


def save_json(data: dict | list, file_path: Path) -> None:
    """保存JSON文件"""
    with open(file_path, "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=2)
    print(f"已保存: {file_path}")


def load_stroke_data() -> dict[str, dict]:
    """加载笔画数据"""
    stroke_data = {}
    
    # 从单个文件加载
    for f in RAW_DIR.glob("*.raw.json"):
        char = f.stem.replace(".raw", "")
        data = load_json(f)
        if data:
            stroke_data[char] = data
    
    # 从合并文件加载
    merged_file = RAW_DIR / "all_chars_raw.json"
    data = load_json(merged_file)
    if data:
        stroke_data.update(data)
    
    return stroke_data


def load_definitions() -> dict[str, dict]:
    """加载释义数据"""
    definitions = {}
    
    # 从基础释义文件加载
    base_file = DATA_DIR / "characters_base.json"
    data = load_json(base_file)
    if data and "characters" in data:
        for entry in data["characters"]:
            char = entry.get("char", "")
            if char:
                definitions[char] = entry
    
    return definitions


def load_primary_chars() -> list[str]:
    """加载生字清单"""
    import sys
    sys.path.insert(0, str(SCRIPTS_DIR))
    from primary_school_chars import get_all_chars
    return get_all_chars()


def merge_entry(char: str, stroke_data: dict, definitions: dict) -> dict:
    """合并单字条目数据"""
    # 基础条目
    entry = {
        "char": char,
        "pinyin": "",
        "radical": "",
        "shuowen": "",
        "pinyinWordExamples": [],
        "radicalStrokeCount": 0,
        "totalStrokeCount": 0,
        "definitions": [],
        "words": [],
        "idioms": [],
        "strokes": [],
        "evolution": [],
        "strokeHints": [],
        "source": "primary_school",
    }
    
    # 合并释义数据
    if char in definitions:
        def_data = definitions[char]
        entry["pinyin"] = def_data.get("pinyin", "")
        entry["radical"] = def_data.get("radical", "")
        entry["shuowen"] = def_data.get("shuowen", "")
        entry["radicalStrokeCount"] = def_data.get("radical_strokes", 0)
        entry["totalStrokeCount"] = def_data.get("total_strokes", 0)
        entry["definitions"] = def_data.get("definitions", [])
        entry["words"] = def_data.get("words", [])
        entry["idioms"] = def_data.get("idioms", [])
        entry["evolution"] = def_data.get("evolution", [])
        entry["strokeHints"] = def_data.get("stroke_hints", [])
    
    # 合并笔画数据
    if char in stroke_data:
        stroke_info = stroke_data[char]
        strokes = stroke_info.get("strokes", [])
        if strokes:
            entry["strokes"] = strokes
            entry["totalStrokeCount"] = len(strokes)
    
    # 添加拼音组词示例
    if entry["pinyin"] and entry["words"]:
        for word in entry["words"][:2]:
            entry["pinyinWordExamples"].append({
                "pinyin": entry["pinyin"],
                "word": word.get("word", ""),
            })
    
    return entry


def build_characters_json() -> dict:
    """构建完整的characters.json"""
    print("加载生字清单...")
    chars = load_primary_chars()
    print(f"  生字数量: {len(chars)}")
    
    print("加载笔画数据...")
    stroke_data = load_stroke_data()
    print(f"  笔画数据: {len(stroke_data)} 个")
    
    print("加载释义数据...")
    definitions = load_definitions()
    print(f"  释义数据: {len(definitions)} 个")
    
    print("构建条目...")
    entries = []
    for char in chars:
        entry = merge_entry(char, stroke_data, definitions)
        entries.append(entry)
    
    return {"characters": entries}


def main():
    """主函数"""
    print("=" * 50)
    print("构建 CnStrokesDict 字库")
    print("=" * 50)
    
    # 构建数据
    data = build_characters_json()
    entries = data["characters"]
    
    # 统计
    with_strokes = sum(1 for e in entries if e["strokes"])
    with_pinyin = sum(1 for e in entries if e["pinyin"])
    with_definitions = sum(1 for e in entries if e["definitions"])
    
    print(f"\n统计:")
    print(f"  总字数: {len(entries)}")
    print(f"  有笔画数据: {with_strokes}")
    print(f"  有拼音: {with_pinyin}")
    print(f"  有释义: {with_definitions}")
    
    # 保存到多个位置
    output_files = [
        OUTPUT_DIR / "characters.json",
        DATA_DIR / "characters_full.json",
    ]
    
    for output_file in output_files:
        save_json(data, output_file)
    
    print("\n" + "=" * 50)
    print("构建完成!")
    print("=" * 50)
    print(f"\n输出文件:")
    for f in output_files:
        print(f"  - {f}")


if __name__ == "__main__":
    main()
