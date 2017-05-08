# ---------------------------------------------------------------------------
#          file: stdconf.mak
#   description: Standard configuration Makefile part
#      creation: 1997/01/21
#        author: dom
# ---------------------------------------------------------------------------
# $Id$
#
# $Log$
# ---------------------------------------------------------------------------
# This file defines parameters for all registered plateform according to
# SYSTEM and SYSTEMFAMILY environment variable values. It defines also
# external system packages which may be used bily applications.
#
# Registered plateforms:  SYSTEM       SYSTEMFAMILY
#                         ------------ ---------------
#                         OSF1V4.0     alpha_3
#                         OSF1V4.0-cxx65     alpha_3
#                         HP-UXB.11-ACC_64 	hp_11
#                         HP-UXB.11.23AA_64 	hp_11
#                         msvc6         win32 
#                         w2k-msvc6     win32  
#                         xp-msvc6      win32  
#                         w2k-msvc7     	win32  
#                         xp-msvc7      	win32 
#                         xp-msvc9      	win32 
#                         xp-msvc10      	win32 
#                         w2003-msvc7		win32
#                         Linux2.4.21		x86
#                         Linux2.6.18		x86
#
# External system packages:  xlib
#                            motif (WARNING: xlib required!!!)
#                            ilogviews[studio]
#                            ilogviews[X11] (WARNING needs xlib)
#                            animator
#                            roguewave
#                            orbacus
#                            TAO
#                            xerces 
#                            tcl 
#                            jdk
#                            tk 
#                            oracle
#                            javascript
#                            python
#                            tsopc
#                            lightopc
#                            CppUnit
#                            Xalan
#                            pcre
#                            SCADAsoft
#                            ATSsoft
#  add OPTIM option for OSF1V4.0
# ---------------------------------------------------------------------------

ifneq ($(SYSTEMFAMILY), win32)
  ARFLAGS = crsu
else
  AR= link.exe -lib
  ARFLAGS = $(OPT_MODE)nologo
endif

# Define the IDL compiler (idlsoap)
ORBIDL=scsidlsoap
# Define the Proxy Generator (idlgen)
IDLGEN=scsidlgen

ifeq ($(SYSTEMFAMILY), win32)
	ifeq ($(OSTYPE), msys)
	# Define the IDL compiler (idlsoap)
	ORBIDL=scsidlsoap
	# Define the Proxy Generator (idlgen)
	IDLGEN=scsidlgen
	else
	# Define the IDL compiler (idlsoap)
  ORBIDL=scsidlsoap.bat
# Define the Proxy Generator (idlgen)
  IDLGEN=scsidlgen.bat
	endif
endif

# Test if "debug" file exists, then force DEBUG compilation
ifeq ($(wildcard debug),debug)
  USE_OPTIM=0
  NT_DEBUG=yes
endif

# DLA on 10/28th/2003: automatically detect IV version
VIEWS_CFLAGS := VIEWS_VERSION=400
ifneq (,$(findstring 5, $(ILVHOME)))
    VIEWS_CFLAGS := VIEWS_VERSION=500
    ifneq (,$(findstring 52, $(ILVHOME)))
      VIEWS_CFLAGS := VIEWS_VERSION=520
    endif
    ifneq (,$(findstring 53, $(ILVHOME)))
      VIEWS_CFLAGS := VIEWS_VERSION=530
    endif
    ifneq (,$(findstring 54, $(ILVHOME)))
      VIEWS_CFLAGS := VIEWS_VERSION=540
    endif
endif

#IDE 03/05/2004 : Automatically detect XML Version
XML_CFLAGS := XML_VERSION=3
ifneq (,$(findstring 5, $(XMLHOME)))
    XML_CFLAGS := XML_VERSION=5
endif
ifneq (,$(findstring xerces, $(XMLHOME)))
    XML_CFLAGS := XML_VERSION=5
endif

# Define specific options for all registered plateform
# ====================================================
# (compiler invocation, Motif, Xlib and XRunner packages)
#

################################################################################
#                vvvvvvvv
ifeq ($(SYSTEM), OSF1V4.0)
#                ^^^^^^^^
  ifeq ($(USE_INSURE), 1)

    CXX           := insure -Zop /nassvr1/softs/insure/.psrc

  else

    ifeq ($(ORB_NAME), orbix)
      ifeq ($(OSFCXX), new)
	CXX := cxx
      else
	CXX := cxx -oldcxx
      endif
    else
      CXX          := cxx
    endif

  endif #USE_INSURE#

  ifeq ($(USE_OPTIM), 1)

    ifeq ($(WITHOUTTHREAD), yes)

      ifeq ($(ORB_NAME), orbix)
        CXXCFLAGS    := -DOSF1 -D__unix__ 
      else
        CXXCFLAGS    := -DOSF1 -D__unix__ -DSCS_STD_OSF1V4_0 -DRW_MULTI_THREAD  -strong_volatile
      endif

      CCFLAGS      := -DOSF1 -D__unix__ 

    else

      ifeq ($(ORB_NAME), orbix)
        CXXCFLAGS    := -pthread -D_REENTRANT -DOSF1 -D__unix__ 
      else
        CXXCFLAGS    := -pthread -D_REENTRANT -DOSF1 -D__unix__ -DSCS_STD_OSF1V4_0 -DRW_MULTI_THREAD  -strong_volatile
      endif

      CCFLAGS      := -pthread -D_REENTRANT -DOSF1 -D__unix__ -g
    endif #WITHOUTTHREAD#

  else

    ifeq ($(WITHOUTTHREAD), yes)

      ifeq ($(ORB_NAME), orbix)
        CXXCFLAGS    := -DOSF1 -D__unix__ -g
      else
        CXXCFLAGS    := -DOSF1 -D__unix__ -DSCS_STD_OSF1V4_0 -DRW_MULTI_THREAD -g -strong_volatile
      endif

      CCFLAGS      := -DOSF1 -D__unix__ -g

    else

      ifeq ($(ORB_NAME), orbix)
        CXXCFLAGS    := -pthread -D_REENTRANT -DOSF1 -D__unix__ -g
      else
        CXXCFLAGS    := -pthread -D_REENTRANT -DOSF1 -D__unix__ -DSCS_STD_OSF1V4_0 -DRW_MULTI_THREAD -g -strong_volatile
      endif

      CCFLAGS      := -pthread -D_REENTRANT -DOSF1 -D__unix__ -g

    endif #WITHOUTTHREAD#

  endif #USE_OPTIM#

  CXXCFLAGS    += -D$(VIEWS_CFLAGS)
  CXXHEADERS   := -I/usr/include
  CCHEADERS    := -I/usr/include

  ifeq ($(USE_INSURE), 1)
    LD           := insure 
  else
    LD           := $(CXX) 
  endif

  CXXLDFLAGS   :=
  CXXLOADLIBES := -L/usr/lib 
  CXXLDLIBS    := -g -pthread -lpthread -lexc -so_archive -lrt -ldnet_stub -lm

  ifeq ($(OLDC), yes)
    CANSIFLAG	 :=
  else
    CANSIFLAG	 := -std1
  endif

  CXXPROFILFLG := -pg
  OBJECT_EXTENSION := o

  ifeq ($(USE_CXPERF), 1)
    CXXLDFLAGS += $(CXXPROFILFLG)
  endif

  CXXFLAGS += -D$(VIEWS_CFLAGS)

  ifeq ($(UNRESOLVEDMOD), warning)
    SHARED_FLAGS     := -shared -warning_unresolved
  else
    SHARED_FLAGS     :=  -shared -error_unresolved
  endif

  SHARED_EXTENSION := so

  OPT_MODE      := -
  OPT_LOAD_LIB  := L

  XLIBFLAGS     :=
  XLIBHEADERS   :=
  XLIBLDFLAGS   :=
  XLIBLOADLIBES :=
  XLIBLDLIBS    := -lX11

  MOTIFFLAGS     :=
  MOTIFHEADERS   :=
  MOTIFLDFLAGS   :=
  MOTIFLOADLIBES :=
  MOTIFLDLIBS    := -lXm -lXt

  XRFLAGS     :=
  XRHEADERS   :=
  XRLDFLAGS   :=
  XRLOADLIBES :=
  XRLDLIBS    :=

  RTAPLIBDIR  := rtap70

  GENLIB      := 

  RSH := rsh

endif #OSF1V4.0#

################################################################################

#                vvvvvvvv
ifeq ($(SYSTEM), OSF1V4.0-cxx65)
#                ^^^^^^^^
  ifeq ($(USE_INSURE), 1)
    CXX           := insure -Zop /nassvr1/softs/insure/.psrc
  else
    CXX          := cxx
  endif

  ifeq ($(USE_OPTIM), 1)

    ifeq ($(WITHOUTTHREAD), yes)
      CXXCFLAGS    := -DOSF1 -D__unix__ -DSCS_STD_OSF1V4_0 -DRW_MULTI_THREAD  -strong_volatile
      CCFLAGS      := -DOSF1 -D__unix__ 
    else
      CXXCFLAGS    := -pthread -D_REENTRANT -DOSF1 -D__unix__ -DSCS_STD_OSF1V4_0 -DRW_MULTI_THREAD  -strong_volatile
      CCFLAGS      := -pthread -D_REENTRANT -DOSF1 -D__unix__ -g
    endif

  else

    ifeq ($(WITHOUTTHREAD), yes)
      CXXCFLAGS    := -DOSF1 -D__unix__ -DSCS_STD_OSF1V4_0 -DRW_MULTI_THREAD -g -strong_volatile
      CCFLAGS      := -DOSF1 -D__unix__ -g
    else
      CXXCFLAGS    := -pthread -D_REENTRANT -DOSF1 -D__unix__ -DSCS_STD_OSF1V4_0 -DRW_MULTI_THREAD -g -strong_volatile
      CCFLAGS      := -pthread -D_REENTRANT -DOSF1 -D__unix__ -g
    endif

  endif #USE_OPTIM#

  CXXFLAGS += -D$(VIEWS_CFLAGS)

  CXXHEADERS   := -I/usr/include
  CCHEADERS    := -I/usr/include

  ifeq ($(USE_INSURE), 1)
    LD           := insure 
  else
    LD           := $(CXX) 
  endif

  CXXLDFLAGS   := -nocxxstd
  CXXLOADLIBES := -L/usr/lib 
  CXXLDLIBS    := -g -pthread -lpthread -lexc -so_archive -lrt -ldnet_stub -lm

  ifeq ($(OLDC), yes)
    CANSIFLAG	 :=
  else
    CANSIFLAG	 := -std1
  endif

  CXXPROFILFLG := -pg
  OBJECT_EXTENSION := o

  ifeq ($(USE_CXPERF), 1)
    CXXLDFLAGS += $(CXXPROFILFLG)
  endif

  ifeq ($(UNRESOLVEDMOD), warning)
    SHARED_FLAGS     := -shared -warning_unresolved
  else
    SHARED_FLAGS     := -shared -error_unresolved
  endif

  SHARED_EXTENSION := so
  STLIB_EXTENSION := a

  OPT_MODE      := -
  OPT_LOAD_LIB  := L

  XLIBFLAGS     :=
  XLIBHEADERS   :=
  XLIBLDFLAGS   :=
  XLIBLOADLIBES :=
  XLIBLDLIBS    := -lX11

  MOTIFFLAGS     :=
  MOTIFHEADERS   :=
  MOTIFLDFLAGS   :=
  MOTIFLOADLIBES :=
  MOTIFLDLIBS    := -lXm -lXt

  XRFLAGS     :=
  XRHEADERS   :=
  XRLDFLAGS   :=
  XRLOADLIBES :=
  XRLDLIBS    :=

  RTAPLIBDIR  := rtap70

  GENLIB      := 

  RSH := rsh

endif #OSF1V4.0-cxx65#

