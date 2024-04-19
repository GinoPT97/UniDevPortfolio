SRC_IMG = $(wildcard img/*.dia)
PS_IMG  = $(SRC_IMG:.dia=.ps)
EPS_IMG = $(SRC_IMG:.dia=.eps)
PDF_IMG = $(SRC_IMG:.dia=.pdf)
SRC    =  $(wildcard sources/*.c) $(wildcard sources/*.h) sources/Makefile
CODE_C =  $(wildcard listati/*.c) 
CODE_H =  $(wildcard listati/*.h)
HTML_C =  $(CODE_C:.c=.c.html) 
HTML_H =  $(CODE_H:.h=.h.html) 
VARIE = README ChangeLog *.sh htgapil.cfg tex4ht.env htgapil/* html/*.html 
ALL_SRC = $(SRC_IMG) $(SRC) *.tex Makefile $(VARIE) *.bib $(CODE_C) $(CODE_H)


all: $(PDF_IMG) $(HTML_C) $(HTML_H) gapil.pdf

$(HTML_C):  %.c.html: %.c
	source-highlight -f html -s cpp $<

$(HTML_H):  %.h.html: %.h
	source-highlight -f html -s cpp $<

$(PDF_IMG): %.pdf: %.eps
	GS_OPTIONS="-dAutoRotatePages=/None" epstopdf $< --outfile=$@

$(PS_IMG): %.ps : %.eps
	eps2eps $< $@ 

$(EPS_IMG): %.eps : %.dia
	dia --nosplash -e $@ $< 

gapil_source.tgz: $(SRC) 
	tar -cvz -f $@ $^

gapil.tgz:  $(ALL_SRC)
	tar -cvz -f $@ $^

gapil.dvi:  *.tex
	latex gapil.tex

gapil.pdf:  *.tex Makefile
	pdflatex gapil.tex
	bibtex gapil
	makeindex gapil.idx
	pdflatex gapil.tex

gapil.ps: gapil.dvi 
	dvips -o gapil.ps gapil.dvi

#install: gapil.pdf gapil.dvi 
#	scp gapil.p* gapil_source.* piccardi@firenze.linux.it:public_html/gapil
#	scp html/*.html piccardi@firenze.linux.it:public_html/gapil/
#	scp gapil.p* gapil_source.tgz root@truelite.it:/var/www/gapil
#	scp html/*.html root@truelite.it:/var/www/gapil/

clean: 
	rm -f *.dvi *.log *.ps *.html *.aux *.toc *.rel *.ilg *.rip *.ind \
	*.pdf  *.out *.idx *~ listati/*.html img/*.ps img/*.eps img/*.pdf 

