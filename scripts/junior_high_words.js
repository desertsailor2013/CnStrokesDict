#!/usr/bin/env node
/**
 * 部编版初中7-9年级教材词语表
 * 基于部编版语文教材整理，包含每课的二字/三字词语
 */

const JUNIOR_HIGH_WORDS = {
    // ==================== 七年级 ====================
    grade_7: {
        semester_1: [
            // 第1课 春
            { lesson: 1, title: "春", words: [
                { word: "朗润", pinyin: "lǎng rùn", meaning: "明亮润泽" },
                { word: "酝酿", pinyin: "yùn niàng", meaning: "造酒的发酵过程" },
                { word: "卖弄", pinyin: "mài nòng", meaning: "炫耀" },
                { word: "宛转", pinyin: "wǎn zhuǎn", meaning: "委婉" },
                { word: "嘹亮", pinyin: "liáo liàng", meaning: "声音清脆响亮" },
                { word: "黄晕", pinyin: "huáng yùn", meaning: "昏黄的光圈" },
                { word: "烘托", pinyin: "hōng tuō", meaning: "衬托" },
                { word: "舒活", pinyin: "shū huó", meaning: "舒展活动" },
                { word: "欣欣然", pinyin: "xīn xīn rán", meaning: "高兴的样子" },
                { word: "繁花嫩叶", pinyin: "fán huā nèn yè", meaning: "繁茂的花和嫩叶" },
            ]},
            // 第2课 济南的冬天
            { lesson: 2, title: "济南的冬天", words: [
                { word: "镶", pinyin: "xiāng", meaning: "把物体嵌入另一物体" },
                { word: "温晴", pinyin: "wēn qíng", meaning: "温暖晴朗" },
                { word: "安适", pinyin: "ān shì", meaning: "安静舒适" },
                { word: "慈善", pinyin: "cí shàn", meaning: "仁慈善良" },
                { word: "秀气", pinyin: "xiù qi", meaning: "清秀" },
                { word: "贮蓄", pinyin: "zhù xù", meaning: "储存" },
                { word: "澄清", pinyin: "chéng qīng", meaning: "清澈明亮" },
                { word: "空灵", pinyin: "kōng líng", meaning: "清净透明" },
            ]},
            // 第3课 雨的四季
            { lesson: 3, title: "雨的四季", words: [
                { word: "花苞", pinyin: "huā bāo", meaning: "花蕾" },
                { word: "娇媚", pinyin: "jiāo mèi", meaning: "妩媚" },
                { word: "棱镜", pinyin: "léng jìng", meaning: "透明体" },
                { word: "粗犷", pinyin: "cū guǎng", meaning: "粗野豪放" },
                { word: "睫毛", pinyin: "jié máo", meaning: "眼睑边缘的毛" },
                { word: "衣裳", pinyin: "yī shang", meaning: "衣服" },
                { word: "铃铛", pinyin: "líng dang", meaning: "铃" },
                { word: "端庄", pinyin: "duān zhuāng", meaning: "端正庄重" },
                { word: "静谧", pinyin: "jìng mì", meaning: "安静" },
                { word: "莅临", pinyin: "lì lín", meaning: "来到" },
                { word: "粗暴", pinyin: "cū bào", meaning: "粗野暴躁" },
            ]},
        ],
        semester_2: [
            // 第1课 邓稼先
            { lesson: 1, title: "邓稼先", words: [
                { word: "元勋", pinyin: "yuán xūn", meaning: "有极大功绩的人" },
                { word: "奠基", pinyin: "diàn jī", meaning: "打下基础" },
                { word: "选聘", pinyin: "xuǎn pìn", meaning: "挑选聘用" },
                { word: "谣言", pinyin: "yáo yán", meaning: "没有事实根据的话" },
                { word: "背诵", pinyin: "bèi sòng", meaning: "凭记忆念出" },
                { word: "昼夜", pinyin: "zhòu yè", meaning: "白天和黑夜" },
                { word: "昆仑", pinyin: "kūn lún", meaning: "山名" },
                { word: "挚友", pinyin: "zhì yǒu", meaning: "亲密的朋友" },
                { word: "可歌可泣", pinyin: "kě gěi kě qì", meaning: "值得歌颂使人感动得流泪" },
                { word: "鲜为人知", pinyin: "xiǎn wéi rén zhī", meaning: "很少有人知道" },
                { word: "鞠躬尽瘁", pinyin: "jū gōng jìn cuì", meaning: "小心谨慎贡献全部力量" },
            ]},
            // 第2课 说和做
            { lesson: 2, title: "说和做", words: [
                { word: "梳头", pinyin: "shū tóu", meaning: "用梳子整理头发" },
                { word: "抱歉", pinyin: "bào qiàn", meaning: "心中不安" },
                { word: "秩序", pinyin: "zhì xù", meaning: "有条理" },
                { word: "深宵", pinyin: "shēn xiāo", meaning: "深夜" },
                { word: "伴侣", pinyin: "bàn lǚ", meaning: "同伴" },
                { word: "小楷", pinyin: "xiǎo kǎi", meaning: "小的楷书" },
                { word: "硕果", pinyin: "shuò guǒ", meaning: "巨大的成绩" },
                { word: "卓越", pinyin: "zhuó yuè", meaning: "非常优秀" },
                { word: "沥尽心血", pinyin: "lì jìn xīn xuè", meaning: "用尽心思" },
                { word: "心不在焉", pinyin: "xīn bù zài yān", meaning: "心思不在这里" },
                { word: "慷概淋漓", pinyin: "kāng kǎi lín lí", meaning: "情绪激动充满正气" },
            ]},
        ],
    },

    // ==================== 八年级 ====================
    grade_8: {
        semester_1: [
            // 第1课 消息二则
            { lesson: 1, title: "消息二则", words: [
                { word: "溃退", pinyin: "kuì tuì", meaning: "失败撤退" },
                { word: "泄气", pinyin: "xiè qì", meaning: "放弃" },
                { word: "督战", pinyin: "dū zhàn", meaning: "监督作战" },
                { word: "要塞", pinyin: "yào sài", meaning: "险要的关口" },
                { word: "业已", pinyin: "yè yǐ", meaning: "已经" },
                { word: "摧枯拉朽", pinyin: "cuī kū lā xiǔ", meaning: "摧毁腐朽的东西" },
                { word: "锐不可当", pinyin: "ruì bù kě dāng", meaning: "锋利无比不可抵挡" },
            ]},
            // 第2课 首届诺贝尔奖颁发
            { lesson: 2, title: "首届诺贝尔奖颁发", words: [
                { word: "颁发", pinyin: "bān fā", meaning: "发布授予" },
                { word: "遗嘱", pinyin: "yí zhǔ", meaning: "人死后生效的行为" },
                { word: "建树", pinyin: "jiàn shù", meaning: "建立的功绩" },
                { word: "仲裁", pinyin: "zhòng cái", meaning: "争执双方同意的第三者裁决" },
                { word: "巨额", pinyin: "jù é", meaning: "很大的数量" },
            ]},
        ],
        semester_2: [
            // 第1课 社戏
            { lesson: 1, title: "社戏", words: [
                { word: "社戏", pinyin: "shè xì", meaning: "乡村节日所演的戏" },
                { word: "消夏", pinyin: "xiāo xià", meaning: "消暑" },
                { word: "归省", pinyin: "guī xǐng", meaning: "回家探望父母" },
                { word: "行辈", pinyin: "háng bèi", meaning: "排行和辈分" },
                { word: "惮", pinyin: "dàn", meaning: "怕" },
                { word: "絮叨", pinyin: "xù dao", meaning: "来回地说" },
                { word: "大抵", pinyin: "dà dǐ", meaning: "大概" },
                { word: "怠慢", pinyin: "dài màn", meaning: "冷淡" },
                { word: "撺掇", pinyin: "cuān duo", meaning: "鼓动别人做某事" },
                { word: "凫水", pinyin: "fú shuǐ", meaning: "游泳" },
                { word: "朦朦胧胧", pinyin: "méng méng lóng lóng", meaning: "模糊不清楚" },
                { word: "漂渺", pinyin: "piāo miǎo", meaning: "隐隐约约若有若无" },
            ]},
        ],
    },

    // ==================== 九年级 ====================
    grade_9: {
        semester_1: [
            // 第1课 沁园春·雪
            { lesson: 1, title: "沁园春·雪", words: [
                { word: "莽莽", pinyin: "mǎng mǎng", meaning: "形容原野辽阔" },
                { word: "妖娆", pinyin: "yāo ráo", meaning: "娇艳美好" },
                { word: "风骚", pinyin: "fēng sāo", meaning: "风指《诗经》骚指《离骚》" },
                { word: "一代天骄", pinyin: "yí dài tiān jiāo", meaning: "一代之中最有成就的人物" },
                { word: "红装素裹", pinyin: "hóng zhuāng sù guǒ", meaning: "形容雪后天晴的景象" },
            ]},
            // 第2课 我爱这土地
            { lesson: 2, title: "我爱这土地", words: [
                { word: "嘶哑", pinyin: "sī yǎ", meaning: "沙哑" },
                { word: "汹涌", pinyin: "xiōng yǒng", meaning: "水势翻腾上涌" },
                { word: "黎明", pinyin: "lí míng", meaning: "天将亮时" },
                { word: "腐烂", pinyin: "fǔ làn", meaning: "有机体由于微生物的滋生而破坏" },
            ]},
        ],
        semester_2: [
            // 第1课 孔乙己
            { lesson: 1, title: "孔乙己", words: [
                { word: "格局", pinyin: "gé jú", meaning: "结构和格式" },
                { word: "阔绰", pinyin: "kuò chuò", meaning: "生活奢侈" },
                { word: "羼", pinyin: "chàn", meaning: "混合掺杂" },
                { word: "绽出", pinyin: "zhàn chū", meaning: "裂开露出" },
                { word: "服辩", pinyin: "fú biàn", meaning: "认罪的供状" },
                { word: "间或", pinyin: "jiàn huò", meaning: "偶尔" },
                { word: "附和", pinyin: "fù hè", meaning: "跟着别人说" },
                { word: "颓唐", pinyin: "tuí táng", meaning: "衰颓败落" },
                { word: "不屑置辩", pinyin: "bú xiè zhì biàn", meaning: "认为不值得争辩" },
                { word: "穷年累月", pinyin: "qióng nián lěi yuè", meaning: "形容时间长久" },
            ]},
        ],
    },
};

