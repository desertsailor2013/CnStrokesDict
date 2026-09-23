# CnStrokesDict - 汉字笔画字典

一款 Android 离线汉字字典应用，支持笔画顺序动画、语音查字、拼音检索、词语听写等功能。

## 当前版本

**V1.1** - 联网更新、更多词库、词语听写

| 功能 | 状态 |
|------|------|
| 小学1-6年级生字库（549字） | ✅ 完成 |
| 初中7-9年级词语（94词） | ✅ 完成 |
| 部编版教材词语表（404词） | ✅ 完成 |
| 自扩充词库功能 | ✅ 完成 |
| 二维码分享词库 | ✅ 完成 |
| 联网更新模式 | ✅ 完成 |
| 词语听写功能 | ✅ 完成 |

详细进度请查看 [项目状态](docs/项目状态.md)

---

## 功能特性

### 核心功能
- **笔画顺序动画**：基于 Hanzi Writer SVG 路径数据，逐笔动态演示汉字书写顺序
- **离线字典**：内置字库，无需联网即可查询汉字信息
- **语音查字**：支持 Vosk 离线中文识别 + 系统语音识别回退
- **汉字详情**：拼音、偏旁、说文解字、释义、组词例句、成语、字形演进
- **拼音搜索**：支持按拼音、汉字进行模糊检索
- **米字格显示**：笔画在米字格中规范展示

### V1.0 新增功能
- **完整字库**：小学1-6年级生字（549字，含笔画、拼音、释义）
- **教材词语**：部编版小学+初中教材词语（404词）
- **自扩充词库**：教师/学生可创建、导入、导出词库
- **二维码分享**：生成二维码分享词库，扫码导入

### V1.1 新增功能
- **联网更新**：支持在线检查更新、下载更新数据
- **更多词库**：初中7-9年级词语数据
- **词语听写**：TTS发音、听写练习、错词复习

## 技术栈

| 技术 | 说明 |
|------|------|
| Kotlin | 主要开发语言 |
| Jetpack Compose | 声明式 UI 框架 |
| Room + FTS5 | 本地数据库 + 全文搜索 |
| Vosk | 离线中文语音识别 |
| Coil | 图片异步加载 |
| Navigation Compose | 页面导航 |
| Kotlin Serialization | JSON 序列化 |
| Retrofit + OkHttp | 网络请求 |
| ZXing | 二维码生成 |
| Android TTS | 语音合成 |

## 项目结构

```
CnStrokesDict/
├── app/
│   └── src/main/
│       ├── assets/dictionary/
│       │   ├── characters.json         # 单字数据（549字）
│       │   └── textbook_words.json     # 词语数据（404词）
│       └── java/com/cnstrokesdict/app/
│           ├── MainActivity.kt              # 主 Activity，导航入口
│           ├── data/
│           │   ├── DictionaryModels.kt      # 数据模型定义
│           │   ├── DictionaryRepository.kt  # 数据仓库层
│           │   ├── SearchTextUtil.kt        # 搜索文本工具
│           │   ├── WordPackageManager.kt    # 词库包管理器
│           │   ├── remote/
│           │   │   ├── NetworkClient.kt     # 网络客户端
│           │   │   ├── UpdateApi.kt         # 更新API接口
│           │   │   └── UpdateModels.kt      # 更新数据模型
│           │   ├── update/
│           │   │   └── UpdateManager.kt     # 更新管理器
│           │   └── db/
│           │       ├── DictionaryDatabase.kt  # Room 数据库
│           │       ├── DictionaryDao.kt       # 数据访问对象
│           │       ├── DictMeta.kt            # 字典元数据实体
│           │       ├── DictMetaFts.kt         # FTS 全文搜索实体
│           │       ├── DictPayload.kt         # 字典详情实体
│           │       ├── WordEntity.kt          # 词语实体
│           │       ├── WordPackageEntity.kt   # 词库包实体
│           │       ├── UserSettingsEntity.kt  # 用户设置实体
│           │       └── WordDao.kt             # 词语数据访问
│           ├── speech/
│           │   └── VoskChineseAsr.kt       # Vosk 离线语音识别
│           ├── ui/
│           │   ├── home/
│           │   │   └── SearchScreen.kt     # 搜索页面
│           │   ├── detail/
│           │   │   ├── DetailScreen.kt     # 汉字详情页面
│           │   │   ├── StrokeCanvas.kt     # 笔画绘制 Canvas
│           │   │   └── MiZiGeStrokeBox.kt  # 米字格容器
│           │   ├── wordmanager/
│           │   │   └── WordPackageManagerScreen.kt  # 词库管理界面
│           │   ├── qrshare/
│           │   │   └── QrShareScreen.kt    # 二维码分享界面
│           │   ├── qrscan/
│           │   │   └── QrScanScreen.kt     # 扫码导入界面
│           │   ├── update/
│           │   │   └── UpdateScreen.kt     # 检查更新界面
│           │   ├── dictation/
│           │   │   └── DictationScreen.kt  # 词语听写界面
│           │   └── theme/
│           │       └── Theme.kt            # Material3 主题
│           ├── util/
│           │   ├── VoiceQueryParser.kt     # 语音查询解析
│           │   ├── QrCodeUtil.kt           # 二维码工具类
│           │   ├── TtsManager.kt           # TTS语音管理器
│           │   ├── DictationManager.kt     # 听写流程管理器
│           │   ├── PerformanceMonitor.kt   # 性能监控工具
│           │   └── MemoryMonitor.kt        # 内存监控工具
│           └── vm/
│               ├── DictionaryViewModel.kt  # 字典ViewModel
│               ├── WordPackageManagerViewModel.kt  # 词库管理ViewModel
│               ├── QrShareViewModel.kt     # 二维码分享ViewModel
│               ├── QrScanViewModel.kt      # 扫码导入ViewModel
│               ├── UpdateViewModel.kt      # 更新界面ViewModel
│               └── DictationViewModel.kt   # 听写界面ViewModel
├── scripts/                                # 构建脚本
│   ├── primary_school_chars.js             # 小学1-6年级生字清单
│   ├── fetch_hanzi_data.js                 # 批量获取笔画数据
│   ├── build_full_dictionary.js            # 构建完整字库JSON
│   ├── textbook_words.js                   # 小学词语表
│   ├── junior_high_words.js                # 初中词语表
│   ├── merge_words.js                      # 合并词语数据
│   ├── hanzi_raw/                          # 原始笔画数据
│   ├── dict_data/                          # 构建的字库数据
│   └── install_android_sdk.ps1             # SDK 安装脚本
├── docs/                                   # 项目文档
│   ├── V1设计方案.md                        # V1版本设计方案
│   ├── V1.1设计方案.md                      # V1.1版本设计方案
│   └── 项目状态.md                          # 项目进度跟踪
├── build.gradle.kts                        # 根构建脚本
├── FAQ.md                                  # 常见问题解答
└── gradle.properties                       # Gradle 配置
```

