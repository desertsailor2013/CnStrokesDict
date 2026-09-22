# -*- coding: utf-8 -*-
"""
批量获取汉字笔画SVG数据
从 Hanzi Writer CDN 获取每个字的笔画路径数据
"""
import json
import time
import requests
from pathlib import Path

# Hanzi Writer 数据源
HANZI_WRITER_CDN = "https://cdn.jsdelivr.net/npm/hanzi-writer-data@2.0"
RAW_DIR = Path(__file__).parent / "hanzi_raw"
RAW_DIR.mkdir(exist_ok=True)

# 请求头
HEADERS = {
    "User-Agent": "CnStrokesDict/1.0 (Educational App)"
}

# 重试配置
MAX_RETRIES = 3
RETRY_DELAY = 1.0


def fetch_char_data(char: str) -> dict | None:
    """
    从 Hanzi Writer CDN 获取单个汉字的笔画数据
    
    返回格式：
    {
        "char": "好",
        "strokes": ["M 123.45 67.89...", ...],  # SVG路径数据
        "medians": [[...], ...],  # 笔画中心线
        "radStrokes": [0, 1, 2],  # 部首笔画索引
        ...
    }
    """
    url = f"{HANZI_WRITER_CDN}/{char}.json"
    
    for attempt in range(MAX_RETRIES):
        try:
            resp = requests.get(url, headers=HEADERS, timeout=10)
            if resp.status_code == 200:
                return resp.json()
            elif resp.status_code == 404:
                print(f"  [跳过] {char} - 数据不存在")
                return None
            else:
                print(f"  [重试] {char} - HTTP {resp.status_code}")
                time.sleep(RETRY_DELAY)
        except requests.RequestException as e:
            print(f"  [重试] {char} - {e}")
            time.sleep(RETRY_DELAY)
    
    print(f"  [失败] {char} - 超过最大重试次数")
    return None


def fetch_batch(chars: list[str], delay: float = 0.1) -> dict[str, dict]:
    """
    批量获取汉字笔画数据
    
    参数：
        chars: 汉字列表
        delay: 请求间隔（秒），避免被封禁
    
    返回：
        {char: data_dict, ...}
    """
    results = {}
    total = len(chars)
    
    for i, char in enumerate(chars, 1):
        print(f"[{i}/{total}] 获取: {char}", end="")
        
        # 检查是否已缓存
        cache_file = RAW_DIR / f"{char}.raw.json"
        if cache_file.exists():
            try:
                with open(cache_file, "r", encoding="utf-8") as f:
                    results[char] = json.load(f)
                print(" (缓存)")
                continue
            except:
                pass
        
        data = fetch_char_data(char)
        if data:
            results[char] = data
            # 保存到缓存
            with open(cache_file, "w", encoding="utf-8") as f:
                json.dump(data, f, ensure_ascii=False, indent=2)
            print(" ✓")
        else:
            print(" ✗")
        
        # 请求间隔
        if i < total:
            time.sleep(delay)
    
    return results


def load_existing_data() -> dict[str, dict]:
    """加载已有的笔画数据"""
    results = {}
    if RAW_DIR.exists():
        for f in RAW_DIR.glob("*.raw.json"):
            try:
                with open(f, "r", encoding="utf-8") as fp:
                    data = json.load(fp)
                    char = f.stem.replace(".raw", "")
                    results[char] = data
            except:
                pass
    return results


def merge_data(existing: dict, new_data: dict) -> dict:
    """合并已有数据和新数据"""
    merged = existing.copy()
    merged.update(new_data)
    return merged


def save_merged_data(data: dict) -> None:
    """保存合并后的数据到单个文件"""
    output_file = RAW_DIR / "all_chars_raw.json"
    with open(output_file, "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=2)
    print(f"\n已保存到: {output_file}")
    print(f"总计: {len(data)} 个汉字")


if __name__ == "__main__":
    import sys
    sys.path.insert(0, str(Path(__file__).parent))
    from primary_school_chars import get_all_chars, get_grade_stats
    
    # 获取所有生字
    chars = get_all_chars()
    print(f"小学1-6年级生字: {len(chars)} 个")
    print("-" * 40)
    
    # 加载已有数据
    existing = load_existing_data()
    print(f"已有数据: {len(existing)} 个")
    print("-" * 40)
    
    # 计算需要获取的字
    to_fetch = [c for c in chars if c not in existing]
    print(f"需要获取: {len(to_fetch)} 个")
    print("-" * 40)
    
    if to_fetch:
        # 批量获取
        new_data = fetch_batch(to_fetch)
        
        # 合并数据
        all_data = merge_data(existing, new_data)
    else:
        all_data = existing
    
    # 保存合并数据
    save_merged_data(all_data)
    
    # 统计
    print("-" * 40)
    grade_stats = get_grade_stats()
    for grade, count in grade_stats.items():
        print(f"  {grade}: {count}字")
