package com.internship;

import com.internship.config.AppConfig;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

public class Application {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(AppConfig.class);
        Context ctx = tomcat.addContext("/", new File(".").getAbsolutePath());
        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);
        Tomcat.addServlet(ctx, "dispatcherServlet", dispatcherServlet)
                .setLoadOnStartup(1);
        ctx.addServletMappingDecoded("/", "dispatcherServlet");
        tomcat.start();
        tomcat.getServer().await();
    }
}