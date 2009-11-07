/*
 * Copyright (c) 2008 Tom Parker <thpr@users.sourceforge.net>
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */
package plugin.lsttokens.companionmod;

import pcgen.cdom.enumeration.StringKey;
import pcgen.core.character.CompanionMod;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractStringToken;
import pcgen.rules.persistence.token.CDOMPrimaryParserToken;

/**
 * Class deals with COPYMASTERCHECK Token
 */
public class CopymastercheckToken extends AbstractStringToken<CompanionMod> implements
		CDOMPrimaryParserToken<CompanionMod>
{

	@Override
	public String getTokenName()
	{
		return "COPYMASTERCHECK";
	}

	@Override
	protected StringKey stringKey()
	{
		return StringKey.MASTER_CHECK_FORMULA;
	}

	public String[] unparse(LoadContext context, CompanionMod cMod)
	{
		String masterCheck =
				context.getObjectContext().getString(cMod,
					StringKey.MASTER_CHECK_FORMULA);
		if (masterCheck == null)
		{
			return null;
		}
		return new String[]{masterCheck};
	}

	public Class<CompanionMod> getTokenClass()
	{
		return CompanionMod.class;
	}

}
