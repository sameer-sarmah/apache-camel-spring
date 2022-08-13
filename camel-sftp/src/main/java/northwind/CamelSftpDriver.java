package northwind;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;

import northwind.routebuilder.DeleteRouteBuilder;
import northwind.routebuilder.OutboundRouteBuilder;



public class CamelSftpDriver {
	
	
	
	public static void main(String[] args) {
		String uri = "sftp://localhost:22/?username=I542324&password=RAW(Lokal)&stepwise=false&disconnect=true"
				+ "&maximumReconnectAttempts=2&reconnectDelay=10000&autoCreate=false&jschLoggingLevel=INFO"
				+ "&fileExist=Override&connectTimeout=300000";
		CamelContext camelContext = new DefaultCamelContext();
        OutboundRouteBuilder outboundRouteBuilder = new OutboundRouteBuilder("direct:start", uri, "direct:end");
        DeleteRouteBuilder deleteRouteBuilder = new DeleteRouteBuilder("direct:end");
        Exchange exchange = null;
        try {
			camelContext.addRoutes(outboundRouteBuilder);
	        camelContext.addRoutes(deleteRouteBuilder);
	        camelContext.start();
	        ProducerTemplate producer = camelContext.createProducerTemplate();
	        exchange = producer.request("direct:start", outboundRouteBuilder.createProcessor());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        finally {
            if(exchange != null) {
               String route = exchange.getFromRouteId();
               try {
				camelContext.getRouteController().stopRoute(route);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            }
            camelContext.stop();
          }

	}
}
