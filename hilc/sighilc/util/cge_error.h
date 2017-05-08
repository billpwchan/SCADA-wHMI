/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*                                                                            */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/*............................................................................*/

/*
!!!!!!!!!! DO NOT EDIT !!!!!!!!!!
!! File automatically generated
!! by scsmkerror
!! at Thu Jun 14 13:22:30 2012
!!!!!!!!!! DO NOT EDIT !!!!!!!!!!
*/
#ifndef _AN_CGE_ERROR_H
#define _AN_CGE_ERROR_H
static AntStatus CgeInitError(AntStatus::AntError,900);
inline AntStatus
CgeErrInit(AntErrorSeverity severity
      ,short line, const char* file
      
      ,AntErrorContext context=AntDetection
      ,const char* source=0)

{
  if (context == AntDetection) AntTrace::trace().initializeErrorLevel();
  else AntTrace::trace().incrementErrorLevel();
  AntLogError(severity, line, file,
              "Init",
              source,
              "CGE",
              AntTrace::trace().getErrorFormat("cge_error.emf","Cge","Init"));
  return CgeInitError;
}
static AntStatus CgeParametersError(AntStatus::AntError,901);
inline AntStatus
CgeErrParameters(AntErrorSeverity severity
      ,short line, const char* file
      ,const  char * arg0
      ,AntErrorContext context=AntDetection
      ,const char* source=0)

{
  if (context == AntDetection) AntTrace::trace().initializeErrorLevel();
  else AntTrace::trace().incrementErrorLevel();
  AntLogError(severity, line, file,
              "Parameters",
              source,
              "CGE",
              AntTrace::trace().getErrorFormat("cge_error.emf","Cge","Parameters"),arg0);
  return CgeParametersError;
}
static AntStatus CgeScadasoftError(AntStatus::AntError,902);
inline AntStatus
CgeErrScadasoft(AntErrorSeverity severity
      ,short line, const char* file
      , int  arg0,const  char * arg1
      ,AntErrorContext context=AntDetection
      ,const char* source=0)

{
  if (context == AntDetection) AntTrace::trace().initializeErrorLevel();
  else AntTrace::trace().incrementErrorLevel();
  AntLogError(severity, line, file,
              "Scadasoft",
              source,
              "CGE",
              AntTrace::trace().getErrorFormat("cge_error.emf","Cge","Scadasoft"),arg0,arg1);
  return CgeScadasoftError;
}
static AntStatus CgeMemoryError(AntStatus::AntError,903);
inline AntStatus
CgeErrMemory(AntErrorSeverity severity
      ,short line, const char* file
      
      ,AntErrorContext context=AntDetection
      ,const char* source=0)

{
  if (context == AntDetection) AntTrace::trace().initializeErrorLevel();
  else AntTrace::trace().incrementErrorLevel();
  AntLogError(severity, line, file,
              "Memory",
              source,
              "CGE",
              AntTrace::trace().getErrorFormat("cge_error.emf","Cge","Memory"));
  return CgeMemoryError;
}
static AntStatus CgeORBError(AntStatus::AntError,904);
inline AntStatus
CgeErrORB(AntErrorSeverity severity
      ,short line, const char* file
      ,const  char * arg0
      ,AntErrorContext context=AntDetection
      ,const char* source=0)

{
  if (context == AntDetection) AntTrace::trace().initializeErrorLevel();
  else AntTrace::trace().incrementErrorLevel();
  AntLogError(severity, line, file,
              "ORB",
              source,
              "CGE",
              AntTrace::trace().getErrorFormat("cge_error.emf","Cge","ORB"),arg0);
  return CgeORBError;
}
static AntStatus CgeEnvironVarError(AntStatus::AntError,905);
inline AntStatus
CgeErrEnvironVar(AntErrorSeverity severity
      ,short line, const char* file
      ,const  char * arg0
      ,AntErrorContext context=AntDetection
      ,const char* source=0)

{
  if (context == AntDetection) AntTrace::trace().initializeErrorLevel();
  else AntTrace::trace().incrementErrorLevel();
  AntLogError(severity, line, file,
              "EnvironVar",
              source,
              "CGE",
              AntTrace::trace().getErrorFormat("cge_error.emf","Cge","EnvironVar"),arg0);
  return CgeEnvironVarError;
}
inline const char *
CgeGetErrorName(AntStatus status)
{
  switch(status.getMinor()) {
  case 900:
    return "Init";
  case 901:
    return "Parameters";
  case 902:
    return "Scadasoft";
  case 903:
    return "Memory";
  case 904:
    return "ORB";
  case 905:
    return "EnvironVar";
  default:
    return "";
  }
}
#endif
