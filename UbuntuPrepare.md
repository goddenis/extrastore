## Prepate repositories ##

sudo aptitude update

sudo aptitude safe-upgrade

apt-get install python-software-properties

sudo add-apt-repository "deb http://archive.canonical.com/ lucid partner"

sudo apt-get update


## Install JDK ##


sudo apt-get install sun-java6-jdk -y

sudo update-java-alternatives -s java-6-sun

sudo vi /etc/environment


>>

```
JDK_HOME="/usr/lib/jvm/java-6-sun"
JAVA_HOME="/usr/lib/jvm/java-6-sun"
```



reboot


## Install Tomcat ##


Ставим из репозитория.

sudo apt-get install tomcat6




Либо ставим бинарник.

cd /opt/

wget http://www.sai.msu.su/apache/tomcat/tomcat-6/v6.0.32/bin/apache-tomcat-6.0.32.tar.gz

tar xvzf http://www.sai.msu.su/apache/tomcat/tomcat-6/v6.0.32/bin/apache-tomcat-6.0.32.tar.gz

sudo ln -s /opt/apache-tomcat-6.0.32 /opt/apache-tomcat

sudo adduser --system tomcat

sudo chown -RLv tomcat:nogroup /opt/apache-tomcat

Запускаем на порту 8080

sudo -u tomcat /opt/apache-tomcat/bin/catalina.sh run

правим /opt/apache-tomcat/conf/server.xml



&lt;connector port="80" protocol="HTTP/1.1" connectiontimeout="20000" redirectport="8443" URIEncoding="UTF-8"&gt;



&lt;/connector&gt;



sudo apt-get install jsvc -y

мш /etc/init.d/tomcat


---

```
#!/bin/sh

JAVA_HOME=/usr/lib/jvm/java-6-sun
CATALINA_HOME=/opt/apache-tomcat
TOMCAT_USER=tomcat



TMP_DIR=/var/tmp
PID_FILE=/var/run/apache-tomcat.pid
CATALINA_BASE=/opt/apache-tomcat



CATALINA_OPTS="-Xmx1024m -Xms512m -XX:MaxPermSize=128m -Djava.awt.headless=true"

CLASSPATH=\
$JAVA_HOME/lib/tools.jar:\
$CATALINA_HOME/bin/commons-daemon.jar:\
$CATALINA_HOME/bin/tomcat-juli.jar:\
$CATALINA_HOME/bin/bootstrap.jar



case "$1" in
start)

#
# Start Tomcat
#

jsvc \
-user $TOMCAT_USER \
-home $JAVA_HOME \
-Dcatalina.home=$CATALINA_HOME \
-Dcatalina.base=$CATALINA_BASE \
-Djava.io.tmpdir=$TMP_DIR \
-Dfile.encoding=UTF-8 \
-Duser.timezone=Europe/Samara \
-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager \
-Djava.util.logging.config.file=$CATALINA_BASE/conf/logging.properties \
-wait 10 \
-pidfile $PID_FILE \
-outfile $CATALINA_HOME/logs/catalina.out \
-errfile '&1' \
$CATALINA_OPTS \
-cp $CLASSPATH \
org.apache.catalina.startup.Bootstrap

#
# To get a verbose JVM
#-verbose \
# To get a debug of jsvc.
#-debug \

exit $?
;;


stop)

#
# Stop Tomcat
#

jsvc \
-stop \
-pidfile $PID_FILE \
org.apache.catalina.startup.Bootstrap

exit $?
;;

restart)

#
# Restart Tomcat
#

$0 stop
sleep 2
$0 start
;;

*)

echo "Usage tomcat start/stop"
exit 1;;

esac
```

---




sudo chown root:root /etc/init.d/tomcat

sudo chmod 755 /etc/init.d/tomcat



sudo /etc/init.d/tomcat start



sudo update-rc.d tomcat defaults


## Install MySQL ##