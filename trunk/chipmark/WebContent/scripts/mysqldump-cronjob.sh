#! /bin/bash
TIME=`date '+%Y-%m-%d'`
mysqldump -u root --password= chipmark > /srv/tomcat6/backup/cm-backup-$TIME.sql
bzip2 -f -9 /srv/tomcat6/backup/cm-backup-$TIME.sql
mysql -u root --password= chipmark -e 'TRUNCATE TABLE log'
mysql -u root --password= chipmark -e 'OPTIMIZE TABLE linktext'
