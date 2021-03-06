/*
 * LevelAbility.java
 * Copyright 2001 (C) Dmitry Jemerov
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
 * Created on July 23, 2001, 8:30 PM
 *
 * $Id: LevelAbility.java,v 1.15 2005/10/22 19:17:37 nuance Exp $
 */
package pcgen.core.levelability;

import pcgen.core.*;
import pcgen.core.pclevelinfo.PCLevelInfo;
import pcgen.core.prereq.PrereqHandler;
import pcgen.util.Logging;
import pcgen.util.chooser.ChooserFactory;
import pcgen.util.chooser.ChooserInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Represents a single ability a character gets when gaining a level (an ADD:
 * entry in the LST file).
 *
 * @author   Dmitry Jemerov <yole@spb.cityline.ru>
 * @version  $Revision: 1.15 $
 */
public class LevelAbility extends PObject implements LevelAbilityInterface
{
	protected PObject          owner;
	protected String           rawTagData;
	protected String           aText;

	private int                level;
	protected int              type;
	protected static final int SKILL       = 2;
	private static final int   DOMAIN      = 4;
	protected static final int SPELLCASTER = 6;
	private static final int   SPELLLEVEL  = 7;
	private static final int   TYPE        = 8;

	LevelAbility(final PObject aOwner, final int aLevel, final String tagData)
	{
		owner      = aOwner;
		level      = aLevel;
		rawTagData = tagData;
	}

	/**
	 * The entry in the LST file with ADD: stripped off the front of it, this
	 * needs parsed and processed before it's useful for adding stuff to a PC
	 *
	 * @return  the the string that specifies what this adds to a character
	 */
	public final String getTagData()
	{
		return rawTagData;
	}

	/**
	 * The Character Level that this LevelAbility was added to the character at.
	 *
	 * @return  The level that this applies to
	 */
	public final int level()
	{
		return level;
	}

	/**
	 * Does this represent an added Ability
	 *
	 * @return  true if this represents an added Ability
	 */
	public boolean isAbility()
	{
		return false;
	}

	/**
	 * Does this represent an added Feat
	 *
	 * @return  true if this represents an added Feat
	 */
	public boolean isFeat()
	{
		return false;
	}

	/**
	 * Does this LevelAbility grant a Language
	 *
	 * @return  true if this adds a Language
	 */
	public boolean isLanguage()
	{
		return false;
	}

	/**
	 * Performs the processing necessary to add this to a PC.
	 *
	 * @param  aPC          A PlayerCharacter object.
	 * @param  pcLevelInfo  The info about the level the PC just acquired
	 */
	public final void process(final PlayerCharacter aPC, final PCLevelInfo pcLevelInfo)
	{
		process(null, aPC, pcLevelInfo);
	}

	/**
	 * Process this Level ability for the given PC.  This is used in two
	 * separate ways.  Firstly, it builds a list of things which may be granted.
	 * Then, if a list was passed to availableList (i.e. not null), the choices
	 * are added to this list and the method returns.  If null is passed to the
	 * first argument, the choices are presented to the user.
	 *
	 * @param  availableList  if non null, gets the list of choices for this
	 *                        LevelAbility
	 * @param  aPC            The PC to process the LevelAbility for
	 * @param  pcLevelInfo    If the choices are being added, this represent the
	 *                        level to add them to.
	 */

