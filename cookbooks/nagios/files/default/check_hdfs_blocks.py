#!/usr/bin/env python

import contextlib
import json
from optparse import OptionParser
import sys
import urllib2


def parse_args():
    parser = OptionParser(usage="%prog -H HOST -p PORT -w WARN -c CRIT", version="%prog 1.0")

    parser.add_option("-H", "--host", dest="host", help="host name", metavar="HOST")
    parser.add_option("-p", "--port", dest="port", help="port number", metavar="PORT")
    parser.add_option("-w", "--warning", dest="warning", help="warning percentage", metavar="WARN")
    parser.add_option("-c", "--critical", dest="critical", help="critical percentage", metavar="CRIT")

    (options, args) = parser.parse_args()

    return (
        options.host,
        int(options.port),
        float(options.warning.rstrip(" %")),
        float(options.critical.rstrip(" %"))
    )


def check_hdfs_blocks():

    host, port, warning_percentage, critical_percentage = parse_args()

    jmx_url = "http://{0}:{1}/jmx?qry=Hadoop:service=NameNode,name=FSNamesystemMetrics".format(host, port)

    with contextlib.closing(urllib2.urlopen(jmx_url)) as response:
        json_line = response.read()

    json_object = json.loads(json_line)

    if "beans" not in json_object or len(json_object["beans"]) != 1:
        raise ValueError("Invalid status from: " + jmx_url)

    dfs_state = json_object["beans"][0]

    missing_blocks = dfs_state["MissingBlocks"]
    corrupt_blocks = dfs_state["CorruptBlocks"]
    total_blocks = dfs_state["BlocksTotal"]

    missing_percent = (missing_blocks / total_blocks) * 100 if total_blocks > 0 else 0
    corrupt_percent = (corrupt_blocks / total_blocks) * 100 if total_blocks > 0 else 0

    status_message = "corrupt_blocks:<{0}>, missing_blocks:<{1}>, total_blocks:<{2}>".format(
        corrupt_blocks, missing_blocks, total_blocks)

    if missing_percent > critical_percentage or corrupt_percent > critical_percentage:
        print "CRITICAL:", status_message
        return 2
    elif missing_percent > warning_percentage or corrupt_percent > warning_percentage:
        print "WARNING:", status_message
        return 1
    else:
        print "OK:", status_message
        return 0


if __name__ == "__main__":
    try:
        exit_code = check_hdfs_blocks()
    except Exception, e:
        print "UNKNOWN:", e
        exit_code = 3

    sys.exit(exit_code)
