package northwind.util;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Util {
    public static String getCustomerResponseXml(DOMSource domSource) throws TransformerConfigurationException,
    TransformerFactoryConfigurationError, TransformerException{
	    Transformer transformer = TransformerFactory.newInstance().newTransformer();
	    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    StringWriter stringWriter = new StringWriter();
	    transformer.transform(domSource, new StreamResult(stringWriter));
	    return stringWriter.toString();
    }
    
   
}
