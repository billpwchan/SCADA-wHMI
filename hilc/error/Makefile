#AlaError : 4000
#atserror : 4200
#SigError : 5800
#ArcError : 9600
#RdnError : 9200
#SimError : 3000

TARGET    = libDummyEmf.$(LIB_EXTENSION)
CURDIR    = error
SRCDIR    = $(ROOTDIR)/$(CURDIR)
BUILDMODE = -l

TARGETS = $(BINDIR)/inc/error/SigError.h \
	$(BINDIR)/inc/error/RdnError.h \
	
emf: createbinarydirifneeded createoutputdirifneeded $(TARGETS) all

ifeq ($(SYSTEMFAMILY), win32)
SCSMKERROR = scsmkerror.exe
else
SCSMKERROR = scsmkerror
endif


MKERRORRULE=$(SCSMKERROR) -o $@ $<

ifeq ($(SYSTEMFAMILY), win32)
  ifeq ($(SCS_USE_CPATH), yes)
    MKERRORRULE=$(SCSMKERROR) -o $(shell cpath "$@") $(shell cpath "$<")
  endif
endif


$(BINDIR)/inc/error/SigError.h : $(SRCDIR)/SigError.emf
	$(MKERRORRULE) -m 5800

$(BINDIR)/inc/error/RdnError.h : $(SRCDIR)/RdnError.emf
	$(MKERRORRULE) -m 9200
	
createoutputdirifneeded:
	@if [ ! -d $(BINDIR)/inc ]; then \
		$(MKINSTALLDIRS) $(BINDIR)/inc ; \
	else true; fi
	@if [ ! -d $(BINDIR)/inc/error ]; then \
		$(MKINSTALLDIRS) $(BINDIR)/inc/error ; \
	else true; fi


include $(ROOTDIR)/config.mak
include $(CONFDIR)/stdrules.mak
