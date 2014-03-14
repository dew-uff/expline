/**
 * $Id: ExpLineEditor.java,v 1.2 2012/11/20 09:08:09 gaudenz Exp $
 * Copyright (c) 2006-2012, JGraph Ltd */
package br.ufrj.cos.expline.swing;

import java.awt.Color;
import java.awt.Point;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.w3c.dom.Document;

import br.ufrj.cos.expline.io.ActivityCodec;
import br.ufrj.cos.expline.io.ExpLineCodec;
import br.ufrj.cos.expline.model.Activity;
import br.ufrj.cos.expline.model.ExpLine;
import br.ufrj.cos.expline.model.Expression;
import br.ufrj.cos.expline.model.RelationSchema;
import br.ufrj.cos.expline.model.RelationSchemaAttribute;
import br.ufrj.cos.expline.model.Rule;
import br.ufrj.cos.expline.swing.editor.BasicGraphEditor;
import br.ufrj.cos.expline.swing.editor.EditorMenuBar;
import br.ufrj.cos.expline.swing.editor.EditorPalette;
import br.ufrj.cos.expline.swing.handler.ConnectionHandler;
import br.ufrj.cos.expline.swing.handler.EdgeHandler;
import br.ufrj.cos.expline.swing.handler.ElbowEdgeHandler;

import com.mxgraph.io.mxCodec;
import com.mxgraph.io.mxCodecRegistry;
import com.mxgraph.io.mxObjectCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxCellHandler;
import com.mxgraph.swing.handler.mxVertexHandler;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxEdgeStyle.mxEdgeStyleFunction;
import com.mxgraph.view.mxGraph;

