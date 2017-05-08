# --------------------
# Mc compilation
# --------------------

ifeq ($(SYSTEMFAMILY), win32)

RCFILE               = $(subst .mc,.rc,$(MCFILE))

ifeq ($(SCS_USE_CPATH), no)
TRUEMCFILE           = $(MCFILE)
else
TRUEMCFILE           = $(strip $(MCFILE:%.mc=`cpath "%.mc"`))
endif

LOCALREMOVEOBJS	    += $(RCFILE) $(ROOTDIR)/$(CURDIR)/MSG00001.bin $(subst .mc,.h,$(MCFILE)) 

ntserv: createbinarydirifneeded $(RCFILE) resource all
$(RCFILE): $(TRUEMCFILE) 
	$(MC) $(TRUEMCFILE) 

include $(CONFDIR)/res.mak

endif # (win32)



