# 工程构建 FAQ

本文记录本仓库在**构建 / 同步 Gradle**、**运行与调试界面**、**笔画 Canvas 绘制**等环节遇到过的问题及处理方式，便于后续排查。

---

## Gradle 分发包下载超时（`SocketTimeoutException`）

### 现象

同步或构建时出现类似报错：

- `Could not install Gradle distribution from 'https://services.gradle.org/distributions/gradle-8.7-bin.zip'.`
- `Reason: java.net.SocketTimeoutException: Read timed out`

### 原因说明

从 Gradle 官方地址 `services.gradle.org` 拉取 `gradle-*-bin.zip` 时，网络不稳定或跨境链路较慢，导致读取超时。与项目代码本身无关。

### 已采用的解决办法（本仓库）

1. **改用国内镜像的同一发行包**  
   在 `gradle/wrapper/gradle-wrapper.properties` 中，将 `distributionUrl` 指向腾讯云 Gradle 镜像。本仓库使用 **`gradle-8.7-all.zip`**（完整分发，含文档与源码；体积大于 `bin`，见下文「Kotlin DSL 与 `-all`」）。

   ```properties
   distributionUrl=https\://mirrors.cloud.tencent.com/gradle/gradle-8.7-all.zip
   ```

2. **增大 Wrapper 下载超时**  
   同一文件中设置：

   ```properties
   networkTimeout=300000
   ```

   单位为毫秒（此处为 5 分钟），避免大文件下载过程中被过早判定为超时。

3. **增大 Gradle HTTP 连接/读取超时**  
   在 `gradle.properties` 中增加：

   ```properties
   systemProp.org.gradle.internal.http.connectionTimeout=120000
   systemProp.org.gradle.internal.http.socketTimeout=120000
   ```

   有助于缓解依赖或元数据下载时的连接超时（单位：毫秒）。

### 工程文件修改点与差异说明

以下仅针对**为解决 Gradle 分发包下载超时**而在本仓库中做过的工程侧修改；便于 Code Review 或迁移到其它机器时对照。

| 文件 | 修改点 | 与常见默认/未改动的差异 |
|------|--------|-------------------------|
| `gradle/wrapper/gradle-wrapper.properties` | `distributionUrl` | 常见模板多为官方 `…/gradle-8.7-bin.zip`。本仓库改为腾讯云：`…/gradle-8.7-all.zip`（**`-all`** 含源码/文档，减轻 IDE 再拉 `gradle-*-src.zip`；**版本号仍为 8.7**）。 |
| 同上 | `networkTimeout` | 若未设置或数值较小（例如默认 `10000` 毫秒），大文件长时间下载易被判定超时。本仓库设为 `300000`（5 分钟），延长 Wrapper 下载 Gradle 分发包时的等待上限。 |
| 同上 | 注释行 | 增加中文注释，说明使用镜像的目的（避免连官方站超时）；**不参与构建逻辑**，仅文档性说明。 |
| `gradle.properties` | `systemProp.org.gradle.internal.http.connectionTimeout` | 未配置时由 Gradle 内部默认策略决定；显式设为 `120000` 毫秒，拉长**建立连接**阶段的可接受耗时。 |
| `gradle.properties` | `systemProp.org.gradle.internal.http.socketTimeout` | 未配置时同理；显式设为 `120000` 毫秒，拉长**单次读数据**阶段的可接受耗时，减轻依赖与元数据下载时的 `Read timed out`。 |

**未改动的相关项（便于理解边界）：**

- `distributionBase`、`distributionPath`、`zipStoreBase`、`zipStorePath`：仍为 Gradle Wrapper 默认布局，未改。
- `validateDistributionUrl=true`：保持启用，用于校验分发包来源；若镜像与官方包一致，一般无需改为 `false`。
- `settings.gradle.kts`、`build.gradle.kts` 等：为解决**本次**超时问题**未强制修改**；若后续仍需加速 **Maven/Google 依赖**，可另行配置仓库镜像，并单独在 FAQ 或构建文档中记录。

### 仍失败时：清理损坏的本地缓存

若曾经下载到一半中断，本地可能残留不完整的分发目录，导致反复失败。可**关闭 IDE 后**删除本机目录（名称随 `distributionUrl` 中带 `-bin` / `-all` 而变，例如）：

- Windows：`%USERPROFILE%\.gradle\wrapper\dists\gradle-8.7-all-*` 或旧的 `gradle-8.7-bin-*`

删除后重新打开工程，再执行 **Sync Project with Gradle Files**，让 Gradle 重新下载。

