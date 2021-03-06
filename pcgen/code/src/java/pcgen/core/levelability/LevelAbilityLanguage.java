/*
 * LevelAbilityLanguage.java
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
 * Created on Jul 31, 2001, 12:40:47 AM
 *
 * $Id: LevelAbilityLanguage.java,v 1.12 2005/10/22 19:17:37 nuance Exp $
 */
package pcgen.core.levelability;

import pcgen.core.Globals;
import pcgen.core.Language;
import pcgen.core.PObject;
import pcgen.core.PlayerCharacter;
import pcgen.core.pclevelinfo.PCLevelInfo;
import pcgen.util.chooser.ChooserInterface;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a bonus language a PC gets when gaining a level (am ADD:Language
 * line in the LST file).
 * @author Dmitry Jemerov <yole@spb.cityline.ru>
 * @version $Revision: 1.12 $
 */
final class LevelAbilityLanguage extends LevelAbility
{
	LevelAbilityLanguage(final PObject aowner, final int aLevel, final String aList)
	{
		super(aowner, aLevel, aList);
	}


	public boolean isLanguage() {
		return true;
	}

	/**
	 * Parses the comma-separated list of the ADD: field and returns the
	 * list of tokens to be shown in the chooser.
	 * @param bString
	 * @param aPC
	 * @return choices list
	 */
	List getChoicesList(final String bString, final PlayerCharacter aPC)
	{
		final List aList = super.getChoicesList(bString.substring(9), aPC);
		Collections.sort(aList);

		return aList;
	}

	/**
	 * Performs the initial setup of a chooser.
	 * @param c
	 * @param aPC
	 * @return String
	 */
	String prepareChooser(final ChooserInterface c, PlayerCharacter aPC)
	{
		super.prepareChooser(c, aPC);
		c.setTitle("Language Choice");

		return rawTagData;
	}

	/**
	 * Process the choice selected by the user.
	 * @param selectedList
	 * @param aPC
	 * @param pcLevelInfo
	 * @param aArrayList
	 */
	public boolean processChoice(final List aArrayList, final List selectedList, final PlayerCharacter aPC, final PCLevelInfo pcLevelInfo)
	{

		for (int index = 0; index < selectedList.size(); ++index)
		{
			aPC.addLanguage(selectedList.get(index).toString());
		}
		return true;
	}

	/**
	 * Processes a single token in the comma-separated list of the ADD:
	 * field and adds the choices to be shown in the list to anArrayList.
	 *
	 * @param aToken the token to be processed.
	 * @param anArrayList the list to add the choice to.
	 * @param aPC the PC this Level ability is adding to.
	 */
	void processToken(
			String                aToken,
			final List            anArrayList,
			final PlayerCharacter aPC)
	{
		if (aToken.startsWith("TYPE=") || aToken.startsWith("TYPE."))
		{
			aToken = aToken.substring(5);

			for (Iterator e = Globals.getLanguageList().iterator(); e.hasNext();)
			{
				final Language aLang = (Language) e.next();

				if (aLang.isType(aToken))
				{
					anArrayList.add(aLang.getName());
				}
			}
		}
		else
		{
			final Language aLang = Globals.getLanguageNamed(aToken);
			if (aLang != null)
			{
				if (!aPC.getLanguagesList().contains(aLang))
				{
					anArrayList.add(aToken);
				}
			}
			else
			{
				System.err.println("Language not found: " + aToken);
			}
		}
	}
}
