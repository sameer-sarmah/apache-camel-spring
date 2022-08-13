package northwind.routebuilder;

import org.apache.camel.builder.RouteBuilder;

import northwind.processor.FileDeleteProcessor;


/**
 * @author I338914
 * route to delete file written by read access
 */
public class DeleteRouteBuilder extends RouteBuilder {

  private String start;

  public DeleteRouteBuilder(String start) {
    this.start = start;
  }

  @Override
  public void configure() throws Exception {
    from(this.start)
    .process(new FileDeleteProcessor())
    .end();
  }

}