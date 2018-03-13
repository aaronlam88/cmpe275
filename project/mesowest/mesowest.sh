#!/bin/bash
#
# Frequency:
# ~15 minutes data is appended to the file (offset download by 3 min)
#
# Files:
# out.gz is the data
# tbl.gz is the station catalog

wget -N --no-cache \
     http://mesowest.utah.edu/data/mesowest.out.gz \
     http://mesowest.utah.edu/data/mesowest_csv.tbl.gz\
     http://mesowest.utah.edu/data/mesowest.tbl.gz 
