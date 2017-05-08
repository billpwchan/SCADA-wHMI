//+-----------------------------------------------------------------------
//|Company       : Thales
//|CSCI          : HILC
//|
//| Purpose           : This class is a main class for handle Database
//|
//| Filename          : HILC_UpdateData.cpp
//|
//| Creation Date     :
//|
//| Author            :
//|
//| History           :
//|
//+-----------------------------------------------------------------------
#include <iostream>
#include <sstream>
#include "HILC_UpdateData.h"
#include "SessionManager.h"
#include "ctlcmd.h"
#include "scs.h"
#include "Traces.h"
#include "dbmerror.h"
#include "scstimer.h"
#include "SigTraces.h"
//Not used yet #include "SigError.h"
#include "HILCdefs.h"
#include "SessionManager.h"
#include "scsalmext.h"
#include <stddef.h>
using namespace std;

// initialisation du pointeur de singleton
HILC_UpdateData *HILC_UpdateData::m_instance = NULL;
SessionManager *HILC_UpdateData::m_SessionManager = NULL;

//+-----------------------------------------------------------------------
//|                           THALES
//+-----------------------------------------------------------------------
//|
//| Class          : HILC_UpdateData
//|
//| Method         : HILC_UpdateData()
//|
//| Description    : Default constructor of the class
//|
//| Preconditions  : -
//|
//| Postconditions : -
//|
//| Error          : -
//|
//+-----------------------------------------------------------------------
HILC_UpdateData::HILC_UpdateData ()
{
    _PtrDbmPoller = NULL;
    _PtrCtlCmdServerWithoutCB = NULL;
    _PtrScsAlmExtern = NULL;
    m_SessionManager = SessionManager::getInstance();
}

//+-----------------------------------------------------------------------
//|                           THALES
//+-----------------------------------------------------------------------
//|
//| Class          : HILC_UpdateDonnees
//|
//| Method         : constructor of the class
//|
//| Description    : Initialise the Dbmpoller pointer
//|
//| Preconditions  : -
//|
//| Postconditions : -
//|
//| Error          : -
//|
//+-----------------------------------------------------------------------
HILC_UpdateData::HILC_UpdateData(DbmPoller *aPtrDbmPoller, DbmServer *aPtrDbmServer, CtlCmdServer *aPtrCtlCmdServerWithoutCB, ScsAlmExtern *aPtrScsAlmExtern): HILC_ManipulationDonnees(aPtrDbmServer)
{
    if( ( aPtrDbmPoller ) && (aPtrCtlCmdServerWithoutCB) && (aPtrScsAlmExtern) )
    {
        _PtrDbmPoller = aPtrDbmPoller;
        _PtrCtlCmdServerWithoutCB = aPtrCtlCmdServerWithoutCB;
        _PtrScsAlmExtern = aPtrScsAlmExtern;
        m_SessionManager=SessionManager::getInstance();
    }
    else
    {
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "   HILC_UpdateData-Init failed for DbmPoller ");
        _PtrDbmPoller = NULL;
        _PtrCtlCmdServerWithoutCB = NULL;
        _PtrScsAlmExtern = NULL;
        m_SessionManager = NULL;
    }
}


//+-----------------------------------------------------------------------
//|                           THALES
//+-----------------------------------------------------------------------
//|
//| Class          : HILC_UpdateData
//|
//| Method         : ~HILC_UpdateData
//|
//| Description    : Destructor of the class
//|
//| Preconditions  : -
//|
//| Postconditions : -
//|
//| Error          : -
//|
//+-----------------------------------------------------------------------
HILC_UpdateData::~HILC_UpdateData()
{
}


HILC_UpdateData* HILC_UpdateData::getInstance()
{
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "   HILC_UpdateData-getInstance " );

    if (m_instance == NULL)
    {
        m_instance = new HILC_UpdateData();
    }

    return m_instance;
}


