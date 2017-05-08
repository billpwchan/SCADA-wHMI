/******************************************************************************/
/* Thales SFI                                                                 */
/*CSCI          : HILC                                                        */
/******************************************************************************/

#ifndef __SESSIONMANAGER_H__
#define __SESSIONMANAGER_H__


#include "HILC_UpdateData.h"
#include "Session.h"
#include "dbm.h"
#include "scs.h"
#include "asc.h"
#include <list>

#include <iostream>
using namespace std;

//Definition de l'enum associe au status d'erreur mineur
enum ENUM_HILC_MINOR_STATUS
{
    HILC_INTERNAL_ERROR = 0,
    HILC_ARG_ERROR = 1,
#ifdef USE_SESSION_EQPT					
    HILC_EXISTING_SESSION_ERROR = 2,
#endif
    HILC_CREATE_COND_ERROR = 3,
    HILC_PREPNOTFOUND_ERROR = 4,
	HILC_MAX_SESSION_ERROR = 5,
	HILC_INIT_COND_ERROR = 6
};


class SessionManager
{
private:
//instance du singleton
    static SessionManager* m_instance;
    std::list<Session*> m_sessionList;
    HILC_UpdateData * m_updateData;
    /**
    * Default constructor are private and not implemented.
    */
    SessionManager();

public:
//getInstance du Singleton
    static SessionManager* getInstance();
    ScsStatus init();
    //commande de preparation
    ScsStatus prepareCmd(string op, string ws, unsigned short ct, short cv, unsigned short cvd, string eq, string et, string cn);
    //commande de confirmation
    ScsStatus confirmCmd(string op, string ws, unsigned short ct, short cv, unsigned short cvd, string eq, string et, string cn);
    //commande de cancel
    // ScsStatus cancelCmd(string op, string ws, unsigned short int ct, unsigned short int cv, string eq, string et, string cn);

    ~SessionManager();
    //methodes de verification de conditions
    ScsStatus verifCreatePrepCond(string op, string ws, string eq, string et, string cn);
    ScsStatus verifEquipementAttributIntvalue(string eq, string att, int val);
    //ScsStatus verifEquipementAttributStringvalue(string eq, string att, string val);

    ScsStatus createSession(string op, string ws,unsigned short ct, short cv, unsigned short cvd, string eq, string et, string cn);
    ScsStatus deleteSession(Session * sessionToDelete);
    ScsStatus notifySessionOfEqtChangement(string eqtAlias);

    //Method de recherche identification
    int findPrepSession(string op, string ws,unsigned short ct, short cv, unsigned short cvd, string eq, string et, string cn);
    int findSessionForEqt(string eqtAlias);

};

#endif //__SESSIONMANAGER_H__
