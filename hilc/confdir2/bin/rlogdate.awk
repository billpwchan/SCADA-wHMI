# ----------------------------------------------------------------- -*- AWK -*-
# rcslock.awk -- awk part of rcslock command
#
# variables bannerFlag, repositoryFlag, versionFlag, userFlag and userOutput
# are set from command line invocation
#
BEGIN {
    count            = 0;
    bannerRepository = "REPOSITORY";
    bannerFile       = "FILE";
    bannerVersion    = "VERSION";
    bannerUser       = "USER";
    maxLenRepository = length(bannerRepository);
    maxLenFile       = length(bannerFile);
    maxLenVersion    = length(bannerVersion);
    maxLenUser       = length(bannerUser);
    maxSelected      = 0;

    userOutputFlag = 0;
    for(i = 1; i < ARGC; i++) {
	if(index(ARGV[i], "=") == 0) {
	    userOutput = ARGV[i];
	    userOutputFlag = 1;
	    ARGV[i] = "-";
	    break;
	}
    }
}

/RCS file:/ {
    repository[count] = $3;
    len = length(repository[count]);
    if (len > maxLenRepository)
	maxLenRepository = len;
    getline;

    if(repositoryFlag == 0) {
	idx = index(repository[count], "RCS");
	path = substr(repository[count], 0, idx - 1);
	file[count] = sprintf("%s%s", path, $3);
    }
    else {
	file[count] = repository[count];
    }
    len = length(file[count]);
    if (len > maxLenFile)
	maxLenFile = len;
    getline;

    getline; # branch line
    getline; # locks  line #1
    getline; # locks  line #2

    gsub(/:/, "", $1);

    user[count] = $1;
    len = length(user[count]);
    if (len > maxLenUser)
	maxLenUser = len;

    version[count]  = $2;
    len = length(version[count]);
    if (len > maxLenVersion)
	maxLenVersion = len;

#    count++;
}

/total revisions:/ {
    selected[count] = $6

    count++;
}

END {
    if ((bannerFlag == 1) && (count > 0)) {
	if (repositoryFlag == 1) {
	    printf("%s", bannerRepository);
	    lenField = length(bannerRepository);
	    maxLenField = maxLenRepository;
	}
	else {
	    printf("%s", bannerFile);
	    lenField = length(bannerFile);
	    maxLenField = maxLenFile;
	}
	if (versionFlag == 1) {
	    printf("%s %s",
		   rep(maxLenField - lenField, " "),
		   bannerVersion);
	}
	if (userFlag == 1) {
	    if (versionFlag == 1)
		printf("%s %s",
		       rep(maxLenVersion - length(bannerVersion), " "),
		       bannerUser);
	    else
		printf("%s %s",
		       rep(maxLenField - lenField, " "),
		       bannerUser);
	}
	printf("\n%s", rep(maxLenField, "-"));
	if (versionFlag == 1) {
	    printf(" %s", rep(maxLenVersion, "-"));
	}
	if (userFlag == 1) {
	    printf(" %s", rep(maxLenUser, "-"));
	}
	printf("\n");
    }
    outputCount = 0;
    for(i = 0; i < count; i++) {
	if(userOutputFlag == 1) {
	    if(user[i] != userOutput)
		continue;
	}
	if (repositoryFlag == 1) {
	    printf("%s", repository[i]);
	    lenField = length(repository[i]);
	    maxLenField = maxLenRepository;
	}
	else {
	    printf("%s", file[i]);
	    lenField = length(file[i]);
	    maxLenField = maxLenFile;
	}
	if (versionFlag == 1)
	    printf("%s %s",
		   rep(maxLenField - lenField, " "),
		   version[i]);
	if (userFlag == 1) {
	    if (versionFlag == 1)
		printf("%s %s",
		       rep(maxLenVersion - length(version[i]), " "),
		       user[i]);
	    else
		printf("%s %s",
		       rep(maxLenField - lenField, " "),
		       user[i]);
	}
        printf(" %s", selected[i]);
	printf("\n");

	outputCount++;
    }
    if ((bannerFlag == 1) && (count > 0)) {
	    printf("%s", rep(maxLenField, "-"));
	    if (versionFlag == 1) {
		printf(" %s", rep(maxLenVersion, "-"));
	    }
	    if (userFlag == 1) {
		printf(" %s", rep(maxLenUser, "-"));
	    }
	printf("\n%d / %d object(s) locked\n", outputCount, count);
    }
}

function rep(n, s, t) {
    while (n-- > 0)
	t = t s;
    return t;
}
