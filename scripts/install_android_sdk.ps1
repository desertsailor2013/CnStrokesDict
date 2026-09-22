#Requires -Version 5.1
<#
.SYNOPSIS
  将 Android SDK 安装到指定目录（默认 D:\rdtools\Android\Sdk）。

.DESCRIPTION
  1) 解压 Google「Command line tools」zip 到 <SdkRoot>\cmdline-tools\latest
  2) 使用 sdkmanager 安装 platform-tools、Android 35 平台与 Build-Tools

  若本机无法访问 dl.google.com，请先在本机浏览器或另一台能访问的机器下载 zip，再通过 -ZipPath 指定。

.PARAMETER SdkRoot
  SDK 根目录，默认 D:\rdtools\Android\Sdk

.PARAMETER ZipPath
  已下载的 commandlinetools-win-*_latest.zip 的完整路径；若为空则尝试从网络下载

.PARAMETER SkipDownload
  与 -ZipPath 配合：不尝试联网下载

.EXAMPLE
  .\install_android_sdk.ps1

.EXAMPLE
  .\install_android_sdk.ps1 -ZipPath D:\Downloads\commandlinetools-win-11076708_latest.zip
#>

param(
    [string] $SdkRoot = "D:\rdtools\Android\Sdk",
    [string] $ZipPath = "",
    [switch] $SkipDownload
)

$ErrorActionPreference = "Stop"

$CmdlineDownloadUrls = @(
    "https://dl.google.com/android/repository/commandlinetools-win-14742923_latest.zip",
    "https://dl.google.com/android/repository/commandlinetools-win-11076708_latest.zip"
)

function Find-JavaExe {
    if ($env:JAVA_HOME -and (Test-Path "$env:JAVA_HOME\bin\java.exe")) {
        return "$env:JAVA_HOME\bin\java.exe"
    }
    $candidates = @(
        "${env:ProgramFiles}\Android\Android Studio\jbr\bin\java.exe",
        "${env:ProgramFiles}\Microsoft\jdk-17*\bin\java.exe",
        "${env:ProgramFiles}\Eclipse Adoptium\jdk-17*\bin\java.exe",
        "${env:ProgramFiles}\Java\jdk-17*\bin\java.exe"
    )
    foreach ($p in $candidates) {
        $r = Get-Item $p -ErrorAction SilentlyContinue | Select-Object -First 1
        if ($r -and $r.FullName) { return $r.FullName }
    }
    $cmd = Get-Command java -ErrorAction SilentlyContinue
    if ($cmd) { return $cmd.Source }
    return $null
}

function Ensure-CmdlineToolsLayout {
    param([string]$Root)
    $latest = Join-Path $Root "cmdline-tools\latest"
    if (-not (Test-Path (Join-Path $latest "bin\sdkmanager.bat"))) {
        throw "未找到 $latest\bin\sdkmanager.bat，请检查 zip 是否解压正确。"
    }
}

New-Item -ItemType Directory -Force -Path $SdkRoot | Out-Null

$zipLocal = $ZipPath
if (-not $zipLocal) {
    $zipLocal = Get-ChildItem -Path $SdkRoot -Filter "commandlinetools-win-*_latest.zip" -ErrorAction SilentlyContinue |
        Sort-Object LastWriteTime -Descending | Select-Object -First 1 -ExpandProperty FullName
}

