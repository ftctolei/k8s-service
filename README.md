#### 项目信息

##### 1. 功能

K8sService : 提供部分k8s接口管理服务。



##### 2. 接口

具体参见接口文档 :  k8s-service接口文档_v${yyyymmdd}.md



##### 3. 运行

编译后将`jar`包与`bin/k8sService.sh`脚本放在同一目录下, 确认脚本开头的参数配置正确, 即可执行:

` bash  k8sService.sh  start|stop|status`



- 可将配置文件`config.properties`和`log4j.properties`放在脚本同一目录下, 脚本启动服务时会自动加载配置文件到`jar`包中, 方便在服务部署时更新`jar`包的配置文件.

