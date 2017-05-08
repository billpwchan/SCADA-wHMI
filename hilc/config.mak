# --------------------------------------------------------------------------
#     SYSECA                                 Animator
#                                            Animator 96
# --------------------------------------------------------------------------
# The information contained in this document is proprietary to Animator's
#   group of SYSECA and shall not be disclosed by the recipent to third
#             persons without the written consent of SYSECA
# --------------------------------------------------------------------------
#         file: config.mak
#  description: Specific Configuration Makefile Part
#     creation: 1997/01/10
#       author: dom
# --------------------------------------------------------------------------
# $Id: config.mak,v 5.2 1997/04/01 09:04:30 v9 Exp $
#
# $Log: config.mak,v $
# Revision 5.2  1997/04/01 09:04:30  v9
# *** empty log message ***
#
# Revision 5.1  1997/02/28 15:39:18  v9
# previous release stamped with 'a96_112'
#
# Revision 4.9  1997/01/20 12:59:06  dom
# *** empty log message ***
#
# Revision 4.8  1997/01/15 16:02:52  v9
# *** empty log message ***
#
# Revision 4.7  1997/01/14 16:23:01  herve
# STUDIOFLAGS...
# DXFEDITFLAGS... added
#
# Revision 4.6  1996/12/27 14:14:21  herve
# *** empty log message ***
#
# Revision 4.5  1996/12/27 11:03:19  herve
# *** empty log message ***
#
# Revision 4.4  1996/11/29 10:07:37  orsi
# *** empty log message ***
#
# Revision 4.3  1996/11/28 17:12:28  dom
# *** empty log message ***
#
# ---------------------------------------------------------------------------
# VPATH:              Application dependencies GNU make search path
# APPFLAGS:           Application specific compiler flags
# APPHEADERS:         Application headers compiler search pathes
# APPEXCLUDEDHEADERS: cf. APPHEADERS, but not used in dependencies
# APPLDFLAGS:         Application specific linker flags
# APPLOADLIBES:       Application libraries linker search pathes
# APPLDLIBS:          Application libraries linker invocation
# ---------------------------------------------------------------------------

# Application parameters
# ----------------------
VPATH := $(SRCDIR):$(INCDIR):$(BINDIR)/inc

APPFLAGS = 
PURIFYCMD =

