/*
 * PreClassTest.java
 *
 * Copyright 2004 (C) Chris Ward <frugal@purplewombat.co.uk>
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
 * Created on 15-Jan-2004
 *
 * Current Ver: $Revision$
 *
 * Last Editor: $Author$
 *
 * Last Edited: $Date$
 *
 */
package pcgen.core.prereq;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import pcgen.AbstractCharacterTestCase;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.StringKey;
import pcgen.cdom.reference.CDOMDirectSingleRef;
import pcgen.core.Globals;
import pcgen.core.PCClass;
import pcgen.core.PlayerCharacter;
import pcgen.core.bonus.Bonus;
import pcgen.core.bonus.BonusObj;
import pcgen.persistence.PersistenceLayerException;
import pcgen.persistence.lst.prereq.PreParserFactory;
import pcgen.rules.context.LoadContext;
import plugin.pretokens.parser.PreClassLevelMaxParser;
import plugin.pretokens.test.PreClassTester;

/**
 * Test class for PRECLASS token
 * 
 * @author frugal@purplewombat.co.uk
 */
public class PreClassTest extends AbstractCharacterTestCase
{
    /**
     * Main method in case we want to run JUnit from the command line
     * 
     * @param args
     */
    public static void main(final String[] args)
	{
		TestRunner.run(PreClassTest.class);
	}

	/**
	 * Std JUnit suite return method
	 * @return PreClassTest
	 */
	public static Test suite()
	{
		return new TestSuite(PreClassTest.class);
	}

	/**
	 * Test to ensure that a character with a named class can be found
	 * @throws Exception
	 */
	public void testNamedClass() throws Exception
	{
		final PCClass pcClass = new PCClass();
		pcClass.setName("MyClass");
		pcClass.put(StringKey.SPELLTYPE, "ARCANE");

		final PlayerCharacter character = getCharacter();
		character.incrementClassLevel(3, pcClass);

		final Prerequisite prereq = new Prerequisite();
		prereq.setKind("class");
		prereq.setKey("myclass");
		prereq.setOperand("1");
		prereq.setOperator(PrerequisiteOperator.GTEQ);

		final PreClassTester test = new PreClassTester();
		final int passes = test.passes(prereq, character, null);
		assertEquals(1, passes);
	}
	/**
	 * Test to ensure that a character with a ServeAs class can be found
	 * @throws Exception
	 */
	public void testNamedClassServesAs() throws Exception
	{
		final PCClass pcClass = new PCClass();
		pcClass.setName("MyClass");
		final PCClass warrior = new PCClass();
		warrior.setName("Warrior");
		final PCClass ranger = new PCClass();
		ranger.setName("Ranger");
		pcClass.addToListFor(ListKey.SERVES_AS_CLASS, CDOMDirectSingleRef.getRef(warrior));
		pcClass.addToListFor(ListKey.SERVES_AS_CLASS, CDOMDirectSingleRef.getRef(ranger));
		
		final PlayerCharacter character = getCharacter();
		character.incrementClassLevel(3, pcClass);
		
		final Prerequisite prereq = new Prerequisite();
		prereq.setKind("class");
		prereq.setKey("Warrior");
		prereq.setOperand("1");
		prereq.setOperator(PrerequisiteOperator.GTEQ);

		final PreClassTester test = new PreClassTester();
		final int passes = test.passes(prereq, character, null);
		assertEquals(1, passes);
	}

	/**
	 * Test to ensure that a character will fail a test
	 * if it does not have the correct number of levels
	 * in the class.
	 * @throws Exception
	 */
	public void testTooFewLevels() throws Exception
	{
		final PCClass pcClass = new PCClass();
		pcClass.setName("MyClass");
		pcClass.put(StringKey.SPELLTYPE, "ARCANE");

		final PlayerCharacter character = getCharacter();
		character.incrementClassLevel(1, pcClass);

		final Prerequisite prereq = new Prerequisite();
		prereq.setKind("class");
		prereq.setKey("myclass");
		prereq.setOperand("3");
		prereq.setOperator(PrerequisiteOperator.GTEQ);

		final PreClassTester test = new PreClassTester();
		final int passes = test.passes(prereq, character, null);
		assertEquals(0, passes);
	}

