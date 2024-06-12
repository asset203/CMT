#!/bin/bash
cd /export/home/telorb/CCN/
date '+%Y%m%d' > /export/home/telorb/CCN/d
TZ=aaa24 date +%Y%m%d > /export/home/telorb/CCN/d1
TZ=aaa48 date +%Y%m%d > /export/home/telorb/CCN/d2
TZ=aaa72 date +%Y%m%d > /export/home/telorb/CCN/d3
TZ=aaa96 date +%Y%m%d > /export/home/telorb/CCN/d4
printf "A" > /export/home/telorb/CCN/name
printf "A" > /export/home/telorb/CCN/name1
printf "A" > /export/home/telorb/CCN/name2
printf "A" > /export/home/telorb/CCN/name3
printf "A" > /export/home/telorb/CCN/name4
printf `cat /export/home/telorb/CCN/d` >> /export/home/telorb/CCN/name
printf `cat /export/home/telorb/CCN/d1` >> /export/home/telorb/CCN/name1
printf `cat /export/home/telorb/CCN/d2` >> /export/home/telorb/CCN/name2
printf `cat /export/home/telorb/CCN/d3` >> /export/home/telorb/CCN/name3
printf `cat /export/home/telorb/CCN/d4` >> /export/home/telorb/CCN/name4
##############################################################################################################################################################################
cd /export/home/telorb/OCC/OCC1
ls | egrep -v "`cat /export/home/telorb/OCC/OCC1/name`|`cat /export/home/telorb/OCC/OCC1/name1`|`cat /export/home/telorb/OCC/OCC1/name2`|`cat /export/home/telorb/OCC/name3`|`cat /export/hom
e/telorb/OCC/OCC1/name4`" > /export/home/telorb/OCC/OCC1/del
rm `cat /export/home/telorb/OCC/OCC1/del`
##############################################################################################################################################################################
cd /export/home/telorb/CCN/
rm /export/home/telorb/CCN/del /export/home/telorb/CCN/d /export/home/telorb/CCN/d1 /export/home/telorb/CCN/d2 /export/home/telorb/CCN/d3 /export/home/telorb/CCN/d4 /export/home/telorb/CCN/name /export/home/telorb/CCN/name1 /export/home/telorb/CCN/name2 /export/home/telorb/CCN/name3 /export/home/telorb/CCN/name4
