package northwind.processor.http;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponseProcessor implements Processor  {

	private static final Logger LOG = LoggerFactory.getLogger(HttpResponseProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		LOG.info("HTTP GET response is: {}", exchange.getIn().getBody(String.class));
	}
}
