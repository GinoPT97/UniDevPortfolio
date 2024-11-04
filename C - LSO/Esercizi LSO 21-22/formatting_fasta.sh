#!/usr/bin/env bash
echo "part a"

for file in *.fasta; do
    echo $file
    sed "/^>/ s/[^\-\>[:alnum:]]/_/g" $file
    echo -e "\n"
done
