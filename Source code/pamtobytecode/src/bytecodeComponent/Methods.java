package bytecodeComponent;

import org.antlr.v4.runtime.RuleContext;

import bytecodeComponent.util.Method;

public class Methods {

	private Method initMethod;
	private Method mainMethod;
	
	public Methods(RuleContext ParserTree, ConstantPool CP, int InitMethodIndex, int InitMethodNameIndex, int InitMethodDescriptorIndex, int MainMethodNameIndex, int MainMethodDescriptorIndex, int CodeNameIndex, int LineNumberTableNameIndex) throws Exception
	{
		initMethod = new Method(CodeNameIndex, LineNumberTableNameIndex);
		mainMethod = new Method(CodeNameIndex, LineNumberTableNameIndex);
		initMethod.createInitMethod(InitMethodIndex, InitMethodNameIndex, InitMethodDescriptorIndex);
		mainMethod.createMainMethod(ParserTree, CP, MainMethodNameIndex, MainMethodDescriptorIndex, InitMethodNameIndex);
	}
	public String toString()
	{
		/*
		 * toString의 반환값 설계
		 * initMethod.toString(); 완성
		 * mainMethod.toString();
		 */
		return initMethod.toString() + mainMethod.toString();
	}
}
