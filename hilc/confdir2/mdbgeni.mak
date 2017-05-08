# ---------------------------------
# mdb .struc-to-.h files generation
# ---------------------------------

ifndef HSTRUCBINDIR
  HSTRUCBINDIR = mdbstruc
endif

STRUCFILE	= $(wildcard *.struc)
HFILE		= $(STRUCFILE:%.struc=$(BINDIR)/inc/$(HSTRUCBINDIR)/%.h)

# LOCALREMOVEOBJS += $(HFILE)
# => Workarround to prevent to reach the make ARG_MAX limit
#    - LOCALREMOVEOBJS variable value would be too long, when passed to make sub-calls
#    - So the variable value is passed through a temporary text file

# Select the name of this text file
LOCALREMOVEOBJSTXTFILE = $(LOCALBINDIR)/$(TARGET).LOCALREMOVEOBJS.txt

# Generate this text file, with an "echo" of the variable value redericted into it
LOCALREMOVEOBJSTXTFILEGEN = $(shell echo $(HFILE) > $(LOCALREMOVEOBJSTXTFILE))

# Set the variable value as a, back-quote encapsulated, "cat" of this file
# NB: An additional despecialisation of the back-quotes (\`) is required here,
#     because, later on in the Makefiles, the variable is used in the following way:
#         $(MAKE) ... LOCALREMOVEOBJS="$(LOCALREMOVEOBJS)" ... $(TARGET)
#     and the sentence `cat file.txt` would else be resolved there, giving back
#     the value of the variable fully expended again (thus too long again...) too early.
LOCALREMOVEOBJS += \`cat $(LOCALREMOVEOBJSTXTFILEGEN) $(LOCALREMOVEOBJSTXTFILE)\` $(LOCALREMOVEOBJSTXTFILE)


mdbgeni: createoutputdirifneeded $(HFILE) all

ifeq ($(SYSTEMFAMILY), win32)
ifeq ($(SCS_USE_CPATH), no)
 # MSYS
 MDBGENIRULE = $(MDB_GENI) -c w -i "$(SRCDIR)" -o "$(BINDIR)/inc/$(HSTRUCBINDIR)"
else
 # cygwin
 MDBGENIRULE = $(MDB_GENI) -c w -i $(shell cpath "$(SRCDIR)") -o $(shell cpath "$(BINDIR)/inc/$(HSTRUCBINDIR)")
endif

else
 MDBGENIRULE = $(MDB_GENI) -c w -i $(SRCDIR) -o $(BINDIR)/inc/$(HSTRUCBINDIR)
endif

$(BINDIR)/inc/$(HSTRUCBINDIR)/%.h : $(SRCDIR)/%.struc
	$(MDBGENIRULE)
	$(shell touch dummy.cpp)

createoutputdirifneeded:
	@if [ ! -d $(BINDIR)/inc ]; then \
		$(MKINSTALLDIRS) $(BINDIR)/inc ; \
	else true; fi
	@if [ ! -d $(BINDIR)/inc/$(HSTRUCBINDIR) ]; then \
		$(MKINSTALLDIRS) $(BINDIR)/inc/$(HSTRUCBINDIR) ; \
	else true; fi

.PHONY: local mdbgeni

#END OF FILE

