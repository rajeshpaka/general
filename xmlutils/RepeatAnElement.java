import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class RepeatAnElement {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException,
			XPathExpressionException, TransformerException {
		
		FileInputStream fis = new FileInputStream(new File("c:\\out.xml"));
		String filepath = "c:\\output.xml";
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document doc = builder.parse(fis);
		String expression = "//Roles/Role";
		XPath xpath = XPathFactory.newInstance().newXPath();
		Node node = (Node) xpath.compile(expression).evaluate(doc, XPathConstants.NODE);

		for (int i = 2; i < 50; i++) {
			Element e = (Element) node.cloneNode(true);
			e.setAttribute("name", "Role" + i);
			e.setAttribute("resourceId", "Role" + i);
			node.getParentNode().appendChild((Node) e);
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filepath));
		transformer.transform(source, result);
	}
}
