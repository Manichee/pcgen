/*
 * Copyright 2010 (C) Thomas Parker <thpr@users.sourceforge.net>
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
 */
package plugin.lsttokens.choose;

import java.util.List;
import java.util.StringTokenizer;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.CategorizedChooseInformation;
import pcgen.cdom.base.CategorizedPersistentChoiceActor;
import pcgen.cdom.base.Category;
import pcgen.cdom.base.ChooseInformation;
import pcgen.cdom.base.ChooseSelectionActor;
import pcgen.cdom.base.Constants;
import pcgen.cdom.base.PrimitiveChoiceSet;
import pcgen.cdom.base.PrimitiveCollection;
import pcgen.cdom.choiceset.CollectionToChoiceSet;
import pcgen.cdom.enumeration.AssociationListKey;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.reference.ReferenceManufacturer;
import pcgen.core.Ability;
import pcgen.core.AbilityCategory;
import pcgen.core.Globals;
import pcgen.core.PlayerCharacter;
import pcgen.core.SettingsHandler;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractTokenWithSeparator;
import pcgen.rules.persistence.token.CDOMSecondaryToken;
import pcgen.rules.persistence.token.ComplexParseResult;
import pcgen.rules.persistence.token.ParseResult;

/**
 * New chooser plugin, handles Abilities.
 */

