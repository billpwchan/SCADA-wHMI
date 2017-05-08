# Executable generated Makefile by mkmf
# <%DATE%> %ID%
# ----------------------------------------------------------
#

# Retrive Plateform dependent configuration
# -----------------------------------------
include $(ROOTDIR)/config.mak

# Define executable to link
# -------------------------
ifndef TARGET
ifeq ($(SCS_USE_CPATH), no)
    TARGET = %TARGET%
else
    TARGET = `cpath "%TARGET%"`
endif
endif

# Search folder paths for source files
# ------------------------------------
VPATH = $(SRCDIR):%VPATH%

# Define objects to compile
# -------------------------
OBJECTS = %OBJECTS% $(LOCALEXTRAOBJS)

# Define objects to remove
# ------------------------
REMOVEOBJS = %OBJECTS% $(LOCALREMOVEOBJS)

# Executable linking rule
# -----------------------
# WVIEW := $(shell cleartool pwv -short >/dev/null)

$(TARGET): $(OBJECTS) $(APPLIBFILES)
	@if [ "$(LOCALMKROOT)" -eq "1" ]; then \
	rm -f $(TARGET);\
	fi
	$(LINK.obj) $(OBJECTS) $(LOADLIBES) $(LDLIBS) -out:$@
	@if [ -n "`echo $(SYSPACKAGES) | grep rtap`" ]; then \
	  ${RTAPROOT}/bin/RtapUnlockExe $@; \
	fi
	@if [ -e "$@.manifest" ]; then \
	  mt.exe -manifest $@.manifest -outputresource:$@\;1 ;\
	fi
	@if [ "$(LOCALMKROOT)" -eq "1" ]; then \
	$(MKROOT) $(TARGET);\
	fi
	@chmod a+x $@
	@mv -f $@ $(BINDIR)/bin/
	@touch $@
	@if [ -e $(basename $(TARGET)).pdb ]; then \
	 mv -f $(basename $(TARGET)).pdb $(BINDIR)/bin/; \
	fi
	@if [ "$(CREATE_LINK)" = "yes" ]; then \
	clink $@ ; \
	fi
	@if [ ! "$(SCSBINCMD)" = "" ]; then \
	echo "$(SCSBINCMD) $(TARGET)";\
	$(SCSBINCMD) $(TARGET);\
	fi

ifdef APPLIBFILES
$(APPLIBFILES):
endif
 
# Generic rules
# -------------
.PHONY: all objects sbrfiles purge clean clobber

all: $(TARGET)      ### for .bsc: sbrfiles

objects: $(OBJECTS)

sbrfiles:
	$(BSC32) $(subst .exe,.bsc,$(TARGET)) $(subst .obj,.sbr,$(OBJECTS))

purge:
	$(RM) $(REMOVEOBJS)

clean:
	$(RM) $(REMOVEOBJS) $(TARGET) $(subst .exe,.pdb,$(TARGET)) $(subst .exe,.bsc,$(TARGET)) $(subst .exe,.ilk,$(TARGET)) $(subst .obj,.sbr,$(OBJECTS)) $(subst .exe,.lib,$(TARGET)) $(subst .exe,.exp,$(TARGET)) *.idb *.pdb

clobber:
	$(RM) $(REMOVEOBJS) $(TARGET) $(subst .exe,.pdb,$(TARGET)) $(subst .exe,.bsc,$(TARGET)) $(subst .exe,.ilk,$(TARGET)) $(subst .obj,.sbr,$(OBJECTS)) $(subst .exe,.lib,$(TARGET)) $(subst .exe,.exp,$(TARGET)) *.idb *.pdb $(TARGET).mak

# Dependency rules
# ----------------
