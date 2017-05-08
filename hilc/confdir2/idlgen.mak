# The following variables should be defined in the calling Makefile to 
# specify which IDL files are candidate for IDL/C++ code generation
# through the Proxy Generator tool.
# INCLUDE_REPLY_IDL_GEN		List of IDL files for which a reply IDL
#                               file should be generated
# INCLUDE_IMGR_IDL_GEN          List of IDL files for which an input manager IDL
#                               file should be generated
# INCLUDE_REPLY_CXX_GEN         List of IDL files for which reply C++ header and code
#                               files should be generated
# INCLUDE_PROXY_CXX_GEN        	List of IDL files for which proxy C++ header and code
# 				files should be generated
# GEN_EXTRA_IDL_CODE            Will call IDL compiler with -A to generate extra IDL
#                               code for list of IDL files specified
# CHANGE_LIB_DIR                Specifies that the default path "lib" is not to be used
#                               for proxy and reply generation for a given list of IDL files
#                               1st argument specifies the path to apply instead of "lib"
#       
# The following lines will create empty variables if not specified in the
# calling Makefile
INCLUDE_REPLY_IDL_GEN += 
INCLUDE_IMGR_IDL_GEN +=
INCLUDE_REPLY_CXX_GEN +=
INCLUDE_PROXY_CXX_GEN +=
INCLUDE_IMGR_CXX_GEN +=

ifeq ($(SYSTEMFAMILY), win32)
OBJECT_EXTENSION=obj
else
OBJECT_EXTENSION=o
endif

#~ ifeq (yes, $(ATS_COMPIL))
	GENSCRIPTSDIR = $(SCSHOME)/dat/gentcl
	GENSOAPSCRIPTSDIR = $(GENSCRIPTSDIR)
#~ else
	#~ GENSCRIPTSDIR = $(ROOTDIR)/src/pge/scripts
	#~ GENSOAPSCRIPTSDIR = $(ROOTDIR)/src/soap/scripts
#~ endif

# Suffixes for generated files from idl
ORBSERVERSUFFIXH  = _skel.hh
ORBSERVERSUFFIXC  = _skel.cpp
ORBSERVERSUFFIXO  = _skel.$(OBJECT_EXTENSION)
ORBCLIENTSUFFIXH  = .hh
ORBCLIENTSUFFIXC  = .cpp
ORBCLIENTSUFFIXO  = .$(OBJECT_EXTENSION)
ORBSOAPSUFFIXH    = _soap.hh
ORBSOAPSUFFIXC    = _soap.cpp
ORBSOAPSUFFIXO    = _soap.$(OBJECT_EXTENSION)
ORBSOAPTIESUFFIXH = _soap_tie.hh
ORBCLIENTTIESUFFIXH = _skel_tie.hh
ORBCLIENTTIESUFFIXC = _skel_tie.cpp
ORBTYPESSUFFIX    = _types.hh
ORBREPLYSUFFIXIDL = reply.idl


# Files generated in the target IDL directory
IDLFILE       = $(wildcard *.idl)
REPLYIDLFILES = $(subst .idl,reply.idl,$(INCLUDE_REPLY_IDL_GEN)) 
IMGRIDLFILES  = $(patsubst %,inputmgr_%,$(INCLUDE_IMGR_IDL_GEN))
REPLYCXXFILES = $(subst .idl,reply_i.cpp,$(INCLUDE_REPLY_CXX_GEN)) $(subst .idl,reply_i.hh,$(INCLUDE_REPLY_CXX_GEN)) 
PROXYCXXFILES = $(subst .idl,proxy.cpp,$(INCLUDE_PROXY_CXX_GEN)) $(subst .idl,proxy.hh,$(INCLUDE_PROXY_CXX_GEN)) 
IMGRCXXFILES  = $(patsubst %,img%,$(subst .idl,_i.cpp,$(INCLUDE_IMGR_CXX_GEN))) \
		$(patsubst %,img%,$(subst .idl,_i.hh,$(INCLUDE_IMGR_CXX_GEN)))
ALLIDLFILES   = $(IDLFILE) $(REPLYIDLFILES) $(IMGRIDLFILES)

GENIDLFILES =   $(REPLYIDLFILES:%=$(BINDIR)/$(CURDIR)/%) \
		$(IMGRIDLFILES:%=$(BINDIR)/$(CURDIR)/%)

