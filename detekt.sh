#!/bin/bash
set -e

ROOT_FOLDER=build/bin
VERSION=1.23.8
DETEKT_JAR="$ROOT_FOLDER/detekt-cli-$VERSION-all.jar"
DETEKT_URL="https://github.com/detekt/detekt/releases/download/v$VERSION/detekt-cli-$VERSION-all.jar"

mkdir -p "$ROOT_FOLDER"

if [ ! -f "$DETEKT_JAR" ]; then
  echo "Downloading Detekt..."
  rm -f "$ROOT_FOLDER"/detekt-*
  curl -sSL "$DETEKT_URL" -o "$DETEKT_JAR"
fi

if [ "$CI" ]; then
  export REVIEWDOG_GITHUB_API_TOKEN="${GITHUB_TOKEN}"
  java -jar "$DETEKT_JAR" \
    --config .github/workflows/assets/detekt.yml \
    --report xml:detekt_report.xml

  reviewdog -f=checkstyle \
    -name="detekt" \
    -reporter="github-pr-review" \
    -fail-level=error <detekt_report.xml
else
  java -jar "$DETEKT_JAR" \
    --config .github/workflows/assets/detekt.yml \
    "$@"
fi

echo "Done!"
