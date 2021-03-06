/*
 * PreMove.java
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
 * Last Editor: $Author: karianna $
 * Last Edited: $Date: 2006/02/15 16:32:42 $
 *
 */
package pcgen.core.prereq;

import pcgen.core.PlayerCharacter;
import pcgen.util.PropertyFactory;

/**
 * Passes PREMOVE tags
 */
public class PreMove extends AbstractPrerequisiteTest implements PrerequisiteTest {

	/** Constructor */
	public PreMove() {
		super();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#passes(pcgen.core.prereq.Prerequisite, pcgen.core.PlayerCharacter)
	 */
	public int passes(final Prerequisite prereq, final PlayerCharacter character)
		throws PrerequisiteException
	{
		int runningTotal = 0;
		int moveAmount = 0;
		
		if (hasMovement(character))
		{
			final String moveType = prereq.getKey();

			try
			{
				moveAmount = Integer.parseInt( prereq.getOperand() );
			}
			catch (NumberFormatException e)
			{
				throw new PrerequisiteException(PropertyFactory.getFormattedString("PreMove.error.bad_operand", prereq.toString()) ); //$NON-NLS-1$
			}

			for (int x = 0; x < character.getNumberOfMovements(); ++x)
			{
			    final String type = character.getMovementType(x);
			    final int speed = character.getMovement(x).intValue();
				if (moveType.equalsIgnoreCase(type) && speed >= moveAmount)
				{
					runningTotal += character.getMovement(x).intValue();
				}
			}
		}

		runningTotal = prereq.getOperator().compare(runningTotal, moveAmount);
		return countedTotal(prereq, runningTotal);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#kindsHandled()
	 */
	public String kindHandled() {
		return "MOVE"; //$NON-NLS-1$
	}
	
	/**
	 * Returns true if character's movements can be found
	 * 
	 * @param character
	 * @return true or false
	 */
	private boolean hasMovement(PlayerCharacter character) {
		if (character != null
				&& character.getRace() != null
				//CONSIDER Is this gate on null movement necessary (can you legally get a race w/ no movement?)
				&& character.getRace().getMovement() != null
				&& character.getRace().getMovement().getNumberOfMovementTypes() != 0) {
			return true;
		}
		return false;
	}

}