HHFILES    =	$(ALLIDLFILES:%.idl=$(BINDIR)/$(CURDIR)/%$(ORBCLIENTSUFFIXH)) \
		$(ALLIDLFILES:%.idl=$(BINDIR)/$(CURDIR)/%$(ORBSERVERSUFFIXH)) \
		$(ALLIDLFILES:%.idl=$(BINDIR)/$(CURDIR)/%$(ORBSOAPSUFFIXH)) \
		$(REPLYCXXFILES:%=$(BINDIR)/$(CURDIR)/../lib/%) \
		$(PROXYCXXFILES:%=$(BINDIR)/$(CURDIR)/../lib/%) \
		$(IMGRCXXFILES:%=$(BINDIR)/$(CURDIR)/../libimg/%)

OBJFILES   =	$(ALLIDLFILES:%.idl=$(BINDIR)/$(CURDIR)/%$(ORBCLIENTSUFFIXO)) \
		$(ALLIDLFILES:%.idl=$(BINDIR)/$(CURDIR)/%$(ORBSERVERSUFFIXO)) \
		$(ALLIDLFILES:%.idl=$(BINDIR)/$(CURDIR)/%$(ORBSOAPSUFFIXO)) 

# Files to remove during a clean/clobber
LOCALREMOVEOBJS =	$(ALLIDLFILES:%.idl=%$(ORBCLIENTSUFFIXH)) \
			$(ALLIDLFILES:%.idl=%$(ORBCLIENTSUFFIXC)) \
			$(ALLIDLFILES:%.idl=%$(ORBCLIENTSUFFIXO)) \
			$(ALLIDLFILES:%.idl=%$(ORBSERVERSUFFIXH)) \
			$(ALLIDLFILES:%.idl=%$(ORBSERVERSUFFIXC)) \
			$(ALLIDLFILES:%.idl=%$(ORBSERVERSUFFIXO)) \
			$(ALLIDLFILES:%.idl=%$(ORBSOAPSUFFIXH)) \
			$(ALLIDLFILES:%.idl=%$(ORBSOAPSUFFIXC)) \
			$(ALLIDLFILES:%.idl=%$(ORBSOAPSUFFIXO)) \
			$(ALLIDLFILES:%.idl=%$(ORBSOAPTIESUFFIXH)) \
			$(ALLIDLFILES:%.idl=%$(ORBCLIENTTIESUFFIXH)) \
			$(ALLIDLFILES:%.idl=%$(ORBCLIENTTIESUFFIXC)) \
			$(ALLIDLFILES:%.idl=%$(ORBTYPESSUFFIX)) \
			$(REPLYCXXFILES:%=$(BINDIR)/$(CURDIR)/../lib/%) \
			$(PROXYCXXFILES:%=$(BINDIR)/$(CURDIR)/../lib/%) \
			$(IMGRCXXFILES:%=$(BINDIR)/$(CURDIR)/../libimg/%)

idl: createdependenciesifneeded createidldirifneeded $(GENIDLFILES) $(HHFILES) $(OBJFILES) all 

createidldirifneeded:
	@if [ ! -d $(BINDIR)/${CURDIR}/../lib ]; then \
		$(MKINSTALLDIRS) $(BINDIR)/${CURDIR}/../lib ; \
	else true; fi
	@if [ ! -d $(BINDIR)/${CURDIR}/../libimg ]; then \
		$(MKINSTALLDIRS) $(BINDIR)/${CURDIR}/../libimg ; \
	else true; fi


# Rules on how to generate the files that have been specified.
# Notes:
#  - $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) is used to find out whether the idl corresponding to
#    the stem (%s) is specified in the GEN_EXTRA_IDL_CODE. If it is, "-A" is specified, otherwise, nothing is specified (%).

ifeq ($(SYSTEMFAMILY), win32)

ifeq ($(SCS_USE_CPATH), no)
# do not use cpath
TMPLOCALAPPHEADERS   = $(strip $(LOCALAPPHEADERS:%=-I%))

# Generate Input Manager C++ Files
$(BINDIR)/$(CURDIR)/../libimg/img%_i.cpp: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/geninputmgrcode.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -b -j $(BINDIR)/$(CURDIR)/../libimg $<

