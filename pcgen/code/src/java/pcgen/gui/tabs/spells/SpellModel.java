/*
 * SpellModel.java
 * Copyright 2004 (C) Chris Ward <frugal@purplewombat.co.uk>
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
 * Created on 10-Dec-2004
 */
package pcgen.gui.tabs.spells;

import pcgen.core.*;
import pcgen.core.character.CharacterSpell;
import pcgen.core.character.SpellInfo;
import pcgen.core.spell.Spell;
import pcgen.gui.GuiConstants;
import pcgen.gui.tabs.InfoSpells;
import pcgen.gui.utils.AbstractTreeTableModel;
import pcgen.gui.utils.PObjectNode;
import pcgen.gui.utils.TreeTableModel;
import pcgen.util.Logging;

import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 *  The TreeTableModel has a single <code>root</code> node
 *  This root node has a null <code>parent</code>.
 *  All other nodes have a parent which points to a non-null node.
 *  Parent nodes contain a list of  <code>children</code>, which
 *  are all the nodes that point to it as their parent.
 *  <code>nodes</code> which have 0 children are leafs (the end of
 *  that linked list).  nodes which have at least 1 child are not leafs
 *  Leafs are like files and non-leafs are like directories.
 *  The leafs contain an Object that we want to know about (Spells)
 **/
public final class SpellModel extends AbstractTreeTableModel
{
	//column positions for tables
	// if you change these, you also need to change
	// the selNameList array in the SpellModel class
	private static final int COL_NAME = 0;
	private static final int COL_SCHOOL = 1;
	private static final int COL_SUBSCHOOL = 2;
	private static final int COL_DESCRIPTOR = 3;
	private static final int COL_COMPONENT = 4;
	private static final int COL_CASTTIME = 5;
	private static final int COL_RANGE = 6;
	private static final int COL_DESCRIPTION = 7;
	private static final int COL_TARGET = 8;
	private static final int COL_DURATION = 9;
	private static final int COL_SAVE = 10;
	private static final int COL_SR = 11;
	private static final int COL_SRC = 12;
	private static final int COL_PPCOST = 13;

    // there are two roots. One for available spells
	// and one for selected spells (spellbooks)
	private PObjectNode theRoot;

	// list of columns names
//	private String[] availNameList = { "" };
//	private String[] selNameList = { "" };
	private String[] colNameList = { "" };

	private int[] colTranslateList = { 0 };

	// Types of the columns.
	private boolean includeRace = false;
    private PlayerCharacter pc;

	/**
	 * Creates a SpellModel
	 * @param spells
	 * @param primaryMode
	 * @param secondaryMode
	 * @param available
	 * @param bookList
	 * @param currSpellBook
	 */
	public SpellModel(InfoSpells spells, int primaryMode, int secondaryMode, boolean available, List bookList, String currSpellBook)
	{
		super(null);

		//
		// if you change/add/remove entries to nameList
		// you also need to change the static COL_XXX defines
		// at the begining of this file
		//

		if (Spell.hasPPCost())
		{
//			availNameList = new String[]{"Name", "School", "Descriptor", "Cost", "Source"};
//			selNameList = new String[]{"Name", "School", "SubSchool", "Descriptor", "Components", "Casting Time", "Range", "Description", "Target Area", "Duration", "Save Info", "SR", "Source File"};
			if (available)
			{
				colTranslateList = new int[]{COL_NAME, COL_SCHOOL, COL_DESCRIPTOR, COL_PPCOST, COL_SRC};
			}
			else
			{
				colTranslateList = new int[]{COL_NAME, COL_SCHOOL, COL_SUBSCHOOL, COL_DESCRIPTOR, COL_PPCOST, COL_COMPONENT, COL_CASTTIME, COL_RANGE, COL_DESCRIPTION, COL_TARGET, COL_DURATION, COL_SAVE, COL_SR, COL_SRC};
			}
		}
		else
		{
//			availNameList = new String[]{"Name", "School", "Descriptor", "Source"};
//			selNameList = new String[]{"Name", "School", "SubSchool", "Descriptor", "Components", "Casting Time", "Range", "Description", "Target Area", "Duration", "Save Info", "SR", "Source File"};
			if (available)
			{
				colTranslateList = new int[]{COL_NAME, COL_SCHOOL, COL_DESCRIPTOR, COL_SRC };
			}
			else
			{
				colTranslateList = new int[]{COL_NAME, COL_SCHOOL, COL_SUBSCHOOL, COL_DESCRIPTOR, COL_COMPONENT, COL_CASTTIME, COL_RANGE, COL_DESCRIPTION, COL_TARGET, COL_DURATION, COL_SAVE, COL_SR, COL_SRC};
			}
		}

		colNameList = makeHeaderList(colTranslateList);

		resetModel(primaryMode, secondaryMode, available, bookList, currSpellBook);
	}


