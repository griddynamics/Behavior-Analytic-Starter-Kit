#
# Cookbook Name:: yumrepo
# Attributes:: postgresql
#
# Copyright 2010, Tippr, Inc.
# Copyright 2012, Bryan W. Berry (<bryan.berry@gmail.com>)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

default['repo']['postgresql']['version'] = 9.0
default['repo']['postgresql']['url'] = "http://yum.pgrpms.org/#{node['repo']['postgresql']['version']}/redhat/rhel-$releasever-$basearch"
default['repo']['postgresql']['key'] = "RPM-GPG-KEY-PGDG"
