package com.Bytecode.util;


public class Constant {

	public static final int UTF8 = 1;
	public static final int Integer = 3;
	public static final int Float = 4;
	public static final int Long = 5;
	public static final int Double = 6;
	public static final int Class = 7;
	public static final int String = 8;
	public static final int Fieldref = 9;
	public static final int Methodref = 10;
	public static final int InterfaceMethodref = 11;
	public static final int NameAndType = 12;
	public static final int MethodHandle = 15;
	public static final int MethodType = 16;
	public static final int InvokeDynamic = 18;
	
	private String Information = "";
	private int TagCode;
	
	private Constant(int TagCode)
	{
		this.TagCode = TagCode;
		Information += util.intToHex((TagCode), 2);
	}
	public int getTagCode()
	{
		return TagCode;
	}
	public String toHexString()
	{
		return Information;
	}
	public String toString()
	{
		if(TagCode == UTF8)
			return new String(util.hexToByteArray(Information.substring(7)));
		else return new String(util.hexToByteArray(Information.substring(3)));
	}
	public boolean equals(Object anObject)
	{
		Constant Target = (Constant)anObject;
		return (this.TagCode == Target.TagCode && this.Information.equals(Target.Information));
	}
	public static Constant UTF8(String Content)
	{
		Constant frame = new Constant(UTF8);
		
		frame.Information += util.intToHex((Content.length()), 4);
		frame.Information += util.strToHex(Content);
		
		return frame;
	}
	public static Constant Integer(int Value)
	{
		Constant frame = new Constant(Integer);
		
		frame.Information += util.intToHex((Value), 8);
		
		return frame;
	}
	public static Constant Class(int NameIndex)
	{
		Constant frame = new Constant(Class);
		
		frame.Information += util.intToHex((NameIndex), 4);
		
		return frame;
	}
	public static Constant String(int UTF8Index)
	{
		Constant frame = new Constant(String);
		
		frame.Information += util.intToHex((UTF8Index), 4);
		
		return frame;
	}
	public static Constant Field(int ClassIndex, int NameAndTypeIndex)
	{
		Constant frame = new Constant(Fieldref);
		
		frame.Information += util.intToHex((ClassIndex), 4);
		frame.Information += util.intToHex((NameAndTypeIndex), 4);
		
		return frame;
	}
	public static Constant Method(int ClassIndex, int NameAndTypeIndex)
	{
		Constant frame = new Constant(Methodref);
		
		frame.Information += util.intToHex((ClassIndex), 4);
		frame.Information += util.intToHex((NameAndTypeIndex), 4);
		
		return frame;
	}
	public static Constant NameAndType(int NameIndex, int DescriptorIndex)
	{
		Constant frame = new Constant(NameAndType);
		
		frame.Information += util.intToHex((NameIndex), 4);
		frame.Information += util.intToHex((DescriptorIndex), 4);
		
		return frame;
	}
}
