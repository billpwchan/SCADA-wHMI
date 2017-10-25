/**
 * 
 */
package com.thalesgroup.config.scdm.extract.hypervisor;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * @author T0126039
 * <p>This class decorate an {@link XMLStreamWriter} in order to format and indent the XML output stream</p>
 */
public class IndentedXmlStreamWriter implements XMLStreamWriter {
	
	private final String indent; // Number of indent chars
	private final XMLStreamWriter xmlStreamWriter;
	private int xmlLevel;
	private boolean lastWasStart;

	/**
	 * 
	 */
	public IndentedXmlStreamWriter(XMLStreamWriter xmlStreamWriter) {
		this.xmlStreamWriter = xmlStreamWriter;
		this.xmlLevel = 0;
		this.lastWasStart = false;
		this.indent = "    ";
	}
	
	public IndentedXmlStreamWriter(XMLStreamWriter xmlStreamWriter, int numberOfIndentChars) {
		this.xmlStreamWriter = xmlStreamWriter;
		this.xmlLevel = 0;
		this.lastWasStart = false;
		
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < numberOfIndentChars; i++) {
			sb.append(" ");
		}
		
		this.indent = sb.toString();
	}
	

	@Override
	public void writeStartElement(String localName) throws XMLStreamException {
		indent();
		xmlStreamWriter.writeStartElement(localName);
	}

	@Override
	public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
		indent();
		xmlStreamWriter.writeStartElement(namespaceURI, localName);
		
	}

	@Override
	public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
		indent();
		xmlStreamWriter.writeStartElement(prefix, localName, namespaceURI);
	}

	@Override
	public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
		indent();
		xmlStreamWriter.writeEmptyElement(namespaceURI, localName);
		unindent();
	}

	@Override
	public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
		indent();
		xmlStreamWriter.writeEmptyElement(namespaceURI, localName, namespaceURI);
		unindent();
	}

	@Override
	public void writeEmptyElement(String localName) throws XMLStreamException {
		indent();
		xmlStreamWriter.writeEmptyElement(localName);
		unindent();
	}

	@Override
	public void writeEndElement() throws XMLStreamException {
		unindent();
		xmlStreamWriter.writeEndElement();
	}

	@Override
	public void writeEndDocument() throws XMLStreamException {
		xmlStreamWriter.writeEndDocument();
		xmlStreamWriter.writeCharacters("\n");
		
	}

	@Override
	public void close() throws XMLStreamException {
		this.xmlStreamWriter.close();
	}

	@Override
	public void flush() throws XMLStreamException {
		this.xmlStreamWriter.flush();
		
	}

	@Override
	public void writeAttribute(String localName, String value) throws XMLStreamException {
		this.xmlStreamWriter.writeAttribute(localName, value);
		
	}

	@Override
	public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
		this.xmlStreamWriter.writeAttribute(prefix, namespaceURI, localName, value);
		
	}

	@Override
	public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
		this.xmlStreamWriter.writeAttribute(namespaceURI, localName, value);
		
	}

	@Override
	public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
		this.xmlStreamWriter.writeNamespace(prefix, namespaceURI);
		
	}

	@Override
	public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
		this.xmlStreamWriter.writeDefaultNamespace(namespaceURI);
	}

	@Override
	public void writeComment(String data) throws XMLStreamException {
		indent();
		this.xmlStreamWriter.writeComment(data);
		unindent();
	}

	@Override
	public void writeProcessingInstruction(String target) throws XMLStreamException {
		this.xmlStreamWriter.writeProcessingInstruction(target);
		
	}

	@Override
	public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
		this.xmlStreamWriter.writeProcessingInstruction(target, data);
	}

	@Override
	public void writeCData(String data) throws XMLStreamException {
		this.xmlStreamWriter.writeCData(data);
	}

	@Override
	public void writeDTD(String dtd) throws XMLStreamException {
		this.xmlStreamWriter.writeDTD(dtd);
	}

	@Override
	public void writeEntityRef(String name) throws XMLStreamException {
		this.xmlStreamWriter.writeEntityRef(name);
	}

	@Override
	public void writeStartDocument() throws XMLStreamException {
		this.xmlStreamWriter.writeStartDocument();
		this.xmlStreamWriter.writeCharacters("\n");
	}

	@Override
	public void writeStartDocument(String version) throws XMLStreamException {
		this.xmlStreamWriter.writeStartDocument(version);
		this.xmlStreamWriter.writeCharacters("\n");
	}

	@Override
	public void writeStartDocument(String encoding, String version) throws XMLStreamException {
		this.xmlStreamWriter.writeStartDocument(encoding, version);
		this.xmlStreamWriter.writeCharacters("\n");
	}

	@Override
	public void writeCharacters(String text) throws XMLStreamException {
		this.xmlStreamWriter.writeCharacters(text);
	}

	@Override
	public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
		this.xmlStreamWriter.writeCharacters(text, start, len);
	}

	@Override
	public String getPrefix(String uri) throws XMLStreamException {
		return this.xmlStreamWriter.getPrefix(uri);
	}

	@Override
	public void setPrefix(String prefix, String uri) throws XMLStreamException {
		this.xmlStreamWriter.setPrefix(prefix, uri);
	}

	@Override
	public void setDefaultNamespace(String uri) throws XMLStreamException {
		this.xmlStreamWriter.setDefaultNamespace(uri);
	}

	@Override
	public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
		this.xmlStreamWriter.setNamespaceContext(context);
	}

	@Override
	public NamespaceContext getNamespaceContext() {
		return this.xmlStreamWriter.getNamespaceContext();
	}

	@Override
	public Object getProperty(String name) throws IllegalArgumentException {
		return this.xmlStreamWriter.getProperty(name);
	}
	
	private final void unindent() throws XMLStreamException {
		xmlLevel -= 1;
		if (!lastWasStart) {
			writeIndent(xmlLevel);
		}
		if (xmlLevel == 0) {
			xmlStreamWriter.writeCharacters("\n"); //$NON-NLS-1$
		}
		lastWasStart = false;
	}

	private final void indent() throws XMLStreamException {
		lastWasStart = true;
		writeIndent(xmlLevel);
		xmlLevel += 1;
	}

	private final void writeIndent(int level) throws XMLStreamException {
		if (level > 0) {
			StringBuilder b = new StringBuilder(level + 1);
			b.append('\n');
			for (int i = 0; i < level; i++) {
				b.append(indent);
			}
			xmlStreamWriter.writeCharacters(b.toString());
		}
	}

}