_LOCALAPPFLAGS     = $(strip $(LOCALAPPFLAGS:_%=$(OPT_MODE)D_%))
aLOCALAPPFLAGS     = $(strip $(_LOCALAPPFLAGS:a%=$(OPT_MODE)Da%)) 	
bLOCALAPPFLAGS     = $(strip $(aLOCALAPPFLAGS:b%=$(OPT_MODE)Db%)) 
cLOCALAPPFLAGS     = $(strip $(bLOCALAPPFLAGS:c%=$(OPT_MODE)Dc%)) 
dLOCALAPPFLAGS     = $(strip $(cLOCALAPPFLAGS:d%=$(OPT_MODE)Dd%)) 
eLOCALAPPFLAGS     = $(strip $(dLOCALAPPFLAGS:e%=$(OPT_MODE)De%)) 
fLOCALAPPFLAGS     = $(strip $(eLOCALAPPFLAGS:f%=$(OPT_MODE)Df%)) 
gLOCALAPPFLAGS     = $(strip $(fLOCALAPPFLAGS:g%=$(OPT_MODE)Dg%)) 
hLOCALAPPFLAGS     = $(strip $(gLOCALAPPFLAGS:h%=$(OPT_MODE)Dh%)) 
iLOCALAPPFLAGS     = $(strip $(hLOCALAPPFLAGS:i%=$(OPT_MODE)Di%)) 
jLOCALAPPFLAGS     = $(strip $(iLOCALAPPFLAGS:j%=$(OPT_MODE)Dj%)) 
kLOCALAPPFLAGS     = $(strip $(jLOCALAPPFLAGS:k%=$(OPT_MODE)Dk%)) 
lLOCALAPPFLAGS     = $(strip $(kLOCALAPPFLAGS:l%=$(OPT_MODE)Dl%)) 
mLOCALAPPFLAGS     = $(strip $(lLOCALAPPFLAGS:m%=$(OPT_MODE)Dm%)) 
nLOCALAPPFLAGS     = $(strip $(mLOCALAPPFLAGS:n%=$(OPT_MODE)Dn%)) 
oLOCALAPPFLAGS     = $(strip $(nLOCALAPPFLAGS:o%=$(OPT_MODE)Do%)) 
pLOCALAPPFLAGS     = $(strip $(oLOCALAPPFLAGS:p%=$(OPT_MODE)Dp%)) 
qLOCALAPPFLAGS     = $(strip $(pLOCALAPPFLAGS:q%=$(OPT_MODE)Dq%)) 
rLOCALAPPFLAGS     = $(strip $(qLOCALAPPFLAGS:r%=$(OPT_MODE)Dr%)) 
sLOCALAPPFLAGS     = $(strip $(rLOCALAPPFLAGS:s%=$(OPT_MODE)Ds%)) 
tLOCALAPPFLAGS     = $(strip $(sLOCALAPPFLAGS:t%=$(OPT_MODE)Dt%)) 
uLOCALAPPFLAGS     = $(strip $(tLOCALAPPFLAGS:u%=$(OPT_MODE)Du%)) 
vLOCALAPPFLAGS     = $(strip $(uLOCALAPPFLAGS:v%=$(OPT_MODE)Dv%)) 
wLOCALAPPFLAGS     = $(strip $(vLOCALAPPFLAGS:w%=$(OPT_MODE)Dw%)) 
xLOCALAPPFLAGS     = $(strip $(wLOCALAPPFLAGS:x%=$(OPT_MODE)Dx%)) 
yLOCALAPPFLAGS     = $(strip $(xLOCALAPPFLAGS:y%=$(OPT_MODE)Dy%)) 
zLOCALAPPFLAGS     = $(strip $(yLOCALAPPFLAGS:z%=$(OPT_MODE)Dz%)) 
ALOCALAPPFLAGS     = $(strip $(zLOCALAPPFLAGS:A%=$(OPT_MODE)DA%)) 
BLOCALAPPFLAGS     = $(strip $(ALOCALAPPFLAGS:B%=$(OPT_MODE)DB%)) 
CLOCALAPPFLAGS     = $(strip $(BLOCALAPPFLAGS:C%=$(OPT_MODE)DC%)) 
DLOCALAPPFLAGS     = $(strip $(CLOCALAPPFLAGS:D%=$(OPT_MODE)DD%)) 
ELOCALAPPFLAGS     = $(strip $(DLOCALAPPFLAGS:E%=$(OPT_MODE)DE%)) 
FLOCALAPPFLAGS     = $(strip $(ELOCALAPPFLAGS:F%=$(OPT_MODE)DF%)) 
GLOCALAPPFLAGS     = $(strip $(FLOCALAPPFLAGS:G%=$(OPT_MODE)DG%)) 
HLOCALAPPFLAGS     = $(strip $(GLOCALAPPFLAGS:H%=$(OPT_MODE)DH%)) 
ILOCALAPPFLAGS     = $(strip $(HLOCALAPPFLAGS:I%=$(OPT_MODE)DI%)) 
JLOCALAPPFLAGS     = $(strip $(ILOCALAPPFLAGS:J%=$(OPT_MODE)DJ%)) 
KLOCALAPPFLAGS     = $(strip $(JLOCALAPPFLAGS:K%=$(OPT_MODE)DK%)) 
LLOCALAPPFLAGS     = $(strip $(KLOCALAPPFLAGS:L%=$(OPT_MODE)DL%)) 
MLOCALAPPFLAGS     = $(strip $(LLOCALAPPFLAGS:M%=$(OPT_MODE)DM%)) 
NLOCALAPPFLAGS     = $(strip $(MLOCALAPPFLAGS:N%=$(OPT_MODE)DN%)) 
OLOCALAPPFLAGS     = $(strip $(NLOCALAPPFLAGS:O%=$(OPT_MODE)DO%)) 
PLOCALAPPFLAGS     = $(strip $(OLOCALAPPFLAGS:P%=$(OPT_MODE)DP%)) 
QLOCALAPPFLAGS     = $(strip $(PLOCALAPPFLAGS:Q%=$(OPT_MODE)DQ%)) 
RLOCALAPPFLAGS     = $(strip $(QLOCALAPPFLAGS:R%=$(OPT_MODE)DR%)) 
SLOCALAPPFLAGS     = $(strip $(RLOCALAPPFLAGS:S%=$(OPT_MODE)DS%)) 
TLOCALAPPFLAGS     = $(strip $(SLOCALAPPFLAGS:T%=$(OPT_MODE)DT%)) 
ULOCALAPPFLAGS     = $(strip $(TLOCALAPPFLAGS:U%=$(OPT_MODE)DU%)) 
VLOCALAPPFLAGS     = $(strip $(ULOCALAPPFLAGS:V%=$(OPT_MODE)DV%)) 
WLOCALAPPFLAGS     = $(strip $(VLOCALAPPFLAGS:W%=$(OPT_MODE)DW%)) 
XLOCALAPPFLAGS     = $(strip $(WLOCALAPPFLAGS:X%=$(OPT_MODE)DX%)) 
YLOCALAPPFLAGS     = $(strip $(XLOCALAPPFLAGS:Y%=$(OPT_MODE)DY%)) 
ZLOCALAPPFLAGS     = $(strip $(YLOCALAPPFLAGS:Z%=$(OPT_MODE)DZ%)) 
#TMPLOCALAPPFLAGS  = $(ZLOCALAPPFLAGS)
TMPLOCALAPPFLAGS   = $(ZLOCALAPPFLAGS:++%=%)

