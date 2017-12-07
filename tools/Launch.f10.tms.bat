@ECHO OFF

CALL LoadPath.bat

cd ../f10/tms/spring-boot

java -jar ./target/tms-0.0.1-SNAPSHOT.jar

ECHO END