package cn.kubernetes.service.rest.bean;


public class ApiResponseBean {
    /** 返回码  */
    private int code;
    /** 请求中提供, 原样返回  */
    private String msgId;
    /** 请求返回时间 */
    private String responseTime;
    /** 请求返回的消息体 */
    private Object message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

}