## 环境要求

- Android Studio Hedgehog (2023.1) 或更高版本
- JDK 17+
- Android SDK 35
- Kotlin 2.0.21
- Gradle 8.7

## 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd CnStrokesDict
```

### 2. 配置 Android SDK

若未安装 SDK，可使用仓库提供的脚本：

```powershell
cd scripts
powershell -ExecutionPolicy Bypass -File .\install_android_sdk.ps1
```

或在 Android Studio 中配置 SDK 路径。

### 3. 构建运行

```bash
# 构建 Debug APK
./gradlew assembleDebug

# 或直接在 Android Studio 中点击 Run
```

### 4. 构建字库数据（可选）

如需重新构建字库：

```bash
cd scripts
node primary_school_chars.js          # 查看生字统计
node fetch_hanzi_data.js              # 获取笔画数据（需联网）
node build_full_dictionary.js         # 构建字库JSON
node junior_high_words.js             # 查看初中词语统计
node merge_words.js                   # 合并小学和初中词语
```

生成的数据文件位于 `app/src/main/assets/dictionary/`。

## 数据库设计

### V1.0 表结构
| 表名 | 说明 |
|------|------|
| `dict_meta` | 字典元数据（汉字、拼音、释义预览、搜索文档） |
| `dict_payload` | 字典详情 JSON（完整字条信息） |
| `dict_meta_fts` | FTS5 全文搜索虚拟表 |

### V1.0 新增表结构
| 表名 | 说明 |
|------|------|
| `words` | 词语表（词语、拼音、释义、年级等） |
| `words_fts` | 词语FTS5全文搜索虚拟表 |
| `word_packages` | 词库包表（名称、描述、作者等） |
| `user_settings` | 用户设置表 |

## 语音识别

- **首选**：Vosk 离线中文识别（首次使用自动下载模型约 50MB）
- **回退**：系统语音识别（需设备支持）

语音查询格式：「XX 的字」，如「文字的字」。

## 字典数据来源

- 笔画数据：Hanzi Writer 项目（Apache-2.0）
- 字形演进图片：百度百科等公开资源
- 释义数据：自定义整理
- 词语数据：部编版语文教材

## 常见问题

详见 [FAQ.md](FAQ.md)，涵盖：
- Gradle 下载超时解决方案
- IDE 源码包解析问题
- Android SDK 安装配置
- 模拟器使用指南
- 黑屏问题处理
- 笔画动画显示问题

## 相关文档

- [V1设计方案](docs/V1设计方案.md) - V1版本详细设计方案
- [V1.1设计方案](docs/V1.1设计方案.md) - V1.1版本详细设计方案
- [项目状态](docs/项目状态.md) - 项目进度跟踪
- [常见问题](FAQ.md) - 构建和运行问题解答

## 许可证

本项目供学习参考使用。笔画数据基于 [Hanzi Writer](https://github.com/chanind/hanzi-writer) 项目（Apache-2.0 许可）。

## 相关链接

- [Hanzi Writer GitHub](https://github.com/chanind/hanzi-writer)
- [Vosk 语音识别](https://alphacephei.com/vosk/)
- [Android Jetpack Compose](https://developer.android.com/jetpack/compose)