$(BINDIR)/$(CURDIR)/../libimg/img%_i.hh: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/geninputmgrheader.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -b -j $(BINDIR)/$(CURDIR)/../libimg $<

# Generate Proxy C++ Files
$(BINDIR)/$(CURDIR)/../lib/%proxy.hh: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/genproxyheader.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -c -k $(BINDIR)/$(CURDIR)/../lib $<

$(BINDIR)/$(CURDIR)/../lib/%proxy.cpp: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/genproxycode.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -c -k $(BINDIR)/$(CURDIR)/../lib $<

# Generate Reply IDL files
$(BINDIR)/$(CURDIR)/%reply.idl: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/genreplyidl.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -e -i $(BINDIR)/$(CURDIR) $<

# Generate Reply C++ Files
$(BINDIR)/$(CURDIR)/../lib/%reply_i.hh: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/genreplyheader.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -d -l $(BINDIR)/$(CURDIR)/../lib $<

$(BINDIR)/$(CURDIR)/../lib/%reply_i.cpp: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/genreplycode.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -d -l $(BINDIR)/$(CURDIR)/../lib $<

# Compile Reply IDL files
$(BINDIR)/$(CURDIR)/%reply.hh: $(BINDIR)/$(CURDIR)/%reply.idl $(GENSCRIPTSDIR)/scsgenutil.tcl $(GENSOAPSCRIPTSDIR)/gensoapheader.tcl $(GENSOAPSCRIPTSDIR)/gensoapcode.tcl $(GENSOAPSCRIPTSDIR)/gensoaptie.tcl $(GENSOAPSCRIPTSDIR)/gensoaptypes.tcl 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o $(BINDIR)/$(CURDIR) $(subst .hh,.idl,$@)

$(BINDIR)/$(CURDIR)/%reply.cpp: $(BINDIR)/$(CURDIR)/%reply.idl $(GENSCRIPTSDIR)/scsgenutil.tcl $(GENSOAPSCRIPTSDIR)/gensoapheader.tcl $(GENSOAPSCRIPTSDIR)/gensoapcode.tcl $(GENSOAPSCRIPTSDIR)/gensoaptie.tcl $(GENSOAPSCRIPTSDIR)/gensoaptypes.tcl 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o $(BINDIR)/$(CURDIR) $(subst .cpp,.idl,$@)

# Generate Input Manager IDL files
$(BINDIR)/$(CURDIR)/inputmgr_%.idl: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/genimgridl.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -a -i $(BINDIR)/$(CURDIR) $<

# Compile Input Manager IDL files
$(BINDIR)/$(CURDIR)/inputmgr_%.hh: $(BINDIR)/$(CURDIR)/inputmgr_%.idl $(GENSCRIPTSDIR)/scsgenutil.tcl $(GENSOAPSCRIPTSDIR)/gensoapheader.tcl $(GENSOAPSCRIPTSDIR)/gensoapcode.tcl $(GENSOAPSCRIPTSDIR)/gensoaptie.tcl $(GENSOAPSCRIPTSDIR)/gensoaptypes.tcl 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o $(BINDIR)/$(CURDIR) $(subst .hh,.idl,$@)

$(BINDIR)/$(CURDIR)/inputmgr_%.cpp: $(BINDIR)/$(CURDIR)/inputmgr_%.idl $(GENSCRIPTSDIR)/scsgenutil.tcl $(GENSOAPSCRIPTSDIR)/gensoapheader.tcl $(GENSOAPSCRIPTSDIR)/gensoapcode.tcl $(GENSOAPSCRIPTSDIR)/gensoaptie.tcl $(GENSOAPSCRIPTSDIR)/gensoaptypes.tcl 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o $(BINDIR)/$(CURDIR) $(subst .hh,.idl,$@)

$(BINDIR)/$(CURDIR)/%$(ORBCLIENTSUFFIXH): $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/scsgenutil.tcl $(GENSOAPSCRIPTSDIR)/gensoapheader.tcl $(GENSOAPSCRIPTSDIR)/gensoapcode.tcl $(GENSOAPSCRIPTSDIR)/gensoaptie.tcl $(GENSOAPSCRIPTSDIR)/gensoaptypes.tcl 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o $(BINDIR)/$(CURDIR) $<

