/**
 * $Id: mxModelCodec.java,v 1.2 2013/10/28 08:45:08 gaudenz Exp $
 * Copyright (c) 2006-2013, Gaudenz Alder, David Benson
 */
package br.ufrj.cos.expline.io;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.ufrj.cos.expline.model.Activity;
import br.ufrj.cos.expline.model.Expression;

import com.mxgraph.io.mxCodec;
import com.mxgraph.io.mxObjectCodec;

/**
 * Codec for mxGraphModels. This class is created and registered
 * dynamically at load time and used implicitly via mxCodec
 * and the mxCodecRegistry.
 */
public class ExpressionCodec extends mxObjectCodec
{

	/**
	 * Constructs a new model codec.
	 */
	public ExpressionCodec()
	{
		this(new Expression(), new String[] { "activities" }, null,
				null);
	}

	/**
	 * Constructs a new model codec for the given template.
	 */
	public ExpressionCodec(Object template)
	{
		this(template, null, null, null);
	}

	/**
	 * Constructs a new model codec for the given arguments.
	 */
	public ExpressionCodec(Object template, String[] exclude, String[] idrefs,
			Map<String, String> mapping)
	{
		super(template, exclude, idrefs, mapping);
	}
	
	/**
	 * Encodes an mxCell and wraps the XML up inside the
	 * XML of the user object (inversion).
	 */
	public Node afterEncode(mxCodec enc, Object obj, Node node)
	{	

		Node activitiesNode = enc.getDocument().createElement("Activities");
		Expression exp = (Expression) obj;
		
		for (Activity activity : exp.getActivities()) {
			Node idNode = enc.getDocument().createElement("id");
			idNode.setTextContent(activity.getId());
			activitiesNode.appendChild(idNode);
		}
		
		node.appendChild(activitiesNode);

		return node;
	}

	@Override
	public Object afterDecode(mxCodec dec, Node node, Object obj) {
		
		List<Activity> activities = new ArrayList<Activity>();
		
		Node activitiesNode = node.getLastChild();
		
		NodeList ids = activitiesNode.getChildNodes();
		
		for (int i = 0; i < ids.getLength(); i++) {
			String id = ids.item(i).getTextContent().trim();
			Object temp = dec.getObject(id);
			activities.add((Activity)temp);
		}
		
		Expression exp = (Expression) obj;
		exp.setActivities(activities);
		
		return obj;
	}
	
	

}
