package bytecodeComponent.util;

import bytecodeComponent.ConstantPool;

public class StackMapFrame {

	private int Offset;
	private int OffsetDelta = 0;
	private int Type;
	private String Information = "";
	private int ScannerClassIndex;
	private boolean isFirst;
	private int Locals;
	private ConstantPool CP;
	private boolean hasInputStatement = false;
	
	public StackMapFrame(boolean isFirst, int ScannerClassIndex, int Offset, int Locals, ConstantPool CP) throws Exception
	{
		this.CP = CP;
		this.isFirst = isFirst;
		this.Offset = Offset;
		this.Locals = Locals;
		this.ScannerClassIndex = ScannerClassIndex;
	}
	public String toString()
	{
		String str = "";
		int Type = this.Type;
		
		if(OffsetDelta == 0)
			OffsetDelta = Offset;
		
		if(Type < StackMapType.Full)
			str = Util.intToHex((Type), 2);
		
		if(Type > StackMapType.SameMax)
		{
			str += Util.intToHex((OffsetDelta), 4);
			
			if(Type >= StackMapType.Full)
			{
				/* Full의 구조
					u1 frame_type = FULL_FRAME = 255 1바이트
				    u2 offset_delta; 2바이트
				    u2 number_of_locals; 2바이트
				    verification_type_info locals[number_of_locals];
				    u2 number_of_stack_items; 1바이트
				    verification_type_info stack[number_of_stack_items]; 1바이트
				 */
				int StringArrayClassIndex = CP.IndexofClass("[Ljava/lang/String;");
				if(StringArrayClassIndex == -1)
				{
					StringArrayClassIndex = CP.size() + 1;
					CP.put(StringArrayClassIndex, Constant.Class(StringArrayClassIndex + 1));
					CP.put(StringArrayClassIndex + 1, Constant.UTF8("[Ljava/lang/String;"));
				}
				Type = StackMapType.Full;
				
				str = Util.intToHex((Type), 2) + str;
				str += Util.intToHex((2 + Information.length() / 2), 4);
				str += TypeInfo.getCode(TypeInfo.ClassObject, StringArrayClassIndex);
				if(hasInputStatement == true)
					str += TypeInfo.getCode(TypeInfo.ClassObject, ScannerClassIndex);
				str += Information;
				str += Util.StretchLenth("0", 4);
			}
			else if(Type >= StackMapType.Append)
			{
				if(isFirst == true && hasInputStatement == true)
					str += TypeInfo.getCode(TypeInfo.ClassObject, ScannerClassIndex);
				str += Information;
			}
		}
		
		return str.toUpperCase();
	}
	
	public int getOffset()
	{
		return this.Offset;
	}
	
	public void setOffset(int Offset)
	{
		this.Offset = Offset;
	}
	public int getLocals()
	{
		return Locals;
	}
	public void setLocals(int Locals)
	{
		this.Locals = Locals;
	}
	
	public int getType()
	{
		return Type;
	}
	public void setType(int Type)
	{
		this.Type = Type;
	}
	public void setOffsetDelta(int OffsetDelta)
	{
		this.OffsetDelta = OffsetDelta;
	}
	public boolean isFirst()
	{
		return isFirst;
	}
	public void isFirst(boolean isFirst)
	{
		this.isFirst = isFirst;
	}
	public void setInformation(String Information)
	{
		this.Information = Information;
	}
	public void hasInputStatement(boolean value)
	{
		hasInputStatement = value;
	}
		
	public static class StackMapType
	{
		public static final int Same = 0;
		public static final int SameMax = 63;
		public static final int Chop = 248;
		public static final int ChopMax = 251;
		public static final int Append = 252;
		public static final int AppendMax = 254;
		public static final int Full = 255;
	}
	
	public static class TypeInfo
	{
		public static final int Integer = 1;
		public static final int ClassObject = 7;
		
		public static String getCode(int Type)
		{
			String str = "";
			
			if(Type == Integer)
				str += Util.intToHex((Integer), 2);
			
			return str;
		}
		public static String getCode(int Type, int ScannerClassIndex)
		{
			String str = "";
			
			if(Type == ClassObject)
			{
				str += Util.intToHex((ClassObject), 2);
				str += Util.intToHex((ScannerClassIndex), 4);
			}
			
			return str;
		}
	}
}
