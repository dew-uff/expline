package br.ufrj.cos.expline.swing.editor;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicButtonUI;

@SuppressWarnings("serial")
public class ConditionPanel extends JPanel{
   protected static final int PREF_W = 600;
   protected static final int PREF_H = 450;
   JPanel panelHolderPanel;

   public ConditionPanel() {
	   
	  super();
	  this.setPreferredSize(new Dimension(PREF_W, PREF_H));
	  panelHolderPanel = new JPanel(new GridLayout(0, 1));
	   
	   
      JPanel borderLayoutPanel = new JPanel(new BorderLayout());
      borderLayoutPanel.add(panelHolderPanel, BorderLayout.NORTH);
      JScrollPane scrollPane = new JScrollPane(borderLayoutPanel);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      
      panelHolderPanel.add(createConditionPanel());

      this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      this.setLayout(new BorderLayout());
      this.add(scrollPane, BorderLayout.CENTER);
      
   }
   
   public JPanel createConditionPanel(){
	   
		JPanel conditionPanel = new JPanel(new BorderLayout());
		
		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 9));
		 
		String[] selectOperator = new String[2];
		selectOperator[0] = "Selected";
		selectOperator[1] = "Not selected";
		JComboBox<String> selectOperatorJComboBox = new JComboBox<String>(selectOperator);
		filterPanel.add(selectOperatorJComboBox);
		
		String[] modifier = new String[9];
		modifier[0] = "Any";
		modifier[1] = "Any optional";
		modifier[2] = "Any variant";
		modifier[3] = "Some";
		modifier[4] = "Some optional";
		modifier[5] = "Some variant";
		modifier[6] = "All";
		modifier[7] = "All Optionals";
		modifier[8] = "All Variants";
		JComboBox<String> modifierJComboBox = new JComboBox<String>(modifier);
		filterPanel.add(modifierJComboBox);
		
		
		final JPopupMenu menu = new JPopupMenu();
		JCheckBox item1 =  new JCheckBox("Other Court");
		JCheckBox item2 =  new JCheckBox("Tribunal Court");
		JCheckBox item3 =  new JCheckBox("High Court");
		JCheckBox item4 =  new JCheckBox("Supreme Court");
		   
		   
		menu.add(item1);
		menu.add(item2);
		menu.add(item3);
		menu.add(item4);
	
	
	    final JButton button = new JButton();
	    button.setAction(new AbstractAction("Choose") {
		    private boolean visible;
	        @Override
	        public void actionPerformed(ActionEvent e) {
	    	
	    	    if(!visible){
	    		    menu.show(button, 0, button.getHeight());
	    		    visible = true;
	    	    }
	    	    else{
	    		    visible = false;
	    		    menu.setVisible(false);
	    	    }
	    		   
	
	        }
	    });
	   
	    filterPanel.add(button);
		
	     
	    JPanel configPanel = new JPanel(new GridBagLayout());
	
	 	GridBagConstraints gbc = new GridBagConstraints();
	 	gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0,0,0,0);
		
		configPanel.add(new AddButton());
		
		gbc.gridy = 1;
		gbc.insets = new Insets(2,0,0,0);
		configPanel.add(new MinusButton(conditionPanel), gbc);
		 
			 
		JPanel flowLayoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		flowLayoutPanel.add(configPanel);
		 
		 
		conditionPanel.add(filterPanel, BorderLayout.CENTER);
		conditionPanel.add(flowLayoutPanel, BorderLayout.EAST);
		 
		//conditionPanel.setBorder(BorderFactory.createTitledBorder("condition"));
		conditionPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			
		return conditionPanel;
   }

   private static void createAndShowGui() {
      ConditionPanel test2 = new ConditionPanel();
      JFrame frame = new JFrame("ConditionPanel");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(test2);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGui();
         }
      });

   }
   
   private class AddButton extends JButton implements ActionListener {
       public AddButton() {
           int size = 17;
           setPreferredSize(new Dimension(size, size));
           setToolTipText("create a new condition");
           //Make the button looks the same for all Laf's
           setUI(new BasicButtonUI());
           //Make it transparent
           setContentAreaFilled(false);
           //No need to be focusable
           setFocusable(false);
           setBorder(BorderFactory.createEtchedBorder());
           setBorderPainted(false);
           //Making nice rollover effect
           //we use the same listener for all buttons
           addMouseListener(null);
           setRolloverEnabled(true);
           //Close the proper tab by clicking the button
           addActionListener(this);
       }

       public void actionPerformed(ActionEvent e) {

			JPanel conditionPanel = createConditionPanel();
					
			panelHolderPanel.add(conditionPanel);
			this.revalidate();
			this.repaint();
         
       }

       //we don't want to update UI for this button
       public void updateUI() {
       }

       //paint the cross
       protected void paintComponent(Graphics g) {
    	   if (getModel().isArmed()) {  
    		      g.setColor(Color.gray);  
    		    } else {  
    		      g.setColor(getBackground());  
    		    }  
    		    Graphics2D g2 = (Graphics2D)g;  
    		    RenderingHints hints = new RenderingHints(null);  
    		    hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
    		    hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);  
    		    hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);  
    		    g2.setRenderingHints(hints);  
    		    g2.fillOval(0, 0, getSize().width-1,getSize().height-1);
    		    g.setColor(Color.gray);
    		    g2.drawOval(0, 0, getSize().width-1,getSize().height-1);
    		    g2.setStroke(new BasicStroke(2));
    		    int delta = 4;
    		    g2.drawLine(delta, getHeight()/2, getWidth() - delta - 1, getHeight()/2);
                g2.drawLine(getWidth()/2, delta, getWidth()/2, getHeight() - delta - 1);
    		    super.paintComponent(g2);  
       }
   }
   
   private class MinusButton extends JButton implements ActionListener {
       JPanel conditionPanel;
	   
	   public MinusButton(JPanel conditionPanel) {
           int size = 17;
           setPreferredSize(new Dimension(size, size));
           setToolTipText("Remove condition");
           //Make the button looks the same for all Laf's
           setUI(new BasicButtonUI());
           //Make it transparent
           setContentAreaFilled(false);
           //No need to be focusable
           setFocusable(false);
           setBorder(BorderFactory.createEtchedBorder());
           setBorderPainted(false);
           //Making nice rollover effect
           //we use the same listener for all buttons
           addMouseListener(null);
           setRolloverEnabled(true);
           //Close the proper tab by clicking the button
           addActionListener(this);
           
           this.conditionPanel = conditionPanel;
       }

       public void actionPerformed(ActionEvent e) {
    	  if(panelHolderPanel.getComponents().length > 1) { 
    		  panelHolderPanel.remove(conditionPanel);
    		  panelHolderPanel.revalidate();
    		  panelHolderPanel.repaint();
    	  }  
       }

       //we don't want to update UI for this button
       public void updateUI() {
       }

       //paint the cross
       protected void paintComponent(Graphics g) {
    	   if (getModel().isArmed()) {  
    		      g.setColor(Color.gray);  
    		    } else {  
    		      g.setColor(getBackground());  
    		    }  
    		    Graphics2D g2 = (Graphics2D)g;  
    		    RenderingHints hints = new RenderingHints(null);  
    		    hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
    		    hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);  
    		    hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);  
    		    g2.setRenderingHints(hints);  
    		    g2.fillOval(0, 0, getSize().width-1,getSize().height-1);
    		    g.setColor(Color.gray);
    		    g2.drawOval(0, 0, getSize().width-1,getSize().height-1);
    		    int delta = 4;
    		    g2.setStroke(new BasicStroke(2));
    		    g2.drawLine(delta, getHeight()/2, getWidth() - delta - 1, getHeight()/2);
    		    super.paintComponent(g2);  
       }
   }
   
}