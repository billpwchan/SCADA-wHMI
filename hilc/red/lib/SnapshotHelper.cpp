//+-----------------------------------------------------------------------
//|Company       : SYSECA
//|CSCI          : rdn
//|
//| Purpose           : 
//|
//| Filename          : SnapshotHelper.cpp
//|
//| Creation Date     : 11/03/2002
//|
//| Author            : MENCHI Jean-Christophe	
//|
//| History           : date, name, action
//|
//+-----------------------------------------------------------------------

// Comments with // are C++ comments
// Comments with /// or //@ are DOC++ instructions

// Standard Includes

#include <string.h>
#include <sys/stat.h>
#include <sys/types.h>

#ifndef WIN32
#include <dirent.h>
#include <signal.h>
#else
#include <direct.h>
#endif

#include <vector>
#include <algorithm>
#include <iostream>

// SCADAsoft Includes
#include "scadaorb.h"
#include "scstimer.h"
#include "ols.h"
#include "olstypes.h"
#include "asc.h"
#include "agent.h"
#include "scs.h"
#include "scsthread.h"
#include "red.h"
#include "ascconnection.h"
#include "manager.h"
#include "scsversion.h"

// Local Includes
#include "SnapshotHelper.h"
#include "RdnServer.h"
#include "RdnTraces.h"
#include "RdnError.h"

SCS_SL_STD_USING

//==============================================================================+
// SnapshotHelper INTERNAL CLASS DECLARATION
//==============================================================================+
class SnapshotHelper::SnapshotHelperImpl {
public:
  SnapshotHelperImpl(SnapshotHelper* sh) : m_ascServer(NULL), m_snapshotDir(NULL),
					   m_binaryMode(true), m_standby(false), 
					   m_ascAgent(NULL), m_subsStateID(0),
					   m_redundant(false), m_shelper(sh),
					   m_rdnServer(NULL), m_lastUFID(0),
					   m_standbyInit(false) {}

  ~SnapshotHelperImpl() 
  {
    if (m_ascAgent) {
      delete m_ascAgent;
      m_ascAgent = NULL;
    }

    if (m_snapshotDir != NULL)
      delete [] m_snapshotDir;
  }

  void loadOlsList();
  void dumpOlsList();
  static const void copyFile(const char* src, const char* dst);
  static const void installSignal();
  void setSnapshotDir(const char* dirname);

  static ScsStatus foregroundSnap(const char* snapdir, void* arg);

  static void sigusr(int sig);
  static void stateChangeCB(const char *processName,
			    const char *state,
			    void* arg);

  // fields
  char* m_snapshotDir;
  AscServer* m_ascServer;
  RdnServer* m_rdnServer;
  AscAgent* m_ascAgent;
  int m_subsStateID;
  SnapshotHelper* m_shelper;

  vector<OlsList*> m_olsLists;
  ofstream m_currentOutFile;
  ifstream m_currentInFile;
  bool m_binaryMode;
  bool m_standby;
  bool m_standbyInit;
  bool m_redundant;
  unsigned long m_lastUFID;

  // static
  static SnapshotHelper* s_instance;
  static const char* s_extfileFilename;
  static const char* s_binaryDataFile;
  static const char* s_textDataFile;
  
private:
  // instance assign not expect : declare private operator = 
  // This avoid Klocwork Warning CL.FFM.ASSIGN
  SnapshotHelperImpl& operator=(const SnapshotHelperImpl&) { return *this; }
  // instance assign not expect : declare private copy contructor
  // This avoid Klocwork Warning CL.FFM.COPY
  SnapshotHelperImpl(const SnapshotHelperImpl& src) : m_ascServer(NULL), m_snapshotDir(NULL),
					   m_binaryMode(true), m_standby(false), 
					   m_ascAgent(NULL), m_subsStateID(0),
					   m_redundant(false), m_shelper(NULL),
					   m_rdnServer(NULL), m_lastUFID(0),
					   m_standbyInit(false)
  { /* do not create copies */
  }    
  
};
// SnapshotHelperImpl static init
SnapshotHelper* SnapshotHelper::SnapshotHelperImpl::s_instance = NULL;
const char* SnapshotHelper::SnapshotHelperImpl::s_extfileFilename = "userFile";
const char* SnapshotHelper::SnapshotHelperImpl::s_binaryDataFile = "snapFile.bin";
const char* SnapshotHelper::SnapshotHelperImpl::s_textDataFile = "snapFile.txt";


//==============================================================================+
// utility to make sure win32 paths are made with '\' only
//==============================================================================+
static void win32path( char *path )
{
  int i;
  for( i = 0; path[i]; i++ ) {
    if ( path[i] == '/' ) path[i] = '\\';
  }
}

