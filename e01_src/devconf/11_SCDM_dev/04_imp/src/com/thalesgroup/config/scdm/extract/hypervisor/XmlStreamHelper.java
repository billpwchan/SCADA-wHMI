/**
 * 
 */
package com.thalesgroup.config.scdm.extract.hypervisor;

import java.io.OutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * @author T0126039
 *
 */
public class XmlStreamHelper {
	
	public static String UTF_8_ENCODING = "UTF-8";
	
	public static XMLStreamWriter createXmlOutputStream(OutputStream stream, String encoding, boolean indent) throws XMLStreamException {
		XMLOutputFactory factory = XMLOutputFactory.newFactory();
		XMLStreamWriter xmlStreamWriter = factory.createXMLStreamWriter(stream, encoding);
		if(indent) {
			return new IndentedXmlStreamWriter(xmlStreamWriter, 4);
		}
		return xmlStreamWriter;		
	}
}
