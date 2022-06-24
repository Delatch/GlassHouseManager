SET CLASSPATH="out/production/GlassHouseManager";+libs\amqp-client-5.7.1.jar;+libs\json-20220320.jar;+libs\log4j-api-2.17.2.jar;+libs\jdmkrt.jar;+libs\log4j-core-2.17.2.jar;+libs\activemq-all-5.17.1.jar;+libs\jms-1.1.jar
rem java -cp .;+libs\amqp-client-5.7.1.jar;+libs\json-20220320.jar;+libs\log4j-api-2.17.2.jar;+libs\jdmkrt.jar;+libs\log4j-core-2.17.2.jar;+libs\activemq-all-5.17.1.jar;+libs\jms-1.1.jar glasshousemanager.WeatherChannelAgent
java -cp %CLASSPATH% glasshousemanager/GlassHouseControllerAgent
pause