ifeq ($(SYSTEMFAMILY), win32)
####################################################################
# WINDOWS
####################################################################
TRUELOCALAPPFLAGS  = $(strip $(TMPLOCALAPPFLAGS:-g=$(OPT_MODE)DDEBUG))
TRUELOCALAPPLDFLAGS = $(strip $(LOCALAPPLDFLAGS:-g=$(OPT_MODE)DEBUG))

ifneq ($(TARGET), )
	ifneq ($(BUILDMODE), -l)
	    TARGET     := $(TARGET).exe
	endif
endif

ifeq ($(APPMODE), -debug)
    APPFLAGS   = $(OPT_MODE)DDEBUG 
    APPLDFLAGS = $(OPT_MODE)DEBUG 
endif

ifeq ($(APPMODE), -profile)
    APPFLAGS   = $(OPT_MODE)DDEBUG 
    APPLDFLAGS = $(OPT_MODE)DEBUG 
endif

ifeq ($(APPMODE), -purify)
    APPFLAGS   = $(OPT_MODE)DDEBUG 
    APPLDFLAGS = $(OPT_MODE)DEBUG 
    PURIFYCMD  = purify
endif

BINAPPHEADER	   = $(strip $(SRCDIR:$(ROOTDIR)/%=$(BINDIR)/%))

APPEXCLUDEDHEADERS = 
APPLDLIBS          =

ifeq ($(SCS_USE_CPATH), no)
###############################################################
# COMPIL AVEC MSYS
###############################################################
#ifeq ($(WINDOWSBIN), yes)
#    LOCALAPPLDLIBS += $(ILVHOME)/lib/$(ILVARCHDIR)/$(ILVLIBDIR)/ilvmain.obj
#endif # (windowsbin) 

