/*
 * $Id: Actions.java,v 1.1 2012/11/15 13:26:46 gaudenz Exp $
 * Copyright (c) 2001-2012, JGraph Ltd
 */
package br.ufrj.cos.expline.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.w3c.dom.Document;

import br.ufrj.cos.expline.derivation.Derivation;
import br.ufrj.cos.expline.io.Activity2Codec;
import br.ufrj.cos.expline.io.ActivityCodec;
import br.ufrj.cos.expline.io.EdgeCodec;
import br.ufrj.cos.expline.main.ScicumulusInstantiator;
import br.ufrj.cos.expline.model.Activity;
import br.ufrj.cos.expline.model.Edge;
import br.ufrj.cos.expline.model.ExpLine;
import br.ufrj.cos.expline.model.Port;
import br.ufrj.cos.expline.model.AbstractWorkflow;

import com.mxgraph.io.mxCodec;
import com.mxgraph.io.mxCodecRegistry;
import com.mxgraph.io.mxGdCodec;
import com.mxgraph.io.mxObjectCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.util.png.mxPngEncodeParam;
import com.mxgraph.util.png.mxPngImageEncoder;
import com.mxgraph.util.png.mxPngTextDecoder;
import com.mxgraph.view.mxGraph;

/**
 *
 */
public class Actions
{
	/**
	 * 
	 * @param e
	 * @return Returns the graph for the given action event.
	 */
	public static final ExpLineEditor getEditor(ActionEvent e)
	{
		if (e.getSource() instanceof Component)
		{
			Component component = (Component) e.getSource();

			while (component != null
					&& !(component instanceof ExpLineEditor))
			{
				component = component.getParent();
			}

			return (ExpLineEditor) component;
		}

		return null;
	}

	
	
	//ExpLine-Begin
	
	/**
	 *
	 */
	@SuppressWarnings("serial")
	public static class AlgebraicOperatorAction extends AbstractAction
	{
		
		/**
		 * 
		 */
		protected String algebraicOperator;

		/**
		 * 
		 */
		public AlgebraicOperatorAction(String algebraicOperator)
		{
			this.algebraicOperator = algebraicOperator;
		}
		
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			
			ExpLineEditor editor = getEditor(e);
			
			mxGraphComponent graphComponent = editor
					.getGraphComponent();
			mxGraph graph = graphComponent.getGraph();
			
			Activity activity = (Activity) graph.getSelectionCell();
			
			if(!algebraicOperator.equals(mxResources.get("join")) && activity.getInputPorts().size()>1)
				checkAffectedPorts(editor, activity.getInputPort(1));
			
			
			activity.setAlgebraicOperator(algebraicOperator);
			
			activity.refreshPortsDefinition();
			
			graph.refresh();
			
			
		}
		
