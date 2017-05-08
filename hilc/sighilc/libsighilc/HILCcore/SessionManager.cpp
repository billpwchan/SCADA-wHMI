#include "SessionManager.h"
#include "HILC_UpdateData.h"
#include "Traces.h"
#include "SigTraces.h"
//Not used yet #include "SigError.h"
#include "HILCdefs.h"
#include <iostream>
#include <sstream>
#include <stddef.h>
#include <ecomlib/confmanager.h>

using namespace std;

// initialisation du pointeur de singleton
SessionManager *SessionManager::m_instance = NULL;

SessionManager::SessionManager()
{
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ),"   SessionManager-ctor " );
    m_updateData = NULL;

}


SessionManager::~SessionManager()
{

    //supprimer tous les elements de la liste de pointeur
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ),"   SessionManager-dtor " );
    for(std::list<Session*>::iterator it = m_sessionList.begin();  it != m_sessionList.end() ; ++it)
    {
        delete *it;
    }
    m_sessionList.clear();
}


SessionManager* SessionManager::getInstance()
{
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   SessionManager-getInstance " );

    if (m_instance == NULL)
    {
        m_instance = new SessionManager();
    }

    return m_instance;
}

ScsStatus SessionManager::init()
{
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   SessionManager-Init: ");
    ScsStatus status = ScsError;
    m_updateData=HILC_UpdateData::getInstance();
    if (m_updateData == NULL)
    {
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "   SessionManager-Init: Failed");
    }
    else
    {
        status = ScsValid;
    }
    return status;
}


ScsStatus SessionManager::prepareCmd(string op, string ws, unsigned short ct, short cv, unsigned short cvd, string eq, string et, string cn)
{
    ScsStatus status = ScsError;
    try
    {
		stringstream sBuffer;
        sBuffer 	<< "Session Manager prepareCmd: "<< endl << " operatorName=" << op<< endl << " workStationName=" <<  ws<< endl<< " cmdType=" << ct<< endl << " cmdValue=" << cv << endl << " cmdValueDiv=" << cvd << endl << " equipmentAlias=" << eq<< endl
        << " equipmentType=" << et<< endl << " commandName=" << cn<< endl ;
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "prepareCmd: %s",sBuffer.str().c_str());

		int maxSessions = HILC_ConfManager::getInstance()->HILC_MAX_SESSIONS;
		if  ( m_sessionList.size() < maxSessions )
		{
			if ( (eq.size() > 0) && (et.size() > 0) && (cn.size() > 0) && (op.size() > 0) && (ws.size() >0) )
			{
	#ifdef USE_SESSION_EQPT				
				SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::prepareCmd activateInitSessionCond ON");

				string att = HILC_ConfManager::getInstance()->HILC_DCI_NODE_NAME + et + HILC_ConfManager::getInstance()->HILC_SESSIONSTATE + cn + HILC_ConfManager::getInstance()->HILC_ATTR_VALUE;

				// Verification de l'absence de session en cours sur cet equipement
				int condVal = HILC_ConfManager::getInstance()->HILC_INITSESSION_COND_VALUE;

				status = m_updateData->verifEquipementAttributIntvalue(eq,att,condVal);
				if (status.isValid())
				{
					SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::prepareCmd Check if no existing Session: OK");
				}
				else
				{
					SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::prepareCmd Check if no existing Session: KO");
					status.setError(HILC_EXISTING_SESSION_ERROR);
				}
				SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::prepareCmd activateInitSessionCond OFF");
				status = ScsValid;
				if (status.isValid())
				{
	#endif
					//Verification des conditions de creation de session
					status = this->verifCreatePrepCond(op, ws, eq, et, cn);
					if (status.isValid())
					{
						SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::prepareCmd CreateSession and subscribeEquipement");

						status = this->createSession(op, ws, ct, cv, cvd, eq, et, cn);
						if (status.isValid())
						{
							SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::prepareCmd: Cmd creation OK");
						}
						else
						{
							SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::prepareCmd: createSession KO");
						}
					}
					else
					{
						SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::prepareCmd: verifCreateCond KO");
						// set status.minor to precise type of error
						status.setError(HILC_CREATE_COND_ERROR);
					}
	#ifdef USE_SESSION_EQPT									
				}
				else
				{
					SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::prepareCmd: verifEqSession KO");
				}
	#endif			
			}
			else
			{
				SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::prepareCmd Error: Wrong Arg");
				// set status.minor to precise type of error
				status.setError(HILC_ARG_ERROR);
			}
		}
		else
		{
			SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::prepareCmd Check if max Session number: KO");
			status.setError(HILC_MAX_SESSION_ERROR);			
		}
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::prepareCmd: Minor Status %d", status.getMinor() );
    }
    catch(exception const& e) //On rattrape les exceptions standard de tous types
    {
        status = ScsError;
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::prepareCmd Error :",e.what() );
    }
    return status;
}



