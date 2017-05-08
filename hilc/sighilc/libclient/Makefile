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

TARGET               = libSigHILCClient.$(LIB_EXTENSION)
CURDIR               = sighilc/libclient
SRCDIR               = $(ROOTDIR)/$(CURDIR)
BUILDMODE            = -l

LOCALAPPHEADERS      = $(ROOTDIR)/red/lib \
                       $(ROOTDIR)/inc \
                       $(ROOTDIR)/sig/inc \
                       $(SRCDIR) \
                       $(SRCDIR)/../libserver \
                       $(BINDIR)/$(CURDIR)/../idl \
                       $(BINDIR)/$(CURDIR)/../lib \
                       $(BINDIR)/$(CURDIR)/../../inc

LOCALAPPFLAGS        =

LOCALEXCLUDEDHEADERS =

LOCALAPPORBSERVEROBJS = $(BINDIR)/$(CURDIR)/../idl/SigHILCServerreply$(ORBSERVERSUFFIXO)

LOCALAPPORBSOAPOBJS   = $(BINDIR)/$(CURDIR)/../idl/SigHILCServerreply$(ORBSOAPSUFFIXO)

LOCALAPPORBCLIENTOBJS = $(BINDIR)/$(CURDIR)/../idl/SigHILCServer$(ORBCLIENTSUFFIXO) \
                        $(BINDIR)/$(CURDIR)/../idl/inputmgr_SigHILCServer$(ORBCLIENTSUFFIXO)


LOCALEXTRAOBJS = $(BINDIR)/$(CURDIR)/../lib/SigHILCServerproxy$(ORBCLIENTSUFFIXO) \
                 $(BINDIR)/$(CURDIR)/../lib/SigHILCServerreply_i$(ORBCLIENTSUFFIXO)

LOCALAPPLDFLAGS      =
LOCALAPPLOADLIBES    = $(BINDIR)/lib

LOCALAPPLDLIBS       =    

#ifeq ($(SYSTEMFAMILY), win32)
#    LOCALAPPLDLIBS += -lscsorb
#endif
LOCALSYSPACKAGES     = $(ORB_NAME) SCADAsoft 


# Retrieve User specific configuration and Standard building rules
# (do NOT delete these two lines)
include $(ROOTDIR)/config.mak
include $(CONFDIR)/stdrules.mak
