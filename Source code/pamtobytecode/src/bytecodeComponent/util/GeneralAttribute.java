package bytecodeComponent.util;


public class GeneralAttribute implements IAttribute {

	protected int NameIndex;
	protected String Information;
	
	public GeneralAttribute() { }
	public GeneralAttribute(int NameIndex, String Information)
	{
		this.NameIndex = NameIndex;
		this.Information = Information;
	}
	public void setNameIndex(int NameIndex) 
	{
		this.NameIndex = NameIndex;
	}
	public String toString()
	{
		String str = "";
		
		int AttributeLenth = Information.length() / 2;
		
		str += Util.intToHex((NameIndex), 4);
		str += Util.intToHex((AttributeLenth), 8);
		str += Information;
		
		return str.toUpperCase();
	}

	public void setInformation(String Information) 
	{
		this.Information = Information;
	}
}
