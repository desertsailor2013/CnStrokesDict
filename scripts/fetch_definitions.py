# -*- coding: utf-8 -*-
"""
批量获取汉字释义数据
从公开数据源获取拼音、部首、释义等信息
"""
import json
import time
import re
from pathlib import Path

# 输出目录
DATA_DIR = Path(__file__).parent / "dict_data"
DATA_DIR.mkdir(exist_ok=True)

# 常用部首表
RADICALS = {
    "一": "一", "丨": "丨", "丶": "丶", "丿": "丿", "乙": "乙", "亅": "亅",
    "二": "二", "亠": "亠", "人": "人", "亻": "人", "儿": "儿", "入": "入",
    "八": "八", "冂": "冂", "冖": "冖", "冫": "冫", "几": "几", "凵": "凵",
    "刀": "刀", "刂": "刀", "力": "力", "勹": "勹", "匕": "匕", "匚": "匚",
    "匸": "匸", "十": "十", "卜": "卜", "卩": "卩", "厂": "厂", "厶": "厶",
    "又": "又", "口": "口", "囗": "囗", "土": "土", "士": "士", "夂": "夂",
    "夊": "夊", "夕": "夕", "大": "大", "女": "女", "子": "子", "宀": "宀",
    "寸": "寸", "小": "小", "尢": "尢", "尸": "尸", "屮": "屮", "山": "山",
    "巛": "巛", "川": "川", "工": "工", "己": "己", "已": "已", "巳": "巳",
    "弓": "弓", "子": "子", "屮": "屮", "山": "山", "巛": "巛", "川": "川",
    "工": "工", "己": "己", "已": "已", "巳": "巳", "巾": "巾", "干": "干",
    "幺": "幺", "广": "广", "廴": "廴", "廾": "廾", "弋": "弋", "弓": "弓",
    "廾": "廾", "彐": "彐", "彡": "彡", "彳": "彳", "心": "心", "忄": "心",
    "戈": "戈", "户": "户", "手": "手", "扌": "手", "支": "支", "攴": "攴",
    "攵": "攴", "文": "文", "斗": "斗", "斤": "斤", "方": "方", "无": "无",
    "日": "日", "曰": "曰", "月": "月", "木": "木", "欠": "欠", "止": "止",
    "歹": "歹", "殳": "殳", "毋": "毋", "比": "比", "毛": "毛", "氏": "氏",
    "气": "气", "水": "水", "氵": "水", "火": "火", "灬": "火", "爪": "爪",
    "父": "父", "爻": "爻", "丬": "丬", "片": "片", "牙": "牙", "牛": "牛",
    "牜": "牛", "犬": "犬", "犭": "犬", "玄": "玄", "玉": "玉", "王": "玉",
    "瓜": "瓜", "瓦": "瓦", "甘": "甘", "生": "生", "用": "用", "田": "田",
    "疋": "疋", "疒": "疒", "癶": "癶", "白": "白", "皮": "皮", "皿": "皿",
    "目": "目", "矛": "矛", "矢": "矢", "石": "石", "示": "示", "礻": "示",
    "禸": "禸", "禾": "禾", "穴": "穴", "立": "立", "竹": "竹", "⺮": "竹",
    "米": "米", "糸": "糸", "纟": "糸", "缶": "缶", "网": "网", "罒": "网",
    "羊": "羊", "羽": "羽", "老": "老", "耂": "老", "而": "而", "耒": "耒",
    "耳": "耳", "聿": "聿", "肉": "肉", "月": "月", "臣": "臣", "自": "自",
    "至": "至", "臼": "臼", "舌": "舌", "舛": "舛", "舟": "舟", "艮": "艮",
    "色": "色", "艸": "艸", "艹": "艸", "虎": "虎", "虍": "虎", "虫": "虫",
    "血": "血", "行": "行", "衣": "衣", "衤": "衣", "襾": "襾", "西": "西",
    "见": "见", "角": "角", "言": "言", "讠": "言", "谷": "谷", "豆": "豆",
    "豕": "豕", "豸": "豸", "贝": "贝", "贝": "贝", "赤": "赤", "走": "走",
    "走": "走", "足": "足", "⻊": "足", "身": "身", "车": "车", "車": "车",
    "辛": "辛", "辰": "辰", "辵": "辵", "辶": "辶", "邑": "邑", "酉": "酉",
    "釆": "釆", "里": "里", "金": "金", "钅": "金", "长": "长", "長": "长",
    "门": "门", "門": "门", "阜": "阜", "阝": "阜", "隶": "隶", "隹": "隹",
    "雨": "雨", "青": "青", "非": "非", "面": "面", "革": "革", "韦": "韦",
    "韭": "韭", "音": "音", "页": "页", "風": "风", "飞": "飞", "食": "食",
    "饣": "食", "首": "首", "香": "香", "马": "马", "馬": "马", "骨": "骨",
    "高": "高", "髟": "髟", "斗": "斗", "鬯": "鬯", "鬲": "鬲", "鬼": "鬼",
    "鱼": "鱼", "魚": "鱼", "鸟": "鸟", "鳥": "鸟", "卤": "卤", "鹿": "鹿",
    "麦": "麦", "麻": "麻", "黄": "黄", "黍": "黍", "黑": "黑", "黽": "黽",
    "鼎": "鼎", "鼓": "鼓", "鼠": "鼠", "鼻": "鼻", "齐": "齐", "齒": "齿",
    "龙": "龙", "龍": "龙", "龟": "龟",
}


