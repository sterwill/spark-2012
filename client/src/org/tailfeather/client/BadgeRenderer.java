package org.tailfeather.client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class BadgeRenderer {
	private static final Logger LOGGER = Logger.getLogger(BadgeRenderer.class.getName());

	private static final int QR_SIZE_PIXELS = 600;

	private static final String XLINK_NS = "http://www.w3.org/1999/xlink";
	private static final String SVG_NS = "http://www.w3.org/2000/svg";

	private final File svgTemplate;

	public BadgeRenderer(File svgTemplate) {
		this.svgTemplate = svgTemplate;
	}

	public void print(String qrCode, String name) {
		try {
			// Load template and find the template elements
			Document doc = readTemplate();

			Element templateQrElement = findElementByAttribute(doc.getDocumentElement(), "id", "qr");
			if (templateQrElement == null) {
				LOGGER.log(Level.SEVERE, "Couldn't find qr element in template");
				return;
			}
			Element templateNameElement = findElementByAttribute(doc.getDocumentElement(), "id", "name");
			if (templateNameElement == null) {
				LOGGER.log(Level.SEVERE, "Couldn't find name element in template");
				return;
			}
			Element templateNameRectElement = (Element) templateNameElement.getFirstChild().getFirstChild();

			float qrX = Float.parseFloat(templateQrElement.getAttribute("x"));
			float qrY = Float.parseFloat(templateQrElement.getAttribute("y"));
			float qrWidth = Float.parseFloat(templateQrElement.getAttribute("width"));
			float qrHeight = Float.parseFloat(templateQrElement.getAttribute("height"));

			String flowRootTransform = templateNameElement.getAttribute("transform");
			String flowRootStyle = templateNameElement.getAttribute("style");
			float nameX = Float.parseFloat(templateNameRectElement.getAttribute("x"));
			float nameY = Float.parseFloat(templateNameRectElement.getAttribute("y"));
			float nameWidth = Float.parseFloat(templateNameRectElement.getAttribute("width"));
			float nameHeight = Float.parseFloat(templateNameRectElement.getAttribute("height"));
			String rectStyle = templateNameRectElement.getAttribute("style");

			// Replace the elements
			Element parent;

			byte[] qrPngBytes = generateQRCodePNG(qrCode);
			byte[] qrPngBase64 = Base64.encodeBase64(qrPngBytes);

			parent = (Element) templateQrElement.getParentNode();
			parent.removeChild(templateQrElement);
			parent.appendChild(createImageElement(doc, qrPngBase64, qrX, qrY, qrWidth, qrHeight));

			parent = (Element) templateNameElement.getParentNode();
			parent.removeChild(templateNameElement);
			parent.appendChild(createTextElement(doc, name, nameX, nameY, nameWidth, nameHeight, flowRootTransform,
					flowRootStyle, rectStyle));

			ProcessBuilder pb = new ProcessBuilder("rm", "-f", "/tmp/badge.pdf", "/tmp/badge.svg");
			Process p = pb.start();
			p.waitFor();

			writeDocument(doc, "/tmp/badge.svg");

			pb = new ProcessBuilder("inkscape", "-z", "-A=/tmp/badge.pdf", "/tmp/badge.svg");
			p = pb.start();
			int exitCode = p.waitFor();
			if (exitCode != 0) {
				LOGGER.log(Level.SEVERE, "Error running inkscape to convert to pdf");
				return;
			}

			pb = new ProcessBuilder("lpr", "/tmp/badge.pdf");
			p = pb.start();
			p.waitFor();
			if (exitCode != 0) {
				LOGGER.log(Level.SEVERE, "Error printing /tmp/badge.pdf with lpr");
				return;
			}
		} catch (IOException | WriterException | InterruptedException | TransformerException | SAXException
				| ParserConfigurationException e) {
			LOGGER.log(Level.SEVERE, "Error printing badge", e);
		}
	}

	private Node createTextElement(Document doc, String name, float x, float y, float width, float height,
			String flowRootTransform, String flowRootStyle, String rectStyle) {
		Element flowRoot = doc.createElementNS(SVG_NS, "flowRoot");
		flowRoot.setAttribute("xml:space", "preserve");
		flowRoot.setAttribute("transform", flowRootTransform);
		flowRoot.setAttribute("style", flowRootStyle);
		doc.getDocumentElement().appendChild(flowRoot);

		Element flowRegion = doc.createElementNS(SVG_NS, "flowRegion");
		flowRoot.appendChild(flowRegion);

		Element rect = doc.createElementNS(SVG_NS, "rect");
		rect.setAttribute("x", Float.toString(x));
		rect.setAttribute("y", Float.toString(y));
		rect.setAttribute("width", Float.toString(width));
		rect.setAttribute("height", Float.toString(height));
		rect.setAttribute("style", rectStyle);
		flowRegion.appendChild(rect);

		Element flowPara = doc.createElementNS(SVG_NS, "flowPara");
		flowPara.setTextContent(name);
		flowRoot.appendChild(flowPara);

		return flowRoot;
	}

	private Node createImageElement(Document doc, byte[] base64Png, float x, float y, float width, float height)
			throws IOException, WriterException {
		Element element = doc.createElementNS(SVG_NS, "image");
		element.setAttribute("x", Float.toString(x));
		element.setAttribute("y", Float.toString(y));
		element.setAttribute("width", Float.toString(width));
		element.setAttribute("height", Float.toString(height));
		element.setAttribute("preserveAspectRatio", "none");
		String href = "data:image/png;base64," + new String(base64Png);
		element.setAttribute("xmlns:xlink", XLINK_NS);
		element.setAttribute("xlink:href", href);
		element.setAttribute("xlink:type", "simple");
		element.setAttribute("xlink:actuate", "onLoad");
		element.setAttribute("xlink:show", "embed");
		doc.getDocumentElement().appendChild(element);
		return element;
	}

	private Element findElementByAttribute(Element e, String localName, String value) {
		String attrValue = e.getAttribute(localName);
		if (attrValue != null && attrValue.equals(value)) {
			return e;
		}

		for (int i = 0; i < e.getChildNodes().getLength(); i++) {
			Node node = e.getChildNodes().item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element child = findElementByAttribute((Element) node, localName, value);
				if (child != null) {
					return child;
				}
			}
		}

		return null;
	}

	private Document readTemplate() throws IOException, SAXException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(svgTemplate);
	}

	private void writeDocument(Document doc, String path) throws TransformerException, UnsupportedEncodingException,
			FileNotFoundException {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		Writer out = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");
		StreamResult result = new StreamResult(out);
		transformer.transform(source, result);
	}

	private byte[] generateQRCodePNG(String qrCode) throws IOException, WriterException {
		QRCodeWriter writer = new QRCodeWriter();
		BitMatrix matrix = writer.encode(qrCode, BarcodeFormat.QR_CODE, QR_SIZE_PIXELS, QR_SIZE_PIXELS);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(matrix, "png", bos);
		return bos.toByteArray();
	}
}
