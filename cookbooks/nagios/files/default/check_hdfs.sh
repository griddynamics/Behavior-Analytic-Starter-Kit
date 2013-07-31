#!/bin/bash

timeout 5 hadoop fs -ls / > /dev/null  2>&1

if [[ $? -ne 0 ]]; then
    echo "CRITICAL: hdfs is not available"
    exit 2
else
    echo "OK: hdfs is available"
    exit 0
fi