ScsStatus HILC_UpdateData::init(DbmServer *ptrDbmServer, DbmPoller *ptrDbmPoller, CtlCmdServer *ptrCtlCmdServerWithoutCB, ScsAlmExtern *ptrScsAlmExtern)
{
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " ###############################HILC_UpdateData-Init: DbmServer DbmPoller CtlCmdServer###############################");
    ScsStatus status = ScsError;

    if( ptrDbmServer )
    {
        _PtrDbmServer = ptrDbmServer;
        if( ptrDbmPoller )
        {
            _PtrDbmPoller = ptrDbmPoller;
            if(ptrCtlCmdServerWithoutCB)
            {
                _PtrCtlCmdServerWithoutCB = ptrCtlCmdServerWithoutCB;
                if( ptrScsAlmExtern )
                {
                    _PtrScsAlmExtern = ptrScsAlmExtern;
                    m_SessionManager=SessionManager::getInstance();
                    if (m_SessionManager == NULL)
                    {
                        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "   HILC_UpdateData-Init: get SessionManager Failed");
                        status = ScsError;
                    }
                    else
                    {
                        // HILC_UpdateData
                        status = this->initSCS();
                        if (status.isValid())
                        {
                            //Initialisation of equipment type list used for HILC
                            status = this->initEquipmentTypeList();
                            if (status.isValid())
                            {
                                // For each equipment type of this list find all equipment and add their address to the vector  _EquipmentAddress
                                status = this->initFullEquipmentAddressList();
                                if (!status.isValid())
                                {
                                    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "   HILC_UpdateData-Init failed for initFullEquipmentAddressList ");
                                }
                            }
                            else
                            {
                                SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "   HILC_UpdateData-Init failed for initEquipmentTypeList ");
                            }
                        }
                        else
                        {
                            SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "   HILC_UpdateData-Init failed for initSCS ");
                        }
                    }
                }
                else
                {
                    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "   HILC_UpdateData-Init failed for ScsAlmExtern ");
                }
            }
            else
            {
                SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "   HILC_UpdateData-Init failed for CtlCmdServerWithoutCB ");
            }
        }
        else
        {
            SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "   HILC_UpdateData-Init failed for DbmPoller ");
        } /*if( pDbmPoller ) */
    }
    else
    {
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "   HILC_UpdateData-Init failed for DbmServer ");
    } /*if( pDbmServer ) */

    return  status;
}

const ScsStatus HILC_UpdateData::initSCS()
{
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "HILC_UpdateData::initSCS");
    ScsStatus status = ScsError;

    status = _PtrDbmServer->getStatus();
    if (!status.isValid())
    {
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "Connection to DbmServer error");
    }

    status = _PtrDbmPoller->getStatus();
    if (!status.isValid())
    {
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "Connection to DbmPoller error");
    }
    status = _PtrCtlCmdServerWithoutCB->getStatus();
    if (!status.isValid())
    {
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "Connection to CtlCmdServerWithoutCB error");
    }

    return status;
}


ScsStatus HILC_UpdateData::verifEquipementAttributIntvalue(string eq, string att, int val)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "HILC_UpdateData::verifEquipementAttributIntvalue start for %s %s",eq.c_str(),att.c_str());

    int value =-1;
    if (eq.size() > 0)
    {
        //Obtention de l'alias reel de l'equipement
        if (att.size() > 0)
        {
            string attribut = HILC_ConfManager::getInstance()->HILC_TAG_ALIAS;
            attribut += eq;
            attribut += att;

            value =  this->ReadInteger(attribut.c_str(), status);
        }
        else
        {
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::verifEquipementAttributIntvalue:ReadInteger error");
        }
    }
    if (status.isValid())
    {
        // La valeur de l'attribut correspond à la valeur testee
        if (value==val)
        {
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData::verifEquipementAttributIntvalue OK for %s %s",eq.c_str(),att.c_str());
        }
        else
        {
            status =ScsError;
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::verifEquipementAttributIntvalue KO for %s %s",eq.c_str(),att.c_str());
        }
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::verifEquipementAttributIntvalue equipment name error");
    }

    return status;
}

/*
ScsStatus HILC_UpdateData::verifEquipementAttributStringvalue(string eq, string att, string val)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "HILC_UpdateData::verifEquipementAttributStringvalue start for %s %s",eq.c_str(),att.c_str());

    char value[500];
    if (eq.size() > 0)
    {
        //Obtention de l'alias reel de l'equipement
        if (att.size() > 0)
        {
            string attribut = HILC_ConfManager::getInstance()->HILC_TAG_ALIAS;
            attribut += eq;
            attribut += att;

            status = this->ReadString(attribut.c_str(), value);

        }
        else
        {
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::verifEquipementAttributStringvalue:ReadString error");
        }
    }
    if (status.isValid())
    {
        string str_value=value;
        // La valeur de l'attribut correspond à la valeur testee
        if (str_value.compare(val) ==0)
        {
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData::verifEquipementAttributStringvalue OK for %s %s",eq.c_str(),att.c_str());
        }
        else
        {
            status =ScsError;
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData::verifEquipementAttributStringvalue KO for %s %s",eq.c_str(),att.c_str());
        }
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::verifEquipementAttributStringvalue equipment name error");
    }

    return status;
}
*/

