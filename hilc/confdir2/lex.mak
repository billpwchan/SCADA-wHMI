# --------------------
# Lex/yacc compilation
# --------------------

ifeq ($(SYSTEMFAMILY), x86)
LFLAGS	= 
YFLAGS	=
else
LFLAGS	= 
YFLAGS	= -d
endif

ifeq (,$(LOCALPREFIX))	
# ========== IL Y A UN LOCALPREFIX ====================
LEXFILE     = $(wildcard *.l)
LEXCFILE    = $(LEXFILE:%.l=$(BINDIR)/$(CURDIR)/%l.cpp)
LEXOFILE    = $(LEXCFILE:%.cpp=%.$(OBJECT_EXTENSION))
YACCFILE    = $(wildcard *.y)
YACCCFILE   = $(YACCFILE:%.y=$(BINDIR)/$(CURDIR)/%y.cpp)
YACCOFILE   = $(YACCCFILE:%.cpp=%.$(OBJECT_EXTENSION))
HFILE       = $(YACCCFILE:%.cpp=%.cpp.h)

LOCALREMOVEOBJS	= $(LEXOFILE:%.$(OBJECT_EXTENSION)=%.cpp) $(LEXOFILE) \
	          $(YACCOFILE:%.$(OBJECT_EXTENSION)=%.cpp) $(YACCOFILE) $(HFILE) 

lex_yacc: createbinarydirifneeded $(YACCCFILE) $(LEXCFILE) all

ifeq ($(SYSTEMFAMILY), win32)
# ************ WIN32 *****************************
# Cas où plusieurs préfixes définis (ie plusieurs fichiers lex&yacc)
ifeq ($(SCS_USE_CPATH), no)
# ------------ MSYS ------------------------------
$(BINDIR)/$(CURDIR)/%l.cpp : $(SRCDIR)/%.l
	$(LEX) $(LFLAGS) -P$(LOCALPREFIX_$*) -o$@ $< ; \
	$(COMPILE.cpp) $(OPT_MODE)Fo$(subst .cpp,.obj,$@) $@

$(BINDIR)/$(CURDIR)/%y.cpp : $(SRCDIR)/%.y
	$(YACC) $(YFLAGS) -p $(LOCALPREFIX_$*) $< -o $@ ; \
	$(COMPILE.cpp) $(OPT_MODE)Fo$(subst .cpp,.obj,$@) $@
else
# ----------- CYGWIN ------------------------------
$(BINDIR)/$(CURDIR)/%l.cpp : $(SRCDIR)/%.l
	$(LEX) $(LFLAGS) -P$(LOCALPREFIX_$*) -o"`cpath "$@"`" "`cpath "$<"`" ; \
	$(COMPILE.cpp) $(OPT_MODE)Fo`cpath $(subst .cpp,.obj,$@)` `cpath $@`

$(BINDIR)/$(CURDIR)/%y.cpp : $(SRCDIR)/%.y
	$(YACC) $(YFLAGS) -p $(LOCALPREFIX_$*) "`cpath "$<"`" -o "`cpath "$@"`" ; \
	$(COMPILE.cpp) $(OPT_MODE)Fo`cpath $(subst .cpp,.obj,$@)` `cpath $@`
endif

else #(win32)

ifeq ($(SYSTEMFAMILY), x86)
# *********** LINUX *******************************
$(BINDIR)/$(CURDIR)/%l.cpp : $(SRCDIR)/%.l
	$(LEX) $(LFLAGS) -P$(*)_yy -o$@ $<

$(BINDIR)/$(CURDIR)/%y.cpp : $(SRCDIR)/%.y
	$(YACC) $(YLAGS) --defines=$@.h -p $(*)_yy $< -o $@

else  #(x86)
# *********** AUTRES UNIX *************************
$(BINDIR)/$(CURDIR)/%.cpp : $(SRCDIR)/%.l
	$(LEX) $(LFLAGS) -P$(*)_yy -o$@ $<

