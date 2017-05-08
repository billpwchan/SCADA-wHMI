# Apppeler ainsi :
# awk -f resume.awk fichier_log

BEGIN {
	displayEqtId = 0
}

{

if (substr($1,1,13) == "<<prepareCmd:")
{
	print "---\nprepareCmd"
	displayEqtId = 1
}

if (($1 == "<<State_Prep") && ($2 == "Send") && ($5 == "OK>>"))
{
	print "State_Prep Send Preparation Command OK"
}

if (substr($1,1,13) == "<<confirmCmd:")
{
	print "---\nconfirmCmd"
}

if (($1 == "<<State_Conf") && ($2 == "Send") && ($5 == "OK>>"))
{
	print "State_Conf Send Confirmation Command OK"
}

if (substr($1,1,14) == "equipmentAlias")
{
	if (displayEqtId == 1)
	{
		print $1
	}
}

if (substr($1,1,13) == "equipmentType")
{
	if (displayEqtId == 1)
	{
		print $1
	}
}

if (substr($1,1,11) == "commandName")
{
	if (displayEqtId == 1)
	{
		print $1
	}
	displayEqtId = 0
}

if ( (substr($10,1,6) == "State_") && ($7 == "state") )
{
	split($10, spState, "####")
	print spState[1]
}

# pas terrible car c'est un check qui se repete
#if ($8 == "intValue") 
#{
#	print $8 $9 substr($10,1,length($10-2))
#}

if (($2 == "No") && ($3 == "Error"))
{
	print substr($1, 3, length($1)-2) " " $2 " " $3
}

if ((substr($1, 3,length($1)-2) == "HILC_UpdateData::timerCallback") && ($2 == "deleteSession"))
{
	print "HILC_UpdateData::timerCallback deleteSession OK"
}

if ( (substr($1, 3,length($1)-2) == "SessionManager::deleteSession") && ($2 == "OK>>") )
{
	print "SessionManager::deleteSession OK"
}

if ( ($3 == ":Preparation") && ($5 == "not") && ($6 == "found" ))
{
	print "confirmCmd  Error :Preparation Command not found"
}

}

END {
}