#!/bin/bash
#
# Note: /usr/local/swig

if [ ! -d /usr/local/swig ]; then
    echo -e "\nERROR /usr/local/swig does not exist\n"
    return 1
fi

./configure --prefix=/usr/local/swig --with-pcre-prefix=/usr/local/swig

make
make install
