package cn.chinatelecom.kubernetes.rest.resource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


import cn.chinatelecom.kubernetes.rest.RequestHandle;
import cn.chinatelecom.kubernetes.rest.Tools;
import cn.chinatelecom.kubernetes.rest.bean.ApiResponseBean;

import com.alibaba.fastjson.JSONObject;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/v1/k8sservice")
public class K8sRestService {
    private static Logger log = LoggerFactory.getLogger(K8sRestService.class);

    @Context
    private HttpServletRequest servletRequest;


    /**
     * k8s service 服务测试接口
     * @return ApiResponseBean
     */
    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponseBean testReq() {
        ApiResponseBean bean = new ApiResponseBean();
        bean.setCode(200);
        bean.setResponseTime(Tools.getNowTime());
        bean.setMessage("[INFO] oops, you got it.");
        return bean;
    }


    /**
     * 根据namespace,label查询启动的pod列表
     * @param msgId 消息唯一id, 建议使用时间戳, 原值返回
     * @param nameSpace nameSpace名称
     * @param deploymentName deployment名称
     * @return ApiResponseBean
     */
    @GET
    @Path("/getPod")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponseBean getPod(@QueryParam("msgId") String msgId,@QueryParam("nameSpace") String nameSpace,@QueryParam("deploymentName") String deploymentName) {
        log.info("[getPod] Request Info: " + servletRequest.toString());
        log.info("[getPod] Request Addr: " + servletRequest.getRemoteAddr());

        ApiResponseBean bean = new ApiResponseBean();
        bean.setMsgId(msgId);
        String message = "default";

        if (Tools.strEmpty(msgId) || Tools.strEmpty(nameSpace) || Tools.strEmpty(deploymentName)) {
            bean.setCode(201);
            bean.setResponseTime(Tools.getNowTime());
            bean.setMessage("[ERROR] mandatory parameters missed. ");
            return bean;
        }

        try{
            bean = RequestHandle.getPod(bean, nameSpace, deploymentName);
        }catch (Exception ex){
            bean.setCode(500);
            bean.setResponseTime(Tools.getNowTime());
            message = "[ERROR] K8sRestService error, check your request and parameters first, or contact the administrator. ";
            bean.setMessage(message);
            log.error(message);
            log.error(ex.toString());
        }
        log.info("[getPod] Response Info: {}", JSONObject.toJSONString(bean));
        return  bean;
    }


    /**
     * 根据namespace,deployment Name更新pod的replicas
     * @param msgId 消息唯一id, 建议使用时间戳, 原值返回
     * @param nameSpace nameSpace名称
     * @param deploymentName deployment名称
     * @return ApiResponseBean
     */
    @POST
    @Path("/updatePodReplicas")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponseBean updatePodReplicas(@FormParam("msgId") String msgId, @FormParam("nameSpace") String nameSpace, @FormParam("deploymentName") String deploymentName, @FormParam("replicas") Integer replicas) {
        log.info("[updatePodReplicas] Request Info: " + servletRequest.toString());
        log.info("[updatePodReplicas] Request Addr: " + servletRequest.getRemoteAddr());

        ApiResponseBean bean = new ApiResponseBean();
        bean.setMsgId(msgId);
        String message = "default";

        if (Tools.strEmpty(msgId) || Tools.strEmpty(nameSpace) || Tools.strEmpty(deploymentName) || replicas == null) {
            bean.setCode(201);
            bean.setResponseTime(Tools.getNowTime());
            bean.setMessage("[ERROR] mandatory parameters missed. ");
            return bean;
        }

        try{
            bean = RequestHandle.updatePodReplicas(bean, nameSpace, deploymentName, replicas);
        }catch (Exception ex){
            bean.setCode(500);
            bean.setResponseTime(Tools.getNowTime());
            message = "[ERROR] K8sRestService error, check your request and parameters first, or contact the administrator. ";
            bean.setMessage(message);
            log.error(message);
            log.error(ex.toString());
        }
        log.info("[updatePodReplicas] Response Info: {}", JSONObject.toJSONString(bean));
        return  bean;

    }


}