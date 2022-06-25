REM *********************************************************
REM ******       Lancement du WeatherChannelAgent      ******
REM *********************************************************
SET CLASSPATH="out/production/GlassHouseManager";+libs\activemq-client-5.17.1.jar;+libs\json-20220320.jar;+libs\log4j-api-2.17.2.jar;+libs\jdmkrt.jar;+libs\log4j-core-2.17.2.jar;+libs\geronimo-j2ee-management_1.1_spec-1.0.1.jar;+libs\jms-1.1.jar;+libs\geronimo-jms_1.1_spec-1.1.1.jar;+libs\hawtbuf-1.11.jar;+libs\log4j-slf4j-impl-2.17.2.jar;+libs\slf4j-api-1.7.36.jar;+libs\org.slf4j.jar
REM arg[0]=délai entre les requêtes en ms, arg[1]=ville observée
java -cp %CLASSPATH% glasshousemanager/WeatherChannelAgent 20000 "Lorient"
pause