/**
 * $Id: mxCellCodec.java,v 1.1 2012/11/15 13:26:47 gaudenz Exp $
 * Copyright (c) 2006, Gaudenz Alder
 */
package br.ufrj.cos.expline.io;

import java.util.Iterator;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.ufrj.cos.expline.model.Activity;
import br.ufrj.cos.expline.model.Port;

import com.mxgraph.io.mxCodec;
import com.mxgraph.io.mxCodecRegistry;
import com.mxgraph.io.mxObjectCodec;

/**
 * Codec for mxCells. This class is created and registered
 * dynamically at load time and used implicitely via mxCodec
 * and the mxCodecRegistry.
 */
public class ActivityCodec extends mxObjectCodec
{

	/**
	 * Constructs a new cell codec.
	 */
	public ActivityCodec()
	{
		this(new Activity(), new String[] { "selected" }, new String[] { "parent", "source", "target" },
				null);
	}

	/**
	 * Constructs a new cell codec for the given template.
	 */
	public ActivityCodec(Object template)
	{
		this(template, null, null, null);
	}

	/**
	 * Constructs a new cell codec for the given arguments.
	 */
	public ActivityCodec(Object template, String[] exclude, String[] idrefs,
			Map<String, String> mapping)
	{
		super(template, exclude, idrefs, mapping);
	}

	/**
	 * Excludes user objects that are XML nodes.
	 */
	public boolean isExcluded(Object obj, String attr, Object value,
			boolean write)
	{
		return exclude.contains(attr)
				|| (write && attr.equals("value") && value instanceof Node && ((Node) value)
						.getNodeType() == Node.ELEMENT_NODE);
	}

	/**
	 * Encodes an mxCell and wraps the XML up inside the
	 * XML of the user object (inversion).
	 */
	public Node afterEncode(mxCodec enc, Object obj, Node node)
	{
		if (obj instanceof Activity)
		{
			Activity activity = (Activity) obj;

			if (activity.getValue() instanceof Node)
			{
				// Wraps the graphical annotation up in the
				// user object (inversion) by putting the
				// result of the default encoding into
				// a clone of the user object (node type 1)
				// and returning this cloned user object.
				Element tmp = (Element) node;
				node = enc.getDocument().importNode((Node) activity.getValue(),
						true);
				node.appendChild(tmp);

				// Moves the id attribute to the outermost
				// XML node, namely the node which denotes
				// the object boundaries in the file.
				String id = tmp.getAttribute("id");
				((Element) node).setAttribute("id", id);
				tmp.removeAttribute("id");
			}
			
			Node portsNode = enc.getDocument().createElement("Ports");
			Node inputportsNode = enc.getDocument().createElement("InputPorts");
			portsNode.appendChild(inputportsNode);
			
			Node outputportsNode = enc.getDocument().createElement("OutputPorts");
			portsNode.appendChild(outputportsNode);
			
			for (Port port : activity.getInputPorts()) {
				Node portNode = enc.encode(port);
				inputportsNode.appendChild(portNode);
			}
			
			Node portNode = enc.encode(activity.getOutputPort());
			if(portNode != null)
				outputportsNode.appendChild(portNode);
			
			node.appendChild(portsNode);
		}

		return node;
	}

	/**
	 * Decodes an mxCell and uses the enclosing XML node as
	 * the user object for the cell (inversion).
	 */
	public Node beforeDecode(mxCodec dec, Node node, Object obj)
	{
		Element inner = (Element) node;

		if (obj instanceof Activity)
		{
			Activity activity = (Activity) obj;
			String classname = getName();
			String nodeName = node.getNodeName();
			
			// Handles aliased names
			if (!nodeName.equals(classname))
			{
				//String tmp = mxCodecRegistry.getaliases.get(nodeName);
				String tmp = null;
				
				if (tmp != null)
				{
					nodeName = tmp;
				}
			}

			if (!nodeName.equals(classname))
			{
				// Passes the inner graphical annotation node to the
				// object codec for further processing of the cell.
				Node tmp = inner.getElementsByTagName(classname).item(0);

				if (tmp != null && tmp.getParentNode() == node)
				{
					inner = (Element) tmp;

					// Removes annotation and whitespace from node
					Node tmp2 = tmp.getPreviousSibling();

					while (tmp2 != null && tmp2.getNodeType() == Node.TEXT_NODE)
					{
						Node tmp3 = tmp2.getPreviousSibling();

						if (tmp2.getTextContent().trim().length() == 0)
						{
							tmp2.getParentNode().removeChild(tmp2);
						}

						tmp2 = tmp3;
					}

					// Removes more whitespace
					tmp2 = tmp.getNextSibling();

					while (tmp2 != null && tmp2.getNodeType() == Node.TEXT_NODE)
					{
						Node tmp3 = tmp2.getPreviousSibling();

						if (tmp2.getTextContent().trim().length() == 0)
						{
							tmp2.getParentNode().removeChild(tmp2);
						}

						tmp2 = tmp3;
					}

					tmp.getParentNode().removeChild(tmp);
				}
				else
				{
					inner = null;
				}

				// Creates the user object out of the XML node
				Element value = (Element) node.cloneNode(true);
				activity.setValue(value);
				String id = value.getAttribute("id");

				if (id != null)
				{
					activity.setId(id);
					value.removeAttribute("id");
				}
			}
			else
			{
				activity.setId(((Element) node).getAttribute("id"));
			}

			// Preprocesses and removes all Id-references
			// in order to use the correct encoder (this)
			// for the known references to cells (all).
			if (inner != null && idrefs != null)
			{
				Iterator<String> it = idrefs.iterator();

				while (it.hasNext())
				{
					String attr = it.next();
					String ref = inner.getAttribute(attr);

					if (ref != null && ref.length() > 0)
					{
						inner.removeAttribute(attr);
						Object object = dec.getObjects().get(ref);

						if (object == null)
						{
							object = dec.lookup(ref);
						}

						if (object == null)
						{
							// Needs to decode forward reference
							Node element = dec.getElementById(ref);

							if (element != null)
							{
								mxObjectCodec decoder = mxCodecRegistry
										.getCodec(element.getNodeName());

								if (decoder == null)
								{
									decoder = this;
								}

								object = decoder.decode(dec, element);
							}
						}

						setFieldValue(obj, attr, object);
					}
				}
			}
		}

		return inner;
	}
	
	@Override
	public Object afterDecode(mxCodec dec, Node node, Object obj) {
		
		Activity activity = (Activity) obj;
				
		Node portsNode = node.getLastChild();
		
		Node inputPortsNode = portsNode.getFirstChild();
		
		Node outputPortNode = portsNode.getLastChild().getFirstChild();
		
		NodeList inputPortsList = inputPortsNode.getChildNodes();
		
		for (int i = 0; i < inputPortsList.getLength(); i++) {
			Port port = (Port) dec.decode(inputPortsList.item(i));
			activity.addInputPort(port);
			
		}
		
		Object port = dec.decode(outputPortNode);
		
		if(outputPortNode != null)
			activity.setOutputPort((Port) port);
		
		return obj;
	}

}