################################################################################
#                vvvvvvvvv
ifeq ($(SYSTEM), Linux2.4.21)
#                ^^^^^^^^^

    CC           := gcc
    CXX          := g++
    CXXCFLAGS    := -pipe -fexceptions -fPIC -D_REENTRANT -D_CMA_NOWRAPPERS_ -Wno-deprecated -D__SCSLINUX__ -DSCS_STD -DAN_STD -DRW_MULTI_THREAD 

    CXXHEADERS   :=
    CCFLAGS      := -pipe -fexceptions -fPIC -D_REENTRANT -D_CMA_NOWRAPPERS_ -Wno-deprecated -D__SCSLINUX__ -DSCS_STD -DAN_STD -DRW_MULTI_THREAD
    CCHEADERS    :=
    LD           := $(CXX)
    CXXLDFLAGS   :=

    CXXLOADLIBES :=
    CXXLDLIBS    := -lm -ldl -lpthread
    CANSIFLAG    :=
    CXXPROFILFLG := -G
    OBJECT_EXTENSION := o

    SHARED_FLAGS     := -shared
    SHARED_EXTENSION := so
    STLIB_EXTENSION := a

    OPT_MODE      := -
    OPT_LOAD_LIB  := L

    XLIBFLAGS     :=
    XLIBHEADERS   :=
    XLIBLDFLAGS   :=
    XLIBLOADLIBES := -L/usr/X11R6/lib
    XLIBLDLIBS    := -lX11

    MOTIFFLAGS     :=
    MOTIFHEADERS   :=
    MOTIFLDFLAGS   :=
    MOTIFLOADLIBES :=
    MOTIFLDLIBS    := -lX11

    XRFLAGS     :=
    XRHEADERS   :=
    XRLDFLAGS   :=

    XRLOADLIBES :=
    XRLDLIBS    :=

    RTAPLIBDIR  :=

    ALLOCALIB   :=
    GENLIB      :=

    MDB_GENI := mdb_geni

    RSH := rsh

ifeq ($(USE_GCOV), 1)
CXXCFLAGS    := -fprofile-arcs -ftest-coverage $(CXXCFLAGS)
CCFLAGS      := -fprofile-arcs -ftest-coverage $(CCFLAGS)
CXXLDFLAGS   := -fprofile-arcs -ftest-coverage $(CXXLDFLAGS)
SHARED_FLAGS     := $(SHARED_FLAGS) -static-libgcc
endif

ifeq ($(USE_OPTIM), 1)
    CXXCFLAGS    := -g0 $(CXXCFLAGS)
    CCFLAGS      := -g0 $(CCFLAGS)
else
    CXXCFLAGS    := -g3 $(CXXCFLAGS)
    CCFLAGS      := -g3 $(CCFLAGS)
    ifeq ($(USE_GPROF), 1)
        CXXCFLAGS    := -pg $(CXXCFLAGS)
        CCFLAGS      := -pg $(CCFLAGS)
        CXXLDFLAGS   := -pg $(CXXLDFLAGS)
    endif
endif

#CXXCFLAGS    += -D$(VIEWS_CFLAGS)

endif


################################################################################

#                vvvvvvvvv
ifeq ($(SYSTEM), Linux2.6.18)
#                ^^^^^^^^^

# -m32 required when executed on x86_64 machines
    CC           := gcc -m32
    CXX          := g++ -m32
    CXXCFLAGS    := -pipe -fexceptions -fPIC -D_REENTRANT -D_CMA_NOWRAPPERS_ -Wno-deprecated -D__SCSLINUX__ -DSCS_STD -DAN_STD -DRW_MULTI_THREAD 

    CXXHEADERS   :=
    CCFLAGS      := -pipe -fexceptions -fPIC -D_REENTRANT -D_CMA_NOWRAPPERS_ -Wno-deprecated -D__SCSLINUX__ -DSCS_STD -DAN_STD -DRW_MULTI_THREAD
    CCHEADERS    :=
    LD           := $(CXX)
    CXXLDFLAGS   :=

    CXXLOADLIBES :=
    CXXLDLIBS    := -lm -ldl -lpthread
    CANSIFLAG    :=
    CXXPROFILFLG := -G
    OBJECT_EXTENSION := o

    SHARED_FLAGS     := -shared
    SHARED_EXTENSION := so
    STLIB_EXTENSION := a

    OPT_MODE      := -
    OPT_LOAD_LIB  := L

    XLIBFLAGS     :=
    XLIBHEADERS   :=
    XLIBLDFLAGS   :=
    XLIBLOADLIBES := -L/usr/X11R6/lib
    XLIBLDLIBS    := -lX11

    MOTIFFLAGS     :=
    MOTIFHEADERS   :=
    MOTIFLDFLAGS   :=
    MOTIFLOADLIBES :=
    MOTIFLDLIBS    := -lX11

    XRFLAGS     :=
    XRHEADERS   :=
    XRLDFLAGS   :=

    XRLOADLIBES :=
    XRLDLIBS    :=

    RTAPLIBDIR  :=

    ALLOCALIB   :=
    GENLIB      :=

    MDB_GENI := mdb_geni

    RSH := rsh

ifeq ($(USE_GCOV), 1)
CXXCFLAGS    := -fprofile-arcs -ftest-coverage $(CXXCFLAGS)
CCFLAGS      := -fprofile-arcs -ftest-coverage $(CCFLAGS)
CXXLDFLAGS   := -fprofile-arcs -ftest-coverage $(CXXLDFLAGS)
SHARED_FLAGS     := $(SHARED_FLAGS) -static-libgcc
endif

ifeq ($(USE_OPTIM), 1)
    CXXCFLAGS    := -g0 $(CXXCFLAGS)
    CCFLAGS      := -g0 $(CCFLAGS)
else
    CXXCFLAGS    := -g3 $(CXXCFLAGS)
    CCFLAGS      := -g3 $(CCFLAGS)
    ifeq ($(USE_GPROF), 1)
        CXXCFLAGS    := -pg $(CXXCFLAGS)
        CCFLAGS      := -pg $(CCFLAGS)
        CXXLDFLAGS   := -pg $(CXXLDFLAGS)
    endif
endif

#CXXCFLAGS    += -D$(VIEWS_CFLAGS)

  endif


################################################################################

#                vvvvvvvvv
ifeq ($(SYSTEM), HP-UXB.11-ACC_64)
#                ^^^^^^^^^
  CC           := cc

  ifeq ($(USE_INSURE), 1)
    CXX           := insure -Zop /nassvr1/users/scadadev/parasoft/insure/.psrc
  else
    CXX          := aCC
  endif

  CXXCFLAGS    := -D$(VIEWS_CFLAGS) -z +DA2.0W -DSCS_STD_HPUX11 -D_REENTRANT -D_HPUX_SOURCE -DRWSTD_MULTI_THREAD -DRW_MULTI_THREAD -DIT_POSIX_THREADS -D_THREAD_SAFE -DHPUX -DHPUX11 -D__unix__ 
  CXXHEADERS   := -I/opt/aCC/include -I/usr/include
  CCFLAGS      := +DA2.0W -D_REENTRANT -DIT_POSIX_THREADS -D_THREAD_SAFE -D_CMA_NOWRAPPERS_ -DHPUX -DHPUX11 -D__unix__
  CCHEADERS    := -I/usr/include

  ifeq ($(USE_OPTIM), 1)
    CXXCFLAGS    := -fast $(CXXCFLAGS)
    CCFLAGS	 := -fast $(CCFLAGS)
  else
    CXXCFLAGS    := -g0 $(CXXCFLAGS)
    CCFLAGS	 := -g $(CCFLAGS)
  endif

  ifeq ($(USE_CXPERF), 1)
    CXXCFLAGS    := $(LOCALCXPERFFLAGS) $(CXXCFLAGS)
    CCFLAGS	 := $(LOCALCXPERFFLAGS) $(CCFLAGS)
  endif

  ifeq ($(USE_INSURE), 1)
    LD           := insure 
  else
    LD           := $(CXX)
  endif

  ifeq ($(USE_PURIFY), 1)
    CXXCFLAGS    := -g0 $(CXXCFLAGS)
    CCFLAGS	 := -g $(CCFLAGS)
    CXX          := /nassvr1/users/scadadev/rational/purifyplus/releases/purify.hp.2002.05.00/purify aCC
    LD          := /nassvr1/users/scadadev/rational/purifyplus/releases/purify.hp.2002.05.00/purify aCC
  endif

  ifeq ($(PRODUCTION), 1)
    SCADA_VERSION := $(shell findversion.ksh)
    LDOPTIONS := +b,/opt/scadasoft/$(SCADA_VERSION)/$(SYSTEM)/lib:/opt/orbacus/$(ORB_VERSION)/lib:/usr/lib/pa20_64:/opt/langtools/lib/pa20_64
  else
  ifeq ($(PRODUCTION), 2)
    SCADA_VERSION := $(shell findversion.ksh)
    LDOPTIONS := +b,/opt/scadasoft/$(SCADA_VERSION)/$(SYSTEM)/lib:/opt/orbacus/$(ORB_VERSION)/lib:/usr/lib/pa20_64:/opt/langtools/lib/pa20_64
  else
    LDOPTIONS := +s
  endif
  endif

  CXXLDFLAGS   := +DA2.0W -Wl,$(LDOPTIONS),-B,immediate,-B,nonfatal,-B,verbose
  SHARED_FLAGS := +DA2.0W -b -Wl,$(LDOPTIONS)

  ifeq ($(USE_OPTIM), 1)
    CXXLDFLAGS   := -fast $(CXXLDFLAGS)
    SHARED_FLAGS := -fast $(SHARED_FLAGS)
    CXXPROFILFLG := -G
  else
    CXXLDFLAGS   := -g0 $(CXXLDFLAGS)
    SHARED_FLAGS := -g0 $(SHARED_FLAGS)
    CXXPROFILFLG := -G
  endif

  ifeq ($(USE_CXPERF), 1)
    CXXLDFLAGS   := $(LOCALCXPERFFLAGS) $(CXXLDFLAGS)
    SHARED_FLAGS := $(LOCALCXPERFFLAGS) $(SHARED_FLAGS)
    CXXPROFILFLG :=
  endif

  ifeq ($(USE_WDB), 1)
    CXXLDFLAGS := $(CXXLDFLAGS) -L/opt/langtools/lib/pa20_64 -lrtc
  endif

  CXXLOADLIBES := 
  CXXLDLIBS    := -lpthread -lm
  CANSIFLAG	 := 
  OBJECT_EXTENSION := o

  SHARED_EXTENSION := sl
  STLIB_EXTENSION := a

  OPT_MODE      := -
  OPT_LOAD_LIB  := L

  XLIBFLAGS     :=
  XLIBHEADERS   := 
  XLIBLDFLAGS   := -L/usr/lib/X11R6/pa20_64
  XLIBLOADLIBES :=
  XLIBLDLIBS    := -lX11

  MOTIFFLAGS     :=
  MOTIFHEADERS   := -I/usr/include/Motif1.2
  MOTIFLDFLAGS   := -L/usr/lib/X11R6/pa20_64
  MOTIFLOADLIBES :=
  MOTIFLDLIBS    := -lXt -lXm

  XRFLAGS     :=
  XRHEADERS   :=
  XRLDFLAGS   :=
  XRLOADLIBES :=
  XRLDLIBS    :=

  RTAPLIBDIR  := rtap80

  ALLOCALIB   := #-lalloca
  GENLIB      := -lgen

  RSH := remsh

endif #HP-UXB.11-ACC_64#

