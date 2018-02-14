#!/bin/bash
#
# You can configure rabbit in two ways 1) provide a config file, 
# or 2) set env vars.
#
# To configure using a file. set the config file in
# $RABBITMQ_HOME/etc/rabbitmq/rabbitmq-env.conf
#
# This implementation uses option 2, env vars.

SIMPLE_HOME=/usr/local/sbin

# where rabbit is installed
export RABBITMQ_HOME=${SIMPLE_HOME}

# rabbit's database 
export RABBITMQ_MNESIA_BASE=${SIMPLE_HOME}/mnesia

# log files here
export RABBITMQ_LOG_BASE=${SIMPLE_HOME}/logs

# extensions
export RABBITMQ_PLUGINS_EXPAND_DIR=${SIMPLE_HOME}/plugins

# port server listens to (5671 is default)
export RABBITMQ_NODE_PORT=5672

# node name
#export RABBITMQ_NODENAME=

# start the server
${RABBITMQ_HOME}/rabbitmq-server
