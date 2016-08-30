package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

public class EquipmentReserve {

}


//# Return the EquipmentReservationID
//proc EquipmentReservationName { Screen } {
//	return ${::env(HOSTNAME)}_${Screen}
//}
//
//# This procedure using to reserve equipment
//# Input:	Env : Server name
//#			Addr: Equipment Point
//#			Mode:	1 Check reserved Only
//#					2 Write and Read Reserved (By it self id)
//# Return:	EqtReserved:	0 Not reserved
//#				 			1 Reserved by self
//#							2 Reserved by other
//proc EquipmentReservation { Screen Env Addr Mode } {
//	
//	variable DbAttrReserved
//	variable DbAttrReservedID
//	variable DbAttrReserveReqID
//	variable DbAttrUnReserveReqID
//	
//	set HostName_Screen ${::env(HOSTNAME)}_${Screen}
//
//	set EqtReserved 0
//	if { $Mode == 1 } {
//		if [ catch { 
//			set EqtReserved [ Database::ReadValue ${Addr}${DbAttrReservedID} $Env ]
//		} error ] {
//			::HSCSTrace::trace $::HSCSTrace::_TRACE_LVL_3 $::HSCSTrace::_WARNING_DOMAIN "DlgInspector::EquipmentReservation - CATCH failed : $error"
//			if { [ ScsStatus::isError ] == "True" } {
//				::HSCSTrace::trace $::HSCSTrace::_TRACE_LVL_3 $::HSCSTrace::_WARNING_DOMAIN "DlgInspector::EquipmentReservation - SCADAsoft reason : [ ScsStatus::getText ]"
//			}
//		}
//	}
//	if { $Mode == 2 } {
//		if [ catch { Database::WriteString ${HostName_Screen} ${Addr}${DbAttrReserveReqID} $Env } error ] {
//			::HSCSTrace::trace $::HSCSTrace::_TRACE_LVL_3 $::HSCSTrace::_WARNING_DOMAIN "DlgInspector::EquipmentReservation - CATCH failed : $error"
//			if { [ ScsStatus::isError ] == "True" } {
//				::HSCSTrace::trace $::HSCSTrace::_TRACE_LVL_3 $::HSCSTrace::_WARNING_DOMAIN "DlgInspector::EquipmentReservation - SCADAsoft reason : [ ScsStatus::getText ]"
//			}
//		}
//		if [ catch { 
//			set EqtReserved [ Database::ReadValue ${Addr}${DbAttrReservedID} $Env ]
//		} error ] {
//			::HSCSTrace::trace $::HSCSTrace::_TRACE_LVL_3 $::HSCSTrace::_WARNING_DOMAIN "DlgInspector::EquipmentReservation - CATCH failed : $error"
//			if { [ ScsStatus::isError ] == "True" } {
//				::HSCSTrace::trace $::HSCSTrace::_TRACE_LVL_3 $::HSCSTrace::_WARNING_DOMAIN "DlgInspector::EquipmentReservation - SCADAsoft reason : [ ScsStatus::getText ]"
//			}
//		}
//	}
//	if { $EqtReserved != "" && $EqtReserved != $HostName_Screen } {
//		set EqtReserved 2
//	} elseif { $EqtReserved != "" && $EqtReserved == $HostName_Screen } {
//		set EqtReserved 1
//	} elseif { $EqtReserved == "" } {
//		set EqtReserved 0
//	}
//	return $EqtReserved
//}
//
//# This procedure using to un-reserve equipment
//# Return:	EqtReserved:	0 Not reserved
//#				 			1 Reserved by self
//#							2 Reserved by other
//proc EquipmentUnreservation { Screen Env Addr } {
//	
//	variable DbAttrReserved
//	variable DbAttrReservedID
//	variable DbAttrReserveReqID
//	variable DbAttrUnReserveReqID
//	
//	set HostName_Screen ${::env(HOSTNAME)}_${Screen}
//	
//	set EqtReserved 0
//	
//	if [ catch { Database::WriteString ${HostName_Screen} ${Addr}${DbAttrUnReserveReqID} $Env } error ] {
//		::HSCSTrace::trace $::HSCSTrace::_TRACE_LVL_3 $::HSCSTrace::_WARNING_DOMAIN "DlgInspector::EquipmentReservation - CATCH failed : $error"
//		if { [ ScsStatus::isError ] == "True" } {
//			::HSCSTrace::trace $::HSCSTrace::_TRACE_LVL_3 $::HSCSTrace::_WARNING_DOMAIN "DlgInspector::EquipmentReservation - SCADAsoft reason : [ ScsStatus::getText ]"
//		}
//	}
//	if [ catch { 
//		set EqtReserved [ Database::ReadValue ${Addr}${DbAttrReservedID} $Env ]
//	} error ] {
//		::HSCSTrace::trace $::HSCSTrace::_TRACE_LVL_3 $::HSCSTrace::_WARNING_DOMAIN "DlgInspector::EquipmentReservation - CATCH failed : $error"
//		if { [ ScsStatus::isError ] == "True" } {
//			::HSCSTrace::trace $::HSCSTrace::_TRACE_LVL_3 $::HSCSTrace::_WARNING_DOMAIN "DlgInspector::EquipmentReservation - SCADAsoft reason : [ ScsStatus::getText ]"
//		}
//	}
//	if { $EqtReserved != "" && $EqtReserved != $HostName_Screen } {
//		set EqtReserved 2
//	} elseif { $EqtReserved != "" && $EqtReserved == $HostName_Screen } {
//		set EqtReserved 1
//	} elseif { $EqtReserved == "" } {
//		set EqtReserved 0
//	}
//	
//	return $EqtReserved
//}