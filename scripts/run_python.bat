@echo off
REM 检查Python环境
python --version
if %errorlevel% neq 0 (
    echo Python未安装或不在PATH中
    pause
    exit /b 1
)

REM 运行脚本
python %*
