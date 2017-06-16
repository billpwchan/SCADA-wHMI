#!/bin/sh
GIT_TAG=$1
rm -rf ../whmi
git clone 'http://titanium.hk.thales:7990/scm/scadagen/whmi.git' '../whmi'
cd '../whmi'
git checkout $GIT_TAG
git log --pretty=format:%H -1 > '../whmi/tools/Build.git_commit.log'