/**
 * 获取所有词语（去重）
 */
function getAllWords() {
    const allWords = [];
    const seen = new Set();

    for (const [grade, semesters] of Object.entries(JUNIOR_HIGH_WORDS)) {
        for (const [semester, lessons] of Object.entries(semesters)) {
            const semesterNum = semester === "semester_1" ? 1 : 2;
            for (const lesson of lessons) {
                for (const word of lesson.words) {
                    const key = `${word.word}_${grade}_${semesterNum}_${lesson.lesson}`;
                    if (!seen.has(key)) {
                        seen.add(key);
                        allWords.push({
                            ...word,
                            grade: parseInt(grade.replace("grade_", "")),
                            semester: semesterNum,
                            lesson: lesson.lesson,
                            lessonTitle: lesson.title,
                        });
                    }
                }
            }
        }
    }

    return allWords;
}

/**
 * 获取各年级词语统计
 */
function getGradeStats() {
    const stats = {};
    for (const [grade, semesters] of Object.entries(JUNIOR_HIGH_WORDS)) {
        let count = 0;
        for (const lessons of Object.values(semesters)) {
            for (const lesson of lessons) {
                count += lesson.words.length;
            }
        }
        stats[grade] = count;
    }
    stats.total = getAllWords().length;
    return stats;
}

// 导出
module.exports = {
    JUNIOR_HIGH_WORDS,
    getAllWords,
    getGradeStats,
};

// 命令行运行时打印统计
if (require.main === module) {
    const stats = getGradeStats();
    console.log("部编版初中7-9年级教材词语统计：");
    for (const [grade, count] of Object.entries(stats)) {
        console.log(`  ${grade}: ${count}词`);
    }
    console.log(`\n总计：${stats.total}词`);
}