#ifdef USE_SIO			
ScsStatus HILC_UpdateData::sendCommand(string commandName, unsigned char* commandMessage, unsigned int uiMessageSize, string src)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "######################################################################################################");
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   HILC_UpdateData-sendCommand " );

    try
    {
        //build Command
        CtlCmdCommand oCommand;
        oCommand.setName(commandName.c_str());
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "uiMessageSize: , %d",uiMessageSize);

        oCommand.setValue((int)uiMessageSize, commandMessage);

        status = _PtrCtlCmdServerWithoutCB->sendCommand(1,&oCommand,src.c_str(),0);

    }
    catch( ... )
    {
        status = ScsError;
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData-sendCommand Error: %s ", commandName.c_str() );
    }

    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "   HILC_UpdateData-sendCommand End" );
    return status;

}
#endif

ScsStatus HILC_UpdateData::sendCommand(string commandName, int commandValue, string src)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "######################################################################################################");
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "   HILC_UpdateData-sendCommand Name: %s", commandName);
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "   HILC_UpdateData-sendCommand Int Value: %d", commandValue);
    try
    {
        //build Command
        CtlCmdCommand oCommand;
        oCommand.setName(commandName.c_str());
        oCommand.setValue(commandValue);

        status = _PtrCtlCmdServerWithoutCB->sendCommand(1,&oCommand,src.c_str(),0);
		// A noter que si une initCond de la commande est false (typiquement : Health Status not Healthy) le CTL
		// n'enverra pas la commande mais son ScsStatus de retour reste OK
		// Si on voulait detecter une erreur pour eviter l'attente du timeout sur la commande il y a 2 solutions, pas ideales :
		// utiliser un appel avec callback, mais du coup pas de retour synchrone NOK au client, et traitement asynchrone + complexe
		// OU consulter l'attribut execStatus, mais comme cette facon est tombée en désuétude il n'y a pas de .h Scadasoft public
		// pour connaitre les différentes valeurs de celui-ci, les valeurs pourraient donc changer suivant les versions Scadasoft..
		if (status.isValid())
		{
			status = checkExecStatus(commandName);
		}
    }
    catch( ... )
    {
        status = ScsError;
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData-sendCommand Int Error: %s ", commandName.c_str() );
    }

    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "   HILC_UpdateData-sendCommand End" );
    return status;
}

ScsStatus HILC_UpdateData::sendCommand(string commandName, float commandValue, string src)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "######################################################################################################");
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "   HILC_UpdateData-sendCommand Name:%s", commandName);
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "   HILC_UpdateData-sendCommand Float Value: %f", commandValue);
    try
    {
        //build Command
        CtlCmdCommand oCommand;
        oCommand.setName(commandName.c_str());
        oCommand.setValue(commandValue);

        status = _PtrCtlCmdServerWithoutCB->sendCommand(1,&oCommand,src.c_str(),0);
		// Cf remarque du sendCommand int
		if (status.isValid())
		{
			status = checkExecStatus(commandName);
		}		
    }
    catch( ... )
    {
        status = ScsError;
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData-sendCommand Float Error: %s ", commandName.c_str() );
    }

    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "   HILC_UpdateData-sendCommand End" );
    return status;
}

ScsStatus HILC_UpdateData::checkExecStatus(string commandName)
{
	ScsStatus statusRead, status;
	commandName += ".execStatus";
	int attValue =  this->ReadInteger(commandName.c_str(), statusRead);
	// We won't check != CTL_K_EXE_STAT_END (there are some temporary states)
	if (attValue == CTL_K_EXE_STAT_INTRCONDINI)
	{
		SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData-checkExecStatus not OK: %d", attValue);
		status = ScsError;
		status.setError(HILC_INIT_COND_ERROR);
	}
	return status;
}

