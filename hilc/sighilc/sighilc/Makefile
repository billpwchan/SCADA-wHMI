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
TARGET                = SigHILC
CURDIR                = sighilc/sighilc
SRCDIR                = $(ROOTDIR)/$(CURDIR)
BUILDMODE             = 
LOCALAPPFLAGS         =
SUBLIBDIRTARGET       = tst

LOCALAPPHEADERS       = $(ROOTDIR)/red/lib \
                      $(ROOTDIR)/error \
                      $(ROOTDIR)/inc \
                      $(ROOTDIR)/sig/inc \
                      $(SRCDIR) \
                      $(SRCDIR)/../libserver \
                      $(SRCDIR)/../libsighilc \
                      $(SRCDIR)/../libsighilc/HILCcore \
                      $(SRCDIR)/../util \
                      $(SRCDIR)/../libclient \
                      $(BINDIR)/$(CURDIR)/../idl \
                      $(BINDIR)/$(CURDIR)/../../inc \
                      $(BINDIR)/inc/error \
                      $(SCSHOME)/inc \

LOCALEXCLUDEDHEADERS  = 
LOCALAPPLDFLAGS       = 
LOCALAPPLOADLIBES     = $(BINDIR)/lib
LOCALAPPLDLIBS        = -lSigHILCClient -lSigHILCServer -lHILCSpec -lHILCcore \
                        -lscsintTraces -lRED -ldbk -ldbmserver -lscsctl -lasc -lscs -lscsalm -lscsError -lscsimg \
                        -lscs$(ORB_NAME) -lecomlib
						
LOCALEXTRAOBJS        = $(BINDIR)/$(CURDIR)/../../inc/hilctypes$(ORBCLIENTSUFFIXO)

ifeq ($(SYSTEMFAMILY), win32)
  LOCALAPPLDLIBS  += -lscsorb
#fix stack overflow crash (1MB as default in msvc7 seems to small : 8MB as default for Linux)
  LOCALAPPLDFLAGS += $(OPT_MODE)STACK:0x800000
endif

LOCALSYSPACKAGES      = $(ORB_NAME) SCADAsoft

LOCALEXTRAOBJS = 

# Retrieve User specific configuration and Standard building rules
# (do NOT delete these two lines)
include $(ROOTDIR)/config.mak
include $(CONFDIR)/stdrules.mak
