ifeq ($(SYSTEMFAMILY), win32)
OBJECT_EXTENSION=obj
else
OBJECT_EXTENSION=o
endif



# Suffixes for generated files from idl
ORBSERVERSUFFIXH  = _skel.hh
ORBSERVERSUFFIXC  = _skel.cpp
ORBSERVERSUFFIXO  = _skel.$(OBJECT_EXTENSION)
ORBCLIENTSUFFIXH  = .hh
ORBCLIENTSUFFIXC  = .cpp
ORBCLIENTSUFFIXO  = .$(OBJECT_EXTENSION)
ORBSOAPSUFFIXH    = _soap.hh
ORBSOAPSUFFIXC    = _soap.cpp
ORBSOAPSUFFIXO    = _soap.$(OBJECT_EXTENSION)

ORBTYPESSUFFIX    = _types.hh
ORBSERVERTIESUFFIX= _skel_tie.hh
ORBSOAPTIESUFFIX  = _soap_tie.hh


IDLFILE  = $(wildcard *.idl)
HHFILES  = $(IDLFILE:%.idl=$(BINDIR)/$(CURDIR)/%$(ORBCLIENTSUFFIXH)) \
           $(IDLFILE:%.idl=$(BINDIR)/$(CURDIR)/%$(ORBSERVERSUFFIXH)) \
           $(IDLFILE:%.idl=$(BINDIR)/$(CURDIR)/%$(ORBSOAPSUFFIXH)) 
OBJFILES = $(IDLFILE:%.idl=$(BINDIR)/$(CURDIR)/%$(ORBCLIENTSUFFIXO)) \
           $(IDLFILE:%.idl=$(BINDIR)/$(CURDIR)/%$(ORBSERVERSUFFIXO)) \
           $(IDLFILE:%.idl=$(BINDIR)/$(CURDIR)/%$(ORBSOAPSUFFIXO)) 

LOCALREMOVEOBJS	= $(IDLFILE:%.idl=%$(ORBCLIENTSUFFIXH)) \
                  $(IDLFILE:%.idl=%$(ORBCLIENTSUFFIXC)) \
                  $(IDLFILE:%.idl=%$(ORBCLIENTSUFFIXO)) \
                  $(IDLFILE:%.idl=%$(ORBSERVERSUFFIXH)) \
                  $(IDLFILE:%.idl=%$(ORBSERVERSUFFIXC)) \
                  $(IDLFILE:%.idl=%$(ORBSERVERSUFFIXO)) \
		  $(IDLFILE:%.idl=%$(ORBSOAPSUFFIXH)) \
		  $(IDLFILE:%.idl=%$(ORBSOAPSUFFIXC)) \
		  $(IDLFILE:%.idl=%$(ORBSOAPSUFFIXO)) \
		  $(IDLFILE:%.idl=%$(ORBTYPESSUFFIX)) \
		  $(IDLFILE:%.idl=%$(ORBSERVERTIESUFFIX)) \
		  $(IDLFILE:%.idl=%$(ORBSOAPTIESUFFIX))





idl: createdependenciesifneeded $(HHFILES) $(OBJFILES) all


ifeq ($(SYSTEMFAMILY), win32)

ifeq ($(SCS_USE_CPATH), no)
$(BINDIR)/$(CURDIR)/%$(ORBCLIENTSUFFIXH): $(SRCDIR)/%.idl
	$(ORBIDL) $(LOCALIDLFLAGS) $(strip $(LOCALAPPHEADERS:%=-I%)) -o $(BINDIR)/$(CURDIR) $<

$(BINDIR)/$(CURDIR)/%$(ORBSERVERSUFFIXH): $(SRCDIR)/%.idl
	$(ORBIDL) $(LOCALIDLFLAGS) $(strip $(LOCALAPPHEADERS:%=-I%)) -o $(BINDIR)/$(CURDIR) $<

$(BINDIR)/$(CURDIR)/%$(ORBSOAPSUFFIXH): $(SRCDIR)/%.idl
	$(ORBIDL) $(LOCALIDLFLAGS) $(strip $(LOCALAPPHEADERS:%=-I%)) -o $(BINDIR)/$(CURDIR) $<
	
else

$(BINDIR)/$(CURDIR)/%$(ORBCLIENTSUFFIXH): $(SRCDIR)/%.idl
	$(ORBIDL) $(LOCALIDLFLAGS) $(strip $(LOCALAPPHEADERS:%=-I`cpath %`)) -o `cpath $(BINDIR)/$(CURDIR)` `cpath $<`

$(BINDIR)/$(CURDIR)/%$(ORBSERVERSUFFIXH): $(SRCDIR)/%.idl
	$(ORBIDL) $(LOCALIDLFLAGS) $(strip $(LOCALAPPHEADERS:%=-I`cpath %`)) -o "`cpath "$(BINDIR)/$(CURDIR)"`" "`cpath "$<"`"

$(BINDIR)/$(CURDIR)/%$(ORBSOAPSUFFIXH): $(SRCDIR)/%.idl
	$(ORBIDL) $(LOCALIDLFLAGS) $(strip $(LOCALAPPHEADERS:%=-I`cpath %`)) -o "`cpath "$(BINDIR)/$(CURDIR)"`" "`cpath "$<"`"

endif

else


$(BINDIR)/$(CURDIR)/%$(ORBCLIENTSUFFIXH): $(SRCDIR)/%.idl
	$(ORBIDL) $(LOCALIDLFLAGS) $(strip $(LOCALAPPHEADERS:%=-I%)) -o $(BINDIR)/$(CURDIR) $<

$(BINDIR)/$(CURDIR)/%$(ORBSERVERSUFFIXH): $(SRCDIR)/%.idl
	$(ORBIDL) $(LOCALIDLFLAGS) $(strip $(LOCALAPPHEADERS:%=-I%)) -o $(BINDIR)/$(CURDIR) $<

$(BINDIR)/$(CURDIR)/%$(ORBSOAPSUFFIXH): $(SRCDIR)/%.idl
	$(ORBIDL) $(LOCALIDLFLAGS) $(strip $(LOCALAPPHEADERS:%=-I%)) -o $(BINDIR)/$(CURDIR) $<


endif
