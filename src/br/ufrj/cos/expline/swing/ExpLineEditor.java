/**
 * $Id: ExpLineEditor.java,v 1.2 2012/11/20 09:08:09 gaudenz Exp $
 * Copyright (c) 2006-2012, JGraph Ltd */
package br.ufrj.cos.expline.swing;

import java.awt.Color;
import java.awt.Point;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.w3c.dom.Document;

import br.ufrj.cos.expline.swing.editor.BasicGraphEditor;
import br.ufrj.cos.expline.swing.editor.EditorMenuBar;
import br.ufrj.cos.expline.swing.editor.EditorPalette;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
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
		this("mxGraph Editor", new CustomGraphComponent(new CustomGraph()));
	}

	/**
	 * 
	 */
	public ExpLineEditor(String appTitle, mxGraphComponent component)
	{
		super(appTitle, component);
		final mxGraph graph = graphComponent.getGraph();

		// Creates the shapes palette
		EditorPalette shapesPalette = insertPalette(mxResources.get("shapes"));
		EditorPalette imagesPalette = insertPalette(mxResources.get("images"));
		EditorPalette symbolsPalette = insertPalette(mxResources.get("symbols"));

		// Sets the edge template to be used for creating new edges if an edge
		// is clicked in the shape palette
		shapesPalette.addListener(mxEvent.SELECT, new mxIEventListener()
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
						((CustomGraph) graph).setEdgeTemplate(cell);
					}
				}
			}

		});

		// Adds some template cells for dropping into the graph
		shapesPalette
				.addTemplate(
						"Container",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/swimlane.png")),
						"swimlane", 280, 280, "Container");
		shapesPalette
				.addTemplate(
						"Icon",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/rounded.png")),
						"icon;image=/images/wrench.png",
						70, 70, "Icon");
		shapesPalette
				.addTemplate(
						"Label",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/rounded.png")),
						"label;image=/images/gear.png",
						130, 50, "Label");
		shapesPalette
				.addTemplate(
						"Rectangle",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/rectangle.png")),
						null, 160, 120, "");
		shapesPalette
				.addTemplate(
						"Rounded Rectangle",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/rounded.png")),
						"rounded=1", 160, 120, "");
		shapesPalette
				.addTemplate(
						"Ellipse",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/ellipse.png")),
						"ellipse", 160, 160, "");
		shapesPalette
				.addTemplate(
						"Double Ellipse",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/doubleellipse.png")),
						"ellipse;shape=doubleEllipse", 160, 160, "");
		shapesPalette
				.addTemplate(
						"Triangle",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/triangle.png")),
						"triangle", 120, 160, "");
		shapesPalette
				.addTemplate(
						"Rhombus",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/rhombus.png")),
						"rhombus", 160, 160, "");
		shapesPalette
				.addTemplate(
						"Horizontal Line",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/hline.png")),
						"line", 160, 10, "");
		shapesPalette
				.addTemplate(
						"Hexagon",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/hexagon.png")),
						"shape=hexagon", 160, 120, "");
		shapesPalette
				.addTemplate(
						"Cylinder",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/cylinder.png")),
						"shape=cylinder", 120, 160, "");
		shapesPalette
				.addTemplate(
						"Actor",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/actor.png")),
						"shape=actor", 120, 160, "");
		shapesPalette
				.addTemplate(
						"Cloud",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/cloud.png")),
						"ellipse;shape=cloud", 160, 120, "");

		shapesPalette
				.addEdgeTemplate(
						"Straight",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/straight.png")),
						"straight", 120, 120, "");
		shapesPalette
				.addEdgeTemplate(
						"Horizontal Connector",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/connect.png")),
						null, 100, 100, "");
		shapesPalette
				.addEdgeTemplate(
						"Vertical Connector",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/vertical.png")),
						"vertical", 100, 100, "");
		shapesPalette
				.addEdgeTemplate(
						"Entity Relation",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/entity.png")),
						"entity", 100, 100, "");
		shapesPalette
				.addEdgeTemplate(
						"Arrow",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/arrow.png")),
						"arrow", 120, 120, "");

		imagesPalette
				.addTemplate(
						"Bell",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/bell.png")),
						"image;image=/images/bell.png",
						50, 50, "Bell");
		imagesPalette
				.addTemplate(
						"Box",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/box.png")),
						"image;image=/images/box.png",
						50, 50, "Box");
		imagesPalette
				.addTemplate(
						"Cube",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/cube_green.png")),
						"image;image=/images/cube_green.png",
						50, 50, "Cube");
		imagesPalette
				.addTemplate(
						"User",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/dude3.png")),
						"roundImage;image=/images/dude3.png",
						50, 50, "User");
		imagesPalette
				.addTemplate(
						"Earth",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/earth.png")),
						"roundImage;image=/images/earth.png",
						50, 50, "Earth");
		imagesPalette
				.addTemplate(
						"Gear",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/gear.png")),
						"roundImage;image=/images/gear.png",
						50, 50, "Gear");
		imagesPalette
				.addTemplate(
						"Home",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/house.png")),
						"image;image=/images/house.png",
						50, 50, "Home");
		imagesPalette
				.addTemplate(
						"Package",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/package.png")),
						"image;image=/images/package.png",
						50, 50, "Package");
		imagesPalette
				.addTemplate(
						"Printer",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/printer.png")),
						"image;image=/images/printer.png",
						50, 50, "Printer");
		imagesPalette
				.addTemplate(
						"Server",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/server.png")),
						"image;image=/images/server.png",
						50, 50, "Server");
		imagesPalette
				.addTemplate(
						"Workplace",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/workplace.png")),
						"image;image=/images/workplace.png",
						50, 50, "Workplace");
		imagesPalette
				.addTemplate(
						"Wrench",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/wrench.png")),
						"roundImage;image=/images/wrench.png",
						50, 50, "Wrench");

		symbolsPalette
				.addTemplate(
						"Cancel",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/cancel_end.png")),
						"roundImage;image=/images/cancel_end.png",
						80, 80, "Cancel");
		symbolsPalette
				.addTemplate(
						"Error",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/error.png")),
						"roundImage;image=/images/error.png",
						80, 80, "Error");
		symbolsPalette
				.addTemplate(
						"Event",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/event.png")),
						"roundImage;image=/images/event.png",
						80, 80, "Event");
		symbolsPalette
				.addTemplate(
						"Fork",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/fork.png")),
						"rhombusImage;image=/images/fork.png",
						80, 80, "Fork");
		symbolsPalette
				.addTemplate(
						"Inclusive",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/inclusive.png")),
						"rhombusImage;image=/images/inclusive.png",
						80, 80, "Inclusive");
		symbolsPalette
				.addTemplate(
						"Link",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/link.png")),
						"roundImage;image=/images/link.png",
						80, 80, "Link");
		symbolsPalette
				.addTemplate(
						"Merge",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/merge.png")),
						"rhombusImage;image=/images/merge.png",
						80, 80, "Merge");
		symbolsPalette
				.addTemplate(
						"Message",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/message.png")),
						"roundImage;image=/images/message.png",
						80, 80, "Message");
		symbolsPalette
				.addTemplate(
						"Multiple",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/multiple.png")),
						"roundImage;image=/images/multiple.png",
						80, 80, "Multiple");
		symbolsPalette
				.addTemplate(
						"Rule",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/rule.png")),
						"roundImage;image=/images/rule.png",
						80, 80, "Rule");
		symbolsPalette
				.addTemplate(
						"Terminate",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/terminate.png")),
						"roundImage;image=/images/terminate.png",
						80, 80, "Terminate");
		symbolsPalette
				.addTemplate(
						"Timer",
						new ImageIcon(
								ExpLineEditor.class
										.getResource("/images/timer.png")),
						"roundImage;image=/images/timer.png",
						80, 80, "Timer");
	}

	/**
	 * 
	 */
	public static class CustomGraphComponent extends mxGraphComponent
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -6833603133512882012L;

		/**
		 * 
		 * @param graph
		 */
		public CustomGraphComponent(mxGraph graph)
		{
			super(graph);

			// Sets switches typically used in an editor
			setPageVisible(true);
			setGridVisible(true);
			setToolTips(true);
			getConnectionHandler().setCreateTarget(true);

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

	}

	/**
	 * A graph that creates new edges from a given template edge.
	 */
	public static class CustomGraph extends mxGraph
	{
		/**
		 * Holds the edge to be used as a template for inserting new edges.
		 */
		protected Object edgeTemplate;

		/**
		 * Custom graph that defines the alternate edge style to be used when
		 * the middle control point of edges is double clicked (flipped).
		 */
		public CustomGraph()
		{
			setAlternateEdgeStyle("edgeStyle=mxEdgeStyle.ElbowConnector;elbow=vertical");
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
			String tip = "<html>";
			mxGeometry geo = getModel().getGeometry(cell);
			mxCellState state = getView().getState(cell);

			if (getModel().isEdge(cell))
			{
				tip += "points={";

				if (geo != null)
				{
					List<mxPoint> points = geo.getPoints();

					if (points != null)
					{
						Iterator<mxPoint> it = points.iterator();

						while (it.hasNext())
						{
							mxPoint point = it.next();
							tip += "[x=" + numberFormat.format(point.getX())
									+ ",y=" + numberFormat.format(point.getY())
									+ "],";
						}

						tip = tip.substring(0, tip.length() - 1);
					}
				}

				tip += "}<br>";
				tip += "absPoints={";

				if (state != null)
				{

					for (int i = 0; i < state.getAbsolutePointCount(); i++)
					{
						mxPoint point = state.getAbsolutePoint(i);
						tip += "[x=" + numberFormat.format(point.getX())
								+ ",y=" + numberFormat.format(point.getY())
								+ "],";
					}

					tip = tip.substring(0, tip.length() - 1);
				}

				tip += "}";
			}
			else
			{
				tip += "geo=[";

				if (geo != null)
				{
					tip += "x=" + numberFormat.format(geo.getX()) + ",y="
							+ numberFormat.format(geo.getY()) + ",width="
							+ numberFormat.format(geo.getWidth()) + ",height="
							+ numberFormat.format(geo.getHeight());
				}

				tip += "]<br>";
				tip += "state=[";

				if (state != null)
				{
					tip += "x=" + numberFormat.format(state.getX()) + ",y="
							+ numberFormat.format(state.getY()) + ",width="
							+ numberFormat.format(state.getWidth())
							+ ",height="
							+ numberFormat.format(state.getHeight());
				}

				tip += "]";
			}

			mxPoint trans = getView().getTranslate();

			tip += "<br>scale=" + numberFormat.format(getView().getScale())
					+ ", translate=[x=" + numberFormat.format(trans.getX())
					+ ",y=" + numberFormat.format(trans.getY()) + "]";
			tip += "</html>";

			return tip;
		}

		/**
		 * Overrides the method to use the currently selected edge template for
		 * new edges.
		 * 
		 * @param graph
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

	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
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

		ExpLineEditor editor = new ExpLineEditor();
		editor.createFrame(new EditorMenuBar(editor)).setVisible(true);
	}
}
