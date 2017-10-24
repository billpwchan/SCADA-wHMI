package com.thalesgroup.config.scdm.attributecomputer.common;

import java.security.MessageDigest;
import java.util.Random;

import com.thalesis.config.utils.Logger;

/******************************************************************************/
/*  FILE  : OPMWinCrypt.java                                                  */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES TRANSPORTATION SYSTEM                                   */
/*  CREATION DATE : 11 juil. 07                                               */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Transportation System 1996-2006.                       */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/


public class OpmWinCrypt {
	
	// the size used for sub password
	static private final int PASSWDSIZE = 8;
	
	// Character set allowed for the salt string
    static private final String SALTCHARS 
        = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567829./";

	static public String crypt(String password) {
		// salt processing
		StringBuffer saltBuf = new StringBuffer();
		Random rnd = new Random();
		int index = rnd.nextInt(SALTCHARS.length());
		saltBuf.append(SALTCHARS.substring(index, index+1));
		index = rnd.nextInt(SALTCHARS.length());
		saltBuf.append(SALTCHARS.substring(index, index+1));
		String salt = saltBuf.toString();
		
		StringBuffer cryptedPasswd = new StringBuffer();
		int length = password.length();
		if(length < 8) {
			cryptedPasswd.append(singleCrypt(password, salt));
		} else {
			int nbPASSWDSIZE = password.length()/PASSWDSIZE;
			for(int i=0; i<nbPASSWDSIZE; i++) {
				String subPasswd = password.substring(i*PASSWDSIZE, (i+1)*PASSWDSIZE);
				cryptedPasswd.append(singleCrypt(subPasswd, salt));
			}
			
			int modulo = password.length()%PASSWDSIZE;
			if(modulo >0) {
				String subPasswd = password.substring(
						nbPASSWDSIZE*PASSWDSIZE, length);
				cryptedPasswd.append(singleCrypt(subPasswd, salt));
			}
		}
				
		return cryptedPasswd.toString();
	}

	// the different algorithms used
	enum ALGO_ID {
		MD2,
		MD5,
		SHA
	}
	
	// to be compliant with C++ encrypting
	static private int INTERNALSIZE = 6;
	static private String singleCrypt(String password, String salt) {
		String cryptedPasswd = "";
		try {			
			byte[] saltA = salt.getBytes();
			int id = (saltA[0] + saltA[1])%3;
			
			ALGO_ID[] algoIDs = ALGO_ID.values();
			String algo = algoIDs[id].name();
			
			byte[] passwdA = password.getBytes();
			MessageDigest md = MessageDigest.getInstance(algo);
			byte[] hash = md.digest(passwdA);
	
			StringBuffer hashString = new StringBuffer();
			for ( int i = 0; i < INTERNALSIZE; ++i ) {
				String hex = Integer.toHexString(hash[i]);
				if(hex.length() == 1) { 
					hashString.append("0");
					hashString.append(hex.substring(0,1));
				}
				else
					hashString.append(hex.substring(0,2));
			}
			cryptedPasswd = 
				salt.substring(0,2) + hashString.toString().substring(0,2*INTERNALSIZE-1);
		} catch (Exception e) {
			Logger.USER.error("Exception : can't crypt password " + password);
		}
		return cryptedPasswd;
	}
	
	public static void main(String[] args) {
		String password = "hellotheworldiscoming";
		System.out.println("Crypt : " + crypt(password));
	}
}