ScsStatus HILC_UpdateData::sendAlarmExt(const char *i_pointAlias)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "######################################################################################################");
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   HILC_UpdateData-sendAlarmExt " );

    try
    {
        //  ScsStatus notify(short classId, const char *objectName, short objectId,int objectState, ...)

        status = _PtrScsAlmExtern->notify(HILC_ConfManager::getInstance()->HILC_K_ALA_EXTERN_01, i_pointAlias,0,ALMEXT_OBJ_STATE_ERROR);
    }
    catch( ... )
    {
        status = ScsError;
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData-sendAlarmExt  Error: %s ", i_pointAlias );
    }
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "   HILC_UpdateData-sendAlarmExt End" );
    return status;
}


ScsStatus HILC_UpdateData::setEquipmentAddress( const char * equipmentType)
{
    //find the list of  eqpt used for HILC command
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "HILC_UpdateData::setEquipmentAddress()");

    ScsAddress *equipmentList = NULL;
    short count = 0;

    ScsStatus status;
	
	for(std::list<std::string>::iterator it = m_rootsList.begin();  it != m_rootsList.end() ; ++it)
	{
		string rootAlias = HILC_ConfManager::getInstance()->HILC_TAG_ALIAS;
		rootAlias += *it;
		SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData : setEquipmentAddress rootAlias : %s", rootAlias.c_str());

		try
		{
			status = _PtrDbmServer->getInstances(equipmentType, equipmentList, count, rootAlias.c_str());
		}
		catch( ... )
		{
			status = ScsError;
			SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData-setEquipmentAddress  Error: %s ", equipmentType );
		}

		if (!status.isValid())
		{
			SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData : DbmServer -> getChildren error %d for %s", status.getMinor(), equipmentType);
			status = ScsValid;
		}
		else
		{
			if ( count > 0)
			{
				SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData : setEquipmentAddress found %d instances of %s",
				  count, equipmentType);
				// REM : on optimise le filtrage scstype en faisant un seul read pour tout les points
				// (donc pas d'utilisation de this->ReadString)
				// autres axes d'optimisation possibles :
				// - utiliser plutot DbkSession multiread ?
				// - OU un equipmentList construit dynamiquement quand besoin
				// - OU une recherche par dio/aio (plus complexe et plus eloigne du principe initial par equipementType)
				DbmDataSet DataSet;
				for( short i = 0 ; i < count ; ++i)
				{
					string strScsTypePath = std::string(equipmentList[i].getPath()) + ".scstype";
					DataSet.add( DbmData( ScsAddress( strScsTypePath.c_str() ))) ;
				}			
				ScsStatus Status = _PtrDbmServer->read( DataSet );
				if(Status.isValid())
				{
					for( short i = 0 ; i < count ; ++i)
					{
						std::string equipmentAlias = (std::string) equipmentList[i].getAlias();
						SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData : setEquipmentAddress Alias : %s", 		equipmentAlias.c_str());
						if (m_scsTypeList.size() > 0)
						{ // We filter with scstype
							string valueStr = (char*)DataSet[i]->getBuffer();
							//SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData : setEquipmentAddress scstype : %s ", valueStr.c_str());
							for(std::list<std::string>::iterator it = m_scsTypeList.begin();  it != m_scsTypeList.end() ; ++it)
							{
								if (valueStr.compare(*it) == 0)
								{
									SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData : setEquipmentAddress scstype OK");
									_EquipmentAddress.push_back(equipmentList[i]);
								}
							}						
						}	
						else
						{
							_EquipmentAddress.push_back(equipmentList[i]);
						}
					}
				}
				else
				{
					SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData : read %d all scstype error", count);
				}
			}
			else
			{
				SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData : No equipment found for type %s", equipmentType);
			}
		}
	}
	
    delete [] equipmentList;
    return status;
}
int HILC_UpdateData::findEquipmentNumber(string str_alias)
{

    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "HILC_UpdateData::findEquipmentNumber for %s", str_alias.c_str());

    int result =-1;
    string alias = HILC_ConfManager::getInstance()->HILC_TAG_ALIAS;

    for( int i = 0 ; i < _EquipmentAddress.size() ; ++i)
    {
        std::string equipmentAlias = alias + (std::string) _EquipmentAddress[i].getAlias() ;

        if (str_alias.compare(equipmentAlias) == 0)
        {
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "###############################HILC_UpdateData::findEquipmentNumber alias: %s", equipmentAlias.c_str());
            result =i;
            break;
        }
    }

    return result;
}