### 备选：其它镜像源

若当前镜像仍较慢，可在各教育网/云厂商镜像站查找**同名** `gradle-8.7-all.zip`（或 `bin`），将 `distributionUrl` 替换为该直链（版本号须与 `gradle-wrapper.properties` 中一致）。例如部分环境会使用中国科学技术大学镜像站提供的 Gradle 列表页面查找对应文件。

> 注意：镜像站上的 zip 应与官方同版本二进制包一致；若遇校验相关报错，再对照 Gradle 官方文档排查 `validateDistributionUrl` 等选项。

---

## IDE：`prepareKotlinBuildScriptModel` / 无法解析 Gradle 源码包（`gradle-*-src.zip`）

### 现象（节选）

同步或打开 `*.gradle.kts` 时出现：

- `Unexpected exception while resolving Gradle distribution sources`
- `Could not resolve gradle:gradle:8.7` / `Could not get resource 'https://services.gradle.org/distributions/gradle-8.7-src.zip'`
- 或对 `https://github.com/gradle/gradle-distributions/.../gradle-8.7-src.zip` 报 `Connection reset`

构建任务本身可能仍为 **UP-TO-DATE**，主要是 **IDE 为 Kotlin DSL 拉取 Gradle 源码** 失败。

### 原因说明

在仅使用 **`-bin.zip`** 时，Android Studio / IntelliJ 的 Kotlin DSL 工具链往往会**再下载** `gradle-*-src.zip`；该解析链路易指向 `services.gradle.org` 或 GitHub，与 `gradle-wrapper.properties` 里配置的腾讯云 **`-bin`** 镜像**无必然联动**，在国内网络下易超时或被重置。

### 本仓库采用的缓解方式

