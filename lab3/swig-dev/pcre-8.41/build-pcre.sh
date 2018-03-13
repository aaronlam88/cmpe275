#!/bin/bash
#
# Notes: 
# 1) install is /usr/local/swig
# 2) this is ran in the swig-xx-yy directory

if [ ! -d /usr/local/swig ]; then
    echo -e "\nERROR /usr/local/swig does not exist\n"
    return 1
fi


unzip pcre-8.41.zip
cd pcre-8.4.1

./configure --prefix=/usr/local/swig                       \
            --docdir=/usr/local/swig/share/doc/pcre \
            --enable-unicode                    \
            --enable-pcre-16                   \
            --enable-pcre-32                   \
            --enable-pcregrep-libz             \
            --enable-pcregrep-libbz2           \
            --enable-pcretest-libreadline      \
            --disable-static

make
make install
