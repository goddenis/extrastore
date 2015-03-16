#Hacks (ugly and not)

### Different modules libs issue ###

Solution: move all libs for a project into one dir (ear/lib), and reference them in war through manifest.mf (lies in war/WEB-INF/classes/META-INF).

### NoSuchMethodException while running seam tests ###

`java.lang.NoSuchMethodError: org.jboss.deployers.spi.structure.ContextInfo.getMetaDataPath()Ljava/util/List`

Solution: remove jboss-deployers-client-spi.jar and jboss-deployers-core-spi.jar from classpath, these jars content is already included in jboss-embedded-all.jar. Sometimes bug wont show itself, because jboss-embedded-all.jar stands earlier (and thus is loaded first) in classpath. Here be dragons!


### import.sql or messages.properties data in russian is broken ###
_Solution:_ start tomcat (or JBoss) JVM with `-Dfile.encoding=UTF-8` parameter


### Strange directories, named after entity classes, appear (like ru.extrastore.model.Product) ###

_Explanation_: This is the hibernate index data for entities annotated with `@Indexed`.

_Solution_:  Add the following property to `persistence.xml`:        `<property name="hibernate.search.default.indexBase" value="/home/tomcat/extrastore_index/" />`

### Data is read from database in correct encoding, but written in question marks (???????? ?????) ###

_Solution_: Add encoding parameters to connection string, it should look like this: `jdbc:mysql://localhost:3306/extrastore?useUnicode=true&amp;characterEncoding=utf8`. You can find connection string `in context.xml`, for Tomcat.