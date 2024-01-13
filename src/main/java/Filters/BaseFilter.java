package Filters;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class BaseFilter {
    protected Map<String, String> getMappingUrl(ServletContext servletContext) {
        //Key: url parttern, Value: class name
        Map<String, String> urlPatters = new TreeMap<>();
        //Get all servlet registration in app
        Map<String, ? extends ServletRegistration> servletsRegistrations = servletContext.getServletRegistrations();

        for (Map.Entry<String, ? extends ServletRegistration> servletsRegistration : servletsRegistrations.entrySet()) {
            //Get servlet registration
            ServletRegistration servletRegistration = servletsRegistration.getValue();
            //Get url mappings
            Collection<String> mappings = servletRegistration.getMappings();
            for (String url : mappings) {
                urlPatters.put(url, servletsRegistration.getValue().getClassName());
            }
        }
        return urlPatters;
    }

    protected String getRequestPath(String fullRequestUrl, String contextPath){
        return fullRequestUrl.replaceAll(contextPath, "");
    }
}
