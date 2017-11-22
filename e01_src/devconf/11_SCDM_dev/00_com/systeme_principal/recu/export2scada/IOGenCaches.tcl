#### Generated from the IO class

#### Version
#### 20150422 SYAU INIT
#### 20150424 SYAU Added time to trace and log file

#### Usage: Description avaiable At bottom of this file

namespace eval ::HSCSIO {
	package require tdom
	
	variable _TRACE_LVL_1		1
	variable _TRACE_LVL_2		2
	variable _TRACE_LVL_6		6
	
	variable _GENERAL_DOMAIN	GENERAL
	variable _ERROR_DOMAIN		ERROR
	
	set g_db_version ""
	set g_db_dir ""

	variable g_EqpType
	variable g_alias
	variable g_nom_instance

	variable EQP
	variable IO
	variable ALIAS
	variable IOFile
	variable EqpInstance
	variable LocSystem

	variable g_eqp_alias_vetable_list
	
	variable PATH_BASE
	variable PATH_IO
	variable PATH_ENV
	variable PATH_XML
	variable PATH_DAC
	
	variable ENVs
	set ENVs [list SILENV WILENV KTEENV]
	
	variable STA_LIST
	set STA_LIST(SILENV) {L01 L02 L03 L04 L05 L06}
	set STA_LIST(WILENV) {S01 S02 S03}
	set STA_LIST(KTEENV) {K01 K02 K03}
	
	set VIRTUAL_NAME_MAPPING {
		{S01 KET}
		{S02 HKU}
		{S03 SYP}
		{OCC OCC}
		{L01 HKB}
		{L02 NFP}
		{L05 LWB}
		{L06 HYZ}
		{L01 ADM}
		{L02 OCP}
		{L03 WCH}
		{L04 LET}
		{L05 SOH}
		{L06 WCD}
		{K01 HOM}
		{K02 WHA}
		{K03 WAB}
	}
	
	foreach sta $VIRTUAL_NAME_MAPPING {
		set Vir_Name_2_Id([lindex $sta 1]) [lindex $sta 0]
	}   

}

