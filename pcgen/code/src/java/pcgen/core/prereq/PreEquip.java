/*
 * PreEquip.java
 * Copyright 2001 (C) Bryan McRoberts <merton_monk@yahoo.com>
 * Copyright 2003 (C) Chris Ward <frugal@purplewombat.co.uk>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	   See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on November 28, 2003
 *
 * Current Ver: $Revision: 1.13 $
 * Last Editor: $Author: binkley $
 * Last Edited: $Date: 2005/10/18 20:23:37 $
 *
 */
package pcgen.core.prereq;

import pcgen.core.Equipment;
import pcgen.core.Globals;
import pcgen.core.PlayerCharacter;
import pcgen.core.RuleConstants;
import pcgen.core.character.WieldCategory;
import pcgen.util.PropertyFactory;

import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * @author wardc
 *
 */
public class PreEquip extends AbstractPrerequisiteTest implements PrerequisiteTest {

	/*
	 * (non-Javadoc)
	 *
	 * @see pcgen.core.prereq.PrerequisiteTest#passes(pcgen.core.PlayerCharacter)
	 */
	public int passes(final Prerequisite prereq, final PlayerCharacter character) throws PrerequisiteException
	{
		int runningTotal = 0;

		final int number;
		try
		{
			number = Integer.parseInt(prereq.getOperand());
		}
		catch (NumberFormatException exceptn)
		{
			throw new PrerequisiteException(PropertyFactory.getFormattedString("PreFeat.error", prereq.toString())); //$NON-NLS-1$
		}

		if (!character.getEquipmentList().isEmpty())
		{

			final String targetEquip = prereq.getKey();
			for (Iterator e1 = character.getEquipmentList().iterator(); e1.hasNext();)
			{
				final Equipment eq = (Equipment) e1.next();

				if (!eq.isEquipped())
				{
					continue;
				}

				if (targetEquip.startsWith("WIELDCATEGORY=") || targetEquip.startsWith("WIELDCATEGORY."))
				{
					if (eq.hasWield())
					{
						if (Globals.checkRule(RuleConstants.SIZECAT) || Globals.checkRule(RuleConstants.SIZEOBJ))
						{
							final WieldCategory wCat = Globals.effectiveWieldCategory(character, eq);
							if ((wCat != null) && wCat.getName().equalsIgnoreCase(targetEquip.substring(14)))
							{
								++runningTotal;
								break;
							}
						}
					}
				}
				else if (targetEquip.startsWith("TYPE=") || targetEquip.startsWith("TYPE."))	//$NON-NLS-1$ //$NON-NLS-2$
				{
					StringTokenizer tok = new StringTokenizer(targetEquip.substring(5).toUpperCase(), ".");
					boolean match = false;
					if (tok.hasMoreTokens())
					{
						match = true;
					}
					//
					// Must match all listed types in order to qualify
					//
					while(tok.hasMoreTokens())
					{
						final String type = tok.nextToken();
						if (!eq.isType(type))
						{
							match = false;
							break;
						}
					}
					if (match)
					{
						++runningTotal;
						break;
					}
				}
				else	//not a TYPE string
				{
					final String eqName = eq.getName().toUpperCase();

					if (targetEquip.indexOf('%') >= 0)
					{
						//handle wildcards (always assume
						// they end the line)
						final int percentPos = targetEquip.indexOf('%');
						final String substring = targetEquip.substring(0, percentPos);
						if ((eqName.startsWith(substring)))
						{
							++runningTotal;
							break;
						}
					}
					else if (eqName.equalsIgnoreCase(targetEquip))
					{
						//just a straight String compare
						++runningTotal;
						break;
					}
				}
			}
		}

		runningTotal = prereq.getOperator().compare(runningTotal, number);
		return countedTotal(prereq, runningTotal);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see pcgen.core.prereq.PrerequisiteTest#kindsHandled()
	 */
	public String kindHandled() {
		return "EQUIP"; //$NON-NLS-1$
	}
}