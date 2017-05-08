# ---------------------------------------------------------------------------
#     SYSECA                                 Animator
#                                            Tools 97
# ---------------------------------------------------------------------------
# The information contained in this document is proprietary to Animator's
#   group of SYSECA and shall not be disclosed by the recipent to third
#             persons without the written consent of SYSECA
# ---------------------------------------------------------------------------
#         file: Makefile
#  description: Tools source Makefile
#     creation: 13/01/2000
#       author: Pascal GEORGET
# ---------------------------------------------------------------------------
#
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

TARGET = libfasclient.$(SHARED_EXTENSION)
CURDIR = fasclient
SRCDIR = $(ROOTDIR)/$(CURDIR)/src

BUILDMODE = -l
LOCALAPPFLAGS = _SCSTRACE_ SCSCLIENT_EXPORTS

#
# Include header search path
# $(SCSHOME)/inc                    SCADAsoft common headers
# $(SCSHOME)/srcred                 scsthread header
# $(ROOTDIR)/sighilc/libclient      SigHILC libclient header
#
LOCALAPPHEADERS = $(SCSHOME)/inc \
                  $(SCSHOME)/srcred \
                  $(ROOTDIR)/sighilc/libclient
                        
                        
LOCALEXCLUDEDHEADERS =
LOCALAPPLDFLAGS  = 
LOCALAPPLOADLIBES = $(SCSHOME)/$(SYSTEM)/lib
LOCALAPPLDLIBS = -lscsclient -lscs -lscsthd -lscs$(ORB_NAME) -lscsError -lSigHILCClient -lscsimg \
                    -lasc -lscsintTraces

ifeq ($(SYSTEMFAMILY), win32)
    LOCALAPPLDLIBS += -lscsorb
endif

LOCALSYSPACKAGES     = $(ORB_NAME)

# Retrieve User specific configuration and Standard building rules
# (do NOT delete these two lines)
include $(ROOTDIR)/config.mak
include $(CONFDIR)/stdrules.mak
