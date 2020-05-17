package com.Bytecode;

import com.Bytecode.util.util;

public class GeneralInformation {

	private final String MagicNumber = "CAFEBABE";
	private final int MinorVersion = 0;
	private final int MajorVersion = 51;
	private final String AccesFlag = "0021";
	private int ThisClassIndex;
	private int SuperClassIndex;
	private final int InterfacesCount = 0;
	private final int FieldsCount = 0;
	private final int MethodsCount = 2;
	private final int AttributesCount = 1;
	
	public void setThisClassIndex(int ClassIndex)
	{
		ThisClassIndex = ClassIndex;
	}
	public void setSuperClassIndex(int ClassIndex)
	{
		SuperClassIndex = ClassIndex;
	}
	public String getFileHeader()
	{
		String str = "";
		
		str += MagicNumber;
		str += util.intToHex((MinorVersion), 4);
		str += util.intToHex((MajorVersion), 4);
		
		return str;
	}
	public String getClassHeader()
	{
		String str = "";
		
		str += AccesFlag;
		str += util.intToHex((ThisClassIndex), 4);
		str += util.intToHex((SuperClassIndex), 4);
		str += util.intToHex((InterfacesCount), 4);
		str += util.intToHex((FieldsCount), 4);
		str += util.intToHex((MethodsCount), 4);
		
		return str;
	}
	public String getAttributeHeader()
	{
		return util.intToHex((AttributesCount), 4);	
	}
}