################################################################################
#                vvvvvvvvv
ifeq ($(SYSTEM), HP-UXB.11.23AA_64)
#                ^^^^^^^^^
  CC           := cc
  AR           := ar
  ifeq ($(USE_INSURE), 1)
    CXX           := insure -Zop /nassvr1/users/scadadev/parasoft/insure/.psrc
  else
    CXX          := aCC
  endif

  CXXCFLAGS    := -z +DD64 -mt -DSCS_STD -DAN_STD -DIT_POSIX_THREADS -DHPUX -DHPUX11 -D__unix__ -D$(VIEWS_CFLAGS)

  CXXHEADERS   := 
  CCFLAGS      := +DD64 -mt -DIT_POSIX_THREADS -DHPUX -DHPUX11 -D__unix__
  CCHEADERS    := 

  ifeq ($(USE_OPTIM), 1)
    CXXCFLAGS    := -fast $(CXXCFLAGS)
    CCFLAGS	 := -fast $(CCFLAGS)
  else
    CXXCFLAGS    := -g0 $(CXXCFLAGS)
    CCFLAGS	 := -g $(CCFLAGS)
  endif

  ifeq ($(USE_CXPERF), 1)
    CXXCFLAGS    := $(LOCALCXPERFFLAGS) $(CXXCFLAGS)
    CCFLAGS	 := $(LOCALCXPERFFLAGS) $(CCFLAGS)
  endif

  ifeq ($(USE_INSURE), 1)
    LD           := insure 
  else
    LD           := $(CXX)
  endif

  ifeq ($(USE_PURIFY), 1)
    LD          := /nassvr1/users/scadadev/rational/purifyplus/releases/purify.hp.2002.05.00/purify aCC
  endif

  ifeq ($(PRODUCTION), 1)
    SCADA_VERSION := $(shell findversion.ksh)
    LDOPTIONS := +b,/opt/scadasoft/$(SCADA_VERSION)/$(SYSTEM)/lib:/opt/orbacus/$(ORB_VERSION)/lib:/usr/lib/hpux64
  else
  ifeq ($(PRODUCTION), 2)
    SCADA_VERSION := $(shell findversion.ksh)
    LDOPTIONS := +b,/opt/scadasoft/$(SCADA_VERSION)/$(SYSTEM)/lib:/opt/orbacus/$(ORB_VERSION)/lib:/usr/lib/hpux64
  else
    LDOPTIONS := +s
  endif
  endif

  CXXLDFLAGS   := +DD64 -mt -DSCS_STD -DAN_STD -Wl,$(LDOPTIONS),-B,immediate,-B,nonfatal,-B,verbose
  SHARED_FLAGS := +DD64 -mt -DSCS_STD -DAN_STD -b -Wl,$(LDOPTIONS)

  ifeq ($(USE_WDB), 1)
    CXXLDFLAGS := $(CXXLDFLAGS) -L/opt/langtools/lib/hpux64 
    CXXLDLIBS    := $(CXXLDLIBS) -lrtc
  endif

  ifeq ($(USE_OPTIM), 1)
    CXXLDFLAGS   := -fast $(CXXLDFLAGS)
    SHARED_FLAGS := -fast $(SHARED_FLAGS)
    CXXPROFILFLG := -G
  else
    CXXLDFLAGS   := -g0 $(CXXLDFLAGS)
    SHARED_FLAGS := -g0 $(SHARED_FLAGS)
    CXXPROFILFLG := -G
  endif

  ifeq ($(USE_CXPERF), 1)
    CXXLDFLAGS   := $(LOCALCXPERFFLAGS) $(CXXLDFLAGS)
    SHARED_FLAGS := $(LOCALCXPERFFLAGS) $(SHARED_FLAGS)
    CXXPROFILFLG :=
  endif

  CXXLOADLIBES := 
  CXXLDLIBS    := $(CXXLDLIBS) -lm
  CANSIFLAG	 := 
  OBJECT_EXTENSION := o

  SHARED_EXTENSION := so
  STLIB_EXTENSION := a
  LIB_EXTENSION := so

  OPT_MODE      := -
  OPT_LOAD_LIB  := L

  XLIBFLAGS     :=
  XLIBHEADERS   := 
  XLIBLDFLAGS   :=
  XLIBLOADLIBES :=
  XLIBLDLIBS    := -lX11

  MOTIFFLAGS     :=
  MOTIFHEADERS   := -I/usr/include/Motif1.2
  MOTIFLDFLAGS   :=
  MOTIFLOADLIBES :=
  MOTIFLDLIBS    := -lXm -lXt

  XRFLAGS     :=
  XRHEADERS   :=
  XRLDFLAGS   :=
  XRLOADLIBES :=
  XRLDLIBS    :=

  RTAPLIBDIR  :=

  ALLOCALIB   := 
  GENLIB      := -lgen

  RSH := remsh

endif #HP-UXB.11.23AA_64#

################################################################################



#                      vvvvv
ifeq ($(SYSTEMFAMILY), win32)
#                      ^^^^^
  CC		 := cl.exe    
  MDB_GENI := mdb_geni.exe
    CXX          := cl.exe
  OPT_MODE     := -

  ifneq (,$(NOSYSTFLAG))
	CXXCFLAGS    := $(OPT_MODE)DWIN32 $(OPT_MODE)D_WINDOWS $(OPT_MODE)nologo $(OPT_MODE)W3 $(OPT_MODE)c
  else
  	CXXCFLAGS    := $(OPT_MODE)DMSDOS $(OPT_MODE)D_WINNT $(OPT_MODE)DWIN32 $(OPT_MODE)D_WINDOWS $(OPT_MODE)D_REENTRANT \
			$(OPT_MODE)D$(VIEWS_CFLAGS) $(OPT_MODE)DSCS_STD $(OPT_MODE)DAN_STD  \
			$(OPT_MODE)nologo $(OPT_MODE)MD $(OPT_MODE)W3 $(OPT_MODE)GR $(OPT_MODE)c 
  endif

  CXXCFLAGS += $(OPT_MODE)DRWDLL $(OPT_MODE)DRW_MULTI_THREAD 
  ifeq ($(NT_DEBUG), yes)
	CXXCFLAGS  += $(OPT_MODE)Z7 $(OPT_MODE)DEBUG
  else
	CXXCFLAGS  += $(OPT_MODE)Og
    endif 

  ifeq ($(SYSTEM), w2k-msvc7)
     CXXCFLAGS    += $(OPT_MODE)EHsc $(OPT_MODE)D_CRT_NONSTDC_NO_DEPRECATE \
                     $(OPT_MODE)wd4251 $(OPT_MODE)wd4244 $(OPT_MODE)wd4018 $(OPT_MODE)wd4800 $(OPT_MODE)wd4275 $(OPT_MODE)wd4251 $(OPT_MODE)D_CRT_SECURE_NO_DEPRECATE
  else
     ifeq ($(SYSTEM), xp-msvc7)
     CXXCFLAGS    += $(OPT_MODE)EHsc $(OPT_MODE)D_CRT_NONSTDC_NO_DEPRECATE \
                     $(OPT_MODE)wd4251 $(OPT_MODE)wd4244 $(OPT_MODE)wd4018 $(OPT_MODE)wd4800 $(OPT_MODE)wd4275 $(OPT_MODE)wd4251 $(OPT_MODE)D_CRT_SECURE_NO_DEPRECATE
     else
        ifeq ($(SYSTEM), w2003-msvc7)
        CXXCFLAGS    += $(OPT_MODE)EHsc $(OPT_MODE)D_CRT_NONSTDC_NO_DEPRECATE \
                     $(OPT_MODE)wd4251 $(OPT_MODE)wd4244 $(OPT_MODE)wd4018 $(OPT_MODE)wd4800 $(OPT_MODE)wd4275 $(OPT_MODE)wd4251 $(OPT_MODE)D_CRT_SECURE_NO_DEPRECATE
        else
          ifeq ($(SYSTEM), xp-msvc9)
          CXXCFLAGS    += $(OPT_MODE)EHac $(OPT_MODE)D_CRT_NONSTDC_NO_DEPRECATE $(OPT_MODE)D_SCL_SECURE_NO_WARNINGS\
                     $(OPT_MODE)wd4251 $(OPT_MODE)wd4244 $(OPT_MODE)wd4018 $(OPT_MODE)wd4800 $(OPT_MODE)wd4275 $(OPT_MODE)wd4251 $(OPT_MODE)wd4290 $(OPT_MODE)D_CRT_SECURE_NO_DEPRECATE
          else
            ifeq ($(SYSTEM), xp-msvc10)
            CXXCFLAGS    += $(OPT_MODE)EHac $(OPT_MODE)D_CRT_NONSTDC_NO_DEPRECATE  $(OPT_MODE)D_SCL_SECURE_NO_WARNINGS\
                        $(OPT_MODE)wd4251 $(OPT_MODE)wd4244 $(OPT_MODE)wd4018 $(OPT_MODE)wd4800 $(OPT_MODE)wd4275 $(OPT_MODE)wd4251 $(OPT_MODE)wd4290 $(OPT_MODE)D_CRT_SECURE_NO_DEPRECATE
  else
              ifeq ($(NT_DEBUG), yes)
               CXXCFLAGS  += $(OPT_MODE)Yd
              endif
              CXXCFLAGS    += $(OPT_MODE)GX 
              ifneq (,$(COMMENT))
                LINK32_FLAGS += $(OPT_MODE)COMMENT:"$(COMMENT)"
              endif

              ifneq (,$(VERSION))
                 LINK32_FLAGS += $(OPT_MODE)VERSION:$(VERSION)
    endif

  endif
          endif
        endif
  endif
  endif

  CXXHEADERS   :=
  
  ifneq (,$(NOSYSTFLAG))
	CCFLAGS      := $(OPT_MODE)DWIN32 $(OPT_MODE)D_WINDOWS $(OPT_MODE)nologo $(OPT_MODE)W3 $(OPT_MODE)Od $(OPT_MODE)c
  else
	CCFLAGS      := $(OPT_MODE)D_WINNT $(OPT_MODE)DWIN32 $(OPT_MODE)D_WINDOWS  \
			$(OPT_MODE)DSCS_STD $(OPT_MODE)DAN_STD \
			$(OPT_MODE)DILVSTD $(OPT_MODE)DRWDLL $(OPT_MODE)DRW_NO_STL \
			$(OPT_MODE)nologo $(OPT_MODE)MD $(OPT_MODE)W3 $(OPT_MODE)GR  \
			$(OPT_MODE)Od
  endif

  ifeq ($(NT_DEBUG), yes)
	CCFLAGS  += $(OPT_MODE)Z7
  endif

  ifeq ($(SYSTEM), w2k-msvc7)
     CCFLAGS    += $(OPT_MODE)EHsc $(OPT_MODE)D_CRT_SECURE_NO_DEPRECATE
  else
     CCFLAGS    += $(OPT_MODE)GX $(OPT_MODE)Yd
  endif

  ifneq ($(WITHOUTWINNTSPEC), yes)
    CXXCFLAGS  += $(OPT_MODE)D_WIN32_WINNT=0x0400
    CCFLAGS    += $(OPT_MODE)D_WIN32_WINNT=0x0400
  else
    ifneq (,$(findstring w2k, $(SYSTEM)))
      CXXCFLAGS  += $(OPT_MODE)D_WIN32_WINNT=0x0500
      CCFLAGS    += $(OPT_MODE)D_WIN32_WINNT=0x0500
    endif
    ifneq (,$(findstring xp, $(SYSTEM)))
      CXXCFLAGS  += $(OPT_MODE)D_WIN32_WINNT=0x0501
      CCFLAGS    += $(OPT_MODE)D_WIN32_WINNT=0x0501
    endif
    ifneq (,$(findstring w2003, $(SYSTEM)))
      CXXCFLAGS  += $(OPT_MODE)D_WIN32_WINNT=0x0502
      CCFLAGS    += $(OPT_MODE)D_WIN32_WINNT=0x0502
  endif
  endif	

  CCHEADERS    :=

  ifeq ($(SCS_DLL), yes)
	CXXCFLAGS += $(OPT_MODE)DSCS_DLL
	CCFLAGS += $(OPT_MODE)DSCS_DLL
    endif

  ifneq (,$(NOSYSTLIB))
	SYSTLIB      := 
  else
  	SYSTLIB := wsock32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib \
		advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib
  endif

  LINK32_FLAGS := $(SYSTLIB) $(OPT_MODE)machine:I386 $(OPT_MODE)incremental:no $(OPT_MODE)fixed:no

    ifeq ($(WINDOWSBIN), yes)
      LINK32_FLAGS += $(OPT_MODE)subsystem:windows
    else
      LINK32_FLAGS += $(OPT_MODE)subsystem:console
    endif

  ifeq ($(NT_DEBUG), yes)
      LINK32_FLAGS += $(OPT_MODE)debug
  endif	



    LD           := link.exe $(LINK32_FLAGS)

  BSC32_FLAGS  := $(OPT_MODE)nologo $(OPT_MODE)o
  BSC32        := bscmake.exe $(BSC32_FLAGS)	

  CXXLDFLAGS   := $(OPT_MODE)nologo 
  CXXLOADLIBES := 
  CXXLDLIBS    := 
  CANSIFLAG	 := 
  CXXPROFILFLG := 
  OBJECT_EXTENSION := obj

  
  OPT_LOAD_LIB  := LIBPATH:

  SHARED_FLAGS     := $(OPT_MODE)nologo
  ifeq ($(SCS_DLL), yes)
  SHARED_EXTENSION := dll
  else
    SHARED_EXTENSION := lib
  endif
  
  LIB_EXTENSION    := lib
  STLIB_EXTENSION := lib

  XLIBFLAGS     :=
  XLIBHEADERS   := 
  XLIBLDFLAGS   :=
  XLIBLOADLIBES :=
  XLIBLDLIBS    := 

  MOTIFFLAGS     :=
  MOTIFHEADERS   := 
  MOTIFLDFLAGS   :=
  MOTIFLOADLIBES :=
  MOTIFLDLIBS    :=

  XRFLAGS     :=
  XRHEADERS   :=
  XRLDFLAGS   :=
  XRLOADLIBES :=
  XRLDLIBS    :=

  RTAPLIBDIR  := 

  ALLOCALIB   :=
  GENLIB      :=

  RSH := 

