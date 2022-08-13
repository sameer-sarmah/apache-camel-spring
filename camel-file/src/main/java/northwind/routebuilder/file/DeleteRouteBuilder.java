package northwind.routebuilder.file;

import org.apache.camel.builder.RouteBuilder;

import northwind.processor.file.FileDeleteProcessor;


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