	private String[] makeHeaderList(int[] transList)
	{
		String[] aList = new String[transList.length];
		String aString;
		for (int i = 0; i < transList.length; ++i)
		{
			switch(transList[i])
			{
				case COL_NAME:
					aString = "Name";
					break;

				case COL_SCHOOL:
					aString = "School";
					break;

				case COL_SUBSCHOOL:
					aString = "SubSchool";
					break;

				case COL_DESCRIPTOR:
					aString = "Descriptor";
					break;

				case COL_COMPONENT:
					aString = "Components";
					break;

				case COL_CASTTIME:
					aString = "Casting Time";
					break;

				case COL_RANGE:
					aString = "Range";
					break;

				case COL_DESCRIPTION:
					aString = "Description";
					break;

				case COL_TARGET:
					aString = "Target Area";
					break;

				case COL_DURATION:
					aString = "Duration";
					break;

				case COL_SAVE:
					aString = "Save Info";
					break;

				case COL_SR:
					aString = "SR";
					break;

				case COL_SRC:
					aString = "Source File";
					break;

				case COL_PPCOST:
					aString = "PP Cost";
					break;

				default:
					aString = Integer.toString(transList[i]);
					break;
			}
			aList[i] = aString;
		}
		return aList;
	}

	private int translateColumn(final int column)
	{
		return colTranslateList[column];
	}


	/**
	 * Returns boolean if can edit a cell. (SpellModel)
	 * @param node
	 * @param column
	 * @return true if cell editable
	 **/
	public boolean isCellEditable(Object node, int column)
	{
		column = translateColumn(column);
		return (column == COL_NAME);
	}

	/**
	 * Returns Class for the column. (SpellModel)
	 * @param column
	 * @return Class
	 **/
	public Class getColumnClass(int column)
	{
		column = translateColumn(column);
		return (column == COL_NAME) ? TreeTableModel.class : String.class;
	}

	/* The JTreeTableNode interface. */

	/**
	 * Returns int number of columns. (SpellModel)
	 * @return column count
	 **/
	public int getColumnCount()
	{
		return colNameList.length;
	}

	/**
	 * Returns String name of a column. (SpellModel)
	 * @param column
	 * @return colmun name
	 **/
	public String getColumnName(int column)
	{
		return colNameList[column];
	}

	/**
	 * return the root node
	 * @return Object
	 */
	public Object getRoot()
	{
		return (PObjectNode) super.getRoot();
	}

	/**
	 * Returns Object value of the column. (SpellModel)
	 * @param node
	 * @param column
	 * @return value
	 **/
	public Object getValueAt(Object node, int column)
	{
		final PObjectNode fn = (PObjectNode) node;
		Spell aSpell = null;
		CharacterSpell spellA = null;

		if (fn == null)
		{
			Logging.errorPrint("Somehow we have no active node when doing getValueAt in ");

			return null;
		}

		if (fn.getItem() instanceof SpellInfo)
		{
			spellA = ((SpellInfo) fn.getItem()).getOwner();
			aSpell = spellA.getSpell();
			((SpellInfo) fn.getItem()).getActualLevel();
		}

		column = translateColumn(column);

		switch (column)
		{
			case COL_NAME:
				return fn.toString();

			case COL_SCHOOL:
				return (aSpell != null) ? aSpell.getSchool() : null;

			case COL_SUBSCHOOL:
				return (aSpell != null) ? aSpell.getSubschool() : null;

			case COL_DESCRIPTOR:
				return (aSpell != null) ? aSpell.descriptor() : null;

			case COL_COMPONENT:
				return (aSpell != null) ? aSpell.getComponentList() : null;

			case COL_CASTTIME:
				return (aSpell != null) ? aSpell.getCastingTime() : null;

			case COL_RANGE:
				return (aSpell != null) ? aSpell.getRange() : null;

			case COL_DESCRIPTION:

				if ((aSpell != null) && (spellA != null))
				{
					return pc.parseSpellString(aSpell, aSpell.getDescription(), spellA.getOwner());
				}

				return (aSpell != null) ? aSpell.piDescString() : null;

			case COL_TARGET:

				if ((aSpell != null) && (spellA != null))
				{
					return pc.parseSpellString(aSpell, aSpell.getTarget(), spellA.getOwner());
				}

				return (aSpell != null) ? aSpell.getTarget() : null;

			case COL_DURATION:

				if ((aSpell != null) && (spellA != null))
				{
					return pc.parseSpellString(aSpell, aSpell.getDuration(), spellA.getOwner());
				}

				return (aSpell != null) ? aSpell.getDuration() : null;

			case COL_SAVE:
				return (aSpell != null) ? aSpell.getSaveInfo() : null;

			case COL_SR:
				return (aSpell != null) ? aSpell.getSpellResistance() : null;

			case COL_SRC:
				return (aSpell != null) ? aSpell.getSource() : null;

			case COL_PPCOST:
				return (spellA != null) ? new Integer(((SpellInfo) fn.getItem()).getActualPPCost()) : null;

			default:
				return fn.getItem();
		}

		// return null;
	}

