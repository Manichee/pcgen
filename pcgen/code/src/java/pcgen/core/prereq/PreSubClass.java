/*
 * PreSubClass.java
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
 * Current Ver: $Revision: 1.10 $
 * Last Editor: $Author: binkley $
 * Last Edited: $Date: 2005/10/18 20:23:37 $
 *
 */
package pcgen.core.prereq;

import pcgen.core.PCClass;
import pcgen.core.PlayerCharacter;
import pcgen.util.PropertyFactory;

import java.util.Iterator;


/**
 * @author wardc
 *
 */
public class PreSubClass  extends AbstractPrerequisiteTest implements PrerequisiteTest {

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#passes(pcgen.core.PlayerCharacter)
	 */
	public int passes(final Prerequisite prereq, final PlayerCharacter character) throws PrerequisiteException {
		int runningTotal=0;
		int num;
		try
		{
			num = Integer.parseInt( prereq.getOperand() ); // number we must match
		}
		catch (NumberFormatException nfe)
		{
			throw new PrerequisiteException(PropertyFactory.getFormattedString("PreSubClass.error.badly_formed", prereq.toString())); //$NON-NLS-1$
		}

		final String thisClass = prereq.getKey();
		for (Iterator it = character.getClassList().iterator(); it.hasNext(); )
		{
			final PCClass aClass = (PCClass) it.next();
			final String subClassName = aClass.getSubClassName();
			if (subClassName.length() != 0)
			{
				if (thisClass.equalsIgnoreCase(subClassName))
				{
					runningTotal++;
					break;
				}
			}
		}

		runningTotal = prereq.getOperator().compare(runningTotal, num);
		return countedTotal(prereq, runningTotal);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#kindsHandled()
	 */
	public String kindHandled() {
		return "SUBCLASS"; //$NON-NLS-1$
	}

}
