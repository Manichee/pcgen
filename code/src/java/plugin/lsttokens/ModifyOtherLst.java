/*
 * Copyright 2014 (C) Thomas Parker <thpr@users.sourceforge.net>
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package plugin.lsttokens;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pcgen.base.calculation.Modifier;
import pcgen.base.formula.base.LegalScope;
import pcgen.base.formula.base.ScopeInstance;
import pcgen.base.formula.base.VarScoped;
import pcgen.base.text.ParsingSeparator;
import pcgen.base.util.FormatManager;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.Constants;
import pcgen.cdom.base.ObjectGrouping;
import pcgen.cdom.content.RemoteModifier;
import pcgen.cdom.content.VarModifier;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.Campaign;
import pcgen.rules.context.Changes;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractTokenWithSeparator;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.rules.persistence.token.ParseResult;
import pcgen.util.Logging;

public class ModifyOtherLst extends AbstractTokenWithSeparator<CDOMObject>
		implements CDOMPrimaryToken<CDOMObject>
{

	@Override
	public String getTokenName()
	{
		return "MODIFYOTHER";
	}

	@Override
	protected char separator()
	{
		return '|';
	}

	//MODIFYOTHER:EQUIPMENT|GROUP=Martial|EqCritRange|ADD|1
	@Override
	protected ParseResult parseTokenWithSeparator(LoadContext context,
		CDOMObject obj, String value)
	{
		if (obj instanceof Campaign)
		{
			return new ParseResult.Fail(getTokenName()
				+ " may not be used in Campaign Files.  "
				+ "Please use the Global Modifier file", context);
		}
		ParsingSeparator sep = new ParsingSeparator(value, '|');
		sep.addGroupingPair('[', ']');
		sep.addGroupingPair('(', ')');

		String scopeName = sep.next();
		/*
		 * Note lvs is implicitly defined as "not global" since the global scope
		 * is "" and thus would have failed the tests imposed by
		 * AbstractTokenWithSeparator
		 */
		final LegalScope lvs = context.getVariableContext().getScope(scopeName);
		if (lvs == null)
		{
			return new ParseResult.Fail(getTokenName()
				+ " found illegal variable scope: " + scopeName
				+ " as first argument: " + value, context);
		}
		if (!sep.hasNext())
		{
			return new ParseResult.Fail(getTokenName()
				+ " needed 2nd argument: " + value, context);
		}
		LoadContext subContext = context.dropIntoContext(lvs.getName());
		return continueParsing(subContext, obj, value, sep);
	}

	private <GT extends VarScoped> ParseResult continueParsing(
		LoadContext context, CDOMObject obj, String value, ParsingSeparator sep)
	{
		ScopeInstance scopeInst = context.getActiveScope();
		@SuppressWarnings("unchecked")
		final LegalScope scope = scopeInst.getLegalScope();
		final String groupingName = sep.next();
		ObjectGrouping group;
		if (groupingName.startsWith("GROUP="))
		{
			final String groupName = groupingName.substring(6);

			group = new ObjectGrouping()
			{
				@Override
				public boolean contains(VarScoped item)
				{
					return (item instanceof CDOMObject)
						&& ((CDOMObject) item).containsInList(ListKey.GROUP,
							groupName);
				}

				@Override
				public LegalScope getScope()
				{
					return scope;
				}

				@Override
				public String getIdentifier()
				{
					return "GROUP=" + groupName;
				}
			};
		}
		else if ("ALL".equals(groupingName))
		{
			group = new ObjectGrouping()
			{
				@Override
				public boolean contains(VarScoped cdo)
				{
					return true;
				}

				@Override
				public LegalScope getScope()
				{
					return scope;
				}

				@Override
				public String getIdentifier()
				{
					return "ALL";
				}
			};
		}
		else
		{
			group = new ObjectGrouping()
			{
				@Override
				public boolean contains(VarScoped cdo)
				{
					return cdo.getKeyName().equalsIgnoreCase(groupingName);
				}

				@Override
				public LegalScope getScope()
				{
					return scope;
				}

				@Override
				public String getIdentifier()
				{
					return groupingName;
				}
			};
		}

		if (!sep.hasNext())
		{
			return new ParseResult.Fail(getTokenName()
				+ " needed 3rd argument: " + value, context);
		}
		String varName = sep.next();
		if (!context.getVariableContext().isLegalVariableID(scope, varName))
		{
			return new ParseResult.Fail(getTokenName()
				+ " found invalid var name: " + varName + " Modified on "
				+ obj.getClass().getSimpleName() + " " + obj.getKeyName(),
				context);
		}
		if (!sep.hasNext())
		{
			return new ParseResult.Fail(getTokenName()
				+ " needed 4th argument: " + value, context);
		}
		String modType = sep.next();
		if (!sep.hasNext())
		{
			return new ParseResult.Fail(getTokenName()
				+ " needed 5th argument: " + value, context);
		}
		String modValue = sep.next();
		int priorityNumber = 0; //Defaults to zero
		if (sep.hasNext())
		{
			String priority = sep.next();
			if (priority.length() < 10)
			{
				return new ParseResult.Fail(getTokenName()
					+ " was expecting PRIORITY= but got " + priority + " in "
					+ value, context);
			}
			if ("PRIORITY=".equalsIgnoreCase(priority.substring(0, 9)))
			{
				try
				{
					priorityNumber = Integer.parseInt(priority.substring(9));
				}
				catch (NumberFormatException e)
				{
					return new ParseResult.Fail(getTokenName()
						+ " requires Priority to be an integer: "
						+ priority.substring(9) + " was not an integer");
				}
				if (priorityNumber < 0)
				{
					return new ParseResult.Fail(getTokenName()
						+ " Priority requires an integer >= 0. "
						+ priorityNumber + " was not positive");
				}
			}
			else
			{
				return new ParseResult.Fail(getTokenName()
					+ " was expecting PRIORITY=x but got " + priority + " in "
					+ value, context);
			}
			if (sep.hasNext())
			{
				return new ParseResult.Fail(getTokenName()
					+ " had too many arguments: " + value, context);
			}
		}
		Modifier<?> modifier;
		try
		{
			FormatManager<?> format =
					context.getVariableContext().getVariableFormat(scope,
						varName);
			modifier =
					context.getVariableContext().getModifier(modType, modValue,
						priorityNumber, scope, format);
		}
		catch (IllegalArgumentException iae)
		{
			return new ParseResult.Fail(getTokenName() + " Modifier " + modType
				+ " had value " + modValue + " but it was not valid: "
				+ iae.getMessage(), context);
		}
		VarModifier<?> vm = new VarModifier<>(varName, scope, modifier);
		RemoteModifier<?> rm = new RemoteModifier<>(group, vm);
		context.getObjectContext().addToList(obj, ListKey.REMOTE_MODIFIER, rm);
		return ParseResult.SUCCESS;
	}

	@Override
	public String[] unparse(LoadContext context, CDOMObject obj)
	{
		Changes<RemoteModifier<?>> changes =
				context.getObjectContext().getListChanges(obj,
					ListKey.REMOTE_MODIFIER);
		if (changes.hasRemovedItems())
		{
			Logging.errorPrint(getTokenName()
				+ " does not support removed items");
			return null;
		}
		if (changes.includesGlobalClear())
		{
			Logging.errorPrint(getTokenName() + " does not support .CLEAR");
			return null;
		}
		Collection<RemoteModifier<?>> added = changes.getAdded();
		List<String> modifiers = new ArrayList<String>();
		if (added != null && added.size() > 0)
		{
			for (RemoteModifier<?> rm : added)
			{
				VarModifier<?> vm = rm.getVarModifier();
				StringBuilder sb = new StringBuilder();
				ObjectGrouping og = rm.getGrouping();
				sb.append(og.getScope().getName());
				sb.append(Constants.PIPE);
				sb.append(og.getIdentifier());
				sb.append(Constants.PIPE);
				sb.append(vm.varName);
				sb.append(Constants.PIPE);
				sb.append(unparseModifier(vm));
				modifiers.add(sb.toString());
			}
		}
		if (modifiers.isEmpty())
		{
			//Legal
			return null;
		}
		return modifiers.toArray(new String[modifiers.size()]);
	}

	private String unparseModifier(VarModifier<?> vm)
	{
		Modifier<?> modifier = vm.modifier;
		String type = modifier.getIdentification();
		int userPriority = modifier.getUserPriority();
		StringBuilder sb = new StringBuilder();
		sb.append(type);
		sb.append(Constants.PIPE);
		sb.append(modifier.getInstructions());
		if (userPriority > 0)
		{
			sb.append(Constants.PIPE);
			sb.append("PRIORITY=");
			sb.append(userPriority);
		}
		return sb.toString();
	}

	@Override
	public Class<CDOMObject> getTokenClass()
	{
		return CDOMObject.class;
	}

}
