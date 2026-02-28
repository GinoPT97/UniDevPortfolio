#es 1.a
seq 1 20 | grep -vwE '4|13'

#es 1.b
awk '!/y/ { print $1 }' file.txt

#es 1.c
ls -la | awk '$1 !~ /^-rw.rw.rw./ { print $9 }'

#es 1.d
sed -n '6,12p' input.txt | grep "with"