/*
 * PreCheckBase.java
 *
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
 * Created on 19-Dec-2003
 *
 * Current Ver: $Revision$
 *
 * Last Editor: $Author$
 *
 * Last Edited: $Date$
 *
 */
package plugin.pretokens.test;

import pcgen.cdom.base.CDOMObject;
import pcgen.core.Globals;
import pcgen.core.PCCheck;
import pcgen.core.PlayerCharacter;
import pcgen.core.prereq.AbstractPrerequisiteTest;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteTest;

/**
 * @author wardc
 *
 */
public class PreCheckBaseTester extends AbstractPrerequisiteTest implements PrerequisiteTest
{

	/**
	 * Get the type of prerequisite handled by this token.
	 * @return the type of prerequisite handled by this token.
	 */
    @Override
	public String kindHandled()
	{
		return "checkbase"; //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#passes(pcgen.core.prereq.Prerequisite, pcgen.core.PlayerCharacter)
	 */
	@Override
	public int passes(final Prerequisite prereq, final PlayerCharacter character, CDOMObject source)
	{
		int runningTotal = 0;

		final String checkName = prereq.getKey();
		final int operand =
				character.getVariableValue(prereq.getOperand(), "").intValue(); //$NON-NLS-1$
		PCCheck check = Globals.getContext().ref
				.silentlyGetConstructedCDOMObject(PCCheck.class, checkName);
		if (check != null)
		{
			final int characterCheckBonus = character.getBaseCheck(check);
			runningTotal =
					prereq.getOperator().compare(characterCheckBonus, operand) > 0
						? 1 : 0;
		}
		return countedTotal(prereq, runningTotal);
	}

}