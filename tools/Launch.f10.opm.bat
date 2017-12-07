@ECHO OFF

CALL LoadPath.bat

cd ../f10/opm/spring-boot

java -jar ./target/opm-0.0.1-SNAPSHOT.jar

ECHO END