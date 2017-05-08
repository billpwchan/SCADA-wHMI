# --------------------
# Res compilation
# --------------------

ifeq ($(SYSTEMFAMILY), win32)

RESFILE             = $(BINDIR)/$(CURDIR)/$(shell basename $(subst .rc,.res,$(RCFILE)))


ifeq ($(SCS_USE_CPATH), no)
TRUERCFILE          = $(RCFILE)
else
TRUERCFILE          = $(strip $(shell cpath $(RCFILE)))
endif

LOCALREMOVEOBJS	   += $(RESFILE)

# To use french language resource
RCOPT               = $(OPT_MODE)l 0x40c

resource: createbinarydirifneeded $(RESFILE) all
$(RESFILE): $(RCFILE)
	$(RC) $(RCOPT) "$(TRUERCFILE)"
	cp $(subst .rc,.res,$(RCFILE)) $(BINDIR)/$(CURDIR)/.
	rm $(subst .rc,.res,$(RCFILE))

endif # (win32)



