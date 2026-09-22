# CnStrokesDict - 汉字笔画字典

一款 Android 离线汉字字典应用，支持笔画顺序动画、语音查字、拼音检索等功能。

## 当前版本

**V1.0 开发中** - 补齐小学1~6年级生字和常用字词

| 功能 | 状态 |
|------|------|
| 小学1-6年级生字库（约2500字） | ⏳ 开发中 |
| 部编版教材词语表（约6600词） | ⏳ 开发中 |
| 自扩充词库功能 | ⏳ 开发中 |
| 二维码分享词库 | ⏳ 开发中 |

详细进度请查看 [项目状态](docs/项目状态.md)

---

## 功能特性

- **笔画顺序动画**：基于 Hanzi Writer SVG 路径数据，逐笔动态演示汉字书写顺序
- **离线字典**：内置字库，无需联网即可查询汉字信息
- **语音查字**：支持 Vosk 离线中文识别 + 系统语音识别回退
- **汉字详情**：拼音、偏旁、说文解字、释义、组词例句、成语、字形演进
- **拼音搜索**：支持按拼音、汉字进行模糊检索
- **米字格显示**：笔画在米字格中规范展示

## V1 新增功能（规划中）

- **完整字库**：小学1-6年级全部生字（约2500字）
- **教材词语**：部编版1-6年级二字/三字词语
- **自扩充词库**：教师/学生可创建、导入、导出词库
- **二维码分享**：生成二维码分享词库，扫码导入

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

## 项目结构

```
CnStrokesDict/
├── app/
│   └── src/main/java/com/cnstrokesdict/app/
│       ├── MainActivity.kt              # 主 Activity，导航入口
│       ├── data/
│       │   ├── DictionaryModels.kt      # 数据模型定义
│       │   ├── DictionaryRepository.kt  # 数据仓库层
│       │   ├── SearchTextUtil.kt        # 搜索文本工具
│       │   └── db/
│       │       ├── DictionaryDatabase.kt  # Room 数据库
│       │       ├── DictionaryDao.kt       # 数据访问对象
│       │       ├── DictMeta.kt            # 字典元数据实体
│       │       ├── DictMetaFts.kt         # FTS 全文搜索实体
│       │       └── DictPayload.kt         # 字典详情实体
│       ├── speech/
│       │   └── VoskChineseAsr.kt       # Vosk 离线语音识别
│       ├── ui/
│       │   ├── home/
│       │   │   └── SearchScreen.kt     # 搜索页面
│       │   ├── detail/
│       │   │   ├── DetailScreen.kt     # 汉字详情页面
│       │   │   ├── StrokeCanvas.kt     # 笔画绘制 Canvas
│       │   │   └── MiZiGeStrokeBox.kt  # 米字格容器
│       │   └── theme/
│       │       └── Theme.kt            # Material3 主题
│       ├── util/
│       │   └── VoiceQueryParser.kt     # 语音查询解析
│       └── vm/
│           └── DictionaryViewModel.kt  # ViewModel 层
├── scripts/
│   ├── build_characters_json.py        # 构建字典 JSON
│   ├── build_characters_bulk.py        # 批量构建字典
│   ├── character_build_util.py         # 构建工具函数
│   ├── meta_rich.py                    # 字典元数据
│   ├── hanzi_raw/                      # 原始笔画数据
│   └── install_android_sdk.ps1         # SDK 安装脚本
├── docs/                               # 项目文档
│   ├── V1设计方案.md                    # V1版本设计方案
│   └── 项目状态.md                      # 项目进度跟踪
├── build.gradle.kts                    # 根构建脚本
├── FAQ.md                              # 常见问题解答
└── gradle.properties                   # Gradle 配置
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

### 4. 生成字典数据（可选）

如需扩展字库：

```bash
cd scripts
python build_characters_json.py
```

生成的 `characters.json` 位于 `app/src/main/assets/dictionary/`。

## 数据库设计

| 表名 | 说明 |
|------|------|
| `dict_meta` | 字典元数据（汉字、拼音、释义预览、搜索文档） |
| `dict_payload` | 字典详情 JSON（完整字条信息） |
| `dict_meta_fts` | FTS5 全文搜索虚拟表 |

## 语音识别

- **首选**：Vosk 离线中文识别（首次使用自动下载模型约 50MB）
- **回退**：系统语音识别（需设备支持）

语音查询格式：「XX 的字」，如「文字的字」。

## 字典数据来源

- 笔画数据：Hanzi Writer 项目（Apache-2.0）
- 字形演进图片：百度百科等公开资源
- 释义数据：自定义整理

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
- [项目状态](docs/项目状态.md) - 项目进度跟踪
- [常见问题](FAQ.md) - 构建和运行问题解答

## 许可证

本项目供学习参考使用。笔画数据基于 [Hanzi Writer](https://github.com/chanind/hanzi-writer) 项目（Apache-2.0 许可）。

## 相关链接

- [Hanzi Writer GitHub](https://github.com/chanind/hanzi-writer)
- [Vosk 语音识别](https://alphacephei.com/vosk/)
- [Android Jetpack Compose](https://developer.android.com/jetpack/compose)
