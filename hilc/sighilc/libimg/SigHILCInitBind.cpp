#include "scsimgmodule.h"

#include "imgSigHILCServer_i.hh"

extern "C" {

#ifdef WIN32
__declspec(dllexport) 
#endif

  void imgrSigHILC_Init()
  {
    ADDTRYBIND(SigHILCServer);
  }

}//extern C