ScsStatus SessionManager::confirmCmd(string op, string ws, unsigned short ct, short cv, unsigned short cvd, string eq, string et, string cn)
{
    ScsStatus status = ScsError;
    try
    {
		stringstream sBuffer;
        sBuffer 	<< "Session Manager confirmCmd: "<< endl << " operatorName=" << op<< endl << " workStationName=" <<  ws<< endl
        << " cmdType=" << ct<< endl << " cmdValue=" << cv<< endl << " cmdValueDiv=" << cvd << endl << " equipmentAlias=" << eq<< endl
        << " equipmentType=" << et<< endl << " commandName=" << cn<< endl ;

        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "confirmCmd: %s",sBuffer.str().c_str());

        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "########SessionManager::confirmCmd  Start ########");

        // arg definits
        if ( (eq.size() > 0) && (et.size() > 0) && (cn.size() > 0) && (op.size() > 0) && (ws.size() >0) )
        {
            int indiceCmdPrep = this->findPrepSession( op,  ws, ct, cv, cvd, eq,  et,  cn);

            if (indiceCmdPrep != -1 && (m_sessionList.size() >indiceCmdPrep) )
            {
                std::list<Session*>::iterator it = m_sessionList.begin();
                std::advance(it, indiceCmdPrep);
                // 'it' points to the Preparation Command

                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::confirmCmd  Preparation Command found ");
                (*it)->print();
                status= (*it)->getCurrentState()->actionOnPreparationCommandFound( (Session*)(*it));

                if (status.isValid())
                {
                    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::confirmCmd: actionOnPreparationCommandFound OK");
                }
                else
                {
                    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::confirmCmd: actionOnPreparationCommandFound KO");
                }
            }
            else
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::confirmCmd  Error :Preparation Command not found ");
                // set status.minor to precise type of error
                status.setError(HILC_PREPNOTFOUND_ERROR);

                // send Alarm
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::confirmCmd Send Alarm");

                HILC_UpdateData * m_updateData = HILC_UpdateData::getInstance();
                m_updateData->sendAlarmExt(eq.c_str());
            }
        }
        else
        {
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::confirmCmd Error: Wrong Arg");
            // set status.minor to precise type of error
            status.setError(HILC_ARG_ERROR);
        }
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::confirmCmd: Minor Status %d", status.getMinor() );
    }
    catch(exception const& e) //On rattrape les exceptions standard de tous types
    {
        status = ScsError;
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::confirmCmd Error :",e.what() );
    }
    return status;
}

