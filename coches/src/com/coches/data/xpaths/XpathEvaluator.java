package com.coches.data.xpaths;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.coches.data.DataProviderException;

public class XpathEvaluator {

	public String evaluateXpathString(Document doc, String xpath) throws DataProviderException {
		try {
			return (String) XPathFactory.newInstance().newXPath().evaluate(xpath, doc, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			handleException(e);
		}
		return null;
	}
	
	public String evaluateXpathString(Node n, String xpath) throws DataProviderException {
		try {
			return (String) XPathFactory.newInstance().newXPath().evaluate(xpath, n, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			handleException(e);
		}
		return null;
	}
	
	public Object evaluateXpathNode(Document doc, String xpath) throws DataProviderException {
		try {
			return XPathFactory.newInstance().newXPath().evaluate(xpath, doc, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			handleException(e);
		}
		return null;
	}

	public Object evaluateXpath(Document doc, String xpath, QName xpathConstant) throws DataProviderException {
		try {
			return XPathFactory.newInstance().newXPath().evaluate(xpath, doc, xpathConstant);
		} catch (XPathExpressionException e) {
			handleException(e);
		}
		return null;
	}

	public Object evaluateXpath(Node n, String xpath, QName xpathConstant) throws DataProviderException {
		try {
			return XPathFactory.newInstance().newXPath().evaluate(xpath, n, xpathConstant);
		} catch (XPathExpressionException e) {
			handleException(e);
		}
		return null;
	}

	private void handleException(XPathExpressionException e) throws DataProviderException {
		throw new DataProviderException("Niepoprawne zapytanie xpath.", e);
	}
}
