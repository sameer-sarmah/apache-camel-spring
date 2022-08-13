package northwind.routebuilder.http;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

public class HttpOutboundRouteBuilder extends RouteBuilder{

	private String start;
	private String uri;
	private String end;
	
	public HttpOutboundRouteBuilder(String start, String uri, String end) {
		this.start = start;
		this.uri = uri;
		this.end = end;
	}
	
	@Override
	public void configure() throws Exception {
	    from(start)
        .process(exchange -> exchange.getIn().setBody(simple(null)))
	        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
	        .setHeader(Exchange.HTTP_QUERY, constant("$format=json"))
	        .to(this.uri)
	        .to(this.end);
		
	}

}
