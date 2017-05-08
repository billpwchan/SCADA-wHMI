//+-----------------------------------------------------------------------
//|Company       : SYSECA
//|CSCI          : rdn
//|
//| Purpose           : 
//|
//| Filename          : SnapshotHelper.h
//|
//| Creation Date     : 11/03/2002
//|
//| Author            : MENCHI Jean-Christophe
//|
//| History           : date, name, action
//| 			21/10/1999	Administration ClearCase
//| 		Automatic template insertion
//|
//+-----------------------------------------------------------------------

// Comments with // are C++ comments
// Comments with /// or //@ are DOC++ instructions

#ifndef SNAPSHOTHELPER_H
#define SNAPSHOTHELPER_H

#include <fstream>

#include <scstimer.h>

#include "RED_api.h"

class OlsList;
class AscServer;
class RdnServer;

/**
   Helper class for implementing the snapshot necessary for the Scadasoft redundancy.

   This class has method to read and write all the types used by the server,
   including Scadasoft types ScsTimer and OlsList.

   This class trap the signal USR1 and USR2:
       USR1 will do a binary snapshot in the running directory.
       USR2 will do an ASCII snapshot in the running directory.
 */
class RED_API SnapshotHelper {
 public:
  /// Destructor
  virtual ~SnapshotHelper();
  
  /// Access to the single SnapshotHelper object.
  static SnapshotHelper* instance();
  
  /**
     Acces to the Scadasoft AscServer.

     The redundancy mecanism is implemented by the AscServer.
     If the SnapshotHelper does not have an AscServer nothing
     will happend and the return of the method isStandby
     and isEnvRedundant is not defined.
   */
  void setAscServer(AscServer* asc);
  AscServer* getAscServer() const;
  
  /**
     Acces to the RdnServer class.
   */
  void setRdnServer(RdnServer* serv);
  RdnServer* getRdnServer() const;

  /**
     Path to the snapshot directory.

     A directory with the name returned by Scadasoft::GetServerName()
     will be created under dirname.
   */
  void setSnapshotDir(const char* dirname);
  const char* getSnapshotDir() const;

  /**
     Format of the snapshot file.

     By default the snapshot file is a binary file
     for efficiency issues. For testing purpose the file
     may be saved in ASCII.
   */
  bool isBinary() const;
  void setBinaryMode(bool m = true);

  /**
     Return true if the physical environment is in standby mode.
   */
  bool isStandby() const;

  /**
     Return true if environment is started from a snapshot file
   */
  bool isInitFromSnap() const;

  /**
     Return true if the physical environment is in standby mode
     and all the process are not started.
   */
  bool isStandbyInit() const;

  /**
     Return true if the physical environment is part of
     a redundant logical environment.
   */
  bool isEnvRedundant() const;

  //=============================
  // snapshot methods
  //=============================

  enum ErrorType {
    NoError = 0,
    SnapshotDirNotDefine = 10,
    CannotOpenSnapshotFile = 11,
    CannotCreateSnapshotDir = 12,
    CannotCreateSnapshotFile = 13,
    WrongSnapshotFormat = 14
  };

  /**
     Load a snapshot.
   */
  int load(const char* snapdir = NULL);

  /** Do the foreground part of the snapshot.
      
      Only the foreground snapshot is implemented for now.
   */
  int foregroundDump();

  //=============================
  // Storage methods
  //=============================

  // basic types
  //-----------------------------
  void storeLong(long l);
  void storeULong(unsigned long ul);
  void storeShort(short s);
  void storeUShort(unsigned short us);
  void storeDouble(double l);
  void storeString(const char* st);  
  void storeMemBlock(const char* adr, size_t size);

  long readLong();
  unsigned long readULong();
  short readShort();
  unsigned short readUShort();
  double readDouble();
  char* readString();
  /* N'est pas utilise, au moins pour l'instant, et provoque une issue memory leak avec Klocwork
  void readMemBlock(char* adr, size_t size);
  */
  // scadasoft types
  //-----------------------------
  void addOlsList(OlsList* ol);
  bool removeOlsList(OlsList* ol);
  void removeAllOlsList();

  void storeTimer(const ScsTimer* scst, int reSync = 0);
  ScsTimer* readTimer(TimerCallbackFunction fct, void* arg = 0);
  ScsTimer* readTimer(unsigned int delta, 
		      TimerCallbackFunction fct, void* arg = 0);
  
  // files management
  //-----------------------------
  /// Add file in snapshot
  void copyFile(const char* sourcefilename);

  /// read file from snapshot and install it
  void installSnapFile();

  /// Add a directory in snapshot
  void copyDir(const char* sourcedirname);

  /// read directory from snapshot and install it
  void installSnapDir();

  /**
     User snapshot file.

     Return a file name where the user can store data
     during dump or read data during load.
     Each call return a new file.
  */
  char* getNextUserSnapFile();

  /// Get output snapshot file
  std::ofstream &getSnapshotOutFile();
  /// Get input snapshot file
  std::ifstream &getSnapshotInFile();

 protected:
  /// Constructor
  SnapshotHelper();

 private:
  class SnapshotHelperImpl;
  SnapshotHelperImpl* m_impl;
  
  // instance assign not expect : declare private operator = 
  // This avoid Klocwork Warning CL.FFM.ASSIGN
  SnapshotHelper& operator=(const SnapshotHelper&) { return *this; }
  // instance assign not expect : declare private copy contructor
  // This avoid Klocwork Warning CL.FFM.COPY
  SnapshotHelper(const SnapshotHelper& src) : m_impl(NULL) { /* do not create copies */ }  
  
};

#endif
