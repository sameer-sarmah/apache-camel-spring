package northwind.config;

import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.learnwebservices.services.hello.HelloEndpoint;

@Configuration
@ImportResource({ "classpath:META-INF/cxf/cxf.xml" })
@ComponentScan(basePackages = {"northwind"})
public class CxfConfig {

	
    @Bean
    public LoggingFeature loggingFeature() {
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(true);
        return loggingFeature;
    }

    @Bean(name = "serviceClient")
    public HelloEndpoint serviceClient(@Autowired LoggingFeature loggingFeature){
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setServiceClass(HelloEndpoint.class);

        Map<String, Object> properties = factoryBean.getProperties();
        if(properties == null){
            properties = new HashMap<>();
        }

        properties.put("javax.xml.ws.client.connectionTimeout", 5000);
        properties.put("javax.xml.ws.client.receiveTimeout", 3000);

        factoryBean.setProperties(properties);

        factoryBean.getFeatures().add(loggingFeature);

        HelloEndpoint theService = (HelloEndpoint) factoryBean.create();
        BindingProvider bindingProvider = (BindingProvider) theService;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "https://webservices.daehosting.com/services/isbnservice.wso");

        return theService;
    }
    
    
}
