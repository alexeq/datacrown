@echo off
@echo Build datacrow-services
rd _classes /S /Q
del standard_services_pack.jar
call ant
move /Y standard_services_pack.jar ..\datacrow-client\services\