ScsStatus SessionManager::createSession(string op, string ws, unsigned short ct, short cv, unsigned short cvd, string eq, string et, string cn)
{
    ScsStatus status = ScsError;
    bool isErrorFound = false;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SessionManager::createSession start for eqt: %s, op: %s",eq.c_str(),op.c_str());

    if ( (eq.size() > 0) && (op.size() >0) && (ws.size() >0) )
    {
        // Find the equipment number in _EquipmentAddress list
        string equip = HILC_ConfManager::getInstance()->HILC_TAG_ALIAS;
        equip += eq;

        int eqtNumber = m_updateData->findEquipmentNumber(equip);

        if (eqtNumber != -1)
        {
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SessionManager::createSession  eqtNumber: %d", eqtNumber);

            //Define the futur groupname use for dataset subscription
            string groupName= "eqtDataSet_";
            std::stringstream sstm;
            sstm << groupName << eqtNumber;

            groupName =sstm.str();
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SessionManager::createSession  groupname %s", groupName.c_str() );

            //Create Session and set attribut
            Session * newSession = new Session(op,  ws, ct, cv, cvd, eq, et, cn, groupName,eqtNumber  );

            //Init Session
            if (newSession)
            {
                newSession->init();

                //subscribe attributes
                status = m_updateData->subscribeEquipment(newSession);
            }

            if (status.isValid() && newSession)
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::createSession and subscribe  OK ");
                // Do Init action of the initial state
                status = newSession->actionOnInit();

                if (status.isValid())
                {
                    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::actionOnInit OK ");
					int listSize=m_sessionList.size();
                    m_sessionList.push_back(newSession);
                    if(m_sessionList.size()==listSize+1)
                    {
                        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::createSession  OK ");

                    }
                    else
                    {
                        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::createSession  Failed");
                        isErrorFound = true;
                    }
                }
                else
                {
                    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::actionOnInit KO");
                    isErrorFound = true;
                }
            }
            else
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::subscribeEquipement KO");
                isErrorFound = true;
            }
            if (isErrorFound && newSession)
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager Error found: delete newSession");
                delete newSession;
            }

        }
        else
        {
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::createSession  findEquipmentNumber  Error ");
        }
    }

    return status;
}


ScsStatus SessionManager::deleteSession(Session * sessionToDelete)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SessionManager::deleteSession start for eqt: %s, op: %s",sessionToDelete->getEqA().c_str(),sessionToDelete->getOpN().c_str());

    string sessionEquipAlias = sessionToDelete->getEqA();
    string sessionGroupName = sessionToDelete->getGpN();


    // equipement  definits
    if (sessionEquipAlias.size() > 0)
    {
        int indice =0;
        for(std::list<Session*>::iterator it = m_sessionList.begin();  it != m_sessionList.end() ; ++it)
        {
            //Comparaison de l'alias d'equipement
            if (   ( sessionEquipAlias.compare((*it)->getEqA()) ==0 )   )
            {
                //CancelTimer
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::CancelTimer ");
                ScsTimer * timerHILC = sessionToDelete->getTimerHILC();
                if (timerHILC)
                {
                    ScsTimer::cancelTimer(timerHILC);
                }

                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SessionManager::unSubscribeEquipement sessionGroupName: %s",sessionGroupName.c_str());

                // Delete subscription group

                status = m_updateData->unSubscribeEquipment(sessionToDelete);
                if (status.isValid())
                {
                    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::unSubscribeEquipement OK");
                    int listSize=m_sessionList.size();
                    //suppression de la Session

                    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SessionManager::deleteSession indice : %d", indice);
                    m_sessionList.erase(it);

                    if(m_sessionList.size()==listSize-1)
                    {
                        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::deleteSession OK");
                        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::m_sessionList.size: %d",m_sessionList.size());
                    }
                    else
                    {
                        status = ScsError;
                        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::deleteSession:erase Error");
                    }
                }
                else
                {
                    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::unSubscribeEquipement %s KO", sessionGroupName.c_str());
                }

                break;// Car une Sesssion par Equipement
            }
            indice ++;
        }
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::deleteSession Error: Incorrect Session ");
    }
    return status;
}


