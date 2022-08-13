package northwind.processor.olingo;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientEntitySet;
import org.apache.olingo.client.api.domain.ClientProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OdataV4ConsumerProcessor implements Processor{

	private static final Logger LOG = LoggerFactory.getLogger(OdataV4ConsumerProcessor.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		ClientEntitySet entitySet = exchange.getIn().getBody(ClientEntitySet.class);
		List<ClientEntity> entities =  entitySet.getEntities();
		StringBuilder builder = new StringBuilder();
		entities.stream().forEach(entity -> {
			List<ClientProperty> properties = entity.getProperties();
			properties.forEach(property -> {
				builder.append(property.getName())
					   .append("=")
					   .append(property.getPrimitiveValue())
					   .append(",");
			});
			builder.append("\n\r");
		});
		LOG.info(builder.toString()) ;
	}

}