APPHEADERS         = $(OPT_MODE)I$(INCDIR) $(OPT_MODE)I$(BINDIR)/inc $(OPT_MODE)I$(SRCDIR) $(OPT_MODE)I$(BINAPPHEADER)
ifneq (,$(findstring angraphics, $(SRCDIR)))
APPHEADERS         = $(OPT_MODE)I$(INCDIR) $(OPT_MODE)I$(BINDIR)/inc $(OPT_MODE)I$(BINAPPHEADER)
endif
ifneq (,$(findstring antools/ant, $(SRCDIR)))
APPHEADERS         = $(OPT_MODE)I$(INCDIR) $(OPT_MODE)I$(BINDIR)/inc $(OPT_MODE)I$(BINAPPHEADER)
endif

APPLOADLIBES       = $(OPT_MODE)LIBPATH:${BINDIR}/lib

TRUELOCALAPPHEADERS   = $(strip $(LOCALAPPHEADERS:%=$(OPT_MODE)I%))
TRUELOCALAPPLOADLIBES = $(strip $(LOCALAPPLOADLIBES:%=$(OPT_MODE)$(OPT_LOAD_LIB)%))
#TRUELOCALAPPLDLIBS      = $(strip $(LOCALAPPLDLIBS:-l%=$(BINDIR)/lib/lib%.lib))
TRUELOCALAPPLDLIBS      = $(strip $(LOCALAPPLDLIBS:-l%=lib%.lib))
TRUELOCALAPPORBSERVEROBJS = $(LOCALAPPORBSERVEROBJS)
TRUELOCALAPPORBSOAPOBJS   = $(LOCALAPPORBSOAPOBJS)
TRUELOCALAPPORBCLIENTOBJS = $(LOCALAPPORBCLIENTOBJS)

TRUELOCALEXTRAOBJS = $(LOCALEXTRAOBJS)
TRUELOCALEXTRALDLIBS = $(LOCALEXTRALDLIBS)

TRUELOCALEXCLUDEDHEADERS  = $(strip $(LOCALEXCLUDEHEADERS:%=$(OPT_MODE)I%))

else  ## no cpath
###############################################################
# COMPIL AVEC CYGWIN
###############################################################
#ifeq ($(WINDOWSBIN), yes)
#    LOCALAPPLDLIBS += `cpath "$(ILVHOME)/lib/msvc6/stat_mda/ilvmain.obj"`	
#endif # (windowsbin) 

APPHEADERS         = $(OPT_MODE)I`cpath "$(INCDIR)"` $(OPT_MODE)I`cpath "$(BINDIR)/inc"` $(OPT_MODE)I`cpath "$(SRCDIR)"` $(OPT_MODE)I`cpath "$(BINAPPHEADER)"`
ifneq (,$(findstring angraphics, $(SRCDIR)))
APPHEADERS         = $(OPT_MODE)I`cpath "$(INCDIR)"` $(OPT_MODE)I`cpath "$(BINDIR)/inc"` $(OPT_MODE)I`cpath "$(BINAPPHEADER)"`
endif
ifneq (,$(findstring antools/ant, $(SRCDIR)))
APPHEADERS         = $(OPT_MODE)I`cpath "$(INCDIR)"` $(OPT_MODE)I`cpath "$(BINDIR)/inc"` $(OPT_MODE)I`cpath "$(BINAPPHEADER)"`
endif

APPLOADLIBES       = $(OPT_MODE)LIBPATH:`cpath "${BINDIR}/lib"`

TRUELOCALAPPHEADERS   = $(strip $(LOCALAPPHEADERS:%=$(OPT_MODE)I`cpath "%"`))
TRUELOCALAPPLOADLIBES = $(strip $(LOCALAPPLOADLIBES:%=$(OPT_MODE)$(OPT_LOAD_LIB)`cpath "%"`))
TMPTRUELOCALAPPLDLIBS   = $(strip $(LOCALAPPLDLIBS:%.obj=`cpath "%.obj"`))
TMP2TRUELOCALAPPLDLIBS   = $(strip $(TMPTRUELOCALAPPLDLIBS:%.res=`cpath "%.res"`))
TRUELOCALAPPLDLIBS      = $(strip $(TMP2TRUELOCALAPPLDLIBS:-l%=lib%.lib))
TRUELOCALAPPORBSERVEROBJS = $(strip $(LOCALAPPORBSERVEROBJS:%.obj=`cpath "%.obj"`))
TRUELOCALAPPORBSOAPOBJS   = $(strip $(LOCALAPPORBSOAPOBJS:%.obj=`cpath "%.obj"`))
TRUELOCALAPPORBCLIENTOBJS = $(strip $(LOCALAPPORBCLIENTOBJS:%.obj=`cpath "%.obj"`))

