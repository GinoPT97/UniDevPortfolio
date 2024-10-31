#!/usr/bin/env bash
echo "part c"
for file in *.fasta; do
    echo $file
    awk '{if ($0 ~ /^[>-]/){header=$0;getline sequence; if(length (sequence) > 199){printf("%s\n%s\n", header, sequence)}}}' $file
    echo -e "\n"
done