public class AbilityToken extends AbstractTokenWithSeparator<CDOMObject>
		implements CDOMSecondaryToken<CDOMObject>,
		CategorizedPersistentChoiceActor<Ability>
{
	private static final Class<AbilityCategory> ABILITY_CATEGORY_CLASS =
			AbilityCategory.class;

	@Override
	public String getParentToken()
	{
		return "CHOOSE";
	}

	@Override
	protected char separator()
	{
		return '|';
	}

	protected ParseResult parseTokenWithSeparator(LoadContext context,
		ReferenceManufacturer<Ability> rm, Category<Ability> category,
		CDOMObject obj, String value)
	{
		int pipeLoc = value.lastIndexOf('|');
		String activeValue;
		String title;
		if (pipeLoc == -1)
		{
			activeValue = value;
			title = getDefaultTitle();
		}
		else
		{
			String titleString = value.substring(pipeLoc + 1);
			if (titleString.startsWith("TITLE="))
			{
				title = titleString.substring(6);
				if (title.startsWith("\""))
				{
					title = title.substring(1, title.length() - 1);
				}
				activeValue = value.substring(0, pipeLoc);
				if (title == null || title.length() == 0)
				{
					return new ParseResult.Fail(getParentToken() + ":"
						+ getTokenName() + " had TITLE= but no title: " + value, context);
				}
			}
			else
			{
				activeValue = value;
				title = getDefaultTitle();
			}
		}

		PrimitiveCollection<Ability> coll =
				context.getChoiceSet(rm, activeValue);
		if (coll == null)
		{
			return ParseResult.INTERNAL_ERROR;
		}
		if (!coll.getGroupingState().isValid())
		{
			ComplexParseResult cpr = new ComplexParseResult();
			cpr.addErrorMessage("Invalid combination of objects was used in: "
				+ activeValue);
			cpr.addErrorMessage("  Check that ALL is not combined");
			cpr.addErrorMessage("  Check that a key is not joined with AND (,)");
			return cpr;
		}
		PrimitiveChoiceSet<Ability> pcs =
				new CollectionToChoiceSet<Ability>(coll);
		CategorizedChooseInformation<Ability> tc =
				new CategorizedChooseInformation<Ability>(getTokenName(),
					category, pcs, Ability.class);
		tc.setTitle(title);
		tc.setChoiceActor(this);
		context.obj.put(obj, ObjectKey.CHOOSE_INFO, tc);
		return ParseResult.SUCCESS;
	}

	@Override
	public String[] unparse(LoadContext context, CDOMObject cdo)
	{
		ChooseInformation<?> tc =
				context.getObjectContext()
					.getObject(cdo, ObjectKey.CHOOSE_INFO);
		if (tc == null)
		{
			return null;
		}
		if (!tc.getName().equals(getTokenName()))
		{
			// Don't unparse anything that isn't owned by this SecondaryToken
			/*
			 * TODO Either this really needs to be a check against the subtoken
			 * (which thus needs to be stored in the ChooseInfo) or there needs
			 * to be a loadtime check that no more than once CHOOSE subtoken
			 * uses the same AssociationListKey... :P
			 */
			return null;
		}
		if (!tc.getGroupingState().isValid())
		{
			context.addWriteMessage("Invalid combination of objects"
				+ " was used in: " + getParentToken() + ":" + getTokenName());
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(((CategorizedChooseInformation<?>) tc).getCategory());
		sb.append('|');
		sb.append(tc.getLSTformat());
		String title = tc.getTitle();
		if (!title.equals(getDefaultTitle()))
		{
			sb.append("|TITLE=");
			sb.append(title);
		}
		return new String[]{sb.toString()};
	}

	@Override
	public void applyChoice(CDOMObject owner, Ability st, PlayerCharacter pc)
	{
		restoreChoice(pc, owner, st);
	}

	@Override
	public void removeChoice(PlayerCharacter pc, CDOMObject owner,
		Ability choice)
	{
		pc.removeAssoc(owner, getListKey(), choice);
		List<ChooseSelectionActor<?>> actors =
				owner.getListFor(ListKey.NEW_CHOOSE_ACTOR);
		if (actors != null)
		{
			for (ChooseSelectionActor ca : actors)
			{
				ca.removeChoice(owner, choice, pc);
			}
		}
		pc.removeAssociation(owner, encodeChoice(choice));
	}

	@Override
	public void restoreChoice(PlayerCharacter pc, CDOMObject owner,
		Ability choice)
	{
		pc.addAssoc(owner, getListKey(), choice);
		pc.addAssociation(owner, encodeChoice(choice));
		List<ChooseSelectionActor<?>> actors =
				owner.getListFor(ListKey.NEW_CHOOSE_ACTOR);
		if (actors != null)
		{
			for (ChooseSelectionActor ca : actors)
			{
				ca.applyChoice(owner, choice, pc);
			}
		}
	}

	@Override
	public List<Ability> getCurrentlySelected(CDOMObject owner,
		PlayerCharacter pc)
	{
		return pc.getAssocList(owner, getListKey());
	}

	@Override
	public boolean allow(Ability choice, PlayerCharacter pc, boolean allowStack)
	{
		/*
		 * This is universally true, as any filter for qualify, etc. was dealt
		 * with by the ChoiceSet built during parse
		 */
		return true;
	}

	private static final Class<Ability> ABILITY_CLASS = Ability.class;

	@Override
	public String getTokenName()
	{
		return "ABILITY";
	}

	@Override
	public ParseResult parseTokenWithSeparator(LoadContext context,
		CDOMObject obj, String value)
	{
		int barLoc = value.indexOf('|');
		if (barLoc == -1)
		{
			return new ParseResult.Fail("CHOOSE:" + getTokenName()
				+ " requires a CATEGORY and arguments : " + value, context);
		}
		String cat = value.substring(0, barLoc);
		Category<Ability> category =
				context.ref.silentlyGetConstructedCDOMObject(
					ABILITY_CATEGORY_CLASS, cat);
		if (category == null)
		{
			return new ParseResult.Fail("CHOOSE:" + getTokenName()
				+ " found invalid CATEGORY: " + cat + " in value: " + value,
				context);
		}
		String abilities = value.substring(barLoc + 1);
		return parseTokenWithSeparator(context,
			context.ref.getManufacturer(ABILITY_CLASS, category), category,
			obj, abilities);
	}

	@Override
	public Class<CDOMObject> getTokenClass()
	{
		return CDOMObject.class;
	}

	protected String getDefaultTitle()
	{
		return "Ability choice";
	}

	protected AssociationListKey<Ability> getListKey()
	{
		return AssociationListKey.getKeyFor(ABILITY_CLASS, "CHOOSE*ABILITY");
	}

	@Override
	public Ability decodeChoice(LoadContext context, String s)
	{
		StringTokenizer st = new StringTokenizer(s, Constants.PIPE);
		String catString = st.nextToken();
		if (!catString.startsWith("CATEGORY="))
		{
			throw new IllegalArgumentException(
				"String in AbilityToken.decodeChoice "
					+ "must start with CATEGORY=, found: " + s);
		}
		String cat = catString.substring(9);
		AbilityCategory ac = SettingsHandler.getGame().getAbilityCategory(cat);
		if (ac == null)
		{
			throw new IllegalArgumentException(
				"Category in AbilityToken.decodeChoice "
					+ "must exist found: " + cat);
		}
		String ab = st.nextToken();
		Ability a =
				Globals.getContext().ref.silentlyGetConstructedCDOMObject(
					Ability.class, ac, ab);
		if (a == null)
		{
			throw new IllegalArgumentException(
				"Second argument in String in AbilityToken.decodeChoice "
					+ "must be an Ability, but it was not found: " + s);
		}
		return a;
	}

	@Override
	public String encodeChoice(Ability choice)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(choice.getKeyName());
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ability decodeChoice(String encoded, Category<?> category)
	{
		String key;
		AbilityCategory abilityCat = (AbilityCategory) category;
		StringTokenizer st = new StringTokenizer(encoded, Constants.PIPE);
		if (st.countTokens() > 1)
		{
			String catString = st.nextToken();
			if (!catString.startsWith("CATEGORY="))
			{
				throw new IllegalArgumentException(
					"Ability cvhoice must be key name or CATEGORY=category|ability" +
					" found: " + encoded);
			}
			String cat = catString.substring(9);
			abilityCat = SettingsHandler.getGame().getAbilityCategory(cat);
			key = st.nextToken();
		}
		else
		{
			key = encoded;
		}
		Ability a =
				Globals.getContext().ref.silentlyGetConstructedCDOMObject(
					Ability.class, abilityCat, key);
		if (a == null)
		{
			throw new IllegalArgumentException(
				"String in decodeChoice "
					+ "must be an Ability, but it was not found: " + encoded);
		}
		return a;
	}

}