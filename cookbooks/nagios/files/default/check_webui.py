#!/usr/bin/env python

import contextlib
from optparse import OptionParser
import sys
import urllib2


def parse_args():
    usage = ("%prog -H host -s service")

    parser = OptionParser(usage=usage, version="%prog 1.0")

    parser.add_option("-s", "--service", dest="service",
        help="service name. Now supported: [namenode|jobtracker|jobhistory|hbase]", metavar="service")
    parser.add_option("-H", "--host", dest="host", help="host name", metavar="host")

    (options, args) = parser.parse_args()
    return options.service, options.host


def check_web_ui():

    service, host = parse_args()

    services = {
        "namenode": ("NameNode web UI", "http://{0}:50070".format(host)),
        "jobtracker": ("JobTracker web UI", "http://{0}:50030".format(host)),
        "jobhistory": ("JobHistory web UI", "http://{0}:51111/jobhistoryhome.jsp".format(host)),
        "hbase": ("HBase Master web UI", "http://{0}:60010/master-status".format(host))
    }

    if service in services:
        (service_name, service_url) = services[service]
        if not service_is_live(service_url):
            print "WARNING: {0} not accessible by {1}".format(service_name, service_url)
            return 1
        else:
            print "OK: Successfully accessed", service_name
            return 0
    else:
        print "Unknown service name:", service
        print "Now supported:", services.keys()
        return 3


def service_is_live(url):
    try:
        with contextlib.closing(urllib2.urlopen(url)) as response:
            return response.getcode() == 200
    except IOError:
        return False


if __name__ == "__main__":
    try:
        exit_code = check_web_ui()
    except Exception, e:
        print "UNKNOWN:", e
        exit_code = 3

    sys.exit(exit_code)
