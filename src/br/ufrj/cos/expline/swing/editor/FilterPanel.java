package br.ufrj.cos.expline.swing.editor;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicButtonUI;

import br.ufrj.cos.expline.model.Activity;
import br.ufrj.cos.expline.model.Expression;

import com.mxgraph.view.mxGraph;

@SuppressWarnings("serial")
public class FilterPanel extends JPanel{
   protected EditRule editRulePanel;
   
   private int type;
   
   public final static int CONDITION_TYPE = 0;
   
   public final static int IMPLICATION_TYPE = 1;
   
   protected JComboBox<String> selectOperatorJComboBox;
   protected JComboBox<String> modifierJComboBox;
   protected JPopupMenu menu;
   
   

   public FilterPanel(EditRule editRulePanel, int type, final Expression exp) {
	   
	  super(new BorderLayout());
	  this.editRulePanel = editRulePanel;
	  
	  this.type = type;
	  
	  JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 9));
		 
		String[] selectOperator = new String[2];
		selectOperator[0] = "Selected";
		selectOperator[1] = "Not selected";
		selectOperatorJComboBox = new JComboBox<String>(selectOperator);
		
		if(exp.getOperation() == Expression.OPERATION_SELECTED){
			selectOperatorJComboBox.setSelectedIndex(0);
		}
		else
			selectOperatorJComboBox.setSelectedIndex(1);
			
		
		filterPanel.add(selectOperatorJComboBox);
		
		String[] modifier = new String[9];
		modifier[0] = "Any";
		modifier[1] = "Any optional";
		modifier[2] = "Any variant";
		modifier[3] = "Some";
		modifier[4] = "Some optional";
		modifier[5] = "Some variant";
		modifier[6] = "All";
		modifier[7] = "All optionals";
		modifier[8] = "All variants";
		modifierJComboBox = new JComboBox<String>(modifier);
		
		menu = new JPopupMenu();
		
		modifierJComboBox.setAction(new AbstractAction("") {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	    	
	        	String selectedItem = (String) modifierJComboBox.getSelectedItem();
	        	
	        	if(selectedItem.contains("variant")){
	        		fillActivityListPopupMenu(exp, Expression.FILTER_VARIANT);
	        	}
	        	else
        		if(selectedItem.contains("optional")){
        			fillActivityListPopupMenu(exp, Expression.FILTER_OPTIONAL);
	        	}
        		else{
        			fillActivityListPopupMenu(exp, Expression.FILTER_NONE);
        		}
	        }
	    });
		
		filterPanel.add(modifierJComboBox);
		
		if(exp.getModifier() == Expression.MODIFIER_ANY){
			if(exp.getFilter() == Expression.FILTER_NONE){
				modifierJComboBox.setSelectedIndex(0);
			}
			else
			if(exp.getFilter() == Expression.FILTER_OPTIONAL){
				modifierJComboBox.setSelectedIndex(1);
			}
			else
			if(exp.getFilter() == Expression.FILTER_VARIANT){
				modifierJComboBox.setSelectedIndex(2);
			}
		}
		else if(exp.getModifier() == Expression.MODIFIER_SOME){
			if(exp.getFilter() == Expression.FILTER_NONE){
				modifierJComboBox.setSelectedIndex(3);
			}
			else
			if(exp.getFilter() == Expression.FILTER_OPTIONAL){
				modifierJComboBox.setSelectedIndex(4);
			}
			else
			if(exp.getFilter() == Expression.FILTER_VARIANT){
				modifierJComboBox.setSelectedIndex(5);
			}
		}
		else if(exp.getModifier() == Expression.MODIFIER_ALL){
			if(exp.getFilter() == Expression.FILTER_NONE){
				modifierJComboBox.setSelectedIndex(6);
			}
			else
			if(exp.getFilter() == Expression.FILTER_OPTIONAL){
				modifierJComboBox.setSelectedIndex(7);
			}
			else
			if(exp.getFilter() == Expression.FILTER_VARIANT){
				modifierJComboBox.setSelectedIndex(8);
			}
		} 
		

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
		configPanel.add(new MinusButton(this), gbc);
		 
			 
		JPanel flowLayoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		flowLayoutPanel.add(configPanel);
		 
		 
		this.add(filterPanel, BorderLayout.CENTER);
		this.add(flowLayoutPanel, BorderLayout.EAST);
		 
		//conditionPanel.setBorder(BorderFactory.createTitledBorder("condition"));
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	  
		
		if(type == CONDITION_TYPE){
			editRulePanel.conditionPanelHolder.add(this);
			editRulePanel.conditionExpressions.add(this);
		}
		else{
			editRulePanel.implicationPanelHolder.add(this);
			editRulePanel.implicationExpressions.add(this);
		}
      
   }
   
   private void fillActivityListPopupMenu(Expression exp, int filter){

	    mxGraph graph = editRulePanel.expLineGraph;
		Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
		
		menu.removeAll();
	   
	   if(filter == Expression.FILTER_NONE){
		   for (Object vertex : vertices) {
			   Activity activity = (Activity) vertex;
			   
			   if(activity.getType().contains("Variant") || activity.getType().contains("Optional")){
				   JCheckBox item =  new JCheckBox((String)activity.getValue());
				   item.setActionCommand(activity.getId());
				   item.setSelected(isSelected(activity, exp.getActivities()));
				   menu.add(item);
			   }
			   
		   }
	   }
	   else
	   if(filter == Expression.FILTER_VARIANT){
		   for (Object vertex : vertices) {
			   Activity activity = (Activity) vertex;
			   
			   if(activity.getType().contains("Variant")){
				   JCheckBox item =  new JCheckBox((String)activity.getValue());
				   item.setActionCommand(activity.getId());
				   item.setSelected(isSelected(activity, exp.getActivities()));
				   menu.add(item);
			   }
		   }
	   }
	   else
	   if(filter == Expression.FILTER_OPTIONAL){
		   for (Object vertex : vertices) {
			   Activity activity = (Activity) vertex;
			   
			   if(activity.getType().contains("Optional")){
				   JCheckBox item =  new JCheckBox((String)activity.getValue());
				   item.setActionCommand(activity.getId());
				   item.setSelected(isSelected(activity, exp.getActivities()));
				   menu.add(item);
			   }
		   }
	   }
   }
   
   private boolean isSelected(Activity activity, List<Activity> activities) {
	
	   for (Activity actv : activities)
		   if(actv.getId().equals(activity.getId()))
			   return true;
	   
	   return false;
   }

private void loadData() {
	   
	   //carrega das expressions que estao em rule

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

			new FilterPanel(editRulePanel, type, new Expression());
					
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
       FilterPanel filterPanel;
	   
	   public MinusButton(FilterPanel filterPanel) {
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
           
           this.filterPanel = filterPanel;
       }

       public void actionPerformed(ActionEvent e) {
    	  
    	  if(type == CONDITION_TYPE){
    		  
        	  if(editRulePanel.conditionPanelHolder.getComponents().length > 1) { 
        		  editRulePanel.conditionPanelHolder.remove(filterPanel);
        		  editRulePanel.conditionExpressions.remove(filterPanel);
        		  editRulePanel.conditionPanelHolder.revalidate();
        		  editRulePanel.conditionPanelHolder.repaint();
        	  }  
    	  }
    	  else{
    		  if(editRulePanel.implicationPanelHolder.getComponents().length > 1) { 
    			  editRulePanel.implicationPanelHolder.remove(filterPanel);
        		  editRulePanel.implicationExpressions.remove(filterPanel);
        		  editRulePanel.implicationPanelHolder.revalidate();
        		  editRulePanel.implicationPanelHolder.repaint();
    		  }
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