	public void process(
		final List            availableList,
		final PlayerCharacter aPC,
		final PCLevelInfo     pcLevelInfo)
	{
		aText = rawTagData;

		setType(aPC);

		final ChooserInterface c       = ChooserFactory.getChooserInstance();
		String                 bString = prepareChooser(c, aPC);

		final List choicesList = getChoicesList(bString, aPC);

		if (availableList != null)
		{
			availableList.addAll(choicesList);
		}
		else
		{
			if (c.getPool() == Integer.MIN_VALUE)
			{
				processChoice(
						choicesList,
						choicesList,
						aPC,
						pcLevelInfo);
			}
			else
			{
				c.setAvailableList(choicesList);
				c.setVisible(false);

				if ((choicesList.size() > 0) && (type != SPELLLEVEL))
				{
					for(;;)
					{
						c.setVisible(true);
						if (processChoice(
							choicesList,
							c.getSelectedList(),
							aPC,
							pcLevelInfo))
						{
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Set the type property of this LevelAbility
	 *
	 * @param  aPC
	 */
	protected void setType(final PlayerCharacter aPC)
	{
		type = DOMAIN;

		if (rawTagData.startsWith("SPELLLEVEL"))
		{
			getSpellLevelChoices(aPC, rawTagData);
			type = SPELLLEVEL;
		}
		else if (rawTagData.startsWith("TYPE=") || rawTagData.startsWith("TYPE."))
		{
			type = TYPE;
		}
	}

	/**
	 * Some sort of choice thing to do with adding a SpellLevel
	 *
	 * @param  aPC
	 * @param  typeString
	 */

	/* I extracted this from setType, but I don't have a clue what it's doing, I
	 * assume that the chooser in PObject sets some attributes that LevelAbility
	 * inherits because it's a PObject */

	private void getSpellLevelChoices(final PlayerCharacter aPC, final String typeString)
	{
		final List            aBonusList = new ArrayList();
		final StringTokenizer aTok       = new StringTokenizer(typeString, "[]", false);
		final String          choices    = aTok.nextToken();

		while (aTok.hasMoreTokens())
		{
			aBonusList.add(aTok.nextToken());
		}

		getChoices(choices, aBonusList, aPC);
	}

	/**
	 * Process the choice selected by the user.
	 *
	 * @param  anArrayList
	 * @param  selectedList
	 * @param  aPC
	 * @param  pcLevelInfo
	 */
	public boolean processChoice(
		final List            anArrayList,
		final List            selectedList,
		final PlayerCharacter aPC,
		final PCLevelInfo     pcLevelInfo)
	{
		switch (type)
		{
			case DOMAIN:

				if (aText.startsWith("DOMAIN"))
				{
					processDomain(selectedList, aPC);
				}
				else
				{
					processSA(selectedList, aPC, pcLevelInfo);
				}

				break;

			case TYPE:

				processType(selectedList);

				break;

			/* Haven't dealt with SPELLLEVEL ??? */
			default:
				Logging.errorPrint(
					"In LevelAbility.processChoice the type " + type +
					" is not supported.");

				break;
		}
		return true;
	}

	/**
	 * Add SAs to the Character
	 *
	 * @param  selectedList
	 * @param  aPC
	 * @param  pcLevelInfo
	 */
	private void processSA(
		final List            selectedList,
		final PlayerCharacter aPC,
		final PCLevelInfo     pcLevelInfo)
	{
		final String zText    = aText + '(';
		final int    listSize = selectedList.size();

		for (int index = 0; index < listSize; ++index)
		{
			String sString = selectedList.get(index).toString();

			// must be a Favoured Enemy type
			if (sString.startsWith("TYPE=") || sString.startsWith("TYPE."))
			{
				final LevelAbility la = new LevelAbility(
						owner,
						level,
						sString);
				la.process(aPC, pcLevelInfo);
				sString = la.rawTagData;
			}

			String zString = new StringBuffer().append(zText).append(sString).append(')').toString();

			final SpecialAbility sa = new SpecialAbility(
					zString,
					"PCCLASS|" + owner.getName() + '|' + level);
			owner.addSpecialAbilityToList(sa);
			owner.addSave(zString);
		}
	}

	/**
	 * Add a domain to the character
	 *
	 * @param  selectedList
	 * @param  aPC
	 */
	private void processDomain(final List selectedList, final PlayerCharacter aPC)
	{
		int aLevel;

		String className = null;

		int dnum = aPC.getMaxCharacterDomains() -
			aPC.getCharacterDomainUsed();

		if (dnum <= 0)
		{
			dnum = (int) owner.bonusTo("DOMAIN", "NUMBER", aPC, aPC);
		}

		if (owner instanceof PCClass)
		{
			className = owner.getName();
			aLevel    = ((PCClass) owner).getLevel();

			if (dnum <= 0)
			{
				dnum = (int) ((PCClass) owner).getBonusTo(
						"DOMAIN",
						"NUMBER",
						aLevel,
						aPC);
			}

			// always assume level 1
		}

		if (dnum <= 0)
		{
			dnum = 1;
		}

		final Iterator i = selectedList.iterator();

		while ((dnum > 0) && i.hasNext())
		{
			final String domName = (String) i.next();

			if (aPC.getCharacterDomainIndex(domName) == -1)
			{
				Domain aDom = Globals.getDomainNamed(domName);

				if (aDom == null)
				{
					continue;
				}

				aDom = (Domain) aDom.clone();

				final CharacterDomain aCD = aPC.getNewCharacterDomain(
						className);
				aCD.setDomain(aDom, aPC);
				aPC.addCharacterDomain(aCD);
				aDom.setIsLocked(true, aPC);
				--dnum;
			}
		}
	}

	/**
	 * Add a "type" (seems to be used for favoured enemy stuff) to the character
	 *
	 * @param  selectedList  treats the first entry of the list as a Type to add
	 */
	private void processType(final List selectedList)
	{
		if (selectedList.size() > 0)
		{
			rawTagData = selectedList.get(0).toString();
		}
	}


	/**
	 * @return Returns the owner.
	 */
	public final PObject getOwner() {
		return owner;
	}

	/**
	 * Add a back reference to whatever PObject this is for
	 *
	 * @param  aOwner
	 */
	public final void setOwner(final PObject aOwner)
	{
		owner = aOwner;
	}

	/**
	 * Factory method for creating LevelAbility instances.
	 *
	 * @param   aowner
	 * @param   aLevel
	 * @param   aString
	 *
	 * @return  LevelAbility
	 */
	public static LevelAbility createAbility(
		final PObject aowner,
		final int     aLevel,
		final String  aString)
	{
		if (aString.startsWith("SPECIAL"))
		{
			return new LevelAbilitySpecial(aowner, aLevel, aString);
		}
		else if (aString.startsWith("FEAT"))
		{
			return new LevelAbilityFeat(aowner, aLevel, aString, false);
		}
		else if (aString.startsWith("VFEAT"))
		{
			return new LevelAbilityFeat(aowner, aLevel, aString, true);
		}
		else if (aString.startsWith("ABILITY"))
		{
			return new LevelAbilityAbility(aowner, aLevel, aString, false);
		}
		else if (aString.startsWith("VABILITY"))
		{
			return new LevelAbilityAbility(aowner, aLevel, aString, true);
		}
		else if (aString.startsWith("CLASSSKILLS"))
		{
			return new LevelAbilityClassSkills(aowner, aLevel, aString);
		}
		else if (aString.startsWith("WEAPONBONUS"))
		{
			return new LevelAbilityWeaponBonus(aowner, aLevel, aString);
		}
		else if (aString.startsWith("EQUIP"))
		{
			return new LevelAbilityEquipment(aowner, aLevel, aString);
		}
		else if (aString.startsWith("LIST"))
		{
			return new LevelAbilityList(aowner, aLevel, aString);
		}
		else if (aString.startsWith("Language"))
		{
			return new LevelAbilityLanguage(aowner, aLevel, aString);
		}
		else if (aString.startsWith("SKILL"))
		{
			return new LevelAbilitySkill(aowner, aLevel, aString);
		}
		else if (aString.startsWith("SPELLCASTER"))
		{
			return new LevelAbilitySpellCaster(aowner, aLevel, aString);
		}
		else
		{
			return new LevelAbility(aowner, aLevel, aString);
		}
	}

	/**
	 * Checks if the process() method applies to the ability.
	 *
	 * @return  true if it can be processed
	 */
	public final boolean canProcess()
	{
		final StringTokenizer aTok = new StringTokenizer(rawTagData, "(", false);

		return aTok.countTokens() > 1;
	}

	/**
	 * Executes the static effects of the ability when a character loses a
	 * level.
	 *
	 * @param  aPC  A PlayerCharacter object.
	 */
	public void subForLevel(final PlayerCharacter aPC)
	{
		final StringTokenizer aStrTok    = new StringTokenizer(rawTagData, ",");
		String                thisString;

		while (aStrTok.hasMoreTokens())
		{
			thisString = aStrTok.nextToken();

			if ("FEAT".equals(thisString))
			{
				aPC.adjustFeats(-1);
//				aPC.setFeats(aPC.getFeats() - 1);
			}
			else if (thisString.startsWith("INIT|"))
			{
				if (owner instanceof PCClass)
				{
					// is this necessary?
					// is initMod even used?
					((PCClass) owner).addInitMod(
						-Integer.parseInt(thisString.substring(5)));
				}
			}
		}

		clearAssociated();
	}

	/**
	 * Parses the comma-separated list of the ADD: field and returns the list of
	 * tokens to be shown in the chooser.
	 *
	 * @param   tokenString unparsed string with the choices to be shown in the chooser
	 * @param   aPC         the PC that this LevelAbility is adding to.
	 *
	 * @return  List of choices
	 */
	List getChoicesList(String tokenString, final PlayerCharacter aPC)
	{
		final List aArrayList = new ArrayList(); // available

		if (type == TYPE) // Favoured Enemy type listed
		{
			final String aString = tokenString.substring(5);
			final List   races   = new ArrayList(Globals.getRaceMap().values());

			for (Iterator e = races.iterator(); e.hasNext();)
			{
				final Race race = (Race) e.next();

				if (race.getType().equalsIgnoreCase(aString))
				{
					aArrayList.add(race.getName());
				}
			}

			for (Iterator e = Globals.getClassList().iterator(); e.hasNext();)
			{
				final PCClass aClass = (PCClass) e.next();

				if (aClass.isType(aString) && !aArrayList.contains(aClass.getName()))
				{
					aArrayList.add(aClass.getName());
				}
			}

			return aArrayList;
		}

		tokenString = truncateStringInsideBalancingParenthesis(tokenString);

		StringTokenizer aTok = new StringTokenizer(tokenString, ",", false);
		boolean         flag = true;

		while (aTok.hasMoreTokens() && flag)
		{
			String     aString     = aTok.nextToken();
			final List preReqArray = new ArrayList();

			if (aString.lastIndexOf('<') > -1)
			{
				final StringTokenizer bTok    = new StringTokenizer(tokenString, "<>|", true);
				int                   len     = 0;
				String                pString = "";

				// cut out of loop on hitting second :
				while (bTok.hasMoreTokens() && !(">").equals(pString))
				{
					pString = bTok.nextToken();

					if (len == 0)
					{
						aString = pString;
					}

					if (
						(len > 0) &&
						(pString.startsWith("PRE") || pString.startsWith("!PRE")))
					{
						preReqArray.add(pString);
					}

					len += pString.length();
				}

				if (len < tokenString.length())
				{
					tokenString = tokenString.substring(len + 1);
					aTok    = new StringTokenizer(tokenString, ",", false);
				}
				else
				{
					flag = false;
				}
			}

			if (PrereqHandler.passesAll(preReqArray, aPC, null))
			{
				processToken(aString, aArrayList, aPC);
			}
		}

		return aArrayList;
	}

	/**
	 * The constructor removed the opening parenthesis, this removes the
	 * balancing parenthesis and sets postBalanced to anything that came
	 * after it.
	 * Languages(Elvish,Dwarvish)foo -> tokenstring = Elvish,Dwarvish)foo,
	 * postBalanced = "" -> tokenstring = Elvish,Dwarish, postBalanced = foo
	 *
	 * @param tokenString
	 * @return The string without it's terminating parenthesis and anything
	 *         following it.
	 */
	private String truncateStringInsideBalancingParenthesis(String tokenString) {

		/* When the ADD:"LEVELABILITY"(foo, bar, baz) string is processed by an
		 * instance of LevelAbility, the string that the choices are generated
		 * from begins at the character following the open parenthesis.  This
		 * means that when we find more close parentheses than we've seen open
		 * ones then the original opening parenthesis has been matched.
		 */

		int open  = 0;
		int index;

		for (index = 0; index < tokenString.length(); index++)
		{
			switch (tokenString.charAt(index))
			{
				case '(':
					open += 1;
					break;

				case ')':
					open -= 1;
					break;

				default:
			}

			if (open < 0)
			{
				break;
			}
		}

		if (open < 0) {
			tokenString      = tokenString.substring(0, index);
		}
		return tokenString;
	}

	/**
	 * Performs the initial setup of a chooser.
	 *
	 * @param   chooser
	 * @param aPC
	 *
	 * @return  String
	 */
	String prepareChooser(final ChooserInterface chooser, PlayerCharacter aPC)
	{
		setNumberofChoices(chooser, aPC);

		String bString = rawTagData;

		switch (type)
		{
			case DOMAIN:

				final int a = rawTagData.lastIndexOf('(');

				if (a > 0)
				{
					aText   = rawTagData.substring(0, a);
					bString = rawTagData.substring(a + 1);
				}
				else
				{
					aText   = rawTagData;
					bString = rawTagData;
				}

				chooser.setTitle(aText);

				break;

			case TYPE:
				chooser.setTitle("Type Selection");

				break;

			default:
				Logging.errorPrint(
					"In LevelAbility.prepareChooser the type " + type +
					" is not supported.");

				break;
		}

		return bString;
	}


	/**
	 * Set how many choices (from those available) need to be selected in the
	 * chooser
	 *
	 * @param  chooser
	 * @param aPC
	 */
	protected void setNumberofChoices(final ChooserInterface chooser, PlayerCharacter aPC)
	{
		final int i = rawTagData.lastIndexOf(')');

		if ((i >= 0) && (i < (rawTagData.length() - 1)))
		{
			if (rawTagData.substring(i + 1).equalsIgnoreCase("ALL"))
			{
				chooser.setPool(Integer.MIN_VALUE); // means pick all without asking
				rawTagData = rawTagData.substring(0, i + 1);
			}
			else
			{
				chooser.setPool(aPC.getVariableValue(rawTagData.substring(i + 1), "").intValue());
				rawTagData = rawTagData.substring(0, i + 1);
			}
		}
		else
		{
			chooser.setPool(1);
		}
	}

	/**
	 * Processes a single token in the comma-separated list of the ADD: field
	 * and adds the choices to be shown to the ArrayList.
	 *
	 * @param  aToken        the token to be processed.
	 * @param  anArrayList   the list to add the choice to.
	 * @param  aPC           the PC this Level ability is adding to.
	 */
	void processToken(
		final String          aToken,
		final List            anArrayList,
		final PlayerCharacter aPC)
	{

		switch (type)
		{
			case DOMAIN:

				if (!aPC.hasSpecialAbility(aText + '(' + aToken + ')'))
				{
					anArrayList.add(aToken);
				}

				break;

			default:
				Logging.errorPrint(
					"In LevelAbility.processToken the type " + type +
					" is not supported.");

				break;
		}
	}

	/**
	 * Make a string representation of this object
	 *
	 * @return  a string representation of this object
	 */
	public String toString()
	{
		StringBuffer res = new StringBuffer();
		res.append(this.getClass().getName());
		res.append(": ").append(level);
		res.append(" - ").append(rawTagData);

		return res.toString();
	}
}
