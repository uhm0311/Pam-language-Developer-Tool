package bytecodeComponent.util;

public class Util {
	public static boolean isVariable(String str)
	{
		String Alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		for(int i = 0; i < Alphabet.length(); i++)
		{
			Character ch = new Character(Alphabet.charAt((i)));
			if(str.contains(ch.toString())) //¾ËÆÄºªÀ» Æ÷ÇÔÇÏ¸é º¯¼ö·Î Ãë±Þ
				return true;
		}
		return false;
	}
	
	public static boolean isConstant(String str)
	{
		String Number = "-1234567890";
	
		for(int i = 0; i < str.length(); i++)
		{
			Character ch = new Character(str.charAt(i));
			if(!Number.contains(ch.toString()))
				return false;
		}
		return true;
	}
	
	public static boolean isInput(String str)
	{
		final String Input1 = "[32";
		
		return str.startsWith(Input1);
	}
	
	public static boolean isOutput(String str)
	{
		final String Output1 = "[33";
		
		return str.startsWith(Output1);
	}
	
	public static boolean isAdditive(String str)
	{
		final String Additive1 = "[106";
		
		return str.startsWith(Additive1);
	}
	
	public static boolean isMultiplicative(String str)
	{
		final String Multiplicative1 = "[105"; //°ö¼À¸¸ ÀÖ´Â °æ¿ì
		final String Multiplicative2 = "[120"; //µ¡¼À ¾Æ·¡ÀÇ °ö¼ÀÀÌ ÇÏ³ªÀÎ °æ¿ì
		final String Multiplicative3 = "[128"; //µ¡¼À ¾Æ·¡ÀÇ ¿ÞÂÊ °ö¼À
		final String Multiplicative4 = "[131"; //µ¡¼À ¾Æ·¡ÀÇ ¿À¸¥ÂÊ °ö¼À
		
		return str.startsWith(Multiplicative1) || str.startsWith(Multiplicative2) || str.startsWith(Multiplicative3) || str.startsWith(Multiplicative4);
	}
	
	public static boolean isElement(String str)
	{
		final String Element1 = "[104"; //´Üµ¶ÀÎ °æ¿ì
		final String Element2 = "[109"; //µ¡¼ÀÀÇ ¿ÞÂÊÀÎ °æ¿ì
		final String Element3 = "[111"; //µ¡¼ÀÀÇ ¿À¸¥ÂÊÀÎ °æ¿ì
		final String Element4 = "[117"; //°ö¼ÀÀÌ Æ÷ÇÔµÈ µ¡¼ÀÀÇ ¿ÞÂÊÀÎ °æ¿ì
		final String Element5 = "[119"; //°ö¼ÀÀÌ Æ÷ÇÔµÈ µ¡¼ÀÀÇ ¿À¸¥ÂÊÀÎ °æ¿ì
		final String Element6 = "[141"; //°ö¼ÀÀÇ ¿ÞÂÊÀÎ °æ¿ì
		final String Element7 = "[143"; //°ö¼ÀÀÇ ¿À¸¥ÂÊÀÎ °æ¿ì
		
		return str.startsWith(Element1) || str.startsWith(Element2) || str.startsWith(Element3) || str.startsWith(Element4) || str.startsWith(Element5) || str.startsWith(Element6) || str.startsWith(Element7);
	}
	
	public static boolean isStatements(String str)
	{
		final String Statements1 = "[30"; //¸Ç Ã³À½
		final String Statements2 = "[76"; //if¹®
		final String Statements3 = "[89"; //to do ¿·¿¡
		final String Statements4 = "[95"; //while do ¿·¿¡
		
		return str.startsWith(Statements1) || str.startsWith(Statements2) || str.startsWith(Statements3) || str.startsWith(Statements4);
	}
	
	public static boolean isConditional(String str)
	{
		final String If1 = "[37";
		
		return str.startsWith(If1);
	}
	
	public static boolean isDefiniteLoop(String str)
	{
		final String Loop1 = "[34";
		
		return str.startsWith(Loop1);
	}
	
	public static boolean isInfiniteLoop(String str)
	{
		final String Loop1 = "[35";
		
		return str.startsWith(Loop1);
	}
	
	public static boolean isAssign(String str)
	{
		final String Assign1 = "[36";
		
		return str.startsWith(Assign1);
	}
	
	public static String byteArrayToHex(byte[] ba) 
	{
		if (ba == null || ba.length == 0)
	        return null;
	 
	    StringBuffer sb = new StringBuffer(ba.length * 2);
	    String hexNumber;
	    for (int x = 0; x < ba.length; x++) 
	    {
	        hexNumber = "0" + Integer.toHexString(0xff & ba[x]);
	        sb.append(hexNumber.substring(hexNumber.length() - 2));
	    }
	    return sb.toString().toUpperCase();
	} 
	
	public static byte[] hexToByteArray(String hex) 
	{
	    if (hex == null || hex.length() == 0) 
	        return null;
	 
	    byte[] ba = new byte[hex.length() / 2];
	    for (int i = 0; i < ba.length; i++) 
	        ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
	    
	    return ba;
	}
	
	public static String intToHex(int value, int length)
	{
		if(value >= 0)
			return PositiveIntToHex(value, length);
		else return NegativeIntToHex(value, length);
	}
	
	private static String PositiveIntToHex(int value, int length)
	{
		return StretchLenth(Integer.toHexString(value), length);
	}
	
	private static String NegativeIntToHex(int value, int length)
	{
		return Long.toHexString(value).substring(16 - length, 16);
	}
	
	public static String strToHex(String str)
	{
		return byteArrayToHex(str.getBytes());
	}
	
	public static String StretchLenth(String str, int lenth)
	{
		while(str.length() < lenth)
			str = "0" + str;
		
		return str;
	}
	public static Object[] Sort(Object[] Array, boolean isAscending)
	{
		Object[] objArray = Array;
		
		for(int i = 1; i < objArray.length; i++)
		{
			for(int j = 0; j < objArray.length; j++)
			{
				try
				{
					if(((int)objArray[i] < (int)objArray[j]) == isAscending)
					{
						Object temp = objArray[i];
						objArray[i] = objArray[j];
						objArray[j] = temp;
					}
				}
				catch (Exception e)
				{
					if((String.valueOf(objArray[i]).compareTo(String.valueOf(objArray[j])) > 0) == isAscending)
					{
						Object temp = objArray[i];
						objArray[i] = objArray[j];
						objArray[j] = temp;
					}
				}
			}
		}
		
		return objArray;
	}
}
