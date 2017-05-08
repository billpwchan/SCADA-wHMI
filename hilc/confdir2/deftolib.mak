# -------------------------------------
# generation d'une lib d'import 
# .lib à partir d'un .def
# => inter-dépendance dll
#
# variables requises :
# SRCDIR
# BINDIR (env)
# 
# -------------------------------------


LOCALLIBDEFFILES = $(wildcard *.def)
LOCALLIBIMPFILES = $(patsubst %.def,%.lib,$(LOCALLIBDEFFILES))

ifeq ($(SCS_USE_CPATH), no)
	LOCALSRCDIR=$(SRCDIR)
	LIBDIR=$(BINDIR)/lib/
	OPT_MODE = -
else
	LOCALSRCDIR=$(shell cpath "$(SRCDIR)")
	LIBDIR=$(shell cpath "$(BINDIR)/lib/")
	OPT_MODE = /
endif

LIBIMPFILES = $(addprefix $(LIBDIR), $(LOCALLIBIMPFILES))

LIBDEFOPTIONS = $(OPT_MODE)machine:I386 $(OPT_MODE)nologo

%.lib : %.def
	lib $(LIBDEFOPTIONS) /DEF:'$<' /OUT:'$@'
	rm -f $(LIBDIR)$@
	mv $@ $(LIBDIR)
	rm -f $(patsubst %.lib,%.exp,$@)

#@touch $@

deftolib: createlibdirifneeded $(LOCALLIBIMPFILES)

createlibdirifneeded:
	@if [ ! -d $(BINDIR)/lib/ ]; then \
		mkdir -p $(BINDIR)/lib/ ; \
	else true; fi

clean:
	rm $(LIBIMPFILES)

#$(LOCALLIBIMPFILES)

.PHONY: deftolib

