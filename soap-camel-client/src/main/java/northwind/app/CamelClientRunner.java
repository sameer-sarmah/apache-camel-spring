package northwind.app;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.ws.client.WebServiceTransportException;

import com.learnwebservices.services.hello.HelloRequest;

import northwind.config.CxfConfig;
import northwind.routebuilder.SoapRouteBuilder;

@Import({CxfConfig.class})
@SpringBootApplication
public class CamelClientRunner implements ApplicationRunner{
	
	@Autowired
	private SoapRouteBuilder routeBuilder;
	
	@Autowired 
	private ProducerTemplate producerTemplate;
	
	@Autowired
	private CamelContext context ;
	
	public static void main(String[] args) {
		SpringApplication.run(CamelClientRunner.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
			sendRequestForDataFormatPojo();
		}
	
	
	private void sendRequestForDataFormatPojo() {
		try {
			context.addRoutes(routeBuilder);
	        context.start();
	        
	        HelloRequest helloRequest = new HelloRequest();
	        helloRequest.setName("Sameer");
			
	      	producerTemplate.sendBody("direct:start", helloRequest);
	        Thread.sleep(10000);
	        context.stop();

		}
		catch (Exception e) {
			if(e instanceof WebServiceTransportException) {
				System.err.println("[WebServiceTransportException]	ExceptionMessage="+e.getMessage());
			}
			else {
				System.err.println("ExceptionMessage="+e.getMessage());
			}
		}
	}

}
