sbt clean publish
rm -fr /tmp/repos
mv ./repos /tmp
git checkout gh-pages
cp -rp /tmp/repos .
git commit -a -m $1
git push origin gh-pages
