package northwind.processor;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class CamelDeleteBean {

  private static final Logger LOG = LoggerFactory.getLogger(CamelDeleteBean.class);

  /**
   * @param context
   * @param processDef
   * deletes the file written by out route.
   */
  @Handler
  public void deleteFileWrittenByOutRoute(CamelContext context) {

    ConsumerTemplate template = context.createConsumerTemplate();
    String uri = "sftp://localhost:22/?username=I542324&password=RAW(Lokal)&stepwise=false&disconnect=true&maximumReconnectAttempts=2&reconnectDelay=10000&autoCreate=false&jschLoggingLevel=INFO&useList=false&fileName=Test_SFTP_Check.txt&delete=true";
    try {
        template.start();
        template.receive(uri, 30000);
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