def get_radical(char: str) -> str:
    """根据汉字获取部首（简化版，基于常见部首表）"""
    # 这里使用简化的匹配逻辑
    # 实际应该使用更完整的部首数据库
    for radical in RADICALS:
        if radical in char:
            return radical
    return ""


def get_stroke_count(strokes: list) -> int:
    """根据笔画数据获取笔画数"""
    return len(strokes) if strokes else 0


def build_char_entry(char: str, stroke_data: dict = None) -> dict:
    """
    构建单字条目数据
    
    这是一个基础模板，实际数据需要从更完整的数据源获取
    """
    strokes = stroke_data.get("strokes", []) if stroke_data else []
    radical_stroke_indices = stroke_data.get("radStrokes", []) if stroke_data else []
    
    return {
        "char": char,
        "pinyin": "",  # 需要从数据源获取
        "radical": get_radical(char),
        "radical_strokes": len(radical_stroke_indices),
        "total_strokes": get_stroke_count(strokes),
        "shuowen": "",  # 需要从数据源获取
        "definitions": [],  # 需要从数据源获取
        "strokes": strokes,
        "stroke_hints": [],  # 需要从数据源获取
        "evolution": [],  # 需要从数据源获取
        "pinyin_word_examples": [],  # 需要从数据源获取
        "idioms": [],  # 需要从数据源获取
        "source": "",  # 来源标记
    }


def load_stroke_data() -> dict[str, dict]:
    """加载笔画数据"""
    raw_dir = Path(__file__).parent / "hanzi_raw"
    all_strokes = {}
    
    # 加载单个字的笔画数据
    for f in raw_dir.glob("*.raw.json"):
        try:
            with open(f, "r", encoding="utf-8") as fp:
                data = json.load(fp)
                char = f.stem.replace(".raw", "")
                all_strokes[char] = data
        except Exception as e:
            print(f"加载失败: {f} - {e}")
    
    # 加载合并的笔画数据
    merged_file = raw_dir / "all_chars_raw.json"
    if merged_file.exists():
        try:
            with open(merged_file, "r", encoding="utf-8") as f:
                data = json.load(f)
                all_strokes.update(data)
        except Exception as e:
            print(f"加载合并数据失败: {e}")
    
    return all_strokes


def build_all_entries(chars: list[str], stroke_data: dict) -> list[dict]:
    """构建所有字的条目数据"""
    entries = []
    for char in chars:
        entry = build_char_entry(char, stroke_data.get(char))
        entries.append(entry)
    return entries


def save_entries(entries: list[dict], output_file: Path) -> None:
    """保存条目数据到JSON文件"""
    with open(output_file, "w", encoding="utf-8") as f:
        json.dump({"characters": entries}, f, ensure_ascii=False, indent=2)
    print(f"已保存: {output_file}")
    print(f"字数: {len(entries)}")


if __name__ == "__main__":
    import sys
    sys.path.insert(0, str(Path(__file__).parent))
    from primary_school_chars import get_all_chars, get_grade_stats
    
    print("=" * 50)
    print("构建小学1-6年级生字数据")
    print("=" * 50)
    
    # 获取所有生字
    chars = get_all_chars()
    print(f"生字数量: {len(chars)}")
    
    # 加载笔画数据
    print("\n加载笔画数据...")
    stroke_data = load_stroke_data()
    print(f"已有笔画数据: {len(stroke_data)} 个")
    
    # 构建条目
    print("\n构建字条目...")
    entries = build_all_entries(chars, stroke_data)
    
    # 保存到脚本目录
    output_file = DATA_DIR / "characters_base.json"
    save_entries(entries, output_file)
    
    # 统计
    print("\n" + "=" * 50)
    grade_stats = get_grade_stats()
    for grade, count in grade_stats.items():
        print(f"  {grade}: {count}字")
    print("=" * 50)
    print("\n注意：")
    print("  1. 拼音、释义等字段需要从更完整的数据源补充")
    print("  2. 笔画数据需要先运行 fetch_hanzi_data.py 获取")
    print("  3. 最终数据需要合并到 app/src/main/assets/dictionary/characters.json")
