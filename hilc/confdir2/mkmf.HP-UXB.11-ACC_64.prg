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

# Define objects to compile
# -------------------------
OBJECTS = %OBJECTS% $(LOCALEXTRAOBJS)

# Define objects to remove
# ------------------------
REMOVEOBJS = %OBJECTS% $(LOCALREMOVEOBJS)

# Executable linking rule
# -----------------------
WVIEW := $(shell cleartool pwv -short)

$(TARGET): $(OBJECTS) $(APPLIBFILES)
	@if [ "$(LOCALMKROOT)" -eq "1" ]; then \
	rm -f $(TARGET);\
	fi
	$(LINK.o) $(OBJECTS) $(LOADLIBES) $(LDLIBS) -o $@
	@if [ -n "`echo $(SYSPACKAGES) | grep rtap`" ]; then \
	  ${RTAPROOT}/bin/RtapUnlockExe $@; \
	fi
	@if [ "$(LOCALMKROOT)" -eq "1" ]; then \
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
	$(RM) $(REMOVEOBJS) $(TARGET) $(TARGET).mak

# Dependency rules
# ----------------
