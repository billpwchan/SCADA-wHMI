#!/bin/sh
BASE_HREF=.

echo Running lint...
npm run lint
if [ ! $? -eq 0 ]; then
    echo Failed in ng lint, please fix the problems before building
else
    echo Building Application...
    ng build --base-href=${BASE_HREF} --target=production --extract-css --aot --sourcemap --stats-json --output-hashing=none
    if [ ! $? -eq 0 ]; then
        echo Failed in ng build
    else
        echo Completed
        echo - Binary at "/dist/"
    fi
fi