	/**
	 * Test to ensure that a character will fail a test
	 * if it does not have the correct number of levels
	 * in the class.
	 * @throws Exception
	 */
	public void testCharWithMultipleClasses() throws Exception
	{
		final PCClass pcClass = new PCClass();
		pcClass.setName("MyClass");
		pcClass.put(StringKey.SPELLTYPE, "ARCANE");

		final PCClass pcClass2 = new PCClass();
		pcClass2.setName("Other Class");

		final PlayerCharacter character = getCharacter();
		character.incrementClassLevel(1, pcClass);
		character.incrementClassLevel(2, pcClass2);

		final Prerequisite prereq = new Prerequisite();
		prereq.setKind("class");
		prereq.setKey("other class");
		prereq.setOperand("2");
		prereq.setOperator(PrerequisiteOperator.GTEQ);

		final PreClassTester test = new PreClassTester();
		final int passes = test.passes(prereq, character, null);
		assertEquals(1, passes);
	}

	/**
	 * Test to ensure that a character will fail a test
	 * if it does not have the correct number of levels
	 * in the class.
	 * @throws Exception
	 */
	public void testCharWithMultipleSpellClasses() throws Exception
	{
		LoadContext context = Globals.getContext();
		final PCClass pcClass = new PCClass();
		pcClass.setName("MyClass");
		context.unconditionallyProcess(pcClass, "SPELLSTAT", "CHA");
		pcClass.put(StringKey.SPELLTYPE, "ARCANE");
		context.unconditionallyProcess(pcClass.getOriginalClassLevel(1), "CAST", "5,4");

		final PCClass pcClass2 = new PCClass();
		pcClass2.setName("Other Class");
		context.unconditionallyProcess(pcClass2, "SPELLSTAT", "INT");
		pcClass2.put(StringKey.SPELLTYPE, "ARCANE");
		context.unconditionallyProcess(pcClass2.getOriginalClassLevel(1), "CAST", "5,4");

		final PlayerCharacter character = getCharacter();
		setPCStat(character, cha, 12);
		setPCStat(character, intel, 12);
		character.incrementClassLevel(1, pcClass);
		character.incrementClassLevel(2, pcClass2);

		final Prerequisite prereq = new Prerequisite();
		prereq.setKind("class");
		prereq.setKey("SPELLCASTER.ARCANE");
		prereq.setOperand("3");
		prereq.setTotalValues(true);
		prereq.setOperator(PrerequisiteOperator.GTEQ);

		final PreClassTester test = new PreClassTester();
		final int passes = test.passes(prereq, character, null);
		assertEquals(3, passes);
	}

	/**
	 * Test to ensure that a character will fail a test
	 * if it does not have the correct number of levels
	 * in the class.
	 * @throws Exception
	 */
	public void testFromParserCharWithMultipleSpellClasses() throws Exception
	{
		LoadContext context = Globals.getContext();
		final PCClass pcClass = new PCClass();
		pcClass.setName("MyClass");
		context.unconditionallyProcess(pcClass, "SPELLSTAT", "CHA");
		pcClass.put(StringKey.SPELLTYPE, "ARCANE");
		context.unconditionallyProcess(pcClass.getOriginalClassLevel(1), "CAST", "5,4");

		final PCClass pcClass2 = new PCClass();
		pcClass2.setName("Other Class");
		context.unconditionallyProcess(pcClass2, "SPELLSTAT", "INT");
		pcClass2.put(StringKey.SPELLTYPE, "ARCANE");
		context.unconditionallyProcess(pcClass2.getOriginalClassLevel(1), "CAST", "5,4");

		final PlayerCharacter character = getCharacter();
		setPCStat(character, cha, 12);
		setPCStat(character, intel, 12);
		character.incrementClassLevel(1, pcClass);
		character.incrementClassLevel(2, pcClass2);

		final PreParserFactory factory = PreParserFactory.getInstance();

		final Prerequisite prereq = factory.parse("PRECLASS:1,SPELLCASTER.ARCANE=3");

		final PreClassTester test = new PreClassTester();
		int passes = test.passes(prereq, character, null);
		// Doens't pass - levels not summed...
		assertEquals(0, passes);
		character.incrementClassLevel(1, pcClass2);
		passes = test.passes(prereq, character, null);
		assertEquals(1, passes);
	}

