#!/usr/bin/env node
/**
 * 合并小学和初中词语数据
 */

const fs = require('fs');
const path = require('path');

const SCRIPTS_DIR = __dirname;
const OUTPUT_DIR = path.join(SCRIPTS_DIR, "..", "app", "src", "main", "assets", "dictionary");
const DATA_DIR = path.join(SCRIPTS_DIR, "dict_data");

// 加载小学词语 - 使用原始数据源
const primaryWordsPath = path.join(SCRIPTS_DIR, "textbook_words.js");
const { getAllWords: getPrimaryWords } = require(primaryWordsPath);
const primaryWords = getPrimaryWords();

// 加载初中词语
const { getAllWords: getJuniorHighWords } = require('./junior_high_words');
const juniorHighWords = getJuniorHighWords();

// 合并
const allWords = [...primaryWords, ...juniorHighWords];

console.log(`小学词语: ${primaryWords.length}词`);
console.log(`初中词语: ${juniorHighWords.length}词`);
console.log(`总计: ${allWords.length}词`);

// 构建输出
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

// 保存
const outputFiles = [
    path.join(OUTPUT_DIR, "textbook_words.json"),
    path.join(DATA_DIR, "textbook_words_full.json"),
];

for (const outputFile of outputFiles) {
    fs.writeFileSync(outputFile, JSON.stringify(output, null, 2), 'utf-8');
    console.log(`已保存: ${outputFile}`);
}
