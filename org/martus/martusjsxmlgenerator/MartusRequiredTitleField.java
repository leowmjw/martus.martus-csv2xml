/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.martus.martusjsxmlgenerator;

public class MartusRequiredTitleField extends MartusField 
{
	public MartusRequiredTitleField()
	{
		super();
	}
	public MartusRequiredTitleField(Object valueToUse)
	{
		super(TITLE_TAG, "", valueToUse);
		requiredFieldTitle = true;
	}
	
	public String getType()
	{
		return STRING_TYPE;
	}
	
	public String getClassName()
	{
		return "MartusRequiredTitleField";
	}
	
	public static final String TITLE_TAG = "title";
}