	/**
	 * Test to ensure that a character will fail a test
	 * if it does not have the correct number of levels
	 * in the class.
	 * @throws Exception
	 */
	public void testFromParserAny() throws Exception
	{
		LoadContext context = Globals.getContext();
		final PCClass pcClass = new PCClass();
		pcClass.setName("MyClass");
		context.unconditionallyProcess(pcClass, "SPELLSTAT", "CHA");
		pcClass.put(StringKey.SPELLTYPE, "ARCANE");
		context.unconditionallyProcess(pcClass.getOriginalClassLevel(1), "CAST", "5,4");

		final PCClass pcClass2 = new PCClass();
		pcClass2.setName("Other Class");
		context.unconditionallyProcess(pcClass2, "SPELLSTAT", "INT");
		pcClass2.put(StringKey.SPELLTYPE, "ARCANE");
		context.unconditionallyProcess(pcClass2.getOriginalClassLevel(1), "CAST", "5,4");

		final PlayerCharacter character = getCharacter();
		character.incrementClassLevel(1, pcClass);
		character.incrementClassLevel(2, pcClass2);

		final PreParserFactory factory = PreParserFactory.getInstance();

		final Prerequisite prereq = factory.parse("PRECLASS:1,ANY=3");

		final PreClassTester test = new PreClassTester();
		int passes = test.passes(prereq, character, null);
		// Doens't pass - levels not summed...
		assertEquals(0, passes);
		character.incrementClassLevel(1, pcClass2);
		passes = test.passes(prereq, character, null);
		assertEquals(1, passes);
	}

	/**
	 * Test to ensure that a character without a named class cannot be found
	 * @throws Exception
	 */
	public void testNamedClassFail() throws Exception
	{
		final PCClass pcClass = new PCClass();
		pcClass.setName("MyClass");
		pcClass.put(StringKey.SPELLTYPE, "ARCANE");

		final PlayerCharacter character = getCharacter();
		character.incrementClassLevel(1, pcClass);

		final Prerequisite prereq = new Prerequisite();
		prereq.setKind("class");
		prereq.setKey("Druid");
		prereq.setOperand("1");
		prereq.setOperator(PrerequisiteOperator.GTEQ);

		final PreClassTester test = new PreClassTester();
		final int passes = test.passes(prereq, character, null);
		assertEquals(0, passes);
	}

	/**
	 * Test to ensure that a character without a named class cannot be found
	 * @throws Exception
	 */
	public void testNoLevelsPass() throws Exception
	{
		final PCClass pcClass = new PCClass();
		pcClass.setName("Monk");

		final PlayerCharacter character = getCharacter();
		character.incrementClassLevel(1, pcClass);

		final Prerequisite prereq = new Prerequisite();
		prereq.setKind("class");
		prereq.setKey("Druid");
		prereq.setOperand("1");
		prereq.setOperator(PrerequisiteOperator.LT);

		final PreClassTester test = new PreClassTester();
		final int passes = test.passes(prereq, character, null);
		assertEquals(1, passes);
	}

	/**
	 * Test to ensure that a character without a named class cannot be found
	 * @throws Exception
	 */
	public void testNoLevelsFail() throws Exception
	{
		final PCClass pcClass = new PCClass();
		pcClass.setName("Monk");

		final PlayerCharacter character = getCharacter();
		character.incrementClassLevel(1, pcClass);

		final Prerequisite prereq = new Prerequisite();
		prereq.setKind("class");
		prereq.setKey("Monk");
		prereq.setOperand("1");
		prereq.setOperator(PrerequisiteOperator.LT);

		final PreClassTester test = new PreClassTester();
		final int passes = test.passes(prereq, character, null);
		assertEquals(0, passes);
	}

	/**
	 * Test to ensure that a character with a spellcasting class can be found
	 * @throws Exception
	 */
	public void testSpellcaster() throws Exception
	{
		LoadContext context = Globals.getContext();
		final PCClass pcClass = new PCClass();
		pcClass.put(StringKey.SPELLTYPE, "ARCANE");
		context.unconditionallyProcess(pcClass.getOriginalClassLevel(1), "CAST", "5,4");
		context.unconditionallyProcess(pcClass, "SPELLSTAT", "CHA");

		final PlayerCharacter character = getCharacter();
		setPCStat(character, cha, 12);
		character.incrementClassLevel(1, pcClass);

		final Prerequisite prereq = new Prerequisite();
		prereq.setKind("class");
		prereq.setKey("Spellcaster");
		prereq.setOperand("1");
		prereq.setOperator(PrerequisiteOperator.GTEQ);

		final PreClassTester test = new PreClassTester();
		final int passes = test.passes(prereq, character, null);
		assertEquals(1, passes);
	}

