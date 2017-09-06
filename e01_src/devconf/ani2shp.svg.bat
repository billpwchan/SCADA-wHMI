CD /d %~dp0

call scs_env.bat

title ANI2SHAPE

@REM Generate Class Mapping
REM PowerShell -ExecutionPolicy bypass -nologo "%~dp0\gen_class_mapping.ps1"

cd import 

REM New configuration with multiple scs2hv.xml
python ani2img.py -e symbol -c ..\..\devconf\classmapping.csv -m ..\..\devconf\convert\hvscs\M001CONN\scs2hv.xml -e instance -e svg -d ..\..\scspaths\HMI1\ScsVisu\Animator > importHMI1.log 

PowerShell -ExecutionPolicy bypass -File "%~dp0\sanitize_svg.ps1" -path "%~dp0\import\images" -pattern "*_shapes.svg"

PAUSE