public class ExpLineEditor extends BasicGraphEditor
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4601740824088314699L;

	/**
	 * Holds the shared number formatter. 
	 * 
	 * @see NumberFormat#getInstance()
	 */
	public static final NumberFormat numberFormat = NumberFormat.getInstance();

	/**
	 * Holds the URL for the icon to be used as a handle for creating new
	 * connections. This is currently unused.
	 */
	public static URL url = null;

	//ExpLineEditor.class.getResource("/images/connector.gif");

	public ExpLineEditor()
	{	
		this("ExpLine", new ExpLineGraphComponent(new ExpLineGraph()));
		
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}

		mxSwingConstants.SHADOW_COLOR = Color.LIGHT_GRAY;
		mxConstants.W3C_SHADOWCOLOR = "#D3D3D3";
		
		mxCodecRegistry.addPackage("br.ufrj.cos.expline.model");
		mxCodecRegistry.register(new ExpLineCodec());
		mxCodecRegistry.register(new ActivityCodec());
		mxCodecRegistry.register(new mxObjectCodec(new RelationSchema()));
		mxCodecRegistry.register(new mxObjectCodec(new RelationSchemaAttribute()));
		mxCodecRegistry.register(new mxObjectCodec(new ArrayList<RelationSchema>()));
		mxCodecRegistry.register(new mxObjectCodec(new ArrayList<RelationSchemaAttribute>()));
		mxCodecRegistry.register(new mxObjectCodec(new Rule()));
		mxCodecRegistry.register(new mxObjectCodec(new Expression()));
		mxCodecRegistry.register(new mxObjectCodec(new ArrayList<Rule>()));
		mxCodecRegistry.register(new mxObjectCodec(new ArrayList<Expression>()));
		//mxCodecRegistry.addAlias("br.ufrj.cos.expline.model.Activity", "Activity");

		this.createFrame(new EditorMenuBar(this)).setVisible(true);
		
		
	}

	/**
	 * 
	 */
	public ExpLineEditor(String appTitle, ExpLineGraphComponent component)
	{
		super(appTitle, component);
		final mxGraph graph = graphComponent.getGraph();

		// Creates the shapes palette
		EditorPalette activitiesPalette = insertPalette(mxResources.get("activities"));
		EditorPalette connectionsPalette = insertPalette(mxResources.get("connections"));

		// Sets the edge template to be used for creating new edges if an edge
		// is clicked in the shape palette
		activitiesPalette.addListener(mxEvent.SELECT, new mxIEventListener()
		{
			public void invoke(Object sender, mxEventObject evt)
			{
				Object tmp = evt.getProperty("transferable");

				if (tmp instanceof mxGraphTransferable)
				{
					mxGraphTransferable t = (mxGraphTransferable) tmp;
					Object cell = t.getCells()[0];

					if (graph.getModel().isEdge(cell))
					{
						((ExpLineGraph) graph).setEdgeTemplate(cell);
					}
				}
			}

		});
		
		
		// Adds experiment line components in the library
		
		activitiesPalette
		.addTemplate(
				"Invariant",
				new ImageIcon(
						ExpLineEditor.class
								.getResource("/images/rounded.png")),
				"rounded=1;strokeWidth=5;fontSize=24", 160, 120, "", "Invariant");
		
		activitiesPalette
		.addTemplate(
				"Optional",
				new ImageIcon(
						ExpLineEditor.class
								.getResource("/images/rounded.png")),
				"rounded=1;dashed=1;strokeWidth=5;fontSize=24", 160, 120, "", "Optional");
		
		activitiesPalette
		.addTemplate(
				"Variation Point",
				new ImageIcon(
						ExpLineEditor.class
								.getResource("/images/doublerectangle.png")),
				"rectangle;shape=doubleRectangle;rounded=1;strokeWidth=5;fontSize=24", 160, 120, "", "VariationPoint");
		activitiesPalette
		.addTemplate(
				"Optional Variation Point",
				new ImageIcon(
						ExpLineEditor.class
								.getResource("/images/doublerectangle.png")),
				"rectangle;shape=doubleRectangle;dashed=1;rounded=1;strokeWidth=5;fontSize=24", 160, 120, "", "OptionalVariationPoint");
		activitiesPalette
		.addTemplate(
				"Variant",
				new ImageIcon(
						ExpLineEditor.class
								.getResource("/images/rounded.png")),
				"strokeWidth=5;fillColor=#FF0033;fontSize=24;cloneable=0", 160, 120, "", "Variant");
		

		connectionsPalette
		.addEdgeTemplate(
				"Workflow edge",
				new ImageIcon(
						ExpLineEditor.class
								.getResource("/images/connect.png")),
				"WorkflowEdge;strokeWidth=8;strokeColor=#000066", 100, 100, "");
		connectionsPalette
		.addEdgeTemplate(
				"Variant relationship",
				new ImageIcon(
						ExpLineEditor.class
								.getResource("/images/arrow.png")),
				"VariantRelationship;arrow;strokeWidth=5;strokeColor=#000000", 120, 120, "");
		

	}

	/**
	 * 
	 */
	public static class ExpLineGraphComponent extends mxGraphComponent
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -6833603133512882012L;

		/**
		 * 
		 * @param graph
		 */
		public ExpLineGraphComponent(mxGraph graph)
		{
			super(graph);

			// Sets switches typically used in an editor
			setPageVisible(false);
			setGridVisible(false);
			setTripleBuffered(true);super.createHandlers();
			setAntiAlias(true);
			setToolTips(true);
			
			getConnectionHandler().setCreateTarget(false);
			// Loads the defalt stylesheet from an external file
			mxCodec codec = new mxCodec();
			Document doc = mxUtils.loadDocument(ExpLineEditor.class.getResource(
					"/resources/default-style.xml")
					.toString());
			codec.decode(doc.getDocumentElement(), graph.getStylesheet());

			// Sets the background to white
			getViewport().setOpaque(true);
			getViewport().setBackground(Color.WHITE);
		}

		/**
		 * Overrides drop behaviour to set the cell style if the target
		 * is not a valid drop target and the cells are of the same
		 * type (eg. both vertices or both edges). 
		 */
		public Object[] importCells(Object[] cells, double dx, double dy,
				Object target, Point location)
		{
			if (target == null && cells.length == 1 && location != null)
			{
				target = getCellAt(location.x, location.y);

				if (target instanceof mxICell && cells[0] instanceof mxICell)
				{
					mxICell targetCell = (mxICell) target;
					mxICell dropCell = (mxICell) cells[0];

					if (targetCell.isVertex() == dropCell.isVertex()
							|| targetCell.isEdge() == dropCell.isEdge())
					{
						mxIGraphModel model = graph.getModel();
						model.setStyle(target, model.getStyle(cells[0]));
						graph.setSelectionCell(target);

						return null;
					}
				}
			}

			return super.importCells(cells, dx, dy, target, location);
		}

		@Override
		protected ConnectionHandler createConnectionHandler() {
			// TODO Auto-generated method stub
			return new ConnectionHandler(this);
		}

		@Override
		public mxCellHandler createHandler(mxCellState state) {
			// TODO Auto-generated method stub
			if (graph.getModel().isVertex(state.getCell()))
			{
				return new mxVertexHandler(this, state);
			}
			else if (graph.getModel().isEdge(state.getCell()))
			{
				mxEdgeStyleFunction style = graph.getView().getEdgeStyle(state,
						null, null, null);

				if (graph.isLoop(state) || style == mxEdgeStyle.ElbowConnector
						|| style == mxEdgeStyle.SideToSide
						|| style == mxEdgeStyle.TopToBottom)
				{
					return new ElbowEdgeHandler(this, state);
				}

				return new EdgeHandler(this, state);
			}

			return new mxCellHandler(this, state);
		}
	}

	/**
	 * A graph that creates new edges from a given template edge.
	 */
	public static class ExpLineGraph extends mxGraph
	{
		/**
		 * Holds the edge to be used as a template for inserting new edges.
		 */
		protected Object edgeTemplate;

		/**
		 * 
		 * 
		 */
		public ExpLineGraph()
		{
			
			//TODO I have to call the builder sending the model....
			super(new ExpLine());
			setMultigraph(false);
			
		}

		/**
		 * Sets the edge template to be used to inserting edges.
		 */
		public void setEdgeTemplate(Object template)
		{
			edgeTemplate = template;
		}

		/**
		 * Prints out some useful information about the cell in the tooltip.
		 */
		public String getToolTipForCell(Object cell)
		{
			String tip = "";

			if (getModel().isVertex(cell))
			{

				mxCell cell_ = (mxCell) cell;
				String style = cell_.getStyle();
				
				String[] key_values = style.trim().split(";");
				
				for (String key_value : key_values) {
					if(key_value.contains("algebraicOperator")){
						String albebraicOperator = key_value.split("=")[1];
						
						tip += "Algebraic Operator = " + albebraicOperator;
						
						break;
					}
				}
				
			}


			return tip;
		}

		/**
		 * Overrides the method to use the currently selected edge template for
		 * new edges.
		 * 
		 * @param expLineGraph
		 * @param parent
		 * @param id
		 * @param value
		 * @param source
		 * @param target
		 * @param style
		 * @return
		 */
		public Object createEdge(Object parent, String id, Object value,
				Object source, Object target, String style)
		{
			if (edgeTemplate != null)
			{
				mxCell edge = (mxCell) cloneCells(new Object[] { edgeTemplate })[0];
				edge.setId(id);

				return edge;
			}

			return super.createEdge(parent, id, value, source, target, style);
		}

		@Override
		public Object createVertex(Object parent, String id, Object value,
				double x, double y, double width, double height, String style) {
			// TODO Auto-generated method stub
			return super.createVertex(parent, id, value, x, y, width, height, style);
		}

		@Override
		public Object createVertex(Object parent, String id, Object value,
				double x, double y, double width, double height, String style,
				boolean relative) {
			
			mxGeometry geometry = new mxGeometry(x, y, width, height);
			geometry.setRelative(relative);

			Activity vertex = new Activity(value, geometry, style, "");
			vertex.setId(id);
			vertex.setVertex(true);
			vertex.setConnectable(true);

			return vertex;
			
		}

		@Override
		public boolean isCellEditable(Object cell) {
			
			mxCell cell_ = (mxCell) cell;
			if(cell != null && cell_.isVertex())
				return super.isCellEditable(cell);
			else
				return false;
		}

	}
}