endif #win32#

################################################################################

# Define optional external system packages
# ========================================
# (according to APPMODE)
#
#                  vvvvvvv
ifeq ($(APPMODE), -xrunner)
#                  ^^^^^^^
  PACKAGESFLAGS     += $(XRFLAGS)
  PACKAGESHEADERS   += $(XRHEADERS)
  PACKAGESLDFLAGS   += $(XRLDFLAGS)
  PACKAGESLOADLIBES += $(XRLOADLIBES)
  PACKAGESLDLIBS    += $(XRLDLIBS)
endif

# For new ascmanager developments
# User can use new ascmanager by defining
# environment variable ASC with asc2
# ========================================
ifeq ($(ASC), )
  ASC	      = asc2
endif

# Define external system packages
# ===============================
# (according to APPSYSPACKAGES and LOCALSYSPACKAGES)
#
SYSPACKAGES = $(LOCALSYSPACKAGES) $(APPSYSPACKAGES)

################################################################################

#                    vvvvvvvvvvvvvvvvv
ifneq (,$(findstring ilogviews[studio], $(SYSPACKAGES)))
#                    ^^^^^^^^^^^^^^^^^

  # Definition des flags
  ILVFLAGS     :=
  ILVLDFLAGS   :=

  ifeq ($(SYSTEMFAMILY), win32)
    ILVFLAGS     :=  $(OPT_MODE)DILVSTD $(OPT_MODE)DILV_DONT_USE_LIBPRAGMA \
                   $(OPT_MODE)DIL_DONT_USE_LIBPRAGMA 

    ILVLIBDIR := stat_mda
    ifeq ($(SCS_DLL), yes)
       ILVFLAGS += $(OPT_MODE)DILVDLL
       ILVLIBDIR := dll_mda
    endif

    ILVARCHDIR := msvc6
    ifeq ($(SYSTEM), w2k-msvc7)
	ILVARCHDIR = x86_.net2003_7.1
    endif
    ifeq ($(SYSTEM), xp-msvc7)
	ILVARCHDIR = x86_.net2003_7.1
    endif
    ifeq ($(SYSTEM), w2003-msvc7)
	ILVARCHDIR = x86_.net2003_7.1
    endif
    ifeq ($(SYSTEM), xp-msvc9)
	ILVARCHDIR = x86_.net2008_9.0
    endif
   ifeq ($(SYSTEM), xp-msvc10)
	ILVARCHDIR = x86_.net2010_10.0
    endif
    
    ifeq ($(SCS_USE_CPATH), no)
	ILVHEADERS   := $(OPT_MODE)I$(ILVHOME)/include $(OPT_MODE)I$(ILVHOME)/views31/include
	ILVLOADLIBES := $(OPT_MODE)LIBPATH:$(ILVHOME)/studio/$(ILVARCHDIR)/$(ILVLIBDIR) $(OPT_MODE)LIBPATH:$(ILVHOME)/lib/$(ILVARCHDIR)/$(ILVLIBDIR) $(OPT_MODE)LIBPATH:$(ILVHOME)/views31/lib/$(ILVARCHDIR)/$(ILVLIBDIR)
    else
	ILVHEADERS   := $(OPT_MODE)I`cpath "$(ILVHOME)/include"` $(OPT_MODE)I`cpath "$(ILVHOME)/views31/include"`
	ILVLOADLIBES := $(OPT_MODE)LIBPATH:`cpath "$(ILVHOME)/studio/$(ILVARCHDIR)/$(ILVLIBDIR)"` $(OPT_MODE)LIBPATH:`cpath "$(ILVHOME)/lib/$(ILVARCHDIR)/$(ILVLIBDIR)"` $(OPT_MODE)LIBPATH:`cpath "$(ILVHOME)/views31/lib/$(ILVARCHDIR)/$(ILVLIBDIR)"`
    endif

    # DLA on 10/21th/2003: change IvStudio headers path for IV5 (same as default)
    ifneq (,$(findstring 5, $(ILVHOME)))
      # DLA on 10/21th/2003: special case for ilvstates.lib (avoids adding its lib path)
      # Case IV5
	ILVLDLIBS := ilvadvgadmgr.lib ilvadvgdt.lib  ilvatext.lib ilvbmp.lib ilvbmpflt.lib \
		     ilvcharts.lib ilvedit.lib ilvgadgraph.lib ilvgadgt.lib \
		     ilvgadmgr.lib ilvgrapher.lib ilvmgr.lib ilvmgrprint.lib \
		     ilvpng.lib ilvppm.lib ilvprint.lib ilvprtdlg.lib ilvstates.lib imm32.lib \
		     winviews.lib views.lib ilog.lib ilvwlook.lib ilvawlook.lib ilvmlook.lib ilvamlook.lib ilvw95look.lib ilvaw95look.lib
# Studio lib
	ILVLDLIBS += ilvstappli.lib ilvstgadget.lib ilvstgrapher.lib  ivstudio.lib
# 31 compat lib
	ILVLDLIBS += views31.lib
	ifeq ($(SCS_DLL), yes)
		ILVLDLIBS += ilvcomdyn.lib
	else
		ILVLDLIBS += ilvcomstat.lib
	endif
#ifneq (,$(findstring w2k, $(SYSTEM)))
#	ILVFLAGS += $(OPT_MODE)DILVWINDOWS95LOOK
#else
#	ILVFLAGS += $(OPT_MODE)ILVWINDOWSXPLOOK
#	ILVLDLIBS += ilvwxplook.lib ilvawxplook.lib
#endif

    else
      # Case IV3 et IV4
      ifeq ($(SCS_USE_CPATH), no)
        ILVHEADERS   += $(OPT_MODE)I$(ILVHOME)/studio
      else
        ILVHEADERS   += $(OPT_MODE)I`cpath "$(ILVHOME)/studio"`
      endif

      ifneq (,$(findstring 4, $(ILVHOME)))
        # Case IV4
	ILVLDLIBS := ilvstates.lib ivstudio.lib ilvstcharts.lib ilvstgadget.lib ilvstappli.lib ilvstgrapher.lib
      else
        # Case IV3
        ILVLDLIBS := ilvatext.lib ilvgadgt.lib views.lib winviews.lib imm32.lib
      endif # (Views4)
    endif # (Views 5)

  else  #--------- (not win32) ---------------------------------------------------------------------

    ILVHEADERS   := -I$(ILVHOME)/include
    ifeq ($(SYSTEMFAMILY), x86)
      ILVARCHDIR := x86_rhel5.0_4.1
      ILVLOADLIBES := -L$(ILVHOME)/studio/$(ILVARCHDIR)/shared -L$(ILVHOME)/lib/$(ILVARCHDIR)/shared -L$(ILVHOME)/views31/lib/$(ILVARCHDIR)/shared -L$(ILVHOME)/studio/lib/$(ILVARCHDIR)/shared
    else
    ILVLOADLIBES := -L$(ILVHOME)/studio/$(SYSTEMFAMILY)/static -L$(ILVHOME)/lib/$(SYSTEMFAMILY)/static
    endif

    ifneq (,$(findstring 5, $(ILVHOME)))
        # Case IV5
	ILVHEADERS += -I$(ILVHOME)/views31/include
        ILVLDLIBS := -lilvadvgadmgr -lilvadvgdt  -lilvatext -lilvbmp -lilvbmpflt \
		     -lilvcharts -lilvedit -lilvgadgraph -lilvgadgt \
		     -lilvgadmgr -lilvgrapher -lilvmgr -lilvmgrprint \
		     -lilvpng -lilvppm -lilvprint -lilvprtdlg -lilvstates \
                     -lviews -lilog -lilvwlook -lilvawlook -lilvmlook -lilvamlook -lilvw95look -lilvaw95look -lilvvxlook -lilvavxlook
        # 31 compat lib
	ILVLDLIBS += -lviews31
        ifneq (,$(findstring ilogviews[studio], $(SYSPACKAGES)))
          # studio
          ILVLDLIBS += -lilvstappli -lilvstgadget -lilvstgrapher -livstudio
        endif
    else
      # Case IV3 & 4
      ILVHEADERS += -I$(ILVHOME)/studio
      ifneq (,$(findstring 4, $(ILVHOME)))
        # Case IV4
        ILVLDLIBS := -lilvatext -lilvgadgraph -lilvppm -lilvpng -lilvstates -lilvedit -lilvadvgadmgr \
	  	     -lilvgadmgr -lilvgrapher -lilvadvgdt -lilvgadgt -lilvmgr -lilvbmp -lviews 
      else
        # Case IV3
        ILVLDLIBS := -lilvatext -lilvgadgt -lviews
      endif # (Views4)
    endif # (Views 5)

    #~ ifneq (,$(findstring motif, $(SYSPACKAGES)))
      ILVLDLIBS += -lmviews -lxviews
    #~ else
      #~ ILVLDLIBS +=   -lX11
    #~ endif # (motif)

  endif #--------- (win32) -------------------------------------------------------------------------

  PACKAGESFLAGS     += $(strip $(ILVFLAGS))
  PACKAGESHEADERS   += $(strip $(ILVHEADERS))
  PACKAGESLDFLAGS   += $(strip $(ILVLDFLAGS))
  PACKAGESLOADLIBES += $(strip $(ILVLOADLIBES))
  PACKAGESLDLIBS    += $(strip $(ILVLDLIBS))
else 
#                       vvvvvvvvvvvvvvvvv
    ifneq (,$(findstring ilogviews[X11], $(SYSPACKAGES)))
#                       vvvvvvvvvvvvvvvvv
      ILVFLAGS     :=
      ILVLDFLAGS   :=
      ifeq ($(SYSTEMFAMILY), win32)
        ifeq ($(SCS_USE_CPATH), no)
          ILVHEADERS   := $(OPT_MODE)I$(ILVHOME)/include
          ILVLOADLIBES := $(OPT_MODE)LIBPATH:$(ANHOME)/$(ILVARCHDIR)/lib
	else
          ILVHEADERS   := $(OPT_MODE)I`cpath "$(ILVHOME)/include"`
          ILVLOADLIBES := $(OPT_MODE)LIBPATH:`cpath "$(ANHOME)/$(ILVARCHDIR)/lib"`
	endif

	ILVLDLIBS    := anwinmdtools.lib imm32.lib
      else
        ILVHEADERS   := -I$(ILVHOME)/include -I$(ILVHOME)/views31/include
        ILVLOADLIBES := -L$(ANHOME)/$(SYSTEM)/lib
	ILVLDLIBS    := -lanxtools 
      endif
      PACKAGESFLAGS     += $(strip $(ILVFLAGS))
      PACKAGESHEADERS   += $(strip $(ILVHEADERS))
      PACKAGESLDFLAGS   += $(strip $(ILVLDFLAGS))
      PACKAGESLOADLIBES += $(strip $(ILVLOADLIBES))
      PACKAGESLDLIBS    += $(strip $(ILVLDLIBS))
    endif
endif # (ilogviews)

################################################################################