//==============================================================================+
// SnapshotHelper CLASS IMPLEMENTATION
//==============================================================================+
//------------------------------------------------------------------------------+
// SnapshotHelper Constructor
//------------------------------------------------------------------------------+
SnapshotHelper::SnapshotHelper() {
  RdnTrace(K_TRACE_LEV_INTCALL, "SnapshotHelper::SnapshotHelper ===>    ");

  m_impl = new SnapshotHelperImpl(this);
  SnapshotHelperImpl::s_instance = this;

  RdnTrace(K_TRACE_LEV_INTCALL, "SnapshotHelper::SnapshotHelper <===    ");
}
//------------------------------------------------------------------------------+
// SnapshotHelper Destructor
//------------------------------------------------------------------------------+
SnapshotHelper::~SnapshotHelper()
{
  RdnTrace(K_TRACE_LEV_INTCALL, "SnapshotHelper::~SnapshotHelper ===>    ");

  SnapshotHelperImpl::s_instance = NULL;
  delete m_impl;

  RdnTrace(K_TRACE_LEV_INTCALL, "SnapshotHelper::~SnapshotHelper <===    ");
}
//------------------------------------------------------------------------------+
// SnapshotHelper::instance
//------------------------------------------------------------------------------+
SnapshotHelper* SnapshotHelper::instance() {
  RdnTrace(K_TRACE_LEV_INTCALL, "SnapshotHelper::instance ===>    ");
  if (SnapshotHelperImpl::s_instance == NULL) {
    SnapshotHelperImpl::s_instance = new SnapshotHelper;
  }
  RdnTrace(K_TRACE_LEV_INTCALL, "SnapshotHelper::instance <===    ");
  return SnapshotHelperImpl::s_instance;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::setAscServer
//------------------------------------------------------------------------------+
void SnapshotHelper::setAscServer(AscServer* asc) {
  RdnTrace(K_TRACE_LEV_INTCALL, "SnapshotHelper::setAscServer ===>    ");

  // remove previous callback
  if (m_impl->m_ascServer != NULL) {
    m_impl->m_ascServer->registerForegroundSnapshot(NULL, NULL);
  }
  if (m_impl->m_ascAgent)
    delete m_impl->m_ascAgent;
  m_impl->m_redundant = false;

  // set callback
  m_impl->m_ascServer = asc;
  if (asc != NULL) {
    asc->registerForegroundSnapshot(SnapshotHelperImpl::foregroundSnap, m_impl);
  
    // install handler on USR1 USR2
    m_impl->installSignal();

    // check redundancy
    if (Scadasoft::IsRedundant()) {
      m_impl->m_redundant = true;
      RdnTrace(SCS_LEVEL(0), "Logical environment is redundant.");
    } else {
      m_impl->m_redundant = false;
      RdnTrace(SCS_LEVEL(0), "Logical environment is not redundant.");
    }

    // check state
    m_impl->m_ascAgent = new AscAgent;
    char* firstState = NULL;
    m_impl->m_ascAgent->subscribeState("AscManager", m_impl->m_subsStateID, firstState,
				       SnapshotHelper::SnapshotHelperImpl::stateChangeCB,
				       m_impl);

    if (firstState != NULL)
	{
		if (strcmp(firstState, SCS_STANDBY) == 0
		|| strcmp(firstState, SCS_STANDBYSYNC) == 0
		|| strcmp(firstState, SCS_STANDBYISOLATED) == 0
		|| strcmp(firstState, SCS_STANDBYINIT) == 0
		|| strcmp(firstState, SCS_DOWN) == 0
		)
		{
			m_impl->m_standby = true;
			m_impl->m_standbyInit = false;
		}

		if (strcmp(firstState, SCS_STANDBYINIT) == 0)
		  m_impl->m_standbyInit = true;

		delete [] firstState;
    }
  }
  RdnTrace(K_TRACE_LEV_INTCALL, "SnapshotHelper::setAscServer <===    ");
}
//------------------------------------------------------------------------------+
// SnapshotHelper::getAscServer
//------------------------------------------------------------------------------+
AscServer* SnapshotHelper::getAscServer() const {
  return m_impl->m_ascServer;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::setRdnServer
//------------------------------------------------------------------------------+
void SnapshotHelper::setRdnServer(RdnServer* serv) {
  m_impl->m_rdnServer = serv;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::getRdnServer
//------------------------------------------------------------------------------+
RdnServer* SnapshotHelper::getRdnServer() const {
  return m_impl->m_rdnServer;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::setSnapshotDir
//------------------------------------------------------------------------------+
void SnapshotHelper::setSnapshotDir(const char* dirname) {
  RdnTrace(K_TRACE_LEV_INTCALL, "SnapshotHelper::setSnapshotDir ===>    ");

  if (m_impl->m_snapshotDir != NULL) {
    delete [] m_impl->m_snapshotDir;
    m_impl->m_snapshotDir = NULL;
  }
  if (dirname != NULL) {
    // if we use a snapshotdir we should standby
    m_impl->m_standby = true;
    m_impl->m_standbyInit = true;

    int ln = strlen(dirname);
    m_impl->m_snapshotDir = new char[ln+1];
    strcpy_s(m_impl->m_snapshotDir, ln+1, dirname);
  }

  RdnTrace(K_TRACE_LEV_INTCALL, "SnapshotHelper::setSnapshotDir <===    ");
}
//------------------------------------------------------------------------------+
// SnapshotHelper::getSnapshotDir
//------------------------------------------------------------------------------+
const char* SnapshotHelper::getSnapshotDir() const {
  return m_impl->m_snapshotDir;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::isInitFromSnap
//------------------------------------------------------------------------------+
bool SnapshotHelper::isInitFromSnap() const {
  if (m_impl->m_snapshotDir != NULL)
    return true;
  else
    return false;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::isBinary
//------------------------------------------------------------------------------+
bool SnapshotHelper::isBinary() const {
  return m_impl->m_binaryMode;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::setBinaryMode
//------------------------------------------------------------------------------+
void SnapshotHelper::setBinaryMode(bool m) {
  m_impl->m_binaryMode = m;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::isStandby
//------------------------------------------------------------------------------+
bool SnapshotHelper::isStandby() const {
  m_impl->m_standby=false;
  if (Scadasoft::GetState()==NULL) 
    {
      RdnTrace(SCS_LEVEL(0), "PLEASE call Scadasoft::initialise() BEFORE isStanby method !!!!");
      exit(1);
    }
  if ( strncmp(Scadasoft::GetState(),"SCS_STANDBY",11) == 0 &&
      Scadasoft::IsRedundant(Scadasoft::SCS_SYNCHRONOUS_REDUNDANCY)) {
    m_impl->m_standby=true;
  }
  return m_impl->m_standby;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::isStandbyInit
//------------------------------------------------------------------------------+
bool SnapshotHelper::isStandbyInit() const {
   m_impl->m_standbyInit = false;
   if (Scadasoft::GetState()==NULL) 
    {
      RdnTrace(SCS_LEVEL(0), "PLEASE call Scadasoft::initialise() BEFORE isStanbyInit method !!!!");
      exit(1);
    }
   if ( strcmp(Scadasoft::GetState(),"SCS_STANDBYINIT") == 0 &&
      Scadasoft::IsRedundant(Scadasoft::SCS_SYNCHRONOUS_REDUNDANCY)) {
    m_impl->m_standbyInit=true;
  }

  return m_impl->m_standbyInit;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::isEnvRedundant
//------------------------------------------------------------------------------+
bool SnapshotHelper::isEnvRedundant() const {
  return Scadasoft::IsRedundant();
}
//------------------------------------------------------------------------------+
// SnapshotHelper::load
//------------------------------------------------------------------------------+
int SnapshotHelper::load(const char * snapdir) {
  RdnTrace(K_TRACE_LEV_INTCALL, "SnapshotHelper::load ===>    ");
  m_impl->m_lastUFID = 1;

  if (snapdir != NULL)
    setSnapshotDir(snapdir);

  if (getSnapshotDir() == NULL)
    return SnapshotHelper::SnapshotDirNotDefine;

  // open file
  unsigned long len = strlen(getSnapshotDir()) + 1 
    + strlen(Scadasoft::GetServerName()) + 1
    + strlen(SnapshotHelperImpl::s_binaryDataFile);
  char* fname = new char[len+1];

  if (isBinary()) {
    sprintf(fname, "%s/%s/%s", getSnapshotDir(), Scadasoft::GetServerName(),
	    SnapshotHelperImpl::s_binaryDataFile);
  } else {
    sprintf(fname, "%s/%s/%s", getSnapshotDir(), Scadasoft::GetServerName(),
	    SnapshotHelperImpl::s_textDataFile);
  }

  bool oldMode = m_impl->m_binaryMode;

  m_impl->m_currentInFile.open(fname, ios::in);
  if (!m_impl->m_currentInFile.good()) {
    // switch file mode
    if (isBinary()) {
      RdnTrace(SCS_LEVEL(0), 
	       "Binary file %s does not exists, switching to text.",
	       fname);

      sprintf(fname, "%s/%s/%s", getSnapshotDir(), Scadasoft::GetServerName(),
	      SnapshotHelperImpl::s_textDataFile);
    } else {
      RdnTrace(SCS_LEVEL(0), 
	       "Text file %s does not exists, switching to binary.",
	       fname);
      sprintf(fname, "%s/%s/%s", getSnapshotDir(), Scadasoft::GetServerName(),
	      SnapshotHelperImpl::s_binaryDataFile);
    }
    m_impl->m_currentInFile.open(fname, ios::in);
    if (!m_impl->m_currentInFile.good()) {
      // error
      RdnErrNoSnapshotFile(SCS_FATAL,  fname);
      m_impl->m_currentInFile.close();
      abort();
    }
    m_impl->m_binaryMode = !m_impl->m_binaryMode;
  }
  
  int lastErrCode = 0;
  // user define function
  if (m_impl->m_rdnServer)
    lastErrCode = m_impl->m_rdnServer->preLoad();

  if (lastErrCode == 0) {
    // general code
    m_impl->loadOlsList();

    // user define function
    if (m_impl->m_rdnServer)
      lastErrCode = m_impl->m_rdnServer->postLoad();
  }

  m_impl->m_currentInFile.close();
  delete [] fname;
  m_impl->m_binaryMode = oldMode;

  RdnTrace(K_TRACE_LEV_INTCALL, "SnapshotHelper::load <===    ");

  return lastErrCode;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::foregroundDump
//------------------------------------------------------------------------------+
int SnapshotHelper::foregroundDump() {
  RdnTrace(K_TRACE_LEV_INTCALL, "SnapshotHelper::foregroundDump ===>    ");
  m_impl->m_lastUFID = 1;

  if (getSnapshotDir() == NULL)
    return SnapshotHelper::SnapshotDirNotDefine;

  // create dir
  unsigned long len = strlen(getSnapshotDir()) + 1 
    + strlen(Scadasoft::GetServerName());
  char* dirname = new char[len+1];
  sprintf(dirname, "%s/%s", m_impl->m_snapshotDir, Scadasoft::GetServerName());


#ifndef WIN32
  int ret = mkdir(dirname, 
		  S_IRUSR | S_IRGRP | S_IROTH
		  | S_IWUSR | S_IWGRP | S_IWOTH
		  | S_IXUSR | S_IXGRP | S_IXOTH);
#else
  int ret = _mkdir(dirname);
#endif

  if (ret && errno != EEXIST) {
    RdnErrSnapshotDirCreate(SCS_FATAL, dirname, strerror(errno));
    delete [] dirname;
    return SnapshotHelper::CannotCreateSnapshotDir;
  }

  // open file
  len = strlen(dirname) + 1 
    + strlen(SnapshotHelperImpl::s_binaryDataFile);
  char* fname = new char[len+1];
  
  if (isBinary()) {
    sprintf(fname, "%s/%s", dirname,
	    SnapshotHelperImpl::s_binaryDataFile);   
  } else {
    sprintf(fname, "%s/%s", dirname,
	    SnapshotHelperImpl::s_textDataFile);    
  }

  m_impl->m_currentOutFile.clear();
  m_impl->m_currentOutFile.open(fname);

  if (!m_impl->m_currentOutFile.good()) {
    RdnTrace(SCS_LEVEL(0), "Cannot create file %s.", fname);
    m_impl->m_currentOutFile.close();
    delete [] fname;
    delete [] dirname;
   return SnapshotHelper::CannotCreateSnapshotFile; 
  }

  delete [] fname;
  delete [] dirname;

  RdnTrace(K_TRACE_LEV_INTCALL, "SnapshotHelper appel preForegroundDump");
  int lastErrCode = 0;
  // user define function
  if (m_impl->m_rdnServer)
    {
    RdnTrace(K_TRACE_LEV_INTCALL, "SnapshotHelper appel preForegroundDump 2");
    lastErrCode = m_impl->m_rdnServer->preForegroundDump();
    }

  RdnTrace(K_TRACE_LEV_INTCALL, "SnapshotHelper OK");
  if (lastErrCode == 0) {
    // general code
    m_impl->dumpOlsList();
    
    // user define function
    if (m_impl->m_rdnServer)
      lastErrCode = m_impl->m_rdnServer->postForegroundDump();
  }

  m_impl->m_currentOutFile.close();

  RdnTrace(K_TRACE_LEV_INTCALL, "SnapshotHelper::foregroundDump <===    ");

  return lastErrCode;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::storeLong
//------------------------------------------------------------------------------+
void SnapshotHelper::storeLong(long l) {
  if (m_impl->m_binaryMode) {
    m_impl->m_currentOutFile.write((const char*)&l, sizeof(long));
  } else {
    m_impl->m_currentOutFile << l << endl;
  }
}
//------------------------------------------------------------------------------+
// SnapshotHelper::storeULong
//------------------------------------------------------------------------------+
void SnapshotHelper::storeULong(unsigned long ul) {
  if (m_impl->m_binaryMode) {
    m_impl->m_currentOutFile.write((const char*)&ul, sizeof(unsigned long));
  } else {
    m_impl->m_currentOutFile << ul << endl;
  }
}
//------------------------------------------------------------------------------+
// SnapshotHelper::storeShort
//------------------------------------------------------------------------------+
void SnapshotHelper::storeShort(short s) {
  if (m_impl->m_binaryMode) {
    m_impl->m_currentOutFile.write((const char*)&s, sizeof(short));
  } else {
    m_impl->m_currentOutFile << s << endl;
  }
}
//------------------------------------------------------------------------------+
// SnapshotHelper::storeUShort
//------------------------------------------------------------------------------+
void SnapshotHelper::storeUShort(unsigned short us) {
  if (m_impl->m_binaryMode) {
    m_impl->m_currentOutFile.write((const char*)&us, sizeof(unsigned short));
  } else {
    m_impl->m_currentOutFile << us << endl;
  }
}
//------------------------------------------------------------------------------+
// SnapshotHelper::storeDouble
//------------------------------------------------------------------------------+
void SnapshotHelper::storeDouble(double l) {
  if (m_impl->m_binaryMode) {
    m_impl->m_currentOutFile.write((const char*)&l, sizeof(double));
  } else {
    m_impl->m_currentOutFile << l << endl;
  }
}
//------------------------------------------------------------------------------+
// SnapshotHelper::storeString
//------------------------------------------------------------------------------+
void SnapshotHelper::storeString(const char* st) {
  if (m_impl->m_binaryMode) {
    size_t l = strlen(st);
    m_impl->m_currentOutFile.write((const char*)&l, sizeof(size_t));
    if (l)
      m_impl->m_currentOutFile.write(st, l);
    // else empty string
  } else {
    m_impl->m_currentOutFile << strlen(st) << " " 
			     << st << endl;
  }
}
//------------------------------------------------------------------------------+
// SnapshotHelper::storeMemBlock
//------------------------------------------------------------------------------+
void SnapshotHelper::storeMemBlock(const char* adr, size_t size) {
  storeULong(size);
  
  if (m_impl->m_binaryMode) {
    m_impl->m_currentOutFile.write(adr, size);
  } else {
    char* fname = getNextUserSnapFile();
    storeString(fname);
    ofstream memfile(fname);
    memfile.write(adr, size);
    memfile.close();
    delete [] fname;
  }
}
//------------------------------------------------------------------------------+
// SnapshotHelper::readLong
//------------------------------------------------------------------------------+
long SnapshotHelper::readLong() {
  long l;
  if (m_impl->m_binaryMode) {
    m_impl->m_currentInFile.read((char*)&l, sizeof(long));
  } else {
    m_impl->m_currentInFile >> l;
  }
  return l;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::readDouble
//------------------------------------------------------------------------------+
double SnapshotHelper::readDouble() {
  double l;
  if (m_impl->m_binaryMode) {
    m_impl->m_currentInFile.read((char*)&l, sizeof(double));
  } else {
    m_impl->m_currentInFile >> l;
  }
  return l;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::readULong
//------------------------------------------------------------------------------+
unsigned long SnapshotHelper::readULong() {
  unsigned long ul;
  if (m_impl->m_binaryMode) {
    m_impl->m_currentInFile.read((char*)&ul, sizeof(unsigned long));
  } else {
    m_impl->m_currentInFile >> ul;
  }
  return ul;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::readShort
//------------------------------------------------------------------------------+
short SnapshotHelper::readShort() {
  short s;
  if (m_impl->m_binaryMode) {
    m_impl->m_currentInFile.read((char*)&s, sizeof(short));
  } else {
    m_impl->m_currentInFile >> s;
  }
  return s;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::readUShort
//------------------------------------------------------------------------------+
unsigned short SnapshotHelper::readUShort() {
  unsigned short us;
  if (m_impl->m_binaryMode) {
    m_impl->m_currentInFile.read((char*)&us, sizeof(unsigned short));
  } else {
    m_impl->m_currentInFile >> us;
  }
  return us;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::readString
//------------------------------------------------------------------------------+
char* SnapshotHelper::readString() {
  size_t len = 0;
  
  if (m_impl->m_binaryMode) {
    m_impl->m_currentInFile.read((char*)&len, sizeof(size_t));
  } else {
    m_impl->m_currentInFile >> len;
  }

  if (!m_impl->m_binaryMode) {
    // skip space
    char c;
    m_impl->m_currentInFile.get(c);
  }
  
  char* txt = new char[len+1];
  if (len)
    m_impl->m_currentInFile.read(txt,len);
  // else empty string
  
  txt[len] = '\0';
  return txt;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::readMemBlock
//------------------------------------------------------------------------------+
/* N'est pas utilise, au moins pour l'instant, et provoque une issue memory leak avec Klocwork
void SnapshotHelper::readMemBlock(char* adr, size_t size) {
 
  // check size
  size_t savedSize = readULong();
  if (savedSize != size) {
    RdnErrMemBlockSize(SCS_WARNING, size, savedSize);
    size = savedSize;
  }

  // read data
  if (m_impl->m_binaryMode) {
    m_impl->m_currentInFile.read(adr, size);
  } else {
    char* fname = readString();
    ifstream memfile(fname);
    memfile.read(adr, size);
    memfile.close();
  }
}
*/

//------------------------------------------------------------------------------+
// SnapshotHelper::addOlsList
//------------------------------------------------------------------------------+
void SnapshotHelper::addOlsList(OlsList* ol) {
  if (ol != NULL) {
    // check for multiple insertion
    vector<OlsList*>::iterator pos 
      = find(m_impl->m_olsLists.begin(), 
	     m_impl->m_olsLists.end(),
	     ol);
    if (pos == m_impl->m_olsLists.end()) {
      m_impl->m_olsLists.push_back(ol);
    }
  }
}
//------------------------------------------------------------------------------+
// SnapshotHelper::removeOlsList
//------------------------------------------------------------------------------+
bool SnapshotHelper::removeOlsList(OlsList* ol) {
  vector<OlsList*>::iterator pos 
    = find(m_impl->m_olsLists.begin(), 
	   m_impl->m_olsLists.end(),
	   ol);
  if (pos != m_impl->m_olsLists.end()) {
    m_impl->m_olsLists.erase(pos);
    return true;
  }

  return false;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::removeAllOlsList
//------------------------------------------------------------------------------+
void SnapshotHelper::removeAllOlsList() {
  m_impl->m_olsLists.clear();
}
//------------------------------------------------------------------------------+
// SnapshotHelper::storeTimer
//------------------------------------------------------------------------------+
void SnapshotHelper::storeTimer(const ScsTimer* scst, int reSync) {
  if (scst != NULL) {
    int wsync = ((ScsTimer*)scst)->getWakeUpSync();
    if (m_impl->m_binaryMode) {
      timeval wakeUp = scst->getWakeUpTime();
      m_impl->m_currentOutFile.write((const char*)&wakeUp, sizeof(timeval));
      unsigned long period = scst->getPeriod();
      m_impl->m_currentOutFile.write((const char*)&period, sizeof(unsigned long));
      m_impl->m_currentOutFile.write((const char*)&reSync, sizeof(int));
      m_impl->m_currentOutFile.write((const char*)&wsync, sizeof(int));
    } else {
      m_impl->m_currentOutFile << scst->getWakeUpTime().tv_sec << " "
			       << scst->getWakeUpTime().tv_usec << " "
			       << scst->getPeriod() << " "
			       << reSync << " "
			       << wsync << endl;
    }
  }
}
//------------------------------------------------------------------------------+
// SnapshotHelper::readTimer
//------------------------------------------------------------------------------+
ScsTimer* SnapshotHelper::readTimer(TimerCallbackFunction fct, void* arg) {
  timeval wakeUp;
  unsigned long period = 0;
  int resync = 0;
  int wakesync = 0;

  if (m_impl->m_binaryMode) {
    m_impl->m_currentInFile.read((char*)&wakeUp, sizeof(timeval));
    m_impl->m_currentInFile.read((char*)&period, sizeof(unsigned long));
    m_impl->m_currentInFile.read((char*)&resync, sizeof(int));
    m_impl->m_currentInFile.read((char*)&wakesync, sizeof(int));
  } else {
    m_impl->m_currentInFile >> wakeUp.tv_sec 
			    >> wakeUp.tv_usec
			    >> period 
			    >> resync
			    >> wakesync;
  }

  ScsTimer* t = new ScsTimer(wakeUp, period, fct, arg, resync, wakesync);
  return t;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::readTimer
//------------------------------------------------------------------------------+
ScsTimer* SnapshotHelper::readTimer(unsigned int delta,
				    TimerCallbackFunction fct, void* arg)
{
  timeval wakeUp;
  unsigned long period = 0;
  int resync = 0;
  int wakesync = 0;

  if (m_impl->m_binaryMode) {
    m_impl->m_currentInFile.read((char*)&wakeUp, sizeof(timeval));
    m_impl->m_currentInFile.read((char*)&period, sizeof(unsigned long));
    m_impl->m_currentInFile.read((char*)&resync, sizeof(int));
    m_impl->m_currentInFile.read((char*)&wakesync, sizeof(int));
  } else {
    m_impl->m_currentInFile >> wakeUp.tv_sec 
			    >> wakeUp.tv_usec
			    >> period 
			    >> resync
			    >> wakesync;
  }

  ScsTimer* t = new ScsTimer(wakeUp, period, delta, fct, arg);
  return t;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::copyFile
//------------------------------------------------------------------------------+
void SnapshotHelper::copyFile(const char* sourcefilename) {
  char* fname = getNextUserSnapFile();
  // store file
  m_impl->copyFile(sourcefilename, fname);
  storeString(sourcefilename);
  delete [] fname;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::installSnapFile
//------------------------------------------------------------------------------+
void SnapshotHelper::installSnapFile() {
  char* tmpfile = getNextUserSnapFile();
  char* instName = readString();
  
  m_impl->copyFile(tmpfile, instName);

  delete [] tmpfile;
  delete [] instName;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::copyDir
//------------------------------------------------------------------------------+
void SnapshotHelper::copyDir(const char* sourcedirname) {

  // check for exception
  if (sourcedirname == NULL) {
    storeShort(1); // not a string
    return;
  }

  int nbf = 0;
#ifndef WIN32
  // check if dir is empty
  DIR *dirp = opendir(sourcedirname);
  if (dirp == NULL) {
    storeShort(2); // not a directory
    return;
  }

  struct dirent *dp;
  while ((dp = readdir(dirp)) != NULL) {
    if (dp->d_name[0] != '.')
      nbf++;
  }
  closedir(dirp);
#else
  HANDLE hList;
  WIN32_FIND_DATA FileData;
  char *sdirp = new char[ strlen(sourcedirname) + 16 ];
  sprintf( sdirp, "%s\\*", sourcedirname );
  win32path( sdirp );
  hList = FindFirstFile( sdirp, &FileData );
  if ( hList != INVALID_HANDLE_VALUE ) {
    BOOL fFinished = FALSE;
    while (!fFinished) {
      // Check the object is a directory or not
      if (FileData.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) {
	if ((strcmp(FileData.cFileName, ".") != 0) && (strcmp(FileData.cFileName, "..") != 0))
	  nbf++;
      }
      else  nbf++;

      if (!FindNextFile(hList, &FileData)) {
	if (GetLastError() == ERROR_NO_MORE_FILES){
	  fFinished = TRUE;
	}
      }
    }
  }
  delete [] sdirp;
#endif

  if (nbf == 0) {
    storeShort(3); // empty directory
    return;
  }

  // dump directory
  storeShort(0);
  char* tarfile = getNextUserSnapFile();
  
  // backup directory
  unsigned long len = 32 + strlen(sourcedirname) + 1 + strlen(tarfile) + 3;
  char* cmd = new char[len+1];
#ifndef WIN32
  sprintf(cmd, "tar cfP %s %s/*", tarfile, sourcedirname);
#else
  sprintf(cmd, "7z a -bd -ttar %s %s\\*", tarfile, sourcedirname );
  win32path( cmd );
#endif
  system(cmd);
  delete [] cmd;
  
  storeString(sourcedirname);
  delete [] tarfile;

}
//------------------------------------------------------------------------------+
// SnapshotHelper::copyDir
//------------------------------------------------------------------------------+
void SnapshotHelper::installSnapDir() {
  
  // check type
  short s = readShort();

  if (s == 0) {
    char* tarfile = getNextUserSnapFile();
    char* dirname = readString();  
    // restore directory
    unsigned long len = 32 + strlen(dirname) + 1 + strlen(tarfile) + 3;
    char* cmd = new char[len+1];
#ifndef WIN32
    sprintf(cmd, "tar xfP %s", tarfile);
#else
    sprintf(cmd, "7z x -y -o%s %s.tar", dirname, tarfile );
    win32path(cmd);
#endif
    system(cmd);
  
    delete [] cmd;
    delete [] tarfile;
    delete [] dirname;
  }
}
//------------------------------------------------------------------------------+
// SnapshotHelper::getNextUserSnapFile
//------------------------------------------------------------------------------+
char* SnapshotHelper::getNextUserSnapFile() {
  // search file name
  unsigned long len = strlen(m_impl->m_snapshotDir) + 1
    + strlen(Scadasoft::GetServerName()) + 1
    + strlen(SnapshotHelperImpl::s_extfileFilename);
  char* fname = new char[len + 10];
  // sprintf_s is used instead of sprintf because more secure (Klocwork issue)
  // Platform and version dependant :_snprintf_s for visual 2010. TODO : ifdef / else on platform / versions when needed
  sprintf_s(fname, len + 10, "%s/%s/%s%04lu", m_impl->m_snapshotDir, Scadasoft::GetServerName(),
	  SnapshotHelperImpl::s_extfileFilename, m_impl->m_lastUFID);
  m_impl->m_lastUFID++;
  
  return fname;
}

//------------------------------------------------------------------------------+
// SnapshotHelper::getSnapshotOutFile
//------------------------------------------------------------------------------+
ofstream &SnapshotHelper::getSnapshotOutFile()
{
  return m_impl->m_currentOutFile;
}

//------------------------------------------------------------------------------+
// SnapshotHelper::getSnapshotInFile
//------------------------------------------------------------------------------+
ifstream &SnapshotHelper::getSnapshotInFile()
{
  return m_impl->m_currentInFile;
}

//==============================================================================+
// SnapshotHelper INTERNAL CLASS IMPLEMENTATION
//==============================================================================+
//------------------------------------------------------------------------------+
// SnapshotHelperImpl::loadOlsList
//------------------------------------------------------------------------------+
void SnapshotHelper::SnapshotHelperImpl::loadOlsList() {
  unsigned long len = strlen(m_snapshotDir) + 1
    + strlen(Scadasoft::GetServerName()) + 1;
  char* fname = new char[len];
  sprintf(fname, "%s/%s", m_snapshotDir, Scadasoft::GetServerName());

  int i;
  for(i = 0; i < m_olsLists.size(); ++i) {
    m_olsLists[i]->restoreFromSnapshot(fname);
  }

  if (m_olsLists.size() > 0) {
    try {

      OlsInitFromSnapshot(fname);

    } catch (CORBA::SystemException & sysEx) {
#if SCS_INTEGER_VERSION < 5000000L
      RdnErrOlsInitError(SCS_WARNING, ScsGetOrbExceptionText(sysEx));
#else
      RdnErrOlsInitError(SCS_WARNING, (ScsGetOrbExceptionText(sysEx)).c_str());
#endif
    } catch (exception& ex) {
      RdnErrOlsInitError(SCS_WARNING, ex.what());
    } catch (...) {
      RdnErrOlsInitError(SCS_WARNING, "Unknown Exception");
    }
 }

  delete [] fname;
}
//------------------------------------------------------------------------------+
// SnapshotHelperImpl::dumpOlsList
//------------------------------------------------------------------------------+
void SnapshotHelper::SnapshotHelperImpl::dumpOlsList() {
  unsigned long len = strlen(m_snapshotDir) + 1
    + strlen(Scadasoft::GetServerName()) + 1;
  char* fname = new char[len];
  sprintf(fname, "%s/%s", m_snapshotDir, Scadasoft::GetServerName());

  int i;
  for(i = 0; i < m_olsLists.size(); ++i) {
    m_olsLists[i]->foregroundSnapshot(fname);
  }
  if (m_olsLists.size() > 0) {
    OlsSnapshot(fname);
  }

  delete [] fname;
}
//------------------------------------------------------------------------------+
// SnapshotHelperImpl::copyFile
//------------------------------------------------------------------------------+
const void SnapshotHelper::SnapshotHelperImpl::copyFile(const char* src, const char* dst)
{
  unsigned long len = 3 + strlen(src) + 1 + strlen(dst);
  char* cmd = new char[len+1];
  sprintf(cmd, "cp %s %s", src, dst);
  system(cmd);
  delete [] cmd;
}
//------------------------------------------------------------------------------+
// SnapshotHelperImpl::foregroundSnap
//------------------------------------------------------------------------------+
ScsStatus SnapshotHelper::SnapshotHelperImpl::foregroundSnap(const char* snapdir,
							     void* arg)
{
  SnapshotHelperImpl* impl = (SnapshotHelperImpl*) arg;
  impl->setSnapshotDir(snapdir);
  SnapshotHelper::instance()->foregroundDump();

  return ScsValid;
} 
//------------------------------------------------------------------------------+
// SnapshotHelperImpl::installSignal
//------------------------------------------------------------------------------+
const void SnapshotHelper::SnapshotHelperImpl::installSignal() {
#ifndef WIN32
  struct sigaction susr;
  susr.sa_handler = SnapshotHelper::SnapshotHelperImpl::sigusr;
  sigfillset(&susr.sa_mask);
  susr.sa_flags = SA_RESTART;

  if (sigaction(SIGUSR1, &susr, NULL) == -1) {
    RdnTrace(SCS_LEVEL(0), "Cannot install signal SIGUSR1.");
  }
  if (sigaction(SIGUSR2, &susr, NULL) == -1) {
    RdnTrace(SCS_LEVEL(0), "Cannot install signal SIGUSR2.");
  }

#else
  RdnTrace(SCS_LEVEL(0),"installSignal not implemented WIN32");
#endif
}
//------------------------------------------------------------------------------+
// SnapshotHelperImpl::sigusr1
//------------------------------------------------------------------------------+
void SnapshotHelper::SnapshotHelperImpl::sigusr(int sig) {
  RdnTrace(K_TRACE_LEV_INTCALL, "SnapshotHelperImpl::sigusr ===>    ");
  // save status
  char* olddir = NULL;
  if (SnapshotHelper::instance()->getSnapshotDir())
    olddir = strdup(SnapshotHelper::instance()->getSnapshotDir());
  bool oldbin = SnapshotHelper::instance()->isBinary();

#ifndef WIN32
  // do snapshot
  if (sig == SIGUSR1) {
    SnapshotHelper::instance()->setBinaryMode(true);
  } else if (sig == SIGUSR2) {
    SnapshotHelper::instance()->setBinaryMode(false);
  }
#endif
  SnapshotHelper::instance()->setSnapshotDir(".");
  SnapshotHelper::instance()->foregroundDump();

  // restore status
  SnapshotHelper::instance()->setBinaryMode(oldbin);
  SnapshotHelper::instance()->setSnapshotDir(olddir);
  if (olddir)
    free(olddir);

  RdnTrace(K_TRACE_LEV_INTCALL, "SnapshotHelperImpl::sigusr <===    ");
}
//------------------------------------------------------------------------------+
// SnapshotHelperImpl::stateChangeCB
//------------------------------------------------------------------------------+
void SnapshotHelper::SnapshotHelperImpl::stateChangeCB(const char *processName,
						       const char *state,
						       void* arg)
{
  SnapshotHelperImpl* impl = (SnapshotHelperImpl*) arg;
  
  RdnTrace(SCS_LEVEL(0), 
	   "SnapshotHelperImpl::stateChangeCB: state of server %s is %s.", 
	   processName, state);

  bool standby = false;
  
  if (strcmp(state, SCS_STANDBY) == 0
      || strcmp(state, SCS_STANDBYSYNC) == 0
      || strcmp(state, SCS_STANDBYISOLATED) == 0
      || strcmp(state, SCS_STANDBYINIT) == 0
      || strcmp(state, SCS_DOWN) == 0
      )
    {
      standby = true;
    }

  if (strcmp(state, SCS_STANDBYINIT) == 0)
    impl->m_standbyInit = true;
  else
    impl->m_standbyInit = false;

  // if we switch from STANDBY to ONLINE
  if (!standby && impl->m_standby && impl->m_rdnServer != NULL)
  {
    // set new state
    impl->m_standby = false;
    // call the changeToOnline method
    impl->m_rdnServer->changeToOnline();
  }
  
  // set new state
  impl->m_standby = standby;
}
//------------------------------------------------------------------------------+
// SnapshotHelper::setSnapshotDir
//------------------------------------------------------------------------------+
void SnapshotHelper::SnapshotHelperImpl::setSnapshotDir(const char* dirname) {
  if (m_snapshotDir != NULL) {
    delete [] m_snapshotDir;
    m_snapshotDir = NULL;
  }
  if (dirname != NULL) {
    int ln = strlen(dirname);
    m_snapshotDir = new char[ln+1];
    strcpy_s(m_snapshotDir, ln+1, dirname);
  }
}
