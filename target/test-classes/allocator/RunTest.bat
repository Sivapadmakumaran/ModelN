@ECHO OFF

SET REPORTPATH=%~1
SET CURRENTSCENARIO=%~2
SET CURRENTTESTCASE=%~3
SET TESTINSTANCE=%~4
SET CURRENTTESTDESC=%~5
SET PARENTFOLDER=%~6
SET WORKSPACEFOLDER=%~7

@ECHO ON
echo %REPORTPATH%

pushd %userprofile%\%PARENTFOLDER%\%WORKSPACEFOLDER%

java -cp ".;.\target\test-classes\;.\target\libs\*" allocator.QcTestRunner "%REPORTPATH%" "%CURRENTSCENARIO%" "%CURRENTTESTCASE%" "%TESTINSTANCE%" "%CURRENTTESTDESC%" 