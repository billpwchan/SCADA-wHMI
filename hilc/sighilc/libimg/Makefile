# ---------------------------------------------------------------------------
# This file defines module features and additionnal local parameters for the
# module. It also redefines several predefined targets.
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
# LOCALSYSPACKAGES:     Installed external libraries required (see
#                       stdconf.mak for predefined package ids).
# LOCALREMOVEOBJS:      Extra local objects to remove
#
# See 'stdrules.mak' for default targets
# ---------------------------------------------------------------------------
TARGET               = libimgrSigHILC.$(SHARED_EXTENSION)
CURDIR               = sighilc/libimg
SRCDIR               = $(ROOTDIR)/$(CURDIR)
BUILDMODE            = -l

LOCALAPPHEADERS      = $(BINDIR)/$(CURDIR)/../idl \
                       $(BINDIR)/$(CURDIR)/../../inc

LOCALEXCLUDEDHEADERS =
LOCALAPPLDFLAGS      =
LOCALAPPLOADLIBES    = $(BINDIR)/lib 
LOCALAPPLDLIBS	     = -lscs -lscsimgbin -lscs$(ORB_NAME) -lSigHILCClient

LOCALSYSPACKAGES      = $(ORB_NAME) SCADAsoft 

LOCALAPPORBSERVEROBJS = $(BINDIR)/$(CURDIR)/../idl/inputmgr_SigHILCServer$(ORBSERVERSUFFIXO)

LOCALAPPORBSOAPOBJS   = $(BINDIR)/$(CURDIR)/../idl/inputmgr_SigHILCServer$(ORBSOAPSUFFIXO)

LOCALEXTRAOBJS = $(BINDIR)/$(CURDIR)/../idl/SigHILCServer$(ORBCLIENTSUFFIXO) \
                 $(BINDIR)/$(CURDIR)/../idl/SigHILCServerreply$(ORBCLIENTSUFFIXO)

ifeq ($(SYSTEMFAMILY), win32)
ifeq ($(SCS_DLL), yes)
  LOCALAPPLDLIBS += -lscsorb
endif
endif

# Retrieve User specific configuration and Standard building rules
# (do NOT delete these two lines)
include $(ROOTDIR)/config.mak
include $(CONFDIR)/stdrules.mak
