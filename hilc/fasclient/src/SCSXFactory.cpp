// ----------------------------------------------------------------------------
// COMPANY  : THALES
// LANGUAGE : C++
// ----------------------------------------------------------------------------
// Copyright © THALES.
// All rights reserved.
// Unauthorized access, use, reproduction or distribution is prohibited.
// ----------------------------------------------------------------------------
// Module   : fasclient
// File     : SCSXFactory.cpp
// Date     : 2017/03/15
// ----------------------------------------------------------------------------
#include <scsclient/SCSFactory.h>
#include <scsclient/SCSGenericServer.h>
#include <SCSSigHILCServerAcc.h>
#include <SCSXFactory.h>

// ----------------------------------------------------------------------------
SCSXFactory::SCSXFactory()
{
}
// ----------------------------------------------------------------------------
SCSServer*
SCSXFactory::createServer(const char* envname, const char* servername, int type)
{
  if (envname != NULL && servername != NULL) {
    if (type == SCSSigHILCServerAcc::scs_type) {
      return new SCSSigHILCServerAcc(envname, servername);
    } else {
      return SCSFactory::createServer(envname, servername, type);
    }
  }
  
  return (SCSServer*)NULL;
}