int SessionManager::findPrepSession(string op, string ws, unsigned short ct, short cv, unsigned short cvd, string eq, string et, string cn)
{
    int result = -1;
    string commandPrep= HILC_ConfManager::getInstance()->HILC_WAITING_STEP_NAME;

    // equipment et operateur definits
    if ((eq.size() > 0) && ( op.size() >0))
    {
        int indice =0;
        for(std::list<Session*>::iterator it = m_sessionList.begin();  it != m_sessionList.end() ; ++it)
        {
            //Si il existe d'autres Session  sur cet equipement pour cet Operateur
            if (   ( eq.compare((*it)->getEqA()) ==0 ) && ( op.compare((*it)->getOpN()) ==0 )  )
            {
                //dans l'etat preparation terminee
                string currentState = (*it)->getCurrentState()->info();
                if (  commandPrep.compare(currentState) ==0 )
                {
                    //Verifier les autres parametres pour identifier si une Session existe avec la commande de Preparation associee
                    if ( (ws.compare((*it)->getWS()) ==0 ) && (et.compare((*it)->getEqT()) ==0 )  && (cn.compare((*it)->getCmdN()) ==0 )  && ( ct==(*it)->getCmdT() ) && (cv==(*it)->getCmdV()) && (cvd==(*it)->getCmdValueDiv()) )
                    {
                        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::FindPrepSession Notif:  Preparation Command found");
                        result = indice;
                    }
                    //la commande de Confirmation ne correspond à aucune commande de Preparation dans les Sessions existantes
                    else
                    {
                        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::FindPrepSession Notif: Existing Session on this equipment with same operator but with different parameters");
                    }
                }
                else
                {
                    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::FindPrepSession Notif: Existing Session on this equipment with same operator  but with  Incorrect Command Step  : %s",currentState.c_str());
                }
            }
            indice ++;
        }
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::FindPrepSession Error:Confirmation Command incorrect");
    }

    return result;
}

ScsStatus SessionManager::verifCreatePrepCond(string op, string ws, string eq, string et, string cn)
{
    ScsStatus status = ScsError;
    bool findErrorCond = false;

#ifdef USE_FALLBACK	
	SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::verifCreateCond verifPrepCondType Cond2");

	if ( (eq.size() > 0) && (et.size() > 0) && (cn.size() > 0) && (op.size() > 0) && (ws.size() >0) )
	{
		SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::verifCreateCond findSessionForEqt Start");
		int sessionIndice= this->findSessionForEqt(eq);
		if (sessionIndice != -1 )
		{
			findErrorCond = true;
			SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::verifCreateCond Prep Error: Existing Session on this equipment");
		}
		else
		{
			string fallBackModeCmdName = HILC_ConfManager::getInstance()->HILC_FALLBACK_CMDNAME;//Fallback

			// La commande d'activation des commandes HILC est permise
			if ( cn.compare(fallBackModeCmdName) == 0)
			{
				SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::verifCreateCond FallBack Mode Command");
			}
			else // sinon, on doit tester si l'on peut envoyer des commandes
				//Identifier si la commande a deja ete emise en verifiant  la valeur de la dci PMI_2000-ModeFallBack
			{
				//Verification de l activation des commandes HILC
				SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::verifCreateCond Check FallBack Mode");
				//CBI_1
				string FallBackEqpt =  HILC_ConfManager::getInstance()->HILC_FALLBACK_EQPT;
				//:dciPMI_2000-ModeFallBack.value
				string attFallBackMode = HILC_ConfManager::getInstance()->HILC_DCI_NODE_NAME + HILC_ConfManager::getInstance()->HILC_FALLBACK_CLASS + HILC_ConfManager::getInstance()->HILC_FALLBACK_DCINAME + HILC_ConfManager::getInstance()->HILC_ATTR_VALUE;

				int condVal = HILC_ConfManager::getInstance()->HILC_FALLBACK_COND_VALUE;

				status = m_updateData->verifEquipementAttributIntvalue(FallBackEqpt,attFallBackMode,condVal);
				if(status.isValid())
				{
					//Commandes HILC activees
					SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::verifCreateCond HILC Command are enabled");
				}
				else
				{
					//Commandes HILC hors service
					findErrorCond = true;
					SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::verifCreateCond HILC Command are disabled");
				}
			}
		}
	}
	else
	{
		// argument invalide
		findErrorCond = true;
		SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::verifCreateCond Error: Wrong Command Arg ");
	}	
#else	
	SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::verifCreateCond verifPrepCondType Cond1");

	// equipement et operateur definits
	if ((eq.size() > 0) && ( op.size() >0))
	{
		for(std::list<Session*>::iterator it = m_sessionList.begin();  it != m_sessionList.end() ; ++it)
		{
			// Verification de l'existance de Session sur cet equipement
			if ( eq.compare((*it)->getEqA()) ==0 )
			{
				findErrorCond = true;
				// Verification de l'operateur
				if ( op.compare((*it)->getOpN()) !=0 )
				{
					//deux operateur ne peuvent avoir de Session simultanement sur un equipement
					SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::verifCreateCond Prep Error : Existing Session on this equipment with different operator");
				}
				// Existance de Session sur cet equipement avec le meme operateur
				else
				{
					//un operateur ne peux renvoyer une commande de Preparation alors qu'une autre commande est en cours sur cet equipement
					SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::verifCreateCond Prep Error: Existing Session on this equipment for this operator");
				}
			}
		}
	}
	else
	{
		findErrorCond = true;
		SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::verifCreateCond Error: Command incorrect");

		// send Alarm
		SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::verifCreateCond Send Alarm");

		HILC_UpdateData * m_updateData = HILC_UpdateData::getInstance();
		m_updateData->sendAlarmExt(eq.c_str());
	}	
#endif

    //Aucune condition empechant la creation trouvee
    if ( !findErrorCond )
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::verifCreateCond OK");
        status = ScsValid;
    }

    return status;
}

