find "${1:-.}" -type f | while read f; do
  echo "===== $f ====="; cat "$f"; echo
done > ~/Documenti/codice.txt

#bash dump_files.sh /path/to/directory