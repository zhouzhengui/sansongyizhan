#!/bin/bash

#构建有版本号的镜像，以防需要回滚，而且是从镜像创建服务
docker build -t registry-vpc.cn-beijing.aliyuncs.com/lepeng-$1/suninggift-$1:$2     . -f Dockerfile_$1

docker push     registry-vpc.cn-beijing.aliyuncs.com/lepeng-$1/suninggift-$1:$2
#构建latest版本，用于自动部署
docker build -t registry-vpc.cn-beijing.aliyuncs.com/lepeng-$1/suninggift-$1:latest . -f Dockerfile_$1
docker push     registry-vpc.cn-beijing.aliyuncs.com/lepeng-$1/suninggift-$1:latest