TMPTRUELOCALEXTRAOBJS = $(strip $(LOCALEXTRAOBJS:%.obj=`cpath "%.obj"`))
TRUELOCALEXTRAOBJS = $(strip $(TMPTRUELOCALEXTRAOBJS:%.obj=`cpath "%.res"`))
TRUELOCALEXTRALDLIBS = $(strip $(LOCALEXTRALDLIBS:%.lib=`cpath "%.lib"`))

TRUELOCALEXCLUDEDHEADERS  = $(strip $(LOCALEXCLUDEHEADERS:%=$(OPT_MODE)I`cpath "%"`))
endif ## no cpath

else   ### if not win32
#######################################################################################
# UNIX
#######################################################################################

TRUELOCALAPPFLAGS  = $(TMPLOCALAPPFLAGS)
TRUELOCALAPPLDFLAGS = $(LOCALAPPLDFLAGS)

ifeq ($(APPMODE), -debug)
    APPFLAGS   = -g
    APPLDFLAGS = -g
endif

ifeq ($(APPMODE), -profile)
    APPFLAGS   = -g -p
    APPLDFLAGS = -g -p
endif

ifeq ($(APPMODE), -purify)
    APPFLAGS   = -g
    APPLDFLAGS = -g
    PURIFYCMD  = purify
endif

BINAPPHEADER	   = $(strip $(SRCDIR:$(ROOTDIR)/%=$(BINDIR)/%))
APPHEADERS         = -I$(INCDIR) -I$(BINDIR)/inc -I$(SRCDIR) -I$(BINAPPHEADER)
APPEXCLUDEDHEADERS = 
APPLOADLIBES       = -L${BINDIR}/lib
APPLDLIBS          =

TRUELOCALAPPHEADERS   = $(strip $(LOCALAPPHEADERS:%=$(OPT_MODE)I%))
TRUELOCALAPPLOADLIBES = $(strip $(LOCALAPPLOADLIBES:%=$(OPT_MODE)L%))
TRUELOCALAPPLDLIBS    = $(LOCALAPPLDLIBS)
TRUELOCALAPPORBSERVEROBJS = $(LOCALAPPORBSERVEROBJS)
TRUELOCALAPPORBSOAPOBJS   = $(LOCALAPPORBSOAPOBJS)
TRUELOCALAPPORBCLIENTOBJS = $(LOCALAPPORBCLIENTOBJS)

TRUELOCALEXTRAOBJS = $(LOCALEXTRAOBJS)
TRUELOCALEXCLUDEDHEADERS  = $(strip $(LOCALEXCLUDEHEADERS:%=$(OPT_MODE)I%))
TRUELOCALEXTRALDLIBS = $(LOCALEXTRALDLIBS)

# MRE : compil CxPerf (+pa dans le cas des exe)
LOCALCXPERFFLAGS= -DUSE_CXPERF
LOCALCXPERFLDFLAGS=
ifeq ($(BUILDMODE), ) 
LOCALCXPERFFLAGS += +pa 
LOCALCXPERFLDFLAGS += +pa
endif

# Avoid unresolved symbols on OSF when compiling librarries so that
# all template symbols are kept (including template functions)
ifneq (,$(findstring OSF1V4.0, $(SYSTEM)))
ifeq ($(ORB_NAME), orbacus)

ifeq ($(BUILDMODE), -l) 
LOCALAPPFLAGS += -tlocal
UNRESOLVEDMOD  = warning
endif

endif
endif

endif # (win32)

# Retrieve Generic Configuration Makefile part
include $(CONFDIR)/stdconf.mak

