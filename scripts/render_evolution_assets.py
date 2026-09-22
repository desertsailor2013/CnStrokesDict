# -*- coding: utf-8 -*-
"""在本地生成 app/src/main/assets/evolution/*.png（完全离线，不访问网络）。
古文字阶段为楷体大字 + 说明条，便于在无拓片资源时仍展示结构；非考古原拓。
楷书阶段为通用字形示意。需安装 Pillow：pip install Pillow
"""
from __future__ import annotations

from pathlib import Path

from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[1]
OUT_DIR = ROOT / "app" / "src" / "main" / "assets" / "evolution"

FONT_CANDIDATES = [
    Path(r"C:\Windows\Fonts\STKAITI.TTF"),
    Path(r"C:\Windows\Fonts\simkai.ttf"),
]


def _load_font(size: int) -> ImageFont.FreeTypeFont | ImageFont.ImageFont:
    for p in FONT_CANDIDATES:
        if p.exists():
            return ImageFont.truetype(str(p), size)
    return ImageFont.load_default()


def _render(path: Path, title: str, glyph: str, big_pt: int = 200) -> None:
    w, h = 900, 380
    img = Image.new("RGB", (w, h), (250, 248, 242))
    draw = ImageDraw.Draw(img)
    font_title = _load_font(26)
    font_big = _load_font(big_pt)
    draw.rectangle((0, 0, w, h), outline=(200, 195, 185), width=2)
    draw.text((24, 16), title, fill=(80, 75, 70), font=font_title)
    bbox = draw.textbbox((0, 0), glyph, font=font_big)
    gw, gh = bbox[2] - bbox[0], bbox[3] - bbox[1]
    cx, cy = (w - gw) // 2, (h - gh) // 2 + 10
    draw.text((cx, cy), glyph, fill=(20, 20, 20), font=font_big)
    path.parent.mkdir(parents=True, exist_ok=True)
    img.save(path, format="PNG", optimize=True)


def main() -> None:
    specs: list[tuple[str, str, str]] = [
        ("hao_oracle.png", "甲骨文 / 金文（字形示意）", "好"),
        ("hao_bw.png", "小篆 → 隶 → 楷（通用字形示意）", "好"),
        ("xue_oracle.png", "甲骨文 / 金文（繁体「學」字形示意）", "學"),
        ("xue_bw.png", "楷书 · 简体「学」", "学"),
        ("zi_bronze.png", "金文（字形示意）", "字"),
        ("zi_bw.png", "楷书通用字形示意", "字"),
    ]
    for filename, title, glyph in specs:
        _render(OUT_DIR / filename, title, glyph)
        print("wrote", OUT_DIR / filename)


if __name__ == "__main__":
    main()
