#Backup, restore postgres in docker container
BACKUP_DIR=$HOME/my-backup-path
mkdir -p $BACKUP_DIR

#Backup:
docker exec -t -u postgres $(docker ps --no-trunc --filter name=dev_postgres_1 | \
 awk '$2 ~ /postgres/ {print $1}') pg_dumpall -c | \
 gzip > $BACKUP_DIR/postgres.sql.gz

#Restore:
gunzip -c $BACKUP_DIR/postgres.sql.gz | \
 docker exec -i $(docker ps --no-trunc --filter name=dev_postgres_1 | \
 awk '$2 ~ /postgres/ {print $1}') psql -Upostgres