将 Wrapper 的 `distributionUrl` 改为 **`-all.zip`**（本仓库：`https://mirrors.cloud.tencent.com/gradle/gradle-8.7-all.zip`）。**`-all`** 分发包含文档与源码，通常可避免 IDE 再单独解析 `gradle-*-src.zip`。说明见 [Gradle 相关讨论](https://github.com/gradle/gradle/issues/18249)。

### 修改后建议操作

1. 在工程根目录执行：`.\gradlew --stop`（或 Android Studio 里 **Stop Gradle daemons**）。  
2. 可选：删除 `%USERPROFILE%\.gradle\wrapper\dists\` 下旧的 **`gradle-8.7-bin-*`** 目录，避免与新的 **`gradle-8.7-all-*`** 混淆；首次同步会重新下载 **all** 包（体积约 200MB+，属正常）。  
3. **File → Sync Project with Gradle Files**。

---

## Android SDK 安装到 `D:\rdtools\Android\Sdk`

### 推荐方式：仓库内脚本

本仓库提供 `scripts/install_android_sdk.ps1`，在**已安装 JDK 17+**（并可在 PATH 或 `JAVA_HOME` 中找到 `java`）的前提下，会：

1. 解压 Google **Command line tools**（`commandlinetools-win-*_latest.zip`）到 `<SdkRoot>\cmdline-tools\latest`；
2. 使用 `sdkmanager` 安装与本工程匹配的组件：`platform-tools`、`platforms;android-35`、`build-tools;35.0.0`；
3. 默认 `SdkRoot` 为 **`D:\rdtools\Android\Sdk`**（可用参数覆盖）。

**用法示例：**

```powershell
cd <本仓库>\scripts
powershell -ExecutionPolicy Bypass -File .\install_android_sdk.ps1
```

若自动下载失败（常见于无法访问 `dl.google.com`），请从 [Android Studio 下载页 — Command line tools only](https://developer.android.com/studio#command-line-tools-only) 用浏览器或另一台机器下载 Windows 的 zip，再执行：

```powershell
.\install_android_sdk.ps1 -SdkRoot "D:\rdtools\Android\Sdk" -ZipPath "D:\Downloads\commandlinetools-win-xxxxxx_latest.zip"
```

也可将 zip **直接放到** `D:\rdtools\Android\Sdk` 下（文件名形如 `commandlinetools-win-*_latest.zip`），脚本会自动发现并解压。

### 重试下载 Command line tools（`Invoke-WebRequest` 易失败时）

在部分网络环境下，`Invoke-WebRequest` 访问 `dl.google.com` 会报「无法连接远程服务器」，可改用系统自带的 **curl**（支持断点重试），例如下载到目标 SDK 目录：

```powershell
$zip = "D:\rdtools\Android\Sdk\commandlinetools-win-11076708_latest.zip"
curl.exe -L --retry 5 --retry-delay 3 --connect-timeout 30 --max-time 600 -o $zip `
  "https://dl.google.com/android/repository/commandlinetools-win-11076708_latest.zip"
```

下载完成后，再执行 `install_android_sdk.ps1`（或用资源管理器将 zip 解压后，将其中 **`cmdline-tools` 文件夹整体放到** `...\Sdk\cmdline-tools\latest`，使存在路径 `...\cmdline-tools\latest\bin\sdkmanager.bat`）。

> **说明**：`sdkmanager` 依赖本机 **JDK 17+**。若尚未安装 Java，可先安装 [Microsoft OpenJDK 17](https://learn.microsoft.com/java/openjdk/download)（或使用 `winget install Microsoft.OpenJDK.17`，安装过程若弹出 UAC 需确认），并保证 `JAVA_HOME` 或 `PATH` 中有 `java`，再运行脚本或手动执行 `sdkmanager` 安装 `platform-tools`、平台与 build-tools。

### 安装后环境变量（建议）

| 变量 | 值 |
|------|-----|
| `ANDROID_HOME` | `D:\rdtools\Android\Sdk` |
| `ANDROID_SDK_ROOT` | 同上（与 `ANDROID_HOME` 二选一或同时设置均可，多数工具认其一） |
| `Path` 追加 | `D:\rdtools\Android\Sdk\platform-tools`（便于 `adb` 等） |

在 Android Studio 中： **Settings → Android SDK → Android SDK Location** 设为上述路径；或在工程根目录 `local.properties` 中配置：

```properties
sdk.dir=D\:\\rdtools\\Android\\Sdk
```

（注意 Windows 下反斜杠的转义写法。）

### 网络说明

`install_android_sdk.ps1` 会尝试从 `dl.google.com` 拉取 Command line tools；若出现超时或「无法连接远程服务器」，请使用**浏览器/代理/离线 zip** 等方式取得同一 zip 后，用 `-ZipPath` 指定路径安装；也可在 PowerShell 中设置 `HTTPS_PROXY` 后再运行脚本。

---

## 无真机调试（Android 模拟器 / AVD）

没有实体机时，使用 **Android Virtual Device（AVD）** 即可安装、运行和调试本工程，与真机流程相同（点击 Run 选择虚拟设备）。

### 前置条件

- 已安装 **Android Studio**，且 **SDK 路径** 已指向本机目录（参见上文「Android SDK 安装」与工程根目录 `local.properties` 中的 `sdk.dir`）。
- 在 **SDK Manager** 中已安装至少一个 **System Image**（系统镜像），例如 `Android 15 (API 35)` 的 **Google APIs** 或 **Google Play** 变体（x86_64 或 arm64-v8a，视 CPU 与加速器而定）。

### 推荐：用 Android Studio 创建并启动模拟器

1. 打开 **Device Manager**（设备管理器）：主工具栏手机图标，或菜单 **View → Tool Windows → Device Manager**。  
2. 点击 **Create Device**，选择机型（如 Pixel）→ **Next**。  
3. 选择 **System Image**：若列表为空，点击 **Download** 下载镜像；建议选择与工程 `compileSdk` 接近的 API（本仓库为 **API 35** 时，选 API 35 镜像最省事）。  
4. 完成向导后，在列表中对该设备点击 **▶** 启动模拟器。  
5. 回到工程，运行配置里选择该 **Virtual Device**，再点 **Run**（绿色三角）即可安装并启动 APP。

### Windows 上的性能与兼容性

- 模拟器依赖 CPU **虚拟化**（VT-x / AMD-V），请在 BIOS 中开启。  
- 常见加速方式包括 **Hyper-V**、**Windows Hypervisor Platform (WHPX)** 等（在「启用或关闭 Windows 功能」中按需勾选）；具体选项因系统与 Android Studio 版本而异，以官方文档为准。  
- 若启动失败或极慢，可在 Device Manager 中编辑该 AVD，尝试更换 **系统镜像 ABI** 或关闭快照等选项排查。

### 命令行（可选）

已配置 `ANDROID_SDK_ROOT` 且 `emulator`、`avdmanager` 在 `PATH` 中时，可在终端创建/启动 AVD（需已用 `sdkmanager` 安装对应 `system-images;...` 包）。日常开发仍推荐使用 **Device Manager** 图形界面。

---

## 运行界面黑屏

### 现象

模拟器或真机上启动 APP 后，整屏为黑色，或几乎看不到界面内容。

### 原因说明（节选）

1. 使用 **`enableEdgeToEdge()`** 后，窗口默认背景可能为黑/透明，Compose 若未铺满或未绘制背景，会透出黑色。  
2. **Navigation Compose** 的 **`NavHost`** 未加 **`Modifier.fillMaxSize()`** 时，在部分机型/API 上子界面可能无法铺满，表现为异常空白或黑屏。  
3. 若模拟器**长时间无系统栏**、纯黑，也可能是模拟器未启动完成或 **GPU 加速**异常（见上文「无真机调试」）。

### 本仓库已采用的修改

| 位置 | 做法 |
|------|------|
| `MainActivity.kt` | 在 `CnStrokesTheme` 内增加根级 **`Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background)`**；**`NavHost(..., modifier = Modifier.fillMaxSize())`**。 |
| `res/values/themes.xml` | 为 **`Theme.CnStrokesDict`** 设置 **`android:windowBackground`**（如白色），减轻首帧绘制前的黑屏。 |
| `SearchScreen.kt` / `DetailScreen.kt` | **`Scaffold(containerColor = MaterialTheme.colorScheme.background)`**，与主题背景一致。 |

若仍黑屏且模拟器无状态栏：可对 AVD 执行 **Cold Boot** / **Wipe Data**，或将 **Graphics** 改为 **Software** 试跑。

---

## 笔画动态演示：倒置、错位或笔画消失

### 现象

- 笔顺动画或静态笔画**上下颠倒**、与常见字形观感不符；或  
- 修正「倒置」后，**动态笔画整块不显示**；或  
- 笔画与预览区域**严重错位**。

数据来自 **Hanzi Writer** 风格的 SVG 路径（`PathParser.createPathFromPathData`），绘制在 **`android.graphics.Canvas`** 上。

### 原因说明

1. **平移量 `dx` / `dy` 与 `scale` 不一致**  
   变换顺序为 **`translate(dx, dy)`** 再 **`scale(scale, scale)`** 时，路径点 `(x, y)` 对应屏幕 **`(dx + scale·x, dy + scale·y)`**。  
   若居中时用 **`bounds.left` / `bounds.top` 直接参与减法而未乘 `scale`，会把整块内容移偏，易误判为「倒置」或严重错位。

   **正确写法（概念）**：  
   `dx = … - scale * bounds.left`，`dy = … - scale * bounds.top`（与本仓库 `StrokeCanvas.kt` 一致）。

2. **竖直方向与屏幕 Y 轴不一致**  
   部分数据/设备组合下，需在路径坐标系内对字框做**竖直镜像**，使字形正向。  
   应使用**绕支点的一次 `scale(1, -1, px, py)`**，等价于「平移到支点 → Y 取反 → 平移回去」，避免手写多段 `translate`/`scale` 时**矩阵顺序错误**。

3. **笔画「消失」的典型原因**  
   曾用 **`translate(-cy) → scale(1,-1) → translate(cy)`** 等**错误顺序**实现翻转，等价于错误的反射，笔画会被变换到视图外，表现为**完全画不出来**。

### 本仓库采用的画法（`app/.../ui/detail/StrokeCanvas.kt`）

在 **`translate(dx, dy)`** 与 **`scale(scale, scale)`** 之后，对字框中心做竖直镜像：

```kotlin
canvas.scale(1f, -1f, bounds.centerX(), bounds.centerY())
```

**不要**用错误顺序的三段 `translate` 拼凑翻转；**支点**使用字框 **`RectF` 的中心**，与路径包围盒一致。

若日后更换笔画数据源，仅需重新校验：**平移公式是否仍为「先 scale 再对齐」**，以及是否需要 **`scale(1,-1, cx, cy)`**。

---

## 文档维护

- 若升级 Gradle Wrapper 版本（例如从 8.7 升到其它版本），请同步更新 `distributionUrl` 中的版本号，并视情况补充本 FAQ 中的说明。
- 若升级 `compileSdk` / `targetSdk`，请同步调整 `install_android_sdk.ps1` 中的 `platforms;android-*` 与 `build-tools` 版本，并更新本节表格与说明；使用模拟器时，请在 **SDK Manager** 中安装对应 API 的 **System Image**，或在 **Device Manager** 中下载镜像。
- 若修改 `StrokeCanvas.kt` 的矩阵顺序或更换笔画数据源，请同步检查「笔画动态演示」一节中的 **`dx`/`dy`** 与 **`scale(1,-1, cx, cy)`** 是否仍适用。
