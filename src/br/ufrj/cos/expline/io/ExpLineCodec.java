/**
 * $Id: mxModelCodec.java,v 1.2 2013/10/28 08:45:08 gaudenz Exp $
 * Copyright (c) 2006-2013, Gaudenz Alder, David Benson
 */
package br.ufrj.cos.expline.io;

import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import br.ufrj.cos.expline.model.ExpLine;

import com.mxgraph.io.mxCodec;
import com.mxgraph.io.mxObjectCodec;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;

/**
 * Codec for mxGraphModels. This class is created and registered
 * dynamically at load time and used implicitly via mxCodec
 * and the mxCodecRegistry.
 */
public class ExpLineCodec extends mxObjectCodec
{

	/**
	 * Constructs a new model codec.
	 */
	public ExpLineCodec()
	{
		this(new ExpLine(), null, new String[] { "rules" },
				null);
	}

	/**
	 * Constructs a new model codec for the given template.
	 */
	public ExpLineCodec(Object template)
	{
		this(template, null, null, null);
	}

	/**
	 * Constructs a new model codec for the given arguments.
	 */
	public ExpLineCodec(Object template, String[] exclude, String[] idrefs,
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
		if (obj instanceof mxGraphModel)
		{
			Node rootNode = enc.getDocument().createElement("root");
			mxGraphModel model = (mxGraphModel) obj;
			enc.encodeCell((mxICell) model.getRoot(), rootNode, true);
			node.appendChild(rootNode);
		}
	}
	
	/**
	 * Encodes an mxCell and wraps the XML up inside the
	 * XML of the user object (inversion).
	 */
	public Node afterEncode(mxCodec enc, Object obj, Node node)
	{
		
		Node rootNode = enc.getDocument().createElement("Test");
		ExpLine model = (ExpLine) obj;
		Node rulesNode = enc.encode(model.getRules());
		node.appendChild(rootNode);
		
//		if (obj instanceof mxCell)
//		{
//			mxCell cell = (mxCell) obj;
//
//			if (cell.getValue() instanceof Node)
//			{
//				// Wraps the graphical annotation up in the
//				// user object (inversion) by putting the
//				// result of the default encoding into
//				// a clone of the user object (node type 1)
//				// and returning this cloned user object.
//				Element tmp = (Element) node;
//				node = enc.getDocument().importNode((Node) cell.getValue(),
//						true);
//				node.appendChild(tmp);
//
//				// Moves the id attribute to the outermost
//				// XML node, namely the node which denotes
//				// the object boundaries in the file.
//				String id = tmp.getAttribute("id");
//				((Element) node).setAttribute("id", id);
//				tmp.removeAttribute("id");
//			}
//		}

		return node;
	}


	/**
	 * Reads the cells into the graph model. All cells are children of the root
	 * element in the node.
	 */
	public Node beforeDecode(mxCodec dec, Node node, Object into)
	{
		if (node instanceof Element)
		{
			Element elt = (Element) node;
			mxGraphModel model = null;

			if (into instanceof mxGraphModel)
			{
				model = (mxGraphModel) into;
			}
			else
			{
				model = new mxGraphModel();
			}

			// Reads the cells into the graph model. All cells
			// are children of the root element in the node.
			Node root = elt.getElementsByTagName("root").item(0);
			mxICell rootCell = null;

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
