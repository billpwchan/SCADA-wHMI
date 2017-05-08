//+-----------------------------------------------------------------------
//|Company       : Thales
//|CSCI          : HILC
//|
//| Purpose           : This class is a main class for handle Database
//|
//| Filename          : HILC_ManipulationDonnees.cpp
//|
//| Creation Date     :
//|
//| Author            :
//|
//| History           :
//|
//+-----------------------------------------------------------------------

#include "HILC_ManipulationDonnees.h"
#include "Traces.h"
#include "dbmerror.h"
#include "SigTraces.h"
//Not used yet #include "SigError.h"
#include "HILCdefs.h"


//+-----------------------------------------------------------------------
//|                           THALES
//+-----------------------------------------------------------------------
//|
//| Class          : HILC_ManipulationDonnees
//|
//| Method         : HILC_ManipulationDonnees()
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
HILC_ManipulationDonnees::HILC_ManipulationDonnees ()
    : _PtrDbmServer(0)
{
}

//+-----------------------------------------------------------------------
//|                           THALES
//+-----------------------------------------------------------------------
//|
//| Class          : HILC_ManipulationDonnees
//|
//| Method         : constructor of the class
//|
//| Description    : Initialise the DbmServer pointer
//|
//| Preconditions  : -
//|
//| Postconditions : -
//|
//| Error          : -
//|
//+-----------------------------------------------------------------------
HILC_ManipulationDonnees::HILC_ManipulationDonnees(DbmServer *aPtrDbmServer)
{

    if( aPtrDbmServer )
    {
        _PtrDbmServer= aPtrDbmServer;
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ),  "   HILC_ManipulationDonnees-Init failed for DbmServer ");
        _PtrDbmServer = NULL;
    }
}


//+-----------------------------------------------------------------------
//|                           THALES
//+-----------------------------------------------------------------------
//|
//| Class          : HILC_ManipulationDonnees
//|
//| Method         : ~HILC_ManipulationDonnees
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
HILC_ManipulationDonnees::~HILC_ManipulationDonnees()
{
}



//+-----------------------------------------------------------------------
//|                           THALES
//+-----------------------------------------------------------------------
//|
//| Class          : HILC_ManipulationDonnees
//|
//| Method         : ReadInteger
//|
//| Description    : Read an integer value in the Database, define by his path
//|
//| Preconditions  : The path must be exist in the Database
//|
//| Postconditions : -
//|
//| Error          : return -911 if the path is not correct and the ScsStatus is ScsError
//|
//+-----------------------------------------------------------------------
const int HILC_ManipulationDonnees::ReadInteger(const char *aPath, ScsStatus &aStatus)
{
    int Result = -911 ;
    DbmDataSet DataSet;
    DataSet.add( DbmData( ScsAddress( aPath ))) ;

    aStatus = _PtrDbmServer->read( DataSet );

    if(aStatus.isValid())
    {
        Result  = *(int*)DataSet[0]->getBuffer() ;
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "ReadInteger : read ok, aPath = %s, intValue = %d", aPath, Result);
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "Read error : %s",aPath);
    }

    return Result ;
}

//+-----------------------------------------------------------------------
//|                           THALES
//+-----------------------------------------------------------------------
//|
//| Class          : HILC_ManipulationDonnees
//|
//| Method         : ReadString
//|
//| Description    : Read a string value in the Database
//|
//| Preconditions  : The path must exist in the Database
//|
//| Postconditions : -
//|
//| Error          : return ScsError if the path is not correct
//|
//+-----------------------------------------------------------------------
/*
const ScsStatus HILC_ManipulationDonnees::ReadString(const char *aPath, char *aStringValue)
{

    DbmDataSet DataSet;
    DataSet.add( DbmData( ScsAddress( aPath ))) ;

    ScsStatus Status = _PtrDbmServer->read( DataSet );

    if(Status.isValid())
    {
        strcpy(  aStringValue , (char*)DataSet[0]->getBuffer());
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "ReadString error : %s not found",aPath);
    }

    return Status ;

}
*/


