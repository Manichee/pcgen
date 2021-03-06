/*
 * PreText.java
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
 * Current Ver: $Revision: 1.14 $
 * Last Editor: $Author: binkley $
 * Last Edited: $Date: 2005/10/18 20:23:37 $
 *
 */
package pcgen.core.prereq;

import pcgen.core.Equipment;
import pcgen.core.PlayerCharacter;
import pcgen.util.PropertyFactory;

import java.util.Iterator;
import java.util.List;

/**
 * @author frugal@purplewombat.co.uk
 *
 */
public class PreType
	extends AbstractPrerequisiteTest
	implements PrerequisiteTest {

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#kindHandled()
	 */
	public String kindHandled() {
		return "TYPE"; //$NON-NLS-1$
	}


	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#passes(pcgen.core.prereq.Prerequisite, pcgen.core.Equipment)
	 */
	public int passes(final Prerequisite prereq, final Equipment equipment, PlayerCharacter aPC)
		throws PrerequisiteException
	{

		final String requiredType = prereq.getKey();
		int runningTotal = 0;

		if (prereq.getOperator().equals(PrerequisiteOperator.EQ))
		{
			if (equipment.isPreType(requiredType))
			{
				runningTotal++;
			}
		}
		else if (prereq.getOperator().equals(PrerequisiteOperator.NEQ))
		{
			if (!equipment.isPreType(requiredType))
			{
				runningTotal++;
			}
		}
		else
		{
			throw new PrerequisiteException(PropertyFactory.getFormattedString("PreType.error.invalidComparison", prereq.getOperator().toString(), prereq.toString() )); //$NON-NLS-1$
		}

		runningTotal = countedTotal(prereq, runningTotal);
		return runningTotal;
	}


	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#passes(pcgen.core.prereq.Prerequisite, pcgen.core.PlayerCharacter)
	 */
	public int passes(final Prerequisite prereq, final PlayerCharacter aPC)
	{

	    if (aPC==null) {
	        return 0;
	    }

	    final String requiredType = prereq.getKey();
	    final int numRequired = Integer.parseInt(prereq.getOperand());
	    int runningTotal = 0;

	    final List typeList = aPC.getTypes();
	    for (Iterator iter = typeList.iterator(); iter.hasNext();) {
            final String element = (String) iter.next();

            if (element.equalsIgnoreCase(requiredType)) {
                runningTotal++;
            }
        }

		runningTotal = prereq.getOperator().compare(runningTotal, numRequired);
		return countedTotal(prereq, runningTotal);
	}


	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#toHtmlString(pcgen.core.prereq.Prerequisite)
	 */
	public String toHtmlString(final Prerequisite prereq) {
		return PropertyFactory.getFormattedString("PreType.toHtml", prereq.getOperator().toDisplayString(), prereq.getKey() ); //$NON-NLS-1$
	}

}
