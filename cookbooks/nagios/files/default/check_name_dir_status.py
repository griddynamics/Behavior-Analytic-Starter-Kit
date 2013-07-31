#!/usr/bin/env python

import contextlib
import json
from optparse import OptionParser
import sys
import urllib2


def parse_args():
    parser = OptionParser(usage="%prog -H host -p port", version="%prog 1.0")

    parser.add_option("-H", "--host", dest="host", help="host name", metavar="host")
    parser.add_option("-p", "--port", dest="port", help="port number", metavar="port")

    (options, args) = parser.parse_args()
    return options.host, options.port


def check_name_dir_status():

    host, port = parse_args()

    jmx_url = "http://{0}:{1}/jmx?qry=Hadoop:service=NameNode,name=NameNodeInfo".format(host, port)

    with contextlib.closing(urllib2.urlopen(jmx_url)) as response:
        json_line = response.read()

    json_object = json.loads(json_line)

    if "beans" not in json_object or len(json_object["beans"]) != 1:
        raise ValueError("Invalid status from: " + jmx_url)

    dfs_state = json_object["beans"][0]

    if "NameDirStatuses" not in dfs_state:
        raise ValueError("Namenode directory status not available via: " + jmx_url)

    name_dir_statuses = json.loads(dfs_state["NameDirStatuses"])

    if name_dir_statuses["failed"]:
        print "CRITICAL: Offline Namenode directories: ", name_dir_statuses["failed"]
        return 2
    else:
        print "OK: All Namenode directories are active"
        return 0


if __name__ == "__main__":
    try:
        exit_code = check_name_dir_status()
    except Exception, e:
        print "UNKNOWN:", e
        exit_code = 3

    sys.exit(exit_code)
