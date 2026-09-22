# -*- coding: utf-8 -*-
"""
从百度百科词条 HTML 与 BaikeLemmaCardApi 提取「字形演进」配图 URL（bkimg.cdn.bcebos.com）。

用法（在项目根目录、已联网）：
  .venv\\Scripts\\python scripts/fetch_baike_evolution_urls.py
  .venv\\Scripts\\python scripts/fetch_baike_evolution_urls.py 好 学 字

说明：
- 第一阶段：在「字源演变 / 汉字源流 / 文字源流」等正文区块中取首张 bkimg 图（多为字形演变流程图）。
- 第二阶段：优先使用 BaikeLemmaCardApi 的词条主图；若与第一阶段相同或 API 歧义跳转，
  则取该区块内下一张不同的图。

版权：图片来自百度百科与百度 CDN，展示时请遵守百科用户协议与著作权说明。
"""
from __future__ import annotations

import html as html_module
import json
import re
import sys
import urllib.parse
import urllib.request

# 词条主 URL 覆盖：单字「字」默认跳转到「表字」，需用汉语文字义项页面
ITEM_URL_OVERRIDES: dict[str, str] = {
    "字": "https://baike.baidu.com/item/%E5%AD%97/1561105",
}

def _bkimg_urls_in_text(text: str) -> list[str]:
    """从引号内完整匹配 bkimg URL（百科正文里常为 &quot; 包裹）。"""
    text = html_module.unescape(text)
    return re.findall(
        r'"(https://bkimg\.cdn\.bcebos\.com/pic/[^"]+)"',
        text,
        flags=re.I,
    )

SECTION_TITLES = (
    "字源演变",
    "汉字源流",
    "文字源流",
)

HEADERS = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
    "(KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
    "Accept-Language": "zh-CN,zh;q=0.9",
}


def fetch_html(char: str) -> str:
    url = ITEM_URL_OVERRIDES.get(char)
    if not url:
        q = urllib.parse.quote(char, safe="")
        url = f"https://baike.baidu.com/item/{q}"
    req = urllib.request.Request(url, headers=HEADERS)
    with urllib.request.urlopen(req, timeout=45) as r:
        return r.read().decode("utf-8", errors="replace")


def fetch_lemma_card(char: str) -> dict:
    q = urllib.parse.quote(char, safe="")
    url = f"https://baike.baidu.com/api/openapi/BaikeLemmaCardApi?appid=379020&bk_key={q}"
    req = urllib.request.Request(url, headers=HEADERS)
    with urllib.request.urlopen(req, timeout=30) as r:
        return json.loads(r.read().decode("utf-8"))


def section_start(html: str, title: str) -> int:
    positions: list[int] = []
    start = 0
    while True:
        p = html.find(title, start)
        if p < 0:
            break
        positions.append(p)
        start = p + len(title)
    if not positions:
        return -1
    if len(positions) >= 2:
        return positions[1]
    return positions[0]


def prefer_display_url(url: str | None) -> str | None:
    """去掉百科页内嵌的缩略 resize 参数，改用 format,f_auto 便于客户端显示更清晰。"""
    if not url or "bkimg.cdn.bcebos.com" not in url:
        return url
    base = url.split("?")[0]
    return base + "?x-bce-process=image/format,f_auto"


def unique_ordered(urls: list[str]) -> list[str]:
    seen: set[str] = set()
    out: list[str] = []
    for u in urls:
        base = u.split("?")[0]
        if base in seen:
            continue
        seen.add(base)
        out.append(u)
    return out


def urls_in_etymology(html: str) -> list[str]:
    for title in SECTION_TITLES:
        pos = section_start(html, title)
        if pos < 0:
            continue
        chunk = html[pos : pos + 90000]
        found = unique_ordered(_bkimg_urls_in_text(chunk))
        if found:
            return found
    return []


def pick_pair(char: str) -> tuple[str | None, str | None]:
    html = fetch_html(char)
    et = urls_in_etymology(html)
    u1 = et[0] if et else None

    u2: str | None = None
    try:
        card = fetch_lemma_card(char)
        if card.get("redirect") and char in ITEM_URL_OVERRIDES:
            u2 = None
        else:
            u2 = card.get("image")
            if not (isinstance(u2, str) and u2.startswith("http")):
                u2 = None
    except OSError:
        u2 = None

    if u2 and u1:
        b1 = u1.split("?")[0]
        b2 = u2.split("?")[0]
        if b2 == b1 and len(et) > 1:
            u2 = et[1]
    elif u2 is None and len(et) > 1:
        u2 = et[1]
    elif u2 is None and len(et) == 1:
        u2 = et[0]

    return prefer_display_url(u1), prefer_display_url(u2)


def main(argv: list[str]) -> None:
    chars = argv[1:] if len(argv) > 1 else ["好", "学", "字"]
    for ch in chars:
        u1, u2 = pick_pair(ch)
        print(f"=== {ch} ===")
        print("  early:", u1 or "(none)")
        print("  late :", u2 or "(none)")


if __name__ == "__main__":
    main(sys.argv)
