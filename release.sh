#!/bin/sh

sbt clean publish
rm -fr /tmp/repos
mv ./repos /tmp
git checkout gh-pages
\cp -rp /tmp/repos .
git add repos
git commit -m "$1"
git push origin gh-pages
git checkout develop
