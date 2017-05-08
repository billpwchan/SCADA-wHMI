# ---------------------------------------------------------------------------
# This file defines module features and additionnal local parameters for the
# module.
#
# Following variables MUST be defined:
#
# TARGET:               Filename of target to build.
# SRCDIR:               Path where to find sources to build target.
# BUILDMODE:            Kind of target to build (-l for library, nothing for
#                       executable).
#
# Following variables may be defined if needed:
# (no previous global bindings in config.mak)
#
# LOCALAPPFLAGS:        Compiler flags.
# LOCALAPPHEADERS:      Headers compiler search pathes.
# LOCALEXCLUDEDHEADERS: Cf. LOCALAPPHEADERS, but not used in dependencies.
# LOCALAPPLDFLAGS:      Linker flags (used for executable building only).
# LOCALAPPLOADLIBES:    Libraries linker search pathes (idem).
# LOCALAPPLDLIBS:       Libraries linker invocation (idem).
# LOCALSYSPACKAGES:     Installed external libraries required (See
#                       stdconf.mak for predefined package ids).
# LOCALREMOVEOBJS	Extra local objects to remove
#
# See 'stdrules.mak' for default targets
# ---------------------------------------------------------------------------

ifeq ($(SYSTEMFAMILY), win32)
TARGET = jfasapi.$(SHARED_EXTENSION)
else
TARGET = libjfasapi.$(SHARED_EXTENSION)
endif

CURDIR = jnac/jfasapi
SRCDIR = $(ROOTDIR)/$(CURDIR)

BUILDMODE = -l
LOCALAPPFLAGS = _SCSTRACE_ JNA_SCS_EXPORTS
LOCALAPPHEADERS = $(SCSHOME)/inc $(SCSHOME)/srcred \
                  $(ROOTDIR)/fasclient/src $(ROOTDIR)/sighilc/libclient $(SRCDIR) $(ROOTDIR)/jnac/jscsapi

LOCALEXCLUDEDHEADERS =
LOCALAPPLDFLAGS = 
LOCALAPPLOADLIBES = $(SCSHOME)/$(SYSTEM)/lib
LOCALAPPLDLIBS = -lscsclient -lfasclient -lasc  \
                 -lscsError -lscs -lscs$(ORB_NAME) -lscsthd
      
#LOCALSYSPACKAGES =  $(ORB_NAME) roguewave
LOCALSYSPACKAGES =  $(ORB_NAME)
LOCALREMOVEOBJS =

# Retrieve User specific configuration and Standard building rules
# (do NOT delete these two lines)
include $(ROOTDIR)/config.mak
include $(CONFDIR)/stdrules.mak
