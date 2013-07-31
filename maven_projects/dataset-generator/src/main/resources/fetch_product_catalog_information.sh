#!/bin/bash

if [ $# -ne 1 ]; then
    echo "Usage: ${0} <server_host> <app_name>"
    echo "Example: ${0} localhost site"
    exit -1
fi

server_host="${1}"
app_name="${2}"
catalog_path="catalog.txt"

curl -o "${catalog_path}" "http://${server_host}:8080/${app_name}/catalog_manager/export"
