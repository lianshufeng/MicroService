#!/bin/bash


#目录权限
mkdir -p ./data/node0
mkdir -p ./data/node1
mkdir -p ./data/node2
chmod -R 777 ./data


# 重启服务
docker-compose down ; docker-compose up -d