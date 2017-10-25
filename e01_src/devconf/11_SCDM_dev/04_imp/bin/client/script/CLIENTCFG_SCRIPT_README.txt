==========================================================================================	
                                                    Configurator client scripts                                                                                              
==========================================================================================

Surely it is good to read the python code linked to the batch before running it.
Some destroy CDV, then better to take care....

Use the CfgInterface.py core that implement the functions and wrap the java API of the configurator client. 

1/ batch script to launch jython file :
   - Script.bat : launch a jython script file
    
    USAGE : Script.bat [option_jython] scriptfile.py [parameters]
    
    [option_jython] -i inspect interactively after running script
    scriptfile.py      jython script
    [parameters]       parameters of jython script
    
    return 0 if success

2/ batch script to launch interactively "CfgInterfaceMain.py" python file :
   - Script_CfgInterfaceMain.bat : launch interactively "CfgInterfaceMain.py" that wrap access to the configurator client java API

    USAGE : Script_CfgInterfaceMain.bat
    
    launch the CfgInterface.py core that implement the functions 
    and wrap the java API of the configurator client.

3/ batch script SAMPLE to launch "generateAllCDV.py" python file :
   - Script_generateAllCDV.bat : launch "generateAllCDV.py" jython file

    USAGE : Script_generateAllCDV.bat
    
    sample to generate all CDV except vdc_vide
    do not generate CDV already generated

4/ python script to launch core that implement the functions 
   and wrap the java API of the configurator client. 
   - CfgInterfaceMain.py :

    USAGE : Script.bat -i CfgInterfaceMain.py

    launch the CfgInterface.py core that implement the functions 
    and wrap the java API of the configurator client.
 
5/ python script to generate all CDV 
   - generateAllCDV.py :

    USAGE : Script.bat generateAllCDV.py [-h] [cdvNameExcept]
 
    [-h]		    display this help and exit
    [cdvNameExcept]	cdvNameExcept has not to generate
    
    generate all CDV except cdvNameExcept
    do not generate CDV already generated
    return 
    0  if success
    n  errors count in generateAllCDV

6/ python script to export a CDV 
   - exportCDV.py : 

    USAGE : Script.bat exportCDV.py cdvName exportName
  
    cdvName     name of the CDV
    exportName  name of the export system
  
    export a CDV
    return 
    0 if success
    1 if incorrect parameters
    2 if cdvName doesn't exist
    3 if exportName doesn't exist
    4 if error in exportCDV
 