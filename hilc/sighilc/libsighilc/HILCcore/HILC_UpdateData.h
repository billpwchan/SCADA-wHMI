/******************************************************************************/
/* Thales SFI                                                                 */
/*CSCI          : HILC                                                        */
/******************************************************************************/
#ifndef __HILC_UPDATE_DATA_H__
#define __HILC_UPDATE_DATA_H__

#include <map>
#include <set>

#include "dac.h" //DacServer
#include "dbm.h"
#include "olstypes.h" //DacServer
#include "scs.h"
#include "ctlcmd.h"
#include "asc.h"
#include "HILC_ConfManager.h"
#include "scsalmext.h"
#include "HILC_ManipulationDonnees.h"
#include <vector>
#include <string>
#include <list>

using namespace std ;

class SessionManager;
class Session;


class HILC_UpdateData : public HILC_ManipulationDonnees
{
public:
    ~HILC_UpdateData ();
    //getInstance du Singleton
    static HILC_UpdateData* getInstance();
    //initialisation
    const ScsStatus initSCS();
    ScsStatus init(DbmServer * ptrDbmServer, DbmPoller *  ptrDbmPoller, CtlCmdServer * ptrCtlCmdServerWithoutCB, ScsAlmExtern * ptrScsAlmExtern);

    ScsStatus initFullEquipmentAddressList();
    ScsStatus initEquipmentTypeList();
    ScsStatus setEquipment(const char * hilc_equipment);
    ScsStatus setEquipmentAddress(const char * equipmentType);

    //abonnement
    ScsStatus subscribeEquipment(string str_alias);
    ScsStatus subscribeEquipment(Session*  session);
    ScsStatus unSubscribeEquipment(string str_alias);
    ScsStatus unSubscribeEquipment(Session* sessionToDelete);

    int findEquipmentNumber(string str_alias);

    //Callbacks
    static void subscribeEquipmentCallBack(const DbmDataSet &dataSet, ScsAny arg);
    static void timerCallback(ScsAny arg);
    static void finalTimerCallback(ScsAny arg);

    //verification des attributs
    ScsStatus verifEquipementAttributIntvalue(string eq, string att, int val);
    //ScsStatus verifEquipementAttributStringvalue(string eq, string att, string val);
	ScsStatus checkExecStatus(string commandName);

    //envoi de commande
    ScsStatus sendCommand(string commandName, unsigned char* commandMessage, unsigned int uiMessageSize, string src);
    ScsStatus sendCommand(string commandName, int commandValue, string src);
    ScsStatus sendCommand(string commandName, float commandValue, string src);
    ScsStatus sendAlarmExt(const char *i_pointAlias);

    DbmPoller  * _PtrDbmPoller;
    CtlCmdServer * _PtrCtlCmdServerWithoutCB;
    ScsAlmExtern * _PtrScsAlmExtern;

private:
    /**
    * Default constructor are private
    */
    HILC_UpdateData ();
    HILC_UpdateData (DbmPoller *aPtrDbmPoller , DbmServer *aPtrDbmServer, CtlCmdServer *aPtrCtlCmdServerWithoutCB, ScsAlmExtern *aPtrScsAlmExtern);
    static HILC_UpdateData* m_instance;
    //Equipment Address vector
    std::vector<ScsAddress> _EquipmentAddress;
    //Equipment Types list
    // BHE std::list<const char *> m_equipmentTypeList;
	std::list<std::string> m_equipmentTypeList;
	std::list<std::string> m_scsTypeList;
	std::list<std::string> m_rootsList;
    static SessionManager* m_SessionManager;

	// Execution status for an IOV
	typedef enum
	{
	  CTL_K_EXE_STAT_INACTIVE = 0,	// Command initialized
	  CTL_K_EXE_STAT_CONDINI,	// Command waiting for init conditions
	  CTL_K_EXE_STAT_CONDRET,	// Command waiting for return conditions
	  CTL_K_EXE_STAT_END,		// Command completed (initial value)
	  CTL_K_EXE_STAT_INTRCONDINI,	// Error on init conditions
	  CTL_K_EXE_STAT_INTRTIMEOUT	// Timeout on init or return conditions
	} CtlExecStatus;

};

#endif // __HILC_UPDATE_DATA_H__