$(BINDIR)/$(CURDIR)/%$(ORBSERVERSUFFIXH): $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/scsgenutil.tcl $(GENSOAPSCRIPTSDIR)/gensoapheader.tcl $(GENSOAPSCRIPTSDIR)/gensoapcode.tcl $(GENSOAPSCRIPTSDIR)/gensoaptie.tcl $(GENSOAPSCRIPTSDIR)/gensoaptypes.tcl 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o $(BINDIR)/$(CURDIR) $<

$(BINDIR)/$(CURDIR)/%$(ORBSOAPSUFFIXH): $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/scsgenutil.tcl $(GENSOAPSCRIPTSDIR)/gensoapheader.tcl $(GENSOAPSCRIPTSDIR)/gensoapcode.tcl $(GENSOAPSCRIPTSDIR)/gensoaptie.tcl $(GENSOAPSCRIPTSDIR)/gensoaptypes.tcl 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o $(BINDIR)/$(CURDIR) $<


# MRE : compilation du fichier s'il n'existe pas
%reply.hh: 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o $(BINDIR)/$(CURDIR) $(subst .hh,.idl,$@)
# MRE : compilation du fichier s'il n'existe pas
inputmgr_%.hh:
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o $(BINDIR)/$(CURDIR) $(subst .hh,.idl,$@)
else
# use cpath
TMPLOCALAPPHEADERS   = $(strip $(LOCALAPPHEADERS:%=-I`cpath "%"`))

# Generate Input Manager C++ Files
$(BINDIR)/$(CURDIR)/../libimg/img%_i.cpp: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/geninputmgrcode.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -b -j `cpath "$(BINDIR)/$(CURDIR)/../libimg"` `cpath "$<"`

$(BINDIR)/$(CURDIR)/../libimg/img%_i.hh: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/geninputmgrheader.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -b -j `cpath "$(BINDIR)/$(CURDIR)/../libimg"` `cpath "$<"`

# Generate Proxy C++ Files
$(BINDIR)/$(CURDIR)/../lib/%proxy.hh: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/genproxyheader.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -c -k `cpath "$(BINDIR)/$(CURDIR)/../lib"` `cpath "$<"`

$(BINDIR)/$(CURDIR)/../lib/%proxy.cpp: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/genproxycode.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -c -k `cpath "$(BINDIR)/$(CURDIR)/../lib"` `cpath "$<"`

# Generate Reply IDL files
$(BINDIR)/$(CURDIR)/%reply.idl: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/genreplyidl.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -e -i `cpath "$(BINDIR)/$(CURDIR)"` `cpath "$<"`

# Generate Reply C++ Files
$(BINDIR)/$(CURDIR)/../lib/%reply_i.hh: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/genreplyheader.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -d -l `cpath "$(BINDIR)/$(CURDIR)/../lib"` `cpath "$<"` 

$(BINDIR)/$(CURDIR)/../lib/%reply_i.cpp: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/genreplycode.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -d -l `cpath "$(BINDIR)/$(CURDIR)/../lib"` `cpath "$<"` 

# Compile Reply IDL files
$(BINDIR)/$(CURDIR)/%reply.hh: $(BINDIR)/$(CURDIR)/%reply.idl $(GENSCRIPTSDIR)/scsgenutil.tcl $(GENSOAPSCRIPTSDIR)/gensoapheader.tcl $(GENSOAPSCRIPTSDIR)/gensoapcode.tcl $(GENSOAPSCRIPTSDIR)/gensoaptie.tcl $(GENSOAPSCRIPTSDIR)/gensoaptypes.tcl 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o `cpath "$(BINDIR)/$(CURDIR)"` `cpath "$(subst .hh,.idl,$@)"`

$(BINDIR)/$(CURDIR)/%reply.cpp: $(BINDIR)/$(CURDIR)/%reply.idl $(GENSCRIPTSDIR)/scsgenutil.tcl $(GENSOAPSCRIPTSDIR)/gensoapheader.tcl $(GENSOAPSCRIPTSDIR)/gensoapcode.tcl $(GENSOAPSCRIPTSDIR)/gensoaptie.tcl $(GENSOAPSCRIPTSDIR)/gensoaptypes.tcl 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o `cpath "$(BINDIR)/$(CURDIR)"` `cpath "$(subst .cpp,.idl,$@)"`

# Generate Input Manager IDL files
$(BINDIR)/$(CURDIR)/inputmgr_%.idl: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/genimgridl.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -a -i `cpath "$(BINDIR)/$(CURDIR)"` `cpath "$<"`  

