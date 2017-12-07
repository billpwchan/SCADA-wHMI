@ECHO OFF

CALL LoadPath.bat

cd ../f10/autologout/spring-boot

java -jar ./target/autologout-0.0.1-SNAPSHOT.jar

ECHO END