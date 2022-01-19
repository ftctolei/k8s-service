#### 一. K8s-service接口说明 ####

本接口文档为K8s-service REST接口说明, 提供k8s接口查询服务.



#### 二. K8s-service Restful接口格式 #### 

##### 0. 测试服务接口连通性

请求地址:

`GET  /v1/k8sservice/test`

返回示例:

```json
{
	"code": 200,
	"message": "[INFO] oops, you got it.",
	"responseTime": "2022-01-11 15:14:12"
}
```



##### 1.  根据namespace,label查询启动的pod列表 #####

请求地址：

 `GET  /v1/k8sservice/getPod`



请求参数：

| 参数名         | 类型   | 必选 | 备注                                 |
| -------------- | ------ | ---- | ------------------------------------ |
| msgId          | String | Y    | 消息唯一id, 建议使用时间戳, 原值返回 |
| nameSpace      | String | Y    | nameSpace名称                        |
| deploymentName | String | Y    | deployment名称                       |



请求示例:

`GET  /v1/k8sservice/getPod?msgId=1641876953&namespace=vss-meter-workspace&deploymentname=app=mysql`

返回示例:

```json
{
	"code": 200,
	"message": {
		"clusterIP": "10.105.227.117",
		"clusterPort": 3306,
		"podList": [
		{
			"podIP": "10.244.0.33",
			"podName": "mysql"
		},
		{
			"podIP": "10.244.1.25",
			"podName": "mysql2"
		}
		]
	},
"msgId": "1641876953",
"responseTime": "2022-01-11 12:56:04"
}
```



##### 2. 根据namespace,deployment Name更新pod的replicas #####

请求地址：

`POST  /v1/k8sservice/updatePodReplicas`



请求参数：
| 参数名         | 类型   | 必选 | 备注                                 |
| -------------- | ------ | ---- | ------------------------------------ |
| msgId          | String | Y    | 消息唯一id, 建议使用时间戳, 原值返回 |
| nameSpace      | String | Y    | nameSpace名称                        |
| deploymentName | String | Y    | deployment名称                       |
| replicas       | int    | Y    | 副本数                               |




请求示例:

`POST  /v1/k8sservice/updatePodReplicas`

```shell
# x-www-form-urlencoded
nameSpace:vss-meter-workspace
deploymentName:mariadb
replicas:3
msgId:1641876953
```



返回示例:

```json
{
    "code": 200,
    "message": {
        "deploymentName": "mariadb",
        "nameSpace": "vss-meter-workspace",
        "replicas": 3
    },
    "msgId": "1641876953",
    "responseTime": "2022-01-11 12:46:27"
}
```



##### 3. 根据namespace,上传的yaml文件创建Deployment,Pod,Service #####

请求地址：

`POST  /v1/k8sservice/updatePodViaYaml`



请求参数：

| 参数名    | 类型   | 必选 | 备注                                                         |
| --------- | ------ | ---- | ------------------------------------------------------------ |
| msgId     | String | Y    | 消息唯一id, 建议使用时间戳, 原值返回                         |
| nameSpace | String | Y    | nameSpace名称                                                |
| op        | String | Y    | 接口操作, 定义如下:  1. createDeployment: 创建Deployment;2.createPod:创建Pod；3.createService:创建Service |
| yamlFile  | File   | Y    | FORM_DATA上传的文件                                          |

请求示例1 创建deployment:

`POST  /v1/k8sservice/updatePodViaYaml`

```shell
# form-data
nameSpace:vss-meter-workspace
op:createDeployment
msgId:1641872394
yamlFile: [上传以下create_pod.yaml文件]
```

参数`create_deployment.yaml` 文件内容参考如下:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  selector:
    matchLabels:
      app: nginx-ljj
  replicas: 2
  template:
    metadata:
      labels:
        app: nginx-ljj
    spec:
      containers:
      - name: nginx
        image: nginx:1.14.2
        ports:
        - containerPort: 80
      
```



返回示例:

```json
{
    "code": 200,
    "message": {
        "deploymentName": "nginx-deployment",
        "nameSpace": "vss-meter-workspace",
        "replicas": 2
    },
    "msgId": "1641872394",
    "responseTime": "2022-01-17 11:20:27"
}
```



重复创建返回示例:

```json
{
    "code": 201,
    "message": "[ERROR] Create namespaced deployment failed: Conflict",
    "msgId": "1641872394",
    "responseTime": "2022-01-17 11:27:50"
}
```



请求示例2 创建pod:

`POST  /v1/k8sservice/updatePodViaYaml`

```
# form-data
nameSpace:vss-meter-workspace
op:createPod
msgId:1641872394
yamlFile: [上传以下create_pod.yaml文件]
```

参数`create_pod.yaml` 文件内容参考如下:

```yaml
apiVersion: v1 # API版本号，注意：具有多个，不同的对象可能会使用不同API
kind: Pod  # 对象类型，pod
metadata:  # 元数据
  name: mysql # POD名称
