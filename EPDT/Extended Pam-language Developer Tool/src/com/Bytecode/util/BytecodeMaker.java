package com.Bytecode.util;

import java.io.File;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;

import com.Bytecode.ConstantPool;
import com.Bytecode.GeneralInformation;
import com.Bytecode.Methods;
import com.pamANTLR.pamLexer;
import com.pamANTLR.pamParser;


public class BytecodeMaker {
	
	String Bytecode = "";
	String ClassName = "";
	RuleContext ParserTree;
	ConstantPool CP = new ConstantPool(); 
	Methods Method;
	IAttribute LastAttribute;
	GeneralInformation Information = new GeneralInformation();
	String SourcePath = "";
	
	public BytecodeMaker(String FilePath) throws Exception
	{
		ANTLRFileStream input = new ANTLRFileStream(FilePath);
		pamLexer lex = new pamLexer(input);
		CommonTokenStream toknes = new CommonTokenStream(lex);
		pamParser parser = new pamParser(toknes);
		RuleContext tree = parser.program();
		
		System.out.println(tree.toStringTree());
		
		SourcePath = FilePath;
		ParserTree = tree;
		ClassName = FilePath.substring(FilePath.lastIndexOf('\\') + 1, FilePath.lastIndexOf('.'));
		initialization();
		
		/*
		 * Bytecode ���� ����
		 * Information.getFileHeader(); �ϼ�
		 * CP.toHexString(); �ϼ�
		 * Information.getClassHeader(); �ϼ�
		 * Method.toString(); �ϼ��̶�� ������. StackMapTable�� ��� ���Ѻ��� �Ѵ�.
		 * Information.getAttributeHeader(); �ϼ�
		 * LastAttribute.toString(); �ϼ�
		 */
		
		Bytecode += Information.getFileHeader();
		Bytecode += CP.toHexString();
		Bytecode += Information.getClassHeader();
		Bytecode += Method.toString();
		Bytecode += Information.getAttributeHeader();
		Bytecode += LastAttribute.toString();
	}
	public String getBytecode()
	{
		return Bytecode.toUpperCase();
	}
	public byte[] getByteStream()
	{
		return util.hexToByteArray(Bytecode);
	}
	
	private void initialization() throws Exception
	{
		CP.put(1, Constant.Class(2)); //������Ʈ Ŭ������ �ε��� ����
		CP.put(2, Constant.UTF8("java/lang/Object")); //������Ʈ Ŭ������ �̸� ����. ��
		CP.put(3, Constant.Method(1, 4)); //������Ʈ Ŭ������ init �޼ҵ� ���� ����
		CP.put(4, Constant.NameAndType(5, 6)); //init �޼ҵ��� �̸��� ��ȯ Ÿ�� ����
		CP.put(5, Constant.UTF8("<init>")); //init �޼ҵ��� �̸� ����. ��
		CP.put(6, Constant.UTF8("()V")); //init �޼ҵ��� �μ��� ��ȯŸ�� ����. ��
		CP.put(7, Constant.Class(8)); //�����̸� Ŭ������ �ε��� ����
		CP.put(8, Constant.UTF8(ClassName)); //�����̸� Ŭ������ �̸� ����. ��
		CP.put(9, Constant.UTF8("main")); //main �޼ҵ��� �̸� ����. ��
		CP.put(10, Constant.UTF8("([Ljava/lang/String;)V")); //main �޼ҵ��� �μ��� ��ȯŸ�� ����. ��
		CP.put(11, Constant.UTF8("Code")); //attribute�� �̸�, Code. ��
		CP.put(12, Constant.UTF8("LineNumberTable")); //attribute�� �̸�, LineNumberTable. ��
		CP.put(13, Constant.UTF8("SourceFile")); //SourceFile
		CP.put(14, Constant.UTF8(new File(SourcePath).getName())); //������ �̸�
		
		int ThisClassIndex = CP.IndexofClass(ClassName);
		int SuperClassIndex = CP.IndexofClass("java/lang/Object");
		int InitMethodIndex = CP.IndexofMethodref("java/lang/Object", "<init>", "()V");
		int InitMethodNameIndex = CP.IndexofUTF8("<init>");
		int InitMethodDescriptorIndex = CP.IndexofUTF8("()V");
		int MainMethodNameIndex = CP.IndexofUTF8("main");
		int MainMethodDescriptorIndex = CP.IndexofUTF8("([Ljava/lang/String;)V");
		int CodeNameIndex = CP.IndexofUTF8("Code");
		int LineNumberTableNameIndex = CP.IndexofUTF8("LineNumberTable");
		int SourceFileIndex = CP.IndexofUTF8("SourceFile");
		int SourceFileNameIndex = CP.IndexofUTF8(new File(SourcePath).getName());

		Information.setThisClassIndex(ThisClassIndex);
		Information.setSuperClassIndex(SuperClassIndex);
		
		Method = new Methods(ParserTree, CP, InitMethodIndex, InitMethodNameIndex, InitMethodDescriptorIndex, MainMethodNameIndex, MainMethodDescriptorIndex, CodeNameIndex, LineNumberTableNameIndex);
		LastAttribute = new GeneralAttribute(SourceFileIndex, util.intToHex((SourceFileNameIndex), 4));
	}
}
