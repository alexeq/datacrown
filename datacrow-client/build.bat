@echo off
@echo Build datacrow-client
rd _classes /S /Q
del datacrow.jar
call ant
copy datacrow.jar ..\datacrow-plugins\lib