spec: # specification of the resource content(资源内容的规范)
  containers: # 容器列表
    - name: mysql # 容器名称
      image: mysql # 容器镜像
```



返回示例:

```
{
    "code": 200,
    "message": {
        "nameSpace": "vss-meter-workspace",
        "podName": "mysql"
    },
    "msgId": "1002",
    "responseTime": "2022-01-19 09:17:53"
}
```

重复创建返回示例:

```
{
    "code": 201,
    "message": "[ERROR] Create namespaced Pod failed: Conflict",
    "msgId": "1002",
    "responseTime": "2022-01-19 09:18:16"
}
```





请求示例3 创建service

`POST  /v1/k8sservice/updatePodViaYaml`

```
# form-data
nameSpace:vss-meter-workspace
op:createService
msgId:1641872394
yamlFile: [上传以下create_service.yaml文件]
```

参数`create_service.yaml` 文件内容参考如下:

```
apiVersion: v1
kind: Service
metadata:
  name: mariadb
  labels:
    app: mariadb
    service: mariadb
spec:
  ports:
    - name: http
      port: 8080
      targetPort: 8080
      nodePort: 30026
  selector:
    app: mariadb
  type: NodePort
```

返回示例:

```
{
    "code": 200,
    "message": {
        "clusterPort": 3306,
        "nameSpace": "vss-meter-workspace",
        "serviceName": "mariadb"
    },
    "msgId": "1002",
    "responseTime": "2022-01-19 09:22:07"
}
```

重复创建返回示例:

```
{
    "code": 201,
    "message": "[ERROR] Create namespaced Service failed: Unprocessable Entity",
    "msgId": "1002",
    "responseTime": "2022-01-19 09:23:03"
}
```



##### 4.根据namespace,deploymentname,删除对应的deployment

请求地址：

`POST  /v1/k8sservice/deleteDeployment`



请求参数：

| 参数名         | 类型   | 必选 | 备注                                 |
| -------------- | ------ | ---- | ------------------------------------ |
| msgId          | String | Y    | 消息唯一id, 建议使用时间戳, 原值返回 |
| nameSpace      | String | Y    | nameSpace名称                        |
| deploymentName | String | Y    | deployment名称                       |

请求示例：

`/v1/k8sservice/deleteDeployment`

```
# x-www-form-urlencoded
nameSpace:vss-meter-workspace
deploymentName:mariadb
msgId:1641876953
```

返回示例:

```
{
    "code": 200,
    "message": {
        "deploymentName": "mariadb",
        "nameSpace": "vss-meter-workspace",
        "replicas": 0
    },
    "msgId": "1641872394",
    "responseTime": "2022-01-18 18:59:08"
}
```

重复创建返回示例:

```
{
    "code": 201,
    "message": "Request ERROR, Check your request and parameters. ",
    "msgId": "1002",
    "responseTime": "2022-01-18 18:59:41"
}
```



#### 三. 返回码定义 ####

| code值 | 定义                       | 备注                     |
| ------ | -------------------------- | ------------------------ |
| 200    | 接口服务正常, 查询返回正常 | code: 2xx 为接口服务正常 |
| 201    | 接口服务正常但查询出错     |                          |
| 500    | 接口服务查询错误           | code: 5xx 为接口查询异常 |

##### 1. 返回码错误示例 ###

1. code:201

   请求示例:

   `GET  /v1/k8sservice/getPod?msgId=1641876953&namespace=vss-meter-workspace`

   返回示例:

   ```json
   {
   	"code": 201,
   	"message": "[ERROR] mandatory parameters missed. ",
   	"msgId": "1641876953",
   	"responseTime": "2022-01-11 13:00:28"
   }
   ```

   

2. code:500

   请求示例:

   `当k8s-service服务查询异常时任意请求接口`

   返回示例:

   ```json
   {
   	"code": 500,
   	"message": "[ERROR] K8sRestService error, check your request and parameters first, or contact the administrator. ",
   	"msgId": "1641876953",
   	"responseTime": "2022-01-11 13:01:29"
   }
   ```

   

