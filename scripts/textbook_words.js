#!/usr/bin/env node
/**
 * 部编版小学1-6年级教材词语表
 * 基于部编版语文教材整理，包含每课的二字/三字词语
 */

const TEXTBOOK_WORDS = {
    // ==================== 一年级 ====================
    grade_1: {
        semester_1: [
            // 识字1 天地人
            { lesson: 1, title: "天地人", words: [
                { word: "天地", pinyin: "tiān dì", meaning: "天和地，指自然界" },
                { word: "人口", pinyin: "rén kǒu", meaning: "人的总数" },
                { word: "你我", pinyin: "nǐ wǒ", meaning: "你和我" },
            ]},
            // 识字2 金木水火土
            { lesson: 2, title: "金木水火土", words: [
                { word: "金子", pinyin: "jīn zi", meaning: "黄金" },
                { word: "木头", pinyin: "mù tou", meaning: "木材" },
                { word: "水果", pinyin: "shuǐ guǒ", meaning: "多汁的果实" },
                { word: "火苗", pinyin: "huǒ miáo", meaning: "火焰" },
                { word: "土地", pinyin: "tǔ dì", meaning: "泥土" },
            ]},
            // 识字3 口耳目
            { lesson: 3, title: "口耳目", words: [
                { word: "口水", pinyin: "kǒu shuǐ", meaning: "唾液" },
                { word: "耳朵", pinyin: "ěr duo", meaning: "听觉器官" },
                { word: "目光", pinyin: "mù guāng", meaning: "视线" },
            ]},
            // 识字4 日月水火
            { lesson: 4, title: "日月水火", words: [
                { word: "日子", pinyin: "rì zi", meaning: "日期" },
                { word: "月亮", pinyin: "yuè liang", meaning: "月球" },
                { word: "水平", pinyin: "shuǐ píng", meaning: "水面" },
                { word: "火车", pinyin: "huǒ chē", meaning: "列车" },
            ]},
            // 识字5 对韵歌
            { lesson: 5, title: "对韵歌", words: [
                { word: "白云", pinyin: "bái yún", meaning: "白色的云" },
                { word: "小雨", pinyin: "xiǎo yǔ", meaning: "细雨" },
                { word: "大风", pinyin: "dà fēng", meaning: "强风" },
                { word: "小花", pinyin: "xiǎo huā", meaning: "花朵" },
            ]},
            // 课文1 a o e
            { lesson: 6, title: "汉语拼音", words: []},
            // 课文2 画
            { lesson: 8, title: "画", words: [
                { word: "山水", pinyin: "shān shuǐ", meaning: "山和水" },
                { word: "来去", pinyin: "lái qù", meaning: "往来" },
                { word: "还有", pinyin: "hái yǒu", meaning: "另外存在" },
            ]},
            // 课文3 四季
            { lesson: 9, title: "四季", words: [
                { word: "春天", pinyin: "chūn tiān", meaning: "春季" },
                { word: "夏天", pinyin: "xià tiān", meaning: "夏季" },
                { word: "秋天", pinyin: "qiū tiān", meaning: "秋季" },
                { word: "冬天", pinyin: "dōng tiān", meaning: "冬季" },
                { word: "就是", pinyin: "jiù shì", meaning: "正是" },
            ]},
            // 课文4 小小的船
            { lesson: 10, title: "小小的船", words: [
                { word: "小船", pinyin: "xiǎo chuán", meaning: "小舟" },
                { word: "月儿", pinyin: "yuè er", meaning: "月亮" },
                { word: "星星", pinyin: "xīng xing", meaning: "星" },
                { word: "闪亮", pinyin: "shǎn liàng", meaning: "发光" },
            ]},
            // 课文5 江南
            { lesson: 11, title: "江南", words: [
                { word: "江南", pinyin: "jiāng nán", meaning: "长江以南" },
                { word: "可以", pinyin: "kě yǐ", meaning: "能够" },
                { word: "莲叶", pinyin: "lián yè", meaning: "荷叶" },
            ]},
            // 课文6 影子
            { lesson: 12, title: "影子", words: [
                { word: "影子", pinyin: "yǐng zi", meaning: "物体的投影" },
                { word: "前后", pinyin: "qián hòu", meaning: "前面和后面" },
                { word: "左右", pinyin: "zuǒ yòu", meaning: "左边和右边" },
                { word: "朋友", pinyin: "péng you", meaning: "友人" },
            ]},
            // 课文7 比尾巴
            { lesson: 13, title: "比尾巴", words: [
                { word: "尾巴", pinyin: "wěi ba", meaning: "尾" },
                { word: "长短", pinyin: "cháng duǎn", meaning: "长度" },
                { word: "好像", pinyin: "hǎo xiàng", meaning: "如同" },
            ]},
            // 课文8 雨点儿
            { lesson: 14, title: "雨点儿", words: [
                { word: "雨点", pinyin: "yǔ diǎn", meaning: "雨滴" },
                { word: "地方", pinyin: "dì fang", meaning: "处所" },
                { word: "没有", pinyin: "méi yǒu", meaning: "无" },
            ]},
            // 课文9 明天要远足
            { lesson: 15, title: "明天要远足", words: [
                { word: "明天", pinyin: "míng tiān", meaning: "次日" },
                { word: "远足", pinyin: "yuǎn zú", meaning: "长途步行" },
                { word: "同学", pinyin: "tóng xué", meaning: "同窗" },
            ]},
            // 课文10 大还是小
            { lesson: 16, title: "大还是小", words: [
                { word: "还是", pinyin: "hái shi", meaning: "或者" },
                { word: "自己", pinyin: "zì jǐ", meaning: "自身" },
                { word: "衣服", pinyin: "yī fu", meaning: "服装" },
            ]},
            // 课文11 项链
            { lesson: 17, title: "项链", words: [
                { word: "项链", pinyin: "xiàng liàn", meaning: "颈饰" },
                { word: "金色", pinyin: "jīn sè", meaning: "金黄的颜色" },
                { word: "雪白", pinyin: "xuě bái", meaning: "像雪一样白" },
            ]},
        ],
        semester_2: [
            // 课文1 吃水不忘挖井人
            { lesson: 1, title: "吃水不忘挖井人", words: [
                { word: "井水", pinyin: "jǐng shuǐ", meaning: "井里的水" },
                { word: "主席", pinyin: "zhǔ xí", meaning: "主持会议的人" },
                { word: "革命", pinyin: "gé mìng", meaning: "变革" },
            ]},
            // 课文2 我多想去看看
            { lesson: 2, title: "我多想去看看", words: [
                { word: "想一想", pinyin: "xiǎng yi xiǎng", meaning: "思考" },
                { word: "告诉", pinyin: "gào su", meaning: "告知" },
                { word: "北京", pinyin: "běi jīng", meaning: "首都" },
                { word: "天安门", pinyin: "tiān ān mén", meaning: "城楼" },
            ]},
            // 课文3 一个接一个
            { lesson: 3, title: "一个接一个", words: [
                { word: "过去", pinyin: "guò qù", meaning: "以前" },
                { word: "有趣", pinyin: "yǒu qù", meaning: "有意思" },
                { word: "这样", pinyin: "zhè yàng", meaning: "如此" },
            ]},
            // 课文4 四个太阳
            { lesson: 4, title: "四个太阳", words: [
                { word: "太阳", pinyin: "tài yáng", meaning: "日" },
                { word: "阳光", pinyin: "yáng guāng", meaning: "日光" },
                { word: "温暖", pinyin: "wēn nuǎn", meaning: "暖和" },
                { word: "季节", pinyin: "jì jié", meaning: "时节" },
            ]},
            // 课文5 小公鸡和小鸭子
            { lesson: 5, title: "小公鸡和小鸭子", words: [
                { word: "小河", pinyin: "xiǎo hé", meaning: "河流" },
                { word: "不对", pinyin: "bù duì", meaning: "错误" },
                { word: "淹死", pinyin: "yān sǐ", meaning: "溺亡" },
            ]},
            // 课文6 树和喜鹊
            { lesson: 6, title: "树和喜鹊", words: [
                { word: "从前", pinyin: "cóng qián", meaning: "以前" },
                { word: "后来", pinyin: "hòu lái", meaning: "以后" },
                { word: "快乐", pinyin: "kuài lè", meaning: "愉快" },
                { word: "孤单", pinyin: "gū dān", meaning: "孤独" },
            ]},
            // 课文7 怎么都快乐
            { lesson: 7, title: "怎么都快乐", words: [
                { word: "怎么", pinyin: "zěn me", meaning: "如何" },
                { word: "独自", pinyin: "dú zì", meaning: "单独" },
                { word: "跳绳", pinyin: "tiào shéng", meaning: "跳绳子" },
            ]},
            // 课文8 静夜思
            { lesson: 8, title: "静夜思", words: [
                { word: "思乡", pinyin: "sī xiāng", meaning: "想念家乡" },
                { word: "月光", pinyin: "yuè guāng", meaning: "月亮的光" },
                { word: "故乡", pinyin: "gù xiāng", meaning: "家乡" },
            ]},
            // 课文9 夜色
            { lesson: 9, title: "夜色", words: [
                { word: "夜色", pinyin: "yè sè", meaning: "夜晚的景色" },
                { word: "微笑", pinyin: "wēi xiào", meaning: "轻轻的笑" },
                { word: "勇敢", pinyin: "yǒng gǎn", meaning: "有勇气" },
            ]},
            // 课文10 端午粽
            { lesson: 10, title: "端午粽", words: [
                { word: "端午", pinyin: "duān wǔ", meaning: "节日" },
                { word: "粽子", pinyin: "zòng zi", meaning: "食物" },
                { word: "知道", pinyin: "zhī dào", meaning: "了解" },
            ]},
            // 课文11 彩虹
            { lesson: 11, title: "彩虹", words: [
                { word: "彩虹", pinyin: "cǎi hóng", meaning: "雨后天弧" },
                { word: "秋千", pinyin: "qiū qiān", meaning: "荡椅" },
                { word: "镜子", pinyin: "jìng zi", meaning: "镜子" },
            ]},
        ],
    },

    // ==================== 二年级 ====================
    grade_2: {
        semester_1: [
            // 课文1 小蝌蚪找妈妈
            { lesson: 1, title: "小蝌蚪找妈妈", words: [
                { word: "蝌蚪", pinyin: "kē dǒu", meaning: "蛙的幼体" },
                { word: "池塘", pinyin: "chí táng", meaning: "水塘" },
                { word: "捕食", pinyin: "bǔ shí", meaning: "捕捉食物" },
                { word: "迎上去", pinyin: "yíng shàng qù", meaning: "上前打招呼" },
            ]},
            // 课文2 我是什么
            { lesson: 2, title: "我是什么", words: [
                { word: "飘浮", pinyin: "piāo fú", meaning: "浮在空中" },
                { word: "雹子", pinyin: "báo zi", meaning: "冰雹" },
                { word: "暴躁", pinyin: "bào zào", meaning: "急躁" },
                { word: "灌溉", pinyin: "guàn gài", meaning: "浇水" },
            ]},
            // 课文3 植物妈妈有办法
            { lesson: 3, title: "植物妈妈有办法", words: [
                { word: "植物", pinyin: "zhí wù", meaning: "草木" },
                { word: "办法", pinyin: "bàn fǎ", meaning: "方法" },
                { word: "准备", pinyin: "zhǔn bèi", meaning: "预备" },
                { word: "旅行", pinyin: "lǚ xíng", meaning: "出游" },
            ]},
            // 课文4 曹冲称象
            { lesson: 4, title: "曹冲称象", words: [
                { word: "称象", pinyin: "chēng xiàng", meaning: "称重量" },
                { word: "果然", pinyin: "guǒ rán", meaning: "确实" },
                { word: "议论", pinyin: "yì lùn", meaning: "讨论" },
            ]},
            // 课文5 玲玲的画
            { lesson: 5, title: "玲玲的画", words: [
                { word: "端详", pinyin: "duān xiáng", meaning: "仔细看" },
                { word: "满意", pinyin: "mǎn yì", meaning: "满足" },
                { word: "懒洋洋", pinyin: "lǎn yáng yáng", meaning: "没精打采" },
            ]},
            // 课文6 一封信
            { lesson: 6, title: "一封信", words: [
                { word: "信封", pinyin: "xìn fēng", meaning: "信的封套" },
                { word: "开心", pinyin: "kāi xīn", meaning: "快乐" },
                { word: "结束", pinyin: "jié shù", meaning: "完毕" },
            ]},
            // 课文7 妈妈睡了
            { lesson: 7, title: "妈妈睡了", words: [
                { word: "睡梦", pinyin: "shuì mèng", meaning: "睡眠" },
                { word: "美丽", pinyin: "měi lì", meaning: "漂亮" },
                { word: "温柔", pinyin: "wēn róu", meaning: "柔和" },
            ]},
            // 课文8 古诗二首（登鹳雀楼、望庐山瀑布）
            { lesson: 8, title: "古诗二首", words: [
                { word: "白日", pinyin: "bái rì", meaning: "太阳" },
                { word: "黄河", pinyin: "huáng hé", meaning: "母亲河" },
                { word: "瀑布", pinyin: "pù bù", meaning: "瀑布" },
            ]},
            // 课文9 黄山奇石
            { lesson: 9, title: "黄山奇石", words: [
                { word: "尤其", pinyin: "yóu qí", meaning: "特别" },
                { word: "著名", pinyin: "zhù míng", meaning: "出名" },
                { word: "奇形怪状", pinyin: "qí xíng guài zhuàng", meaning: "形状奇特" },
            ]},
            // 课文10 日月潭
            { lesson: 10, title: "日月潭", words: [
                { word: "湖水", pinyin: "hú shuǐ", meaning: "湖中的水" },
                { word: "环绕", pinyin: "huán rào", meaning: "围绕" },
                { word: "隐约会现", pinyin: "yǐn yuē xiàn", meaning: "时隐时现" },
            ]},
            // 课文11 葡萄沟
            { lesson: 11, title: "葡萄沟", words: [
                { word: "葡萄", pinyin: "pú táo", meaning: "水果" },
                { word: "梯田", pinyin: "tī tián", meaning: "阶梯状田地" },
                { word: "茂密", pinyin: "mào mì", meaning: "繁茂" },
                { word: "碉堡", pinyin: "diāo bǎo", meaning: "防御工事" },
            ]},
        ],
        semester_2: [
            // 课文1 古诗村居、咏柳
            { lesson: 1, title: "古诗二首", words: [
                { word: "村居", pinyin: "cūn jū", meaning: "乡村居住" },
                { word: "杨柳", pinyin: "yáng liǔ", meaning: "柳树" },
                { word: "春风", pinyin: "chūn fēng", meaning: "春天的风" },
                { word: "纸鸢", pinyin: "zhǐ yuān", meaning: "风筝" },
            ]},
            // 课文2 找春天
            { lesson: 2, title: "找春天", words: [
                { word: "害羞", pinyin: "hài xiū", meaning: "怕难为情" },
                { word: "嫩芽", pinyin: "nèn yá", meaning: "新芽" },
                { word: "音符", pinyin: "yīn fú", meaning: "乐谱符号" },
            ]},
            // 课文3 开满鲜花的小路
            { lesson: 3, title: "开满鲜花的小路", words: [
                { word: "邮递员", pinyin: "yóu dì yuán", meaning: "送信的人" },
                { word: "包裹", pinyin: "guǒ bāo", meaning: "包扎的物品" },
                { word: "绚丽", pinyin: "xuàn lì", meaning: "灿烂美丽" },
            ]},
            // 课文4 邓小平爷爷植树
            { lesson: 4, title: "邓小平爷爷植树", words: [
                { word: "植树", pinyin: "zhí shù", meaning: "种树" },
                { word: "格外", pinyin: "gé wài", meaning: "特别" },
                { word: "茁壮", pinyin: "zhuó zhuàng", meaning: "健壮" },
            ]},
            // 课文5 雷锋叔叔，你在哪里
            { lesson: 5, title: "雷锋叔叔，你在哪里", words: [
                { word: "雷锋", pinyin: "léi fēng", meaning: "英雄" },
                { word: "泥泞", pinyin: "ní nìng", meaning: "烂泥" },
                { word: "晶莹", pinyin: "jīng yíng", meaning: "光亮透明" },
            ]},
            // 课文6 千人糕
            { lesson: 6, title: "千人糕", words: [
                { word: "糕点", pinyin: "gāo diǎn", meaning: "糕饼" },
                { word: "特别", pinyin: "tè bié", meaning: "与众不同" },
                { word: "应该", pinyin: "yīng gāi", meaning: "理当" },
            ]},
            // 课文7 一匹出色的马
            { lesson: 7, title: "一匹出色的马", words: [
                { word: "出色", pinyin: "chū sè", meaning: "出众" },
                { word: "葱绿", pinyin: "cōng lǜ", meaning: "青翠" },
                { word: "恋恋不舍", pinyin: "liàn liàn bù shě", meaning: "舍不得离开" },
            ]},
            // 课文8 彩色的梦
            { lesson: 8, title: "彩色的梦", words: [
                { word: "彩色", pinyin: "cǎi sè", meaning: "多种颜色" },
                { word: "梦境", pinyin: "mèng jìng", meaning: "梦中的境界" },
                { word: "精灵", pinyin: "jīng líng", meaning: "神仙" },
            ]},
            // 课文9 枫树上的喜鹊
            { lesson: 9, title: "枫树上的喜鹊", words: [
                { word: "喜鹊", pinyin: "xǐ què", meaning: "鸟名" },
                { word: "枫树", pinyin: "fēng shù", meaning: "槭树" },
                { word: "渡口", pinyin: "dù kǒu", meaning: "过河的地方" },
            ]},
            // 课文10 沙滩上的童话
            { lesson: 10, title: "沙滩上的童话", words: [
                { word: "城堡", pinyin: "chéng bǎo", meaning: "堡垒" },
                { word: "插上", pinyin: "chā shàng", meaning: "插入" },
                { word: "商量", pinyin: "shāng liáng", meaning: "商讨" },
            ]},
            // 课文11 我是一只小虫子
            { lesson: 11, title: "我是一只小虫子", words: [
                { word: "屁股", pinyin: "pì gu", meaning: "臀部" },
                { word: "幸运", pinyin: "xìng yùn", meaning: "好运气" },
                { word: "使劲", pinyin: "shǐ jìn", meaning: "用力" },
            ]},
        ],
    },

    // ==================== 三年级 ====================
    grade_3: {
        semester_1: [
            // 课文1 大青树下的小学
            { lesson: 1, title: "大青树下的小学", words: [
                { word: "坪坝", pinyin: "píng bà", meaning: "平地" },
                { word: "穿戴", pinyin: "chuān dài", meaning: "穿着" },
                { word: "鲜艳", pinyin: "xiān yàn", meaning: "鲜明艳丽" },
                { word: "服装", pinyin: "fú zhuāng", meaning: "衣服" },
                { word: "打扮", pinyin: "dǎ bàn", meaning: "装扮" },
            ]},
            // 课文2 花的学校
            { lesson: 2, title: "花的学校", words: [
                { word: "湿润", pinyin: "shī rùn", meaning: "潮湿" },
                { word: "碰触", pinyin: "pèng chù", meaning: "接触" },
                { word: "荒野", pinyin: "huāng yě", meaning: "荒凉的野外" },
            ]},
            // 课文3 不懂就要问
            { lesson: 3, title: "不懂就要问", words: [
                { word: "私塾", pinyin: "sī shú", meaning: "旧时学校" },
                { word: "照例", pinyin: "zhào lì", meaning: "按照惯例" },
                { word: "糊涂", pinyin: "hú tu", meaning: "不明白" },
            ]},
            // 课文4 古诗三首（山行、赠刘景文、夜书所见）
            { lesson: 4, title: "古诗三首", words: [
                { word: "寒山", pinyin: "hán shān", meaning: "深秋的山" },
                { word: "石径", pinyin: "shí jìng", meaning: "石头小路" },
                { word: "霜叶", pinyin: "shuāng yè", meaning: "经霜的叶子" },
            ]},
            // 课文5 铺满金色巴掌的水泥道
            { lesson: 5, title: "铺满金色巴掌的水泥道", words: [
                { word: "水泥", pinyin: "shuǐ ní", meaning: "建材" },
                { word: "印着", pinyin: "yìn zhe", meaning: "留有痕迹" },
                { word: "凌乱", pinyin: "líng luàn", meaning: "杂乱" },
            ]},
            // 课文6 秋天的雨
            { lesson: 6, title: "秋天的雨", words: [
                { word: "颜料", pinyin: "yán liào", meaning: "染色的材料" },
                { word: "邮票", pinyin: "yóu piào", meaning: "邮资凭证" },
                { word: "频频", pinyin: "pín pín", meaning: "连续多次" },
            ]},
            // 课文7 听听，秋的声音
            { lesson: 7, title: "听听，秋的声音", words: [
                { word: "歌吟", pinyin: "gē yín", meaning: "歌唱" },
                { word: "道别", pinyin: "dào bié", meaning: "告别" },
                { word: "掠过", pinyin: "lüè guò", meaning: "轻轻擦过" },
            ]},
            // 课文8 去年的树
            { lesson: 8, title: "去年的树", words: [
                { word: "融化", pinyin: "róng huà", meaning: "化开" },
                { word: "剩下", pinyin: "shèng xià", meaning: "余下" },
                { word: "伐木", pinyin: "fá mù", meaning: "砍树" },
            ]},
            // 课文9 那一定会很好
            { lesson: 9, title: "那一定会很好", words: [
                { word: "种子", pinyin: "zhǒng zi", meaning: "植物的籽" },
                { word: "努力", pinyin: "nǔ lì", meaning: "尽力" },
                { word: "舒展", pinyin: "shū zhǎn", meaning: "展开" },
            ]},
            // 课文10 在牛肚子里旅行
            { lesson: 10, title: "在牛肚子里旅行", words: [
                { word: "旅行", pinyin: "lǚ xíng", meaning: "出游" },
                { word: "胃里", pinyin: "wèi lǐ", meaning: "胃中" },
                { word: "悲哀", pinyin: "bēi āi", meaning: "悲伤" },
            ]},
            // 课文11 一块奶酪
            { lesson: 11, title: "一块奶酪", words: [
                { word: "奶酪", pinyin: "nǎi lào", meaning: "乳制品" },
                { word: "诱使", pinyin: "yòu shǐ", meaning: "引诱" },
                { word: "毅力", pinyin: "yì lì", meaning: "坚持的意志" },
            ]},
            // 课文12 总也倒不了的老屋
            { lesson: 12, title: "总也倒不了的老屋", words: [
                { word: "老屋", pinyin: "lǎo wū", meaning: "旧房子" },
                { word: "窟窿", pinyin: "kū long", meaning: "洞" },
                { word: "勉强", pinyin: "miǎn qiǎng", meaning: "将就" },
            ]},
        ],
        semester_2: [
            // 课文1 古诗三首（绝句、惠崇春江晚景、三衢道中）
            { lesson: 1, title: "古诗三首", words: [
                { word: "融融", pinyin: "róng róng", meaning: "温暖" },
                { word: "鸳鸯", pinyin: "yuān yāng", meaning: "水鸟" },
                { word: "蒌蒿", pinyin: "lóu hāo", meaning: "草名" },
            ]},
            // 课文2 燕子
            { lesson: 2, title: "燕子", words: [
                { word: "燕子", pinyin: "yàn zi", meaning: "鸟名" },
                { word: "聚拢", pinyin: "jù lǒng", meaning: "聚集" },
                { word: "增添", pinyin: "zēng tiān", meaning: "增加" },
                { word: "掠过", pinyin: "lüè guò", meaning: "擦过" },
            ]},
            // 课文3 荷花
            { lesson: 3, title: "荷花", words: [
                { word: "花瓣", pinyin: "huā bàn", meaning: "花的片状部分" },
                { word: "莲蓬", pinyin: "lián péng", meaning: "荷的果实" },
                { word: "饱胀", pinyin: "bǎo zhàng", meaning: "鼓起" },
                { word: "姿势", pinyin: "zī shì", meaning: "姿态" },
            ]},
            // 课文4 昆虫备忘录
            { lesson: 4, title: "昆虫备忘录", words: [
                { word: "复眼", pinyin: "fù yǎn", meaning: "昆虫的眼睛" },
                { word: "琢磨", pinyin: "zuó mo", meaning: "思考" },
                { word: "凡是", pinyin: "fán shì", meaning: "所有" },
            ]},
            // 课文5 守株待兔
            { lesson: 5, title: "守株待兔", words: [
                { word: "守株待兔", pinyin: "shǒu zhū dài tù", meaning: "不劳而获" },
                { word: "宋国", pinyin: "sòng guó", meaning: "古国名" },
                { word: "耕田", pinyin: "gēng tián", meaning: "种地" },
            ]},
            // 课文6 陶罐和铁罐
            { lesson: 6, title: "陶罐和铁罐", words: [
                { word: "陶罐", pinyin: "táo guàn", meaning: "陶瓷罐子" },
                { word: "骄傲", pinyin: "jiāo ào", meaning: "自满" },
                { word: "谦虚", pinyin: "qiān xū", meaning: "不自满" },
                { word: "懦弱", pinyin: "nuò ruò", meaning: "软弱" },
            ]},
            // 课文7 狮子和鹿
            { lesson: 7, title: "狮子和鹿", words: [
                { word: "池塘", pinyin: "chí táng", meaning: "水塘" },
                { word: "倒映", pinyin: "dào yìng", meaning: "映照" },
                { word: "匀称", pinyin: "yún chèn", meaning: "均匀" },
            ]},
            // 课文8 古诗三首（元日、清明、九月九日忆山东兄弟）
            { lesson: 8, title: "古诗三首", words: [
                { word: "爆竹", pinyin: "bào zhú", meaning: "鞭炮" },
                { word: "屠苏", pinyin: "tú sū", meaning: "酒名" },
                { word: "茱萸", pinyin: "zhū yú", meaning: "植物名" },
            ]},
            // 课文9 赵州桥
            { lesson: 9, title: "赵州桥", words: [
                { word: "设计", pinyin: "shè jì", meaning: "筹划" },
                { word: "参加", pinyin: "cān jiā", meaning: "加入" },
                { word: "横跨", pinyin: "héng kuà", meaning: "跨越" },
                { word: "创举", pinyin: "chuàng jǔ", meaning: "首创" },
            ]},
            // 课文10 一幅名扬中外的画
            { lesson: 10, title: "一幅名扬中外的画", words: [
                { word: "名扬中外", pinyin: "míng yáng zhōng wài", meaning: "闻名世界" },
                { word: "汴京", pinyin: "biàn jīng", meaning: "古都名" },
                { word: "传神", pinyin: "chuán shén", meaning: "逼真" },
            ]},
        ],
    },

    // ==================== 四年级 ====================
    grade_4: {
        semester_1: [
            // 课文1 观潮
            { lesson: 1, title: "观潮", words: [
                { word: "观潮", pinyin: "guān cháo", meaning: "观看潮水" },
                { word: "笼罩", pinyin: "lǒng zhào", meaning: "覆盖" },
                { word: "屹立", pinyin: "yì lì", meaning: "高耸" },
                { word: "人声鼎沸", pinyin: "rén shēng dǐng fèi", meaning: "喧闹" },
            ]},
            // 课文2 走月亮
            { lesson: 2, title: "走月亮", words: [
                { word: "淘洗", pinyin: "táo xǐ", meaning: "冲洗" },
                { word: "坑坑洼洼", pinyin: "kēng kēng wā wā", meaning: "不平" },
                { word: "闪闪烁烁", pinyin: "shǎn shǎn shuò shuò", meaning: "忽明忽暗" },
            ]},
            // 课文3 现代诗二首
            { lesson: 3, title: "现代诗二首", words: [
                { word: "归巢", pinyin: "guī cháo", meaning: "回窝" },
                { word: "霸占", pinyin: "bà zhàn", meaning: "强占" },
                { word: "尽管", pinyin: "jǐn guǎn", meaning: "虽然" },
            ]},
            // 课文4 繁星
            { lesson: 4, title: "繁星", words: [
                { word: "繁星", pinyin: "fán xīng", meaning: "众多的星星" },
                { word: "半明半昧", pinyin: "bàn míng bàn mèi", meaning: "忽明忽暗" },
                { word: "摇摇欲坠", pinyin: "yáo yáo yù zhuì", meaning: "快要掉下" },
            ]},
            // 课文5 一个豆荚里的五粒豆
            { lesson: 5, title: "一个豆荚里的五粒豆", words: [
                { word: "豌豆", pinyin: "wān dòu", meaning: "豆类" },
                { word: "暖洋洋", pinyin: "nuǎn yáng yáng", meaning: "温暖" },
                { word: "僵硬", pinyin: "jiāng yìng", meaning: "不灵活" },
            ]},
            // 课文6 蝙蝠和雷达
            { lesson: 6, title: "蝙蝠和雷达", words: [
                { word: "蝙蝠", pinyin: "biān fú", meaning: "夜行动物" },
                { word: "雷达", pinyin: "léi dá", meaning: "探测设备" },
                { word: "敏锐", pinyin: "mǐn ruì", meaning: "灵敏" },
                { word: "障碍", pinyin: "zhàng ài", meaning: "阻碍" },
            ]},
            // 课文7 呼风唤雨的世纪
            { lesson: 7, title: "呼风唤雨的世纪", words: [
                { word: "获得", pinyin: "huò dé", meaning: "取得" },
                { word: "依赖", pinyin: "yī lài", meaning: "依靠" },
                { word: "洞察", pinyin: "dòng chá", meaning: "看透" },
            ]},
            // 课文8 蝴蝶的家
            { lesson: 8, title: "蝴蝶的家", words: [
                { word: "震撼", pinyin: "zhèn hàn", meaning: "震动" },
                { word: "玷污", pinyin: "diàn wū", meaning: "弄脏" },
                { word: "屋檐", pinyin: "wū yán", meaning: "房顶边沿" },
            ]},
        ],
        semester_2: [
            // 课文1 琥珀
            { lesson: 1, title: "琥珀", words: [
                { word: "琥珀", pinyin: "hǔ pò", meaning: "树脂化石" },
                { word: "拂拭", pinyin: "fú shì", meaning: "擦掉" },
                { word: "推测", pinyin: "tuī cè", meaning: "推断" },
            ]},
            // 课文2 飞向蓝天的恐龙
            { lesson: 2, title: "飞向蓝天的恐龙", words: [
                { word: "恐龙", pinyin: "kǒng lóng", meaning: "古生物" },
                { word: "笨重", pinyin: "bèn zhòng", meaning: "沉重" },
                { word: "凌空", pinyin: "líng kōng", meaning: "高悬空中" },
            ]},
            // 课文3 纳米技术就在我们身边
            { lesson: 3, title: "纳米技术就在我们身边", words: [
                { word: "纳米", pinyin: "nà mǐ", meaning: "长度单位" },
                { word: "拥有", pinyin: "yōng yǒu", meaning: "具有" },
                { word: "造福", pinyin: "zào fú", meaning: "带来幸福" },
            ]},
            // 课文4 千年梦圆在今朝
            { lesson: 4, title: "千年梦圆在今朝", words: [
                { word: "嫦娥", pinyin: "cháng é", meaning: "神话人物" },
                { word: "揽月", pinyin: "lǎn yuè", meaning: "摘月亮" },
                { word: "振奋", pinyin: "zhèn fèn", meaning: "振作" },
            ]},
            // 课文5 琥珀
            { lesson: 5, title: "琥珀", words: [
                { word: "刷洗", pinyin: "shuā xǐ", meaning: "清洗" },
                { word: "一番", pinyin: "yì fān", meaning: "一次" },
                { word: "挣扎", pinyin: "zhēng zhá", meaning: "用力支撑" },
            ]},
        ],
    },

    // ==================== 五年级 ====================
    grade_5: {
        semester_1: [
            // 课文1 白鹭
            { lesson: 1, title: "白鹭", words: [
                { word: "精巧", pinyin: "jīng qiǎo", meaning: "精致巧妙" },
                { word: "色素", pinyin: "sè sù", meaning: "颜色" },
                { word: "适宜", pinyin: "shì yí", meaning: "合适" },
                { word: "忘却", pinyin: "wàng què", meaning: "忘记" },
            ]},
            // 课文2 落花生
            { lesson: 2, title: "落花生", words: [
                { word: "花生", pinyin: "huā shēng", meaning: "豆科植物" },
                { word: "吩咐", pinyin: "fēn fù", meaning: "嘱咐" },
                { word: "辨认", pinyin: "biàn rèn", meaning: "识别" },
                { word: "体面", pinyin: "tǐ miàn", meaning: "好看" },
            ]},
            // 课文3 桂花雨
            { lesson: 3, title: "桂花雨", words: [
                { word: "桂花", pinyin: "guì huā", meaning: "木犀花" },
                { word: "姿态", pinyin: "zī tài", meaning: "姿势" },
                { word: "尤其", pinyin: "yóu qí", meaning: "特别" },
            ]},
            // 课文4 珍珠鸟
            { lesson: 4, title: "珍珠鸟", words: [
                { word: "珍珠", pinyin: "zhēn zhū", meaning: "蚌珠" },
                { word: "雏儿", pinyin: "chú er", meaning: "幼鸟" },
                { word: "信赖", pinyin: "xìn lài", meaning: "信任" },
            ]},
        ],
        semester_2: [
            // 课文1 古诗三首（四时田园杂兴、稚子弄冰、村晚）
            { lesson: 1, title: "古诗三首", words: [
                { word: "耘田", pinyin: "yún tián", meaning: "除草" },
                { word: "绩麻", pinyin: "jì má", meaning: "纺麻" },
                { word: "信口", pinyin: "xìn kǒu", meaning: "随口" },
            ]},
            // 课文2 祖父的园子
            { lesson: 2, title: "祖父的园子", words: [
                { word: "蝴蝶", pinyin: "hú dié", meaning: "昆虫" },
                { word: "蜻蜓", pinyin: "qīng tíng", meaning: "昆虫" },
                { word: "蚂蚱", pinyin: "mà zha", meaning: "昆虫" },
                { word: "圆滚滚", pinyin: "yuán gǔn gǔn", meaning: "很圆" },
            ]},
        ],
    },

    // ==================== 六年级 ====================
    grade_6: {
        semester_1: [
            // 课文1 北京的春节
            { lesson: 1, title: "北京的春节", words: [
                { word: "春节", pinyin: "chūn jié", meaning: "农历新年" },
                { word: "腊月", pinyin: "là yuè", meaning: "农历十二月" },
                { word: "翡翠", pinyin: "fěi cuì", meaning: "玉石" },
                { word: "万象更新", pinyin: "wàn xiàng gēng xīn", meaning: "一切焕然一新" },
            ]},
            // 课文2 腊八粥
            { lesson: 2, title: "腊八粥", words: [
                { word: "腊八", pinyin: "là bā", meaning: "农历十二月初八" },
                { word: "熬粥", pinyin: "áo zhōu", meaning: "煮粥" },
                { word: "甜腻", pinyin: "tián nì", meaning: "又甜又腻" },
            ]},
            // 课文3 古诗三首（寒食、迢迢牵牛星、十五夜望月）
            { lesson: 3, title: "古诗三首", words: [
                { word: "寒食", pinyin: "hán shí", meaning: "节日名" },
                { word: "迢迢", pinyin: "tiáo tiáo", meaning: "遥远" },
                { word: "皎皎", pinyin: "jiǎo jiǎo", meaning: "明亮" },
            ]},
            // 课文4 藏戏
            { lesson: 4, title: "藏戏", words: [
                { word: "藏戏", pinyin: "zàng xì", meaning: "藏族戏曲" },
                { word: "面具", pinyin: "miàn jù", meaning: "假面" },
                { word: "吞噬", pinyin: "tūn shì", meaning: "吞掉" },
            ]},
        ],
        semester_2: [
            // 课文1 北京的春节
            { lesson: 1, title: "北京的春节", words: [
                { word: "初旬", pinyin: "chū xún", meaning: "每月前十天" },
                { word: "蒜瓣", pinyin: "suàn bàn", meaning: "蒜的瓣" },
                { word: "醋酸", pinyin: "cù suān", meaning: "醋的味道" },
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

    for (const [grade, semesters] of Object.entries(TEXTBOOK_WORDS)) {
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
    for (const [grade, semesters] of Object.entries(TEXTBOOK_WORDS)) {
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

/**
 * 导出为JSON格式
 */
function exportToJson() {
    const words = getAllWords();
    return {
        textbook_words: TEXTBOOK_WORDS,
        flat_words: words,
        stats: getGradeStats(),
    };
}

// 导出
module.exports = {
    TEXTBOOK_WORDS,
    getAllWords,
    getGradeStats,
    exportToJson,
};

// 命令行运行时打印统计
if (require.main === module) {
    const stats = getGradeStats();
    console.log("部编版小学1-6年级教材词语统计：");
    for (const [grade, count] of Object.entries(stats)) {
        console.log(`  ${grade}: ${count}词`);
    }
    console.log(`\n总计：${stats.total}词`);
}
