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
######################################################################################################################
cd /export/home/telorb/CCN/ECCN1/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/ECCN1/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/ECCN2/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/ECCN2/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/ECCN3/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/ECCN3/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/ECCN4/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/ECCN4/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN3/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN3/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN4/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN4/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN5/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN5/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN6/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN6/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN7/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN7/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN8/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN8/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN9/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN9/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN10/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN10/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN11/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN11/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN12/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN12/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN13/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN13/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN14/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN14/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN15/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN15/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
############################################################################################################################################################################################################################################
cd /export/home/telorb/CCN/DataCCN16/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/hom
e/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN16/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/hom
e/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
##############################################################################################################################################################################
##############################################################
cd /export/home/telorb/CCN/DataCCN17/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/hom
e/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN17/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/hom
e/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
##############################################################################################################################################################################
##############################################################
cd /export/home/telorb/CCN/DataCCN18/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/hom
e/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN18/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/hom
e/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
##############################################################################################################################################################################
cd /export/home/telorb/CCN/DataCCN19/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/hom
e/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/DataCCN19/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/hom
e/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
##############################################################################################################################################################################
cd /export/home/telorb/OCC/OCC1
ls | egrep -v "`cat /export/home/telorb/OCC/OCC1/name`|`cat /export/home/telorb/OCC/OCC1/name1`|`cat /export/home/telorb/OCC/OCC1/name2`|`cat /export/home/telorb/OCC/name3`|`cat /export/hom
e/telorb/OCC/OCC1/name4`" > /export/home/telorb/OCC/OCC1/del
rm `cat /export/home/telorb/OCC/OCC1/del`
##############################################################################################################################################################################
cd /export/home/telorb/CCN/CCN21/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN21/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN22/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN22/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN23/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN23/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN24/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN24/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN25/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN25/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN26/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN26/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN27/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN27/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN28/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN28/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN31/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN31/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN32/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN32/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN33/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN33/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/home/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN34/CcnCounters
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/hom
e/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del`
######################################################################################################################
cd /export/home/telorb/CCN/CCN34/PlatformMeasures
ls | egrep -v "`cat /export/home/telorb/CCN/name`|`cat /export/home/telorb/CCN/name1`|`cat /export/home/telorb/CCN/name2`|`cat /export/home/telorb/CCN/name3`|`cat /export/hom
e/telorb/CCN/name4`" > /export/home/telorb/CCN/del
rm `cat /export/home/telorb/CCN/del
######################################################################################################################
cd /export/home/telorb/CCN/
rm /export/home/telorb/CCN/del /export/home/telorb/CCN/d /export/home/telorb/CCN/d1 /export/home/telorb/CCN/d2 /export/home/telorb/CCN/d3 /export/home/telorb/CCN/d4 /export/home/telorb/CCN/name /export/home/telorb/CCN/name1 /export/home/telorb/CCN/name2 /export/home/telorb/CCN/name3 /export/home/telorb/CCN/name4
