package demo.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class ServletContextInitialiser implements WebApplicationInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(ServletContextInitialiser.class);

    public void onStartup(ServletContext servletContext) throws ServletException {
        LOG.info("Start servlet context");

        LOG.debug("Building Spring application context");
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.setServletContext(servletContext);
        applicationContext.scan("demo");

        LOG.debug("Register DispatcherServlet");
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setApplicationContext(applicationContext);
        applicationContext.refresh();
        ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcherServlet", dispatcherServlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/api/*");

        LOG.debug("onStartup() complete");
    }

}

