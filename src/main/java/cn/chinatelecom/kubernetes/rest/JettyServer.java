package cn.chinatelecom.kubernetes.rest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class JettyServer {

    private static final Logger log = LoggerFactory.getLogger(JettyServer.class);

    public void start(int port) throws Exception {
        Server server = new Server(port);

        ServletHolder servletHolder = new ServletHolder(org.glassfish.jersey.servlet.ServletContainer.class);
        Map<String, String> parameterMap = new HashMap<String, String>(6);
        parameterMap.put("jersey.config.server.provider.packages", "cn.chinatelecom.kubernetes.rest.resource");
        parameterMap.put("jersey.config.beanValidation.enableOutputValidationErrorEntity.server", "true");
        parameterMap.put("jersey.api.json.POJOMappingFeature", "true");
        parameterMap.put("jersey.config.property.resourceConfigClass", "jersey.api.core.PackagesResourceConfig");
        parameterMap.put("org.glassfish.jersey.logging.level", "FINE");
        parameterMap.put("java.util.logging.ConsoleHandler.level", "FINE");

        servletHolder.setInitParameters(parameterMap);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.addServlet(servletHolder, "/*");
        ErrorPageErrorHandler errorHandler = new ErrorPageErrorHandler();
        errorHandler.addErrorPage(404, "../html/404.html");
        context.setErrorHandler(errorHandler);
        server.setHandler(context);

        server.start();
    }


/*   public static void main(String[] args) throws Exception {

      *//*  ResourceBundle resource = ResourceBundle.getBundle("config");
        int port = 8080;

        try{
            port = Integer.valueOf(resource.getString("rest.api.server.port"));
        }catch (Exception ex){
            log.warn("获取配置项出错, 使用默认配置项: jetty port:{}.", port);
            log.warn("获取配置项出错. Exception: {}", ex.toString());
        }

        JettyServer server = new JettyServer();
        server.start(port);*//*
    }*/

}
