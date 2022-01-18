package cn.kubernetes.service.rest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * TODO Jetty服务出错页面自定义, 关闭默认出错页面. (404页面等)
 */
public class JettyServer {

    private static final Logger log = LoggerFactory.getLogger(JettyServer.class);

    public void start(int port) throws Exception {
        Server server = new Server(port);

        ServletHolder servletHolder = new ServletHolder(org.glassfish.jersey.servlet.ServletContainer.class);
        Map<String, String> parameterMap = new HashMap<>(7);
        parameterMap.put("jersey.config.server.provider.packages", "cn.kubernetes.service.rest.resource");
        parameterMap.put("jersey.config.beanValidation.enableOutputValidationErrorEntity.server", "true");
        parameterMap.put("jersey.api.json.POJOMappingFeature", "true");
        parameterMap.put("jersey.config.property.resourceConfigClass", "jersey.api.core.PackagesResourceConfig");
        parameterMap.put("org.glassfish.jersey.logging.level", "FINE");
        parameterMap.put("java.util.logging.ConsoleHandler.level", "FINE");
        // 添加对POST请求中MultiPart的支持, 如上传文件等.
        parameterMap.put("jersey.config.server.provider.classnames", "org.glassfish.jersey.media.multipart.MultiPartFeature");

        servletHolder.setInitParameters(parameterMap);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.addServlet(servletHolder, "/*");
        ErrorPageErrorHandler errorHandler = new ErrorPageErrorHandler();
        errorHandler.addErrorPage(404, "html/404.html");
        context.setErrorHandler(errorHandler);
        server.setHandler(context);

        server.start();
        server.join();
    }

}
