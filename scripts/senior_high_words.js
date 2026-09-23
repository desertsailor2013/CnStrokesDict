#!/usr/bin/env node
/**
 * 部编版高中10-12年级教材词语表
 * 基于部编版高中语文教材整理，包含必修和选择性必修的词语
 */

const SENIOR_HIGH_WORDS = {
    // ==================== 高一（必修） ====================
    grade_10: {
        // 必修上册
        semester_1: [
            // 第1课 沁园春·长沙
            { lesson: 1, title: "沁园春·长沙", words: [
                { word: "独立寒秋", pinyin: "dú lì hán qiū", meaning: "独自站在深秋" },
                { word: "湘江北去", pinyin: "xiāng jiāng běi qù", meaning: "湘江向北流去" },
                { word: "橘子洲头", pinyin: "jú zǐ zhōu tóu", meaning: "橘子洲的头部" },
                { word: "万山红遍", pinyin: "wàn shān hóng biàn", meaning: "所有山都变红了" },
                { word: "层林尽染", pinyin: "céng lín jìn rǎn", meaning: "层层树林全部染红" },
                { word: "漫江碧透", pinyin: "màn jiāng bì tòu", meaning: "满江碧绿清澈" },
                { word: "百舸争流", pinyin: "bǎi gě zhēng liú", meaning: "许多船争相行驶" },
                { word: "鹰击长空", pinyin: "yīng jī cháng kōng", meaning: "雄鹰在高空飞翔" },
                { word: "鱼翔浅底", pinyin: "yú xiáng qiǎn dǐ", meaning: "鱼在清澈的水底游动" },
                { word: "万类霜天", pinyin: "wàn lèi shuāng tiān", meaning: "万物在秋天" },
                { word: "竞自由", pinyin: "jìng zì yóu", meaning: "争相自由" },
                { word: "怅寥廓", pinyin: "chàng liáo kuò", meaning: "面对广阔的天地感慨" },
                { word: "问苍茫大地", pinyin: "wèn cāng máng dà dì", meaning: "问广阔的大地上" },
                { word: "谁主沉浮", pinyin: "shuí zhǔ chén fú", meaning: "谁来主宰兴衰" },
                { word: "恰同学少年", pinyin: "qià tóng xué shào nián", meaning: "正值同学年少时" },
                { word: "风华正茂", pinyin: "fēng huá zhèng mào", meaning: "风采才华正旺盛" },
                { word: "书生意气", pinyin: "shū shēng yì qì", meaning: "读书人的意气" },
                { word: "挥斥方遒", pinyin: "huī chì fāng qiú", meaning: "挥洒自如有力" },
                { word: "指点江山", pinyin: "zhǐ diǎn jiāng shān", meaning: "评论国家大事" },
                { word: "激扬文字", pinyin: "jī yáng wén zì", meaning: "激浊扬清的文章" },
                { word: "粪土当年万户侯", pinyin: "fèn tǔ dāng nián wàn hù hóu", meaning: "把当年的权贵视为粪土" },
            ]},
            // 第2课 烛之武退秦师
            { lesson: 2, title: "烛之武退秦师", words: [
                { word: "晋侯", pinyin: "jìn hóu", meaning: "晋国国君" },
                { word: "秦伯", pinyin: "qín bó", meaning: "秦国国君" },
                { word: "郑伯", pinyin: "zhèng bó", meaning: "郑国国君" },
                { word: "国危矣", pinyin: "guó wēi yǐ", meaning: "国家危险了" },
                { word: "若使烛之武见秦君", pinyin: "ruò shǐ zhú zhī wǔ jiàn qín jūn", meaning: "如果派烛之武见秦君" },
                { word: "臣之壮也", pinyin: "chén zhī zhuàng yě", meaning: "我壮年的时候" },
                { word: "犹不如人", pinyin: "yóu bù rú rén", meaning: "尚且不如别人" },
                { word: "今老矣", pinyin: "jīn lǎo yǐ", meaning: "现在老了" },
                { word: "无能为也已", pinyin: "wú néng wéi yě yǐ", meaning: "没有能力做什么了" },
                { word: "许之", pinyin: "xǔ zhī", meaning: "答应了他" },
            ]},
            // 第3课 鸿门宴
            { lesson: 3, title: "鸿门宴", words: [
                { word: "沛公", pinyin: "pèi gōng", meaning: "刘邦" },
                { word: "项羽", pinyin: "xiàng yǔ", meaning: "西楚霸王" },
                { word: "范增", pinyin: "fàn zēng", meaning: "项羽谋士" },
                { word: "张良", pinyin: "zhāng liáng", meaning: "刘邦谋士" },
                { word: "樊哙", pinyin: "fán kuài", meaning: "刘邦部将" },
                { word: "鸿门宴", pinyin: "hóng mén yàn", meaning: "比喻暗藏杀机的宴会" },
                { word: "项庄舞剑", pinyin: "xiàng zhuāng wǔ jiàn", meaning: "意在沛公" },
                { word: "人为刀俎", pinyin: "rén wéi dāo zǔ", meaning: "我为鱼肉" },
                { word: "大行不顾细谨", pinyin: "dà xíng bù gù xì jǐn", meaning: "做大事不必顾及小节" },
                { word: "大礼不辞小让", pinyin: "dà lǐ bù cí xiǎo ràng", meaning: "行大礼不必计较小的谦让" },
            ]},
        ],
        // 必修下册
        semester_2: [
            // 第1课 子路、曾皙、冉有、公西华侍坐
            { lesson: 1, title: "子路、曾皙、冉有、公西华侍坐", words: [
                { word: "侍坐", pinyin: "shì zuò", meaning: "陪坐在尊长旁边" },
                { word: "率尔", pinyin: "shuài ěr", meaning: "轻率急忙的样子" },
                { word: "千乘之国", pinyin: "qiān shèng zhī guó", meaning: "有一千辆兵车的国家" },
                { word: "摄乎大国之间", pinyin: "shè hū dà guó zhī jiān", meaning: "夹在大国之间" },
                { word: "加之以师旅", pinyin: "jiā zhī yǐ shī lǚ", meaning: "加上有军队侵犯" },
                { word: "因之以饥馑", pinyin: "yīn zhī yǐ jī jǐn", meaning: "接着又有饥荒" },
                { word: "比及三年", pinyin: "bǐ jí sān nián", meaning: "等到三年" },
                { word: "可使有勇", pinyin: "kě shǐ yǒu yǒng", meaning: "可以使百姓有勇气" },
                { word: "且知方也", pinyin: "qiě zhī fāng yě", meaning: "并且知道道理" },
                { word: "方六七十", pinyin: "fāng liù qī shí", meaning: "纵横六七十里" },
                { word: "如五六十", pinyin: "rú wǔ liù shí", meaning: "或者五六十里" },
                { word: "求也为之", pinyin: "qiú yě wéi zhī", meaning: "冉求去做这件事" },
                { word: "比及三年", pinyin: "bǐ jí sān nián", meaning: "等到三年" },
                { word: "可使足民", pinyin: "kě shǐ zú mín", meaning: "可以使百姓富足" },
                { word: "如其礼乐", pinyin: "rú qí lǐ yuè", meaning: "至于礼乐教化" },
                { word: "以俟君子", pinyin: "yǐ sì jūn zǐ", meaning: "要等待君子" },
            ]},
            // 第2课 齐桓晋文之事
            { lesson: 2, title: "齐桓晋文之事", words: [
                { word: "齐宣王", pinyin: "qí xuān wáng", meaning: "齐国国君" },
                { word: "齐桓晋文之事", pinyin: "qí huán jìn wén zhī shì", meaning: "齐桓公晋文公称霸的事" },
                { word: "保民而王", pinyin: "bǎo mín ér wáng", meaning: "使人民安定而称王" },
                { word: "不忍其觳觫", pinyin: "bù rěn qí hú sù", meaning: "不忍心看它恐惧发抖" },
                { word: "是不为也", pinyin: "shì bù wéi yě", meaning: "这是不肯做" },
                { word: "非不能也", pinyin: "fēi bù néng yě", meaning: "不是没有能力" },
                { word: "不为者与不能者之形", pinyin: "bù wéi zhě yǔ bù néng zhě zhī xíng", meaning: "不肯做和没有能力的表现" },
                { word: "何以异", pinyin: "hé yǐ yì", meaning: "有什么不同" },
                { word: "挟太山以超北海", pinyin: "xié tài shān yǐ chāo běi hǎi", meaning: "夹着泰山跨越北海" },
                { word: "为长者折枝", pinyin: "wèi zhǎng zhě zhé zhī", meaning: "替长辈折树枝" },
            ]},
            // 第3课 庖丁解牛
            { lesson: 3, title: "庖丁解牛", words: [
                { word: "庖丁", pinyin: "páo dīng", meaning: "厨师" },
                { word: "解牛", pinyin: "jiě niú", meaning: "宰牛" },
                { word: "手之所触", pinyin: "shǒu zhī suǒ chù", meaning: "手接触的地方" },
                { word: "肩之所倚", pinyin: "jiān zhī suǒ yǐ", meaning: "肩膀靠的地方" },
                { word: "足之所履", pinyin: "zú zhī suǒ lǚ", meaning: "脚踩的地方" },
                { word: "膝之所踦", pinyin: "xī zhī suǒ yǐ", meaning: "膝盖顶的地方" },
                { word: "砉然向然", pinyin: "huò rán xiàng rán", meaning: "哗哗作响" },
                { word: "奏刀騞然", pinyin: "zòu dāo huō rán", meaning: "进刀时发出嚯嚯声" },
                { word: "莫不中音", pinyin: "mò bù zhòng yīn", meaning: "没有不合乎音律的" },
                { word: "合于桑林之舞", pinyin: "hé yú sāng lín zhī wǔ", meaning: "合乎桑林舞曲的节拍" },
            ]},
            // 第4课 烛之武退秦师（补充）
            { lesson: 4, title: "烛之武退秦师（补充）", words: [
                { word: "越国以鄙远", pinyin: "yuè guó yǐ bǐ yuǎn", meaning: "越过别国把远地当作边邑" },
                { word: "焉用亡郑以陪邻", pinyin: "yān yòng wáng zhèng yǐ péi lín", meaning: "为什么要灭掉郑国来增加邻国的土地" },
                { word: "若舍郑以为东道主", pinyin: "ruò shě zhèng yǐ wéi dōng dào zhǔ", meaning: "如果放弃围攻郑国作为东方道路上的主人" },
                { word: "行李之往来", pinyin: "xíng lǐ zhī wǎng lái", meaning: "外交使节的往来" },
                { word: "共其乏困", pinyin: "gòng qí fá kùn", meaning: "供给他们缺少的东西" },
                { word: "夫晋，何厌之有", pinyin: "fū jìn, hé yàn zhī yǒu", meaning: "晋国，有什么满足的" },
                { word: "既东封郑", pinyin: "jì dōng fēng zhèng", meaning: "已经在东边使郑国成为边界" },
                { word: "又欲肆其西封", pinyin: "yòu yù sì qí xī fēng", meaning: "又想扩张西边的疆界" },
                { word: "若不阙秦", pinyin: "ruò bù quē qín", meaning: "如果不使秦国土地减少" },
                { word: "将焉取之", pinyin: "jiāng yān qǔ zhī", meaning: "将从哪里取得它" },
            ]},
        ],
    },

    // ==================== 高二（选择性必修） ====================
    grade_11: {
        // 选择性必修上册
        semester_1: [
            // 第1课 论语十二章
            { lesson: 1, title: "论语十二章", words: [
                { word: "君子食无求饱", pinyin: "jūn zǐ shí wú qiú bǎo", meaning: "君子饮食不求饱足" },
                { word: "居无求安", pinyin: "jū wú qiú ān", meaning: "居住不求舒适" },
                { word: "敏于事而慎于言", pinyin: "mǐn yú shì ér shèn yú yán", meaning: "做事勤勉说话谨慎" },
                { word: "就有道而正焉", pinyin: "jiù yǒu dào ér zhèng yān", meaning: "到有道的人那里去匡正自己" },
                { word: "人而不仁", pinyin: "rén ér bù rén", meaning: "一个人如果没有仁德" },
                { word: "如礼何", pinyin: "rú lǐ hé", meaning: "怎样对待礼呢" },
                { word: "朝闻道", pinyin: "zhāo wén dào", meaning: "早晨得知真理" },
                { word: "夕死可矣", pinyin: "xī sǐ kě yǐ", meaning: "当晚死去也可以" },
                { word: "君子喻于义", pinyin: "jūn zǐ yù yú yì", meaning: "君子明白的是义" },
                { word: "小人喻于利", pinyin: "xiǎo rén yù yú lì", meaning: "小人明白的是利" },
            ]},
            // 第2课 大学之道
            { lesson: 2, title: "大学之道", words: [
                { word: "大学之道", pinyin: "dà xué zhī dào", meaning: "大学的宗旨" },
                { word: "在明明德", pinyin: "zài míng míng dé", meaning: "在于弘扬光明的品德" },
                { word: "在亲民", pinyin: "zài qīn mín", meaning: "在于亲近爱抚民众" },
                { word: "在止于至善", pinyin: "zài zhǐ yú zhì shàn", meaning: "在于达到最完善的境界" },
                { word: "知止而后有定", pinyin: "zhī zhǐ ér hòu yǒu dìng", meaning: "知道要达到的境界才能志向坚定" },
                { word: "定而后能静", pinyin: "dìng ér hòu néng jìng", meaning: "志向坚定才能心静" },
                { word: "静而后能安", pinyin: "jìng ér hòu néng ān", meaning: "心静才能安详" },
                { word: "安而后能虑", pinyin: "ān ér hòu néng lǜ", meaning: "安详才能思虑周全" },
                { word: "虑而后能得", pinyin: "lǜ ér hòu néng dé", meaning: "思虑周全才能有所收获" },
            ]},
            // 第3课 人皆有不忍人之心
            { lesson: 3, title: "人皆有不忍人之心", words: [
                { word: "不忍人之心", pinyin: "bù rěn rén zhī xīn", meaning: "同情怜悯别人的心" },
                { word: "恻隐之心", pinyin: "cè yǐn zhī xīn", meaning: "同情心" },
                { word: "羞恶之心", pinyin: "xiū wù zhī xīn", meaning: "羞耻心" },
                { word: "辞让之心", pinyin: "cí ràng zhī xīn", meaning: "谦让心" },
                { word: "是非之心", pinyin: "shì fēi zhī xīn", meaning: "明辨是非的心" },
                { word: "仁之端也", pinyin: "rén zhī duān yě", meaning: "仁的开端" },
                { word: "义之端也", pinyin: "yì zhī duān yě", meaning: "义的开端" },
                { word: "礼之端也", pinyin: "lǐ zhī duān yě", meaning: "礼的开端" },
                { word: "智之端也", pinyin: "zhì zhī duān yě", meaning: "智的开端" },
                { word: "苟能充之", pinyin: "gǒu néng chōng zhī", meaning: "如果能够扩充它们" },
            ]},
            // 第4课 老子四章
            { lesson: 4, title: "老子四章", words: [
                { word: "三十辐共一毂", pinyin: "sān shí fú gòng yī gǔ", meaning: "三十根辐条汇集到一个毂上" },
                { word: "当其无，有车之用", pinyin: "dāng qí wú, yǒu chē zhī yòng", meaning: "有了车毂中空的地方，才有车的作用" },
                { word: "埏埴以为器", pinyin: "shān zhí yǐ wéi qì", meaning: "揉捏黏土做成器皿" },
                { word: "当其无，有器之用", pinyin: "dāng qí wú, yǒu qì zhī yòng", meaning: "有了器皿中空的地方，才有器皿的作用" },
                { word: "凿户牖以为室", pinyin: "záo hù yǒu yǐ wéi shì", meaning: "开凿门窗建造房屋" },
                { word: "当其无，有室之用", pinyin: "dāng qí wú, yǒu shì zhī yòng", meaning: "有了房屋中空的地方，才有房屋的作用" },
                { word: "故有之以为利", pinyin: "gù yǒu zhī yǐ wéi lì", meaning: "所以有给人带来便利" },
                { word: "无之以为用", pinyin: "wú zhī yǐ wéi yòng", meaning: "无发挥作用" },
            ]},
            // 第5课 五石之瓠
            { lesson: 5, title: "五石之瓠", words: [
                { word: "五石之瓠", pinyin: "wǔ shí zhī hù", meaning: "能容纳五石的大葫芦" },
                { word: "何不虑以为大樽", pinyin: "hé bù lǜ yǐ wéi dà zūn", meaning: "为什么不把它系在身上作为腰舟" },
                { word: "浮乎江湖", pinyin: "fú hū jiāng hú", meaning: "浮游在江湖上" },
                { word: "则夫子犹有蓬之心也夫", pinyin: "zé fū zǐ yóu yǒu péng zhī xīn yě fū", meaning: "那么先生您还有些不通达呀" },
                { word: "剖之以为瓢", pinyin: "pōu zhī yǐ wéi piáo", meaning: "剖开它做成瓢" },
                { word: "则瓠落无所容", pinyin: "zé hù luò wú suǒ róng", meaning: "那么瓢太大无处可容" },
            ]},
        ],
        // 选择性必修中册
        semester_2: [
            // 第1课 屈原列传
            { lesson: 1, title: "屈原列传", words: [
                { word: "屈平", pinyin: "qū píng", meaning: "屈原的字" },
                { word: "王听之不聪", pinyin: "wáng tīng zhī bù cōng", meaning: "大王听信谗言不明察" },
                { word: "谗谄之蔽明", pinyin: "chán chǎn zhī bì míng", meaning: "小人混淆黑白蒙蔽圣明" },
                { word: "邪曲之害公", pinyin: "xié qǔ zhī hài gōng", meaning: "邪恶小人危害公正" },
                { word: "方正之不容", pinyin: "fāng zhèng zhī bù róng", meaning: "端方正直的人不被容纳" },
                { word: "忧愁幽思", pinyin: "yōu chóu yōu sī", meaning: "忧愁深思" },
                { word: "离骚", pinyin: "lí sāo", meaning: "遭遇忧患" },
                { word: "人穷则反本", pinyin: "rén qióng zé fǎn běn", meaning: "人困苦就会思念根本" },
                { word: "故劳苦倦极", pinyin: "gù láo kǔ juàn jí", meaning: "所以劳苦疲倦到极点" },
                { word: "未尝不呼天也", pinyin: "wèi cháng bù hū tiān yě", meaning: "没有不喊天的" },
                { word: "疾痛惨怛", pinyin: "jí tòng cǎn dá", meaning: "痛苦悲伤" },
                { word: "未尝不呼父母也", pinyin: "wèi cháng bù hū fù mǔ yě", meaning: "没有不喊父母的" },
            ]},
            // 第2课 苏武传
            { lesson: 2, title: "苏武传", words: [
                { word: "武", pinyin: "wǔ", meaning: "苏武" },
                { word: "以相当", pinyin: "yǐ xiāng dāng", meaning: "来相抵" },
                { word: "天汉元年", pinyin: "tiān hàn yuán nián", meaning: "汉武帝天汉元年" },
                { word: "且鞮侯单于", pinyin: "jū dī hòu chán yú", meaning: "匈奴且鞮侯单于" },
                { word: "汉亦留之以相当", pinyin: "hàn yì liú zhī yǐ xiāng dāng", meaning: "汉朝也扣留匈奴使节来相抵" },
                { word: "乃遣武以中郎将使", pinyin: "nǎi qiǎn wǔ yǐ zhōng láng jiàng shǐ", meaning: "于是派苏武以中郎将身份出使" },
                { word: "置币遗单于", pinyin: "zhì bì wèi chán yú", meaning: "送礼物给单于" },
                { word: "阴相与谋劫单于母阏氏归汉", pinyin: "yīn xiāng yǔ móu jié chán yú mǔ yān zhī guī hàn", meaning: "暗中一起谋划劫持单于母亲阏氏归汉" },
                { word: "事如此，此必及我", pinyin: "shì rú cǐ, cǐ bì jí wǒ", meaning: "事情到了这个地步，一定会牵连到我" },
                { word: "见犯乃死", pinyin: "jiàn fàn nǎi sǐ", meaning: "被侮辱才死" },
            ]},
            // 第3课 过秦论（上）
            { lesson: 3, title: "过秦论（上）", words: [
                { word: "秦孝公据崤函之固", pinyin: "qín xiào gōng jù xiáo hán zhī gù", meaning: "秦孝公占据着崤山和函谷关的险固地势" },
                { word: "拥雍州之地", pinyin: "yōng yōng zhōu zhī dì", meaning: "拥有雍州的土地" },
                { word: "君臣固守以窥周室", pinyin: "jūn chén gù shǒu yǐ kuī zhōu shì", meaning: "君臣牢固守卫来伺机夺取周朝政权" },
                { word: "有席卷天下", pinyin: "yǒu xí juǎn tiān xià", meaning: "有席卷天下" },
                { word: "包举宇内", pinyin: "bāo jǔ yǔ nèi", meaning: "包举宇内" },
                { word: "囊括四海之意", pinyin: "náng kuò sì hǎi zhī yì", meaning: "囊括四海的意图" },
                { word: "并吞八荒之心", pinyin: "bìng tūn bā huāng zhī xīn", meaning: "并吞八荒的雄心" },
                { word: "当是时也", pinyin: "dāng shì shí yě", meaning: "在这个时候" },
                { word: "商君佐之", pinyin: "shāng jūn zuǒ zhī", meaning: "商鞅辅佐他" },
                { word: "内立法度", pinyin: "nèi lì fǎ dù", meaning: "对内建立法律制度" },
            ]},
            // 第4课 过秦论（中）
            { lesson: 4, title: "过秦论（中）", words: [
                { word: "二世", pinyin: "èr shì", meaning: "秦二世" },
                { word: "杀豪杰", pinyin: "shā háo jié", meaning: "杀害豪杰" },
                { word: "收天下之兵", pinyin: "shōu tiān xià zhī bīng", meaning: "收缴天下的兵器" },
                { word: "聚之咸阳", pinyin: "jù zhī xián yáng", meaning: "聚集到咸阳" },
                { word: "销锋镝", pinyin: "xiāo fēng dí", meaning: "销毁兵器" },
                { word: "铸以为金人十二", pinyin: "zhù yǐ wéi jīn rén shí èr", meaning: "铸造成十二个铜人" },
                { word: "以弱天下之民", pinyin: "yǐ ruò tiān xià zhī mín", meaning: "来削弱天下的百姓" },
                { word: "然后践华为城", pinyin: "rán hòu jiàn huá wéi chéng", meaning: "然后依据华山当作城墙" },
                { word: "因河为池", pinyin: "yīn hé wéi chí", meaning: "依据黄河当作护城河" },
                { word: "据亿丈之城", pinyin: "jù yì zhàng zhī chéng", meaning: "占据亿丈高的城墙" },
            ]},
        ],
    },

    // ==================== 高三（复习） ====================
    grade_12: {
        semester_1: [
            // 高考必背古诗文词语
            { lesson: 1, title: "高考必背古诗文", words: [
                { word: "蜉蝣", pinyin: "fú yóu", meaning: "一种昆虫" },
                { word: "须臾", pinyin: "xū yú", meaning: "片刻" },
                { word: "跬步", pinyin: "kuǐ bù", meaning: "半步" },
                { word: "骐骥", pinyin: "qí jì", meaning: "骏马" },
                { word: "驽马", pinyin: "nú mǎ", meaning: "劣马" },
                { word: "锲而不舍", pinyin: "qiè ér bù shě", meaning: "坚持不懈" },
                { word: "镂刻", pinyin: "lòu kè", meaning: "雕刻" },
                { word: "蟹螯", pinyin: "xiè áo", meaning: "螃蟹的钳子" },
                { word: "糟粕", pinyin: "zāo pò", meaning: "酒糟、豆渣" },
                { word: "浸渍", pinyin: "jìn zì", meaning: "浸泡" },
            ]},
            // 高考常考成语
            { lesson: 2, title: "高考常考成语", words: [
                { word: "按部就班", pinyin: "àn bù jiù bān", meaning: "按照一定的条理或程序" },
                { word: "白驹过隙", pinyin: "bái jū guò xì", meaning: "时间过得很快" },
                { word: "别出心裁", pinyin: "bié chū xīn cái", meaning: "独创一格" },
                { word: "不落窠臼", pinyin: "bù luò kē jiù", meaning: "不落入老套子" },
                { word: "沧海桑田", pinyin: "cāng hǎi sāng tián", meaning: "世事变化很大" },
                { word: "陈词滥调", pinyin: "chén cín làn diào", meaning: "陈旧空洞的话" },
                { word: "登峰造极", pinyin: "dēng fēng zào jí", meaning: "达到最高境界" },
                { word: "耳濡目染", pinyin: "ěr rú mù rǎn", meaning: "经常听到看到而受到影响" },
                { word: "釜底抽薪", pinyin: "fǔ dǐ chōu xīn", meaning: "从根本上解决" },
                { word: "高屋建瓴", pinyin: "gāo wū jiàn líng", meaning: "居高临下势不可挡" },
            ]},
        ],
        semester_2: [
            // 高考常考词语
            { lesson: 1, title: "高考常考词语", words: [
                { word: "遏制", pinyin: "è zhì", meaning: "制止控制" },
                { word: "遏止", pinyin: "è zhǐ", meaning: "阻止使停止" },
                { word: "暴发", pinyin: "bào fā", meaning: "突然发财或得势" },
                { word: "爆发", pinyin: "bào fā", meaning: "火山爆发或突然发作" },
                { word: "品位", pinyin: "pǐn wèi", meaning: "档次格调" },
                { word: "品味", pinyin: "pǐn wèi", meaning: "品尝体会" },
                { word: "启用", pinyin: "qǐ yòng", meaning: "开始使用" },
                { word: "起用", pinyin: "qǐ yòng", meaning: "重新任用" },
                { word: "质疑", pinyin: "zhì yí", meaning: "提出疑问" },
                { word: "置疑", pinyin: "zhì yí", meaning: "怀疑（多用于否定）" },
                { word: "不止", pinyin: "bù zhǐ", meaning: "继续不停" },
                { word: "不只", pinyin: "bù zhǐ", meaning: "不但不仅" },
                { word: "世故", pinyin: "shì gù", meaning: "处世经验" },
                { word: "事故", pinyin: "shì gù", meaning: "意外损失" },
                { word: "委屈", pinyin: "wěi qū", meaning: "受到不公平待遇" },
                { word: "委曲", pinyin: "wěi qū", meaning: "事情的底细" },
                { word: "熟悉", pinyin: "shú xī", meaning: "清楚地知道" },
                { word: "熟习", pinyin: "shú xí", meaning: "学习得很熟练" },
                { word: "度过", pinyin: "dù guò", meaning: "度过时间" },
                { word: "渡过", pinyin: "dù guò", meaning: "通过江河等" },
            ]},
            // 高考常考虚词
            { lesson: 2, title: "高考常考虚词", words: [
                { word: "以", pinyin: "yǐ", meaning: "用拿凭借" },
                { word: "而", pinyin: "ér", meaning: "但是而且" },
                { word: "乃", pinyin: "nǎi", meaning: "于是就才" },
                { word: "因", pinyin: "yīn", meaning: "于是因为" },
                { word: "则", pinyin: "zé", meaning: "就却" },
                { word: "者", pinyin: "zhě", meaning: "的人的事" },
                { word: "所", pinyin: "suǒ", meaning: "的地方" },
                { word: "之", pinyin: "zhī", meaning: "的去到" },
                { word: "其", pinyin: "qí", meaning: "他的那难道" },
                { word: "于", pinyin: "yú", meaning: "在对于到" },
            ]},
            // 高考必背文言文实词
            { lesson: 3, title: "高考必背文言文实词", words: [
                { word: "爱", pinyin: "ài", meaning: "喜爱吝啬爱护" },
                { word: "安", pinyin: "ān", meaning: "安稳怎么哪里" },
                { word: "被", pinyin: "bèi", meaning: "覆盖遭受" },
                { word: "倍", pinyin: "bèi", meaning: "加倍背叛" },
                { word: "本", pinyin: "běn", meaning: "根本原来" },
                { word: "鄙", pinyin: "bǐ", meaning: "边疆浅陋" },
                { word: "兵", pinyin: "bīng", meaning: "兵器军队" },
                { word: "病", pinyin: "bìng", meaning: "疾病困苦" },
                { word: "察", pinyin: "chá", meaning: "观察考察" },
                { word: "彻", pinyin: "chè", meaning: "通透" },
            ]},
            // 高考必背文言文实词2
            { lesson: 4, title: "高考必背文言文实词2", words: [
                { word: "乘", pinyin: "chéng", meaning: "乘坐趁着" },
                { word: "从", pinyin: "cóng", meaning: "跟随顺从" },
                { word: "当", pinyin: "dāng", meaning: "应当面对" },
                { word: "道", pinyin: "dào", meaning: "道路方法" },
                { word: "得", pinyin: "dé", meaning: "获得能够" },
                { word: "度", pinyin: "dù", meaning: "度过限度" },
                { word: "非", pinyin: "fēi", meaning: "不对不是" },
                { word: "复", pinyin: "fù", meaning: "又再恢复" },
                { word: "负", pinyin: "fù", meaning: "背负违背" },
                { word: "盖", pinyin: "gài", meaning: "大概因为" },
            ]},
            // 高考必背文言文实词3
            { lesson: 5, title: "高考必背文言文实词3", words: [
                { word: "故", pinyin: "gù", meaning: "所以原因" },
                { word: "固", pinyin: "gù", meaning: "坚固本来" },
                { word: "归", pinyin: "guī", meaning: "返回归还" },
                { word: "过", pinyin: "guò", meaning: "经过过错" },
                { word: "何", pinyin: "hé", meaning: "什么怎么" },
                { word: "恨", pinyin: "hèn", meaning: "遗憾怨恨" },
                { word: "厚", pinyin: "hòu", meaning: "厚道深厚" },
                { word: "胡", pinyin: "hú", meaning: "为什么" },
                { word: "患", pinyin: "huàn", meaning: "担心祸患" },
                { word: "或", pinyin: "huò", meaning: "有人也许" },
            ]},
            // 高考必背文言文实词4
            { lesson: 6, title: "高考必背文言文实词4", words: [
                { word: "疾", pinyin: "jí", meaning: "疾病痛恨" },
                { word: "及", pinyin: "jí", meaning: "等到赶上" },
                { word: "即", pinyin: "jí", meaning: "靠近就是" },
                { word: "既", pinyin: "jì", meaning: "已经既然" },
                { word: "假", pinyin: "jiǎ", meaning: "借如果" },
                { word: "间", pinyin: "jiān", meaning: "中间暗中" },
                { word: "见", pinyin: "jiàn", meaning: "看见被" },
                { word: "将", pinyin: "jiāng", meaning: "将要率领" },
                { word: "解", pinyin: "jiě", meaning: "解开理解" },
                { word: "尽", pinyin: "jìn", meaning: "完了全部" },
            ]},
            // 高考必背文言文实词5
            { lesson: 7, title: "高考必背文言文实词5", words: [
                { word: "举", pinyin: "jǔ", meaning: "举起推举" },
                { word: "绝", pinyin: "jué", meaning: "断绝极" },
                { word: "堪", pinyin: "kān", meaning: "忍受能够" },
                { word: "克", pinyin: "kè", meaning: "能够战胜" },
                { word: "类", pinyin: "lèi", meaning: "类似大都" },
                { word: "怜", pinyin: "lián", meaning: "怜悯可爱" },
                { word: "弥", pinyin: "mí", meaning: "更加满" },
                { word: "莫", pinyin: "mò", meaning: "没有谁不" },
                { word: "乃", pinyin: "nǎi", meaning: "于是就才" },
                { word: "内", pinyin: "nèi", meaning: "里面接纳" },
            ]},
            // 高考必背文言文实词6
            { lesson: 8, title: "高考必背文言文实词6", words: [
                { word: "期", pinyin: "qī", meaning: "约定日期" },
                { word: "奇", pinyin: "qí", meaning: "奇异零数" },
                { word: "迁", pinyin: "qiān", meaning: "迁移贬谪" },
                { word: "请", pinyin: "qǐng", meaning: "请求请允许" },
                { word: "穷", pinyin: "qióng", meaning: "困窘穷尽" },
                { word: "求", pinyin: "qiú", meaning: "寻找要求" },
                { word: "去", pinyin: "qù", meaning: "离开除去" },
                { word: "劝", pinyin: "quàn", meaning: "勉励劝说" },
                { word: "却", pinyin: "què", meaning: "退回头" },
                { word: "如", pinyin: "rú", meaning: "如同如果" },
            ]},
            // 高考必背文言文实词7
            { lesson: 9, title: "高考必背文言文实词7", words: [
                { word: "若", pinyin: "ruò", meaning: "像如果" },
                { word: "善", pinyin: "shàn", meaning: "好善于" },
                { word: "少", pinyin: "shǎo", meaning: "不多轻视" },
                { word: "涉", pinyin: "shè", meaning: "徒步过水经历" },
                { word: "胜", pinyin: "shèng", meaning: "胜利超过" },
                { word: "识", pinyin: "shí", meaning: "知道记住" },
                { word: "使", pinyin: "shǐ", meaning: "派使者" },
                { word: "是", pinyin: "shì", meaning: "正确这" },
                { word: "适", pinyin: "shì", meaning: "到恰好" },
                { word: "书", pinyin: "shū", meaning: "书写书籍" },
            ]},
            // 高考必背文言文实词8
            { lesson: 10, title: "高考必背文言文实词8", words: [
                { word: "孰", pinyin: "shú", meaning: "谁哪个" },
                { word: "属", pinyin: "shǔ", meaning: "类别连接" },
                { word: "数", pinyin: "shù", meaning: "数目多次" },
                { word: "率", pinyin: "shuài", meaning: "率领大概" },
                { word: "说", pinyin: "shuō", meaning: "说话解说" },
                { word: "私", pinyin: "sī", meaning: "私人的" },
                { word: "素", pinyin: "sù", meaning: "白色向来" },
                { word: "汤", pinyin: "tāng", meaning: "热水商汤" },
                { word: "涕", pinyin: "tì", meaning: "眼泪鼻涕" },
                { word: "图", pinyin: "tú", meaning: "图画谋求" },
            ]},
            // 高考必背文言文实词9
            { lesson: 11, title: "高考必背文言文实词9", words: [
                { word: "徒", pinyin: "tú", meaning: "步行徒弟" },
                { word: "退", pinyin: "tuì", meaning: "后退辞退" },
                { word: "亡", pinyin: "wáng", meaning: "逃跑死亡" },
                { word: "王", pinyin: "wáng", meaning: "帝王统治" },
                { word: "望", pinyin: "wàng", meaning: "向远处看期望" },
                { word: "恶", pinyin: "wù", meaning: "厌恶坏" },
                { word: "微", pinyin: "wēi", meaning: "轻微暗中" },
                { word: "闻", pinyin: "wén", meaning: "听到闻名" },
                { word: "相", pinyin: "xiāng", meaning: "互相容貌" },
                { word: "谢", pinyin: "xiè", meaning: "道歉辞别" },
            ]},
            // 高考必背文言文实词10
            { lesson: 12, title: "高考必背文言文实词10", words: [
                { word: "信", pinyin: "xìn", meaning: "信用确实" },
                { word: "兴", pinyin: "xīng", meaning: "兴起兴盛" },
                { word: "行", pinyin: "xíng", meaning: "走实行" },
                { word: "幸", pinyin: "xìng", meaning: "幸运希望" },
                { word: "修", pinyin: "xiū", meaning: "修理整治" },
                { word: "许", pinyin: "xǔ", meaning: "答应处所" },
                { word: "续", pinyin: "xù", meaning: "连接继续" },
                { word: "寻", pinyin: "xún", meaning: "寻找不久" },
                { word: "遗", pinyin: "yí", meaning: "丢失送给" },
                { word: "易", pinyin: "yì", meaning: "交换改变" },
            ]},
            // 高考必背文言文实词11
            { lesson: 13, title: "高考必背文言文实词11", words: [
                { word: "意", pinyin: "yì", meaning: "心意料想" },
                { word: "引", pinyin: "yǐn", meaning: "拉引导" },
                { word: "有", pinyin: "yǒu", meaning: "存在" },
                { word: "又", pinyin: "yòu", meaning: "再" },
                { word: "予", pinyin: "yǔ", meaning: "给" },
                { word: "与", pinyin: "yǔ", meaning: "和给" },
                { word: "欲", pinyin: "yù", meaning: "想要" },
                { word: "缘", pinyin: "yuán", meaning: "沿着原因" },
                { word: "远", pinyin: "yuǎn", meaning: "距离长疏远" },
                { word: "曰", pinyin: "yuē", meaning: "说" },
            ]},
            // 高考必背文言文实词12
            { lesson: 14, title: "高考必背文言文实词12", words: [
                { word: "云", pinyin: "yún", meaning: "说" },
                { word: "再", pinyin: "zài", meaning: "第二次又" },
                { word: "造", pinyin: "zào", meaning: "到制造" },
                { word: "知", pinyin: "zhī", meaning: "知道主持" },
                { word: "致", pinyin: "zhì", meaning: "送达招致" },
                { word: "质", pinyin: "zhì", meaning: "本质人质" },
                { word: "治", pinyin: "zhì", meaning: "治理惩处" },
                { word: "诸", pinyin: "zhū", meaning: "众之于" },
                { word: "走", pinyin: "zǒu", meaning: "跑" },
                { word: "足", pinyin: "zú", meaning: "脚够" },
            ]},
            // 高考必背文言文实词13
            { lesson: 15, title: "高考必背文言文实词13", words: [
                { word: "卒", pinyin: "zú", meaning: "士兵终" },
                { word: "作", pinyin: "zuò", meaning: "兴起做" },
                { word: "坐", pinyin: "zuò", meaning: "坐下因为" },
                { word: "左", pinyin: "zuǒ", meaning: "左边" },
                { word: "尊", pinyin: "zūn", meaning: "地位高尊重" },
                { word: "遵", pinyin: "zūn", meaning: "依照" },
                { word: "昨", pinyin: "zuó", meaning: "昨天" },
                { word: "作", pinyin: "zuò", meaning: "兴起制造" },
                { word: "座", pinyin: "zuò", meaning: "座位" },
                { word: "做", pinyin: "zuò", meaning: "从事" },
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

    for (const [grade, semesters] of Object.entries(SENIOR_HIGH_WORDS)) {
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
    for (const [grade, semesters] of Object.entries(SENIOR_HIGH_WORDS)) {
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
    SENIOR_HIGH_WORDS,
    getAllWords,
    getGradeStats,
};

// 命令行运行时打印统计
if (require.main === module) {
    const stats = getGradeStats();
    console.log("部编版高中10-12年级教材词语统计：");
    for (const [grade, count] of Object.entries(stats)) {
        console.log(`  ${grade}: ${count}词`);
    }
    console.log(`\n总计：${stats.total}词`);
}
