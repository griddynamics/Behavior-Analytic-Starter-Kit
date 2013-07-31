
from optparse import OptionParser
import re
import sys


def parse_args():
    usage = ("%prog -f status_file_path -s status_code"
             " (-t host | -t service -n service_name)"
             " -w warn_percent -c crit_percent")

    parser = OptionParser(usage=usage, version="%prog 1.0")

    parser.add_option("-f", dest="status_file", help="status file", metavar="file_path")
    parser.add_option("-s", dest="status_code", help="status code", metavar="status_code")
    parser.add_option("-n", dest="service_name", help="write report to FILE", metavar="service_name")
    parser.add_option("-w", dest="warning", help="warning percentage", metavar="percent")
    parser.add_option("-c", dest="critical", help="critical percentage", metavar="percent")
    parser.add_option("-t", dest="type", help="type of check (service or host)", metavar="type")

    (options, args) = parser.parse_args()

    return (
        options.status_file,
        options.status_code,
        options.type,
        options.service_name,
        options.warning,
        options.critical,
    )


def check_aggregate():

    status_file, \
    status_code, \
    check_type, \
    service_name, \
    warning_percentage, \
    critical_percentage  = parse_args()

    with open(status_file, "r") as file:
        status_file_content = file.read()

    if check_type == "service":
        counts = query_alert_count(status_file_content, service_name, status_code)
    else:
        counts = query_host_count(status_file_content, status_code)

    if counts["total"] == 0:
        percent = 0
    else:
        percent = (counts["actual"] / counts["total"]) * 100

    stat_message = "total: <{0}>, affected: <{1}>".format(counts["total"], counts["affected"])

    if percent >= critical_percentage:
        print "CRITICAL:", stat_message
        return 2
    elif percent >= warning_percentage:
        print "WARNING:", stat_message
        return 1
    else:
        print "OK:", stat_message
        return 0


def query_alert_count(status_file_content, service_name, status_code):
    counters = {
        "total": 0,
        "actual": 0
    }

    pattern = "/servicestatus \{([\S\s]*?)\}/"
    matches = re.findall(pattern, status_file_content)

    for object in matches:
        if get_parameter(object, "service_description") == service_name:
            counters["total"] += 1
            if get_parameter(object, "current_state") >= status_code:
                counters["actual"] += 1

    return counters


def query_host_count(status_file_content, status_code):
    counters = {
        "total": 0,
        "actual": 0
    }

    pattern = "/hoststatus \{([\S\s]*?)\}/"
    matches = re.findall(pattern, status_file_content)

    for object in matches:
        counters["total"] += 1
        if get_parameter(object, "current_state") == status_code:
            counters["actual"] += 1

    return counters


def get_parameter(object, key):
    pattern="/\s" + key + "[\s= ]*([\S, ]*)\n/"

    m = re.match(pattern, object)

    if m is not None:
        return m.group(1)

    return ""


if __name__ == "__main__":
    return_code = check_aggregate()
    sys.exit(return_code)
