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
    TARGET = %TARGET%
endif

# Search folder paths for source files
# ------------------------------------
VPATH = $(SRCDIR):%VPATH%

# Define objects to compile
# -------------------------
OBJECTS = %OBJECTS%

# Define objects to remove
# ------------------------
REMOVEOBJS = %OBJECTS% $(LOCALREMOVEOBJS)

# Executable linking rule
# -----------------------
$(TARGET): $(OBJECTS) $(APPLIBFILES)
	$(LINK.$(OBJECT_EXTENSION)) $(OBJECTS) $(LOADLIBES) $(LDLIBS) -o $@
	@if [ -n "`echo $(SYSPACKAGES) | grep rtap`" ]; then \
		$(RTAPROOT)/bin/RtapUnlockExe $@ ;\
	fi

#	remsh scadix1 /usr/rtap/bin/RtapUnlockExe $(BINDIR)/$(subst $(ROOTDIR)/,,$(SRCDIR))/$@ ;\ !!! Modif. J.P 22/09/97

ifdef APPLIBFILES
$(APPLIBFILES):
endif
 
# Generic rules
# -------------
.PHONY: all objects purge clean clobber

all: $(TARGET)

objects: $(OBJECTS)

purge:
	$(RM) $(REMOVEOBJS)

clean:
	$(RM) $(REMOVEOBJS) $(TARGET)

clobber:
	$(RM) $(REMOVEOBJS) $(TARGET) $(TARGET).$(LIB_EXTENSION).mak

# Dependency rules
# ----------------
