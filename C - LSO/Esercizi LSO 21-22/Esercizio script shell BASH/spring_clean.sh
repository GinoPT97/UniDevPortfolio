#!/usr/bin/env bash

if [ $# -ne 2 ]; then
    echo -e "Usage: spring_clean.sh start_folder file_extension (without dot i.e. html not .html)\nFor the start folder I need the absolute path"
    exit 1
fi

dfs_clean(){
    cd $1
    for elem in *; do
        if [ -f $elem -a ! -L $elem ]; then
            remove=$(echo $elem | awk '{if ($0 ~ /\.'$2'$/) {printf "remove"} else {printf "dont_remove"}}')
            if [ $remove = "remove" ]; then
                rm -v $elem
            fi
        elif [ -d $elem ]; then
            dir=$elem
            dfs_clean $dir $2
            remove_dir=$(ls -A $dir)
            if [ -z $remove_dir ]; then
                rmdir -v $dir
            fi
        fi
    done
    cd ..
}

start_dir=$1
extension=$2

pushd $start_dir 1>/dev/null
dfs_clean "." $extension
popd 1>/dev/null