if (-not $zipLocal -and -not $SkipDownload) {
    $destZip = Join-Path $env:TEMP ("commandlinetools-win-" + [Guid]::NewGuid().ToString("N") + ".zip")
    $ok = $false
    foreach ($url in $CmdlineDownloadUrls) {
        Write-Host "尝试下载: $url"
        try {
            Invoke-WebRequest -Uri $url -OutFile $destZip -UseBasicParsing -TimeoutSec 600
            if ((Get-Item $destZip).Length -gt 1MB) {
                $zipLocal = $destZip
                $ok = $true
                break
            }
        } catch {
            Write-Warning "下载失败: $($_.Exception.Message)"
        }
    }
    if (-not $ok) {
        throw @"
无法自动下载 Command line tools。请任选其一：
1) 浏览器打开 Android 官网「Command line tools only」下载 Windows zip：
   https://developer.android.com/studio#command-line-tools-only
   将 zip 放到 $SdkRoot 下，或执行：
   .\install_android_sdk.ps1 -ZipPath `"完整路径\commandlinetools-win-xxxxx_latest.zip`"
2) 配置系统代理后重试，或在 PowerShell 中设置：
   `$env:HTTPS_PROXY = 'http://127.0.0.1:端口'`
"@
    }
}

if (-not $zipLocal -or -not (Test-Path $zipLocal)) {
    throw "未指定有效的 ZipPath，且未在 $SdkRoot 找到 commandlinetools-win-*_latest.zip"
}

Write-Host "使用 zip: $zipLocal"

$extractTmp = Join-Path $env:TEMP ("android-cmdline-extract-" + [Guid]::NewGuid().ToString("N"))
New-Item -ItemType Directory -Force -Path $extractTmp | Out-Null
try {
    Expand-Archive -Path $zipLocal -DestinationPath $extractTmp -Force
    $inner = Join-Path $extractTmp "cmdline-tools"
    if (-not (Test-Path $inner)) {
        throw "zip 内未找到 cmdline-tools 目录，请确认是官方 commandline tools（非 platform-tools 单独包）。"
    }
    $targetLatest = Join-Path $SdkRoot "cmdline-tools\latest"
    if (Test-Path $targetLatest) {
        Remove-Item -Recurse -Force $targetLatest
    }
    New-Item -ItemType Directory -Force -Path (Split-Path $targetLatest) | Out-Null
    Move-Item -Path $inner -Destination $targetLatest
} finally {
    Remove-Item -Recurse -Force $extractTmp -ErrorAction SilentlyContinue
}

Ensure-CmdlineToolsLayout -Root $SdkRoot

$java = Find-JavaExe
if (-not $java) {
    throw "未找到 java.exe。请安装 JDK 17+（如 Microsoft OpenJDK / Temurin），并设置 JAVA_HOME，或将 java 加入 PATH。"
}
$env:JAVA_HOME = Split-Path (Split-Path $java)
Write-Host "使用 Java: $java"

$sdkmanager = Join-Path $SdkRoot "cmdline-tools\latest\bin\sdkmanager.bat"
if (-not (Test-Path $sdkmanager)) { throw "缺少 sdkmanager: $sdkmanager" }

$env:ANDROID_SDK_ROOT = $SdkRoot
$env:ANDROID_HOME = $SdkRoot

Write-Host "正在接受 SDK 许可证..."
$lic = '(for /L %i in (1,1,120) do @echo y) | "' + $sdkmanager + '" --sdk_root="' + $SdkRoot + '" --licenses'
Start-Process -FilePath cmd.exe -ArgumentList @('/c', $lic) -Wait -NoNewWindow

$packages = @(
    "platform-tools",
    "platforms;android-35",
    "build-tools;35.0.0"
)
Write-Host "正在安装组件: $($packages -join ', ') ..."
$inst = '"' + $sdkmanager + '" --sdk_root="' + $SdkRoot + '" platform-tools "platforms;android-35" "build-tools;35.0.0"'
Start-Process -FilePath cmd.exe -ArgumentList @('/c', $inst) -Wait -NoNewWindow

Write-Host ""
Write-Host "完成。SDK 根目录: $SdkRoot"
Write-Host "建议在「系统环境变量」中设置:"
Write-Host "  ANDROID_HOME = $SdkRoot"
Write-Host "  ANDROID_SDK_ROOT = $SdkRoot"
Write-Host "并在 Path 中追加: $SdkRoot\platform-tools"