#                    vvvvvvvv
ifneq (,$(findstring animator, $(SYSPACKAGES)))
#                    ^^^^^^^^
  ANFLAGS     :=
  ANLDFLAGS   :=
  ifeq ($(SYSTEMFAMILY), win32)

    ifeq ($(AN_INT), yes)
	ifeq ($(SCS_USE_CPATH), no)
	  ANHEADERS   := $(OPT_MODE)I$(ROOTDIR)/an/include
	  ANLOADLIBES := $(OPT_MODE)LIBPATH:$(BINDIR)/lib
	else
	  ANHEADERS   := $(OPT_MODE)I`cpath "$(ROOTDIR)/an/include"`
	  ANLOADLIBES := $(OPT_MODE)LIBPATH:`cpath "$(BINDIR)/lib"`
	endif
    else
	ifeq ($(SCS_USE_CPATH), no)
	  ANHEADERS   := $(OPT_MODE)I$(ANHOME)/include
	  ANLOADLIBES := $(OPT_MODE)LIBPATH:$(ANHOME)/$(ILVARCHDIR)/lib
	else
	  ANHEADERS   := $(OPT_MODE)I`cpath "$(ANHOME)/include"`
	  ANLOADLIBES := $(OPT_MODE)LIBPATH:`cpath "$(ANHOME)/$(ILVARCHDIR)/lib"`
	endif
    endif

    ANLDLIBS    := anstudio.lib angraphics.lib antools.lib imm32.lib $(XLIBLDLIBS)

  else
    ifeq ($(AN_INT), yes)
	    ANHEADERS   := -I$(ROOTDIR)/an/include
	    ANLOADLIBES := -L$(BINDIR)/lib $(XLIBLDFLAGS) $(XLIBLOADLIBES)
	    ANLDLIBS    := -lanstudio -langraphics -lantools -lXm -lXt $(XLIBLDLIBS)
    else 
	    ANHEADERS   := -I$(ROOTDIR)/an/include
	    ANLOADLIBES := -L$(BINDIR)/lib $(XLIBLDFLAGS) $(XLIBLOADLIBES)
	    ANLDLIBS    := -lanstudio -langraphics -lantools -lanmtools -lOBX11 -lXm -lXt $(XLIBLDLIBS)
    endif
  endif # (win32)

  PACKAGESFLAGS     += $(ANFLAGS)
  PACKAGESHEADERS   += $(ANHEADERS)
  PACKAGESLDFLAGS   += $(ANLDFLAGS)
  PACKAGESLOADLIBES += $(ANLOADLIBES)
  PACKAGESLDLIBS    += $(ANLDLIBS)

endif # (animator)

################################################################################
#                    vvvvvvvvv
ifneq (,$(findstring roguewave, $(SYSPACKAGES)))
#                    ^^^^^^^^^
  ifeq ($(SYSTEMFAMILY), win32)
  #---------
  # WINDOWS
  #---------
    ifeq ($(RWVERSION), V7)
      RWFLAGS     := 
    else
      RWFLAGS     := $(OPT_MODE)D_RWCONFIG=12d
    endif
    RWLDFLAGS   := 
    ifeq ($(SCS_USE_CPATH), no)
      RWHEADERS   := $(OPT_MODE)I$(RWHOME)
      RWLOADLIBES := $(OPT_MODE)LIBPATH:$(RWHOME)/lib
    else
      RWHEADERS   := $(OPT_MODE)I`cpath "$(RWHOME)/"`
      RWLOADLIBES := $(OPT_MODE)LIBPATH:`cpath "$(RWHOME)/lib"`
    endif

    RWLDLIBS    := tls12d.lib
  else
    ifeq ($(SYSTEM), HP-UXB.11.23AA_64)
    #---------
    # ia64
    #---------
    # Use system roguewave libraries 2.0
    RWFLAGS     :=
    RWHEADERS   :=
    RWLDFLAGS   := 
    RWLOADLIBES :=
    RWLDLIBS    := -lrwtool_v2 -lstream
    else
      ifeq ($(SYSTEM), HP-UXB.11.23_64)
        #---------
        # ia64 (AP)
        #---------
      # Use system roguewave libraries 1.2
      RWFLAGS     :=
      RWHEADERS   :=
      RWLDFLAGS   := 
      RWLOADLIBES :=
      RWLDLIBS    := -lrwtool
      else
        ifeq ($(SYSTEM), Linux2.4.21)
	  #------------
	  # Linux
	  #-----------
          ifeq ($(RWVERSION), V7)
            RWFLAGS     := 
	    RWLDLIBS    := -lrwtool
          else
            RWFLAGS     := $(OPT_MODE)D_RWCONFIG_12d
	    RWLDLIBS    := -ltls71012d
	  endif

	else	
          ifeq ($(SYSTEM), Linux2.6.18)
            RWFLAGS     := $(OPT_MODE)D_RWCONFIG=12d
            ifeq ($(RWVERSION), V9)
	    RWLDLIBS    := -ltls71012d
            endif
            ifeq ($(RWVERSION), V11)
              RWLDLIBS    := -ltls9112d
            endif
	    
          else
	    RWFLAGS     :=
	    RWLDLIBS    := -lrwtool
          endif
        endif
        # Use Scs Roguewave libraries
        ifeq ($(RWHOME), )
          RWHEADERS   :=
          RWLOADLIBES :=
        else
          RWHEADERS   := -I$(RWHOME)/
          RWLOADLIBES := -L$(RWHOME)/lib
        endif
        RWLDFLAGS   :=
      endif
    endif
  endif # (win32)

  PACKAGESFLAGS     += $(RWFLAGS)
  PACKAGESHEADERS   += $(RWHEADERS)
  PACKAGESLDFLAGS   += $(RWLDFLAGS)
  PACKAGESLOADLIBES += $(RWLOADLIBES)
  PACKAGESLDLIBS    += $(RWLDLIBS)

endif # (roguewave)

################################################################################

#                    vvvvvvv
ifneq (,$(findstring orbacus, $(SYSPACKAGES)))
#                    ^^^^^^^
      ORBLDFLAGS   := 
      ORBFLAGS     := $(OPT_MODE)D_SCS_ORBACUS_ 


      ifeq ($(SYSTEMFAMILY), win32)
      
        ifeq ($(SCS_DLL), yes)
           ORBFLAGS += $(OPT_MODE)DOB_DLL
        endif

	ifeq ($(SCS_USE_CPATH), no)
	  ORBHEADERS   := $(OPT_MODE)I$(ORBACUSHOME)/include
          ORBLOADLIBES := $(OPT_MODE)LIBPATH:$(ORBACUSHOME)/lib
  else
	  ORBHEADERS   := $(OPT_MODE)I`cpath "$(ORBACUSHOME)/include"`
          ORBLOADLIBES := $(OPT_MODE)LIBPATH:`cpath "$(ORBACUSHOME)/lib"`
	endif

        ifeq ($(NT_DEBUG), no)
	  ORBLDLIBS    := OB.lib JTC.lib CosNaming.lib 
  else
	  ORBLDLIBS    := OB.lib JTC.lib CosNaming.lib 
	endif

else
        ORBHEADERS   := -I$(ORBACUSHOME)/include
        ORBLOADLIBES := -L$(ORBACUSHOME)/lib
        ORBLDLIBS    := -lOB -lJTC -lCosNaming

    endif # (win32)	

    PACKAGESFLAGS     += $(ORBFLAGS)
    PACKAGESHEADERS   += $(ORBHEADERS)
    PACKAGESLDFLAGS   += $(ORBLDFLAGS)
    PACKAGESLOADLIBES += $(ORBLOADLIBES)
    PACKAGESLDLIBS    += $(ORBLDLIBS)

endif  #(orbacus)

#                    vvvvvvv
ifneq (,$(findstring TAO, $(SYSPACKAGES)))
#                    ^^^^^^^
      ORBLDFLAGS   := 
      ORBFLAGS     := $(OPT_MODE)D_SCS_TAO_ $(OPT_MODE)D_SCS_TAO_VERSION_=$(TAO_VERSION) $(OPT_MODE)DTAO_HAS_INTERCEPTORS=1
      ORBHEADERS   := 
      ORBLOADLIBES :=      

      TMPORBHEADERS   := $(ACE_ROOT) $(TAO_ROOT) $(TAO_ROOT)/orbsvcs
      TMPORBLOADLIBES := $(ACE_ROOT)/lib

      ifeq ($(SYSTEMFAMILY), win32)
	ifeq ($(SCS_DLL), yes)
		ORBFLAGS += $(OPT_MODE)DOB_DLL
	endif

        ifeq ($(NT_DEBUG), no)
	  ORBLDLIBS    := ACE.lib TAO.lib TAO_PortableServer.lib TAO_AnyTypeCode.lib TAO_CosNaming.lib TAO_DynamicAny.lib TAO_PI.lib TAO_PI_Server.lib TAO_CodecFactory.lib TAO_DynamicInterface.lib TAO_Strategies.lib TAO_Messaging.lib TAO_Valuetype.lib TAO_TC.lib TAO_TC_IIOP.lib TAO_CosNaming_Serv.lib
	else
	  ORBLDLIBS    := ACE.lib TAO.lib TAO_PortableServer.lib TAO_AnyTypeCode.lib TAO_CosNaming.lib TAO_DynamicAny.lib TAO_PI.lib TAO_PI_Server.lib TAO_CodecFactory.lib TAO_DynamicInterface.lib TAO_Strategies.lib TAO_Messaging.lib TAO_Valuetype.lib TAO_TC.lib TAO_TC_IIOP.lib TAO_CosNaming_Serv.lib
	endif

      else
	# !win32
        ORBLDLIBS    := -lACE -lTAO -lTAO_PortableServer -lTAO_AnyTypeCode -lTAO_CosNaming -lTAO_DynamicAny -lTAO_PI_Server -lTAO_CodecFactory -lTAO_PI -lTAO_DynamicInterface -lTAO_Strategies -lTAO_Messaging -lTAO_Valuetype -lTAO_TC_IIOP -lTAO_CosNaming_Serv
      endif # (win32/other)

      ifeq ($(SYSTEMFAMILY), win32)
        ifeq ($(SCS_USE_CPATH), yes)
		ORBHEADERS := $(strip $(TMPORBHEADERS:%=$(OPT_MODE)I`cpath "%"`))
		ORBLOADLIBES := $(strip $(TMPORBLOADLIBES:%=$(OPT_MODE)LIBPATH:`cpath "%"`))
        else
		ORBHEADERS := $(strip $(TMPORBHEADERS:%=$(OPT_MODE)I%))
		ORBLOADLIBES := $(strip $(TMPORBLOADLIBES:%=$(OPT_MODE)LIBPATH:%))
        endif
      else # NOT win32
		ORBHEADERS := $(strip $(TMPORBHEADERS:%=$(OPT_MODE)I%))
		ORBLOADLIBES := $(strip $(TMPORBLOADLIBES:%=$(OPT_MODE)L%))
      endif

      PACKAGESFLAGS     += $(ORBFLAGS) 
      PACKAGESHEADERS   += $(ORBHEADERS)
      PACKAGESLDFLAGS   += $(ORBLDFLAGS)
      PACKAGESLOADLIBES += $(ORBLOADLIBES)
      PACKAGESLDLIBS    += $(ORBLDLIBS)

endif  #(TAO)

    #                    vvvvvvv
ifneq (,$(findstring orbidl, $(SYSPACKAGES)))
#                    ^^^^^^^
# Sert pour pge/bin

      ORBFLAGS     := $(OPT_MODE)D_SCS_ORBACUS_ 
      ORBLDFLAGS   := 

      ifeq ($(SYSTEMFAMILY), win32)
	ifeq ($(SCS_DLL), yes)
		ORBFLAGS += $(OPT_MODE)DOB_DLL
	endif

	ifeq ($(SCS_USE_CPATH), no)
	  ORBHEADERS   := $(OPT_MODE)I$(ORBACUSHOME)/include
	  ORBLOADLIBES := $(OPT_MODE)LIBPATH:$(ORBACUSHOME)/lib
	else
	  ORBHEADERS   := $(OPT_MODE)I`cpath "$(ORBACUSHOME)/include"`
	  ORBLOADLIBES := $(OPT_MODE)LIBPATH:`cpath "$(ORBACUSHOME)/lib"`
	endif

        ifeq ($(NT_DEBUG), no)
	  ORBLDLIBS    := idl.lib OB.lib JTC.lib CosNaming.lib 
	else
	  ORBLDLIBS    := idl.lib OB.lib JTC.lib CosNaming.lib 
	endif

      else
        ORBHEADERS   := -I$(ORBACUSHOME)/include
        ORBLOADLIBES := -L$(ORBACUSHOME)/lib
        ORBLDLIBS    := -lIDL -lOB -lJTC -lCosNaming

      endif # (win32)

      PACKAGESFLAGS     += $(ORBFLAGS) 
      PACKAGESHEADERS   += $(ORBHEADERS)
      PACKAGESLDFLAGS   += $(ORBLDFLAGS)
      PACKAGESLOADLIBES += $(ORBLOADLIBES)
      PACKAGESLDLIBS    += $(ORBLDLIBS)

