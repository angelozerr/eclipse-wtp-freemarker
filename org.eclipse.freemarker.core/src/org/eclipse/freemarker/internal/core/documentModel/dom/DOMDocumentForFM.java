/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Zend Technologies
 *******************************************************************************/
package org.eclipse.freemarker.internal.core.documentModel.dom;

import org.eclipse.wst.html.core.internal.document.DocumentStyleImpl;
import org.eclipse.wst.xml.core.internal.document.DocumentImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class DOMDocumentForFM extends DocumentStyleImpl {

	public DOMDocumentForFM() {
		super();
	}

	protected DOMDocumentForFM(DocumentImpl that) {
		super(that);
	}

	public Node cloneNode(boolean deep) {
		DOMDocumentForFM cloned = new DOMDocumentForFM(this);
		if (deep)
			cloned.importChildNodes(this, true);
		return cloned;
	}

	/**
	 * createElement method
	 * 
	 * @return org.w3c.dom.Element
	 * @param tagName
	 *            java.lang.String
	 */
	public Element createElement(String tagName) throws DOMException {
		checkTagNameValidity(tagName);

		ElementImplForFM element = new ElementImplForFM();
		element.setOwnerDocument(this);
		element.setTagName(tagName);
		return element;
	}

	/**
	 * createAttribute method
	 * 
	 * @return org.w3c.dom.Attr
	 * @param name
	 *            java.lang.String
	 */
	public Attr createAttribute(String name) throws DOMException {
		AttrImplForFM attr = new AttrImplForFM();
		attr.setOwnerDocument(this);
		attr.setName(name);
		return attr;
	}

	/**
	 * createTextNode method
	 * 
	 * @return org.w3c.dom.Text
	 * @param data
	 *            java.lang.String
	 */
	public Text createTextNode(String data) {
		TextImplForFM text = new TextImplForFM();
		text.setOwnerDocument(this);
		text.setData(data);
		return text;
	}

	public void setModel(IDOMModel model) {
		super.setModel(model);
	}
}