//+-----------------------------------------------------------------------
//|                           THALES
//+-----------------------------------------------------------------------
//|
//| Class          : HILC_ManipulationDonnees
//|
//| Method         : ReadVector
//|
//| Description    : Read a vector value in the Database.
//|                  Set a list with the value of the vector
//|
//| Preconditions  : The path mus be exist
//|
//| Postconditions : -
//|
//| Error          : return ScsValid if success, ScsError otherwise
//|
//+-----------------------------------------------------------------------
/*
const ScsStatus HILC_ManipulationDonnees::ReadVector(string aPath, vector<string> &aListe, int &aNbrRecord)
{

    string VectorValue;
    ScsData Data ;
    DbmDataSet DataSet ;
    DataSet.add( DbmData( ScsAddress( aPath.data() ))) ;

    ScsStatus Status = _PtrDbmServer->read( DataSet );
    if (Status.isValid())
    {
        Data = DataSet[0]->getData() ;
        aNbrRecord = Data.getNbRecords() ;
        for(int i = 0; i < aNbrRecord; i++)
        {
            VectorValue = (const char*) Data.getValue( i , 0 ) ;
            aListe.push_back( VectorValue  ) ;
        }
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "Read vector error : %s", aPath.data() ) ;
    }

    return Status ;
}
*/

//+-----------------------------------------------------------------------
//|                           THALES
//+-----------------------------------------------------------------------
//|
//| Class          : HILC_ManipulationDonnees
//|
//| Method         : WriteInteger
//|
//| Description    : Write an integer value in the Database
//|
//| Preconditions  : The path must be exist in the Database
//|
//| Postconditions : The value is updating in the Database
//|
//| Error          : return ScsValid if success, ScsError otherwise
//|
//+-----------------------------------------------------------------------
/*
const ScsStatus HILC_ManipulationDonnees::WriteInteger(const char *aPath, int aValue)
{
    DbmDataSet DataSet;
    DataSet.add( DbmData( aPath, aValue ));

    ScsStatus Status = _PtrDbmServer->write( DataSet );

    if (Status.isError())
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "WriteInteger error : %s", aPath);
    }

    return Status ;

}
*/

//+-----------------------------------------------------------------------
//|                           THALES
//+-----------------------------------------------------------------------
//|
//| Class          : CGE_ManipulationDonnees
//|
//| Method         : WriteString
//|
//| Description    : Write an String value in the Database
//|
//| Preconditions  : The path must be exist in the Database
//|
//| Postconditions : The value is updating in the Database
//|
//| Error          : return ScsValid if success, ScsError otherwise
//|
//+-----------------------------------------------------------------------
/*
const ScsStatus HILC_ManipulationDonnees::WriteString(const char *aPath, char *aString)
{
    DbmDataSet DataSet;
    DataSet.add( DbmData( aPath, aString ));

    ScsStatus Status = _PtrDbmServer->write( DataSet );

    if (Status.isError())
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "WriteString error : %s", aPath);
    }

    return Status ;

}
*/

//+-----------------------------------------------------------------------
//|                           THALES
//+-----------------------------------------------------------------------
//|
//| Class          : HILC_ManipulationDonnees
//|
//| Method         : GetFullPath
//|
//| Description    : Get the full path of the alias
//|
//| Preconditions  : -
//|
//| Postconditions : -
//|
//| Error          :
//|
//+-----------------------------------------------------------------------
/*
const char *HILC_ManipulationDonnees::GetFullPath(const char *aAlias) const
{
    ScsAddress Adr( aAlias );
    _PtrDbmServer->getFullPath( Adr );
    return Adr.getPath() ;
}
*/

//+-----------------------------------------------------------------------
//|                           THALES
//+-----------------------------------------------------------------------
//|
//| Class          : HILC_ManipulationDonnees
//|
//| Method         : GetAlias
//|
//| Description    : Get the alias of the current point
//|
//| Preconditions  : -
//|
//| Postconditions : -
//|
//| Error          :
//|
//+-----------------------------------------------------------------------
/*
string HILC_ManipulationDonnees::GetAlias(const char *aPath) const
{
    ScsAddress CurrentAdr( aPath ) ;
    _PtrDbmServer->getAlias( CurrentAdr ) ;
    string sRet = CurrentAdr.getPath();
    return sRet;
}
*/

//+-----------------------------------------------------------------------
//|                           THALES
//+-----------------------------------------------------------------------
//|
//| Class          : HILC_ManipulationDonnees
//|
//| Method         : GetAliasFather
//|
//| Description    : Get the alias's father of the current point
//|		     <alias>XXXX
//|
//| Preconditions  : -
//|
//| Postconditions : -
//|
//| Error          :
//|
//+-----------------------------------------------------------------------
/*
string HILC_ManipulationDonnees::GetAliasFather(const char *aCurrentAlias) const
{

    ScsAddress CurrentAdr( aCurrentAlias );
    _PtrDbmServer->getFullPath(CurrentAdr ) ;
    ScsAddress ParentAdr( CurrentAdr.getParentPath() ) ;
    _PtrDbmServer->getAlias( ParentAdr ) ;

    string sRet = ParentAdr.getPath();
    return sRet;
}
*/



