# ---------------------------------------------------------------------------
#          file: stdrules.mak
#   description: Standard building rules Makefile part
#      creation: 1997/01/21
#        author: dom
# ---------------------------------------------------------------------------
# $Id$
#
# $Log$
# ---------------------------------------------------------------------------
# This file defines targets and rules to manage modules of application:
#
# Targets: 'all'           Builds module.
#          'depend'        Recomputes dependencies (rebuild real
#                          module building Makefile. See Makefile
#                          templates for other target definitions).
#          'update'        Checks out new or modified sources from
#                          repository.
#          'rebuild'       Do 'update', 'depend', then 'all'.
#          'updateforced'  Checks out unconditionally sources from
#                          repository.
#          'rebuildforced' Do 'updateforced', 'depend', then 'all'.
#
# Other targets defined here are not to be used directly by users.
# ---------------------------------------------------------------------------

# Useful variables
# ================

MAKEDEP=make
ifneq ($(SYSTEMFAMILY), win32)
MAKEDEP=gmake
endif

TARGETMAKEFILE = $(TARGET:%.$(LIB_EXTENSION)=%).mak
LOCALBINDIR    = $(BINDIR)/$(subst $(ROOTDIR)/,,$(SRCDIR))
VARBINDIR      = $(subst $(ROOTDIR)/,,$(SRCDIR))
TMPSRCEXTRADIR = $(strip $(SRCEXTRADIR:%=%/*))

ifeq ($(BUILDMODE), -lc)
INVOKEMKMF     = $(MKMF) -p "$(CC) $(OPT_MODE)E $(CFLAGS)" \
  -c "$(OPT_MODE)D_$(USER)_ $(TRUELOCALAPPFLAGS) $(APPFLAGS) $(PACKAGESFLAGS)" \
  -e "$(TRUELOCALEXCLUDEDHEADERS) $(EXCLUDEDHEADERS)" \
  -i "$(APPHEADERS) $(TRUELOCALAPPHEADERS)" \
  -s "$(TMPSRCEXTRADIR)" \
  -l -t "$(LOCALBINDIR)/$(TARGET)" -o "$(LOCALBINDIR)/$(TARGETMAKEFILE)"
else
INVOKEMKMF     = $(MKMF) -p "$(CXX) $(OPT_MODE)E $(CXXCFLAGS)" \
  -c "$(OPT_MODE)D_$(USER)_ $(TRUELOCALAPPFLAGS) $(APPFLAGS) $(PACKAGESFLAGS)" \
  -e "$(TRUELOCALEXCLUDEDHEADERS) $(EXCLUDEDHEADERS)" \
  -i " $(APPHEADERS) $(TRUELOCALAPPHEADERS)" \
  -s "$(TMPSRCEXTRADIR)" \
  $(BUILDMODE) -t "$(LOCALBINDIR)/$(TARGET)" -o "$(LOCALBINDIR)/$(TARGETMAKEFILE)"
endif

# Rules to manage modules of application
# ======================================
.PHONY: all depend aniemf aniarm
.PHONY: rebuild
.PHONY: rebuildforced
.PHONY: createbinarydirifneeded

.DEFAULT: createdependenciesifneeded 
	@echo "### Making '$@' in $(SRCDIR) for $(TARGET) ($(SYSTEM)$(APPMODE))..."
	@cd $(LOCALBINDIR); $(MAKEDEP) TARGET=$(TARGET) SRCDIR=$(SRCDIR) LOCALINCDIR=$(LOCALINCDIR) UNRESOLVEDMOD=$(UNRESOLVEDMOD) \
	TRUELOCALAPPFLAGS="$(TRUELOCALAPPFLAGS)" \
	TRUELOCALAPPHEADERS="$(TRUELOCALAPPHEADERS)" \
	LOCALEXCLUDEDHEADERS="$(TRUELOCALEXCLUDEDHEADERS)" \
	LOCALAPPLDFLAGS="$(TRUELOCALAPPLDFLAGS)" \
	TRUELOCALAPPLOADLIBES="$(TRUELOCALAPPLOADLIBES)" \
	TRUELOCALAPPLDLIBS="$(TRUELOCALAPPLDLIBS)" \
	LOCALSYSPACKAGES="$(LOCALSYSPACKAGES)" \
	APPHEADERS="$(APPHEADERS)" \
	PACKAGESHEADERS="$(PACKAGESHEADERS)" \
	APPLOADLIBES="$(APPLOADLIBES)" \
	PACKAGESLOADLIBES="$(PACKAGESLOADLIBES)" \
	LOCALEXTRAOBJS="$(TRUELOCALEXTRAOBJS)" \
	LOCALEXTRALDLIBS="$(TRUELOCALEXTRALDLIBS)" \
	LOCALREMOVEOBJS="$(LOCALREMOVEOBJS)" \
	LOCALMKROOT="$(LOCALMKROOT)" \
	OSFCXX="$(OSFCXX)" \
	WINDOWSBIN="$(WINDOWSBIN)" \
	RCFILE="$(RCFILE)" \
	MCFILE="$(MCFILE)" \
	COMMENT="$(COMMENT)" \
	VERSION="$(VERSION)" \
	WITHOUTTHREAD="$(WITHOUTTHREAD)" \
	OLDC="$(OLDC)" \
	WITHOUTWINNTSPEC="$(WITHOUTWINNTSPEC)" \
	LOCALCXPERFFLAGS="$(LOCALCXPERFFLAGS)" \
	LOCALCXPERFLDFLAGS="$(LOCALCXPERFLDFLAGS)" \
	$(PARALLELMAKEOPTION) -f $(TARGETMAKEFILE) $@

all: createdependenciesifneeded 
	@echo "### Making '$@' in $(SRCDIR) for $(TARGET) ($(SYSTEM)$(APPMODE))..."
	@cd $(LOCALBINDIR); $(MAKEDEP) TARGET=$(TARGET) SRCDIR=$(SRCDIR) LOCALINCDIR=$(LOCALINCDIR) UNRESOLVEDMOD=$(UNRESOLVEDMOD) \
	TRUELOCALAPPFLAGS="$(TRUELOCALAPPFLAGS)" \
	TRUELOCALAPPHEADERS="$(TRUELOCALAPPHEADERS)" \
	LOCALEXCLUDEDHEADERS="$(TRUELOCALEXCLUDEDHEADERS)" \
	LOCALAPPLDFLAGS="$(TRUELOCALAPPLDFLAGS)" \
	TRUELOCALAPPLOADLIBES="$(TRUELOCALAPPLOADLIBES)" \
	TRUELOCALAPPLDLIBS="$(TRUELOCALAPPLDLIBS)" \
	LOCALSYSPACKAGES="$(LOCALSYSPACKAGES)" \
	APPHEADERS="$(APPHEADERS)" \
	PACKAGESHEADERS="$(PACKAGESHEADERS)" \
	APPLOADLIBES="$(APPLOADLIBES)" \
	PACKAGESLOADLIBES="$(PACKAGESLOADLIBES)" \
	LOCALEXTRAOBJS="$(TRUELOCALEXTRAOBJS)" \
	LOCALEXTRALDLIBS="$(TRUELOCALEXTRALDLIBS)" \
	LOCALREMOVEOBJS="$(LOCALREMOVEOBJS)" \
	LOCALMKROOT="$(LOCALMKROOT)" \
	OSFCXX="$(OSFCXX)" \
	WINDOWSBIN="$(WINDOWSBIN)" \
	RCFILE="$(RCFILE)" \
	MCFILE="$(MCFILE)" \
	COMMENT="$(COMMENT)" \
	VERSION="$(VERSION)" \
	WITHOUTTHREAD="$(WITHOUTTHREAD)" \
	OLDC="$(OLDC)" \
	WITHOUTWINNTSPEC="$(WITHOUTWINNTSPEC)" \
	LOCALCXPERFFLAGS="$(LOCALCXPERFFLAGS)" \
	LOCALCXPERFLDFLAGS="$(LOCALCXPERFLDFLAGS)" \
	$(PARALLELMAKEOPTION) -f $(TARGETMAKEFILE) $@


createdependenciesifneeded: createbinarydirifneeded
	@if [ ! -f $(LOCALBINDIR)/$(TARGETMAKEFILE) ]; then \
	  echo "### Creating dependencies for '$(TARGET)' ($(SYSTEM)$(APPMODE))..."; \
	  echo '$(INVOKEMKMF)' ; $(INVOKEMKMF) ; \
	else true; fi

createbinarydirifneeded: aniemf aniarm
	@if [ ! -d $(BINDIR)/bin ]; then \
		$(MKINSTALLDIRS) $(BINDIR)/bin ; \
	else true; fi
	@if [ ! -d $(BINDIR)/lib ]; then \
		$(MKINSTALLDIRS) $(BINDIR)/lib ; \
	else true; fi
	@if [ ! -d $(LOCALBINDIR) ]; then \
		echo "### Creating binary repository for '$(TARGET)' ($(SYSTEM)$(APPMODE))..."; \
		cd $(ROOTDIR) ; \
		$(MKINSTALLDIRS) $(LOCALBINDIR) ; \
	else true; fi
	@if [ ! "$(SYSTEMFAMILY)" = "win32" ]; then \
		if [ ! "$(TARGET)" = "" ]; then \
			if [ ! "$(TARGET)" = "libidl.a" ]; then \
				if [ \( "$(BUILDMODE)" = "-l" \) -o \( "$(BUILDMODE)" = "-lc" \) ]; then \
					if [ ! -f $(BINDIR)/lib/$(TARGET) ]; then \
						echo "### Creating link for '$(TARGET)' ($(SYSTEM)$(APPMODE))..."; \
						cd $(BINDIR)/lib ; \
						ln -sf ../$(VARBINDIR)/$(TARGET) . ; \
					else true; fi \
				else \
					if [ ! -f $(BINDIR)/bin/$(TARGET) ]; then \
						echo "### Creating link for '$(TARGET)' ($(SYSTEM)$(APPMODE))..."; \
						cd $(BINDIR)/bin ; \
						ln -sf ../$(VARBINDIR)/$(TARGET) . ; \
					else true; fi \
				fi \
			fi \
		fi \
	fi

depend: $(HHFILES)
	@echo "### Rebuilding dependencies for '$(TARGET)' ($(SYSTEM)$(APPMODE))..."
	 $(INVOKEMKMF)

report:
	@$(RCS_COMMAND) RCS/*

rebuild: update depend all

rebuildforced: updateforced depend all

ANMKERROR  = $(BINDIR)/bin/mkerror
ANRMBUILDER  = $(BINDIR)/bin/rmbuilder

ifneq (,$(findstring scs_source/an, $(SRCDIR)))
ANEMFFILE  = $(wildcard *.emf)
ANARMFILE  = $(wildcard *.arm)
endif

ANEMFHFILES   = $(ANEMFFILE:%.emf=$(SRCDIR)/%.h)
LOCALREMOVEOBJS	+= $(ANEMFFILE:%.emf=$(SRCDIR)/%.h)

aniemf: $(ANEMFHFILES)

ANARMHFILES   = $(ANARMFILE:%.arm=$(SRCDIR)/%.h)
LOCALREMOVEOBJS	+= $(ANARMFILE:%.arm=$(SRCDIR)/%.h)

aniarm: $(ANARMHFILES)


.SUFFIXES: .emf .arm

ifeq ($(SCS_USE_CPATH), no)
.emf.h:
	$(ANMKERROR) -o $@ $<
.arm.h:
	$(ANRMBUILDER) $< . $(ANRMEXPORTMACRO)
else
.emf.h:
	$(ANMKERROR) -o `cpath $@` `cpath $<`
.arm.h:
	$(ANRMBUILDER) `cpath $<` .
endif

.PHONY: ls
ls:
	@for f in *; do \
	  if [ -d $$f ]; then \
	    $(MAKE) --no-print-directory -C $$f ls; \
	  else echo "$(SRCDIR)/$$f"; fi; \
	done

#.PHONY: TAGS tags
#TAGS tags:
#	@$(SHELL) -c "cd ..; $(MAKE) tags"

# No more rules.
# ==============
