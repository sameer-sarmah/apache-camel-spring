package northwind.routebuilder;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.namespace.QName;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import northwind.response.handler.IResponseHandler;

@Component
public class SoapRouteBuilder extends RouteBuilder {
		
	private String CamelCxfMessage = "CamelCxfMessage";
	
	@Autowired
	private List<IResponseHandler> responseHandlers;
	
    @Override
    public void configure() throws Exception {
		   String endpoint = "cxf://http://apps.learnwebservices.com/services/hello?serviceClass=com.learnwebservices.services.hello.HelloEndpoint"
			   		+ "&wsdlURL=https://apps.learnwebservices.com/services/hello?WSDL";
		   //endpoint = endpoint +"&dataformat=PAYLOAD";
		   //	 endpoint = endpoint +"&dataformat=RAW";
		   System.out.println(endpoint);
		   CxfEndpoint cxfEndpoint = (CxfEndpoint)getContext().getEndpoint(endpoint);
		   
		   
        from("direct:start")
            .to(endpoint)
            .process((exchange)->{    	
            	if(Objects.nonNull(exchange.getIn()) && Objects.nonNull(exchange.getIn().getBody())) {
            		Object response = exchange.getIn().getBody();
            		
            		Map<String,Object> headers = exchange.getIn().getHeaders();
            		logHeaders(headers);
            		List<IResponseHandler> relevantResponseHandlers = responseHandlers
            			.stream()
            			.filter(responseHandler -> responseHandler.canHandle(response,headers))
            			.collect(Collectors.toList());
            		relevantResponseHandlers.forEach(responseHandler -> responseHandler.handle(response));
            	}
            });
    }
    
    
    private void logHeaders(Map<String,Object> headers ) {
    	System.out.println("Response Code="+headers.get("CamelHttpResponseCode"));
		Map<String,Object> responseContext = (Map<String, Object>) headers.get("ResponseContext");
		for(Map.Entry<String, Object> responseEntry :responseContext.entrySet()) {
			if(Objects.nonNull(responseEntry.getValue()) && (responseEntry.getValue() instanceof String 
					|| responseEntry.getValue() instanceof QName)) {
				System.out.println(responseEntry.getKey()+"="+responseEntry.getValue());
			}
		}
    }
    
    
}