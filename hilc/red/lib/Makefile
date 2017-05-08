#-----------------------------------------------------------------------------------
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
#
# See 'stdrules.mak' for default targets
# ---------------------------------------------------------------------------
TARGET 		= libRED.$(SHARED_EXTENSION)
CURDIR          = red/lib
SRCDIR 		= $(ROOTDIR)/$(CURDIR)
BUILDMODE 	= -l
LOCALAPPFLAGS   = $(OPT_MODE)DRED_EXPORT


LOCALAPPHEADERS = 	$(ROOTDIR)/inc \
			$(BINDIR)/inc/error 

LOCALEXCLUDEDHEADERS =
LOCALAPPLDFLAGS      =
LOCALAPPLOADLIBES    =
LOCALAPPLDLIBS       =  -lscs -lscsError -lasc -lols

#~ ifeq ($(SYSTEMFAMILY), win32)
    #~ LOCALAPPLDLIBS += -lscsorb
#~ endif


LOCALSYSPACKAGES = $(ORB_NAME) SCADAsoft 


# Retrieve User specific configuration and Standard building rules
# (do NOT delete these two lines)
include $(ROOTDIR)/config.mak
include $(CONFDIR)/stdrules.mak

