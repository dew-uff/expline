package br.ufrj.cos.expline.swing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;

import br.ufrj.cos.expline.swing.Actions.HistoryAction;
import br.ufrj.cos.expline.swing.Actions.NewAction;
import br.ufrj.cos.expline.swing.Actions.OpenAction;
import br.ufrj.cos.expline.swing.Actions.PrintAction;
import br.ufrj.cos.expline.swing.Actions.SaveAction;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraphView;

public class ToolBar extends JToolBar
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8015443128436394471L;

	/**
	 * 
	 * @param frame
	 * @param orientation
	 */
	private boolean ignoreZoomChange = false;

	/**
	 * 
	 */
	public ToolBar(final ExpLineEditor editor, int orientation)
	{
		super(orientation);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createEmptyBorder(3, 3, 3, 3), getBorder()));
		setFloatable(false);

		add(editor.bind("New", new NewAction(),
				"/images/new.gif"));
		add(editor.bind("Open", new OpenAction(),
				"/images/open.gif"));
		add(editor.bind("Save", new SaveAction(false),
				"/images/save.gif"));

		addSeparator();

		add(editor.bind("Print", new PrintAction(),
				"/images/print.gif"));

		addSeparator();

		add(editor.bind("Cut", TransferHandler.getCutAction(),
				"/images/cut.gif"));
		add(editor.bind("Copy", TransferHandler.getCopyAction(),
				"/images/copy.gif"));
		add(editor.bind("Paste", TransferHandler.getPasteAction(),
				"/images/paste.gif"));

		addSeparator();

		add(editor.bind("Delete", mxGraphActions.getDeleteAction(),
				"/images/delete.gif"));

		addSeparator();

		add(editor.bind("Undo", new HistoryAction(true),
				"/images/undo.gif"));
		add(editor.bind("Redo", new HistoryAction(false),
				"/images/redo.gif"));

		addSeparator();

		final mxGraphView view = editor.getGraphComponent().getGraph()
				.getView();
		final JComboBox zoomCombo = new JComboBox(new Object[] { "400%",
				"200%", "150%", "100%", "75%", "50%", "25%" });
		zoomCombo.setEditable(true);
		zoomCombo.setMinimumSize(new Dimension(75, 0));
		zoomCombo.setPreferredSize(new Dimension(75, 0));
		zoomCombo.setMaximumSize(new Dimension(75, 100));
		zoomCombo.setMaximumRowCount(9);
		add(zoomCombo);

		// Sets the zoom in the zoom combo the current value
		mxIEventListener scaleTracker = new mxIEventListener()
		{
			/**
			 * 
			 */
			public void invoke(Object sender, mxEventObject evt)
			{
				ignoreZoomChange = true;

				try
				{
					zoomCombo.setSelectedItem((int) Math.round(100 * view
							.getScale())
							+ "%");
				}
				finally
				{
					ignoreZoomChange = false;
				}
			}
		};

		// Installs the scale tracker to update the value in the combo box
		// if the zoom is changed from outside the combo box
		view.getGraph().getView().addListener(mxEvent.SCALE, scaleTracker);
		view.getGraph().getView().addListener(mxEvent.SCALE_AND_TRANSLATE,
				scaleTracker);

		// Invokes once to sync with the actual zoom value
		scaleTracker.invoke(null, null);

		zoomCombo.addActionListener(new ActionListener()
		{
			/**
			 * 
			 */
			public void actionPerformed(ActionEvent e)
			{
				mxGraphComponent graphComponent = editor.getGraphComponent();

				// Zoomcombo is changed when the scale is changed in the diagram
				// but the change is ignored here
				if (!ignoreZoomChange)
				{
					String zoom = zoomCombo.getSelectedItem().toString();

					try
					{
						zoom = zoom.replace("%", "");
						double scale = Math.min(16, Math.max(0.01,
								Double.parseDouble(zoom) / 100));
						graphComponent.zoomTo(scale, graphComponent
								.isCenterZoom());
					}
					catch (Exception ex)
					{
						JOptionPane.showMessageDialog(editor, ex
								.getMessage());
					}
				}
			}
		});
	}
}
