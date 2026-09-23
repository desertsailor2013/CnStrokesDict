const fs = require('fs');
const path = require('path');

// 导入医学术语
const medicalTerms = require('./professional/medical_terms.js');

// 合并所有专业术语
const allTerms = [
  ...medicalTerms.map(term => ({ ...term, category: 'medical', difficulty: 1 })),
];

// 构建JSON数据
const jsonData = {
  version: "1.0.0",
  count: allTerms.length,
  categories: {
    medical: { name: "医学术语", count: medicalTerms.length },
  },
  terms: allTerms,
};

// 写入JSON文件
const outputPath = path.join(__dirname, '..', 'app', 'src', 'main', 'assets', 'professional_terms.json');
fs.writeFileSync(outputPath, JSON.stringify(jsonData, null, 2), 'utf-8');

console.log(`生成专业词库：${allTerms.length} 个术语`);
console.log(`输出路径：${outputPath}`);
