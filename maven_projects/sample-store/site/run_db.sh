#!/bin/bash

mvn exec:java -e -Dexec.mainClass="org.hsqldb.Server" -Dexec.args="-database.0 file:target/data/ecask -dbname.0 ecask"
