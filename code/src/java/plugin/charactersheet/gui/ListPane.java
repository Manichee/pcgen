/*
 * featPane.java
 *
 * Created on February 12, 2004, 4:32 PM
 */

package plugin.charactersheet.gui;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author  ddjone3
 */
public class ListPane extends javax.swing.JPanel
{
	private List list = new ArrayList();

	/** Creates new form featPane */
	public ListPane()
	{
		initComponents();
		setLocalColor();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents()
	{//GEN-BEGIN:initComponents
		jPanel1 = new javax.swing.JPanel();
		title = new javax.swing.JLabel();
		listDisplay = new javax.swing.JTextArea();

		setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

		jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		title.setFont(new java.awt.Font("Dialog", 1, 14));
		title.setText(" ");
		jPanel1.add(title);

		add(jPanel1);

		listDisplay.setLineWrap(true);
		listDisplay.setWrapStyleWord(true);
		add(listDisplay);

	}//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel jPanel1;
	private javax.swing.JTextArea listDisplay;
	private javax.swing.JLabel title;

	// End of variables declaration//GEN-END:variables

	/** Set the color */
	public void setColor()
	{
		setLocalColor();
		refresh();
	}

	/** Set the local color for the pane */
	public void setLocalColor()
	{
		setBackground(CharacterPanel.white);
		jPanel1.setBackground(CharacterPanel.header);
		jPanel1.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border));
		listDisplay.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border));
	}

	/** 
	 * Set the list for this pane
	 * @param title
	 * @param list
	 */
	public void setList(String title, List list)
	{
		this.list = list;
		this.title.setText(title);
		setVisible(false);
		refresh();
	}

	/** Refresh this pane */
	public void refresh()
	{
		if (list.size() > 0)
		{
			setVisible(true);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < list.size(); i++)
			{
				if (i > 0)
				{
					sb.append(", ");
				}
				sb.append(list.get(i).toString());
			}
			listDisplay.setText(sb.toString());
		}
		else
		{
			setVisible(false);
		}
	}

	/** Destroy */
	public void destruct()
	{
		//Put any code here that is needed to prevent memory leaks when this panel is destroyed
	}
}