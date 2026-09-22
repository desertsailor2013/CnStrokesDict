#!/usr/bin/env node
/**
 * 构建教材词语JSON文件
 * 合并词语数据，生成最终的 textbook_words.json
 */

const fs = require('fs');
const path = require('path');

// 路径配置
const SCRIPTS_DIR = __dirname;
const OUTPUT_DIR = path.join(SCRIPTS_DIR, "..", "app", "src", "main", "assets", "dictionary");
const DATA_DIR = path.join(SCRIPTS_DIR, "dict_data");

// 确保目录存在
[OUTPUT_DIR, DATA_DIR].forEach(dir => {
    if (!fs.existsSync(dir)) {
        fs.mkdirSync(dir, { recursive: true });
    }
});

/**
 * 加载词语数据
 */
function loadWords() {
    const { getAllWords, getGradeStats, TEXTBOOK_WORDS } = require('./textbook_words');
    return {
        words: getAllWords(),
        stats: getGradeStats(),
        raw: TEXTBOOK_WORDS,
    };
}

/**
 * 保存JSON文件
 */
function saveJson(data, filePath) {
    fs.writeFileSync(filePath, JSON.stringify(data, null, 2), 'utf-8');
    console.log(`已保存: ${filePath}`);
}

/**
 * 构建词语JSON
 */
function buildWordsJson() {
    console.log("加载词语数据...");
    const { words, stats, raw } = loadWords();
    console.log(`  词语数量: ${words.length}`);

    // 构建输出数据
    const output = {
        version: "1.0",
        description: "部编版小学1-6年级教材词语表",
        stats: stats,
        words: words,
        textbook_words: raw,
    };

    return output;
}

/**
 * 主函数
 */
function main() {
    console.log("=".repeat(50));
    console.log("构建教材词语库");
    console.log("=".repeat(50));

    // 构建数据
    const data = buildWordsJson();
    const words = data.words;

    // 统计
    const twoCharWords = words.filter(w => w.word.length === 2).length;
    const threeCharWords = words.filter(w => w.word.length === 3).length;
    const otherWords = words.filter(w => w.word.length > 3).length;

    console.log("\n统计:");
    console.log(`  总词语数: ${words.length}`);
    console.log(`  二字词语: ${twoCharWords}`);
    console.log(`  三字词语: ${threeCharWords}`);
    console.log(`  其他: ${otherWords}`);

    // 保存到多个位置
    const outputFiles = [
        path.join(OUTPUT_DIR, "textbook_words.json"),
        path.join(DATA_DIR, "textbook_words_full.json"),
    ];

    for (const outputFile of outputFiles) {
        saveJson(data, outputFile);
    }

    console.log("\n" + "=".repeat(50));
    console.log("构建完成!");
    console.log("=".repeat(50));
    console.log("\n输出文件:");
    outputFiles.forEach(f => console.log(`  - ${f}`));
}

// 运行
main();
