@echo off
cd ..
java -cp ./conf -jar -DPOSP_HOME=%cd% lib/posp-1.0.0.jar
@echo on