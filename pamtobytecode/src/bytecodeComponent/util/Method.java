package bytecodeComponent.util;

import org.antlr.v4.runtime.RuleContext;

import bytecodeComponent.Bytecode;
import bytecodeComponent.ConstantPool;
import bytecodeComponent.LineNumberTable;
import bytecodeComponent.StackMapTable;

public class Method {
	
	private String MethodInfo;
	private int CodeNameIndex;
	private String CodeInfo; //어트리뷰트 네임 인덱스 ~ 실제 코드 길이까지
	private String Code; //실제 코드 부분만
	private int ExceptionTableCount;
	private LineNumberTable LineNumTable;
	private StackMapTable StackMap;
	private int AttributeCount;
	private boolean hasStackMap;
	
	public Method(int CodeNameIndex, int LineNumberTableNameIndex)
	{
		LineNumTable = new LineNumberTable();
		StackMap = new StackMapTable(LineNumTable);
		MethodInfo = "";
		CodeInfo = "";
		Code = "";
		ExceptionTableCount = -1;
		this.CodeNameIndex = CodeNameIndex;
		AttributeCount = -1;
		
		LineNumTable.setNameIndex(LineNumberTableNameIndex);
	}
	public void createInitMethod(int MethodIndex, int MethodNameIndex, int MethodDescriptorIndex)
	{
		//init Method Info
		MethodInfo += Util.intToHex((MethodFlag.Public), 4); //public
		MethodInfo += Util.intToHex((MethodNameIndex), 4); //init 메소드의 이름의 인덱스
		MethodInfo += Util.intToHex((MethodDescriptorIndex), 4); //init 메소드의 인수와 반환타입의 인덱스
		MethodInfo += Util.intToHex((1), 4); //Code의 개수
		//init Method Code
		Code += Bytecode.Aload_0; //aload_0
		Code += Bytecode.Invokespecial(MethodIndex); //invokespecial init메소드 ref의 인덱스
		Code += Bytecode.Return; //return
		//init Method Exception Table
		ExceptionTableCount = 0; //안쓸것이므로 항상 0이다.
		//init Method Attribute Table
		AttributeCount = 1; //기본적으로 1이다.
		//init Method line number table attribute
		LineNumTable.add(new LineNumberFrame(0, 1));
		hasStackMap = false;
		
		//init Method Code Info
		CodeInfo = createCodeInfo(1, 1);
	}
	public void createMainMethod(RuleContext ParserTree, ConstantPool CP, int MethodNameIndex, int MethodDescriptorIndex, int InitMethodNameIndex) throws Exception
	{
		Object[] returnArray;
		int MaxStack;
		int MaxLocal;
		
		//main Method Info
		MethodInfo += Util.intToHex((MethodFlag.Public + MethodFlag.Static), 4); //public static
		MethodInfo += Util.intToHex((MethodNameIndex), 4);
		MethodInfo += Util.intToHex((MethodDescriptorIndex), 4);
		MethodInfo += Util.intToHex((1), 4); //Code의 개수
		
		returnArray = createMainCode(ParserTree, CP, InitMethodNameIndex);
		MaxStack = (int)returnArray[0];
		MaxLocal = (int)returnArray[1];
		hasStackMap = (boolean)returnArray[2];
		
		//main Method Exception Table
		ExceptionTableCount = 0; //안쓸것이므로 항상 0이다.
		//main Method Attribute Table
		if(hasStackMap == true)
			AttributeCount = 2;
		else AttributeCount = 1;
		
		CodeInfo = createCodeInfo(MaxStack, MaxLocal);
	}
	private String createCodeInfo(int MaxStack, int MaxLocal)
	{
		String str = "";
		int AttributeLenth = 0;
		int CodeLenth = 0;
		
		if(hasStackMap == true)
			AttributeLenth += StackMap.toString().length();
		
		CodeLenth = Code.length() / 2;
		AttributeLenth += (Util.intToHex((MaxStack), 4) + Util.intToHex((MaxLocal), 4) + Util.intToHex((CodeLenth), 8) + Code + Util.intToHex((ExceptionTableCount), 4) + Util.intToHex((AttributeCount), 4) + LineNumTable.toString()).length();
		
		AttributeLenth /= 2;

		//init Method Code Info
		str += Util.intToHex((CodeNameIndex), 4); //이름의 인덱스. Code
		str += Util.intToHex((AttributeLenth), 8); //길이
		str += Util.intToHex((MaxStack), 4); //max stack
		str += Util.intToHex((MaxLocal), 4); //max local
		str += Util.intToHex((CodeLenth), 8); //코드 길이
		
		return str;
	}
	private Object[] createMainCode(RuleContext ParserTree, ConstantPool CP, int InitMethodNameIndex) throws Exception
	{
		Int MaxStack = new Int(3);
		Int MaxLocal = new Int(2);
		Bool HasStackMapTable = new Bool(false);
		
		Parsertree2Bytecode treeParser = new Parsertree2Bytecode(ParserTree, CP, MaxStack, MaxLocal, HasStackMapTable, LineNumTable, StackMap, InitMethodNameIndex);
		Code = treeParser.getCode(false);
		
		return new Object[] { (Object)MaxStack.value, (Object)MaxLocal.value, (Object)HasStackMapTable.value };
	}
	public String toString()
	{
		String str = "";
		
		str += MethodInfo;
		str += CodeInfo;
		str += Code;
		str += Util.intToHex((ExceptionTableCount), 4);
		str += Util.intToHex((AttributeCount), 4);
		str += LineNumTable.toString();
		if(hasStackMap == true)
			str += StackMap.toString();
		
		return str;
	}
	private class MethodFlag 
	{ 
		public static final int Public = 1; 
		public static final int Private = 2; 
		public static final int Protected = 4; 
		public static final int Static = 8;
		public static final int Final = 16;
		public static final int Synchronized = 32;
		public static final int	Bridge = 64;
		public static final int Varargs = 128;
		public static final int Native = 256;
		public static final int Abstract = 1024;
		public static final int Strict = 2048;
		public static final int Syntheti = 4096;
	}
}
