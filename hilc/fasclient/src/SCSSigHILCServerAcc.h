// ----------------------------------------------------------------------------
// COMPANY  : THALES
// LANGUAGE : C++
// ----------------------------------------------------------------------------
// Copyright © THALES.
// All rights reserved.
// Unauthorized access, use, reproduction or distribution is prohibited.
// ----------------------------------------------------------------------------
// Module   : fasclient
// File     : SCSSigHILCServerAcc.h
// Date     : 2017/03/15
// ----------------------------------------------------------------------------
// HISTORY  :
// ----------------------------------------------------------------------------
#ifndef __SCSSIGHILCSERVERACC_H__
#define __SCSSIGHILCSERVERACC_H__
// ----------------------------------------------------------------------------
#include <scsclient/SCSServer.h>
#include <SigHILCServerAcc.h>
// ----------------------------------------------------------------------------
class SCSSigHILCServerAcc;
// ----------------------------------------------------------------------------
/// \class SCSSigHILCServerAcc
/// \brief HILC server services.
// ----------------------------------------------------------------------------
class SCSCLIENT_API SCSSigHILCServerAcc : public SCSServer {
public:
  /// \enum Type
  /// \par Description:
  /// Defines the type of Scadasoft service. Its value is unique and identifies this service among all others Scadasoft services.
  enum Type { scs_type = 32 };
  /// \par Description:
  /// Creates a new Scadasoft server services.
  /// \param[in] envname The Scadasoft server environment name.
  /// \param[in] servername The Scadasoft service name.
  SCSSigHILCServerAcc(const char* envname, const char* servername);
  virtual ~SCSSigHILCServerAcc();
  /// \par Description:
  /// Tries to establish a connection to the Scadasoft server.
  /// \param[in] direct Indicates whether or not the connection must be direct.
  /// \return The operation result (true means a success, false otherwise).
  virtual bool connect(bool direct = false);
  /// \par Description:
  /// Tries to close the connection to the Scadasoft server.
  /// \return The operation result (true means a success, false otherwise).
  virtual bool disconnect();
  ////////////////////////////////////////////////////// Preparation Requests //////////////////////////////////////////////////////             CmdPreparationRequest
  /// \par Description:
  /// Preparation of command request.
  /// \param[in] should identify the HMI operator.
  /// \param[in] should identify workstation name.
  /// \param[in] identify if the command will use a DIO (2) or AIO (3).
  /// \param[in] value to put in the DIO / AIO.
  /// \param[in] divisor (in case of AIO sending a float).
  /// \param[in] equipment’s UNIVNAME.
  /// \param[in] identify what is put between the “dio” and the “-Cmd” in the dio UNIVNAME.
  /// \param[in] name of the double command on which the Select step is called.
  /// \return The operation result.
    int HILCPreparationRequest (
        const char* i_sOperatorName,
        const char* i_sWorkStationName,
        unsigned int  i_sCmdType,
        int  i_sCmdValue,
		    unsigned int i_sCmdValueDiv,
        const char* i_sEquipmentAlias,
        const char* i_sEquipmentType,
        const char* i_sCommandName);
    ////////////////////////////////////////////////////// Confirmation request  //////////////////////////////////////////////////////               CmdConfirmRequest
    int HILCConfirmRequest (
        const char* i_sOperatorName,
        const char* i_sWorkStationName,
        unsigned int  i_sCmdType,
        int  i_sCmdValue,
		    unsigned int i_sCmdValueDiv,
        const char* i_sEquipmentAlias,
        const char* i_sEquipmentType,
        const char* i_sCommandName);
  /// \par Description:
  /// Returns the type of this Scadasoft service.
  /// \return The value of the type.
  virtual int serverType() const { return scs_type; }

protected:
  SCSSigHILCServerAcc() {}
  SCSSigHILCServerAcc(const SCSSigHILCServerAcc&) {}
  virtual void changeState();

private:
  class SCSSigHILCServerAccImpl;
  SCSSigHILCServerAccImpl* m_impl;
};
// ----------------------------------------------------------------------------
#endif // __SCSSIGHILCSERVERACC_H__
