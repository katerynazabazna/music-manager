#!/bin/sh
set -eu

echo "loading demo data ..."
rm -rf ./data
mkdir data
cp ./0-mm-utils/src/main/resources/* ./data/
echo "done."