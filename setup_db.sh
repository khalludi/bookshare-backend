#!/bin/bash
#echo "Wait for servers to be up"
#sleep 10

HOSTPARAMS="--host db --insecure"
SQL="/cockroach/cockroach.sh sql $HOSTPARAMS"

$SQL -e "CREATE DATABASE testdb;"
$SQL -e "CREATE USER user17;"
$SQL -e "GRANT ALL ON DATABASE testdb TO user17"
