@ECHO OFF

CALL LoadPath.bat

cd ../f10/timeSchedule/spring-boot

java -jar ./target/time-schedule-0.0.1-SNAPSHOT.jar

ECHO END