ScsStatus HILC_UpdateData::subscribeEquipment(Session*  session)
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "###############################HILC_UpdateData::subscribeEquipement()###############################");
    ScsStatus status = ScsError;

    string gpName = session->getGpN();
    int indiceEqt = session->getId();
    string aliasEqt = session->getEqA();
    string typeEqt = session->getEqT();
    string cmdName = session->getCmdN();

    string aliasEquip = HILC_ConfManager::getInstance()->HILC_TAG_ALIAS;
    aliasEquip += aliasEqt;


    if(gpName.compare("") ==0)
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::subscribeEquipement KO :  session groupname Error");
    }
    else
    {

        const char * groupname= gpName.c_str();

        DbmDataSet eqtDataSet(groupname, 0, HILC_UpdateData::subscribeEquipmentCallBack, (ScsAny)this);

        for ( int i=0;  i<HILC_ConfManager::getInstance()->HILC_ATT_NUMBER; i++)
        {
            string HILC_Attribut_Path = session->getHILC_Att_Path(i);
            if (HILC_Attribut_Path.compare("") ==0)
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::subscribeEquipement warning :  HILC attribute Path Error for indice %d", i);
            }
            else
            {
                string addr_Elm= aliasEquip + HILC_Attribut_Path;
                DbmData data(ScsAddress (addr_Elm.c_str()));
                data.setId(indiceEqt);
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "         Adding subscription to HILC attribute %d path %s", i, addr_Elm.c_str() );
                eqtDataSet.add(data);
            }
        }



        unsigned short result = 0;
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "         Check  group " );
        status = _PtrDbmPoller->isGroupCreated(groupname, result);

        if (status.isValid() && result == 0)
        {
            SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "         HILC_UpdateData::creategroup  start");
            status = _PtrDbmPoller->createGroup(eqtDataSet);
        }
        else
        {
            SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "         HILC_UpdateData::isGroupCreated  error");
        }
        if (status.isValid())
        {
            status = _PtrDbmPoller->readSubscribeAndUpdate(eqtDataSet);
            if (status.isValid())
            {
                SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "HILC_UpdateData::readSubscribeAndUpdate  OK");
            }
            else
            {
                SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::readSubscribeAndUpdate  error");
            }
        }
        else
        {
            SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "         HILC_UpdateData::creategroup  error");
        }

    }
    return status;
}

ScsStatus HILC_UpdateData::unSubscribeEquipment(Session * sessionToDelete)
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "###############################HILC_UpdateData::unSubscribeEquipment()###############################");
    ScsStatus status = ScsError;
    string sessionGroupName = sessionToDelete->getGpN();
    string sessionEquipAlias = sessionToDelete->getEqA();

    //unsuscribe dataset
    status = _PtrDbmPoller->unsubscribe(sessionGroupName.c_str());

    if (status.isValid())
    {
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData::unSubscribeEquipment:unsubscribe for equipment: %s : OK",sessionEquipAlias.c_str());
        // Delete subscription group
        status = _PtrDbmPoller->deleteGroup(sessionGroupName.c_str());
        if (status.isValid())
        {
            SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData::unSubscribeEquipment:deleteGroup  for equipment: %s : OK",sessionEquipAlias.c_str());
        }
        else
        {
            SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::unSubscribeEquipment:deleteGroup  KO for equipment: %s  : failed",sessionEquipAlias.c_str());
        }

    }
    else
    {
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::unSubscribeEquipment:unsubscribe  KO for equipment: %s  : failed",sessionEquipAlias.c_str());
    }
    return status;
}