	/**
	 * There must be a root node, but we keep it hidden
	 * @param aNode
	 */
	private void setRoot(PObjectNode aNode)
	{
		super.setRoot(aNode);
	}

	/**
	 * this method looks for any domains associated with this class
	 * and finds any spells associated with this domain
	 * and then adds this list to a "Domains" directory
	 * to keep the Domain list in an easily distinguished place
	 * @param className
	 * @param theParent
	 * @param iLev
	 **/
	private void addDomainSpellsForClass(String className, PObjectNode theParent, int iLev)
	{
		int iMax = pc.getCharacterDomainList().size();

		if (iMax == 0)
		{
			return;
		}

		PObjectNode p = new PObjectNode();
		p.setItem("Domains");

		boolean dom = false;

		for (int iDom = 0; iDom < pc.getCharacterDomainList().size(); ++iDom)
		{
			CharacterDomain aCD = (CharacterDomain) pc.getCharacterDomainList().get(iDom);
			Domain aDom = aCD.getDomain();

			// if any domains have this class as a source
			// and is a valid domain, add them
			if ((aDom != null) && aCD.isFromPCClass(className))
			{
				List domainSpells = Globals.getSpellsIn(iLev, "", aDom.getName());
				p.setParent(theParent);

				if (!dom)
				{
					theParent.addChild(p);
				}

				dom = true;
				setNodeSpells(domainSpells, p, iLev, aDom, Globals.getDefaultSpellBook(), pc);
			}
		}
	}
	/**
	 * This function takes a branch and adds the spells to it.
	 * @param charSpells
	 * @param tNode
	 * @param iLev
	 * @param obj
	 * @param book
	 * @param pc
	 */
	private void setNodeSpells(List charSpells, PObjectNode tNode, int iLev, PObject obj, String book, PlayerCharacter pc)
	{
		for (Iterator fI = charSpells.iterator(); fI.hasNext();)
		{
			Object o = fI.next();
			PObjectNode fCN;

			if (o instanceof CharacterSpell)
			{
				final CharacterSpell cs = (CharacterSpell) o;
				final SpellInfo si = cs.getSpellInfoFor(book, iLev, -1);

				if (si == null)
				{
					continue;
				}

				fCN = new PObjectNode();
				fCN.setItem(si);
			}
			else
			{
				Spell aSpell = (Spell) o;

				if (!aSpell.levelForKeyContains(obj.getSpellKey(), iLev, pc))
				{
					continue;
				}

				CharacterSpell cs = new CharacterSpell(obj, aSpell);
				SpellInfo si = cs.addInfo(iLev, 1, book);
				fCN = new PObjectNode();
				fCN.setItem(si);
			}

			fCN.setParent(tNode);
			tNode.addChild(fCN);
		}
		 // end spells loop
	}