endif  #(idlorbacus)


################################################################################

#                    vvvvv
ifneq (,$(findstring motif, $(SYSPACKAGES)))
#                    ^^^^^
  PACKAGESFLAGS     += $(MOTIFFLAGS)
  PACKAGESHEADERS   += $(MOTIFHEADERS)
  PACKAGESLDFLAGS   += $(MOTIFLDFLAGS)
  PACKAGESLOADLIBES += $(MOTIFLOADLIBES)
  PACKAGESLDLIBS    += $(MOTIFLDLIBS)
endif # (motif)

################################################################################

#                    vvvv
ifneq (,$(findstring xlib, $(SYSPACKAGES)))
#                    ^^^^
  PACKAGESFLAGS     += $(XLIBFLAGS)
  PACKAGESHEADERS   += $(XLIBHEADERS)
  PACKAGESLDFLAGS   += $(XLIBLDFLAGS)
  PACKAGESLOADLIBES += $(XLIBLOADLIBES)
  PACKAGESLDLIBS    += $(XLIBLDLIBS)
endif # (xlib)

################################################################################

#                    vvvv
ifneq (,$(findstring xerces, $(SYSPACKAGES)))
#                    ^^^^
  ifeq ($(SYSTEMFAMILY), win32)
    PACKAGESFLAGS     += $(OPT_MODE)D$(XML_CFLAGS)
    PACKAGESLDFLAGS   += 
    
    ifeq ($(SCS_USE_CPATH), no)
      PACKAGESHEADERS   += $(OPT_MODE)I$(XMLHOME)/include $(OPT_MODE)I$(XMLHOME)/include/xercesc
      PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:$(XMLHOME)/lib
    else
      PACKAGESHEADERS   += $(OPT_MODE)I`cpath "$(XMLHOME)/include"` $(OPT_MODE)I`cpath "$(XMLHOME)/include/xercesc"`
      PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:`cpath "$(XMLHOME)/lib"`
    endif
    
    ifneq (,$(findstring xerces, $(XMLHOME)))
        ifeq ($(SYSTEM), xp-msvc10)
          PACKAGESLDLIBS    += xerces-c_3.lib
         else 
           ifeq ($(SYSTEM), xp-msvc9)
              PACKAGESLDLIBS    += xerces-c_3.lib 
             else
              PACKAGESLDLIBS    += xerces-c_2.lib
             endif 
        endif      
    else
    PACKAGESLDLIBS    += xml4c_5.lib
    endif
    
  else
    PACKAGESLDFLAGS   += 
    PACKAGESLOADLIBES += -L$(XMLHOME)/lib
    PACKAGESFLAGS     += -D$(XML_CFLAGS)
    PACKAGESHEADERS   += -I$(XMLHOME)/include -I$(XMLHOME)/include/xercesc
    PACKAGESLDLIBS    += -lxerces
  endif # (win32)





endif # (xerces)


################################################################################

#                    vvvv
ifneq (,$(findstring netsnmp, $(SYSPACKAGES)))
#                    ^^^^
  ifeq ($(SYSTEMFAMILY), win32)
    PACKAGESFLAGS     += 
    PACKAGESLDFLAGS   += 
	# for Windows, we have an additional and specific "win32" include directory - which must be placed in first position
	ifeq ($(SYSTEM), xp-msvc7)
		COMPILO = msvc7
	else
		ifeq ($(SYSTEM), xp-msvc9)
		else
			COMPILO = msvc9
			ifeq ($(SYSTEM), xp-msvc10)
				COMPILO = msvc10
			else
				COMPILO = msvc7
			endif
		endif
	endif
	
    ifeq ($(SCS_USE_CPATH), no)
      PACKAGESHEADERS   += $(OPT_MODE)I$(SNMPHOME)/$(COMPILO)/inc/win32 $(OPT_MODE)I$(SNMPHOME)/$(COMPILO)/inc/include 
      PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:$(SNMPHOME)/$(COMPILO)/lib/release
    else
      PACKAGESHEADERS   += $(OPT_MODE)I`cpath "$(SNMPHOME)/$(COMPILO)/inc/win32"` $(OPT_MODE)I`cpath "$(OPT_MODE)I$(SNMPHOME)/$(COMPILO)/inc/include"`
      PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:`cpath "$(SNMPHOME)/$(COMPILO)/lib/release"`
    endif
    PACKAGESLDLIBS    += netsnmp.lib
    
  else
    # Linux / RedHat
    PACKAGESLDFLAGS   += 
    ifeq ($(SYSTEM), Linux2.4.21)
      PACKAGESLOADLIBES += -L$(SNMPHOME)/rhel3/lib
      PACKAGESHEADERS   += -I$(SNMPHOME)/rhel3/include
    endif
    ifeq ($(SYSTEM), Linux2.6.18)  
      PACKAGESLOADLIBES += -L$(SNMPHOME)/rhel5/lib
      PACKAGESHEADERS   += -I$(SNMPHOME)/rhel5/include
    endif
    PACKAGESLDLIBS    += -lnetsnmp -lcrypto
  endif

endif # (netsnmp)




################################################################################

#                    vvv
ifneq (,$(findstring tcl, $(SYSPACKAGES)))
#                    ^^^
  ifeq ($(SYSTEMFAMILY), win32)
    PACKAGESFLAGS     += 
    PACKAGESLDFLAGS   += 
    
    ifeq ($(SCS_USE_CPATH), no)
      PACKAGESHEADERS   += $(OPT_MODE)I$(TCLHOME)/include
      PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:$(TCLHOME)/lib
    else
      PACKAGESHEADERS   += $(OPT_MODE)I`cpath "$(TCLHOME)/include"` 
      PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:`cpath "$(TCLHOME)/lib"`
    endif
    ifeq ($(SYSTEM), xp-msvc10)
       PACKAGESLDLIBS    += tcl85.lib
    else
      ifeq ($(SYSTEM), xp-msvc9)
       PACKAGESLDLIBS    += tcl85.lib
      else
    PACKAGESLDLIBS    += tcl84.lib
      endif  
    endif
  else
    PACKAGESFLAGS     += 
  ifeq ($(SYSTEM), Linux2.6.18)
    PACKAGESHEADERS   += -I$(TCLHOME)/include
    PACKAGESLDLIBS += -ltcl8.5
  else
    PACKAGESHEADERS   += -I$(TCLHOME)/../generic -I$(TCLHOME)/include
    PACKAGESLDLIBS    += -ltcl8.4
  endif
    PACKAGESLDFLAGS   += 
    PACKAGESLOADLIBES += -L$(TCLHOME)/lib

  endif # (win32)
endif # (tcl)

################################################################################

#                    vvv
ifneq (,$(findstring jdk, $(SYSPACKAGES)))
#                    ^^^
  PACKAGESFLAGS     += 
  PACKAGESLDFLAGS   += 
  PACKAGESLOADLIBES +=
  PACKAGESLDLIBS    += 
    
  ifeq ($(SYSTEMFAMILY), win32)
    ifeq ($(SCS_USE_CPATH), no)
      PACKAGESHEADERS   += $(OPT_MODE)I$(JDKHOME)/include $(OPT_MODE)I$(JDKHOME)/include/win32 
    else
      PACKAGESHEADERS   += $(OPT_MODE)I`cpath "$(JDKHOME)/include"` $(OPT_MODE)I`cpath "$(JDKHOME)/include/win32"` 
    endif
  else
    ifeq ($(SYSTEMFAMILY), x86)
      PACKAGESHEADERS   += -I$(JDKHOME)/include -I$(JDKHOME)/include/linux
    else
      PACKAGESHEADERS   += -I$(JDKHOME)/include
    endif
  endif # (win32)
endif # (jdk)

################################################################################

#                    vv
ifneq (,$(findstring tk, $(SYSPACKAGES)))
#                    ^^
  ifeq ($(SYSTEMFAMILY), win32)
    PACKAGESFLAGS     += 
    PACKAGESHEADERS   += $(OPT_MODE)I`cpath "$(TCLHOME)/include"` 
    PACKAGESLDFLAGS   += 
    PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:`cpath "$(TCLHOME)/lib"`
    ifeq ($(SYSTEM), xp-msvc10)
       PACKAGESLDLIBS    += tk85.lib
    else
      ifeq ($(SYSTEM), xp-msvc9)
         PACKAGESLDLIBS    += tk85.lib
      else      
    PACKAGESLDLIBS    += tk84.lib
      endif   
    endif
  else
    PACKAGESFLAGS     += 
    PACKAGESHEADERS   += -I$(TCLHOME)/include
    PACKAGESLDFLAGS   += 
    PACKAGESLOADLIBES += -L$(TCLHOME)/lib
    PACKAGESLDLIBS    += -ltk8.4
  endif # (win32)
endif # (tk)

################################################################################

#                    vvvvvv
ifneq (,$(findstring oracle, $(SYSPACKAGES)))
#                    ^^^^^^
  ifeq ($(SYSTEMFAMILY), win32)
    PACKAGESFLAGS     += 
    PACKAGESLDFLAGS   += 
    ifeq ($(SCS_USE_CPATH), no)
      PACKAGESHEADERS   += $(OPT_MODE)I$(ORACLEHOME)/include
      PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:$(ORACLEHOME)/lib
    else
      PACKAGESHEADERS   += $(OPT_MODE)I`cpath "$(ORACLEHOME)/include"` 
      PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:`cpath "$(ORACLEHOME)/lib"`
    endif
    
    PACKAGESLDLIBS    += Sqllib80.lib
  else
    PACKAGESFLAGS     += 
    PACKAGESHEADERS   += -I$(ORACLE_HOME)/include -I$(CRTOOLS_ROOT)/include
    PACKAGESLDFLAGS   += 
    PACKAGESLOADLIBES += -L$(ORACLE_HOME)/lib
    PACKAGESLDLIBS    += -lclntsh -lsql -lclient -lcommon -lc3v6 -lnlsrtl3 -lgeneric -lepc -lncr -lm
    #PACKAGESLDLIBS    += -lsql -lclient -lcommon -lc3v6 -lnlsrtl3 -lclntsh -lgeneric -lepc -lncr -lm
  endif # (win32)
endif # (oracle)

################################################################################

#                    vvv
ifneq (,$(findstring tsopc, $(SYSPACKAGES)))
#       
  ifeq ($(SYSTEMFAMILY), win32)
    PACKAGESFLAGS     += 
    ifeq ($(SCS_USE_CPATH), no)
      PACKAGESHEADERS   += $(OPT_MODE)I'$(OPCSDK)/Include' $(OPT_MODE)I$(OPCCLDIR)/include
    else
      PACKAGESHEADERS   += $(OPT_MODE)I`cpath "$(OPCSDK)/Include"` $(OPT_MODE)I`cpath "$(OPCCLDIR)/Include"`
    endif
    PACKAGESLDFLAGS   += 
    ifeq ($(SYSTEM), xp-msvc9)
      PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:$(OPCCLDIR)/lib/vc9.0
    else
       ifeq ($(SYSTEM), xp-msvc10)
         PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:$(OPCCLDIR)/lib/vc10.0
       else    
    PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:$(OPCCLDIR)/lib/vc7.1
       endif 
    endif
    PACKAGESLDLIBS    += TsOpcCDaFramework.lib
  else
    ATL_DEFINEDS = -D_ATL_STATIC_REGISTRY -DSAG_COM_ATL30_NO_IMPL -D_ATL_DLL
    DCOSDK = $(EXXDIR)/$(EXXVERS)
    C_DEFINES = -DSAG_COM=1 -DCE_TLINUX86 
    PACKAGESFLAGS     += -D_TSOPC_UNIX $(ATL_DEFINEDS)  $(C_DEFINES)
    PACKAGESHEADERS   += -I$(DCOSDK)/include -I$(DCOSDK)/atl30 -I$(OPCCLDIR)/include
    PACKAGESLDFLAGS   += 
    PACKAGESLOADLIBES += -L$(EXXDIR)/$(EXXVERS)/lib -L$(OPCCLDIR)/lib
