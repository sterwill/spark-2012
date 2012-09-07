package org.tailfeather.client;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class BadgeRenderer {
	private static final Logger LOGGER = Logger.getLogger(BadgeRenderer.class.getName());
	private static final int QR_SIZE_PIXELS = 1200;
	private final File svgTemplate;
	private final String qrCodeElementId;

	public BadgeRenderer(File svgTemplate, String qrCodeElementId) {
		this.svgTemplate = svgTemplate;
		this.qrCodeElementId = qrCodeElementId;
	}

	public void print(String qrCode) {
		try {
			BufferedImage qrImage = generateQRCode(qrCode);

			Document doc = readTemplate();
			Element qrCodeElement = doc.getElementById(qrCodeElementId);
			if (qrCodeElement == null) {
				LOGGER.log(Level.SEVERE, MessageFormat.format("Could not find element by id [{0}] in SVG file {1}",
						qrCodeElementId, svgTemplate));
				return;
			}

			float x = Float.parseFloat(qrCodeElement.getAttribute("x"));
			float y = Float.parseFloat(qrCodeElement.getAttribute("y"));
			float width = Float.parseFloat(qrCodeElement.getAttribute("width"));
			float height = Float.parseFloat(qrCodeElement.getAttribute("height"));

			// Round up so the image is a bit bigger than what's there
			width = (float) Math.ceil(width);
			height = (float) Math.ceil(height);

			SVGGraphics2D generator = new SVGGraphics2D(doc);

			AffineTransformOp affineOp = new AffineTransformOp(AffineTransform.getScaleInstance(
					width / qrImage.getWidth(), height / qrImage.getHeight()), null);
			generator.drawImage(qrImage, affineOp, (int) x, (int) y);

			doc.getDocumentElement().appendChild(generator.getRoot());

			Writer out = new OutputStreamWriter(new FileOutputStream("/tmp/out.svg"), "UTF-8");
			generator.stream(doc.getDocumentElement(), out, true);
			out.close();

		} catch (IOException | WriterException e) {
			LOGGER.log(Level.SEVERE, "Error printing badge", e);
			e.printStackTrace();
		}
	}

	private Document readTemplate() throws IOException {
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
		return f.createDocument(svgTemplate.toURI().toString());
	}

	private Document newDocument() throws IOException {
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
		String svgNS = "http://www.w3.org/2000/svg";
		return domImpl.createDocument(svgNS, "svg", null);
	}

	private BufferedImage generateQRCode(String qrCode) throws WriterException {
		QRCodeWriter writer = new QRCodeWriter();
		BitMatrix matrix = writer.encode(qrCode, BarcodeFormat.QR_CODE, QR_SIZE_PIXELS, QR_SIZE_PIXELS);
		return MatrixToImageWriter.toBufferedImage(matrix);
	}
}
