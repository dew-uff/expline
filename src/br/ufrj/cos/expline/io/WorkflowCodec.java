/**
 * $Id: mxModelCodec.java,v 1.2 2013/10/28 08:45:08 gaudenz Exp $
 * Copyright (c) 2006-2013, Gaudenz Alder, David Benson
 */
package br.ufrj.cos.expline.io;

import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import br.ufrj.cos.expline.model.ExpLine;
import br.ufrj.cos.expline.model.Workflow;

import com.mxgraph.io.mxCodec;
import com.mxgraph.io.mxObjectCodec;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;

/**
 * Codec for mxGraphModels. This class is created and registered
 * dynamically at load time and used implicitly via mxCodec
 * and the mxCodecRegistry.
 */
public class WorkflowCodec extends mxObjectCodec
{

	/**
	 * Constructs a new model codec.
	 */
	public WorkflowCodec()
	{
		this(new Workflow(), null, null,
				null);
	}

	/**
	 * Constructs a new model codec for the given template.
	 */
	public WorkflowCodec(Object template)
	{
		this(template, null, null, null);
	}

	/**
	 * Constructs a new model codec for the given arguments.
	 */
	public WorkflowCodec(Object template, String[] exclude, String[] idrefs,
			Map<String, String> mapping)
	{
		super(template, exclude, idrefs, mapping);
	}

	/**
	 * Encodes the given mxGraphModel by writing a (flat) XML sequence
	 * of cell nodes as produced by the mxCellCodec. The sequence is
	 * wrapped-up in a node with the name root.
	 */
	protected void encodeObject(mxCodec enc, Object obj, Node node)
	{

		
		if (obj instanceof Workflow)
		{
			mxGraphModel model = (Workflow) obj;
			
			mxICell root = (mxICell) model.getRoot();
			root = root.getChildAt(0);
			
			for (int i = 0; i < root.getChildCount(); i++) {
				mxICell element = root.getChildAt(i);
				
//				if (element instanceof Activity) 
					enc.encodeCell(element, node, false);
			}
			
//			node.appendChild(rootNode);
		}
		
		
	}
	

	public Node beforeDecode(mxCodec dec, Node node, Object into)
	{
		if (node instanceof Element)
		{
			Element elt = (Element) node;
			Workflow model = null;

			if (into instanceof Workflow)
			{
				model = (Workflow) into;
			}
			else
			{
				model = new Workflow();
			}

			// Reads the cells into the graph model. All cells
			// are children of the root element in the node.
			Node root = elt.getElementsByTagName("Workflow").item(0);
			mxICell rootCell = null;
			
			Node rootEl1 = elt.getOwnerDocument().createElement("mxCell");
			
			Attr id = elt.getOwnerDocument().createAttribute("id");
			id.setValue("0");
			NamedNodeMap rootAttr = rootEl1.getAttributes();
			rootAttr.setNamedItem(id);
			
			root.insertBefore(rootEl1, root.getFirstChild());
			
			
			
			Node rootEl2 = elt.getOwnerDocument().createElement("mxCell");
			
			Attr id2 = elt.getOwnerDocument().createAttribute("id");
			id2.setValue("1");
			Attr parent = elt.getOwnerDocument().createAttribute("parent");
			parent.setValue("0");
			rootAttr = rootEl2.getAttributes();
			rootAttr.setNamedItem(id2);
			rootAttr.setNamedItem(parent);
			
			root.insertBefore(rootEl2, rootEl1);


			if (root != null)
			{
				Node tmp = root.getFirstChild();

				while (tmp != null)
				{
					mxICell cell = dec.decodeCell(tmp, true);

					if (cell != null && cell.getParent() == null)
					{
						rootCell = cell;
					}

					tmp = tmp.getNextSibling();
				}

				root.getParentNode().removeChild(root);
			}

			// Sets the root on the model if one has been decoded
			if (rootCell != null)
			{
				model.setRoot(rootCell);
			}
		}

		return node;
	}
	

}
