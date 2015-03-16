#How to checkout project and config it for development in IntelliJ IDEA.

# Необходимые шаги #
  1. Сделайте checkout проекта из svn-хранилища https://extrastore.googlecode.com/svn/trunk/. Это можно сделать с помощью Идеи:
    1. Version Control->Checkout from Version Control->Subversion.
    1. Добавляете указанное выше хранилище, выделяете его мышкой, нажимаете Checkout, создаёте нужную папку (она должна называться extrastore), все остальные настройки оставляете по умолчанию.
    1. После checkout Идея спросит вас, не создать ли проект на базе полученых исходников? Уверенно отвечаем "НЕТ".
  1. Загрузите из раздела Downloads сайта extrastore.googlecode.com папки [lib](http://extrastore.googlecode.com/files/lib.zip) и [bootstrap](http://extrastore.googlecode.com/files/bootstrap.zip) в заархивированом виде. Разархивируйте их в директорию проекта.
  1. Загрузите из Downloads архив [tomcat\_libs.zip](http://extrastore.googlecode.com/files/tomcat_libs.zip) и разархивируйте его содеhжимое в папку tomcat/libs (где 'tomcat' -- ваша инсталляция томката).
  1. Идеей откройте проект.
  1. Создайте настройку Томката, задеплойте туда артефакт extrastore3:dev
  1. Можно работать!

