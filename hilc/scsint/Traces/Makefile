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
TARGET               = libscsintTraces.$(SHARED_EXTENSION)
CURDIR               = scsint/Traces
SRCDIR               = $(ROOTDIR)/$(CURDIR)
LOCALAPPFLAGS        = $(OPT_MODE)DSCSINT_TRACES_EXPORTS
BUILDMODE            = -l

LOCALAPPHEADERS      = 	$(SRCDIR)/../inc \
			$(ROOTDIR)/inc

LOCALAPPLOADLIBES    = $(BINDIR)/lib
LOCALAPPLDLIBS       = -lscs -lscsError

LOCALSYSPACKAGES = SCADAsoft

# Retrieve User specific configuration and Standard building rules
# (do NOT delete these two lines)
include $(ROOTDIR)/config.mak
include $(CONFDIR)/stdrules.mak
