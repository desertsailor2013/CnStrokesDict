#!/usr/bin/env node
/**
 * 扩充成语数据
 * 在现有基础上添加更多常用成语
 */

const additionalIdioms = {
    // ==================== 学习成语 ====================
    study: [
        { idiom: "悬梁刺股", pinyin: "xuán liáng cì gǔ", meaning: "形容刻苦学习", source: "《太平御览》", examples: ["他悬梁刺股，终于考上了大学。"], synonyms: ["凿壁偷光", "囊萤映雪"], antonyms: ["不学无术", "胸无点墨"], difficulty: 2 },
        { idiom: "凿壁偷光", pinyin: "záo bì tōu guāng", meaning: "形容家贫而读书刻苦", source: "《西京杂记》", examples: ["他凿壁偷光，刻苦学习。"], synonyms: ["悬梁刺股", "囊萤映雪"], antonyms: ["不学无术", "胸无点墨"], difficulty: 2 },
        { idiom: "囊萤映雪", pinyin: "náng yíng yìng xuě", meaning: "形容家境贫寒仍刻苦读书", source: "《晋书·车胤传》", examples: ["他囊萤映雪，勤奋学习。"], synonyms: ["悬梁刺股", "凿壁偷光"], antonyms: ["不学无术", "胸无点墨"], difficulty: 2 },
        { idiom: "废寝忘食", pinyin: "fèi qǐn wàng shí", meaning: "形容专心致志地学习或工作", source: "《列子·开瑞篇》", examples: ["他废寝忘食地研究这个问题。"], synonyms: ["夜以继日", "通宵达旦"], antonyms: ["好吃懒做", "游手好闲"], difficulty: 1 },
        { idiom: "学富五车", pinyin: "xué fù wǔ chē", meaning: "形容读书多，学问大", source: "《庄子·天下》", examples: ["他学富五车，知识渊博。"], synonyms: ["博学多才", "满腹经纶"], antonyms: ["不学无术", "胸无点墨"], difficulty: 2 },
        { idiom: "才高八斗", pinyin: "cái gāo bā dǒu", meaning: "形容文才非常高", source: "《释常谈·八斗之才》", examples: ["他才高八斗，文采飞扬。"], synonyms: ["学富五车", "博学多才"], antonyms: ["才疏学浅", "胸无点墨"], difficulty: 2 },
        { idiom: "博学多才", pinyin: "bó xué duō cái", meaning: "学识渊博，有多方面的才能", source: "《晋书·郤诜传》", examples: ["他博学多才，什么都懂。"], synonyms: ["学富五车", "才高八斗"], antonyms: ["不学无术", "胸无点墨"], difficulty: 1 },
        { idiom: "勤能补拙", pinyin: "qín néng bǔ zhuō", meaning: "勤奋能够弥补天资的不足", source: "《咏史诗》", examples: ["勤能补拙，只要努力就能成功。"], synonyms: ["笨鸟先飞", "熟能生巧"], antonyms: ["好吃懒做", "游手好闲"], difficulty: 1 },
        { idiom: "笨鸟先飞", pinyin: "bèn niǎo xiān fēi", meaning: "比喻能力差的人怕落后，做事比别人先动手", source: "《陈母教子》", examples: ["我笨鸟先飞，提前开始学习。"], synonyms: ["勤能补拙", "熟能生巧"], antonyms: ["自暴自弃", "不思进取"], difficulty: 1 },
        { idiom: "熟能生巧", pinyin: "shú néng shēng qiǎo", meaning: "熟练了就能产生巧办法", source: "《欧阳文忠公文集》", examples: ["熟能生巧，多练习就会了。"], synonyms: ["勤能补拙", "笨鸟先飞"], antonyms: ["半途而废", "浅尝辄止"], difficulty: 1 },
    ],

    // ==================== 友情成语 ====================
    friendship: [
        { idiom: "志同道合", pinyin: "zhì tóng dào hé", meaning: "志向相同，道路一致", source: "《三国志·魏书·陈思王植传》", examples: ["他们志同道合，一起创业。"], synonyms: ["情投意合", "心心相印"], antonyms: ["分道扬镳", "貌合神离"], difficulty: 1 },
        { idiom: "情投意合", pinyin: "qíng tóu yì hé", meaning: "形容双方思想感情很融洽", source: "《西游记》", examples: ["他们情投意合，成了好朋友。"], synonyms: ["志同道合", "心心相印"], antonyms: ["格格不入", "貌合神离"], difficulty: 1 },
        { idiom: "心心相印", pinyin: "xīn xīn xiāng yìn", meaning: "形容彼此思想感情完全投合", source: "《六祖大师法宝坛经》", examples: ["他们夫妻俩心心相印，恩爱无比。"], synonyms: ["情投意合", "志同道合"], antonyms: ["貌合神离", "同床异梦"], difficulty: 1 },
        { idiom: "推心置腹", pinyin: "tuī xīn zhì fù", meaning: "比喻真心待人", source: "《后汉书·光武帝纪》", examples: ["他推心置腹地和我谈心。"], synonyms: ["开诚布公", "坦诚相待"], antonyms: ["虚情假意", "口是心非"], difficulty: 1 },
        { idiom: "开诚布公", pinyin: "kāi chéng bù gōng", meaning: "指以诚心待人，坦白无私", source: "《三国志·蜀书·诸葛亮传论》", examples: ["我们开诚布公地谈一谈吧。"], synonyms: ["推心置腹", "坦诚相待"], antonyms: ["虚情假意", "口是心非"], difficulty: 1 },
        { idiom: "肝胆相照", pinyin: "gān dǎn xiāng zhào", meaning: "比喻以真心相见", source: "《史记·淮阴侯列传》", examples: ["我们肝胆相照，互相帮助。"], synonyms: ["推心置腹", "开诚布公"], antonyms: ["虚情假意", "尔虞我诈"], difficulty: 1 },
        { idiom: "患难与共", pinyin: "huàn nàn yǔ gòng", meaning: "共同承担困难和危险", source: "《史记·越王勾践世家》", examples: ["我们患难与共，一起度过难关。"], synonyms: ["同甘共苦", "风雨同舟"], antonyms: ["同床异梦", "各奔东西"], difficulty: 1 },
        { idiom: "同甘共苦", pinyin: "tóng gān gòng kǔ", meaning: "共同享受幸福，共同担当艰苦", source: "《战国策·燕策一》", examples: ["夫妻同甘共苦，生活幸福。"], synonyms: ["患难与共", "风雨同舟"], antonyms: ["同床异梦", "各奔东西"], difficulty: 1 },
        { idiom: "风雨同舟", pinyin: "fēng yǔ tóng zhōu", meaning: "比喻共同经历患难", source: "《孙子·九地》", examples: ["我们风雨同舟，共渡难关。"], synonyms: ["患难与共", "同甘共苦"], antonyms: ["同床异梦", "各奔东西"], difficulty: 1 },
        { idiom: "莫逆之交", pinyin: "mò nì zhī jiāo", meaning: "指非常要好的朋友", source: "《庄子·大宗师》", examples: ["他们是莫逆之交，关系很好。"], synonyms: ["生死之交", "刎颈之交"], antonyms: ["泛泛之交", "酒肉朋友"], difficulty: 2 },
    ],

    // ==================== 成功成语 ====================
    success: [
        { idiom: "一举成功", pinyin: "yī jǔ chéng gōng", meaning: "一次行动就取得成功", source: "《后汉书·张骞传》", examples: ["他一举成功，获得了冠军。"], synonyms: ["马到成功", "旗开得胜"], antonyms: ["屡战屡败", "一败涂地"], difficulty: 1 },
        { idiom: "旗开得胜", pinyin: "qí kāi dé shèng", meaning: "比喻事情刚一开始就取得成功", source: "《元曲选·郑廷为》", examples: ["祝你旗开得胜，马到成功！"], synonyms: ["马到成功", "一举成功"], antonyms: ["屡战屡败", "一败涂地"], difficulty: 1 },
        { idiom: "马到成功", pinyin: "mǎ dào chéng gōng", meaning: "形容工作刚开始就取得成功", source: "《三国演义》", examples: ["祝你马到成功，旗开得胜！"], synonyms: ["旗开得胜", "一举成功"], antonyms: ["屡战屡败", "一败涂地"], difficulty: 1 },
        { idiom: "大功告成", pinyin: "dà gōng gào chéng", meaning: "巨大工程或重要任务宣告完成", source: "《汉书·王莽传》", examples: ["这项工程终于大功告成了。"], synonyms: ["功成名就", "功德圆满"], antonyms: ["功败垂成", "半途而废"], difficulty: 1 },
        { idiom: "功成名就", pinyin: "gōng chéng míng jiù", meaning: "功业建立了，名声也有了", source: "《墨子·修身》", examples: ["他功成名就，实现了人生目标。"], synonyms: ["大功告成", "功德圆满"], antonyms: ["一事无成", "壮志未酬"], difficulty: 1 },
        { idiom: "功德圆满", pinyin: "gōng dé yuán mǎn", meaning: "比喻事情顺利完成", source: "《六祖大师法宝坛经》", examples: ["这项任务终于功德圆满了。"], synonyms: ["大功告成", "功成名就"], antonyms: ["功败垂成", "半途而废"], difficulty: 2 },
        { idiom: "功成名遂", pinyin: "gōng chéng míng suì", meaning: "功业建立了，名声也有了", source: "《墨子·修身》", examples: ["他功成名遂，衣锦还乡。"], synonyms: ["功成名就", "大功告成"], antonyms: ["一事无成", "壮志未酬"], difficulty: 2 },
        { idiom: "名利双收", pinyin: "míng lì shuāng shōu", meaning: "既得名声又得利益", source: "《官场现形记》", examples: ["他名利双收，事业有成。"], synonyms: ["功成名就", "名利兼收"], antonyms: ["名落孙山", "一无所获"], difficulty: 1 },
        { idiom: "飞黄腾达", pinyin: "fēi huáng téng dá", meaning: "比喻人的官职地位很快升迁", source: "《符读书城南》", examples: ["他飞黄腾达，步步高升。"], synonyms: ["平步青云", "扶摇直上"], antonyms: ["一落千丈", "江河日下"], difficulty: 1 },
        { idiom: "平步青云", pinyin: "píng bù qīng yún", meaning: "比喻一下子就升到了很高的地位", source: "《史记·范睢蔡泽列传》", examples: ["他平步青云，升职很快。"], synonyms: ["飞黄腾达", "扶摇直上"], antonyms: ["一落千丈", "江河日下"], difficulty: 1 },
    ],

    // ==================== 勤奋成语 ====================
    diligence: [
        { idiom: "勤勤恳恳", pinyin: "qín qín kěn kěn", meaning: "形容勤劳踏实", source: "《汉书·王莽传》", examples: ["他勤勤恳恳地工作。"], synonyms: ["兢兢业业", "任劳任怨"], antonyms: ["好吃懒做", "游手好闲"], difficulty: 1 },
        { idiom: "兢兢业业", pinyin: "jīng jīng yè yè", meaning: "形容做事小心谨慎", source: "《诗经·大雅·云汉》", examples: ["他兢兢业业地完成每一项任务。"], synonyms: ["勤勤恳恳", "任劳任怨"], antonyms: ["敷衍了事", "马马虎虎"], difficulty: 1 },
        { idiom: "任劳任怨", pinyin: "rèn láo rèn yuàn", meaning: "比喻做事不辞劳苦，不怕埋怨", source: "《盐铁论·刺权》", examples: ["她任劳任怨，默默付出。"], synonyms: ["勤勤恳恳", "兢兢业业"], antonyms: ["拈轻怕重", "挑肥拣瘦"], difficulty: 1 },
        { idiom: "废寝忘食", pinyin: "fèi qǐn wàng shí", meaning: "形容专心致志地学习或工作", source: "《列子·开瑞篇》", examples: ["他废寝忘食地研究这个问题。"], synonyms: ["夜以继日", "通宵达旦"], antonyms: ["好吃懒做", "游手好闲"], difficulty: 1 },
        { idiom: "夜以继日", pinyin: "yè yǐ jì rì", meaning: "晚上接着白天，日夜不停", source: "《孟子·离娄下》", examples: ["他夜以继日地工作。"], synonyms: ["废寝忘食", "通宵达旦"], antonyms: ["好吃懒做", "游手好闲"], difficulty: 1 },
        { idiom: "通宵达旦", pinyin: "tōng xiāo dá dàn", meaning: "整整一夜，从天黑到天亮", source: "《左传·僖公二十三年》", examples: ["他通宵达旦地学习。"], synonyms: ["夜以继日", "废寝忘食"], antonyms: ["好吃懒做", "游手好闲"], difficulty: 1 },
        { idiom: "闻鸡起舞", pinyin: "wén jī qǐ wǔ", meaning: "比喻有志之人及时奋发努力", source: "《晋书·祖逖传》", examples: ["他闻鸡起舞，刻苦锻炼。"], synonyms: ["发愤图强", "卧薪尝胆"], antonyms: ["好吃懒做", "游手好闲"], difficulty: 2 },
        { idiom: "卧薪尝胆", pinyin: "wò xīn cháng dǎn", meaning: "形容人刻苦自励，发奋图强", source: "《史记·越王勾践世家》", examples: ["他卧薪尝胆，终于成功。"], synonyms: ["闻鸡起舞", "发愤图强"], antonyms: ["好吃懒做", "游手好闲"], difficulty: 2 },
        { idiom: "发愤图强", pinyin: "fā fèn tú qiáng", meaning: "下定决心，努力谋求强盛", source: "《史记·平原君虞卿列传》", examples: ["他发愤图强，振兴中华。"], synonyms: ["卧薪尝胆", "闻鸡起舞"], antonyms: ["好吃懒做", "游手好闲"], difficulty: 1 },
        { idiom: "自强不息", pinyin: "zì qiáng bù xī", meaning: "自觉地努力向上，永不松懈", source: "《周易·乾》", examples: ["他自强不息，不断进步。"], synonyms: ["发愤图强", "奋发图强"], antonyms: ["自暴自弃", "不思进取"], difficulty: 1 },
    ],

    // ==================== 智慧成语 ====================
    wisdom: [
        { idiom: "足智多谋", pinyin: "zú zhì duō móu", meaning: "富有智慧，善于谋划", source: "《三国演义》", examples: ["他足智多谋，多次化解危机。"], synonyms: ["聪明才智", "神机妙算"], antonyms: ["愚不可及", "束手无策"], difficulty: 1 },
        { idiom: "神机妙算", pinyin: "shén jī miào suàn", meaning: "形容预料准确，善于估计形势", source: "《三国演义》", examples: ["诸葛亮神机妙算，料事如神。"], synonyms: ["足智多谋", "料事如神"], antonyms: ["愚不可及", "束手无策"], difficulty: 2 },
        { idiom: "料事如神", pinyin: "liào shì rú shén", meaning: "形容预料事情非常准确", source: "《三国演义》", examples: ["他料事如神，每次都猜对了。"], synonyms: ["神机妙算", "未卜先知"], antonyms: ["愚不可及", "束手无策"], difficulty: 2 },
        { idiom: "未卜先知", pinyin: "wèi bǔ xiān zhī", meaning: "没有占卜就能预先知道", source: "《东周列国志》", examples: ["他未卜先知，提前做好了准备。"], synonyms: ["料事如神", "神机妙算"], antonyms: ["愚不可及", "后知后觉"], difficulty: 2 },
        { idiom: "足智多谋", pinyin: "zú zhì duō móu", meaning: "富有智慧，善于谋划", source: "《三国演义》", examples: ["他足智多谋，多次化解危机。"], synonyms: ["聪明才智", "神机妙算"], antonyms: ["愚不可及", "束手无策"], difficulty: 1 },
        { idiom: "聪明才智", pinyin: "cōng míng cái zhì", meaning: "指智力发达，记忆和理解能力强", source: "《颜氏家训·勉学》", examples: ["他充分发挥自己的聪明才智。"], synonyms: ["足智多谋", "才智过人"], antonyms: ["愚不可及", "才疏学浅"], difficulty: 1 },
        { idiom: "才智过人", pinyin: "cái zhì guò rén", meaning: "才能和智慧超过一般人", source: "《汉书·高帝纪》", examples: ["他才智过人，出类拔萃。"], synonyms: ["聪明才智", "才高八斗"], antonyms: ["愚不可及", "才疏学浅"], difficulty: 1 },
        { idiom: "学识渊博", pinyin: "xué shí yuān bó", meaning: "学问深广", source: "《汉书·董仲舒传》", examples: ["他学识渊博，令人敬佩。"], synonyms: ["博学多才", "学富五车"], antonyms: ["才疏学浅", "胸无点墨"], difficulty: 1 },
        { idiom: "见多识广", pinyin: "jiàn duō shí guǎng", meaning: "见过的多，知道的广", source: "《古今小说》", examples: ["他见多识广，经验丰富。"], synonyms: ["博学多才", "见闻广博"], antonyms: ["孤陋寡闻", "井底之蛙"], difficulty: 1 },
        { idiom: "博闻强识", pinyin: "bó wén qiáng zhì", meaning: "见闻广博，记忆力强", source: "《礼记·曲礼上》", examples: ["他博闻强识，知识渊博。"], synonyms: ["见多识广", "博学多才"], antonyms: ["孤陋寡闻", "才疏学浅"], difficulty: 2 },
    ],
};

module.exports = additionalIdioms;
