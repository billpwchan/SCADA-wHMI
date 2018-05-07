package com.thalesgroup.scadagen.whmi.opm;

import java.io.*;
import java.util.*;

import javax.xml.namespace.QName;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class MergeMwtFiles
{
    private XPathFactory xPathFactory_;
    
    
    public MergeMwtFiles()
    {
    }
    
    public String[] readMergeIndex(String indexFilename) throws IOException
    {
        ArrayList<String> filenames = new ArrayList<String>();

        File f = new File(indexFilename);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
        
        try
        {
            String line = null;

            while ((line = bufferedReader.readLine()) != null)
            {
                line = line.trim();
                if (line.length() > 0 && line.charAt(0) != '#')
                    filenames.add(line);
            }
        }
        finally {
            bufferedReader.close();
        }
        
        String[] result = new String[filenames.size()];
        for (int i = 0; i < filenames.size(); i++)
            result[i] = filenames.get(i);
        return result;
    }
    

    public boolean safeMergePermissions(String[] inputFilenames, String outputFilename)
    {
        try
        {
            mergePermissions(inputFilenames, outputFilename);
        }
        catch (Exception e)
        {
            System.err.println("Exception while merging files: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

    
    public void mergePermissions(String[] inputFilenames, String outputFilename) throws ParserConfigurationException,
            SAXException, IOException, XPathExpressionException, TransformerException
    {
        if (inputFilenames.length == 0)
            throw new IllegalArgumentException("MergePermissions.mergePermissions: must at least give one file");

        xPathFactory_ = XPathFactory.newInstance();

        Document[] documents = openAllDocuments(inputFilenames);
        assert documents.length > 0;

        mergeRoles(documents);
        concatenatePermissionsToMainDocument(documents);
        
        // dbgPrintRootNodes(mainDocument);

        // concatenatePermissionsToMainDocument(documents);

        writeDocument(documents[0], outputFilename);
    }

    
    private XPathExpression createXPath(String expression) throws XPathExpressionException
    {
        assert(xPathFactory_ != null);
        XPath xPath = xPathFactory_.newXPath();
        return xPath.compile(expression);               
    }
    
    
    private Object createAndEvaluateXPath(Document doc, String expression, QName returnType) throws XPathExpressionException
    {
        XPathExpression xPathExpression = createXPath(expression);
        return xPathExpression.evaluate(doc, returnType);
    }
    
    
    private void mergeRoles(Document[] documents) throws XPathExpressionException
    {
        Document mainDocument = documents[0];
        
        Node firstRoleInMainDocument = getFirstNodeWithTag(mainDocument, "role");  // Can be null.
        Node mainNodeInMainDocument = getMainNode(mainDocument);
        XPathExpression allRolesXPath = createXPath("/maestroHMIConfiguration/role");
        
        for (int id = 1; id < documents.length; id++)
        {
            Document mergedDocument = documents[id];
            NodeList rolesInMergedDocument = (NodeList) allRolesXPath.evaluate(mergedDocument, XPathConstants.NODESET);
            
            for (int irmd = 0; irmd < rolesInMergedDocument.getLength(); irmd++)
            {
                Node roleInMergedDocument = rolesInMergedDocument.item(irmd);
                String roleIdentifier = getIdentifier(roleInMergedDocument);
                if (roleIdentifier == null)
                    continue;  // Role has no identifier. Continue to next role.
                
                Node roleInMainDocument = (Node) createAndEvaluateXPath(mainDocument, 
                        "/maestroHMIConfiguration/role[@id = \"" + roleIdentifier + "\"]", 
                        XPathConstants.NODE);
                if (roleInMainDocument != null)
                {
                    // The role already exists in the main document. Merge the permissions list.
                    
                    mergeRolePermissions(mainDocument, roleIdentifier, roleInMergedDocument, roleInMainDocument);
                }
                else {
                    // The role does not exist in the main document. Add it.
                    
                    importAndAddNode(mainDocument, mainNodeInMainDocument, firstRoleInMainDocument, roleInMergedDocument);
                }                                
            }
        }
    }
    
    
    private void mergeRolePermissions(Document mainDocument, String roleIdentifier, Node roleInMergedDocument, Node roleInMainDocument) throws XPathExpressionException
    {
        NodeList mergedDocumentChildren = roleInMergedDocument.getChildNodes();
        for (int ip = 0; ip < mergedDocumentChildren.getLength(); ip++)
        {
            Node mergedDocumentChild = mergedDocumentChildren.item(ip);
            if (mergedDocumentChild.getNodeType() == Node.ELEMENT_NODE && mergedDocumentChild.getNodeName() == "permissionAllowList")
            {
                Element permission = (Element) mergedDocumentChild;
                String permissionName = permission.getTextContent();
                
                // Find if this permission exists in the role of the main document.
                // TBD: there must be a faster way to do this.                 
                boolean permissionExistsInMainDocument 
                    = (Boolean) createAndEvaluateXPath(mainDocument, 
                            "count(/maestroHMIConfiguration/role[@id = \"" + roleIdentifier + "\"]/permissionAllowList[. = \"" + permissionName + "\"]) > 0", 
                            XPathConstants.BOOLEAN);
                
                if (!permissionExistsInMainDocument)
                    importAndAddNode(mainDocument, roleInMainDocument, null, permission);
            }
        }
    }
    
    
    // Returns the identifier associated to a role or a permission, or null if the role has
    // no identifier.
    
    private String getIdentifier(Node n)
    {
        NamedNodeMap attributes = n.getAttributes();
        if (attributes == null)
            return null;  // Role or permission without attributes.
        
        Node attributeId = attributes.getNamedItem("id");
        if (attributeId == null)
            return null;  // Role or permission without an identifier.
        
        String identifier = attributeId.getTextContent();
        
        // Can be null if the identifier has no text.
        return identifier;
    }
    
    
    private Node getFirstNodeWithTag(Document document, String tagName) throws XPathExpressionException
    {
        return (Node) createAndEvaluateXPath(document, "/maestroHMIConfiguration/" + tagName, XPathConstants.NODE);
    }
    
    // Concatenate the permissions of the other documents to the main document.

    private void concatenatePermissionsToMainDocument(Document[] documents) throws XPathExpressionException
    {
        assert documents.length > 0;

        Document mainDocument = documents[0];
        Node mainNodeInMainDocument = getMainNode(mainDocument);
        Node firstPermissionInMainDocument = getFirstNodeWithTag(mainDocument, "permission");
        
        XPathExpression allPermissionsXPath = createXPath("/maestroHMIConfiguration/permission");

        for (int id = 1; id < documents.length; id++)
        {
            Document mergedDocument = documents[id];
            NodeList permissionsInMergedDocument = (NodeList) allPermissionsXPath.evaluate(mergedDocument, XPathConstants.NODESET);
            
            for (int ipmd = 0; ipmd < permissionsInMergedDocument.getLength(); ipmd++)
            {
                Node permissionInMergedDocument = permissionsInMergedDocument.item(ipmd);
                String permissionIdentifier = getIdentifier(permissionInMergedDocument);
                
                boolean permissionExistsInMainDocument 
                    = (Boolean) createAndEvaluateXPath(mainDocument,
                            "count(/maestroHMIConfiguration/permission[@id = \"" + permissionIdentifier + "\"]) > 0",
                            XPathConstants.BOOLEAN);
                if (!permissionExistsInMainDocument)
                    importAndAddNode(mainDocument, mainNodeInMainDocument, firstPermissionInMainDocument, permissionInMergedDocument);                                            
            }
        }
    }

    private Document[] openAllDocuments(String inputFilenames[])
            throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document[] result = new Document[inputFilenames.length];

        for (int i = 0; i < inputFilenames.length; i++)
            result[i] = documentBuilder.parse(inputFilenames[i]);

        return result;
        
    }

    private void importAndAddNode(Document toDocument, Node parent, Node nodeBefore, Node n)
    {
        Node importedNode = toDocument.importNode(n, true);
        parent.insertBefore(importedNode, nodeBefore);
    }

    
    private void writeDocument(Document document, String outputFilename) throws TransformerException
    {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();        
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");                
        
        DOMSource source = new DOMSource(document);
        StreamResult outputStream = new StreamResult(new File(outputFilename));
        transformer.transform(source, outputStream);
    }

    
    private Node getMainNode(Document doc)
    {
        final String rootNodeTagName = "tns:maestroHMIConfiguration";

        NodeList children = doc.getElementsByTagName(rootNodeTagName);
        Node rootNode = null;
        if (children.getLength() == 0)
        {
            // Create this node now.
            rootNode = doc.createElement(rootNodeTagName);
            doc.appendChild(rootNode);
        }
        else
            rootNode = children.item(0);

        return rootNode;
    }
}