void HILC_UpdateData::subscribeEquipmentCallBack(const DbmDataSet &dataSet, ScsAny arg)
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "###############################HILC_UpdateData::subscribeEquipmentCallBack###############################");


    short count = 0;
    count = dataSet.getDataCount();
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData::subscribeEquipmentCallBack count: %d",count);

    HILC_UpdateData *m_HILC_UpdateData = (HILC_UpdateData *) arg;
    if( !m_HILC_UpdateData )
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::subscribeEquipmentCallBack: Cannot get the m_HILC_UpdateData instance! subscription callback will do nothing");
        return;
    }

    if (count > 0)
    {
        DbmData *dbdata = dataSet[0];
        if( dbdata )
        {
            int id = dbdata->getId();
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData::subscribeEquipmentCallBack: dbdata Id: %d ",id );

            if (m_HILC_UpdateData->_EquipmentAddress[id])
            {
                int Value = *(int*)dbdata->getBuffer();
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData::subscribeEquipmentCallBack: %s    Id: %d   Value: %d",  m_HILC_UpdateData->_EquipmentAddress[id].getAlias(),id, Value );

                string eqtAlias = m_HILC_UpdateData->_EquipmentAddress[id].getAlias();

                if( !eqtAlias.empty())
                {
                    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData::subscribeEquipmentCallBack: notifyChangeOnEqt send to  %s", eqtAlias.c_str() );

                    //Find the Sesssion on this equipement  and notify changement of attribut
                    ScsStatus status = ScsError;
                    status = m_SessionManager->notifySessionOfEqtChangement(eqtAlias);

                    if (status.isValid())
                    {
                        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData::subscribeEquipmentCallBack: notifySessionOfEqtChangement %s OK", eqtAlias.c_str() );
                    }
                    else
                    {
                        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::subscribeEquipmentCallBack: notifySessionOfEqtChangement %s Error", eqtAlias.c_str() );
                    }
                }
                else
                {
                    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::subscribeEquipmentCallBack: eqtAlias NUL" );
                }
            }
            else
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::subscribeEquipmentCallBack: _EquipmentAddress NUL" );
            }
        }
    }
}

ScsStatus HILC_UpdateData::setEquipment(const char * hilc_equipment)
{
    ScsStatus status = ScsError;
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " #############  HILC_UpdateData : setEquipment  start for %s", hilc_equipment);
    status = this->setEquipmentAddress( hilc_equipment);

    if (status.isValid())
    {
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "   HILC_UpdateData : setEquipment  OK for %s", hilc_equipment);
    }

    else
    {
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "   HILC_UpdateData : setEquipment  failed for %s", hilc_equipment);
    }
    return status;
}

ScsStatus HILC_UpdateData::initEquipmentTypeList()
{
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   HILC_UpdateData-Init: initEquipmentTypeList ");
    ScsStatus status = ScsError;
		
    int LoadSize = HILC_ConfManager::getInstance()->HILC_EquipmentTypes.size();
    int ConfSize = HILC_ConfManager::getInstance()->HILC_CLASS_NUMBER;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData::initEquipmentTypeList equipmentType LoadSize = %i ConfSize = %i", LoadSize, ConfSize);
    if(  (LoadSize == ConfSize) && (ConfSize >0) )
    {

        for (int i=0; i<LoadSize; ++i)
        {
            string tmp = HILC_ConfManager::getInstance()->HILC_EquipmentTypes[i];
            SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData::initEquipmentTypeList equipmentType %s", tmp.c_str() );
            //BHE m_equipmentTypeList.push_back( tmp.c_str() );
			m_equipmentTypeList.push_back( tmp );
        }
    }
	
    if( (m_equipmentTypeList.size() == ConfSize)  && (ConfSize >0) )
    {
        status = ScsValid;
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "HILC_UpdateData::initEquipmentTypeList   m_equipmentTypeList.size = %i",m_equipmentTypeList.size());
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::initEquipmentTypeList   Error m_equipmentTypeList.size = %i",m_equipmentTypeList.size());
    }
	
    LoadSize = HILC_ConfManager::getInstance()->HILC_ScsTypes.size();
    ConfSize = HILC_ConfManager::getInstance()->HILC_SCSTYPES_NUMBER;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData::initEquipmentTypeList scstype LoadSize = %i ConfSize = %i", LoadSize, ConfSize);
    if(  (LoadSize == ConfSize) && (ConfSize >0) )
    {
        for (int i=0; i<LoadSize; ++i)
        {
            string tmp = HILC_ConfManager::getInstance()->HILC_ScsTypes[i];
            SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData::initEquipmentTypeList scstype %s", tmp.c_str() );
			m_scsTypeList.push_back( tmp );
        }
    }
	
    if( (m_scsTypeList.size() == ConfSize)  && (ConfSize >0) )
    {
        status = ScsValid;
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "HILC_UpdateData::initEquipmentTypeList   scstypes.size = %i",m_scsTypeList.size());
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::initEquipmentTypeList   Error m_scsTypeList.size = %i",m_scsTypeList.size());
    }
		
	// Init Roots where equipement types will be searched
    LoadSize = HILC_ConfManager::getInstance()->HILC_Roots.size();
    ConfSize = HILC_ConfManager::getInstance()->HILC_ROOTS_NUMBER;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData::initEquipmentTypeList roots LoadSize = %i ConfSize = %i", LoadSize, ConfSize);
    if(  (LoadSize == ConfSize) && (ConfSize >0) )
    {
        for (int i=0; i<LoadSize; ++i)
        {
            string tmp = HILC_ConfManager::getInstance()->HILC_Roots[i];
            SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData::initEquipmentTypeList root %s", tmp.c_str() );
			m_rootsList.push_back( tmp );
        }
    }
    if( (m_rootsList.size() == ConfSize)  && (ConfSize >0) )
    {
        status = ScsValid;
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "HILC_UpdateData::initEquipmentTypeList   roots.size = %i",m_rootsList.size());
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::initEquipmentTypeList   Error m_rootsList.size = %i",m_rootsList.size());
    }
		
    return status;
}

