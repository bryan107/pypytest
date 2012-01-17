cd /d %0\..
set FOLDER=lib
set CP=%CP%;%FOLDER%/activemq-all-5.1.0.jar
set CP=%CP%;%FOLDER%/comm-3.0.0.jar
set CP=%CP%;%FOLDER%/commons-collections-3.2.jar
set CP=%CP%;%FOLDER%/commons-logging-1.1.1.jar
set CP=%CP%;%FOLDER%/jmsStub-1.0.0.jar
set CP=%CP%;%FOLDER%/JsonUtils-1.0.4.jar
set CP=%CP%;%FOLDER%/junit-3.8.1.jar
set CP=%CP%;%FOLDER%/log4j-1.2.14.jar
set CP=%CP%;%FOLDER%/quartz-all-1.6.5.jar
set CP=%CP%;%FOLDER%/s2h.osgi.wrap.org.apache.activemq-1.0.0.jar
java -cp bin;%CP% wsnMessageControl.WSNControlNode

