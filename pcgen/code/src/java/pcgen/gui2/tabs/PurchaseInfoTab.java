/*
 * PurchaseInfoTab.java
 * Copyright 2011 Connor Petty <cpmeister@users.sourceforge.net>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Created on Jan 14, 2011, 7:35:28 PM
 */
package pcgen.gui2.tabs;

import static pcgen.gui2.tabs.equip.EquipmentSelection.equipmentArrayFlavor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import pcgen.core.facade.CharacterFacade;
import pcgen.core.facade.EquipmentFacade;
import pcgen.core.facade.EquipmentListFacade;
import pcgen.core.facade.EquipmentListFacade.EquipmentListEvent;
import pcgen.core.facade.EquipmentListFacade.EquipmentListListener;
import pcgen.core.facade.util.DefaultListFacade;
import pcgen.core.facade.util.ListFacade;
import pcgen.gui2.UIPropertyContext;
import pcgen.gui2.filter.Filter;
import pcgen.gui2.filter.FilterBar;
import pcgen.gui2.filter.FilterButton;
import pcgen.gui2.filter.FilterButtonGroupPanel;
import pcgen.gui2.filter.FilteredTreeViewTable;
import pcgen.gui2.filter.SearchFilterPanel;
import pcgen.gui2.tabs.equip.EquipmentSelection;
import pcgen.gui2.tools.FlippingSplitPane;
import pcgen.gui2.tools.Icons;
import pcgen.gui2.tools.InfoPane;
import pcgen.gui2.util.SortMode;
import pcgen.gui2.util.SortingPriority;
import pcgen.gui2.util.treeview.DataView;
import pcgen.gui2.util.treeview.DataViewColumn;
import pcgen.gui2.util.treeview.DefaultDataViewColumn;
import pcgen.gui2.util.treeview.TreeView;
import pcgen.gui2.util.treeview.TreeViewModel;
import pcgen.gui2.util.treeview.TreeViewPath;
import pcgen.system.LanguageBundle;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public class PurchaseInfoTab extends FlippingSplitPane implements CharacterInfoTab
{

	private final FilteredTreeViewTable availableTable;
	private final FilteredTreeViewTable purchasedTable;
	private final JCheckBox autoResizeBox;
	private final JButton addCustomButton;
	private final JButton addEquipmentButton;
	private final JButton removeEquipmentButton;
	private final InfoPane infoPane;
	private EquipmentFacade selectedItem;

	public PurchaseInfoTab()
	{
		this.availableTable = new FilteredTreeViewTable();
		this.purchasedTable = new FilteredTreeViewTable();
		this.autoResizeBox = new JCheckBox();
		this.addCustomButton = new JButton();
		this.addEquipmentButton = new JButton();
		this.removeEquipmentButton = new JButton();
		this.infoPane = new InfoPane();
		initComponents();
	}

	private void initComponents()
	{
		setOrientation(VERTICAL_SPLIT);
		FlippingSplitPane splitPane = new FlippingSplitPane();
		splitPane.setOrientation(HORIZONTAL_SPLIT);
		{// Top Left panel
			FilterBar filterBar = new FilterBar();
			{// Filters
				filterBar.addDisplayableFilter(new SearchFilterPanel());
				FilterButton premadeFilter = new FilterButton();
				premadeFilter.setText("Premade Only");
				premadeFilter.setFilter(new Filter<CharacterFacade, EquipmentFacade>()
				{

					public boolean accept(CharacterFacade context, EquipmentFacade element)
					{
						//TODO: make this exclude custom items
						return context.getInfoFactory().getCost(element) != 0f
								|| context.getInfoFactory().getWeight(element) != 0f;
					}

				});
				FilterButton customFilter = new FilterButton();
				customFilter.setText("Custom Only");
				customFilter.setFilter(new Filter<CharacterFacade, EquipmentFacade>()
				{

					public boolean accept(CharacterFacade context, EquipmentFacade element)
					{
						// TODO: make this include custom items
						return context.getInfoFactory().getCost(element) == 0f
								&& context.getInfoFactory().getWeight(element) == 0f;
					}

				});
				FilterButtonGroupPanel groupPanel = new FilterButtonGroupPanel();
				groupPanel.addFilterButton(premadeFilter);
				groupPanel.addFilterButton(customFilter);
				filterBar.addDisplayableFilter(groupPanel);
			}
			JPanel panel = new JPanel(new BorderLayout());
			panel.add(filterBar, BorderLayout.NORTH);

			availableTable.getTree().setLargeModel(true);
			availableTable.setDisplayableFilter(filterBar);
			availableTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
			{

				public void valueChanged(ListSelectionEvent e)
				{
					if (!e.getValueIsAdjusting())
					{
						List<Object> data = availableTable.getSelectedData();
						EquipmentFacade equipment = null;
						if (!data.isEmpty() && data.get(0) instanceof EquipmentFacade)
						{
							equipment = (EquipmentFacade) data.get(0);
						}
						setSelectedItem(equipment);
					}
				}

			});
			availableTable.setSortingPriority(Collections.singletonList(new SortingPriority(0, SortMode.ASCENDING)));
			availableTable.sortModel();
			panel.add(new JScrollPane(availableTable), BorderLayout.CENTER);

			Box box = Box.createHorizontalBox();
			box.add(Box.createHorizontalStrut(5));
			box.add(autoResizeBox);
			box.add(Box.createHorizontalGlue());
			addCustomButton.setHorizontalTextPosition(SwingConstants.LEADING);
			box.add(addCustomButton);
			box.add(Box.createHorizontalStrut(5));
			addEquipmentButton.setHorizontalTextPosition(SwingConstants.LEADING);
			box.add(addEquipmentButton);
			box.add(Box.createHorizontalStrut(5));
			box.setBorder(new EmptyBorder(0,  0, 5, 0));
			panel.add(box, BorderLayout.SOUTH);
			splitPane.setLeftComponent(panel);
		}
		{// Top Right panel
			FilterBar filterBar = new FilterBar();
			filterBar.addDisplayableFilter(new SearchFilterPanel());

			JPanel panel = new JPanel(new BorderLayout());
			panel.add(filterBar, BorderLayout.NORTH);

			purchasedTable.setDisplayableFilter(filterBar);
			purchasedTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
			{

				public void valueChanged(ListSelectionEvent e)
				{
					if (!e.getValueIsAdjusting())
					{
						List<Object> data = purchasedTable.getSelectedData();
						EquipmentFacade skill = null;
						if (!data.isEmpty() && data.get(0) instanceof EquipmentFacade)
						{
							skill = (EquipmentFacade) data.get(0);
						}
						setSelectedItem(skill);
					}
				}

			});
			purchasedTable.setSortingPriority(Collections.singletonList(new SortingPriority(0, SortMode.ASCENDING)));
			purchasedTable.sortModel();
			panel.add(new JScrollPane(purchasedTable), BorderLayout.CENTER);

			Box box = Box.createHorizontalBox();
			box.add(Box.createHorizontalStrut(5));
			box.add(removeEquipmentButton);
			box.add(Box.createHorizontalGlue());
			box.setBorder(new EmptyBorder(0,  0, 5, 0));
			panel.add(box, BorderLayout.SOUTH);
			splitPane.setRightComponent(panel);
		}
		setTopComponent(splitPane);
		splitPane = new FlippingSplitPane();
		splitPane.setOrientation(HORIZONTAL_SPLIT);
		{// Bottom Left Panel
			JPanel panel = new JPanel();
			splitPane.setLeftComponent(panel);
		}
		{// Bottom Right Panel
			infoPane.setTitle("Equipment Info");
			splitPane.setRightComponent(infoPane);
		}
		setBottomComponent(splitPane);
	}

	private void setSelectedItem(EquipmentFacade item)
	{
		this.selectedItem = item;
	}

	public Hashtable<Object, Object> createModels(CharacterFacade character)
	{
		Hashtable<Object, Object> state = new Hashtable<Object, Object>();
		state.put(AvailableTreeViewModel.class, new AvailableTreeViewModel(character));
		state.put(PurchasedTreeViewModel.class, new PurchasedTreeViewModel(character));
		state.put(UseAutoResizeAction.class, new UseAutoResizeAction(character));
		state.put(AddCustomAction.class, new AddCustomAction(character));
		state.put(AddAction.class, new AddAction(character));
		state.put(RemoveAction.class, new RemoveAction(character));
		state.put(EquipInfoHandler.class, new EquipInfoHandler(character));
		state.put(EquipmentRenderer.class, new EquipmentRenderer(character));
		state.put(EquipmentFilterHandler.class, new EquipmentFilterHandler(character));
		state.put(EquipmentTransferHandler.class, new EquipmentTransferHandler(character));
		return state;
	}

	public void restoreModels(Hashtable<?, ?> state)
	{
		availableTable.setTreeViewModel((TreeViewModel) state.get(AvailableTreeViewModel.class));
		availableTable.setTreeCellRenderer((EquipmentRenderer) state.get(EquipmentRenderer.class));
		purchasedTable.setTreeViewModel((TreeViewModel) state.get(PurchasedTreeViewModel.class));
		purchasedTable.setTreeCellRenderer((EquipmentRenderer) state.get(EquipmentRenderer.class));
		autoResizeBox.setAction((UseAutoResizeAction) state.get(UseAutoResizeAction.class));
		addCustomButton.setAction((AddCustomAction) state.get(AddCustomAction.class));
		addEquipmentButton.setAction((AddAction) state.get(AddAction.class));
		removeEquipmentButton.setAction((RemoveAction) state.get(RemoveAction.class));
		((EquipInfoHandler) state.get(EquipInfoHandler.class)).install();
		((EquipmentFilterHandler) state.get(EquipmentFilterHandler.class)).install();
		((EquipmentTransferHandler) state.get(EquipmentTransferHandler.class)).install();
		((UseAutoResizeAction) state.get(UseAutoResizeAction.class)).install();
		((AddAction) state.get(AddAction.class)).install();
		((RemoveAction) state.get(RemoveAction.class)).install();
	}

	public void storeModels(Hashtable<Object, Object> state)
	{
		((EquipInfoHandler) state.get(EquipInfoHandler.class)).uninstall();
		((AddAction) state.get(AddAction.class)).uninstall();
		((RemoveAction) state.get(RemoveAction.class)).uninstall();
	}

	public TabTitle getTabTitle()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private class AddAction extends AbstractAction
	{

		private CharacterFacade character;

		public AddAction(CharacterFacade character)
		{
			super("Add Equipment");
			putValue(SMALL_ICON, Icons.Forward16.getImageIcon());
			this.character = character;
		}

		public void actionPerformed(ActionEvent e)
		{
			List<Object> data = availableTable.getSelectedData();
			if (data != null)
			{
				for (Object object : data)
				{
					if (object instanceof EquipmentFacade)
					{
						EquipmentFacade equip = (EquipmentFacade) object;
						if (character.isAutoResize())
						{
							equip = character.getEquipmentSizedForCharacter(equip);
						}
						character.addPurchasedEquipment(equip, 1, false);
					}
				}
			}
		}
		
		public void install()
		{
			availableTable.addActionListener(this);
		}
		
		public void uninstall()
		{
			availableTable.removeActionListener(this);
		}

	}

	private class AddCustomAction extends AbstractAction
	{

		private CharacterFacade character;

		public AddCustomAction(CharacterFacade character)
		{
			super("Add Custom");
			putValue(SMALL_ICON, Icons.Forward16.getImageIcon());
			this.character = character;
		}

		public void actionPerformed(ActionEvent e)
		{
			List<Object> data = availableTable.getSelectedData();
			if (data != null)
			{
				for (Object object : data)
				{
					if (object instanceof EquipmentFacade)
					{
						EquipmentFacade equip = (EquipmentFacade) object;
						if (character.isAutoResize())
						{
							equip = character.getEquipmentSizedForCharacter(equip);
						}
						character.addPurchasedEquipment(equip, 1, true);
					}
				}
			}
		}

	}

	private class UseAutoResizeAction extends AbstractAction
	{

		private CharacterFacade character;

		public UseAutoResizeAction(CharacterFacade character)
		{
			super(LanguageBundle.getString("in_igAutoResize"));
			this.character = character;
		}

		public void actionPerformed(ActionEvent e)
		{
			character.setAutoResize(autoResizeBox.isSelected());
		}

		public void install()
		{
			autoResizeBox.setSelected(character.isAutoResize());
		}

	}

	private class RemoveAction extends AbstractAction
	{

		private CharacterFacade character;

		public RemoveAction(CharacterFacade character)
		{
			super("Remove Equipment");
			putValue(SMALL_ICON, Icons.Back16.getImageIcon());
			this.character = character;
		}

		public void actionPerformed(ActionEvent e)
		{
			List<Object> data = purchasedTable.getSelectedData();
			if (data != null)
			{
				for (Object object : data)
				{
					character.removePurchasedEquipment((EquipmentFacade) object, 1);
				}

			}
		}
		
		public void install()
		{
			purchasedTable.addActionListener(this);
		}
		
		public void uninstall()
		{
			purchasedTable.removeActionListener(this);
		}

	}

	private class EquipInfoHandler implements ListSelectionListener
	{

		private CharacterFacade character;

		public EquipInfoHandler(CharacterFacade character)
		{
			this.character = character;
		}

		public void install()
		{
			availableTable.getSelectionModel().addListSelectionListener(this);
		}

		public void uninstall()
		{
			availableTable.getSelectionModel().removeListSelectionListener(this);
		}

		public void valueChanged(ListSelectionEvent e)
		{
			if (!e.getValueIsAdjusting())
			{
				int selectedRows[] = availableTable.getSelectedRows();
				StringBuilder sb = new StringBuilder(2000);
				for (int row : selectedRows)
				{
					EquipmentFacade equip = null;
					if (row != -1)
					{
						Object value = availableTable.getModel().getValueAt(row, 0);
						if (value instanceof EquipmentFacade)
						{
							equip = (EquipmentFacade) value;
						}
					}
					if (equip != null)
					{
						sb.append(character.getInfoFactory().getHTMLInfo(equip));
					}
				}
				infoPane.setText("<html>" + sb.toString() + "</html>");
			}
		}

	}

	private static class AvailableTreeViewModel
			implements TreeViewModel<EquipmentFacade>, DataView<EquipmentFacade>
	{

		private static final ListFacade<? extends TreeView<EquipmentFacade>> treeviews =
				new DefaultListFacade<TreeView<EquipmentFacade>>(Arrays.asList(EquipmentTreeView.values()));
		private static final List<DefaultDataViewColumn> columns =
				Arrays.asList(new DefaultDataViewColumn("Cost", Float.class, true),
							  new DefaultDataViewColumn("Weight", Float.class, true));
		private final CharacterFacade character;
		private final ListFacade<EquipmentFacade> equipmentList;

		public AvailableTreeViewModel(CharacterFacade character)
		{
			this.character = character;
			this.equipmentList = character.getDataSet().getEquipment();
		}

		public ListFacade<? extends TreeView<EquipmentFacade>> getTreeViews()
		{
			return treeviews;
		}

		public int getDefaultTreeViewIndex()
		{
			return 1;
		}

		public DataView<EquipmentFacade> getDataView()
		{
			return this;
		}

		public ListFacade<EquipmentFacade> getDataModel()
		{
			return equipmentList;
		}

		public List<?> getData(EquipmentFacade obj)
		{
			return Arrays.asList(character.getInfoFactory().getCost(obj), character.getInfoFactory().getWeight(obj));
		}

		public List<? extends DataViewColumn> getDataColumns()
		{
			return columns;
		}

	}

	private class PurchasedTreeViewModel
			implements TreeViewModel<EquipmentFacade>, DataView<EquipmentFacade>, EquipmentListListener
	{

		private final ListFacade<? extends TreeView<EquipmentFacade>> treeviews =
				new DefaultListFacade<TreeView<EquipmentFacade>>(Arrays.asList(EquipmentTreeView.values()));
		private final List<DefaultDataViewColumn> columns =
				Arrays.asList(new DefaultDataViewColumn("Cost", Float.class, true),
							  new DefaultDataViewColumn("Weight", Float.class, false),
							  new DefaultDataViewColumn("Qty", Integer.class, true));
		private final CharacterFacade character;
		private final EquipmentListFacade equipmentList;

		public PurchasedTreeViewModel(CharacterFacade character)
		{
			this.character = character;
			this.equipmentList = character.getPurchasedEquipment();
			this.equipmentList.addEquipmentListListener(this);
		}

		public ListFacade<? extends TreeView<EquipmentFacade>> getTreeViews()
		{
			return treeviews;
		}

		public int getDefaultTreeViewIndex()
		{
			return 0;
		}

		public DataView<EquipmentFacade> getDataView()
		{
			return this;
		}

		public ListFacade<EquipmentFacade> getDataModel()
		{
			return equipmentList;
		}

		public List<?> getData(EquipmentFacade obj)
		{
			return Arrays.asList(character.getInfoFactory().getCost(obj), character.getInfoFactory().getWeight(obj), equipmentList.getQuantity(obj));
		}

		public List<? extends DataViewColumn> getDataColumns()
		{
			return columns;
		}

		public void quantityChanged(EquipmentListEvent equipment)
		{
			purchasedTable.refreshModelData();
		}

	}

	private enum EquipmentTreeView implements TreeView<EquipmentFacade>
	{

		NAME("Name"),
		TYPE_NAME("Type/Name");
		//SOURCE_NAME("Source/Name");
		private String name;

		private EquipmentTreeView(String name)
		{
			this.name = name;
		}

		public String getViewName()
		{
			return name;
		}

		public List<TreeViewPath<EquipmentFacade>> getPaths(EquipmentFacade pobj)
		{
			switch (this)
			{
				case TYPE_NAME:
					String[] types = pobj.getTypes();
					if (types != null && types.length > 0)
					{

						List<TreeViewPath<EquipmentFacade>> paths = new ArrayList<TreeViewPath<EquipmentFacade>>(
								types.length);
						String type = types[0];
						paths.add(new TreeViewPath<EquipmentFacade>(pobj, type));
						return paths;
					}
				case NAME:
					return Collections.singletonList(new TreeViewPath<EquipmentFacade>(pobj));
//				case SOURCE_NAME:
//					return Collections.singletonList(
//							new TreeViewPath<ClassFacade>(pobj,
//														  pobj.getSource()));
				default:
					throw new InternalError();
			}


		}

	}

	/**
	 * The Class <code>EquipmentRenderer</code> displays the tree cells of the
	 * available and purchased equipment tables.  
	 */
	private class EquipmentRenderer extends DefaultTreeCellRenderer
	{

		private CharacterFacade character;

		public EquipmentRenderer(CharacterFacade character)
		{
			this.character = character;
			setTextNonSelectionColor(UIPropertyContext.getQualifiedColor());
			setClosedIcon(null);
			setLeafIcon(null);
			setOpenIcon(null);
		}

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
													  boolean sel, boolean expanded, boolean leaf, int row,
													  boolean hasFocus)
		{

			super.getTreeCellRendererComponent(tree, value, sel, expanded,
											   leaf, row, hasFocus);
			Object equipObj = ((DefaultMutableTreeNode) value).getUserObject();
			if (equipObj instanceof EquipmentFacade
					&& !character.isQualifiedFor((EquipmentFacade) equipObj))
			{
				setForeground(UIPropertyContext.getNotQualifiedColor());
			}
			return this;
		}

	}

	private class EquipmentFilterHandler
	{

		private final CharacterFacade character;

		public EquipmentFilterHandler(CharacterFacade character)
		{
			this.character = character;
		}

		public void install()
		{
			availableTable.setContext(character);
		}

	}

	private class PurchaseTransferHandler extends TransferHandler
	{

		private CharacterFacade character;

		public PurchaseTransferHandler(CharacterFacade character)
		{
			this.character = character;
		}

		@Override
		public int getSourceActions(JComponent c)
		{
			return MOVE;
		}

		@Override
		protected Transferable createTransferable(JComponent c)
		{
			List<Object> data = purchasedTable.getSelectedData();

			if (data == null)
			{
				return null;
			}
			Iterator<Object> it = data.iterator();
			while (it.hasNext())
			{
				if (!(it.next() instanceof EquipmentFacade))
				{
					it.remove();
				}
			}
			if (data.isEmpty())
			{
				return null;
			}

			EquipmentFacade[] equipArray = data.toArray(new EquipmentFacade[0]);
			return new EquipmentSelection(equipArray);
		}

		@Override
		public boolean canImport(TransferSupport support)
		{
			if (!support.isDataFlavorSupported(equipmentArrayFlavor))
			{
				return false;
			}
			support.setShowDropLocation(false);
			return true;
		}

	}

	private class EquipmentTransferHandler extends TransferHandler
	{

		private CharacterFacade character;

		public EquipmentTransferHandler(CharacterFacade character)
		{
			this.character = character;
		}

		public void install()
		{
			availableTable.setDragEnabled(true);
			availableTable.setDropMode(DropMode.ON);
			availableTable.setTransferHandler(this);

			purchasedTable.setDragEnabled(true);
			purchasedTable.setDropMode(DropMode.ON);
			purchasedTable.setTransferHandler(this);
		}

		@Override
		public int getSourceActions(JComponent c)
		{
			return MOVE;
		}

		@Override
		protected Transferable createTransferable(JComponent c)
		{
			List<Object> data = null;
			if (c == availableTable)
			{
				data = availableTable.getSelectedData();
			}
			else if (c == purchasedTable)
			{
				data = purchasedTable.getSelectedData();
			}
			if (data == null)
			{
				return null;
			}
			Iterator<Object> it = data.iterator();
			while (it.hasNext())
			{
				if (!(it.next() instanceof EquipmentFacade))
				{
					it.remove();
				}
			}
			if (data.isEmpty())
			{
				return null;
			}

			EquipmentFacade[] equipArray = data.toArray(new EquipmentFacade[0]);
			return new EquipmentSelection(equipArray);
		}

		@Override
		public boolean canImport(TransferSupport support)
		{
			if (!support.isDataFlavorSupported(equipmentArrayFlavor))
			{
				return false;
			}
			support.setShowDropLocation(false);
			return true;
		}

		private EquipmentFacade[] getEquipmentArray(TransferSupport support)
		{
			EquipmentFacade[] equipmentArray = null;
			try
			{
				equipmentArray = (EquipmentFacade[]) support.getTransferable().getTransferData(equipmentArrayFlavor);
			}
			catch (UnsupportedFlavorException ex)
			{
			}
			catch (IOException ex)
			{
			}
			return equipmentArray;
		}

		@Override
		public boolean importData(TransferSupport support)
		{
			if (!canImport(support))
			{
				return false;
			}
			if (!support.isDrop())
			{
				return false;
			}
			EquipmentFacade[] equipmentArray = getEquipmentArray(support);
			if (support.getComponent() == availableTable)
			{
				for (EquipmentFacade equipmentFacade : equipmentArray)
				{
					character.removePurchasedEquipment(equipmentFacade, 1);
				}
			}
			else if (support.getComponent() == purchasedTable)
			{
				for (EquipmentFacade equipmentFacade : equipmentArray)
				{
					EquipmentFacade equip = character.getEquipmentSizedForCharacter(equipmentFacade);
					character.addPurchasedEquipment(equip, 1, false);
				}
			}
			return true;
		}

	}

}
