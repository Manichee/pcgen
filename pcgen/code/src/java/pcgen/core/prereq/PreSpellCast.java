/*
 * PreSpellCast.java
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
 * Current Ver: $Revision: 1.11 $
 * Last Editor: $Author: binkley $
 * Last Edited: $Date: 2005/10/18 20:23:37 $
 *
 */
package pcgen.core.prereq;

import pcgen.core.PCClass;
import pcgen.core.PlayerCharacter;
import pcgen.util.PropertyFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author wardc
 *
 */
public class PreSpellCast
extends AbstractPrerequisiteTest
implements PrerequisiteTest {

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#passes(pcgen.core.PlayerCharacter)
	 */
	public int passes(final Prerequisite prereq, final PlayerCharacter character) {

		final int requiredNumber = Integer.parseInt( prereq.getOperand() );
		final String prereqSpellType = prereq.getKey();
		int runningTotal=0;

		final List classList = (ArrayList) character.getClassList().clone();
		if (!classList.isEmpty())
		{
			for (Iterator e1 = classList.iterator(); e1.hasNext();)
			{
				final PCClass aClass = (PCClass) e1.next();
				if (prereqSpellType.equals(aClass.getSpellType()))
				{
					runningTotal++;
				}
			}
		}

		runningTotal = prereq.getOperator().compare(runningTotal, requiredNumber);
		return countedTotal(prereq, runningTotal);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#kindsHandled()
	 */
	public String kindHandled() {
		return "spellcast.type"; //$NON-NLS-1$
	}


	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#toHtmlString(pcgen.core.prereq.Prerequisite)
	 */
	public String toHtmlString(final Prerequisite prereq) {
		final Object[] args = new Object[] { prereq.getOperator().toDisplayString(),
		prereq.getOperand(),
		prereq.getKey()};
		return  PropertyFactory.getFormattedString("PreSpellCast.toHtml", args); //$NON-NLS-1$
	}

}