	/**
	 * changes the column order sequence and/or number of
	 * columns based on modelType (0=available, 1=selected)
	 **/
/*	private int adjustAvailColumnConst(int column)
	{
		if (modelType == MODEL_AVAIL)
		{
			if (column == COL_SUBSCHOOL)
			{
				column = COL_DESCRIPTOR;
			}
			else if (column >= COL_DESCRIPTOR)
			{
				column = COL_SRC;
			}
		}

		return column;
	}
*/
	/**
	 * This assumes the SpellModel exists but
	 * needs branches and nodes to be repopulated
	 * @param primaryMode
	 * @param secondaryMode
	 * @param available
	 * @param bookList
	 * @param currSpellBook
	 */
	public void resetModel(int primaryMode, int secondaryMode, boolean available, List bookList, String currSpellBook)
	{
		List classList = new ArrayList();
		List spellList = new ArrayList();
		includeRace = false;

		PObjectNode [] primaryNodes = null;
		PObjectNode [] secondaryNodes = null;
		PObjectNode [] bookNodes = null;

		theRoot = new PObjectNode();
		setRoot(theRoot);

		if (pc == null) {
		    return;
		}

		if (!available) {
			bookNodes = new PObjectNode [bookList.size()];
			int ix = 0;
			for (Iterator iBook = bookList.iterator(); iBook.hasNext();)
			{
				String bookName = (String)iBook.next();
				bookNodes[ix] = new PObjectNode();
				bookNodes[ix].setItem(bookName);
				bookNodes[ix].setParent(theRoot);
				spellList.addAll(pc.getRace().getSpellSupport().getCharacterSpell(null, bookName, -1));
				ix++;
			}
			theRoot.setChildren(bookNodes);
		}

		includeRace = !spellList.isEmpty();


		getSpellcastingClasses(available, classList, spellList);

		if (includeRace)
		{
			classList.add(pc.getRace());
		}


		// the structure will be
		// root
		//   (book names) bookNodes (only for right-side tab, the !available e.g. selected spells)
		//     primary nodes  (the first "sort by" selection)
		//       secondary nodes (the second "sort by" selection)
		// the first time (e.g. firstPass==true) through the loop, make sure all nodes are created and attached
		boolean firstPass = true;
		HashMap usedMap = new HashMap();
		String mapKey;

		for (Iterator ii = spellList.iterator(); ii.hasNext();)
		{
			Object sp = ii.next();
			Spell spell = null;
			CharacterSpell cs = null;

			if (sp instanceof CharacterSpell)
			{
				cs = (CharacterSpell)sp;
				spell = cs.getSpell();
				if (available && cs.getOwner() instanceof Domain)
				{
					// domain spells elsewhere
					continue;
				}
			}
			if (sp instanceof Spell)
			{
				spell = (Spell)sp;
			}

			// for each spellbook, ignored for "available" left-side of tab
			// the <= bookList.size() is intended, so it will be processed once
			// when ix==0 for the !available (selected) model
			for (int ix = 0; ix <= bookList.size(); ix++)
			{
				if (!available && ix == bookList.size())
					break;
				if (available && ix>0) {
				    break;
				}
				// default currently selected spellbook
				String bookName = currSpellBook;
				if (!available)
				{
					bookName = bookList.get(ix).toString();
				}
				if (firstPass)
				{
					primaryNodes = getNodesByMode(primaryMode, classList);
				}
				else if (!available)
				{
					// get the primaryNodes, which are the specified bookNode's children
					bookNodes[ix].getChildren().toArray(primaryNodes);
				}
				else
				{
					// get the primaryNodes, which are the root's children
					theRoot.getChildren().toArray(primaryNodes);
				}

				for (int pindex = 0 ; pindex < primaryNodes.length; pindex++)
				{
					if (sp instanceof Spell)
						cs = null;
					boolean primaryMatch = false; // spell match's primaryNode's criteria
					boolean spellMatch = false; // spell match's primaryNode and secondaryNode criteria
					SpellInfo si = null;
					PObject aClass = null;
					int iLev = -1;

					switch (primaryMode)
					{
						case GuiConstants.INFOSPELLS_VIEW_CLASS:     	// By Class
							aClass = (PObject)classList.get(pindex);
							primaryMatch = (spell.getFirstLevelForKey(aClass.getSpellKey(), pc) >= 0);
							if (cs != null)
							{
								if (aClass instanceof Race)
								{
									primaryMatch = (cs.getOwner() == aClass);
								}
								else if (cs.getOwner() instanceof Domain)
								{
									primaryMatch = aClass.getSpellSupport().containsCharacterSpell(cs);
								}
								else
								{
									primaryMatch = (aClass == cs.getOwner());
								}
							}
						break;
						case GuiConstants.INFOSPELLS_VIEW_LEVEL:     	// By Level
							iLev = pindex;
							primaryMatch = true;
							si = null;
							if (primaryMatch && cs != null)
							{
								si = cs.getSpellInfoFor(bookName, iLev, -1);
							}
							if (si == null)
								primaryMatch = spell.isLevel(iLev, pc);
							else if (available && si != null && si.getFeatList()!=null)
								continue;
						break;
						case GuiConstants.INFOSPELLS_VIEW_DESCRIPTOR:   // By Descriptor
							primaryMatch = spell.getDescriptorList().contains(primaryNodes[pindex].toString());
						break;
						case GuiConstants.INFOSPELLS_VIEW_RANGE:   // By Range
							primaryMatch = spell.getRange().equals(primaryNodes[pindex].toString());
						break;
						case GuiConstants.INFOSPELLS_VIEW_DURATION:   // By Duration
							primaryMatch = spell.getDuration().equals(primaryNodes[pindex].toString());
						break;
						case GuiConstants.INFOSPELLS_VIEW_TYPE:   // By Type
							primaryMatch = spell.isType(primaryNodes[pindex].toString());
						break;
						case GuiConstants.INFOSPELLS_VIEW_SCHOOL:   // By Type
							primaryMatch = spell.getSchools().contains(primaryNodes[pindex].toString());
						break;
					}
       					if (secondaryMode == GuiConstants.INFOSPELLS_VIEW_NOTHING)
					{
						if (!firstPass && !primaryMatch)
							continue;
						secondaryNodes = new PObjectNode[1];
						secondaryNodes[0] = new PObjectNode();
						secondaryNodes[0].setItem("");
					}
					else if (firstPass)
					{
						secondaryNodes = getNodesByMode(secondaryMode, classList);
					}
					else
					{
						if (!primaryMatch)
						{
							continue;
						}
						primaryNodes[pindex].getChildren().toArray(secondaryNodes);
					}

					for (int sindex = 0 ; sindex < secondaryNodes.length; sindex++)
					{
						mapKey = bookName+"."+primaryNodes[pindex].toString()+"."+secondaryNodes[sindex].toString();
						switch (secondaryMode)
						{
							case GuiConstants.INFOSPELLS_VIEW_CLASS:     	// By Class
								aClass = (PObject)classList.get(sindex);
								spellMatch = primaryMatch && (spell.getFirstLevelForKey(aClass.getSpellKey(), pc) >= 0);
							break;
							case GuiConstants.INFOSPELLS_VIEW_LEVEL:     	// By Level
								iLev = sindex;
								spellMatch = primaryMatch;
								si = null;
								if (spellMatch && cs != null)
								{
									si = cs.getSpellInfoFor(bookName, iLev, -1);
								}
								if (si == null && primaryMatch)
									spellMatch = spell.isLevel(iLev, pc);
								if (available && si != null && si.getFeatList()!=null)
									continue;
							break;
							case GuiConstants.INFOSPELLS_VIEW_DESCRIPTOR:   // By Descriptor
								spellMatch = primaryMatch && spell.getDescriptorList().contains(secondaryNodes[sindex].toString());
							break;
							case GuiConstants.INFOSPELLS_VIEW_RANGE:   // By Range
								spellMatch = primaryMatch && spell.getRange().equals(secondaryNodes[sindex].toString());
							break;
							case GuiConstants.INFOSPELLS_VIEW_DURATION:   // By Duration
								spellMatch = primaryMatch && spell.getRange().equals(secondaryNodes[sindex].toString());
							break;
							case GuiConstants.INFOSPELLS_VIEW_TYPE:   // By Type
								spellMatch = primaryMatch && spell.isType(secondaryNodes[sindex].toString());
							break;
							case GuiConstants.INFOSPELLS_VIEW_NOTHING:   // No secondary criteria
								spellMatch = primaryMatch;
							break;
							case GuiConstants.INFOSPELLS_VIEW_SCHOOL:   // By Type
								spellMatch = primaryMatch && spell.getSchools().contains(secondaryNodes[sindex].toString());
							break;
						}
						if (firstPass && secondaryMode != GuiConstants.INFOSPELLS_VIEW_NOTHING)
						{
							secondaryNodes[sindex].setParent(primaryNodes[pindex]);
							if (available && aClass != null && iLev > -1 && (aClass instanceof PCClass))
							{
								addDomainSpellsForClass(((PCClass)aClass).getCastAs(), secondaryNodes[sindex], iLev);
							}
						}

						// Make sure we have
						// the right spellbook
						if ((si != null) && !si.getBook().equals(bookName))
						{
							continue;
						}

						if (!available && (si != null))
						{
							List aList = (List)usedMap.get(mapKey);
							if (aList != null && aList.contains(si))
							{
								continue;
							}
						}
						if (aClass != null && iLev > -1 && spellMatch && (aClass instanceof PCClass))
						{
							int theLevel = iLev;
							if (si != null)
								theLevel = -1;
							PObject theObject = aClass;
							if (cs != null)
								theObject = cs.getOwner();
							spellMatch = spell.levelForKeyContains(theObject.getSpellKey(), theLevel, pc);
						}
						if (spellMatch && si == null && available)
						{
							PObject bClass =aClass;
							// if there's only 1 class, then use that to determine which spells are qualified
							if (aClass == null && classList.size()==1)
								bClass = (PObject)classList.get(0);
							cs = new CharacterSpell(bClass, spell);
							si = cs.addInfo(iLev, 1, bookName);
						}
						// didn't find a match, so continue
						if (!spellMatch || si == null)
						{
							continue;
						}

						// Everything looks ok
						// so add this spell
						PObjectNode spellNode = new PObjectNode();
						spellNode.setItem(si);
						PObjectNode thisNode = secondaryNodes[sindex];
						if (secondaryMode == GuiConstants.INFOSPELLS_VIEW_NOTHING)
							thisNode = primaryNodes[pindex];
						spellNode.setParent(thisNode);
						thisNode.addChild(spellNode);
						List aList = (List)usedMap.get(mapKey);
						if (aList == null)
							aList = new ArrayList();
						aList.add(si);
						usedMap.put(mapKey, aList);
					}
					if (secondaryMode != GuiConstants.INFOSPELLS_VIEW_NOTHING)
						primaryNodes[pindex].setChildren(secondaryNodes);
					if (available)
					{
						primaryNodes[pindex].setParent(theRoot);
					}
					else
					{
						primaryNodes[pindex].setParent(bookNodes[ix]);
					}
				} // end primaryNodes
				if (!available)
				{
					bookNodes[ix].setChildren(primaryNodes);
				}
				else
				{
					theRoot.setChildren(primaryNodes);
				}
			} // end bookNodes
			firstPass = false;
		} // end spell list


		PObjectNode rootAsPObjectNode = (PObjectNode) super.getRoot();
		if (rootAsPObjectNode.getChildCount() > 0)
		{
			rootAsPObjectNode.pruneEmpty();
			fireTreeNodesChanged(super.getRoot(), new TreePath(super.getRoot()));
		}
	}


