:: Installs aFileChooser to local repository as an apklib
:: groupId: com.ipaulpro
:: artifactId: afilechooser
:: version: 1
:: packaging: apklib

mvn install:install-file -Dfile=aFileChooser.apklib -DgroupId=com.ipaulpro -DartifactId=afilechooser -Dversion=1 -Dpackaging=apklib