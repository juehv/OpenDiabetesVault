#! /bin/bash
# runs plotting script and pdflatex to build report

# cleanup
./clean.sh
mkdir out

# plot
python plot.py -c config.ini -f $1 -d -l -o ./out

# latex
cd out
pdflatex report.tex
pdflatex report.tex
