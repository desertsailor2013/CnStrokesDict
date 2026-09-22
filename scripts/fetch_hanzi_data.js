#!/usr/bin/env node
/**
 * 批量获取汉字笔画SVG数据
 * 从 Hanzi Writer CDN 获取每个字的笔画路径数据
 */

const https = require('https');
const fs = require('fs');
const path = require('path');

// Hanzi Writer 数据源
const HANZI_WRITER_CDN = "https://cdn.jsdelivr.net/npm/hanzi-writer-data@2.0";
const RAW_DIR = path.join(__dirname, "hanzi_raw");

// 确保目录存在
if (!fs.existsSync(RAW_DIR)) {
    fs.mkdirSync(RAW_DIR, { recursive: true });
}

// 请求配置
const MAX_RETRIES = 3;
const RETRY_DELAY = 1000;
const REQUEST_DELAY = 100;

/**
 * 从 Hanzi Writer CDN 获取单个汉字的笔画数据
 */
async function fetchCharData(char) {
    const url = `${HANZI_WRITER_CDN}/${char}.json`;
    
    for (let attempt = 0; attempt < MAX_RETRIES; attempt++) {
        try {
            const data = await new Promise((resolve, reject) => {
                https.get(url, (res) => {
                    let data = '';
                    res.on('data', (chunk) => data += chunk);
                    res.on('end', () => {
                        if (res.statusCode === 200) {
                            try {
                                resolve(JSON.parse(data));
                            } catch (e) {
                                reject(new Error(`JSON解析失败: ${e.message}`));
                            }
                        } else if (res.statusCode === 404) {
                            resolve(null);
                        } else {
                            reject(new Error(`HTTP ${res.statusCode}`));
                        }
                    });
                }).on('error', reject);
            });
            
            return data;
        } catch (error) {
            console.log(`  [重试 ${attempt + 1}] ${char} - ${error.message}`);
            if (attempt < MAX_RETRIES - 1) {
                await sleep(RETRY_DELAY);
            }
        }
    }
    
    console.log(`  [失败] ${char} - 超过最大重试次数`);
    return null;
}

/**
 * 延迟函数
 */
function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

/**
 * 批量获取汉字笔画数据
 */
async function fetchBatch(chars) {
    const results = {};
    const total = chars.length;
    
    for (let i = 0; i < total; i++) {
        const char = chars[i];
        process.stdout.write(`[${i + 1}/${total}] 获取: ${char}`);
        
        // 检查是否已缓存
        const cacheFile = path.join(RAW_DIR, `${char}.raw.json`);
        if (fs.existsSync(cacheFile)) {
            try {
                const data = JSON.parse(fs.readFileSync(cacheFile, 'utf-8'));
                results[char] = data;
                console.log(" (缓存)");
                continue;
            } catch (e) {
                // 缓存损坏，重新获取
            }
        }
        
        const data = await fetchCharData(char);
        if (data) {
            results[char] = data;
            // 保存到缓存
            fs.writeFileSync(cacheFile, JSON.stringify(data, null, 2), 'utf-8');
            console.log(" ✓");
        } else {
            console.log(" ✗");
        }
        
        // 请求间隔
        if (i < total - 1) {
            await sleep(REQUEST_DELAY);
        }
    }
    
    return results;
}

/**
 * 加载已有的笔画数据
 */
function loadExistingData() {
    const results = {};
    if (fs.existsSync(RAW_DIR)) {
        const files = fs.readdirSync(RAW_DIR).filter(f => f.endsWith('.raw.json'));
        for (const file of files) {
            try {
                const data = JSON.parse(fs.readFileSync(path.join(RAW_DIR, file), 'utf-8'));
                const char = file.replace('.raw.json', '');
                results[char] = data;
            } catch (e) {
                // 忽略损坏的文件
            }
        }
    }
    return results;
}

/**
 * 保存合并后的数据到单个文件
 */
function saveMergedData(data) {
    const outputFile = path.join(RAW_DIR, "all_chars_raw.json");
    fs.writeFileSync(outputFile, JSON.stringify(data, null, 2), 'utf-8');
    console.log(`\n已保存到: ${outputFile}`);
    console.log(`总计: ${Object.keys(data).length} 个汉字`);
}

/**
 * 主函数
 */
async function main() {
    console.log("=".repeat(50));
    console.log("批量获取汉字笔画数据");
    console.log("=".repeat(50));
    
    // 导入生字清单
    const { getAllChars, getGradeStats } = require('./primary_school_chars');
    
    // 获取所有生字
    const chars = getAllChars();
    console.log(`小学1-6年级生字: ${chars.length} 个`);
    console.log("-".repeat(40));
    
    // 加载已有数据
    const existing = loadExistingData();
    console.log(`已有数据: ${Object.keys(existing).length} 个`);
    console.log("-".repeat(40));
    
    // 计算需要获取的字
    const toFetch = chars.filter(c => !existing[c]);
    console.log(`需要获取: ${toFetch.length} 个`);
    console.log("-".repeat(40));
    
    let allData;
    if (toFetch.length > 0) {
        // 批量获取
        const newData = await fetchBatch(toFetch);
        
        // 合并数据
        allData = { ...existing, ...newData };
    } else {
        allData = existing;
    }
    
    // 保存合并数据
    saveMergedData(allData);
    
    // 统计
    console.log("-".repeat(40));
    const gradeStats = getGradeStats();
    for (const [grade, count] of Object.entries(gradeStats)) {
        console.log(`  ${grade}: ${count}字`);
    }
}

// 运行
main().catch(console.error);
