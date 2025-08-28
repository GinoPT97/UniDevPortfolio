#!/usr/bin/env bash
echo "part b"
for file in *.fasta; do
    echo $file
    sed "/^>/ s/[^\-\>[:alnum:]]/_/g" $file | awk 'BEGIN{count=1} {if ($0 ~ /^[>-]/) {printf("> %d %s", count, substr($0, 2)); count++} else print $0}'
    echo -e "\n"
done
