package northwind.app;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.Registry;
import org.apache.camel.support.SimpleRegistry;

import northwind.routebuilder.olingo.ODataV4InboundRouteBuilder;
import northwind.routebuilder.olingo.ODataV4OutboundRouteBuilder;


public class CamelOlingoDriver {
	
	public static void main(String[] args) {
		String uri = "olingo4://read/Products?serviceUri=https://services.odata.org/Experimental/OData/OData.svc&runLoggingLevel=INFO"
				+ "&queryParams=#queryParams";
		CamelContext camelContext = new DefaultCamelContext();
		ODataV4OutboundRouteBuilder outboundRouteBuilder = new ODataV4OutboundRouteBuilder("direct:start", uri, "direct:end");
		ODataV4InboundRouteBuilder deleteRouteBuilder = new ODataV4InboundRouteBuilder("direct:end");
		Map<String,String> queryParams = new HashMap<>();
		queryParams.put("$top", "1");
		queryParams.put("$format", "json");
		Registry registry = Objects.nonNull(camelContext.getRegistry()) ? camelContext.getRegistry() : new SimpleRegistry();
		registry.bind("queryParams", queryParams);
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