$(BINDIR)/$(CURDIR)/%.cpp : $(SRCDIR)/%.y
	$(YACC) $(YFLAGS) -p $(*)_yy $< -o $@

endif  #(x86)

endif  #(win32)

else #(localprefix)
# ========== PAS DE LOCALPREFIX ====================
.PHONY: local lex_yacc

LEXFILE     = $(wildcard *.l)
LEXCFILE    = $(LEXFILE:%.l=$(BINDIR)/$(CURDIR)/%.cpp)
LEXOFILE    = $(LEXCFILE:%.cpp=%.$(OBJECT_EXTENSION))
YACCFILE    = $(wildcard *.y)
YACCCFILE   = $(YACCFILE:%.y=$(BINDIR)/$(CURDIR)/%.cpp)
YACCOFILE   = $(YACCCFILE:%.cpp=%.$(OBJECT_EXTENSION))
HFILE       = $(YACCCFILE:%.cpp=%.cpp.h)

LOCALREMOVEOBJS	= $(LEXOFILE:%.$(OBJECT_EXTENSION)=%.cpp) $(LEXOFILE) \
	          $(YACCOFILE:%.$(OBJECT_EXTENSION)=%.cpp) $(YACCOFILE) $(HFILE) 

lex_yacc: createbinarydirifneeded $(YACCCFILE) $(LEXCFILE) all

ifeq ($(SYSTEMFAMILY), win32)
# ************ WIN32 *****************************
ifeq ($(SCS_USE_CPATH), no)
# ------------ MSYS ------------------------------
$(BINDIR)/$(CURDIR)/%.cpp : $(SRCDIR)/%.l
	$(LEX) $(LFLAGS) -P$(LOCALPREFIX) -o$@ $< ;\
	$(COMPILE.cpp) $(OPT_MODE)Fo$(subst .cpp,.obj,$@) $@

$(BINDIR)/$(CURDIR)/%.cpp : $(SRCDIR)/%.y
	$(YACC) $(YFLAGS) -p $(LOCALPREFIX) $< -o $@ ;\
	$(COMPILE.cpp) $(OPT_MODE)Fo$(subst .cpp,.obj,$@) $@
else
# ----------- CYGWIN ------------------------------
$(BINDIR)/$(CURDIR)/%.cpp : $(SRCDIR)/%.l
	$(LEX) $(LFLAGS) -P$(LOCALPREFIX) -o"`cpath "$@"`" "`cpath "$<"`" ;\
	$(COMPILE.cpp) $(OPT_MODE)Fo`cpath $(subst .cpp,.obj,$@)` `cpath $@`

$(BINDIR)/$(CURDIR)/%.cpp : $(SRCDIR)/%.y
	$(YACC) $(YFLAGS) -p $(LOCALPREFIX) "`cpath "$<"`" -o "`cpath "$@"`" ;\
	$(COMPILE.cpp) $(OPT_MODE)Fo`cpath $(subst .cpp,.obj,$@)` `cpath $@`
endif

else

ifeq ($(SYSTEMFAMILY), x86)
# *********** LINUX *******************************

$(BINDIR)/$(CURDIR)/%.cpp : $(SRCDIR)/%.l
	$(LEX) $(LFLAGS) -P$(LOCALPREFIX) -o$@ $<

$(BINDIR)/$(CURDIR)/%.cpp : $(SRCDIR)/%.y
	$(YACC) $(YFLAGS) --defines=$@.h -p $(LOCALPREFIX) $< -o $@

else
# *********** AUTRES UNIX *************************

$(BINDIR)/$(CURDIR)/%.cpp : $(SRCDIR)/%.l
	$(LEX) $(LFLAGS) -P$(LOCALPREFIX) -o$@ $<

$(BINDIR)/$(CURDIR)/%.cpp : $(SRCDIR)/%.y
	$(YACC) $(YFLAGS) -p $(LOCALPREFIX) $< -o $@

endif  #(x86)

endif  #(win32)

endif  #(localprefix)