# Compile Input Manager IDL files
$(BINDIR)/$(CURDIR)/inputmgr_%.hh: $(BINDIR)/$(CURDIR)/inputmgr_%.idl $(GENSCRIPTSDIR)/scsgenutil.tcl $(GENSOAPSCRIPTSDIR)/gensoapheader.tcl $(GENSOAPSCRIPTSDIR)/gensoapcode.tcl $(GENSOAPSCRIPTSDIR)/gensoaptie.tcl $(GENSOAPSCRIPTSDIR)/gensoaptypes.tcl 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o `cpath "$(BINDIR)/$(CURDIR)"` `cpath "$(subst .hh,.idl,$@)"` 

$(BINDIR)/$(CURDIR)/inputmgr_%.cpp: $(BINDIR)/$(CURDIR)/inputmgr_%.idl $(GENSCRIPTSDIR)/scsgenutil.tcl $(GENSOAPSCRIPTSDIR)/gensoapheader.tcl $(GENSOAPSCRIPTSDIR)/gensoapcode.tcl $(GENSOAPSCRIPTSDIR)/gensoaptie.tcl $(GENSOAPSCRIPTSDIR)/gensoaptypes.tcl 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o `cpath "$(BINDIR)/$(CURDIR)"` `cpath "$(subst .hh,.idl,$@)"` 

$(BINDIR)/$(CURDIR)/%$(ORBCLIENTSUFFIXH): $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/scsgenutil.tcl $(GENSOAPSCRIPTSDIR)/gensoapheader.tcl $(GENSOAPSCRIPTSDIR)/gensoapcode.tcl $(GENSOAPSCRIPTSDIR)/gensoaptie.tcl $(GENSOAPSCRIPTSDIR)/gensoaptypes.tcl 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o `cpath "$(BINDIR)/$(CURDIR)"` `cpath "$<"` 

$(BINDIR)/$(CURDIR)/%$(ORBSERVERSUFFIXH): $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/scsgenutil.tcl $(GENSOAPSCRIPTSDIR)/gensoapheader.tcl $(GENSOAPSCRIPTSDIR)/gensoapcode.tcl $(GENSOAPSCRIPTSDIR)/gensoaptie.tcl $(GENSOAPSCRIPTSDIR)/gensoaptypes.tcl 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o `cpath "$(BINDIR)/$(CURDIR)"` `cpath "$<"`

$(BINDIR)/$(CURDIR)/%$(ORBSOAPSUFFIXH): $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/scsgenutil.tcl $(GENSOAPSCRIPTSDIR)/gensoapheader.tcl $(GENSOAPSCRIPTSDIR)/gensoapcode.tcl $(GENSOAPSCRIPTSDIR)/gensoaptie.tcl $(GENSOAPSCRIPTSDIR)/gensoaptypes.tcl 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o `cpath "$(BINDIR)/$(CURDIR)"` `cpath "$<"` 


# MRE : compilation du fichier s'il n'existe pas
%reply.hh: 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o `cpath "$(BINDIR)/$(CURDIR)"` `cpath "$(subst .hh,.idl,$@)"`
# MRE : compilation du fichier s'il n'existe pas
inputmgr_%.hh:
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o `cpath "$(BINDIR)/$(CURDIR)"` `cpath "$(subst .hh,.idl,$@)"`

endif

# If not Win32
else

TMPLOCALAPPHEADERS   = $(strip $(LOCALAPPHEADERS:%=-I%))

# Generate Input Manager C++ Files
$(BINDIR)/$(CURDIR)/../libimg/img%_i.hh: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/geninputmgrheader.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -b -j $(BINDIR)/$(CURDIR)/../libimg $<

$(BINDIR)/$(CURDIR)/../libimg/img%_i.cpp: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/geninputmgrcode.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -b -j $(BINDIR)/$(CURDIR)/../libimg $<

# Generate Proxy C++ Files
$(BINDIR)/$(CURDIR)/../lib/%proxy.hh: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/genproxyheader.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -c -k $(BINDIR)/$(CURDIR)/../lib $<

$(BINDIR)/$(CURDIR)/../lib/%proxy.cpp: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/genproxycode.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -c -k $(BINDIR)/$(CURDIR)/../lib $<

