package northwind.processor;

import java.util.List;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.ExtendedExchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileMessage;
import org.apache.camel.component.file.remote.SftpRemoteFileSingle;
import org.apache.camel.spi.Synchronization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileDeleteProcessor implements Processor {
	
	private static final Logger LOG = LoggerFactory.getLogger(FileDeleteProcessor.class);
	
	
	public void process(Exchange exchange) throws Exception {
		ConsumerTemplate template = exchange.getContext().createConsumerTemplate();
		String uri = "sftp://localhost:22/?username=I542324&password=RAW(Lokal)&stepwise=false&disconnect=true&maximumReconnectAttempts=2&reconnectDelay=10000&autoCreate=false&jschLoggingLevel=INFO&useList=false&fileName=Test_SFTP_Check.txt&delete=true&readLockCheckInterval=1&readLockTimeout=3";
		try {
			template.start();
			Exchange responseExchange = template.receive(uri, 30000);
			Message message = responseExchange.getIn();
			if (message instanceof GenericFileMessage) {
				GenericFileMessage fileMessage = (GenericFileMessage) message;
				GenericFile file = fileMessage.getGenericFile();
				System.out.println("File=" + file.getAbsoluteFilePath());
				SftpRemoteFileSingle remoteFile = (SftpRemoteFileSingle) file.getFile();
				Object obj = remoteFile.getRemoteFile();
				System.out.println();
			}
	        /*
	        GenericFileOnCompletion is one of the on-completion handlers which gets added when "delete=true" is present in Url param.
		    GenericFileOnCompletion has a reference to GenericFileDeleteProcessStrategy which is responsible for executing deletion of the file.
			In our scenario an instance of GenericFileOnCompletion is created with GenericFileDeleteProcessStrategy when the consumer(RemoteFileConsumer) is created.
			When the consumer polls the file an camel Exchange object is populated with GenericFileDeleteProcessStrategy as a on-completion handlers.
			We are copying the on-completion handlers to the original camel Exchange object. 
	         * */
			if(exchange instanceof ExtendedExchange && responseExchange instanceof ExtendedExchange) {
				ExtendedExchange extendedResponseExchange = (ExtendedExchange)responseExchange;
				ExtendedExchange extendedExchange = (ExtendedExchange)exchange;
				List<Synchronization> onCompletions = extendedResponseExchange.handoverCompletions();
	
//				for(Synchronization onCompletion : onCompletions) {			
//					if(!extendedExchange.containsOnCompletion(onCompletion)) {
//						extendedExchange.addOnCompletion(onCompletion);
//						LOG.info("Adding onCompletion of class="+onCompletion.getClass());
//					}
//				}	
				
				onCompletions.stream()
					.filter(onCompletion -> !extendedExchange.containsOnCompletion(onCompletion))
					.forEach(onCompletion -> {
						extendedExchange.addOnCompletion(onCompletion);
						LOG.info("Adding onCompletion of class="+onCompletion.getClass());
					});
			}
		} catch (Exception e) {
			LOG.error("SFTP Credential Tester Exception while deleting created file", e);
		} finally {
			try {
				template.stop();
			} catch (Exception e) {
				LOG.error("SFTP Credential Tester Exception while stopping delete template ", e);
			}
		}
	}
}