ScsStatus HILC_UpdateData::initFullEquipmentAddressList()
{
    //set EquipementAddress list for all  equipment used for HILC command
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   HILC_UpdateData-Init: initFullEquipmentAddressList ");
    ScsStatus status = ScsValid;

    for(std::list<std::string>::iterator it = m_equipmentTypeList.begin();  it != m_equipmentTypeList.end() ; ++it)
    {
        if (status.isValid())
        {
            SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SessionManager::initFullEquipmentAddressList equipmentType %s", (*(it)).c_str() );
            // for each  equipment call setEquipment
            status = this->setEquipment((*(it)).c_str());
        }
    }
	SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   HILC_UpdateData-Init: initFullEquipmentAddressList, _EquipmentAddress size %d", _EquipmentAddress.size());
    if (status.isValid())
    {
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   HILC_UpdateData-Init: initFullEquipmentAddressList OK ");
    }
    else
    {
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "   HILC_UpdateData-Init: initFullEquipmentAddressList Failed ");
    }

    return status;
}


void HILC_UpdateData::timerCallback(ScsAny arg)
{
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "###############################HILC_UpdateData::timerCallback###############################");
    //Cast arg as a Session *, in order to be able to work on the right Session when the timer end
    Session * session = (Session *) arg;
    int eqtNumber = session->getId();
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   HILC_UpdateData::timerCallback for Session on eqt: %d", eqtNumber);

    ScsStatus status = ScsError;

    status = session->actionOnTimerEnd();
    if (status.isValid())
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "HILC_UpdateData::timerCallback actionOnTimer OK");
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::timerCallback actionOnTimer KO" );
    }
    //Delete State Session and dbmPoller subscription
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "HILC_UpdateData::timerCallback Delete State Session and dbmPoller subscription");

    SessionManager * m_SessionManager=SessionManager::getInstance();
    status= m_SessionManager->deleteSession(session);

    if (status.isValid())
    {
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "HILC_UpdateData::timerCallback deleteSession OK");
    }
    else
    {
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::timerCallback deleteSession KO" );
    }

}

void HILC_UpdateData::finalTimerCallback(ScsAny arg)
{
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "###############################HILC_UpdateData::finalTimerCallback###############################");
    //Cast arg as a Session *, in order to be able to work on the right Session when the timer end
    Session * session = (Session *) arg;
    int eqtNumber = session->getId();
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   HILC_UpdateData::finalTimerCallback for Session on eqt: %d", eqtNumber);

    ScsStatus status = ScsError;
    ScsTimer * finalTimer = session->getFinalTimerHILC();

    SessionManager * m_SessionManager=SessionManager::getInstance();
    status = m_SessionManager->deleteSession(session);

    if (status.isValid())
    {
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "HILC_UpdateData::finalTimerCallback deleteSession OK");
    }
    else
    {
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::finalTimerCallback deleteSession KO" );
    }
    //cancelTimer
    //if cancelTimer is called in the expiration callback function, this function should absolutely be the last instruction.
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "HILC_UpdateData::finalTimerCallback cancel final Timer");
    ScsTimer::cancelTimer(finalTimer);

}
