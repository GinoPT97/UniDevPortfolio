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
                echo "remove '"$(pwd)"/"$elem"'"
                rm $elem
            fi
        elif [ -d $elem ]; then
            dfs_clean $elem $2
        fi
    done
    remove_dir=$(echo $(ls -A) | awk '{if ($0 ~ /^$/) {printf "empty_dir"} else {printf "non_empty_dir"}}')
    if [ $remove_dir = "empty_dir" ]; then
        rmdir -v $(pwd)
    fi
    cd ..
}

echo "                _                    _                  "
echo " ___ _ __  _ __(_)_ __   __ _    ___| | ___  __ _ _ __  "
echo "/ __| '_ \| '__| | '_ \ / _\ |  / __| |/ _ \/ _\ | '_ \ "
echo "\__ \ |_) | |  | | | | | (_| | | (__| |  __/ (_| | | | |"
echo "|___/ .__/|_|  |_|_| |_|\__, |  \___|_|\___|\__,_|_| |_|"
echo "    |_|                 |___/                           "
echo ""



start_dir=$1
extension=$2

pushd $start_dir 1>/dev/null
dfs_clean "." $extension
popd 1>/dev/null

echo ""
