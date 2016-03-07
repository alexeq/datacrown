@rem IzPack variable IZPACK_HOME must be present
@echo Build datacrow-install
call %IZPACK_HOME%\bin\compile.bat installer.xml -b ..\
@echo on
