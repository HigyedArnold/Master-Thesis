Install Google OR-Tools 7.5.7466, Google OR-Tools 7.5.7466 jni and Google Protobuf 3.11.2 in your local .m2 maven repository:

1) mvn install:install-file -Dfile=ortools.jar -DgroupId=com.google -DartifactId=ortools_2.13 -Dversion=7.5.7466 -Dpackaging=jar

2) jar cf jniortools.jar natives (structure is: natives->windows_64/linux_64->lib files from ortools 7.5.7466) with -Dclassifier=windows-x64/linux-18.04-x64
   mvn install:install-file -Dfile=jniortools.jar -DgroupId=com.google -DartifactId=jniortools_2.13 -Dversion=7.5.7466 -Dpackaging=jar
      
3) mvn install:install-file -Dfile=protobuf.jar -DgroupId=com.google -DartifactId=protobuf_2.13 -Dversion=3.11.2 -Dpackaging=jar

Documentation: http://google.github.io/or-tools/java/namespacecom_1_1google_1_1ortools_1_1sat.html
Github:        https://github.com/google/or-tools
Release:       https://github.com/google/or-tools/releases/tag/v7.5