	/**
	 * Test to ensure that a character with a spellcasting class
	 * does not match a different spellcasting type
	 * @throws Exception
	 */
	public void testSpellcasterTypeFail() throws Exception
	{
		final PCClass pcClass = new PCClass();
		pcClass.put(StringKey.SPELLTYPE, "ARCANE");

		final PlayerCharacter character = getCharacter();
		character.incrementClassLevel(1, pcClass);

		final Prerequisite prereq = new Prerequisite();
		prereq.setKind("class");
		prereq.setKey("Spellcaster.DIVINE");
		prereq.setOperand("1");
		prereq.setOperator(PrerequisiteOperator.GTEQ);

		final PreClassTester test = new PreClassTester();
		final int passes = test.passes(prereq, character, null);
		assertEquals(0, passes);
	}

	/**
	 * Test to ensure that a character with a spellcasting class
	 * will pass a prerequisute that requires a level of that
	 * classes spell type.
	 * @throws Exception
	 */
	public void testSpellcasterTypePass() throws Exception
	{
		LoadContext context = Globals.getContext();
		final PCClass pcClass = new PCClass();
		pcClass.put(StringKey.SPELLTYPE, "ARCANE");
		context.unconditionallyProcess(pcClass.getOriginalClassLevel(1), "CAST", "5,4");
		context.unconditionallyProcess(pcClass, "SPELLSTAT", "CHA");

		final PlayerCharacter character = getCharacter();
		setPCStat(character, cha, 12);
		character.incrementClassLevel(1, pcClass);

		final Prerequisite prereq = new Prerequisite();
		prereq.setKind("class");
		prereq.setKey("Spellcaster.ARCANE");
		prereq.setOperand("1");
		prereq.setOperator(PrerequisiteOperator.GTEQ);

		final PreClassTester test = new PreClassTester();
		final int passes = test.passes(prereq, character, null);
		assertEquals(1, passes);
	}

	/**
	 * Test to ensure that a spellcaster type check is case insensitive
	 * @throws Exception
	 */
	public void testSpellcasterTypeWrongCasePass() throws Exception
	{
		LoadContext context = Globals.getContext();
		final PCClass pcClass = new PCClass();
		pcClass.put(StringKey.SPELLTYPE, "ARCANE");
		context.unconditionallyProcess(pcClass.getOriginalClassLevel(1), "CAST", "5,4");
		context.unconditionallyProcess(pcClass, "SPELLSTAT", "CHA");

		final PlayerCharacter character = getCharacter();
		character.incrementClassLevel(1, pcClass);
		setPCStat(character, cha, 12);

		final Prerequisite prereq = new Prerequisite();
		prereq.setKind("class");
		prereq.setKey("Spellcaster.Arcane");
		prereq.setOperand("1");
		prereq.setOperator(PrerequisiteOperator.GTEQ);

		final PreClassTester test = new PreClassTester();
		final int passes = test.passes(prereq, character, null);
		assertEquals(1, passes);
	}

	/**
	 * Test the PRE CLASSLEVELMAX token 
	 * @throws Exception
	 */
	public void testPreClassLevelMax() throws Exception
	{
		final PreClassLevelMaxParser parser = new PreClassLevelMaxParser();
		final Prerequisite prereq =
				parser.parse("CLASSLEVELMAX", "1,Monk=1", false, false);
		final Prerequisite dualPrereq =
				parser.parse("CLASSLEVELMAX", "2,Monk=1,Fighter=1", false, false);
		final Prerequisite singlePrereq =
				parser.parse("CLASSLEVELMAX", "1,Monk=1,Fighter=1", false, false);

		final PlayerCharacter character = getCharacter();

		assertTrue("Should pass with no levels", PrereqHandler.passes(prereq,
			character, null));
		assertTrue("Should pass with no levels of either", PrereqHandler
			.passes(dualPrereq, character, null));

		final PCClass pcClass = new PCClass();
		pcClass.setName("Monk");
		character.incrementClassLevel(1, pcClass);
		assertTrue("Should pass with 1 level", PrereqHandler.passes(prereq,
			character, null));
		assertTrue("Should pass with 1 level of one", PrereqHandler.passes(
			dualPrereq, character, null));

		final PCClass ftrClass = new PCClass();
		ftrClass.setName("Fighter");
		character.incrementClassLevel(1, ftrClass);
		assertTrue("Should pass with 1 level of each", PrereqHandler.passes(
			dualPrereq, character, null));

		character.incrementClassLevel(1, pcClass);
		assertFalse("Should not pass with 2 levels", PrereqHandler.passes(
			prereq, character, null));
		assertFalse("Should not pass with 2 levels of one", PrereqHandler
			.passes(dualPrereq, character, null));
		assertTrue("Should pass with 2 levels of one", PrereqHandler.passes(
			singlePrereq, character, null));
	}

