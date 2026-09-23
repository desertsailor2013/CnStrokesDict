#!/usr/bin/env node
/**
 * 合并成语数据
 * 将现有成语和新增成语合并为一个完整的数据集
 */

const fs = require("fs");
const path = require("path");
const { IDIOMS: originalIdioms } = require("./idioms");
const additionalIdioms = require("./additional_idioms");

// 合并成语数据
const mergedIdioms = { ...originalIdioms };

// 添加新分类
for (const [category, idioms] of Object.entries(additionalIdioms)) {
    if (mergedIdioms[category]) {
        // 如果分类已存在，追加成语
        mergedIdioms[category] = [...mergedIdioms[category], ...idioms];
    } else {
        // 如果分类不存在，创建新分类
        mergedIdioms[category] = idioms;
    }
}

// 获取所有成语
function getAllIdioms() {
    const allIdioms = [];
    for (const [category, idioms] of Object.entries(mergedIdioms)) {
        for (const idiom of idioms) {
            allIdioms.push({ ...idiom, category });
        }
    }
    return allIdioms;
}

// 获取成语统计
function getIdiomStats() {
    const stats = {};
    for (const [category, idioms] of Object.entries(mergedIdioms)) {
        stats[category] = idioms.length;
    }
    stats.total = getAllIdioms().length;
    return stats;
}

// 构建JSON数据
function buildIdiomDictionary() {
    console.log("开始合并成语数据...");

    const allIdioms = getAllIdioms();
    console.log(`共 ${allIdioms.length} 条成语`);

    // 按拼音排序
    allIdioms.sort((a, b) => a.pinyin.localeCompare(b.pinyin));

    // 生成JSON数据
    const jsonData = {
        version: "1.0",
        description: "成语词典",
        totalCount: allIdioms.length,
        categories: Object.keys(mergedIdioms).map((cat) => ({
            name: cat,
            label: {
                animal: "动物成语",
                number: "数字成语",
                color: "颜色成语",
                body: "人体成语",
                nature: "自然成语",
                study: "学习成语",
                friendship: "友情成语",
                success: "成功成语",
                diligence: "勤奋成语",
                wisdom: "智慧成语",
            }[cat],
            count: mergedIdioms[cat].length,
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

// 运行合并
if (require.main === module) {
    buildIdiomDictionary();
}

module.exports = { buildIdiomDictionary, mergedIdioms, getAllIdioms, getIdiomStats };
