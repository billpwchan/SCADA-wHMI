if [ $# -ne 3 ]
then
        echo "Usage: $(basename $0) task_id begin_hash end_hash"
        exit 1
fi

repo=`git rev-parse --show-toplevel`
branch=`git branch | grep \* | cut -d ' ' -f2`
filename="${repo}-${1}.txt"
out=${filename}

echo "@Task Id: ${1}	repository: `basename ${repo}`	branch: ${branch}" > ${out}

git diff ${2}~1..${3} --name-status >> ${out}

echo output: $out