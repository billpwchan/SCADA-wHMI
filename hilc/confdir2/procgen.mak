
PROC = $(ORACLE_HOME)/bin/proc
LOCALPROCFLAGS =

# Suffixes for generated files from pc
PROCSUFFIXC  = .c

ifeq ($(SYSTEMFAMILY), win32)
PROCSUFFIXO  = .obj
else
PROCSUFFIXO  = .o
endif

PROCFILES +=
NOMFILE	= basename % .pc

ALLPROCFILES = $(PROCFILES)
OBJFILES   = $(PROCFILES:%.pc=$(BINDIR)/$(CURDIR)/%$(PROCSUFFIXO))
#OBJECTS    = $(OBJFILES)
LOCALEXTRAOBJS = $(OBJFILES)

pc: createdependenciesifneeded $(OBJFILES) all

$(BINDIR)/$(CURDIR)/%$(PROCSUFFIXC): $(SRCDIR)/%.pc
	$(PROC) $(LOCALPROCINC) $(CHEKSEMORA) iname=$< \
	oname=$(BINDIR)/$(CURDIR)/`basename $< .pc`$(PROCSUFFIXC)

LOCALREMOVEOBJS = $(PROCFILES:%.pc=%$(PROCSUFFIXO))
#LOCALREMOVEOBJS = $(PROCFILES:%.pc=%$(PROCSUFFIXC)) \
#                  $(PROCFILES:%.pc=%$(PROCSUFFIXO))

