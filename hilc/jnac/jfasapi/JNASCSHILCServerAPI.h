// ----------------------------------------------------------------------------
// COMPANY  : TTSHK
// LANGUAGE : C++
// ----------------------------------------------------------------------------
// Copyright © THALES.
// All rights reserved.
// Unauthorized access, use, reproduction or distribution is prohibited.
// ----------------------------------------------------------------------------
// Module   : GWebHMI
// File     : JNASCSHILCServerAPI.h
// Date     : 2017/03/13
// ----------------------------------------------------------------------------
// HISTORY  :
// ----------------------------------------------------------------------------
#ifndef __JNASCSHILCServerAPI__
#define __JNASCSHILCServerAPI__
// ----------------------------------------------------------------------------
#include <JNASCSDef.h>
// ----------------------------------------------------------------------------
/// \file JNASCSHILCServerAPI.h
/// \brief JNA API for HILC server services.

extern "C" {
  // ----------------------------------------------------------------------------
  /// \fn JNA_SCS_DECL int JNA_HILCServer_PreparationRequest
  /// \par Description:
  /// JNA API for changing the integer value of a variable.
  /// \param[in] handler.
  /// \param[in] should identify the HMI operator.
  /// \param[in] should identify workstation name.
  /// \param[in] identify if the command will use a DIO (2) or AIO (3).
  /// \param[in] value to put in the DIO / AIO.
  /// \param[in] divisor (in case of AIO sending a float).
  /// \param[in] equipment’s UNIVNAME.
  /// \param[in] identify what is put between the “dio” and the “-Cmd” in the dio UNIVNAME.
  /// \param[in] name of the double command on which the Select step is called.
  /// \return The operation result.
  JNA_SCS_API int JNA_HILCServer_HILCPreparationRequest(int hilcserverHandle,
                                                  const char* i_sOperatorName,
                                                  const char* i_sWorkStationName,
                                                  int  i_sCmdType,
                                                  int  i_sCmdValue,
                                          		    int i_sCmdValueDiv,
                                                  const char* i_sEquipmentAlias,
                                                  const char* i_sEquipmentType,
                                                  const char* i_sCommandName);    
  // ----------------------------------------------------------------------------
  /// \fn JNA_SCS_DECL int JNA_HILCServer_HILCConfirmRequest
  /// \par Description:
  /// JNA API for changing the integer value of a variable.
  /// \param[in] handler.
  /// \param[in] should identify the HMI operator.
  /// \param[in] should identify workstation name.
  /// \param[in] identify if the command will use a DIO (2) or AIO (3).
  /// \param[in] value to put in the DIO / AIO.
  /// \param[in] divisor (in case of AIO sending a float).
  /// \param[in] equipment’s UNIVNAME.
  /// \param[in] identify what is put between the “dio” and the “-Cmd” in the dio UNIVNAME.
  /// \param[in] name of the double command on which the Select step is called.
  /// \return The operation result.
  JNA_SCS_API int JNA_HILCServer_HILCConfirmRequest(int hilcserverHandle,
                                                  const char* i_sOperatorName,
                                                  const char* i_sWorkStationName,
                                                  int  i_sCmdType,
                                                  int  i_sCmdValue,
                                          		    int i_sCmdValueDiv,
                                                  const char* i_sEquipmentAlias,
                                                  const char* i_sEquipmentType,
                                                  const char* i_sCommandName);    
  // ----------------------------------------------------------------------------
}
#endif /* ! __JNASCSHILCServerAPI__ */
