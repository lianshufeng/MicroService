#!/bin/bash


#wget https://raw.githubusercontent.com/redis/redis/unstable/redis.conf -O $(pwd)/redis.conf  


#允许通知过期事件
sed -i 's/notify-keyspace-events ""/notify-keyspace-events Exg/g' $(pwd)/redis.conf

#允许所有人连接
sed -i 's/bind 127.0.0.1 -::1/bind * -::*/g' $(pwd)/redis.conf


#重启
docker-compose down ; docker-compose up -d