namespace eval ::HSCSIO {

proc setTracePath { PATH {LogToFileOff 1} } {
	variable p
	variable pOff
	set p $PATH
	set pOff $LogToFileOff
	
	# Open the file for writing only. Truncate it if it exists. If it doesn't exist, create a new file. 
	
	if { $pOff == 0 } {
		::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_6  $::HSCSIO::_GENERAL_DOMAIN "::HSCSIO::Log to file $p"
		set f [ open $p w ] 
		close $f
	} else {
		::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_6  $::HSCSIO::_GENERAL_DOMAIN "::HSCSIO::Log to file disabled"
	}
}

proc trace { PROC TRACE_LVL DOMAIN MESSAGE } {
	variable p
	variable pOff
	variable f
	
	set log " \[[clock format [clock seconds] -format {%Y-%m-%d %H:%M:%S}]\] $PROC $DOMAIN $MESSAGE"
	puts $log
	
	if { $pOff == 0 } {
		# Open the file for writing only. The file must already exist, and the file is positioned so that new data is appended to the file. 
		set f [ open $p a ]
		puts $f $log
		close $f
	}
}

# Entry Point
proc GenIOFile { ENV VER IN_PATH OUT_PATH LOG } {	

	variable PATH_BASE	$IN_PATH
	variable PATH_IO	${PATH_BASE}/IO
	variable PATH_DB	${PATH_IO}/${ENV}_${VER}
	variable PATH_XML	${PATH_BASE}/Database/xml
	variable PATH_DAC	${PATH_BASE}/dac
	
	variable PATH_ENV	${OUT_PATH}/DBEnv/${ENV}_${VER}	
	
	variable STA_LIST
	
	variable STAs
	
	::HSCSIO::setTracePath ${PATH_ENV}.log $LOG
	
	::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_6  $::HSCSIO::_GENERAL_DOMAIN "ENV=\[$ENV\]"
	::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_6  $::HSCSIO::_GENERAL_DOMAIN "VER=\[$VER\]"
	::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_6  $::HSCSIO::_GENERAL_DOMAIN "IN_PATH=\[$IN_PATH\]"
	::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_6  $::HSCSIO::_GENERAL_DOMAIN "OUT_PATH=\[$OUT_PATH\]"
	
	set STAs $STA_LIST($ENV)

	if { [file exists $PATH_ENV] != 1 } { file mkdir $PATH_ENV }
	
	set start [clock seconds]

	if { 0 == [ file exists ${PATH_DB}.ini ] } {
		CreateNewCache $ENV
	}
	
	set end [clock seconds]
	::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_6  $::HSCSIO::_GENERAL_DOMAIN "Total Cost: \[[expr $end - $start]\](s)"
}

proc CreateNewCache { ENV } { 
    if [catch {
	
        # GenerateDbEnvFiles_RemoveAllFiles
        # RemoveAllIOCaches

		#if { $ENV == "SILENV" } {
		ProcessEVariables
		#}
		ProcessNewCaches
		GenerateDbEnvFiles

    } CatchMsgResult ] {
        ::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_2  $::HSCSIO::_ERROR_DOMAIN "CatchMsgResult=$CatchMsgResult"
    }
}

proc RemoveAllIOCaches {} {
return
	::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_1  $::HSCSIO::_GENERAL_DOMAIN "begin to remove all IO files..."
	variable PATH_IO
	if [catch {
		set io_file $PATH_IO
		regsub {.ini$} $io_file "" io_folder
		file delete -force $io_file
		file delete -force $io_folder
		::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_1  $::HSCSIO::_GENERAL_DOMAIN "removed the $io_folder"   
	} CatchMsgResult ] {
		::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_2  $::HSCSIO::_ERROR_DOMAIN "CatchMsgResult=$CatchMsgResult"
	}
}


proc ReadXmlFileAsDom {xmlFileName} {
	::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_6  $::HSCSIO::_GENERAL_DOMAIN "xmlFileName=$xmlFileName"   
	variable document
	if [ catch {
		set fp [open $xmlFileName r]
		fconfigure $fp -encoding utf-8
		set data [read $fp]
		close $fp

		set document [dom parse $data]
	} CatchMsgResult ] {
		::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_2  $::HSCSIO::_ERROR_DOMAIN "CatchMsgResult = $CatchMsgResult"
		set document 0
	}
	return $document
}

#read the basicEquipmentInstances.xml
#infomration can get:
#   EqpInstance(alias) [shortLabel, label]
#       e.g. HKUAFCGATE0001 [Gate,07]
#       e.g. HKUESCESC_0001 [Escalator,ESC-01]
proc ProcessBasicEquipmentInstances {doc fp} {
    variable g_EqpType
    variable g_alias

    variable EqpInstance

    if [catch {
        set nodes [$doc getElementsByTagName "InstanceDescription"]

        foreach node $nodes {

            # 2013-05-06 make sure variables are initialized
            set l_alias ""
            set l_shortLabel ""
            set l_label ""
            set l_eqpType ""

            #get the alias
            if {[$node hasAttribute "alias"]} {
                set l_alias [$node getAttribute "alias"]
                lappend g_alias $l_alias
            }

            #go to the <Context>
            set node [$node firstChild]

            #process the label node
            set nodeLabel [$node find attrId "label"]
            if {$nodeLabel != ""} {
                set valueNode [$nodeLabel firstChild]
                set l_label [$valueNode getAttribute "value"]
            }

            #process the shortLabel node
            set shortLabelNode [$node find attrId "shortLabel"]
            if {$shortLabelNode != ""} {
                set valueNode [$shortLabelNode firstChild]
                set l_shortLabel [$valueNode getAttribute "value"]
            }

            #process the EqpType node
            set eqpTypeNode [$node find attrId "EqpType"]
            if {$eqpTypeNode != ""} {
                set valueNode [$eqpTypeNode firstChild]
                set l_eqpType [$valueNode getAttribute "value"]
            }

            #save the information to EqpInstance
            if {$l_alias != ""} {
                set EqpInstance($l_alias) "$l_shortLabel,$l_label"
                #puts "EqpInstance($l_alias) $l_shortLabel,$l_label"
                set EqpInstance($l_alias) "[list $l_shortLabel],[list $l_label]"
                puts $fp "set EqpInstance($l_alias) {[list $l_shortLabel],[list $l_label]}"
            }

            #save the eqptype with alias
            set l_type_name [string range $l_alias 3 5]
            set l_eqpType "$l_type_name$l_eqpType"
            if {![info exists g_EqpType($l_eqpType)]} {
                set g_EqpType($l_eqpType) $l_alias
                #puts "***debug, $l_eqpType=$l_alias"
            }
        }

    } CatchMsgResult] {
        ::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_2  $::HSCSIO::_ERROR_DOMAIN "CatchMsgResult=$CatchMsgResult"
    }
}

# Create a list of valid iopoints
variable IO_List

proc ProcessAliasDetails {doc fp} {
    variable g_alias
    variable ALIAS
    variable g_hmi_order
    variable g_eqp_alias_vetable_list
    variable LocSystem
    variable IO_List
    variable io_name
    variable io_hmiorder

    set IO_List [list]
    array set LocSystem [list]
    set listItem [list]
    set l_alias ""
    if [catch {
        set root [$doc documentElement]
        set node [$root selectNodes /HierarchyDescription]

        foreach alias $g_alias {
            set lstIOname [list]
            set lstHmiOrder [list]

            set targetNode [$node find alias $alias]
            if {[$targetNode hasChildNodes]} {
                set childNodes [$targetNode childNodes]
                
                foreach childNode $childNodes {
                    set io_name [$childNode getAttribute "name"]
                    set l_alias [$childNode getAttribute "alias"]
                    if {[info exists g_hmi_order($l_alias)]} {
                        set io_hmiorder $g_hmi_order($l_alias)                   
                    } else {
                        set io_hmiorder 0
                    }
					lappend lstIOname $io_name
                    lappend lstHmiOrder $io_hmiorder
                    lappend IO_List $alias$io_name

                    #process the inner hierarchy level, dac, dal, dfo...
                    if {[$childNode hasChildNodes]} {
                        set subNodes [$childNode childNodes]
                        foreach subNode $subNodes {
                            lappend g_eqp_alias_vetable_list [$subNode getAttribute "alias"]
                        }
                    }
                }
            }
            set ALIAS($alias) "[list $lstIOname] [list $lstHmiOrder]"
            puts $fp "set ALIAS($alias) \"[list $lstIOname] [list $lstHmiOrder]\""

            set locSystemName [string range $alias 0 5]
            if {[info exists LocSystem($locSystemName)]} {
                set listItem $LocSystem($locSystemName)
                lappend listItem "<alias>$alias"
                set LocSystem($locSystemName) $listItem
            } else {
                set listItem [list]
                lappend listItem "<alias>$alias"
                set LocSystem($locSystemName) $listItem
            }
        }

        foreach {keys values} [array get LocSystem] {
            puts $fp "variable $keys"
            puts $fp "set $keys [list $values]"
        }

    } CatchMsgResult ] {
        ::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_2  $::HSCSIO::_ERROR_DOMAIN "CatchMsgResult=$CatchMsgResult"
    }

}

proc ProcessHmiOrderSpecially {} {
    variable g_hmi_order
    variable g_nom_instance
	
	variable PATH_XML

    set l_alias ""
    set l_hmi_order 0

    set l_nom_instance ""
    set l_label ""

    set file_list [list]
    lappend file_list "aci_cb_typeInstances.xml"
    lappend file_list "aci_mt_typeInstances.xml"
    lappend file_list "aci_st_typeInstances.xml"

    lappend file_list "dci_cb_typeInstances.xml"
    lappend file_list "dci_mt_typeInstances.xml"
    lappend file_list "dci_soe_typeInstances.xml"
    lappend file_list "dci_st_typeInstances.xml"

    lappend file_list "aio_typeInstances.xml"

    lappend file_list "dio_typeInstances.xml"
	
    lappend file_list "sci_cb_typeInstances.xml"
    lappend file_list "sci_mt_typeInstances.xml"
    lappend file_list "sci_st_typeInstances.xml"
	
	lappend file_list "sio_typeInstances.xml"
	
	lappend file_list "SCI_TYPEInstances.xml"

    if [catch {
        foreach xmlFile $file_list {
            set docDom [ReadXmlFileAsDom ${PATH_XML}/${xmlFile}]
            set doc [$docDom documentElement]
            set nodes [$doc getElementsByTagName "InstanceDescription"]
            foreach node $nodes {
                if {[$node hasAttribute "alias"]} {
                    set l_alias [$node getAttribute "alias"]
                }
                if {[$node hasChildNodes]} {
                    set contextNode [$node firstChild]
                    if {[$contextNode hasChildNodes]} {
                        set attNodes [$contextNode childNodes]
                        foreach attNode $attNodes {
                            set attId [$attNode getAttribute "attrId"]

                            #do the hmiOrder tag
                            if {$attId == "hmiOrder"} {
                                if {[$attNode hasChildNodes]} {
                                    set valueNode [$attNode firstChild]
                                    if {[$valueNode hasAttribute "value"]} {
                                        set l_hmi_order [$valueNode getAttribute "value"]
                                    }
                                }
                            }

                            #do the nom_instance tag
                            if {$attId == "nom_instance"} {
                                if {[$attNode hasChildNodes]} {
                                    set valueNode [$attNode firstChild]
                                    if {[$valueNode hasAttribute "value"]} {
                                        set l_nom_instance [$valueNode getAttribute "value"]
                                    }
                                }
                            }

                            #do the label tag
                            if {$attId == "label"} {
                                if {[$attNode hasChildNodes]} {
                                    set valueNode [$attNode firstChild]
                                    if {[$valueNode hasAttribute "value"]} {
                                        set l_label [$valueNode getAttribute "value"]
                                    }
                                }
                            }
                        }

                        if {$l_alias != ""} {
                            set g_hmi_order($l_alias) $l_hmi_order
                            #puts "DEBUG: l_alias=$l_alias, l_hmi_order=$l_hmi_order"
                            set l_hmi_order 0
                        }

                        if {$l_label != "" && $l_nom_instance != ""} {
                            set g_nom_instance($l_nom_instance) $l_label
                            #puts "g_nom_instance($l_nom_instance) $l_label"
                        }
                    }
                }
            }
        }
    } CatchMsgResult ] {
        ::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_2  $::HSCSIO::_ERROR_DOMAIN "CatchMsgResult=$CatchMsgResult"
    }

}

proc ProcessEqp {fp} {
    variable ALIAS
    variable EQP
    variable g_EqpType

    if [catch {
        foreach {eqp alias} [array get g_EqpType] {
            set d1 [lindex $ALIAS($alias) 0]
            set d2 [lindex $ALIAS($alias) 1]
            set EQP($eqp) "[list $d1] [list $d2]"
            puts $fp "set EQP($eqp) \"[list $d1] [list $d2]\""
        }
    } CatchMsgResult ] {
        ::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_2  $::HSCSIO::_ERROR_DOMAIN "CatchMsgResult=$CatchMsgResult"
    }
}

# Generate IO array based on IO_List and *typeInstances.xml
proc ProcessIO {_doc fp} {

	variable PATH_XML

	variable IO_List
    variable g_nom_instance

	if [catch {
		set documents [list]
		lappend documents "aal_typeInstances.xml"
		lappend documents "dal_typeInstances.xml"
		lappend documents "dal_type32Instances.xml"
		lappend documents "dal_type12Instances.xml"
		lappend documents "dio_typeInstances.xml"
		lappend documents "aio_typeInstances.xml"
		lappend documents "sio_typeInstances.xml"

		foreach docFile $documents {
			set doc [ReadXmlFileAsDom ${PATH_XML}/${docFile}]
			set nodes [$doc getElementsByTagName "InstanceDescription"]
			foreach node $nodes {
			if {[$node hasAttribute "alias"]} {
				set _first 0
				set _alias ""
				set _cmp ""
				set alias [$node getAttribute "alias"]
				if {[string match *dci* $alias]} {
					set _first [string first "dci" $alias]
					set _alias [string range $alias $_first end-3]
					set _cmp [string range $alias 0 end-3]
				} elseif {[string match *dio* $alias]} {
					set _first [string first "dio" $alias]
					set _alias [string range $alias $_first end]
					set _cmp [string range $alias 0 end]
				} elseif {[string match *aci* $alias]} {
					set _first [string first "aci" $alias]
					set _alias [string range $alias $_first end-3]
					set _cmp [string range $alias 0 end-3]
				} elseif {[string match *aio* $alias]} {
					set _first [string first "aio" $alias]
					set _alias [string range $alias $_first end]
					set _cmp [string range $alias 0 end]	
				} elseif {[string match *sci* $alias]} {
					set _first [string first "sci" $alias]
					set _alias [string range $alias $_first end-3]
					set _cmp [string range $alias 0 end-3]
				} elseif {[string match *sio* $alias]} {
					set _first [string first "sio" $alias]
					set _alias [string range $alias $_first end]
					set _cmp [string range $alias 0 end]
				} else {
					::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_6  $::HSCSIO::_GENERAL_DOMAIN  "Unfiltered alias=$alias"
				}
				set _eqp [string range $alias 6 9]

				# Check for valid alias and guard duplicated entry
				if {[info exists IO_List]} {			 
				if {[lsearch $IO_List $_cmp] > 0} {
					if { [info exists IOLIST($_eqp$_alias)] == 1 } {
						continue;
					}		    			

					set contextNode [$node firstChild]

						if {[$contextNode hasChildNodes]} {
							set attrNodes [$contextNode childNodes]
							foreach attrNode $attrNodes {
								set l_list_id [list]
								set l_list_label [list]
								set l_list_3 [list]	 
								set attrId [$attrNode getAttribute "attrId"]
								if {[string match *sio* $alias]} {
									if {$attrId == "label"} { 
										# set scalarValueNodes [$attrNode firstChild]
										# lappend l_list_label [$scalarValueNodes getAttribute value]
										if {[info exists g_nom_instance($_alias)]} {
											puts $fp "set IO($_eqp$_alias) {\"$g_nom_instance($_alias)\" [list $l_list_label] [list $l_list_id] [list $l_list_3]}"
											set IOLIST($_eqp$_alias) ""
										}
									}
								} elseif {[string match *aio* $alias]} {
									if {$attrId == "label"} { 
										set scalarValueNodes [$attrNode firstChild]
										lappend l_list_label [$scalarValueNodes getAttribute value]
										if {[info exists g_nom_instance($_alias)]} {
											puts $fp "set IO($_eqp$_alias) {\"$g_nom_instance($_alias)\" [list $l_list_label] [list $l_list_id] [list $l_list_3]}"
											set IOLIST($_eqp$_alias) ""
										}
									}
								} else {
									if {$attrId == "valueTable"} {
										set structVectorNodes [$attrNode firstChild]
										set structFieldNodes [$structVectorNodes childNodes]
										foreach structFieldNode $structFieldNodes {
											set scalarValueNodes [$structFieldNode childNodes]
											set i 0
											foreach scalarValueNode $scalarValueNodes {
												if {$i == 0} {
													lappend l_list_id [$scalarValueNode getAttribute value]
												} elseif {$i == 1} {
													lappend l_list_label [$scalarValueNode getAttribute value]
												} elseif {$i == 2} {
													lappend l_list_3 [$scalarValueNode getAttribute value]
												}
												incr i
											}
										}

										if {[info exists g_nom_instance($_alias)]} {
											puts $fp "set IO($_eqp$_alias) {\"$g_nom_instance($_alias)\" [list $l_list_label] [list $l_list_id] [list $l_list_3]}"
											set IOLIST($_eqp$_alias) ""
										}	
									# if {$attrId == "valueTable"}
									}
								# if *aio* not match
								}
							# foreach attrNode $attrNodes
							}
						# if contextNode hasChildNodes
						}
					# if alias exists in IO List
					}
				# if IO_List exists
				}
				#if node has Attribute alias
				}
			# for each node $nodes
			}
		#foreach docFile $documents
		} 
	# catching
	} CatchMsgResult ] {
		::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_2  $::HSCSIO::_ERROR_DOMAIN "CatchMsgResult=$CatchMsgResult"
	}
}

# Find out the AI EV from EVFile
proc ProcessEVariables {} {

	variable PATH_ENV
	variable PATH_DAC

	::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_6  $::HSCSIO::_GENERAL_DOMAIN "PATH_DAC=$PATH_DAC"
	
	set ListOfEVFiles [glob -directory "${PATH_DAC}/" "*EVariables.dat"]
	
	if { [llength $ListOfEVFiles ] > 0 } {
	
		set AIFile "${PATH_ENV}/Anal_Process.csv"
	
		::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_6  $::HSCSIO::_GENERAL_DOMAIN "AIFile=$AIFile"
	
		set fp [open $AIFile w]
		
		foreach EVFile $ListOfEVFiles {
		
			::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_6  $::HSCSIO::_GENERAL_DOMAIN "EVFile=$EVFile"
		
			set fEV [open $EVFile r]
			set file_data [read $fEV]
			set data [split $file_data "\n"]
			close $fEV
			
			foreach l $data {
				if { [ string first "AEIV=" $l ] == 0 } {
					set d [split $l ";"]
					
					set d1 [string trimleft [lindex $d 1] " "]
					set d3 [lindex $d 3]
					set d5 [lindex $d 5]

					puts $fp "$d1,$d3,$d5,"
				}
			}
		}	
		close $fp
	} else {
puts "NO EV File Found!"
	}
}

proc ProcessNewCaches {} {

	variable PATH_ENV
	variable PATH_XML

	variable g_EqpType
	variable g_alias
	variable g_nom_instance

	variable EQP
	variable IO
	variable ALIAS
	variable IOFile
	variable EqpInstance

	set fp [open ${PATH_ENV}.ini w]

	ProcessHmiOrderSpecially

	set docBasicEqpInst		[ReadXmlFileAsDom "${PATH_XML}/basicEquipmentInstances.xml"]
	ProcessBasicEquipmentInstances $docBasicEqpInst $fp

	#added for ENV equipment @31Jul2014 
	set docBasicEqpInst		[ReadXmlFileAsDom "${PATH_XML}/HSERInstances.xml"]
	ProcessBasicEquipmentInstances $docBasicEqpInst $fp

	set docBasicEqpInst		[ReadXmlFileAsDom "${PATH_XML}/POSTInstances.xml"]
	ProcessBasicEquipmentInstances $docBasicEqpInst $fp

	set docInstHierarchy	[ReadXmlFileAsDom "${PATH_XML}/instancesHierarchy.xml"]
	ProcessAliasDetails $docInstHierarchy $fp

	ProcessEqp $fp

	set docDalInstance		[ReadXmlFileAsDom "${PATH_XML}/dal_typeInstances.xml"]
	ProcessIO $docDalInstance $fp

	close $fp
}

proc GenerateDbEnvFiles {} {
    ::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_1  $::HSCSIO::_GENERAL_DOMAIN "START generate new DbEnv files"
    if [catch {
        GenerateDbEnvFiles_StationNumbers
        GenerateDbEnvFiles_Data                
    } CatchMsgResult ] {
        ::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_2  $::HSCSIO::_ERROR_DOMAIN "CatchMsgResult=$CatchMsgResult"
    }
    ::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_1  $::HSCSIO::_GENERAL_DOMAIN "END generate new DbEnv files"
}

proc GenerateDbEnvFiles_StationNumbers {} {
	variable STAs
	variable PATH_ENV
    if [catch {
		set staList $STAs
        foreach sta $staList {
            set env_dir ${PATH_ENV}/$sta
            if {[file exists $env_dir] == 0} {
                ::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_6  $::HSCSIO::_GENERAL_DOMAIN "Create target DbEnv directory $env_dir"
                file mkdir $env_dir   
            }            
        } 
    } CatchMsgResult ] {
        ::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_2  $::HSCSIO::_ERROR_DOMAIN "CatchMsgResult=$CatchMsgResult"
    }
}

proc GenerateDbEnvFiles_Data {} {
    variable ALIAS
    if [catch {
        foreach {key val} [array get ALIAS] {
            GenerateDbEnvFiles_GetFileName $key            
        }
    } CatchMsgResult ] {
        ::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_2  $::HSCSIO::_ERROR_DOMAIN "CatchMsgResult=$CatchMsgResult"
    } 
}

proc GenerateDbEnvFiles_GetFileName { key } {
    variable PATH_ENV
    variable ALIAS
    if [catch {
        set sta_name [string range $key 0 2]
        if {$sta_name != "OCC" && $sta_name != ""} {
            set STA $::HSCSIO::Vir_Name_2_Id($sta_name)
            set SYS [string range $key 3 5]
            set file_name "${PATH_ENV}/${STA}/listofpoints_${SYS}.ini"
            set record_io [lindex $ALIAS($key) 0]
			if { [ file exists $file_name ] == 0 } {
				set f [ open $file_name w ]
				close $f
			}
            set fp [open $file_name a]
            foreach io $record_io {
                set dci_idx [string first "dci" $io]
                set aci_idx [string first "aci" $io]
				set sci_idx [string first "sci" $io]
                
                if {$dci_idx != -1 || $aci_idx != -1 || $sci_idx != -1} {
                    puts $fp "[string range $key 0 2] $SYS $key $io"
                }
            }
            close $fp
        } 
    } CatchMsgResult ] {
        ::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_2  $::HSCSIO::_ERROR_DOMAIN "CatchMsgResult=$CatchMsgResult"
    }
}

proc GenerateDbEnvFiles_RemoveAllFiles {} {
    ::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_1  $::HSCSIO::_GENERAL_DOMAIN "begin to remove all DBEnv files..."
	variable PATH_ENV
	variable STAs
    if [catch {
        foreach sta $STAs {
            set env_dir ${PATH_ENV}/$sta
            file delete -force $env_dir
            ::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_1  $::HSCSIO::_GENERAL_DOMAIN "removed the dir $env_dir"   
        }    
    } CatchMsgResult ] {
        ::HSCSIO::trace [lindex [info level 0] 0] $::HSCSIO::_TRACE_LVL_2  $::HSCSIO::_ERROR_DOMAIN "CatchMsgResult=$CatchMsgResult"
    }
}

#end of namespace HSCSIO
}

# Main LOOP
if { $::argc >= 2 } {
	::HSCSIO::GenIOFile [lindex $::argv 0] [lindex $::argv 1] [lindex $::argv 2] [lindex $::argv 3] [lindex $::argv 4]
} else {
	puts "Args: tclsh IOGenCaches.tcl ENV DB_VERSION IN_PATH OUT_PATH"
	puts "ENV: ENV of the Server"
	puts "DB_VERSION: DB Version"
	puts "IN_PATH: Source of the Database"
	puts "OUT_PATH: Destination of the Database"
	puts "LOG: 0 = enable, 1 = disable"
	puts "E.g.: tclsh IOGenCaches.tcl SILENV SIL_1431 \"D:/Temp/966/caches\" \"D:/Temp/966/caches\" 1
}