# EntireX
    PACKAGESLDLIBS    += -lmutantstubs -loleaut32 -lole32 -lrpcrt4
# Database Access
    ifeq ($(USE_OPTIM), 1)
      PACKAGESLDLIBS    += -lTsCDaFramework
    else   
      PACKAGESLDLIBS    += -lTsCDaFramework_debug
    endif
# Alarms & Events
#    PACKAGESLDLIBS    += -lTsCAeFramework
  endif # (win32)
endif # (opc)

################################################################################
#                    vvvvvvvv
ifneq (,$(findstring lightopc, $(SYSPACKAGES)))
#       
  ifeq ($(SYSTEMFAMILY), win32)
    PACKAGESFLAGS     +=
#~ lightopclib: $(LIGHTOPCROOT)/LightOPC/lightopc.lib all
#~ $(LIGHTOPCROOT)/LightOPC/lightopc.lib: $(LIGHTOPCROOT)/LightOPC/lightopc.def
	#~ echo "### Creating $(LIGHTOPCROOT)/LightOPC/lightopc.lib ..."; \
	#~ lib.exe $(OPT_MODE)machine:I386 $(OPT_MODE)def:$(LIGHTOPCROOT)/LightOPC/lightopc.def $(OPT_MODE)out:$(LIGHTOPCROOT)/LightOPC/lightopc.lib

    ifeq ($(SCS_USE_CPATH), no)
      PACKAGESHEADERS   += $(OPT_MODE)I'$(OPCSDK)/Include' $(OPT_MODE)I'$(LIGHTOPCROOT)/LightOPC'
      PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:'$(LIGHTOPCROOT)/LightOPC'
    else
      PACKAGESHEADERS   += $(OPT_MODE)I'$(OPCSDK)/Include' $(OPT_MODE)I`cpath "$(LIGHTOPCROOT)/LightOPC"`
      PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:`cpath "$(LIGHTOPCROOT)/LightOPC"`
    endif
    PACKAGESLDLIBS    += lightopc.lib 
  else
  endif # (win32)
endif # (lightopc)

################################################################################

#                    vvvvvvv
ifneq (,$(findstring CppUnit, $(SYSPACKAGES)))
#                    ^^^^^^^
  ifeq ($(SYSTEMFAMILY), win32)
    CPPUNITFLAGS     :=
    CPPUNITLDLIBS    :=
    ifeq ($(SCS_USE_CPATH), no)
        CPPUNITHEADERS   := $(OPT_MODE)I$(CPPUNITHOME)/include
        CPPUNITLDFLAGS   := cppunit.lib
        CPPUNITLOADLIBES := $(OPT_MODE)LIBPATH:$(CPPUNITHOME)/lib
    else
    CPPUNITHEADERS   := $(OPT_MODE)I`cpath "$(CPPUNITHOME)/include"`
    CPPUNITLDFLAGS   := cppunit.lib
    CPPUNITLOADLIBES := $(OPT_MODE)LIBPATH:`cpath "$(CPPUNITHOME)/lib"`
    endif
  else
    CPPUNITHEADERS   := -I$(CPPUNITHOME)/include
    CPPUNITLOADLIBES := -L$(CPPUNITHOME)/lib
    CPPUNITFLAGS     :=
    CPPUNITLDLIBS    := -lcppunit
    CPPUNITLDFLAGS   :=
  endif # (win32)

  PACKAGESFLAGS     += $(CPPUNITFLAGS)
  PACKAGESHEADERS   += $(CPPUNITHEADERS)
  PACKAGESLDFLAGS   += $(CPPUNITLDFLAGS)
  PACKAGESLOADLIBES += $(CPPUNITLOADLIBES)
  PACKAGESLDLIBS    += $(CPPUNITLDLIBS)

endif # (CppUnit)

################################################################################
#                    vvvvv
ifneq (,$(findstring Xalan, $(SYSPACKAGES)))
#                    ^^^^^
  ifeq ($(SYSTEMFAMILY), win32)
    XALANFLAGS     :=
    XALANLDLIBS    := 
    ifeq ($(SCS_USE_CPATH), no)
        XALANHEADERS   := $(OPT_MODE)I$(XALANHOME)/c/src
        XALANLDFLAGS   := XalanMessages_1_10.lib Xalan-C_1.lib
        XALANLOADLIBES := $(OPT_MODE)LIBPATH:$(XALANHOME)/lib
    else
    XALANHEADERS   := $(OPT_MODE)I`cpath "$(XALANHOME)/c/src"`
    XALANLDFLAGS   := XalanMessages_1_10.lib Xalan-C_1.lib
    XALANLOADLIBES := $(OPT_MODE)LIBPATH:`cpath "$(XALANHOME)/lib"`
    endif
  else
    XALANHEADERS   := -I$(XALANHOME)/c/src
    XALANLOADLIBES := -L$(XALANHOME)/c/lib
    XALANFLAGS     :=
    XALANLDLIBS    := -lxalanMsg -lxalan-c
    XALANLDFLAGS   :=
  endif # (win32)

  PACKAGESFLAGS     += $(XALANFLAGS)
  PACKAGESHEADERS   += $(XALANHEADERS)
  PACKAGESLDFLAGS   += $(XALANLDFLAGS)
  PACKAGESLOADLIBES += $(XALANLOADLIBES)
  PACKAGESLDLIBS    += $(XALANLDLIBS)

endif # (Xalan)

################################################################################

################################################################################
#                    vvvvvvvv
ifneq (,$(findstring javascript, $(SYSPACKAGES)))
#

  ifeq ($(SYSTEMFAMILY), win32)
    PACKAGESFLAGS += $(OPT_MODE)DXP_WIN $(OPT_MODE)DJSFILE $(OPT_MODE)DJSD_LOWLEVEL_SOURCE
    ifeq ($(SCS_USE_CPATH), no)
      PACKAGESHEADERS   += $(OPT_MODE)I'$(JSROOT)/include'
      PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:'$(JSROOT)/lib'
    else
      PACKAGESHEADERS   += $(OPT_MODE)I`cpath "$(JSROOT)/include"`
      PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:`cpath "$(JSROOT)/lib"`
    endif
    PACKAGESLDLIBS    += js32.lib 
  else
  endif # (win32)
endif # (javascript)

################################################################################

################################################################################
#                    vvvvvvvv
ifneq (,$(findstring python, $(SYSPACKAGES)))
#

  ifeq ($(SYSTEMFAMILY), win32)
    
    ifeq ($(SCS_USE_CPATH), no)
      PACKAGESHEADERS   += $(OPT_MODE)I$(PYTHONROOT)/include
      PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:$(PYTHONROOT)/libs
    else
      PACKAGESHEADERS   += $(OPT_MODE)I`cpath "$(PYTHONROOT)/include"`
      PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:`cpath "$(PYTHONROOT)/libs"`
    endif
    PACKAGESLDLIBS    += python27.lib
  else
  endif # (win32)
endif # (python)

################################################################################

################################################################################
#                    vvvvvvvv
ifneq (,$(findstring pcre, $(SYSPACKAGES)))
#

  ifeq ($(SYSTEMFAMILY), win32)
    PACKAGESFLAGS += $(OPT_MODE)DPCRE_STATIC
    ifeq ($(SCS_USE_CPATH), no)
      PACKAGESHEADERS   += $(OPT_MODE)I$(PCREHOME)/include
      PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:$(PCREHOME)/lib
    else
      PACKAGESHEADERS   += $(OPT_MODE)I`cpath "$(PCREHOME)/include"`
      PACKAGESLOADLIBES += $(OPT_MODE)LIBPATH:`cpath "$(PCREHOME)/lib"`
    endif
    PACKAGESLDLIBS    += pcre.lib
  else
  endif # (win32)
endif # (pcre)

################################################################################


################################################################################
#                    vvvvv
ifneq (,$(findstring SCADAsoft, $(SYSPACKAGES)))
#
  SCSFLAGS := $(OPT_MODE)D_SCSTRACE_
  SCSHEADERS :=
  SCSLDFLAGS   :=
  SCSLOADLIBES :=
  SCSLDLIBS    :=
  
  TMPSCSHEADERS := $(TCLHOME)/include
  TMPSCSLOADLIBES := 
  
  ifneq (,$(SCSHOMEPATCH))
	TMPSCSHEADERS += $(SCSHOMEPATCH)/inc \
			$(SCSHOMEPATCH)/inc/dbk \
			$(SCSHOMEPATCH)/inc/alm \
			$(SCSHOMEPATCH)/inc/dac \
			$(SCSHOMEPATCH)/srcred \
			$(SCSHOMEPATCH)/$(SYSTEM)/inc

	TMPSCSLOADLIBES += $(SCSHOMEPATCH)/$(SYSTEM)/lib
  endif # (SCSHOMEPATCH)
  
  TMPSCSHEADERS += $(SCSHOME)/inc \
		$(SCSHOME)/inc/dbk \
		$(SCSHOME)/inc/alm \
		$(SCSHOME)/inc/dac \
		$(SCSHOME)/srcred \
		$(SCSHOME)/$(SYSTEM)/inc

  TMPSCSLOADLIBES += $(SCSHOME)/$(SYSTEM)/lib

  ifeq ($(SYSTEMFAMILY), win32)
    ifeq ($(SCS_USE_CPATH), yes)
	SCSHEADERS := $(strip $(TMPSCSHEADERS:%=$(OPT_MODE)I`cpath "%"`))
	SCSLOADLIBES := $(strip $(TMPSCSLOADLIBES:%=$(OPT_MODE)LIBPATH:`cpath "%"`))
    else
	SCSHEADERS := $(strip $(TMPSCSHEADERS:%=$(OPT_MODE)I%))
	SCSLOADLIBES := $(strip $(TMPSCSLOADLIBES:%=$(OPT_MODE)LIBPATH:%))
    endif
  else # NOT win32
	SCSHEADERS := $(strip $(TMPSCSHEADERS:%=$(OPT_MODE)I%))
	SCSLOADLIBES := $(strip $(TMPSCSLOADLIBES:%=$(OPT_MODE)L%))
  endif

  PACKAGESFLAGS     += $(SCSFLAGS)
  PACKAGESHEADERS   += $(SCSHEADERS)
  PACKAGESLDFLAGS   += $(SCSLDFLAGS)
  PACKAGESLOADLIBES += $(SCSLOADLIBES)
  PACKAGESLDLIBS    += $(SCSLDLIBS)

endif # (SCADAsoft)

################################################################################
#                    vvvvvvv
ifneq (,$(findstring ATSsoft, $(SYSPACKAGES)))
#
  PATSFLAGS :=
  PATSLDFLAGS :=
  PATSLDLIBS :=

  TMPPATSHEADERS := $(PATSHOME)/inc\
		$(PATSHOME)/inc/error \
                $(PATSHOME)/inc/ihm \
                $(PATSHOME)/inc/mdbstruc \
                $(PATSHOME)/inc/server \
                $(PATSHOME)/$(SYSTEM)/inc

  TMPPATSLOADLIBES := $(PATSHOME)/$(SYSTEM)/lib

ifeq ($(SYSTEMFAMILY), win32)
  ifeq ($(SCS_USE_CPATH), yes)
	PATSHEADERS := $(strip $(TMPPATSHEADERS:%=$(OPT_MODE)I`cpath "%"`))
	PATSLOADLIBES := $(strip $(TMPPATSLOADLIBES:%=$(OPT_MODE)LIBPATH:`cpath "%"`))
  else
	PATSHEADERS := $(strip $(TMPPATSHEADERS:%=$(OPT_MODE)I%))
	PATSLOADLIBES := $(strip $(TMPPATSLOADLIBES:%=$(OPT_MODE)LIBPATH:%))
  endif
else # NOT win32
	PATSHEADERS := $(strip $(TMPPATSHEADERS:%=$(OPT_MODE)I%))
	PATSLOADLIBES := $(strip $(TMPPATSLOADLIBES:%=$(OPT_MODE)L%))