    /**
     * @param available
     * @param classList
     * @param spellList
     */
    private void getSpellcastingClasses(boolean available, List classList, List spellList) {
        // get the list of spell casting Classes
		for (Iterator iClass = pc.getClassList().iterator(); iClass.hasNext();)
		{
			PCClass aClass = (PCClass) iClass.next();
			if (!aClass.getSpellType().equals(Constants.s_NONE))
			{
				if (aClass.zeroCastSpells() && aClass.getKnownList().isEmpty())
				{
					continue;
				}

				classList.add(aClass);

				//if (available && currSpellBook.equals(Globals.getDefaultSpellBook()))
				if (available)
				{
					List aList = Globals.getSpellsIn(-1, aClass.getSpellKey(), "");
					for (Iterator si = aList.iterator(); si.hasNext();)
					{
						Object s = si.next();
						if (!spellList.contains(s))
						spellList.add(s);
					}
				}
				/*
				else if (available)
				{
					spellList.addAll(aClass.getCharacterSpell(null, Globals.getDefaultSpellBook(), -1));
				}
				*/
				else
				{
					spellList.addAll(aClass.getSpellSupport().getCharacterSpellList());
				}
			}
		}
    }


    /**
     * @param primaryMode
     * @param classList
     * @return PObjectNode[]
     */
    private PObjectNode[] getNodesByMode(int primaryMode, List classList) {
        PObjectNode[] primaryNodes = null;
        switch (primaryMode)
        {
        	case GuiConstants.INFOSPELLS_VIEW_CLASS:     	// By Class
        		primaryNodes = getClassNameNodes(classList);
        	break;
        	case GuiConstants.INFOSPELLS_VIEW_LEVEL:     	// By Level
        		primaryNodes = getLevelNodes();
        	break;
        	case GuiConstants.INFOSPELLS_VIEW_DESCRIPTOR:   // By Descriptor
        		primaryNodes = getDescriptorNodes();
        	break;
        	case GuiConstants.INFOSPELLS_VIEW_RANGE:   	// By Range
        		primaryNodes = getRangeNodes();
        	break;
        	case GuiConstants.INFOSPELLS_VIEW_DURATION: 	// By Duration
        		primaryNodes = getDurationNodes();
        	break;
        	case GuiConstants.INFOSPELLS_VIEW_TYPE: 	// By Type
        		primaryNodes = getTypeNodes();
        	break;
        	case GuiConstants.INFOSPELLS_VIEW_SCHOOL: 	// By School
        		primaryNodes = getSchoolNodes();
        	break;
        }
        return primaryNodes;
    }

