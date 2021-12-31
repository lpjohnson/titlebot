FROM hseeberger/scala-sbt:8u312_1.5.8_2.13.7

ADD				. /opt/app

WORKDIR			/opt/app

ENV				JAVA_OPTS "-Xmx500M"
ENTRYPOINT 	    ["sbt"]
