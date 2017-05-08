# --------------------
# Lex/yacc compilation
# --------------------

# for awk processing,
# normally the binary 'awk' is independant from platform
AWK = awk

ifeq ($(SYSTEMFAMILY), x86)
LFLAGS	= 
YFLAGS	=
else
LFLAGS	= 
YFLAGS	= -d
endif

ifeq (,$(LOCALPREFIX))	

LEXMETAFILE  = $(wildcard *.l.meta)
LEXFILE      = $(LEXMETAFILE:%.l.meta=$(BINDIR)/$(CURDIR)/%.l)
LEXCFILE     = $(LEXFILE:%.l=%l.cpp)
LEXOFILE     = $(LEXCFILE:%.cpp=%.$(OBJECT_EXTENSION))

YACCMETAFILE = $(wildcard *.y.meta)
YACCFILE     = $(YACCMETAFILE:%.y.meta=$(BINDIR)/$(CURDIR)/%.y)
YACCCFILE    = $(YACCFILE:%.y=/%y.cpp)
YACCOFILE    = $(YACCCFILE:%.cpp=%.$(OBJECT_EXTENSION))
HFILE        = $(YACCCFILE:%.cpp=%.cpp.h)

LOCALREMOVEOBJS	= $(LEXFILE) $(LEXOFILE:%.$(OBJECT_EXTENSION)=%.cpp) $(LEXOFILE) \
	          $(YACCFILE) $(YACCOFILE:%.$(OBJECT_EXTENSION)=%.cpp) $(YACCOFILE) $(HFILE) 

lex_yacc: createbinarydirifneeded $(YACCCFILE) $(LEXCFILE) all

ifeq ($(SYSTEMFAMILY), win32)

# Cas où plusieurs préfixes définis (ie plusieurs fichiers lex&yacc)
ifeq ($(SCS_USE_CPATH), no)
$(LEXCFILE) : $(LEXFILE)
	$(LEX) $(LFLAGS) -P$(LOCALPREFIX_$*) -o$@ $< ; \
	$(COMPILE.cpp) $(OPT_MODE)Fo$(subst .cpp,.obj,$@) $@

$(YACCCFILE) : $(YACCFILE)
	$(YACC) $(YFLAGS) -p $(LOCALPREFIX_$*) $< -o $@ ; \
	$(COMPILE.cpp) $(OPT_MODE)Fo$(subst .cpp,.obj,$@) $@
else
$(LEXCFILE) : $(LEXFILE)
	$(LEX) $(LFLAGS) -P$(LOCALPREFIX_$*) -o"`cpath "$@"`" "`cpath "$<"`" ; \
	$(COMPILE.cpp) $(OPT_MODE)Fo`cpath $(subst .cpp,.obj,$@)` `cpath $@`

$(YACCCFILE) : $(YACCFILE)
	$(YACC) $(YFLAGS) -p $(LOCALPREFIX_$*) "`cpath "$<"`" -o "`cpath "$@"`" ; \
	$(COMPILE.cpp) $(OPT_MODE)Fo`cpath $(subst .cpp,.obj,$@)` `cpath $@`
endif

else #(win32)

ifeq ($(SYSTEMFAMILY), x86)

$(LEXCFILE) : $(LEXFILE)
	$(LEX) $(LFLAGS) -P$(*)_yy -o$@ $<

$(YACCCFILE) : $(YACCFILE)
	$(YACC) $(YLAGS) --defines=$@.h -p $(*)_yy $< -o $@

else  #(x86)

$(LEXCFILE) : $(LEXFILE)
	$(LEX) $(LFLAGS) -P$(*)_yy -o$@ $<

$(YACCCFILE) : $(YACCFILE)
	$(YACC) $(YFLAGS) -p $(*)_yy $< -o $@

endif  #(x86)

endif  #(win32)

else #(localprefix)

.PHONY: local lex_yacc

LEXMETAFILE  = $(wildcard *.l.meta)
LEXFILE      = $(LEXMETAFILE:%.l.meta=$(BINDIR)/$(CURDIR)/%.l)
LEXCFILE     = $(LEXFILE:%.l=%.cpp)
LEXOFILE     = $(LEXCFILE:%.cpp=%.$(OBJECT_EXTENSION))

YACCMETAFILE = $(wildcard *.y.meta)
YACCFILE     = $(YACCMETAFILE:%.y.meta=$(BINDIR)/$(CURDIR)/%.y)
YACCCFILE    = $(YACCFILE:%.y=%.cpp)
YACCOFILE    = $(YACCCFILE:%.cpp=%.$(OBJECT_EXTENSION))
HFILE        = $(YACCCFILE:%.cpp=%.cpp.h)

LOCALREMOVEOBJS	= $(LEXFILE) $(LEXOFILE:%.$(OBJECT_EXTENSION)=%.cpp) $(LEXOFILE) \
	           $(YACCFILE) $(YACCOFILE:%.$(OBJECT_EXTENSION)=%.cpp) $(YACCOFILE) $(HFILE) 

lex_yacc: createbinarydirifneeded $(YACCCFILE) $(LEXCFILE) all

ifeq ($(SYSTEMFAMILY), win32)

ifeq ($(SCS_USE_CPATH), no)
$(LEXCFILE) : $(LEXFILE)
	$(LEX) $(LFLAGS) -P$(LOCALPREFIX) -o$@ $< ;\
	$(COMPILE.cpp) $(OPT_MODE)Fo$(subst .cpp,.obj,$@) $@

$(YACCCFILE) : $(YACCFILE)
	$(YACC) $(YFLAGS) -p $(LOCALPREFIX) $< -o $@ ;\
	$(COMPILE.cpp) $(OPT_MODE)Fo$(subst .cpp,.obj,$@) $@
else
$(LEXCFILE) : $(LEXFILE)
	$(LEX) $(LFLAGS) -P$(LOCALPREFIX) -o"`cpath "$@"`" "`cpath "$<"`" ;\
	$(COMPILE.cpp) $(OPT_MODE)Fo`cpath $(subst .cpp,.obj,$@)` `cpath $@`

$(YACCCFILE) : $(YACCFILE)
	$(YACC) $(YFLAGS) -p$(LOCALPREFIX) "`cpath "$<"`" -o "`cpath "$@"`" ;\
	$(COMPILE.cpp) $(OPT_MODE)Fo`cpath $(subst .cpp,.obj,$@)` `cpath $@`
endif

else

ifeq ($(SYSTEMFAMILY), x86)

$(LEXCFILE) : $(LEXFILE)
	$(LEX) $(LFLAGS) -P$(LOCALPREFIX) -o$@ $<

$(YACCCFILE) : $(YACCFILE)
	$(YACC) $(YFLAGS) --defines=$@.h -p $(LOCALPREFIX) $< -o $@

else

$(LEXCFILE) : $(LEXFILE)
	$(LEX) $(LFLAGS) -P$(LOCALPREFIX) -o$@ $<

$(YACCCFILE) : $(YACCFILE)
	$(YACC) $(YFLAGS) -p$(LOCALPREFIX) $< -o $@

endif  #(x86)

endif  #(win32)

endif  #(localprefix)

# independant from platform
$(BINDIR)/$(CURDIR)/%.l : $(SRCDIR)/%.l.meta
	$(AWK) -f $(SRCDIR)/$(AWKFILE) $(AWKFLAGS) $< > $@
	
$(BINDIR)/$(CURDIR)/%.y : $(SRCDIR)/%.y.meta
	$(AWK) -f $(SRCDIR)/$(AWKFILE) $(AWKFLAGS) $< > $@