ScsStatus SessionManager::notifySessionOfEqtChangement(string eqtAlias)
{
    ScsStatus status = ScsError;
    try
    {
        int sessionIndice= this->findSessionForEqt(eqtAlias);

        if (sessionIndice != -1 )
        {
            std::list<Session*>::iterator it = m_sessionList.begin();
            std::advance(it, sessionIndice);
            // 'it' points to the Preparation Command

            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::notifySessionOfEqtChangement  Session  found ");
            (*it)->print();

            status = (*it)->actionOnNotify();

            if (status.isValid())
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::notifySessionOfEqtChangement: actionOnNotify  OK");
            }
            else
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::notifySessionOfEqtChangement :actionOnNotify Error");
            }

        }
        else
        {
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::notifySessionOfEqtChangement : Session not found ");
        }
    }
    catch(...)
    {
        status = ScsError;
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::notifySessionOfEqtChangement Error :");
    }

    return status;
}


int SessionManager::findSessionForEqt(string eqtAlias)
{
    int result = -1;

    // equipement et operateur definits
    if (eqtAlias.size() > 0)
    {
        int indice =0;
        for(std::list<Session*>::iterator it = m_sessionList.begin();  it != m_sessionList.end() ; ++it)
        {
            //Si il existe d'autres Session  sur cet equipement pour cet Operateur
            if (   ( eqtAlias.compare((*it)->getEqA()) ==0 )  )
            {
                // et que le State est renseigné
                string currentState = (*it)->getCurrentState()->info();
                if ( !currentState.empty() )
                {
                    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::findSessionForEqt Eqpt Found ");
                    result = indice;
                    //break;
                }
                else
                {
                    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::findSessionForEqt Null currentState Found ");
                }
            }
            indice ++;
        }

    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::findSessionForEqt Error: Incorrect Equipment ");
    }
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SessionManager::findSessionForEqt indice : %d", result);

    return result;
}
