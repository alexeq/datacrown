@echo off
echo Creation release %VERSION%
java -jar ./staging/dc-server/datacrow-server.jar -credentials:rwaals/nvn126611 -userdir:C:\Users\RJ\Data\Development\dc data -debug
pause