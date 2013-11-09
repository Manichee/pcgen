/*
 * PreEquipPrimaryTest.java
 * Copyright 2006 (C) Aaron Divinsky <boomer70@yahoo.com>
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
 */
package pcgen.core.prereq;

import pcgen.AbstractCharacterTestCase;
import pcgen.cdom.enumeration.EquipmentLocation;
import pcgen.cdom.enumeration.FormulaKey;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.enumeration.Type;
import pcgen.cdom.formula.FixedSizeFormula;
import pcgen.core.Equipment;
import pcgen.core.Globals;
import pcgen.core.PlayerCharacter;
import pcgen.core.Race;
import pcgen.core.character.WieldCategory;
import pcgen.rules.context.LoadContext;

/**
 * <code>PreEquipPrimaryTest</code> tests that the PREEQUIPPRIMARY tag is
 * working correctly.
 *
 * Last Editor: $Author: $
 * Last Edited: $Date$
 *
 * @author Aaron Divinsky <boomer70@yahoo.com>
 * @version $Revision$
 */
public class PreEquipPrimaryTest extends AbstractCharacterTestCase
{

	/*
	 * Class under test for int passes(Prerequisite, PlayerCharacter)
	 */
	public void testPassesPrerequisitePlayerCharacter()
	{
		final PlayerCharacter character = getCharacter();

		final Equipment dagger = new Equipment();
		dagger.setName("Dagger");

		character.addEquipment(dagger);
		dagger.setIsEquipped(true, character);
		dagger.setLocation(EquipmentLocation.EQUIPPED_PRIMARY);
		character.doAfavorForAunitTestThatIgnoresEquippingRules();

		final Prerequisite prereq = new Prerequisite();
		prereq.setKind("equipprimary");
		prereq.setKey("DAGGER");
		prereq.setOperand("1");
		prereq.setOperator(PrerequisiteOperator.EQ);

		final boolean passes = PrereqHandler.passes(prereq, character, null);
		assertTrue(passes);

		dagger.setName("Dagger (Masterwork)");

		assertFalse("Should be an exact match only", PrereqHandler.passes(
			prereq, character, null));

		prereq.setKey("DAGGER%");

		assertTrue("Should allow wildcard match", PrereqHandler.passes(prereq,
			character, null));
	}

	/**
	 * Test equipment type tests
	 * @throws Exception
	 */
	public void testType() throws Exception
	{
		final PlayerCharacter character = getCharacter();

		final Equipment longsword = new Equipment();
		longsword.setName("Longsword");

		character.addEquipment(longsword);
		longsword.setIsEquipped(true, character);
		longsword.setLocation(EquipmentLocation.EQUIPPED_PRIMARY);
		character.doAfavorForAunitTestThatIgnoresEquippingRules();

		Prerequisite prereq = new Prerequisite();
		prereq.setKind("equipprimary");
		prereq.setKey("TYPE=Slashing");
		prereq.setOperand("1");
		prereq.setOperator(PrerequisiteOperator.EQ);

		assertFalse("Equipment has no type", PrereqHandler.passes(prereq,
			character, null));

		longsword.addType(Type.getConstant("SLASHING"));

		assertTrue("Equipment is slashing", PrereqHandler.passes(prereq,
			character, null));

		prereq.setKey("TYPE.Armor");

		assertFalse("Equipment is not armor", PrereqHandler.passes(prereq,
			character, null));
	}

	/**
	 * Test wield category tests
	 * @throws Exception
	 */
	public void testWield() throws Exception
	{
		final PlayerCharacter character = getCharacter();

		final Race race = new Race();
		race.setName("Test Race");
		race.put(FormulaKey.SIZE, new FixedSizeFormula(medium));

		character.setRace(race);
		LoadContext context = Globals.getContext();

		final Equipment longsword = new Equipment();
		longsword.setName("Dagger");

		character.addEquipment(longsword);
		longsword.setIsEquipped(true, character);
		longsword.setLocation(EquipmentLocation.EQUIPPED_PRIMARY);
		character.doAfavorForAunitTestThatIgnoresEquippingRules();

		Prerequisite prereq = new Prerequisite();
		prereq.setKind("equipprimary");
		prereq.setKey("WIELDCATEGORY=OneHanded");
		prereq.setOperand("1");
		prereq.setOperator(PrerequisiteOperator.EQ);

		// Test 3.0 Style
		longsword.put(ObjectKey.SIZE, medium);
		longsword.put(ObjectKey.BASESIZE, medium);

		assertTrue("Weapon is M therefore OneHanded", PrereqHandler.passes(
			prereq, character, null));

		longsword.put(ObjectKey.SIZE, large);
		longsword.put(ObjectKey.BASESIZE, large);

		assertFalse("Weapon is L therefore TwoHanded", PrereqHandler.passes(
			prereq, character, null));

		// Test 3.5 style
		longsword.put(ObjectKey.SIZE, medium);
		longsword.put(ObjectKey.BASESIZE, medium);
		longsword.put(ObjectKey.WIELD, context.ref.silentlyGetConstructedCDOMObject(
				WieldCategory.class, "TwoHanded"));

		assertFalse("Weapon is TwoHanded", PrereqHandler.passes(prereq,
			character, null));

		longsword.put(ObjectKey.WIELD, context.ref.silentlyGetConstructedCDOMObject(
				WieldCategory.class, "OneHanded"));

		assertTrue("Weapon is OneHanded", PrereqHandler.passes(prereq,
			character, null));

	}
}