    private PObjectNode [] getClassNameNodes(List classList)
	{
		PObjectNode [] tempNodes = new PObjectNode[classList.size()];
		for (int ix = 0; ix < classList.size(); ++ix)
		{
			PObject obj = (PObject) classList.get(ix);
			String objName = obj.piString();
			tempNodes[ix] = new PObjectNode();
			tempNodes[ix].setItem(objName);
		}
		return tempNodes;
	}

    private PObjectNode [] getLevelNodes()
    {
        PObjectNode [] tempNodes = new PObjectNode[20];
        for (int ix = 0; ix < 20; ++ix)
        {
            tempNodes[ix] = new PObjectNode();
            String ix2 = ""+ix;
            if (ix < 10)
            {
                ix2 = " " + ix2;
            }
            tempNodes[ix].setItem("level " + ix2);
        }
        return tempNodes;
    }


	private PObjectNode [] getDescriptorNodes()
	{
		PObjectNode [] tempNodes = new PObjectNode[Globals.getDescriptorSet().size()];
		int ix = 0;
		for (Iterator ti = Globals.getDescriptorSet().iterator(); ti.hasNext(); )
		{
			tempNodes[ix] = new PObjectNode();
			tempNodes[ix++].setItem(ti.next());
		}
		return tempNodes;
	}