		public void checkAffectedPorts(ExpLineEditor editor, Port port){
			
			mxGraph graph = editor.getGraphComponent().getGraph();
			
			int portNumbers = graph.getModel().getEdgeCount(port);
			
			for (int i = 0; i < portNumbers; i++) {
				
				Edge edge = (Edge) graph.getModel().getEdgeAt(port, i);
				
				graph.getModel().remove(edge);
			}
			
		}
	}
	
	@SuppressWarnings("serial")
	public static class AlgebraicOperatorOptionItem extends JRadioButtonMenuItem
	{
		
		/**
		 * 
		 */
		public AlgebraicOperatorOptionItem(ButtonGroup group, final ExpLineEditor editor, String name, boolean selected)
		{
			super(name);
			setSelected(selected);
			group.add(this);

			addActionListener(editor.bind(mxResources.get("albegraicOperator"), new AlgebraicOperatorAction(name)));
		}
	}
	
	//ExpLine-End

	/**
	 *
	 */
	@SuppressWarnings("serial")
	public static class ExitAction extends AbstractAction
	{
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			ExpLineEditor editor = getEditor(e);

			if (editor != null)
			{
				editor.exit();
			}
		}
	}

	/**
	 *
	 */
	@SuppressWarnings("serial")
	public static class ScaleAction extends AbstractAction
	{
		/**
		 * 
		 */
		protected double scale;

		/**
		 * 
		 */
		public ScaleAction(double scale)
		{
			this.scale = scale;
		}

		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() instanceof mxGraphComponent)
			{
				mxGraphComponent graphComponent = (mxGraphComponent) e
						.getSource();
				double scale = this.scale;

				if (scale == 0)
				{
					String value = (String) JOptionPane.showInputDialog(
							graphComponent, mxResources.get("value"),
							mxResources.get("scale") + " (%)",
							JOptionPane.PLAIN_MESSAGE, null, null, "");

					if (value != null)
					{
						scale = Double.parseDouble(value.replace("%", "")) / 100;
					}
				}

				if (scale > 0)
				{
					graphComponent.zoomTo(scale, graphComponent.isCenterZoom());
				}
			}
		}
	}

	/**
	 *
	 */
	@SuppressWarnings("serial")
	public static class PageSetupAction extends AbstractAction
	{
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() instanceof mxGraphComponent)
			{
				mxGraphComponent graphComponent = (mxGraphComponent) e
						.getSource();
				PrinterJob pj = PrinterJob.getPrinterJob();
				PageFormat format = pj.pageDialog(graphComponent
						.getPageFormat());

				if (format != null)
				{
					graphComponent.setPageFormat(format);
					graphComponent.zoomAndCenter();
				}
			}
		}
	}

	/**
	 *
	 */
	@SuppressWarnings("serial")
	public static class PrintAction extends AbstractAction
	{
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() instanceof mxGraphComponent)
			{
				mxGraphComponent graphComponent = (mxGraphComponent) e
						.getSource();
				PrinterJob pj = PrinterJob.getPrinterJob();

				if (pj.printDialog())
				{
					PageFormat pf = graphComponent.getPageFormat();
					Paper paper = new Paper();
					double margin = 36;
					paper.setImageableArea(margin, margin, paper.getWidth()
							- margin * 2, paper.getHeight() - margin * 2);
					pf.setPaper(paper);
					pj.setPrintable(graphComponent, pf);

					try
					{
						pj.print();
					}
					catch (PrinterException e2)
					{
						System.out.println(e2);
					}
				}
			}
		}
	}

	/**
	 *
	 */
	@SuppressWarnings("serial")
	public static class SaveAction extends AbstractAction
	{
		/**
		 * 
		 */
		protected boolean showDialog;

		/**
		 * 
		 */
		protected String lastDir = null;

		/**
		 * 
		 */
		public SaveAction(boolean showDialog)
		{
			this.showDialog = showDialog;
		}

		/**
		 * Saves XML+PNG format.
		 */
		protected void saveXmlPng(ExpLineEditor editor, String filename,
				Color bg) throws IOException
		{
			mxGraphComponent graphComponent = editor.getGraphComponent();
			mxGraph graph = graphComponent.getGraph();

			// Creates the image for the PNG file
			BufferedImage image = mxCellRenderer.createBufferedImage(graph,
					null, 1, bg, graphComponent.isAntiAlias(), null,
					graphComponent.getCanvas());

			// Creates the URL-encoded XML data
			mxCodec codec = new mxCodec();
			String xml = URLEncoder.encode(
					mxXmlUtils.getXml(codec.encode(graph.getModel())), "UTF-8");
			mxPngEncodeParam param = mxPngEncodeParam
					.getDefaultEncodeParam(image);
			param.setCompressedText(new String[] { "mxGraphModel", xml });

			// Saves as a PNG file
			FileOutputStream outputStream = new FileOutputStream(new File(
					filename));
			try
			{
				mxPngImageEncoder encoder = new mxPngImageEncoder(outputStream,
						param);

				if (image != null)
				{
					encoder.encode(image);

					editor.setModified(false);
					editor.setCurrentFile(new File(filename));
				}
				else
				{
					JOptionPane.showMessageDialog(graphComponent,
							mxResources.get("noImageData"));
				}
			}
			finally
			{
				outputStream.close();
			}
		}

		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			ExpLineEditor editor = getEditor(e);

			if (editor != null)
			{
				mxGraphComponent graphComponent = editor.getGraphComponent();
				mxGraph graph = graphComponent.getGraph();
				FileFilter selectedFilter = null;
				DefaultFileFilter mxeFilter = new DefaultFileFilter(".xpl",
						"ExpLine Editor " + mxResources.get("file")
						+ " (.xpl)");
				String filename = null;
				boolean dialogShown = false;

				if (showDialog || editor.getCurrentFile() == null)
				{
					String wd;

					if (lastDir != null)
					{
						wd = lastDir;
					}
					else if (editor.getCurrentFile() != null)
					{
						wd = editor.getCurrentFile().getParent();
					}
					else
					{
						wd = System.getProperty("user.dir");
					}

					JFileChooser fc = new JFileChooser(wd);

					// Adds the default file format
					FileFilter defaultFilter = mxeFilter;
					fc.addChoosableFileFilter(defaultFilter);

					fc.setFileFilter(defaultFilter);
					int rc = fc.showDialog(null, mxResources.get("save"));
					dialogShown = true;

					if (rc != JFileChooser.APPROVE_OPTION)
					{
						return;
					}
					else
					{
						lastDir = fc.getSelectedFile().getParent();
					}

					filename = fc.getSelectedFile().getAbsolutePath();
					selectedFilter = fc.getFileFilter();

					if (selectedFilter instanceof DefaultFileFilter)
					{
						String ext = ((DefaultFileFilter) selectedFilter)
								.getExtension();

						if (!filename.toLowerCase().endsWith(ext))
						{
							filename += ext;
						}
					}

					if (new File(filename).exists()
							&& JOptionPane.showConfirmDialog(graphComponent,
									mxResources.get("overwriteExistingFile")) != JOptionPane.YES_OPTION)
					{
						return;
					}
				}
				else
				{
					filename = editor.getCurrentFile().getAbsolutePath();
				}

				try
				{
					String ext = filename
							.substring(filename.lastIndexOf('.') + 1);

					
					if (ext.equalsIgnoreCase("xpl")
							|| ext.equalsIgnoreCase("xml"))
					{
						mxCodec codec = new mxCodec();
						String xml = mxXmlUtils.getXml(codec.encode(graph
								.getModel()));

						mxUtils.writeFile(xml, filename);

						editor.setModified(false);
						editor.setCurrentFile(new File(filename));
					}
				}
				catch (Throwable ex)
				{
					ex.printStackTrace();
					JOptionPane.showMessageDialog(graphComponent,
							ex.toString(), mxResources.get("error"),
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	
	/**
	 *
	 */
	@SuppressWarnings("serial")
	public static class GenerateAbstractWorkflowAction extends AbstractAction
	{
		/**
		 * 
		 */
		protected boolean showDialog;

		/**
		 * 
		 */
		protected String lastDir = null;

		/**
		 * 
		 */
		public GenerateAbstractWorkflowAction(boolean showDialog)
		{
			this.showDialog= showDialog;
		}

		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			ExpLineEditor editor = getEditor(e);
			
			Derivation derivation = editor.getDerivation();
			
			if(!derivation.validatesDerivedWorkflow()){
				
				JOptionPane.showMessageDialog(editor,
						"Derivation Status: Selection is not Valid");
				
				return;
			}
			

			if (editor != null)
			{
				mxGraphComponent graphComponent = editor.getGraphComponent();
				FileFilter selectedFilter = null;
				DefaultFileFilter mxeFilter = new DefaultFileFilter(".xml",
						"Abstract Workflow " + mxResources.get("file")
						+ " (.xml)");
				String filename = null;
				boolean dialogShown = false;

				if (showDialog || editor.getCurrentFile() == null)
				{
					String wd;

					if (lastDir != null)
					{
						wd = lastDir;
					}
					else if (editor.getCurrentFile() != null)
					{
						wd = editor.getCurrentFile().getParent();
					}
					else
					{
						wd = System.getProperty("user.dir");
					}

					JFileChooser fc = new JFileChooser(wd);

					// Adds the default file format
					FileFilter defaultFilter = mxeFilter;
					fc.addChoosableFileFilter(defaultFilter);

					fc.setFileFilter(defaultFilter);
					int rc = fc.showDialog(null, mxResources.get("save"));
					dialogShown = true;

					if (rc != JFileChooser.APPROVE_OPTION)
					{
						return;
					}
					else
					{
						lastDir = fc.getSelectedFile().getParent();
					}

					filename = fc.getSelectedFile().getAbsolutePath();
					selectedFilter = fc.getFileFilter();

					if (selectedFilter instanceof DefaultFileFilter)
					{
						String ext = ((DefaultFileFilter) selectedFilter)
								.getExtension();

						if (!filename.toLowerCase().endsWith(ext))
						{
							filename += ext;
						}
					}

					if (new File(filename).exists()
							&& JOptionPane.showConfirmDialog(graphComponent,
									mxResources.get("overwriteExistingFile")) != JOptionPane.YES_OPTION)
					{
						return;
					}
				}
				else
				{
					filename = editor.getCurrentFile().getAbsolutePath();
				}

				try
				{
					String ext = filename
							.substring(filename.lastIndexOf('.') + 1);

					
					if (ext.equalsIgnoreCase("xml"))
					{
						
						
						derivation = editor.getDerivation();
						
						AbstractWorkflow workflow = derivation.derive();	
						
						
						mxCodecRegistry.register(new Activity2Codec());
						mxCodecRegistry.register(new mxObjectCodec(new Port(), new String[] { "type", "parent", "geometry" }, new String[] { "parent", "source", "target" },
								null));
						mxCodecRegistry.register(new mxObjectCodec(new Edge(), new String[] { "edge", "value", "type", "style", "parent", "geometry", "vertex" }, new String[] { "parent", "source", "target" },
								null));
						
						mxCodec codec = new mxCodec();
						String xml = mxXmlUtils.getXml(codec.encode(workflow));
						
						mxCodecRegistry.register(new ActivityCodec());
						mxCodecRegistry.register(new mxObjectCodec(new Port(), null, new String[] { "parent", "source", "target" },
								null));
						mxCodecRegistry.register(new EdgeCodec());
						

						mxUtils.writeFile(xml, filename);

						
						JOptionPane.showMessageDialog(editor,
								"Derivation Status: Workflow Derived Successfully");
						
						
					}
				}
				catch (Throwable ex)
				{
					ex.printStackTrace();
					JOptionPane.showMessageDialog(graphComponent,
							ex.toString(), mxResources.get("error"),
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	
	/**
	 *
	 */
	@SuppressWarnings("serial")
	public static class GenerateConcreteWorkflowAction extends AbstractAction
	{
		/**
		 * 
		 */
		protected boolean showDialog;

		/**
		 * 
		 */
		protected String lastDir = null;

		/**
		 * 
		 */
		public GenerateConcreteWorkflowAction(boolean showDialog)
		{
			this.showDialog= showDialog;
		}
		
		public File generateAbstractWorkflow(Derivation derivation){
			
			
			AbstractWorkflow workflow = derivation.derive();	
			
			
			mxCodecRegistry.register(new Activity2Codec());
			mxCodecRegistry.register(new mxObjectCodec(new Port(), new String[] { "type", "parent", "geometry" }, new String[] { "parent", "source", "target" },
					null));
			mxCodecRegistry.register(new mxObjectCodec(new Edge(), new String[] { "edge", "value", "type", "style", "parent", "geometry", "vertex" }, new String[] { "parent", "source", "target" },
					null));
			
			mxCodec codec = new mxCodec();
			String xml = mxXmlUtils.getXml(codec.encode(workflow));
			
			mxCodecRegistry.register(new ActivityCodec());
			mxCodecRegistry.register(new mxObjectCodec(new Port(), null, new String[] { "parent", "source", "target" },
					null));
			mxCodecRegistry.register(new EdgeCodec());
			

			File file = null;
			try {
				file = File.createTempFile("abstractWorkflow", ".xml");
				mxUtils.writeFile(xml, file.getAbsolutePath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return file;		
		}
		
		public void generateConcreteWorkflow(ExpLineEditor editor, File abstractWorkflow){
			

			mxGraphComponent graphComponent = editor.getGraphComponent();
			FileFilter selectedFilter = null;
			DefaultFileFilter mxeFilter = new DefaultFileFilter(".xml",
					"Scicumulus Workflow File" + mxResources.get("file")
					+ " (.xml)");
			String filename = null;
			boolean dialogShown = false;

			if (showDialog || editor.getCurrentFile() == null)
			{
				String wd;

				if (lastDir != null)
				{
					wd = lastDir;
				}
				else if (editor.getCurrentFile() != null)
				{
					wd = editor.getCurrentFile().getParent();
				}
				else
				{
					wd = System.getProperty("user.dir");
				}

				JFileChooser fc = new JFileChooser(wd);

				// Adds the default file format
				FileFilter defaultFilter = mxeFilter;
				fc.addChoosableFileFilter(defaultFilter);

				fc.setFileFilter(defaultFilter);
				int rc = fc.showDialog(null, mxResources.get("save"));
				dialogShown = true;

				if (rc != JFileChooser.APPROVE_OPTION)
				{
					return;
				}
				else
				{
					lastDir = fc.getSelectedFile().getParent();
				}

				filename = fc.getSelectedFile().getAbsolutePath();
				selectedFilter = fc.getFileFilter();

				if (selectedFilter instanceof DefaultFileFilter)
				{
					String ext = ((DefaultFileFilter) selectedFilter)
							.getExtension();

					if (!filename.toLowerCase().endsWith(ext))
					{
						filename += ext;
					}
				}

				if (new File(filename).exists()
						&& JOptionPane.showConfirmDialog(graphComponent,
								mxResources.get("overwriteExistingFile")) != JOptionPane.YES_OPTION)
				{
					return;
				}
			}
			else
			{
				filename = editor.getCurrentFile().getAbsolutePath();
			}

			try
			{
				String ext = filename
						.substring(filename.lastIndexOf('.') + 1);

				
				if (ext.equalsIgnoreCase("xml"))
				{
					
					
					//
					ScicumulusInstantiator scicumulusInstantiator = new ScicumulusInstantiator(); 
					
//					Map<String,String> properties = new HashMap<String, String>();

//					scicumulusInstantiator.instantiate(new File(filename), abstractWorkflow, properties);
					
					
					JFrame frame = (JFrame) SwingUtilities.windowForComponent(editor);
					
					scicumulusInstantiator.instantiate(frame, abstractWorkflow, new File(filename));
					
//					JOptionPane.showMessageDialog(editor,
//							"Derivation Status: Workflow Derived Successfully");
					
					
				}
			}
			catch (Throwable ex)
			{
				ex.printStackTrace();
				JOptionPane.showMessageDialog(graphComponent,
						ex.toString(), mxResources.get("error"),
						JOptionPane.ERROR_MESSAGE);
			}
		
			
		}
		
		public boolean isDerivedWorkflowValid(Derivation derivation){
			return derivation.validatesDerivedWorkflow();
		}

		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			ExpLineEditor editor = getEditor(e);
			
			Derivation derivation = editor.getDerivation();
			
			if(isDerivedWorkflowValid(derivation)){
				
				File abstractWorkflow = generateAbstractWorkflow(derivation);
				
				generateConcreteWorkflow(editor, abstractWorkflow);
			}

		}
	}
	
	/**
	 *
	 */
	@SuppressWarnings("serial")
	public static class HistoryAction extends AbstractAction
	{
		/**
		 * 
		 */
		protected boolean undo;

		/**
		 * 
		 */
		public HistoryAction(boolean undo)
		{
			this.undo = undo;
		}

		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			ExpLineEditor editor = getEditor(e);

			if (editor != null)
			{
				if (undo)
				{
					editor.getUndoManager().undo();
				}
				else
				{
					editor.getUndoManager().redo();
				}
			}
		}
	}

	/**
	 *
	 */
	@SuppressWarnings("serial")
	public static class NewAction extends AbstractAction
	{
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			ExpLineEditor editor = getEditor(e);

			if (editor != null)
			{
				if (!editor.isModified()
						|| JOptionPane.showConfirmDialog(editor,
								mxResources.get("loseChanges")) == JOptionPane.YES_OPTION)
				{
					mxGraph graph = editor.getGraphComponent().getGraph();

					// Check modified flag and display save dialog
					mxCell root = new mxCell();
					root.insert(new mxCell());
					
					ExpLine model = (ExpLine) graph.getModel();
					model.clearRules();
					
					graph.getModel().setRoot(root);

					editor.setModified(false);
					editor.setCurrentFile(null);
					editor.getGraphComponent().zoomAndCenter();
				}
			}
		}
	}

	/**
	 *
	 */
	@SuppressWarnings("serial")
	public static class OpenAction extends AbstractAction
	{
		/**
		 * 
		 */
		protected String lastDir;

		/**
		 * 
		 */
		protected void resetEditor(ExpLineEditor editor)
		{
			editor.setModified(false);
			editor.getUndoManager().clear();
			editor.getGraphComponent().zoomAndCenter();
		}

		/**
		 * Reads XML+PNG format.
		 */
		protected void openXmlPng(ExpLineEditor editor, File file)
				throws IOException
		{
			Map<String, String> text = mxPngTextDecoder
					.decodeCompressedText(new FileInputStream(file));

			if (text != null)
			{
				String value = text.get("mxGraphModel");

				if (value != null)
				{
					Document document = mxXmlUtils.parseXml(URLDecoder.decode(
							value, "UTF-8"));
					mxCodec codec = new mxCodec(document);
					codec.decode(document.getDocumentElement(), editor
							.getGraphComponent().getGraph().getModel());
					editor.setCurrentFile(file);
					resetEditor(editor);

					return;
				}
			}

			JOptionPane.showMessageDialog(editor,
					mxResources.get("imageContainsNoDiagramData"));
		}

		/**
		 * @throws IOException
		 *
		 */
		protected void openGD(ExpLineEditor editor, File file,
				String gdText)
		{
			mxGraph graph = editor.getGraphComponent().getGraph();

			// Replaces file extension with .mxe
			String filename = file.getName();
			filename = filename.substring(0, filename.length() - 4) + ".mxe";

			if (new File(filename).exists()
					&& JOptionPane.showConfirmDialog(editor,
							mxResources.get("overwriteExistingFile")) != JOptionPane.YES_OPTION)
			{
				return;
			}

			((mxGraphModel) graph.getModel()).clear();
			mxGdCodec.decode(gdText, graph);
			editor.getGraphComponent().zoomAndCenter();
			editor.setCurrentFile(new File(lastDir + "/" + filename));
		}

		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			ExpLineEditor editor = getEditor(e);

			if (editor != null)
			{
				if (!editor.isModified()
						|| JOptionPane.showConfirmDialog(editor,
								mxResources.get("loseChanges")) == JOptionPane.YES_OPTION)
				{
					mxGraph graph = editor.getGraphComponent().getGraph();

					if (graph != null)
					{
						String wd = (lastDir != null) ? lastDir : System
								.getProperty("user.dir");

						JFileChooser fc = new JFileChooser(wd);

						// Adds file filter for supported file format
						DefaultFileFilter defaultFilter = new DefaultFileFilter(".xpl",
								"ExpLine Editor " + mxResources.get("file")
								+ " (.xpl)")
						{

							public boolean accept(File file)
							{
								String lcase = file.getName().toLowerCase();

								return super.accept(file)
										|| lcase.endsWith(".png")
										|| lcase.endsWith(".vdx");
							}
						};
						fc.addChoosableFileFilter(defaultFilter);

						fc.setFileFilter(defaultFilter);

						int rc = fc.showDialog(null,
								mxResources.get("openFile"));

						if (rc == JFileChooser.APPROVE_OPTION)
						{
							lastDir = fc.getSelectedFile().getParent();

							try
							{
								Document document = mxXmlUtils
										.parseXml(mxUtils.readFile(fc
												.getSelectedFile()
												.getAbsolutePath()));

								mxCodec codec = new mxCodec(document);
								codec.decode(
										document.getDocumentElement(),
										graph.getModel());
								editor.setCurrentFile(fc
										.getSelectedFile());

								resetEditor(editor);
							}
							catch (IOException ex)
							{
								ex.printStackTrace();
								JOptionPane.showMessageDialog(
										editor.getGraphComponent(),
										ex.toString(),
										mxResources.get("error"),
										JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
			}
		}
	}

	/**
	 *
	 */
	@SuppressWarnings("serial")
	public static class AlignCellsAction extends AbstractAction
	{
		/**
		 * 
		 */
		protected String align;

		/**
		 * 
		 * @param key
		 */
		public AlignCellsAction(String align)
		{
			this.align = align;
		}

		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			mxGraph graph = mxGraphActions.getGraph(e);

			if (graph != null && !graph.isSelectionEmpty())
			{
				graph.alignCells(align);
			}
		}
	}

	//ExpLine-Begin
	/**
	 *
	 */
	@SuppressWarnings("serial")
	public static class AlgbraicOperatorAction extends AbstractAction
	{
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() instanceof mxGraphComponent)
			{
				mxGraphComponent graphComponent = (mxGraphComponent) e
						.getSource();
				mxGraph graph = graphComponent.getGraph();

			}
		}
	}
	
	//ExpLine-End
}