	public void testOldPreClassLevelMax() throws Exception
	{
		final PreClassLevelMaxParser parser = new PreClassLevelMaxParser();
		try
		{
			parser.parse("CLASSLEVELMAX", "Fighter=2", false, false);
		    fail();
		}
		catch (PersistenceLayerException e)
		{
		    // Do Nothing, we should catch an exception here
		}
	}

	/**
	 * Test to ensure that a character will fail a test
	 * if it does not have the correct number of levels
	 * in the class.
	 * @throws Exception
	 */
	public void testAnyLevelsOneClass() throws Exception
	{
		final PCClass pcClass = new PCClass();
		pcClass.setName("MyClass");
		pcClass.put(StringKey.SPELLTYPE, "ARCANE");

		final PlayerCharacter character = getCharacter();
		character.incrementClassLevel(1, pcClass);

		final Prerequisite prereq = new Prerequisite();
		prereq.setKind("class");
		prereq.setKey("Any");
		prereq.setOperand("3");
		prereq.setOperator(PrerequisiteOperator.GTEQ);

		final PreClassTester test = new PreClassTester();
		assertEquals(0, test.passes(prereq, character, null));

		character.incrementClassLevel(2, pcClass);
		assertEquals(1, test.passes(prereq, character, null));
	}

	public void testAnyLevelTwo() throws Exception
	{
		final PCClass pcClass = new PCClass();
		pcClass.setName("MyClass");
		pcClass.put(StringKey.SPELLTYPE, "ARCANE");

		final PCClass pcClass2 = new PCClass();
		pcClass2.setName("MyClass2");
		pcClass2.put(StringKey.SPELLTYPE, "DIVINE");

		final PlayerCharacter character = getCharacter();
		character.incrementClassLevel(1, pcClass);

		final Prerequisite prereq = new Prerequisite();
		prereq.setKind("class");
		prereq.setKey("Any");
		prereq.setOperand("2");
		prereq.setOperator(PrerequisiteOperator.GTEQ);

		final PreClassTester test = new PreClassTester();
		assertEquals(0, test.passes(prereq, character, null));

		character.incrementClassLevel(1, pcClass2);
		assertEquals(0, test.passes(prereq, character, null));

		character.incrementClassLevel(1, pcClass2);
		assertEquals(1, test.passes(prereq, character, null));
	}


	public void testLevelsTwoClasses() throws Exception
	{
		final PCClass pcClass = new PCClass();
		pcClass.setName("MyClass");
		pcClass.put(StringKey.SPELLTYPE, "ARCANE");

		final PCClass pcClass2 = new PCClass();
		pcClass2.setName("MyClass2");
		pcClass2.put(StringKey.SPELLTYPE, "DIVINE");

		final PlayerCharacter character = getCharacter();
		character.incrementClassLevel(1, pcClass);

		final PreParserFactory factory = PreParserFactory.getInstance();
		Prerequisite prereq = factory.parse("PRECLASS:2,MyClass=1,MyClass2=2");

		assertEquals(false, PrereqHandler.passes(prereq, character, null));

		character.incrementClassLevel(1, pcClass2);
		assertEquals(false, PrereqHandler.passes(prereq, character, null));

		character.incrementClassLevel(1, pcClass2);
		assertEquals(true, PrereqHandler.passes(prereq, character, null));
	}

