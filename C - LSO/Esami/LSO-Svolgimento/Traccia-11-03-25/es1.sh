#es1
find /home -name "*.txt" -newermt "2010-02-20" ! -newermt "2023-02-20" -type f -exec cat {} +

#es2
awk '{ for (i=1; i<=NF; i++) if ($i ~ /^es/ && $i !~ /ame$/) { print; break } }' parole.txt

#es3
awk '{ if (NF > max) max = NF; sum += NF } END { print max, sum }' campi.txt

#es4
sed '/annullato/ { /202[2-4]/ d }' prenotazioni.txt