# Generate Reply IDL files
$(BINDIR)/$(CURDIR)/%reply.idl: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/genreplyidl.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -e -i $(BINDIR)/$(CURDIR) $<

# Generate Reply C++ Files
$(BINDIR)/$(CURDIR)/../lib/%reply_i.hh: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/genreplyheader.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -d -l $(BINDIR)/$(CURDIR)/../lib $<

$(BINDIR)/$(CURDIR)/../lib/%reply_i.cpp: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/genreplycode.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -d -l $(BINDIR)/$(CURDIR)/../lib $<

# Compile Reply IDL files
$(BINDIR)/$(CURDIR)/%reply.hh: $(BINDIR)/$(CURDIR)/%reply.idl $(GENSCRIPTSDIR)/scsgenutil.tcl $(GENSOAPSCRIPTSDIR)/gensoapheader.tcl $(GENSOAPSCRIPTSDIR)/gensoapcode.tcl $(GENSOAPSCRIPTSDIR)/gensoaptie.tcl $(GENSOAPSCRIPTSDIR)/gensoaptypes.tcl 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o $(BINDIR)/$(CURDIR) $<

# Generate Input Manager IDL files
$(BINDIR)/$(CURDIR)/inputmgr_%.idl: $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/genimgridl.tcl $(GENSCRIPTSDIR)/scsgenutil.tcl
	$(IDLGEN) $(TMPLOCALAPPHEADERS) -a -i $(BINDIR)/$(CURDIR) $<

# Compile Input Manager IDL files
$(BINDIR)/$(CURDIR)/inputmgr_%.hh: $(BINDIR)/$(CURDIR)/inputmgr_%.idl $(GENSCRIPTSDIR)/scsgenutil.tcl $(GENSOAPSCRIPTSDIR)/gensoapheader.tcl $(GENSOAPSCRIPTSDIR)/gensoapcode.tcl $(GENSOAPSCRIPTSDIR)/gensoaptie.tcl $(GENSOAPSCRIPTSDIR)/gensoaptypes.tcl 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o $(BINDIR)/$(CURDIR) $<

$(BINDIR)/$(CURDIR)/%$(ORBCLIENTSUFFIXH): $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/scsgenutil.tcl $(GENSOAPSCRIPTSDIR)/gensoapheader.tcl $(GENSOAPSCRIPTSDIR)/gensoapcode.tcl $(GENSOAPSCRIPTSDIR)/gensoaptie.tcl $(GENSOAPSCRIPTSDIR)/gensoaptypes.tcl 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o $(BINDIR)/$(CURDIR) $<

$(BINDIR)/$(CURDIR)/%$(ORBSERVERSUFFIXH): $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/scsgenutil.tcl $(GENSOAPSCRIPTSDIR)/gensoapheader.tcl $(GENSOAPSCRIPTSDIR)/gensoapcode.tcl $(GENSOAPSCRIPTSDIR)/gensoaptie.tcl $(GENSOAPSCRIPTSDIR)/gensoaptypes.tcl 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o $(BINDIR)/$(CURDIR) $<

$(BINDIR)/$(CURDIR)/%$(ORBSOAPSUFFIXH): $(SRCDIR)/%.idl $(GENSCRIPTSDIR)/scsgenutil.tcl $(GENSOAPSCRIPTSDIR)/gensoapheader.tcl $(GENSOAPSCRIPTSDIR)/gensoapcode.tcl $(GENSOAPSCRIPTSDIR)/gensoaptie.tcl $(GENSOAPSCRIPTSDIR)/gensoaptypes.tcl 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o $(BINDIR)/$(CURDIR) $<


# MRE : compilation du fichier s'il n'existe pas
inputmgr_%.hh:
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o $(BINDIR)/$(CURDIR) "$(subst .hh,.idl,$@)"
# $(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o $(BINDIR)/$(CURDIR) "$(BINDIR)/$(CURDIR)/`basename $@ .hh `.idl"

# MRE : compilation du fichier s'il n'existe pas
%reply.hh: 
	$(ORBIDL) $(LOCALIDLFLAGS) $(patsubst %, -A, $(findstring $(*F).idl, $(GEN_EXTRA_IDL_CODE))) $(TMPLOCALAPPHEADERS) -o $(BINDIR)/$(CURDIR) "$(subst .hh,.idl,$@)"

endif     # if win32
