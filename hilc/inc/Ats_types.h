/*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
**
** NOM FICHIER INCLUDE : ATS_types.h
**
** OBJET:
**
**  Définition des types de données numériques manipulés dans l'ATS
**  et provenant d'autres logiciels (ex: SCADA). Ce fichier permet 
**  de mettre en cohérence la taille de ces données (en octets) et 
**  donc de s'affranchir des fichiers d'include externes à l'ATS 
**  pour les compilations.
**  
**
**----------------------------------------------------------------------------
**
**  DATE   AUTEUR MODIFICATION
**
**  SEP 98 EM     Création
**    
**----------------------------------------------------------------------------
**
*/

#ifndef __ATS_types
#define __ATS_types

#ifndef   AtsTRUE
#define   AtsTRUE (1)
#endif

#ifndef   AtsFALSE
#define   AtsFALSE (0)
#endif

typedef char	AtsChar;	   /* represents a character */
#if defined(crDEC_COMP)
typedef long	AtsInt;	   
#else
typedef int	AtsInt;
#endif

typedef AtsChar AtsLogical;

typedef int	AtsInt32;   /* 32 bit signed integer */
typedef short	AtsInt16;   /* 16 bit signed integer */


#if defined(crIBM_COMP)
typedef signed char AtsInt8;	   /* 8-bit signed integer */
#else
typedef char	    AtsInt8;	   /* 8-bit signed integer */
#endif

typedef unsigned int AtsUInt32;  /* 32 bit unsigned integer */

#ifdef lint
#define AtsUInt16 unsigned int
#define AtsUInt8	 unsigned int
#else
typedef unsigned short AtsUInt16; /* 16-bit unsigned integer */
typedef unsigned char  AtsUInt8;	 /* 8-bit unsigned integer */
#endif

typedef float 	 AtsFloat;	/* 4 byte floating point */ 
typedef double   AtsDouble;	/* 8 byte floating point */

typedef unsigned int  AtsBitfld;  /* allows coersion to bit fields
					           for enums */

typedef AtsUInt8  AtsBytes4[4];
typedef AtsUInt8  AtsBytes8[8];
typedef AtsUInt8  AtsBytes12[12];
typedef AtsUInt8  AtsBytes16[16];
typedef AtsUInt8  AtsBytes128[128];
typedef AtsUInt8  AtsBytes256[256];

/* EM 040699 FFT 195 : debut */
typedef  AtsUInt16  AtsPlin;
/* EM 040699 FFT 195 : fin */

/* LB 17 01 2000 ajout pour compile, a virer absolument*/
typedef  AtsInt32 AtsDbConnection;
typedef  AtsUInt8 AtsDeType;
/* LB 17 01 2000 ajout pour compile, a virer absolument*/

#endif 
