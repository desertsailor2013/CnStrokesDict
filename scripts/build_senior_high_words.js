#!/usr/bin/env node
/**
 * 构建高中词语JSON数据
 * 将senior_high_words.js中的词语数据转换为JSON格式
 */

const fs = require("fs");
const path = require("path");
const { SENIOR_HIGH_WORDS, getAllWords, getGradeStats } = require("./senior_high_words");

/**
 * 构建词语JSON数据
 */
function buildSeniorHighWords() {
    console.log("开始构建高中词语JSON数据...");

    // 获取所有词语
    const allWords = getAllWords();
    console.log(`共 ${allWords.length} 个词语`);

    // 按年级分组
    const wordsByGrade = {};
    for (const word of allWords) {
        const gradeKey = `grade_${word.grade}`;
        if (!wordsByGrade[gradeKey]) {
            wordsByGrade[gradeKey] = [];
        }
        wordsByGrade[gradeKey].push(word);
    }

    // 生成JSON数据
    const jsonData = {
        version: "1.0",
        description: "部编版高中10-12年级教材词语",
        totalCount: allWords.length,
        grades: Object.keys(SENIOR_HIGH_WORDS).map((gradeKey) => {
            const gradeNum = parseInt(gradeKey.replace("grade_", ""));
            const gradeWords = wordsByGrade[gradeKey] || [];
            return {
                grade: gradeNum,
                gradeName: `高${gradeNum === 10 ? "一" : gradeNum === 11 ? "二" : "三"}`,
                wordCount: gradeWords.length,
                words: gradeWords.map((w) => ({
                    word: w.word,
                    pinyin: w.pinyin,
                    meaning: w.meaning,
                    lesson: w.lesson,
                    lessonTitle: w.lessonTitle,
                    semester: w.semester,
                })),
            };
        }),
        stats: getGradeStats(),
    };

    // 保存到JSON文件
    const outputPath = path.join(__dirname, "../app/src/main/assets/dictionary/senior_high_words.json");
    const outputDir = path.dirname(outputPath);

    if (!fs.existsSync(outputDir)) {
        fs.mkdirSync(outputDir, { recursive: true });
    }

    fs.writeFileSync(outputPath, JSON.stringify(jsonData, null, 2), "utf-8");
    console.log(`已保存到: ${outputPath}`);

    // 打印统计信息
    console.log("\n高中词语统计：");
    for (const [grade, count] of Object.entries(jsonData.stats)) {
        console.log(`  ${grade}: ${count}词`);
    }

    return jsonData;
}

// 运行构建
if (require.main === module) {
    buildSeniorHighWords();
}

module.exports = { buildSeniorHighWords };
