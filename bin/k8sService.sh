#!/bin/sh
#  K8s-service 服务 启动|停止|状态 控制脚本
#  本服务提供部分k8s接口管理服务.

jar_version="0.1"
jar_file="libs/k8s-service-${jar_version}.jar"
config_properties="config.properties"
log4j_properties="log4j.properties"
JAVA_CMD="/usr/bin/java "
JAR_CMD="/usr/bin/jar "
JAVA_ARGS="-Xmx4g "


displayHelp(){
cat << HelpContent
  USAGE:

      K8s-service 服务 启动|停止|状态 控制脚本.
      执行: sh  $0  start|stop|status

      本服务提供部分k8s接口管理服务.

HelpContent
exit 0
}


logPrint(){
    log_type=${1:-"info"}
    log_content=${2:-"no log content."}
    case ${log_type} in
    error)
        echo -e "$(date +'%F %H:%M:%S')[ERROR] ${log_content}";;
    info)
        echo -e "$(date +'%F %H:%M:%S')[INFO] ${log_content}";;
    warn)
        echo -e "$(date +'%F %H:%M:%S')[WARN] ${log_content}" ;;
    debug)
        echo -e "$(date +'%F %H:%M:%S')[DEBUG] ${log_content}" ;;
    esac
}


startService(){
    pid=$(/bin/ps  -ef |grep  ${jar_file} |grep -v grep  |awk -F ' ' '{print $2}')
    if [[ "${pid}"  != "" ]];then
        logPrint info "服务已在运行, 请先停止."
        statusService
        exit 0
    fi
    if [[ -f  ${config_properties} ]];then
        logPrint info  "加载配置文件: ${config_properties}"
        ${JAR_CMD}  -uf   ${jar_file}   ${config_properties}
    fi
    if [[ -f  ${log4j_properties} ]];then
         logPrint info "加载配置文件: ${log4j_properties}"
        ${JAR_CMD}  -uf   ${jar_file}   ${log4j_properties}
    fi
    nohup ${JAVA_CMD}  ${JAVA_ARGS}  -jar  ${jar_file}  > /dev/null 2>&1 &
}


stopService(){
    /bin/ps  -ef |grep  ${jar_file} |grep -v grep
    pid=$(/bin/ps  -ef |grep  ${jar_file} |grep -v grep  |awk -F ' ' '{print $2}')
    logPrint info  "PID: ${pid}"
    if [[ "${pid}" != "" ]] ;then
        /bin/kill  -9  "${pid}"
    fi
}


statusService(){
    logPrint info  "/bin/ps  -ef |grep  ${jar_file} |grep -v grep"
    /bin/ps  -ef |grep  ${jar_file} |grep -v grep
    pid=$(/bin/ps  -ef |grep  ${jar_file} |grep -v grep  |awk -F ' ' '{print $2}')
    if [[ "${pid}"  != "" ]];then
        logPrint info  "[RUNNING]服务正在运行, PID: ${pid}"
    else
        logPrint info  "[NOT RUNNING]未发现服务进程."
    fi
    echo  -e ""
}


if [[ $# != 1 ]] ; then
    displayHelp
    exit 0
fi

op="$1"
case ${op} in
    start)
        logPrint info  "Start K8sService ..."
        startService
	    logPrint info  "finished ."
        ;;
    stop)
        logPrint info  "Stop K8sService ..."
        stopService
	    logPrint info  "finished ."
        ;;
    status)
        logPrint info  "K8sService status, (ps -ef)"
        statusService
        ;;
     *)
        displayHelp
        exit 0
        ;;
esac