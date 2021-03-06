/*
 * PreRegion.java
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
 * Current Ver: $Revision: 1.7 $
 * Last Editor: $Author: byngl $
 * Last Edited: $Date: 2005/10/03 13:54:30 $
 *
 */
package pcgen.core.prereq;

import pcgen.core.PlayerCharacter;
import pcgen.util.PropertyFactory;

/**
 * @author wardc
 *
 */
public class PreRegion extends AbstractPrerequisiteTest implements PrerequisiteTest {

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#passes(pcgen.core.PlayerCharacter)
	 */
	public int passes(final Prerequisite prereq, final PlayerCharacter character)  throws PrerequisiteException{

		final String requiredRegion = prereq.getKey().toUpperCase();
		final String characterRegion = character.getFullRegion().toUpperCase();

		final boolean sameRegion = characterRegion.startsWith( requiredRegion );

		int runningTotal;
		if (prereq.getOperator().equals(PrerequisiteOperator.EQ))
		{
			runningTotal = sameRegion ? 1 : 0;
		}
		else if (prereq.getOperator().equals(PrerequisiteOperator.NEQ))
		{
			runningTotal = sameRegion ? 0 : 1;
		}
		else
		{
			throw new PrerequisiteException(PropertyFactory.getFormattedString("PreRegion.error.invalid_comparator", prereq.toString() )); //$NON-NLS-1$
		}

		return countedTotal(prereq, runningTotal);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#kindsHandled()
	 */
	public String kindHandled() {
		return "REGION"; //$NON-NLS-1$
	}

}
