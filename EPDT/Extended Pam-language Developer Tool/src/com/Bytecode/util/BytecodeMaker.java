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
		 * Bytecode 순서 설계
		 * Information.getFileHeader(); 완성
		 * CP.toHexString(); 완성
		 * Information.getClassHeader(); 완성
		 * Method.toString(); 완성이라고 생각함. StackMapTable은 계속 지켜봐야 한다.
		 * Information.getAttributeHeader(); 완성
		 * LastAttribute.toString(); 완성
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
		CP.put(1, Constant.Class(2)); //오브젝트 클래스의 인덱스 저장
		CP.put(2, Constant.UTF8("java/lang/Object")); //오브젝트 클래스의 이름 저장. 끝
		CP.put(3, Constant.Method(1, 4)); //오브젝트 클래스의 init 메소드 정보 저장
		CP.put(4, Constant.NameAndType(5, 6)); //init 메소드의 이름과 반환 타입 저장
		CP.put(5, Constant.UTF8("<init>")); //init 메소드의 이름 저장. 끝
		CP.put(6, Constant.UTF8("()V")); //init 메소드의 인수와 반환타입 저장. 끝
		CP.put(7, Constant.Class(8)); //파일이름 클래스의 인덱스 저장
		CP.put(8, Constant.UTF8(ClassName)); //파일이름 클래스의 이름 저장. 끝
		CP.put(9, Constant.UTF8("main")); //main 메소드의 이름 저장. 끝
		CP.put(10, Constant.UTF8("([Ljava/lang/String;)V")); //main 메소드의 인수와 반환타입 저장. 끝
		CP.put(11, Constant.UTF8("Code")); //attribute의 이름, Code. 끝
		CP.put(12, Constant.UTF8("LineNumberTable")); //attribute의 이름, LineNumberTable. 끝
		CP.put(13, Constant.UTF8("SourceFile")); //SourceFile
		CP.put(14, Constant.UTF8(new File(SourcePath).getName())); //파일의 이름
		
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
