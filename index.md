```
扩展：docker 一键启动

一键启动所有docker 容器：docker start $(docker ps -a | awk '{ print $1}' | tail -n +2)

一键关闭所有docker 容器：docker stop $(docker ps -a | awk '{ print $1}' | tail -n +2)

一键删除所有docker 容器：docker rm $(docker ps -a | awk '{ print $1}' | tail -n +2)

一键删除所有docker 镜像: docker rmi $(docker images | awk '{print $3}' |tail -n +2)
```
