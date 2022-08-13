package northwind.app;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;

import northwind.routebuilder.http.HttpInboundRouteBuilder;
import northwind.routebuilder.http.HttpOutboundRouteBuilder;

public class CamelHttpDriver {

	public static void main(String[] args) {
		String uri = "https://services.odata.org/Experimental/OData/OData.svc/Products?$top=1";
		CamelContext camelContext = new DefaultCamelContext();
		HttpOutboundRouteBuilder outboundRouteBuilder = new HttpOutboundRouteBuilder("direct:start", uri, "direct:end");
		HttpInboundRouteBuilder deleteRouteBuilder = new HttpInboundRouteBuilder("direct:end");
        Exchange exchange = null;
        try {
			camelContext.addRoutes(outboundRouteBuilder);
	        camelContext.addRoutes(deleteRouteBuilder);
	        camelContext.start();
	        ProducerTemplate producer = camelContext.createProducerTemplate();
	        exchange = producer.request("direct:start",(ex) -> {});
		} catch (Exception e) {
			e.printStackTrace();
		}
        finally {
            if(exchange != null) {
               String route = exchange.getFromRouteId();
               try {
				camelContext.getRouteController().stopRoute(route);
			} catch (Exception e) {
				e.printStackTrace();
			}
            }
            camelContext.stop();
          }
	}

}
