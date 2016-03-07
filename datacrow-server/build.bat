@echo off
@echo Build datacrow-server
rd _classes /S /Q
del datacrow-server.jar
call ant
copy datacrow-server.jar ..\datacrow-client\lib
copy datacrow-server.jar ..\datacrow-beans\lib
