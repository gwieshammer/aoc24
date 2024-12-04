cat Day03.txt | perl -e '$s=0;while(<>){$l=$_;while($l=~m/mul\((\d{1,3}),(\d{1,3})\)/g){$s+=$1*$2;}}print $s'
