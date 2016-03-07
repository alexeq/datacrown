@echo off
@echo Build datacrow-beans
rd _classes /S /Q
call ant
rd ..\datacrow-server\webapp\datacrow\WEB-INF\classes\net /S /Q
xcopy _classes\net ..\datacrow-server\webapp\datacrow\WEB-INF\classes\net\ /S /Y

