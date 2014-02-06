package br.ufrj.cos.expline.swing.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.AbstractTableModel;

import br.ufrj.cos.expline.swing.editor.EditorActions.AlgebraicOperatorOptionItem;

import com.mxgraph.util.mxResources;

public class EditorPropertiesFrame extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378029138434324390L;

	/**
	 * 
	 */
	public EditorPropertiesFrame(Frame owner)
	{
		super(owner);
		setTitle(mxResources.get("aboutGraphEditor"));
		setLayout(new BorderLayout());

		// Creates the gradient panel
		JPanel panel = new JPanel(new BorderLayout())
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = -5062895855016210947L;

			/**
			 * 
			 */
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);

				// Paint gradient background
				Graphics2D g2d = (Graphics2D) g;
				g2d.setPaint(new GradientPaint(0, 0, Color.WHITE, getWidth(),
						0, getBackground()));
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}

		};

		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createMatteBorder(0, 0, 1, 0, Color.GRAY), BorderFactory
				.createEmptyBorder(8, 8, 12, 8)));

		// Adds optional subtitle
		JLabel titleLabel = new JLabel(mxResources.get("properties"));
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 18, 0, 0));
		titleLabel.setOpaque(false);
		panel.add(titleLabel, BorderLayout.CENTER);

		getContentPane().add(panel, BorderLayout.NORTH);
		
		JPanel content = new JPanel();
		content.setLayout(new FlowLayout(FlowLayout.LEFT));
		content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		content.add(new JLabel(mxResources.get("albegraicOperator")+":"));
		String[] algebraicOperators = {"Map", "Split Map", "Reduce", "Filter", "Join"};
		content.add(new JComboBox<String>(algebraicOperators));

		//getContentPane().add(content, BorderLayout.NORTH);
        
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Input", null, createTablePanel(),
                          "Does nothing");
        tabbedPane.addTab("Outuput", null, createTablePanel(),
                "Does nothing");
        //getContentPane().add(tabbedPane, BorderLayout.CENTER);
        
        JPanel middle = new JPanel(new BorderLayout());
        middle.add(tabbedPane, BorderLayout.CENTER);
        middle.add(content, BorderLayout.NORTH);
        getContentPane().add(middle, BorderLayout.CENTER);


		//getContentPane().add(content, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createMatteBorder(1, 0, 0, 0, Color.GRAY), BorderFactory
				.createEmptyBorder(16, 8, 8, 8)));
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		// Adds OK button to close window
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});

		buttonPanel.add(closeButton);

		closeButton = new JButton("Close");
		buttonPanel.add(closeButton);
		
		// Sets default button for enter key
		getRootPane().setDefaultButton(closeButton);

		setResizable(false);
		setSize(400, 400);
	}
	
	public JPanel createTablePanel(){
		
		JPanel tablePanel = new JPanel(new BorderLayout());
		
		
		List<RelationalSchemaAttribute> clicks = new ArrayList<>(25);
        clicks.add(new RelationalSchemaAttribute(620, 1028));
        clicks.add(new RelationalSchemaAttribute(480, 230));
        
        RelationalSchemaTableModel model = new RelationalSchemaTableModel(clicks);
        JTable table = new JTable(model);
        
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        
        JPanel commandPanel = new JPanel();
        commandPanel.setLayout(new BoxLayout(commandPanel, BoxLayout.Y_AXIS));

		// Adds OK button to close window
		JButton closeButton = new JButton("Close");
		
		commandPanel.add(closeButton);
		
		

		// Adds OK button to close window
	    closeButton = new JButton("Open");
		
		commandPanel.add(closeButton);
		
		
		tablePanel.add(commandPanel, BorderLayout.EAST);
        
		return tablePanel;
	}

	/**
	 * Overrides {@link JDialog#createRootPane()} to return a root pane that
	 * hides the window when the user presses the ESCAPE key.O
	 */
	protected JRootPane createRootPane()
	{
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		JRootPane rootPane = new JRootPane();
		rootPane.registerKeyboardAction(new ActionListener()
		{
			public void actionPerformed(ActionEvent actionEvent)
			{
				setVisible(false);
			}
		}, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}
	
	
	
	public class RelationalSchemaAttribute {

        private int name;
        private int type;

        public RelationalSchemaAttribute(int name, int type) {
            this.name = name;
            this.type = type;
        }

        public int getName() {
            return name;
        }

        public int getType() {
            return type;
        }

    }

    public class RelationalSchemaTableModel extends AbstractTableModel {

        private List<RelationalSchemaAttribute> clicks;

        public RelationalSchemaTableModel(List<RelationalSchemaAttribute> clicks) {
            this.clicks = new ArrayList<>(clicks);
        }

        
        @Override
        public int getRowCount() {
            return clicks.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int column) {
            String name = "??";
            switch (column) {
                case 0:
                    name = "Attribute Type";
                    break;
                case 1:
                    name = "Attribute Name";
                    break;
            }
            return name;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            Class type = String.class;
            switch (columnIndex) {
                case 0:
                case 1:
                    type = Integer.class;
                    break;
            }
            return type;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            RelationalSchemaAttribute click = clicks.get(rowIndex);
            Object value = null;
            switch (columnIndex) {
                case 0:
                    value = click.getName();
                    break;
                case 1:
                    value = click.getType();
                    break;
            }
            return value;
        }            
    }

}
