#How to config tomcat to run several seam apps at a time

Also allows to use apps with totally empty WEB-INF/lib dir. Makes them to copy faster.

  * Make dir tomcat/lib/seam
  * Move jboss-seam`*`.jar files there (commonly, jboss-seam.jar, jboss-seam-debug.jar, jboss-seam-ui.jar).
  * Move all other necessary libs to tomcat/lib (WEB-INF/lib folder of the apps must be completely empty).
  * Make the following change in tomcat/conf/catalina.properties file: `shared.loader=${catalina.home}/lib/seam/*.jar,${catalina.base}/lib/seam/*.jar`
  * Reboot