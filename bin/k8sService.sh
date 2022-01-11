#!/bin/bash                                                                                                                      op@10.142.97.1
#  K8s-service 服务 启动|停止|状态 控制脚本
#  本服务提供部分k8s接口管理服务.

jar_file="K8sService.jar"
config_properties="config.properties"
log4j_properties="log4j.properties"
JAVA_CMD="/usr/bin/java "
JAR_CMD="/usr/bin/jar "
JAVA_ARGS="-Xmx10g "
JAR_MAIN_CLASS="cn.chinatelecom.kubernetes.StartService"

displayHelp(){
    echo -e  ""
    echo -e  "Usage:  bash  $0  start|stop|status"
    echo -e  "    K8s-service  服务 启动|停止|状态 ."
    echo -e  "    本服务提供部分k8s接口管理服务."
    echo -e  ""
}


startService(){
    pid=`/bin/ps  -ef |grep  ${jar_file} |grep -v grep  |awk -F ' ' '{print $2}'`
    if [ "${pid}"  != "" ];then
        echo "服务已在运行, 请先停止."
        statusService
        exit 0
    fi
    ${JAR_CMD}  -uf   ${jar_file}   ${config_properties}
    ${JAR_CMD}  -uf   ${jar_file}   ${log4j_properties}
    nohup ${JAVA_CMD}  ${JAVA_ARGS}  -jar  ${jar_file}  ${JAR_MAIN_CLASS} > /dev/null 2>&1 &
}


stopService(){
    /bin/ps  -ef |grep  ${jar_file} |grep -v grep
    pid=`/bin/ps  -ef |grep  ${jar_file} |grep -v grep  |awk -F ' ' '{print $2}'`
    echo "PID: ${pid}"
    if [ "${pid}" != "" ] ;then
        /bin/kill  -9  ${pid}
    fi
}


statusService(){
    echo "/bin/ps  -ef |grep  ${jar_file} |grep -v grep"
    /bin/ps  -ef |grep  ${jar_file} |grep -v grep
    pid=`/bin/ps  -ef |grep  ${jar_file} |grep -v grep  |awk -F ' ' '{print $2}'`
    if [ "${pid}"  != "" ];then
        echo "[runing]服务正在运行, PID: ${pid}"
    else
        echo "[NOT runing]未发现服务进程."
    fi
    echo  -e ""
}


if [ $# != 1 ] ; then
    displayHelp
    exit 0
fi

op="$1"
case $op in
    start)
        echo "Start K8sService ..."
        startService
	echo "finished ."
        ;;
    stop)
        echo "Stop K8sService ..."
        stopService
	echo "finished ."
        ;;
    status)
        echo "K8sService status, (ps -ef)"
        statusService
        ;;
     *)
        displayHelp
        exit 0
        ;;
esac
