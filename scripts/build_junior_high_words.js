#!/usr/bin/env node
/**
 * 构建初中词语JSON文件
 * 合并小学和初中词语数据，生成最终的 textbook_words.json
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
 * 加载小学词语数据
 */
function loadPrimarySchoolWords() {
    const primaryPath = path.join(DATA_DIR, "textbook_words_full.json");
    if (!fs.existsSync(primaryPath)) {
        console.log("未找到小学词语数据，跳过");
        return [];
    }
    const data = JSON.parse(fs.readFileSync(primaryPath, 'utf-8'));
    return data.flat_words || [];
}

/**
 * 加载初中词语数据
 */
function loadJuniorHighWords() {
    const { getAllWords } = require('./junior_high_words');
    return getAllWords();
}

/**
 * 保存JSON文件
 */
function saveJson(data, filePath) {
    fs.writeFileSync(filePath, JSON.stringify(data, null, 2), 'utf-8');
    console.log(`已保存: ${filePath}`);
}

/**
 * 主函数
 */
function main() {
    console.log("=".repeat(50));
    console.log("构建初中词语库");
    console.log("=".repeat(50));

    // 加载小学词语
    const primaryWords = loadPrimarySchoolWords();
    console.log(`小学词语: ${primaryWords.length}词`);

    // 加载初中词语
    const juniorHighWords = loadJuniorHighWords();
    console.log(`初中词语: ${juniorHighWords.length}词`);

    // 合并所有词语
    const allWords = [...primaryWords, ...juniorHighWords];
    console.log(`总计: ${allWords.length}词`);

    // 统计
    const twoCharWords = allWords.filter(w => w.word.length === 2).length;
    const threeCharWords = allWords.filter(w => w.word.length === 3).length;
    const otherWords = allWords.filter(w => w.word.length > 3).length;

    console.log("\n统计:");
    console.log(`  总词语数: ${allWords.length}`);
    console.log(`  二字词语: ${twoCharWords}`);
    console.log(`  三字词语: ${threeCharWords}`);
    console.log(`  其他: ${otherWords}`);

    // 构建输出数据
    const output = {
        version: "1.1",
        description: "部编版小学+初中教材词语表",
        stats: {
            primary_school: primaryWords.length,
            junior_high: juniorHighWords.length,
            total: allWords.length,
        },
        words: allWords,
    };

    // 保存到多个位置
    const outputFiles = [
        path.join(OUTPUT_DIR, "textbook_words.json"),
        path.join(DATA_DIR, "textbook_words_full.json"),
    ];

    for (const outputFile of outputFiles) {
        saveJson(output, outputFile);
    }

    console.log("\n" + "=".repeat(50));
    console.log("构建完成!");
    console.log("=".repeat(50));
    console.log("\n输出文件:");
    outputFiles.forEach(f => console.log(`  - ${f}`));
}

// 运行
main();
