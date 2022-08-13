package northwind.routebuilder.olingo;

import org.apache.camel.builder.RouteBuilder;

import northwind.processor.olingo.OdataV4ConsumerProcessor;


public class ODataV4InboundRouteBuilder extends RouteBuilder{
	  private String start;

	  public ODataV4InboundRouteBuilder(String start) {
	    this.start = start;
	  }

	  @Override
	  public void configure() throws Exception {
	    from(this.start)
	    .process(new OdataV4ConsumerProcessor())
	    .end();
	  }

}

