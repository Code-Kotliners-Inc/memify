@ECHO OFF
TITLE Check style with Detekt
SETLOCAL ENABLEDELAYEDEXPANSION

SET ROOT_FOLDER=build\bin
SET VERSION=1.23.8
SET DETEKT_JAR=%ROOT_FOLDER%\detekt-cli-%VERSION%-all.jar
SET DETEKT_URL=https://github.com/detekt/detekt/releases/download/v%VERSION%/detekt-cli-%VERSION%-all.jar

REM Создать папку, если не существует
IF NOT EXIST "%ROOT_FOLDER%" MKDIR "%ROOT_FOLDER%"

REM Скачать Detekt, если отсутствует
IF NOT EXIST "%DETEKT_JAR%" (
  ECHO Please wait, first download...
  DEL /Q "%ROOT_FOLDER%\detekt-*" 2>NUL
  curl.exe -sSL "%DETEKT_URL%" -o "%DETEKT_JAR%"
)

java -jar "%DETEKT_JAR%" --config .github/workflows/assets/detekt.yml %*
echo Done!
PAUSE
