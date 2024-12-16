# Docker 常用命令整理

## 一键操作命令

- **一键启动所有 Docker 容器**
  ```bash
  docker start $(docker ps -a | awk '{print $1}' | tail -n +2)
  ```

- **一键关闭所有 Docker 容器**
  ```bash
  docker stop $(docker ps -a | awk '{print $1}' | tail -n +2)
  ```

- **一键删除所有 Docker 容器**
  ```bash
  docker rm $(docker ps -a | awk '{print $1}' | tail -n +2)
  ```

- **一键删除所有 Docker 镜像**
  ```bash
  docker rmi $(docker images | awk '{print $3}' | tail -n +2)
  ```

## 镜像操作

- **拉取 Docker 镜像**
  ```bash
  docker pull <image_name>
  ```

- **查看宿主机上的镜像**
  ```bash
  docker images
  ```

- **删除指定镜像**
  ```bash
  docker rmi <image_name>:<tag>  
  docker rmi <image_id>
  ```

- **查找 Docker Hub 上的镜像**
  ```bash
  docker search <image_name>
  ```

## 容器操作

- **查看当前正在运行的容器**
  ```bash
  docker ps
  ```

- **查看所有容器（包括停止的）**
  ```bash
  docker ps -a
  ```

- **启动容器**
  ```bash
  docker start <container_name>/<container_id>
  ```

- **停止容器**
  ```bash
  docker stop <container_name>/<container_id>
  ```

- **重启容器**
  ```bash
  docker restart <container_name>/<container_id>
  ```

- **进入正在运行的容器**
  ```bash
  docker attach <container_name>/<container_id>
  ```

- **删除指定容器**
  ```bash
  docker rm <container_name>/<container_id>
  ```

- **删除所有停止的容器**
  ```bash
  docker rm $(docker ps -a -q)
  ```

## 系统信息

- **查看当前系统的 Docker 信息**
  ```bash
  docker info
  ```

- **从 Docker Hub 下载镜像**
  ```bash
  docker pull <image_name>:<tag>
  ```

## 高级操作

- **查看容器详细信息**
  ```bash
  docker inspect <container_name>/<container_id>
  ```

- **复制文件到容器中**
  ```bash
  docker cp <local_path> <container_name>:<container_path>
  ```

- **从容器中复制文件到宿主机**
  ```bash
  docker cp <container_name>:<container_path> <local_path>
  ```

- **提交容器为新的镜像**
  ```bash
  docker commit <container_id> <new_image_name>:<tag>
  ```

- **查看 Docker 网络配置**
  ```bash
  docker network ls
  ```

- **查看容器使用的网络**
  ```bash
  docker network inspect <network_name>
  ```

- **运行交互式容器**
  ```bash
  docker run -it <image_name> /bin/bash
  ```

- **查看容器日志**
  ```bash
  docker logs <container_name>/<container_id>
  ```

- **查看容器的资源使用情况**
  ```bash
  docker stats
  ```
