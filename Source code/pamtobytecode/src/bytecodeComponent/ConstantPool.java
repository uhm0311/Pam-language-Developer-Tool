package bytecodeComponent;

import java.util.HashMap;

import bytecodeComponent.util.Constant;
import bytecodeComponent.util.Util;

public class ConstantPool extends HashMap<Integer, Constant> {
	
	private static final long serialVersionUID = 2598532935491742445L;
	
	public ConstantPool() { }
	
	public String toHexString()
	{
		String str = Util.intToHex((this.size() + 1), 4); //CPcount
		
		for(int i = 1; i <= this.size(); i++)
			str += this.get(i).toHexString();
		
		return str;
	}
	public int IndexofMethodref(String ClassName, String Name, String Descriptor)
	{
		int ClassIndex = this.IndexofClass(ClassName);
		int NameAndTypeIndex = this.IndexofNameAndType(Name, Descriptor);
		
		return this.Indexof(Constant.Method(ClassIndex, NameAndTypeIndex));
	}
	public int IndexofClass(String ClassName)
	{
		int ClassNameIndex = this.Indexof(Constant.UTF8(ClassName));
		
		return this.Indexof(Constant.Class(ClassNameIndex));
	}
	public int IndexofNameAndType(String Name, String Descriptor)
	{
		int NameIndex = this.Indexof(Constant.UTF8(Name));
		int DescriptorIndex = this.Indexof(Constant.UTF8(Descriptor));
		
		return this.Indexof(Constant.NameAndType(NameIndex, DescriptorIndex));
	}
	public int IndexofUTF8(String Content)
	{
		return this.Indexof(Constant.UTF8(Content));
	}
	public int IndexofField(String ClassName, String Name, String Descriptor)
	{
		int ClassIndex = this.IndexofClass(ClassName);
		int NameAndTypeIndex = this.IndexofNameAndType(Name, Descriptor);
		
		return this.Indexof(Constant.Field(ClassIndex, NameAndTypeIndex));
	}
	public int IndexofString(String UTF8Content)
	{
		int UTF8Index = this.IndexofUTF8(UTF8Content);
		
		return this.Indexof(Constant.String(UTF8Index));
	}
	public int IndexofInteger(int Value)
	{
		return this.Indexof(Constant.Integer(Value));
	}
	private int Indexof(Constant Target)
	{
		for(int i = 1; i <= this.size(); i++)
		{
			if(this.get(i).equals(Target))
				return i;
		}
		return -1;
	}
	public void Insert(int TargetIndex, Constant Element)
	{
		int loop = this.size();
		
		for(int i = loop - 1; i >= TargetIndex; i--)
			this.put(i + 1, this.get(i));
		
		this.put(TargetIndex, Element);
	}
}
