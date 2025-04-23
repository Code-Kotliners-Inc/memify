#!/bin/bash

set -e

EDITORCONFIG_PATH_OPTION="--editorconfig=./.editorconfig"
ROOT_FOLDER=build/bin
VERSION=1.5.0
KTLINT_BIN=$ROOT_FOLDER/ktlint-$VERSION
mkdir -p $ROOT_FOLDER
if [ ! -f "$KTLINT_BIN" ]; then
  echo "Please wait, first download..."
  rm -f $ROOT_FOLDER/ktlint-*
  curl -sSL https://github.com/pinterest/ktlint/releases/download/$VERSION/ktlint --output $KTLINT_BIN
  chmod a+x $KTLINT_BIN
fi
if [ $CI ]; then
  export REVIEWDOG_GITHUB_API_TOKEN="${GITHUB_TOKEN}"
  $KTLINT_BIN --color "$EDITORCONFIG_PATH_OPTION" --reporter=checkstyle |
    reviewdog -f=checkstyle \
      -name="ktlint" \
      -reporter="github-pr-review" \
      -fail-level=error
else
  $KTLINT_BIN --color "$@" "$EDITORCONFIG_PATH_OPTION"
fi

echo "Done!"
