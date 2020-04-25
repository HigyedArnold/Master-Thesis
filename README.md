# planr
Planning application using: sbt, Scala, Play2, Akka Actors, Cats, OR-Tools, Docker and Swarm.

## How to build and run
Build process with sbt: clean, compile, test compile, extract jars, assembly and create docker image.
Run process with Docker: run docker image or use docker-compose.
In Windows 10 run Cmder (like Command Prompt with shell script support) and execute 'sh build.sh' from the current directory.
In Linux execute './build.sh' from the current directory.

## Performance test results
In the results.7z can be found the results of all performance tests on which the analysis and conclusions will be based.
It contains the following scenarios: local-warmup, docker-warmup and docker performance tests. The focus is mainly on the docker file, where
the tests were run for version 1 (1 threads/app scaled to 4), version 2 (2 threads/app scaled to 2) and version 4 (4 threads/app scaled to 1).
The main comparison is done between version 1 and version 4, version 2 being only a hybrid model of the previous two.

## Copyright and License
All code is available to you under the Apache 2.0 license, available at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0) and also in the [LICENSE](./LICENSE) file.
