/*
 * AbilityListTokenTest.java
 * Copyright 2008 (C) James Dempsey
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
 * Created on 26/12/2008 9:55:57 AM
 *
 * $Id: $
 */

package plugin.lsttokens.gamemode.abilitycategory;

import java.util.ArrayList;
import java.util.Collection;

import pcgen.PCGenTestCase;
import pcgen.cdom.reference.CDOMDirectSingleRef;
import pcgen.cdom.reference.CDOMSingleRef;
import pcgen.core.Ability;
import pcgen.core.AbilityCategory;
import pcgen.core.Campaign;
import pcgen.rules.context.ConsolidatedListCommitStrategy;
import pcgen.rules.context.LoadValidator;
import pcgen.rules.context.RuntimeLoadContext;
import pcgen.rules.context.RuntimeReferenceContext;


/**
 * The Class <code>AbilityListTokenTest</code> verifies the processing of the 
 * AbilityListToken.
 * 
 * Last Editor: $Author: $
 * Last Edited: $Date:  $
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision:  $
 */
public class AbilityListTokenTest extends PCGenTestCase
{

	private RuntimeLoadContext context;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		context = new RuntimeLoadContext(new RuntimeReferenceContext(),
				new ConsolidatedListCommitStrategy());
		context.ref.importObject(AbilityCategory.FEAT);
	}

	private Ability buildFeat(RuntimeLoadContext context, String abName)
	{
		Ability ab = context.ref.constructCDOMObject(Ability.class, abName);
		context.ref.reassociateCategory(AbilityCategory.FEAT, ab);
		return ab;
	}
	
	private void assertContains(AbilityCategory cat, Ability ab, boolean expected)
	{
		String key = ab.getKeyName();
		Collection<CDOMSingleRef<Ability>> refs = cat.getAbilityRefs();
		boolean found = false;
		for (CDOMSingleRef<Ability> ref : refs)
		{
			found |= ref.getLSTformat(false).equals(key);
		}
		assertEquals(key + " in the list (" + expected + ") incorrect",
				expected, found);
	}
	
	/**
	 * Test a single entry is parsed correctly
	 */
	public void testSingleEntry()
	{
		AbilityCategory aCat = context.ref.constructCDOMObject(
				AbilityCategory.class, "TestCat");
		aCat.setAbilityCategory(CDOMDirectSingleRef.getRef(AbilityCategory.FEAT));
		assertFalse("Test category should start with an empty list of keys",
			aCat.hasDirectReferences());
		assertEquals("Test category should start with an empty list of keys",
			0, aCat.getAbilityRefs().size());

		AbilityListToken token = new AbilityListToken();
		Ability track = buildFeat(context, "Track");
		token.parseToken(context, aCat, "Track");
		assertEquals("Test category should now have 1 key", 1, aCat
				.getAbilityRefs().size());
		assertContains(aCat, track, true);
	}

	/**
	 * Test that multiple entries are parsed correctly.
	 */
	public void testMultipleEntries()
	{
		AbilityCategory aCat = context.ref.constructCDOMObject(
				AbilityCategory.class, "TestCat");
		aCat.setAbilityCategory(CDOMDirectSingleRef.getRef(AbilityCategory.FEAT));
		assertFalse("Test category should start with an empty list of keys",
			aCat.hasDirectReferences());
		assertEquals("Test category should start with an empty list of keys",
			0, aCat.getAbilityRefs().size());

		AbilityListToken token = new AbilityListToken();
		Ability track = buildFeat(context, "Track");
		Ability pbs = buildFeat(context, "Point Blank Shot");
		Ability pa = buildFeat(context, "Power Attack");
		token.parseToken(context, aCat, "Track|Point Blank Shot");
		assertEquals("Test category should now have 2 keys", 2, aCat
			.getAbilityRefs().size());
		assertContains(aCat, track, true);
		assertContains(aCat, pbs, true);
		assertContains(aCat, pa, false);
	}
	
	/**
	 * Test that entries with associated choices are parsed correctly
	 */
	public void testEntriesWithAssoc()
	{
		AbilityCategory aCat = context.ref.constructCDOMObject(
				AbilityCategory.class, "TestCat");
		aCat.setAbilityCategory(CDOMDirectSingleRef.getRef(AbilityCategory.FEAT));
		assertFalse("Test category should start with an empty list of keys",
			aCat.hasDirectReferences());
		assertEquals("Test category should start with an empty list of keys",
			0, aCat.getAbilityRefs().size());

		AbilityListToken token = new AbilityListToken();
		Ability pbs = buildFeat(context, "Point Blank Shot");
		Ability sf = buildFeat(context, "Skill Focus");
		token.parseToken(context, aCat, "Point Blank Shot|Skill Focus (Ride)|Skill Focus (Bluff)");
		assertEquals("Test category should now have 3 keys", 3, aCat
			.getAbilityRefs().size());
		assertContains(aCat, pbs, true);
		assertContains(aCat, sf, false); //Because this tests LST format
		context.ref.validate(new LoadValidator(new ArrayList<Campaign>()));
		assertTrue(context.ref.resolveReferences(null));
		Collection<CDOMSingleRef<Ability>> refs = aCat.getAbilityRefs();
		boolean found = false;
		for (CDOMSingleRef<Ability> ref : refs)
		{
			found |= ref.contains(pbs);
		}
		assertTrue("Expected Point Blank Shot Ability", found);
		found = false;
		for (CDOMSingleRef<Ability> ref : refs)
		{
			found |= ref.contains(sf);
		}
		assertTrue("Expected Skill Focus Ability", found);
	}
}