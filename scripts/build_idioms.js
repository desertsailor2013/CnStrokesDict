#!/usr/bin/env node
/**
 * 构建成语词典JSON数据
 */

const fs = require("fs");
const path = require("path");
const { IDIOMS, getAllIdioms, getIdiomStats } = require("./idioms");

function buildIdiomDictionary() {
    console.log("开始构建成语词典JSON数据...");

    const allIdioms = getAllIdioms();
    console.log(`共 ${allIdioms.length} 条成语`);

    // 按拼音排序
    allIdioms.sort((a, b) => a.pinyin.localeCompare(b.pinyin));

    // 生成JSON数据
    const jsonData = {
        version: "1.0",
        description: "成语词典",
        totalCount: allIdioms.length,
        categories: Object.keys(IDIOMS).map((cat) => ({
            name: cat,
            label: {
                animal: "动物成语",
                number: "数字成语",
                color: "颜色成语",
                body: "人体成语",
                nature: "自然成语",
            }[cat],
            count: IDIOMS[cat].length,
        })),
        idioms: allIdioms,
        stats: getIdiomStats(),
    };

    // 保存到JSON文件
    const outputPath = path.join(__dirname, "../app/src/main/assets/dictionary/idioms.json");
    const outputDir = path.dirname(outputPath);

    if (!fs.existsSync(outputDir)) {
        fs.mkdirSync(outputDir, { recursive: true });
    }

    fs.writeFileSync(outputPath, JSON.stringify(jsonData, null, 2), "utf-8");
    console.log(`已保存到: ${outputPath}`);

    // 打印统计信息
    console.log("\n成语分类统计：");
    for (const [category, count] of Object.entries(jsonData.stats)) {
        console.log(`  ${category}: ${count}条`);
    }

    return jsonData;
}

if (require.main === module) {
    buildIdiomDictionary();
}

module.exports = { buildIdiomDictionary };