endif

  PACKAGESFLAGS     += $(PATSFLAGS)
  PACKAGESHEADERS   += $(PATSHEADERS)
  PACKAGESLDFLAGS   += $(PATSLDFLAGS)
  PACKAGESLOADLIBES += $(PATSLOADLIBES)
  PACKAGESLDLIBS    += $(PATSLDLIBS)
endif # (ATSsoft)

################################################################################



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

################################################################################

ifneq ($(wildcard /bin/bash)$(wildcard /bin/bash.exe),)
    MKMF          = bash $(CONFDIR)/bin/mkmf.scs.bash.sh -d $(CONFDIR)
else
    MKMF          = sh $(CONFDIR)/bin/mkmf.scs.sh -d $(CONFDIR)
endif
MKINSTALLDIRS = sh $(CONFDIR)/bin/mkinstalldirs.sh 
MKROOT        = sh $(CONFDIR)/bin/mkroot.sh

# Define useful tools and their arguments
# =======================================
ifeq ($(SYSTEMFAMILY), win32)

  LEX  	        = flex.exe
  YACC          = bison.exe
  RC            = rc.exe
  MC            = mc.exe  

else

  ifeq ($(SYSTEMFAMILY), x86)
    LEX           = /usr/bin/flex 
    YACC          = /usr/bin/bison
  else
    LEX	          = /nassvr1/softs/bin/$(SYSTEM)/flex 
    YACC          = /nassvr1/softs/bin/$(SYSTEM)/bison
  endif

endif

################################################################################

# Define derived parameters
# =========================
# (fairly nothing to customize beyond this line...)
#
CXXFLAGS = $(strip $(OPT_MODE)D_$(USER)_ \
$(TRUELOCALAPPFLAGS) $(APPFLAGS) $(PACKAGESFLAGS) $(CXXCFLAGS) \
$(APPHEADERS) $(APPEXCLUDEDHEADERS) \
$(TRUELOCALAPPHEADERS) $(TRUELOCALEXCLUDEDHEADERS) \
$(PACKAGESHEADERS) $(CXXHEADERS))

CFLAGS = $(strip $(OPT_MODE)D_$(USER)_ \
$(TRUELOCALAPPFLAGS) $(APPFLAGS) $(PACKAGESFLAGS) $(CCFLAGS) $(CANSIFLAG) \
$(APPHEADERS) $(APPEXCLUDEDHEADERS) \
$(TRUELOCALAPPHEADERS) $(TRUELOCALEXCLUDEDHEADERS) \
$(CCHEADERS) $(PACKAGESHEADERS)) 

LDFLAGS = $(strip $(TRUELOCALAPPLDFLAGS) $(APPLDFLAGS) \
$(PACKAGESLDFLAGS) $(CXXLDFLAGS))

LOADLIBES = $(strip  $(APPLOADLIBES) $(TRUELOCALAPPLOADLIBES) \
$(PACKAGESLOADLIBES) $(CXXLOADLIBES))

################################################################################

# Include both ORB stub & skeleton objects as generated
# from IDL
ifneq ($(TARGET), libversion.$(STLIB_EXTENSION))
  ifeq ($(PRODUCTION), 1)
    ifeq ($(SYSTEMFAMILY), win32)
        ifeq ($(SCS_USE_CPATH), no)
          IDENT_OBJ := $(BINDIR)/ident/version/version.$(OBJECT_EXTENSION)
        else
          IDENT_OBJ := `cpath "$(BINDIR)/ident/version/version.$(OBJECT_EXTENSION)"`
        endif
    else
      IDENT_OBJ := $(BINDIR)/ident/version/version.$(OBJECT_EXTENSION)
    endif
  else
    ifeq ($(PRODUCTION), 2)
      ifeq ($(SYSTEMFAMILY), win32)
        ifeq ($(SCS_USE_CPATH), no)
          IDENT_OBJ := $(BINDIR)/ident/version/version.$(OBJECT_EXTENSION)
        else
          IDENT_OBJ := `cpath "$(BINDIR)/ident/version/version.$(OBJECT_EXTENSION)"`
        endif
      else
        IDENT_OBJ := $(BINDIR)/ident/version/version.$(OBJECT_EXTENSION)
      endif
    else
      IDENT_OBJ :=
    endif
  endif
else
  IDENT_OBJ :=
endif

ifeq ($(USE_CXPERF), 1)
    LOCALEXTRAOBJS += $(IDENT_OBJ) $(TRUELOCALAPPORBSERVEROBJS) $(TRUELOCALAPPORBSOAPOBJS) $(TRUELOCALAPPORBCLIENTOBJS) 
else
    ifeq ($(LIB_EXTENSION),$(STLIB_EXTENSION))
	LOCALEXTRAOBJS += $(IDENT_OBJ) $(TRUELOCALAPPORBSERVEROBJS) $(TRUELOCALAPPORBSOAPOBJS) $(TRUELOCALAPPORBCLIENTOBJS) 
    else
	TRUELOCALAPPLDLIBS += $(IDENT_OBJ) $(TRUELOCALAPPORBSERVEROBJS) $(TRUELOCALAPPORBSOAPOBJS) $(TRUELOCALAPPORBCLIENTOBJS)
    endif
endif

# if ORB is orbacus or TAO, both stub and skeleton object files (.o)
# must be included.
ifneq (,$(findstring $(ORB_NAME), $(SYSPACKAGES)))
  # Only if there are server objects
  ifneq (,$(TRUELOCALAPPORBSERVEROBJS))
    ifeq ($(USE_CXPERF), 1)
      LOCALEXTRAOBJS += $(subst $(ORBSERVERSUFFIXO),$(ORBCLIENTSUFFIXO),$(TRUELOCALAPPORBSERVEROBJS))
    else
      ifeq ($(LIB_EXTENSION),$(STLIB_EXTENSION))
	  LOCALEXTRAOBJS += $(subst $(ORBSERVERSUFFIXO),$(ORBCLIENTSUFFIXO),$(TRUELOCALAPPORBSERVEROBJS))
      else
	  TRUELOCALAPPLDLIBS += $(subst $(ORBSERVERSUFFIXO),$(ORBCLIENTSUFFIXO),$(TRUELOCALAPPORBSERVEROBJS))
      endif
    endif
  endif
endif

LDLIBS = $(strip $(APPLDLIBS) $(TRUELOCALAPPLDLIBS) $(PACKAGESLDLIBS) $(CXXLDLIBS) )

APPLIBFILES = $(strip $(APPLDLIBS:-l%=lib%.$(LIB_EXTENSION)) $(TRUELOCALAPPLDLIBS:-l%=lib%.$(LIB_EXTENSION)))

EXCLUDEDHEADERS = $(strip $(APPEXCLUDEDHEADERS) $(PACKAGESHEADERS) $(CXXHEADERS))

####
# VPATH pour que make trouve les libs
####
ifeq ($(SYSTEMFAMILY), win32)
	# KO sous Windows
	#~ VPATH_TEMP = $(strip $(LOADLIBES:/LIBPATH:%=;%))
	#~ VPATH += $(strip $(VPATH_TEMP))
else
	VPATH_TEMP = $(strip $(LOADLIBES:-L%=:%))
	VPATH += $(strip $(VPATH_TEMP))
endif

################################################################################

# Rules Extensions
# ================
# invoke linker rather than C-front,
# add rules for .cpp and .cxx suffixes,
# inhibit rules for RCS and SCCS.
#
ifeq ($(SYSTEMFAMILY), win32)

  LINK.obj = $(LD) $(LDFLAGS) $(TARGET_ARCH)

  .SUFFIXES: .cpp 
  COMPILE.cpp = $(COMPILE.cc)
  LINK.cpp    = $(LINK.cc)

  ifeq ($(SCS_USE_CPATH), no)
    OUTPUT_OPTION = $(OPT_MODE)Fo$@
  else
    OUTPUT_OPTION = $(OPT_MODE)Fo$(shell cpath $@)    ### for .sbr: /FR`cpath $(subst .obj,.sbr,$@)`
  endif
  LEXYACC_OUTPUT_OPTION = $(OPT_MODE)Fo$@    ### for .sbr: /FR$(subst .obj,.sbr,$@)
  LEXYACC_OPTION = $(subst .obj,.cpp,$@) 

  %.cpp:

  %: %.cpp
	$(LINK.cpp) $^ $(LOADLIBES) $(LDLIBS) $(OPT_MODE)out:$@

  ifeq ($(SCS_USE_CPATH), no)
%.obj: %.cpp
	$(COMPILE.cpp) $(OUTPUT_OPTION) $<
  else
  %.obj: %.cpp
	$(COMPILE.cpp) $(OUTPUT_OPTION) $(shell cpath "$<" )
  endif

  .SUFFIXES: .cxx
  COMPILE.cxx = $(COMPILE.cc)
  LINK.cxx    = $(LINK.cc)

  .cxx.obj:
	$(COMPILE.cxx) $(OUTPUT_OPTION) "`cpath "$<"`" 

  %.cxx:

  %: %.cxx
	$(LINK.cxx) $^ $(LOADLIBES) $(LDLIBS) $(OPT_MODE)out:$@

  %.obj: %.cxx
	$(COMPILE.cxx) $(OUTPUT_OPTION) "`cpath "$<"`" 
  
  .SUFFIXES: .cc

  ifeq ($(SCS_USE_CPATH), no)
  .cc.obj:
	$(COMPILE.cc) $(OUTPUT_OPTION) $<
  else
  .cc.obj:
	$(COMPILE.cc) $(OUTPUT_OPTION) "`cpath "$<"`"
  endif

  %.cc:

  %: %.cc
	$(LINK.cc) $^ $(LOADLIBES) $(LDLIBS) $(OPT_MODE)out:$@

  ifeq ($(SCS_USE_CPATH), no)
  %.obj: %.cc
	$(COMPILE.cc) $(OUTPUT_OPTION) $<
  else
  %.obj: %.cc
	$(COMPILE.cc) $(OUTPUT_OPTION) "`cpath "$<"`" 
  endif

  .SUFFIXES: .c
  COMPILE.c = $(COMPILE.cc)
  
  ifeq ($(SCS_USE_CPATH), no)
  LINK"$<".c    = $(LINK.cc)
  .c.obj:
	$(COMPILE.c) $(OUTPUT_OPTION) $<
  %.obj: %.c
	$(COMPILE.c) $(OUTPUT_OPTION) $<

   else
  LINK"`cpath "$<"`".c    = $(LINK.cc)

  .c.obj:
	$(COMPILE.c) $(OUTPUT_OPTION) "`cpath "$<"`" 
  %.obj: %.c
	$(COMPILE.c) $(OUTPUT_OPTION) "`cpath "$<"`" 
   
   endif

  %.c:

  %: %.c
	$(LINK.c) $^ $(LOADLIBES) $(LDLIBS) $(OPT_MODE)out:$@

  %:: %,v;

  %:: RCS/%,v;

  %:: s.%;

  %:: SCCS/s.%;

else   ## if not win32

  LINK.o = $(LD) $(LDFLAGS) $(TARGET_ARCH)

  .SUFFIXES: .cpp
  COMPILE.cpp = $(COMPILE.cc)
  LINK.cpp    = $(LINK.cc)

  .cpp.o:
	$(COMPILE.cpp) $< $(OUTPUT_OPTION)

  %.cpp:

  %: %.cpp
	$(LINK.cpp) $^ $(LOADLIBES) $(LDLIBS) -o $@

  %.o: %.cpp
	$(COMPILE.cpp) $< $(OUTPUT_OPTION)

  .SUFFIXES: .cxx
  COMPILE.cxx = $(COMPILE.cc)
  LINK.cxx    = $(LINK.cc)

  .cxx.o:
	$(COMPILE.cxx) $< $(OUTPUT_OPTION)

  %.cxx:

  %: %.cxx
	$(LINK.cxx) $^ $(LOADLIBES) $(LDLIBS) -o $@

  %.o: %.cxx
	$(COMPILE.cxx) $< $(OUTPUT_OPTION)

  %:: %,v;

  %:: RCS/%,v;

  %:: s.%;

  %:: SCCS/s.%;

endif

# This is the end.