	public void testAnyLevelsTwoClasses() throws Exception
	{
		final PCClass pcClass = new PCClass();
		pcClass.setName("MyClass");
		pcClass.put(StringKey.SPELLTYPE, "ARCANE");

		final PCClass pcClass2 = new PCClass();
		pcClass2.setName("MyClass2");
		pcClass2.put(StringKey.SPELLTYPE, "DIVINE");

		final PlayerCharacter character = getCharacter();

		final PreParserFactory factory = PreParserFactory.getInstance();
		Prerequisite prereq = factory.parse("PRECLASS:2,ANY=1");

		character.incrementClassLevel(1, pcClass);
		assertEquals(false, PrereqHandler.passes(prereq, character, null));

		character.incrementClassLevel(1, pcClass2);
		assertEquals(true, PrereqHandler.passes(prereq, character, null));
	}


	public void testAnyTwoLevelsTwoClasses() throws Exception
	{
		final PCClass pcClass = new PCClass();
		pcClass.setName("MyClass");
		pcClass.put(StringKey.SPELLTYPE, "ARCANE");

		final PCClass pcClass2 = new PCClass();
		pcClass2.setName("MyClass2");
		pcClass2.put(StringKey.SPELLTYPE, "DIVINE");

		final PlayerCharacter character = getCharacter();

		final PreParserFactory factory = PreParserFactory.getInstance();
		Prerequisite prereq = factory.parse("PRECLASS:2,ANY=2");

		character.incrementClassLevel(2, pcClass);
		assertEquals(false, PrereqHandler.passes(prereq, character, null));

		character.incrementClassLevel(1, pcClass2);
		assertEquals(false, PrereqHandler.passes(prereq, character, null));

		character.incrementClassLevel(1, pcClass2);
		assertEquals(true, PrereqHandler.passes(prereq, character, null));
	}

	public void testSpellcasterLevelsTwoClasses() throws Exception
	{
		final PCClass pcClass = new PCClass();
		LoadContext context = Globals.getContext();

		pcClass.setName("MyClass");
		pcClass.put(StringKey.SPELLTYPE, "ARCANE");
		BonusObj aBonus = Bonus.newBonus(context, "CASTERLEVEL|MyClass|CL");
		
		if (aBonus != null)
		{
			pcClass.addToListFor(ListKey.BONUS, aBonus);
		}

		final PCClass pcClass2 = new PCClass();
		pcClass2.setName("MyClass2");
		pcClass2.put(StringKey.SPELLTYPE, "DIVINE");
		aBonus = Bonus.newBonus(context, "CASTERLEVEL|MyClass2|CL");
		
		if (aBonus != null)
		{
			pcClass.addToListFor(ListKey.BONUS, aBonus);
		}

		final PlayerCharacter character = getCharacter();

		final PreParserFactory factory = PreParserFactory.getInstance();
		Prerequisite prereq = factory.parse("PRECLASS:2,SPELLCASTER=1");

		character.incrementClassLevel(1, pcClass);
		assertEquals(false, PrereqHandler.passes(prereq, character, null));

		character.incrementClassLevel(1, pcClass2);
		assertEquals(true, PrereqHandler.passes(prereq, character, null));
	}
	
	public void testSpellcasterTypeLevelsTwoClasses() throws Exception
	{
		final PCClass pcClass = new PCClass();
		LoadContext context = Globals.getContext();

		pcClass.setName("MyClass");
		pcClass.put(StringKey.SPELLTYPE, "ARCANE");
		BonusObj aBonus = Bonus.newBonus(context, "CASTERLEVEL|MyClass|CL");
		
		if (aBonus != null)
		{
			pcClass.addToListFor(ListKey.BONUS, aBonus);
		}

		final PCClass pcClass2 = new PCClass();
		pcClass2.setName("MyClass2");
		pcClass2.put(StringKey.SPELLTYPE, "ARCANE");
		aBonus = Bonus.newBonus(context, "CASTERLEVEL|MyClass2|CL");
		
		if (aBonus != null)
		{
			pcClass.addToListFor(ListKey.BONUS, aBonus);
		}

		final PlayerCharacter character = getCharacter();

		final PreParserFactory factory = PreParserFactory.getInstance();
		Prerequisite prereq = factory.parse("PRECLASS:2,SPELLCASTER.ARCANE=1");

		character.incrementClassLevel(1, pcClass);
		assertEquals(false, PrereqHandler.passes(prereq, character, null));

		character.incrementClassLevel(1, pcClass2);
		assertEquals(true, PrereqHandler.passes(prereq, character, null));
	}
}