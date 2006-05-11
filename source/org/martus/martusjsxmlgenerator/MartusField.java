/*

 The Martus(tm) free, social justice documentation and
 monitoring software. Copyright (C) 2005-2006, Beneficent
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
import org.martus.common.bulletin.Bulletin;
import org.martus.common.fieldspec.FieldSpec;
import org.martus.common.fieldspec.FieldTypeBoolean;
import org.martus.common.fieldspec.FieldTypeDate;
import org.martus.common.fieldspec.FieldTypeDateRange;
import org.martus.common.fieldspec.FieldTypeDropdown;
import org.martus.common.fieldspec.FieldTypeGrid;
import org.martus.common.fieldspec.FieldTypeLanguage;
import org.martus.common.fieldspec.FieldTypeMessage;
import org.martus.common.fieldspec.FieldTypeMultiline;
import org.martus.common.fieldspec.FieldTypeNormal;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;


abstract public class MartusField extends ScriptableObject
{
	
	abstract public String getType();
	
	public MartusField()
	{
	}

	public MartusField(String tagToUse, String labelToUse, Object valueToUse)
	{
		tag = tagToUse;
		label = labelToUse;
		value = valueToUse;
	}
	
	public String getLabel()
	{
		return label;
	}

	public String getTag()
	{
		return tag;
	}
	
	public Object getValue()
	{
		return value;
	}
	
	public String jsGet_getLabel()
	{
		return label;
	}
	
	public String jsGet_getTag()
	{
		return tag;
	}

	public Object jsGet_getValue()
	{
		return value;
	}
	
	public String getMartusValue( Scriptable scriptable ) throws Exception
	{
		if ( getValue() instanceof String ) 
		{
			return scriptable.get( (String)getValue(), scriptable ).toString();
		}
		
		if ( getValue() instanceof Function ) 
		{

			Function function = (Function)getValue();
			
			return function.call(
						Context.getCurrentContext(),
						scriptable, scriptable,
						null
						).toString();
		}
		throw new RuntimeException( "getMartusValue::Illegal value type" );
	}
	
	public String getFieldSpec(Scriptable scriptable) throws Exception
	{
		StringBuffer xmlFieldSpec = new StringBuffer();
		xmlFieldSpec.append(getFieldSpecTypeStartTag(getType()));
		xmlFieldSpec.append(getXMLData(FieldSpec.FIELD_SPEC_TAG_XML_TAG, getTag()));
		xmlFieldSpec.append(getXMLData(FieldSpec.FIELD_SPEC_LABEL_XML_TAG, getLabel()));
		xmlFieldSpec.append(getFieldSpecSpecificXmlData(scriptable));
		xmlFieldSpec.append(getEndTag(FieldSpec.FIELD_SPEC_XML_TAG));
		return xmlFieldSpec.toString();
	}
	
	public String getFieldData(Scriptable scriptable) throws Exception
	{
		StringBuffer xmlFieldData = new StringBuffer();
		xmlFieldData.append(getFieldTagStartTag(getTag()));
		xmlFieldData.append(getXmlFieldValue(scriptable));
		xmlFieldData.append(getEndTagWithExtraNewLine(BulletinXmlConstants.FIELD));
		return xmlFieldData.toString();
	}

	public String getXmlFieldValue(Scriptable scriptable) throws Exception
	{
		return getXMLData(BulletinXmlConstants.VALUE, getMartusValue( scriptable ));
	}
	
	public String getFieldSpecSpecificXmlData(Scriptable scriptable)  throws Exception
	{
		return "";
	}
	
	static public String getFieldTagStartTag(String tag)
	{
		return getStartTagNewLine(BulletinXmlConstants.FIELD +" "+BulletinXmlConstants.TAG_ATTRIBUTE+"='"+tag+"'");
	}

	static public String getXMLData(String xmlTag, String data)
	{
		StringBuffer xmlData = new StringBuffer(getStartTag(xmlTag));
		xmlData.append(data);
		xmlData.append(getEndTag(xmlTag));
		return xmlData.toString();
	}
	
	static public String getStartTag(String text)
	{
		return ("<" + text + ">");
	}

	static public String getStartTagNewLine(String text)
	{
		return getStartTag(text) + BulletinXmlConstants.NEW_LINE;
	}

	static public String getEndTag(String text)
	{
		return getStartTagNewLine("/" + text);
	}
	
	static public String getEndTagWithExtraNewLine(String text)
	{
		return getEndTag(text) + BulletinXmlConstants.NEW_LINE;
	}

	static public String getFieldSpecTypeStartTag(String type)
	{
		return getStartTagNewLine(FieldSpec.FIELD_SPEC_XML_TAG +" "+FieldSpec.FIELD_SPEC_TYPE_ATTR+"='"+type+"'");
	}
	
	static public String getPrivateFieldSpec()
	{
		StringBuffer privateSpec = new StringBuffer(getStartTagNewLine(BulletinXmlConstants.PRIVATE_FIELD_SPECS));
		privateSpec.append(getFieldSpecTypeStartTag(MULTILINE_TYPE));
		privateSpec.append(getXMLData(FieldSpec.FIELD_SPEC_TAG_XML_TAG, Bulletin.TAGPRIVATEINFO));
		privateSpec.append(getXMLData(FieldSpec.FIELD_SPEC_LABEL_XML_TAG, ""));
		privateSpec.append(getEndTag(FieldSpec.FIELD_SPEC_XML_TAG));
		privateSpec.append(getEndTagWithExtraNewLine(BulletinXmlConstants.PRIVATE_FIELD_SPECS));
		return privateSpec.toString();
	}
	
	public boolean isMartusDefaultField()
	{
		return false;
	}
	
	public void cleanup()
	{
	}
	
	
	static public void clearRequiredFields()
	{
		requiredFieldLanguage = false;
		requiredFieldAuthor = false;
		requiredFieldTitle = false;
		requiredFieldEntryDate = false;
		requiredFieldPrivate = false;
	}
	
	static public void verifyRequiredFields() throws Exception
	{
		StringBuffer missingFieldsErrorMessage = new StringBuffer();
		if(!requiredFieldLanguage)
			missingFieldsErrorMessage.append("MartusRequiredLanguageField missing.  ");
		if(!requiredFieldAuthor)
			missingFieldsErrorMessage.append("MartusRequiredAuthorField missing.  ");
		if(!requiredFieldTitle)
			missingFieldsErrorMessage.append("MartusRequiredTitleField missing.  ");
		if(!requiredFieldEntryDate)
			missingFieldsErrorMessage.append("MartusRequiredDateEntryField missing.  ");
		if(!requiredFieldPrivate)
			missingFieldsErrorMessage.append("MartusRequiredPrivateField missing.  ");
		
		if (missingFieldsErrorMessage.toString().length() > 0)
			throw new Exception(missingFieldsErrorMessage.toString());
	}
	
	String tag;
	String label;
	Object value;
	static boolean requiredFieldLanguage;
	static boolean requiredFieldAuthor;
	static boolean requiredFieldTitle;
	static boolean requiredFieldEntryDate;
	static boolean requiredFieldPrivate;
	
	static public final String LANGUAGE_TYPE = FieldTypeLanguage.getTypeNameString();
	static public final String STRING_TYPE = FieldTypeNormal.getTypeNameString();
	static public final String MULTILINE_TYPE = FieldTypeMultiline.getTypeNameString();
	static public final String DATE_TYPE = FieldTypeDate.getTypeNameString();
	static public final String DATERANGE_TYPE = FieldTypeDateRange.getTypeNameString();
	static public final String DROPDOWN_TYPE = FieldTypeDropdown.getTypeNameString();
	static public final String BOOLEAN_TYPE = FieldTypeBoolean.getTypeNameString();
	static public final String MESSAGE_TYPE = FieldTypeMessage.getTypeNameString();
	static public final String GRID_TYPE = FieldTypeGrid.getTypeNameString();
	static public final String ATTACHMENT_TYPE = BulletinXmlConstants.ATTACHMENT;
	static public final String ATTACHMENT_SECTION_TOP = Bulletin.TOP_SECTION;
	static public final String ATTACHMENT_SECTION_BOTTOM = Bulletin.BOTTOM_SECTION;
}
