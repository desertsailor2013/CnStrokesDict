import json

with open('scripts/vendor/graphics.txt', encoding='utf-8') as f:
    for i, line in enumerate(f):
        if i >= 5:
            break
        line = line.strip()
        if line:
            try:
                data = json.loads(line)
                print(f"Line {i+1}: char='{data.get('character', '')}', strokes={len(data.get('strokes', []))}")
            except json.JSONDecodeError as e:
                print(f"Line {i+1}: JSON error: {e}")
                print(f"Content: {line[:100]}")
