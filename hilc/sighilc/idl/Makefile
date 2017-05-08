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
# LOCALREMOVEOBJS	Extra local objects to remove
#
# See 'stdrules.mak' for default targets
# ---------------------------------------------------------------------------
TARGET               = libidl.a
CURDIR		     = sighilc/idl
SRCDIR               = $(ROOTDIR)/$(CURDIR)
BUILDMODE            = -l

LOCALAPPHEADERS      = 	$(SCSHOME)/srcred \
			$(BINDIR)/$(CURDIR) \
			$(SRCDIR)

LOCALAPPLDLIBS	     =
LOCALEXCLUDEDHEADERS =
LOCALAPPLDFLAGS      =
LOCALAPPLOADLIBES    =
LOCALSYSPACKAGES     = $(ORB_NAME) SCADAsoft
GEN_EXTRA_IDL_CODE = SigHILCServer.idl

INCLUDE_REPLY_IDL_GEN = SigHILCServer.idl
INCLUDE_IMGR_IDL_GEN  = SigHILCServer.idl
INCLUDE_REPLY_CXX_GEN = SigHILCServer.idl
INCLUDE_PROXY_CXX_GEN = SigHILCServer.idl
INCLUDE_IMGR_CXX_GEN  = SigHILCServer.idl

include $(CONFDIR)/idlgen.mak

# Retrieve User specific configuration and Standard building rules
# (do NOT delete these two lines)
include $(ROOTDIR)/config.mak
include $(CONFDIR)/stdrules.mak
