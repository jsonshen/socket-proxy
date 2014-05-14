@echo off
cd ..
java -cp ./conf -jar -Dlisten=8888 -Dhost=172.16.102.142 -Dport=8 lib/socketproxy-1.0.0.jar
@echo on