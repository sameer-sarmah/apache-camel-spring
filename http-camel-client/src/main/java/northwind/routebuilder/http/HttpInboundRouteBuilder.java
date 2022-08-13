package northwind.routebuilder.http;

import org.apache.camel.builder.RouteBuilder;

import northwind.processor.http.HttpResponseProcessor;


public class HttpInboundRouteBuilder extends RouteBuilder{
	  private String start;

	  public HttpInboundRouteBuilder(String start) {
	    this.start = start;
	  }

	  @Override
	  public void configure() throws Exception {
	    from(this.start)
	    .process(new HttpResponseProcessor())
	    .end();
	  }

}

