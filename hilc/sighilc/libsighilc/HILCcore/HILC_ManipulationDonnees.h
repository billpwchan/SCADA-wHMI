#ifndef __HILC_MANIPULATION_DONNEES_H__
#define __HILC_MANIPULATION_DONNEES_H__


#include "dbm.h"
#include <vector>
#include <string>

using namespace std ;
/*
Manipulation des donnees de la BD
Lecture / Ecriture
*/

class HILC_ManipulationDonnees
{
public:
  HILC_ManipulationDonnees ();
  HILC_ManipulationDonnees ( DbmServer * aPtrDbmServer );
  ~HILC_ManipulationDonnees ();
  // Read an integer value in the Database, define by his path
  const int ReadInteger( const char * aPath , ScsStatus & aStatus );
  // Read a string value in the Database
  // const ScsStatus ReadString( const char * aPath , char * aStringValue );
  // Read a vector value in the Database
  //const ScsStatus ReadVector( string aPath , vector<string> & aListe , int & aNbrRecord );
  // Write an integer value in the Database
  //const ScsStatus WriteInteger( const char * aPath , int aValue ) ;
  // Write an String value in the Database
  //const ScsStatus WriteString(const char *aPath, char *aString);

  //Get the alias's father of the current point
  //string GetAliasFather(const char *aCurrentAlias) const;
  //Get the alias of the current point
  //string GetAlias(const char *aPath) const;
  // Get the full path of the alias
  // const char * GetFullPath( const char * aAlias ) const;

  DbmServer * _PtrDbmServer ;
 
};

#endif // __HILC_MANIPULATION_DONNEES_H__
