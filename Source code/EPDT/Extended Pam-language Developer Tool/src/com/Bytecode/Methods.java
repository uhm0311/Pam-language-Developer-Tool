package com.Bytecode;

import org.antlr.v4.runtime.RuleContext;

import com.Bytecode.util.Method;


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
		 * toString�� ��ȯ�� ����
		 * initMethod.toString(); �ϼ�
		 * mainMethod.toString();
		 */
		return initMethod.toString() + mainMethod.toString();
	}
}
