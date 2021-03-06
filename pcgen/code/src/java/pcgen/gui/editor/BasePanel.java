/*
 * BasePanel.java
 * Copyright 2002 (C) Greg Bingleman <byngl@hotmail.com>
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
 * Created on November 5, 2002, 4:29 PM
 *
 * @(#) $Id: BasePanel.java,v 1.23 2005/10/18 20:23:42 binkley Exp $
 */
package pcgen.gui.editor;

import pcgen.core.PObject;

import javax.swing.JPanel;

/**
 * <code>BasePanel</code>
 *
 * @author  Greg Bingleman <byngl@hotmail.com>
 * @version $Revision: 1.23 $
 */
abstract class BasePanel extends JPanel implements PObjectUpdater
{
	public abstract void updateData(PObject thisPObject);
	public abstract void updateView(PObject thisPObject);
}
