REM *********************************************************
REM ******       Lancement du WeatherChannelAgent      ******
REM *********************************************************
SET CLASSPATH="out/production/GlassHouseManager";+libs\amqp-client-5.7.1.jar;+libs\json-20220320.jar;+libs\log4j-api-2.17.2.jar;+libs\jdmkrt.jar;+libs\log4j-core-2.17.2.jar;+libs\activemq-all-5.17.1.jar;+libs\jms-1.1.jar
REM arg[0]=d�lai entre les requ�tes en ms, arg[1]=ville observ�e
java -cp %CLASSPATH% glasshousemanager/WeatherChannelAgent 20000 "Lorient"
pause