	private PObjectNode [] getRangeNodes()
	{
		PObjectNode [] tempNodes = new PObjectNode[Globals.getRangesSet().size()];
		int ix = 0;
		for (Iterator ti = Globals.getRangesSet().iterator(); ti.hasNext(); )
		{
			tempNodes[ix] = new PObjectNode();
			tempNodes[ix++].setItem(ti.next());
		}
		return tempNodes;
	}

	private PObjectNode [] getDurationNodes()
	{
		PObjectNode [] tempNodes = new PObjectNode[Globals.getDurationSet().size()];
		int ix = 0;
		for (Iterator ti = Globals.getDurationSet().iterator(); ti.hasNext(); )
		{
			tempNodes[ix] = new PObjectNode();
			tempNodes[ix++].setItem(ti.next());
		}
		return tempNodes;
	}

	private PObjectNode [] getTypeNodes()
	{
		PObjectNode [] tempNodes = new PObjectNode[Globals.getTypeForSpells().size()];
		int ix = 0;
		for (Iterator ti = Globals.getTypeForSpells().iterator(); ti.hasNext(); )
		{
			tempNodes[ix] = new PObjectNode();
			tempNodes[ix++].setItem(ti.next());
		}
		return tempNodes;
	}

	private PObjectNode [] getSchoolNodes()
	{
		PObjectNode [] tempNodes = new PObjectNode[SettingsHandler.getGame().getUnmodifiableSchoolsList().size()];
		int ix = 0;
		for (Iterator ti = SettingsHandler.getGame().getUnmodifiableSchoolsList().iterator(); ti.hasNext(); )
		{
			tempNodes[ix] = new PObjectNode();
			tempNodes[ix++].setItem(ti.next());
		}
		return tempNodes;
	}

    /**
     * @param pc
     */
    public void setCharacter(PlayerCharacter pc) {
        this.pc = pc;
    }

}