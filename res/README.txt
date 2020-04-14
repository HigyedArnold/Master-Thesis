Install Google OR-Tools 7.5.7466, Google OR-Tools 7.5.7466 jni and Google Protobuf 3.11.2 in your local .m2 maven repository:

1) mvn install:install-file -Dfile=ortools.jar -DgroupId=com.google -DartifactId=ortools_2.13 -Dversion=7.5.7466 -Dpackaging=jar

2) jar cf jniortools-win.jar natives-win
   jar cf jniortools-lin.jar natives-lin
   
   mvn install:install-file -Dfile=jniortools-win.jar -DgroupId=com.google -DartifactId=jniortools-win_2.13 -Dversion=7.5.7466 -Dpackaging=jar
   mvn install:install-file -Dfile=jniortools-lin.jar -DgroupId=com.google -DartifactId=jniortools-lin_2.13 -Dversion=7.5.7466 -Dpackaging=jar

3) mvn install:install-file -Dfile=protobuf.jar -DgroupId=com.google -DartifactId=protobuf_2.13 -Dversion=3.11.2 -Dpackaging=jar

Documentation: https://google.github.io/or-tools/dotnet/classGoogle_1_1OrTools_1_1ConstraintSolver_1_1Solver.html
Github:        https://github.com/google/or-tools
Release:       https://github.com/google/or-tools/releases/tag/v7.5
