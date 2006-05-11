/*

The Martus(tm) free, social justice documentation and
monitoring software. Copyright (C) 2006, Beneficent
Technology, Inc. (Benetech).

Martus is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either
version 2 of the License, or (at your option) any later
version with the additions and exceptions described in the
accompanying Martus license file entitled "license.txt".

It is distributed WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, including warranties of fitness of purpose or
merchantability.  See the accompanying Martus License and
GPL license for more details on the required license terms
for this software.

You should have received a copy of the GNU General Public
License along with this program; if not, write to the Free
Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA 02111-1307, USA.

*/
package org.martus.martusjsxmlgenerator;

import org.martus.client.core.BulletinXmlConstants;

public class MartusBottomSectionAttachments extends MartusAttachments
{
	public MartusBottomSectionAttachments()
	{
	}
	
	public MartusBottomSectionAttachments(Object value, Object attachmentDelimeterToUse, Object attachmentDirectoryToUse)
	{
		super(value, attachmentDelimeterToUse, attachmentDirectoryToUse, ATTACHMENT_SECTION_BOTTOM);
	}

	public String getClassName()
	{
		return "MartusBottomSectionAttachments";
	}

	String getAttachmentListTag()
	{
		return BulletinXmlConstants.BOTTOM_SECTION_ATTACHMENT_LIST;
	}
	
}
