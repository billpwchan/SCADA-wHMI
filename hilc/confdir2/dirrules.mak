# Default rule (call sub-module default rules).
.DEFAULT:
	@for target in $(SUBLIBDIRTARGET); do \
	  if [ -f $$target/Makefile ]; then \
	    $(MAKE) -C $$target $@; \
	  else echo "*** Can't make [$@] in '$$target'"; fi \
	done

# Rule to rebuild all.
.PHONY: all
all: $(SUBLIBDIRTARGET)

# Rules to build sub-modules (individually).
.PHONY: $(SUBLIBDIRTARGET)
$(SUBLIBDIRTARGET):
	@if [ -f $@/Makefile ]; then \
	    $(MAKE) -C $@; \
	else echo "*** Can't make [default rule] in '$@'"; fi
