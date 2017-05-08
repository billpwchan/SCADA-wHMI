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
OBJECTS = %OBJECTS% $(LOCALEXTRAOBJS)

# Define objects to remove
# ------------------------
REMOVEOBJS = %OBJECTS% $(LOCALREMOVEOBJS)

# Executable linking rule
# -----------------------
WVIEW := $(shell cleartool pwv -short > /dev/null)

$(TARGET): $(OBJECTS) $(APPLIBFILES)
	@if [ "$(LOCALMKROOT)" = "1" ]; then \
	rm -f $(TARGET);\
	fi
	$(LINK.o) $(OBJECTS) $(LOADLIBES) $(LDLIBS) -o $@
	@if [ -n "`echo $(SYSPACKAGES) | grep rtap`" ]; then \
	${RTAPROOT}/bin/RtapUnlockExe $@; \
	fi
	@if [ "$(LOCALMKROOT)" =  "1" ]; then \
	$(MKROOT) $(TARGET);\
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
