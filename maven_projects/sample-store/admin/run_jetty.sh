#!/bin/bash

export MAVEN_OPTS="-XX:MaxPermSize=512M -Xmx1024M -Xdebug -Xrunjdwp:transport=dt_socket,address=8001,server=y,suspend=n"
mvn -Druntime.environment=development -Dmaven.compiler.fork=true compile war:exploded jetty:run
