# --------------------------------------------------------------- -*- AWK -*-
#							GeniTools source file
# File:          mkmf.awk
# Author:        Dominique FAURE
# Maintainer:    
# Created:       December 1996
# Version:       
# Last Modified: 
# Keywords:      
# --------------------------------------------------------------------------
#  Copyright (C) 1996 by GENIGRAPH.
#  All Rights Reserved.
#
#  N O T I C E
#
#  THIS MATERIAL IS CONSIDERED A TRADE SECRET BY GENIGRAPH.
#  UNAUTHORIZED ACCESS, USE, REPRODUCTION OR DISTRIBUTION IS PROHIBITED.
# --------------------------------------------------------------------------
# 
# mkmf.awk -- awk part of mkmf command
#
# see mkmf script for further documentation.
#
# variables DEPENDENCY_REGEXP, EXCLUDED_DIRS and OBJECT are set from command
# line invocation.
#
# --------------------------------------------------------------------------
#
BEGIN {
    dependency_regexp   = "";
    excluded_dirs_count = 0;
    dependencies_count  = 0;
}

# Store header extracter regular expression
#
DEPENDENCY_REGEXP != "" {
    dependency_regexp = "\"" DEPENDENCY_REGEXP "\"";
    DEPENDENCY_REGEXP = "";
}

# Store directories containing headers to exclude in dependencies
#
EXCLUDED_DIRS != "" {
    excluded_dirs_count = split(EXCLUDED_DIRS, excluded_dirs);
    for(i = 1; i <= excluded_dirs_count; i++) {
	sub(/^-I/, "", excluded_dirs[i]);
    }
    EXCLUDED_DIRS = "";
}

# Parse preprocessor result and exclude unwanted headers
# lines: #line <number> "<DEPENDENCY_REGEXP>"
# or:    # <number> "<DEPENDENCY_REGEXP>"
#
($1 ~ /#.*/ && $2 ~ /[0-9][0-9]*/ && $3 ~ dependency_regexp) {

    dependency = $3;

    sub(/^\"/, "", dependency);
    sub(/\"$/, "", dependency);

    is_excluded = 0;
    for(i = 1; i <= excluded_dirs_count; i++) {
	if(index(dependency, excluded_dirs[i]) == 1) {
	    is_excluded = 1;
	    break;
	}
    }
    if(is_excluded == 0) {
	dependencies[++dependencies_count] = dependency;
    }
}

# Parse preprocessor result and exclude unwanted headers
# lines: #<number> "<DEPENDENCY_REGEXP>"
#
($1 ~ /#[0-9][0-9]*/ && $2 ~ dependency_regexp) {

    dependency = $2;

    sub(/^\"/, "", dependency);
    sub(/\"$/, "", dependency);

    is_excluded = 0;
    for(i = 1; i <= excluded_dirs_count; i++) {
	if(index(dependency, excluded_dirs[i]) == 1) {
	    is_excluded = 1;
	    break;
	}
    }
    if(is_excluded == 0) {
	dependencies[++dependencies_count] = dependency;
    }
}

# Generate Makefile dependency rule
#
END {
    object_length = length(OBJECT);
    printf("%s:", OBJECT);
    for(i = 1; i <= dependencies_count; i++) {
	if(i == 1) {
	    printf(" %s", dependencies[i]);
	    if(dependencies_count > 1)
		printf(" \\\n");
	}
	else {
	    printf("%s  %s", rep(object_length, " "), dependencies[i]);
	    if(i < dependencies_count)
		printf(" \\\n");
	}
    }
    printf("\n");
}

# Useful function to align outputs
#
function rep(n, s, t) {
    while (n-- > 0)
	t = t s;
    return t;
}
