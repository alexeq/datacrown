@echo off
echo Creation release %1
pause
REM ..
REM ## Build the individual packages
cd ..\datacrow-client\help\en\
call create_index.bat
cd ..\..
cd ..\datacrow-core
call build.bat
rem pause
cd ..\datacrow-services
call build.bat
rem pause
cd ..\datacrow-server
call build.bat
rem pause
cd ..\datacrow-beans
call build.bat
rem pause
cd ..\datacrow-client
call build.bat
rem pause
cd ..\datacrow-plugins
call build.bat
rem pause
cd ..\datacrow-build
REM ..
REM ## Create the staging area
rd .\staging /S /Q
md .\staging\dc-client
md .\staging\dc-server
md .\staging\dc-client\services
md .\staging\dc-client\lib
md .\staging\dc-client\fonts
md .\staging\dc-client\plugins
md .\staging\dc-client\plugins\discid
md .\staging\dc-server\lib
md .\staging\dc-server\plugins
md .\staging\dc-server\webapp
md .\staging\dc-server\modules
REM ..
REM ## Copy the files from the various packages
move ..\datacrow-server\datacrow-server.jar staging\dc-server\
copy ..\datacrow-client\datacrow.jar staging\dc-client\
xcopy ..\datacrow-client\plugins\discid staging\dc-client\plugins\discid\ /S /Y
xcopy ..\datacrow-core\lib staging\dc-client\lib\ /S /Y
xcopy ..\datacrow-core\lib staging\dc-server\lib\ /S /Y
xcopy ..\datacrow-client\lib staging\dc-client\lib\ /S /Y
xcopy ..\datacrow-client\upgrade staging\dc-client\upgrade\ /S /Y
xcopy ..\datacrow-client\fonts staging\dc-client\fonts\ /S /Y
xcopy ..\datacrow-server\upgrade staging\dc-client\upgrade\ /S /Y
xcopy ..\datacrow-client\readme staging\dc-client\readme\ /S /Y
xcopy ..\datacrow-client\readme staging\dc-server\readme\ /S /Y
xcopy ..\datacrow-server\lib staging\dc-client\lib\ /S /Y
xcopy ..\datacrow-server\lib staging\dc-server\lib\ /S /Y
xcopy ..\datacrow-services\lib staging\dc-client\lib\ /S /Y
copy ..\datacrow-plugins\_classes\plugins\*.class .\staging\dc-client\plugins\ /Y
xcopy ..\datacrow-client\help staging\dc-client\help\ /S
xcopy ..\datacrow-client\icons staging\dc-client\icons\
xcopy ..\datacrow-server\icons staging\dc-server\icons\
xcopy ..\datacrow-server\webapp staging\dc-server\webapp\ /S /Y
xcopy ..\datacrow-client\modules staging\dc-client\modules\ /S
xcopy ..\datacrow-client\modules staging\dc-server\modules\ /S
xcopy ..\datacrow-client\plugins\discid staging\dc-client\plugins\discid\ /S /Y
xcopy ..\datacrow-client\reports staging\dc-client\reports\ /S
xcopy ..\datacrow-client\resources staging\dc-client\resources\ /S
xcopy ..\datacrow-client\resources staging\dc-server\resources\ /S
xcopy ..\datacrow-client\services staging\dc-client\services\ /S
copy ..\datacrow-client\datacrow32bit.* staging\dc-client\
copy ..\datacrow-client\datacrow64bit.* staging\dc-client\
copy ..\datacrow-client\log4j.properties staging\dc-client\
copy ..\datacrow-server\log4j.properties staging\dc-server\
copy ..\datacrow-server\datacrow-server32bit.* staging\dc-server\
copy ..\datacrow-server\datacrow-server64bit.* staging\dc-server\
rem pause
REM ..
REM ## Change the file properties within the staging area
chmod -R +r+w ./staging/*
cd ..\datacrow-install
call create-installer.bat
7z a -tZip datacrow_%1_installer installer.jar installer.sh readme.txt
7z a -tZip datacrow_%1_windows_installer installer.jar installer.sh readme.txt setup32bit.exe setup64bit.exe
move datacrow_%1_installer.zip ..\datacrow-build\staging\
move datacrow_%1_windows_installer.zip ..\datacrow-build\staging\
cd ..\datacrow-build\staging
7z a -tZip datacrow_%1_client_zipped .\dc-client
7z a -tZip datacrow_%1_server_zipped .\dc-server
cd ..
cd ..
7z a -tZip datacrow_%1_source .\datacrow-client .\datacrow-core .\datacrow-server
move datacrow_%1_source.zip .\datacrow-build\staging\
cd .\datacrow-build\staging\
rem pause