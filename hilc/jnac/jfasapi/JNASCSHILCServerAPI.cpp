// ----------------------------------------------------------------------------
// COMPANY  : THALES
// LANGUAGE : C++
// ----------------------------------------------------------------------------
// Copyright © THALES.
// All rights reserved.
// Unauthorized access, use, reproduction or distribution is prohibited.
// ----------------------------------------------------------------------------
// Module   : GWebHMI
// File     : JNASCSHILCServerAPI.cpp
// Date     : 2017/03/15
// ----------------------------------------------------------------------------
// HISTORY  :
// ----------------------------------------------------------------------------
#include <SCSSigHILCServerAcc.h>
// -------------------------------------------------------------------------
#include <JNASCSHILCServerAPI.h>
#include <JNASCSConnector.h>

// ----------------------------------------------------------------------------
int JNA_HILCServer_HILCPreparationRequest(int hilcserverHandle,
                                      const char* i_sOperatorName,
                                      const char* i_sWorkStationName,
                                      int  i_sCmdType,
                                      int  i_sCmdValue,
                                      int i_sCmdValueDiv,
                                      const char* i_sEquipmentAlias,
                                      const char* i_sEquipmentType,
                                      const char* i_sCommandName)
{
  SCSAPITrace(SCS_LEVEL(1), "JNA_HILCServer_PreparationRequest()");
  SCSSigHILCServerAcc* server = JNASCSConnector::Instance()->getHILCServer(hilcserverHandle);
  if (server != NULL) {
    return server->HILCPreparationRequest (
        i_sOperatorName,
        i_sWorkStationName,
        i_sCmdType,
        i_sCmdValue,
		i_sCmdValueDiv,
        i_sEquipmentAlias,
        i_sEquipmentType,
        i_sCommandName);
  }
  return -1;
}
// -------------------------------------------------------------------------
int JNA_HILCServer_HILCConfirmRequest(int hilcserverHandle,
                                      const char* i_sOperatorName,
                                      const char* i_sWorkStationName,
                                      int  i_sCmdType,
                                      int  i_sCmdValue,
                                      int i_sCmdValueDiv,
                                      const char* i_sEquipmentAlias,
                                      const char* i_sEquipmentType,
                                      const char* i_sCommandName)
{
  SCSAPITrace(SCS_LEVEL(1), "JNA_HILCServer_HILCConfirmRequest()");
  SCSSigHILCServerAcc* server = JNASCSConnector::Instance()->getHILCServer(hilcserverHandle);
  if (server != NULL) {
    return server->HILCConfirmRequest (
        i_sOperatorName,
        i_sWorkStationName,
        i_sCmdType,
        i_sCmdValue,
		i_sCmdValueDiv,
        i_sEquipmentAlias,
        i_sEquipmentType,
        i_sCommandName);
  }
  return -1;
}
// -------------------------------------------------------------------------
