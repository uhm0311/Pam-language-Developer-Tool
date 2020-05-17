package bytecodeComponent.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.antlr.v4.runtime.tree.Tree;

import bytecodeComponent.Bytecode;
import bytecodeComponent.ConstantPool;
import bytecodeComponent.LineNumberTable;
import bytecodeComponent.StackMapTable;

public class Parsertree2Bytecode {

	private ArrayList<String> DeclaredVariables = new ArrayList<String>();
	
	private String Code = "";
	private String BeforeCode = "";
	private Int LineNumber = new Int(2);
	
	private Int InsideCodeDepth = new Int(0);
	private Bool hasInputStatement = new Bool(false);
	
	public Parsertree2Bytecode(Tree tree, ConstantPool CP, Int MaxStack, Int MaxLocal, Bool HasStackMapTable, LineNumberTable LineNumTable, StackMapTable StackMap, int InitMethodNameIndex) throws Exception
	{
		DeclaredVariables.add("ù��°"); //�ǹ̾���. �ε��� �����ֱ� ���� ����
		
		hasInputStatement.value = hasInputStatement(tree);
		if(hasInputStatement.value == true)
		{
			DeclaredVariables.add("��ĳ��"); //1�� �ε����� scanner�� �ִ´�.
			CreateScanner(CP, LineNumTable, InitMethodNameIndex);
			StackMap.hasInputStatement(true);
		}
		Parse(tree, CP, MaxStack, MaxLocal, HasStackMapTable, LineNumTable, StackMap, InitMethodNameIndex);
		
		LineNumTable.add(new LineNumberFrame(Code.length() / 2, ++LineNumber.value));
		Code += Bytecode.Return;
	}
	private Parsertree2Bytecode(String Code, ArrayList<String> DeclaredVariables, Bool hasInputStatement, Tree tree, Int LineNumber, Int InsideCodeDepth, ConstantPool CP, Int MaxStack, Int MaxLocal, Bool HasStackMapTable, LineNumberTable LineNumTable, StackMapTable StackMap, int InitMethodNameIndex) throws Exception
	{
		this.LineNumber = LineNumber;
		this.InsideCodeDepth = new Int(InsideCodeDepth.value + 1);
		this.hasInputStatement = hasInputStatement;
		BeforeCode = Code;
		this.Code = new String(Code);
		this.DeclaredVariables.addAll(DeclaredVariables);
		
		Parse(tree, CP, MaxStack, MaxLocal, HasStackMapTable, LineNumTable, StackMap, InitMethodNameIndex);
	}
	public String getCode(boolean isInside)
	{
		if(isInside == false)
			return Code.toUpperCase();
		else return Code.substring(BeforeCode.length()).toUpperCase();
	}
	private void Parse(Tree tree, ConstantPool CP, Int MaxStack, Int MaxLocal, Bool hasStackMapTable, LineNumberTable LineNumTable, StackMapTable StackMap, int InitMethodNameIndex) throws Exception
	{
		for(int i = 0; i < tree.getChildCount(); i++)
		{
			String identifier = tree.getChild(i).toString();
			
			if(Util.isStatements(identifier))
				Parse(tree.getChild(i), CP, MaxStack, MaxLocal, hasStackMapTable, LineNumTable, StackMap, InitMethodNameIndex);
			
			else if(Util.isConditional(identifier)) //�ڵ� ������ ����� �Ǵ°Ͱ���. StackMapTable ���� ���Ѻ��� �Ѵ�.
			{
				boolean hasElse = false; //else�� �ִ°�
				boolean isInsideOfStatement = false;

				if(hasStackMapTable.value == false)
				{
					hasStackMapTable.value = true;
					int StackMapTableNameIndex = CP.size() + 1;
					CP.put(StackMapTableNameIndex, Constant.UTF8("StackMapTable"));
					StackMap.setNameIndex(StackMapTableNameIndex);
				}
				if(tree.getChild(i).getChildCount() > 5)
					hasElse = true;
				
				LineNumTable.add(new LineNumberFrame(Code.length() / 2, ++LineNumber.value));
				
				isInsideOfStatement = isInsideOfStatement(tree);
				
				Tree Comparison = tree.getChild(i).getChild(1);
				Tree Statements = tree.getChild(i).getChild(3);
				Tree ElseStatements = null;
				
				if(hasElse == true)
					ElseStatements = tree.getChild(i).getChild(5);
				
				Parsertree2Bytecode p;
				
				String IfStatement = "";
				String GotoStatement = "";
				
				Tree LeftExpression = Comparison.getChild(0);
				String Relationship = Comparison.getChild(1).toString();
				Tree RightExpression = Comparison.getChild(2);
				
				String InsideCode = "";
				String ElseInsideCode = "";
				
				String IfCode = "";
				
				int Offset = 0;
				int ElseOffset = 0;
				int Goto = 0;
				int ElseGoto = 0;
				
				Bool isRightZero = new Bool(false); //�������� ��� 0�ΰ�. ����� ����� 0�� ���� ����.
				
				//������ ������ �ҷ��´�. �����U�� 0�ϰ�� �ҷ����� �ʴ´�.
				if(Util.isElement(LeftExpression.getChild(0).toString())) //������ ������Ʈ�� ���
				{					
					Tree LeftElement = LeftExpression.getChild(0);
					IfStatement += LoadIntegerForComparison(LeftElement.getChild(0).toString(), CP, true, isRightZero);
				}
				else //������Ʈ�� �ƴϸ� ����� �ؾ� �Ѵ�.
					IfStatement += CalculateForComparison(LeftExpression, CP, LineNumTable, true, isRightZero);
				
				if(Util.isElement(RightExpression.getChild(0).toString())) //������ ������Ʈ�� ���
				{					
					Tree RightElement = RightExpression.getChild(0);
					IfStatement += LoadIntegerForComparison(RightElement.getChild(0).toString(), CP, false, isRightZero);
				}
				else //����ؾ��Ѵ�.
					IfStatement += CalculateForComparison(RightExpression, CP, LineNumTable, false, isRightZero);
				
				switch (Relationship) //�׻� ����Ʈ�ڵ�� ���� ���̽��� �ݴ��. 
				{ //�������� ��� 0�� ���, �ҷ����� �����Ƿ� ���� ���۷��带 �ϳ��� ����ϴ� ����Ʈ�ڵ带 ����. �� ���� ��� ���� ���۷��带 �ΰ� ���� ����Ʈ�ڵ带 ����.
				case ">": //Less than or Equal
					if(isRightZero.value == true)
						IfCode = Bytecode.Ifle(0);
					else IfCode = Bytecode.If_icmple(0);
					break;
				case ">=": //Less then
					if(isRightZero.value == true)
						IfCode = Bytecode.Iflt(0);
					else IfCode = Bytecode.If_icmplt(0);
					break;
				case "<": //grater then or equal
					if(isRightZero.value == true)
						IfCode = Bytecode.Ifge(0);
					else IfCode = Bytecode.If_icmpge(0);
					break;
				case "<=": //grater then
					if(isRightZero.value == true)
						IfCode = Bytecode.Ifgt(0);
					else IfCode = Bytecode.If_icmpgt(0);
					break;
				case "=": //Not Equal
					if(isRightZero.value == true)
						IfCode = Bytecode.Ifne(0);
					else IfCode = Bytecode.If_icmpne(0);
					break;
				case "<>": //Equal
					if(isRightZero.value == true)
						IfCode = Bytecode.Ifeq(0);
					else IfCode = Bytecode.If_icmpeq(0);
					break;
				default:
					throw new Exception("Unknown Exception at Comparison. Relationship : " + Relationship);
				}
				//If�� Goto�� (IfStatement�� ���� ũ�� + OffsetCode�� ũ��) / 2�� �ȴ�.
				//Else�� Goto�� (GotoStatement�� ũ��(3����Ʈ) + ElseOffsetCode�� ũ��) / 2�� �ȴ�.
				//If�� Offset�� (�ڵ� ���� + Ifstatement ����) / 2 + Goto�� �ȴ�.
				//Else�� Offset�� If�� Offset + (ElseInsideCode ����) / 2�� �ȴ�.
				
				p = new Parsertree2Bytecode(Code + IfStatement + Util.StretchLenth("0", 6), DeclaredVariables, hasInputStatement, Statements, LineNumber, InsideCodeDepth, CP, MaxStack, MaxLocal, hasStackMapTable, LineNumTable, StackMap, InitMethodNameIndex);
				InsideCode = p.getCode(true);
				
				if(hasElse == true)
				{
					p = new Parsertree2Bytecode(Code + IfStatement + InsideCode + Util.StretchLenth("0", 12), DeclaredVariables, hasInputStatement, ElseStatements, LineNumber, InsideCodeDepth, CP, MaxStack, MaxLocal, hasStackMapTable, LineNumTable, StackMap, InitMethodNameIndex);
					ElseInsideCode = p.getCode(true);
					ElseGoto = (ElseInsideCode.length() + 6) / 2;
					GotoStatement = Bytecode.Goto(ElseGoto);
					InsideCode += GotoStatement;
				}
				Goto = (InsideCode.length() + 6) / 2;
				Offset = ((Code + IfStatement).length() / 2) + Goto;
				IfStatement += IfCode.substring(0, 2) + Util.intToHex((Goto), 4);
				
				StoreToStackMapTable(StackMap, CP, Offset);
				
				if(hasElse == true)
				{
					ElseOffset = Offset + (ElseInsideCode.length() / 2);
					StoreToStackMapTable(StackMap, CP, ElseOffset);
				}
				
				Code += IfStatement + InsideCode;
				if(hasElse)
					Code += ElseInsideCode;
				
				if(isInsideOfStatement == false)
					StackMap.setFirstOffset(Offset);
			}
			
			else if(Util.isDefiniteLoop(identifier))
			{
				if(hasStackMapTable.value == false)
				{
					hasStackMapTable.value = true;
					int StackMapTableNameIndex = CP.size() + 1;
					CP.put(StackMapTableNameIndex, Constant.UTF8("StackMapTable"));
					StackMap.setNameIndex(StackMapTableNameIndex);
				}
				LineNumTable.add(new LineNumberFrame(Code.length() / 2, ++LineNumber.value));
				
				boolean isInsideOfStatement = isInsideOfStatement(tree);
				
				String Flag = "flag " + String.valueOf(InsideCodeDepth.value + 1) + "flag ".hashCode();
				
				Flag += String.valueOf(System.currentTimeMillis() + Flag.hashCode());
				LoadInteger("0", CP, LineNumTable);
				StoreInteger(Flag, false, MaxStack, MaxLocal, LineNumTable);
				
				int LoopIndex = Code.length() / 2;
				Tree Expression = tree.getChild(i).getChild(1);
				Tree Statements = tree.getChild(i).getChild(3);
				String ForStatement = "";
				
				ForStatement += LoadIntegerForComparison(Flag, CP, true, new Bool(false)); //Flag�� �ҷ��´�.
				
				if(Util.isElement(Expression.getChild(0).toString())) //������ �ƴ� ���
				{
					Tree Element = Expression.getChild(0);
					ForStatement += LoadIntegerForComparison(Element.getChild(0).toString(), CP, false, new Bool(false));
				}
				else
					ForStatement += CalculateForComparison(Expression, CP, LineNumTable, true, new Bool(false));
				
				Parsertree2Bytecode p = new Parsertree2Bytecode(Code + ForStatement + Util.StretchLenth("0", 6), DeclaredVariables, hasInputStatement, Statements, LineNumber, InsideCodeDepth, CP, MaxStack, MaxLocal, hasStackMapTable, LineNumTable, StackMap, InitMethodNameIndex);
				String InsideCode = p.getCode(true);
				InsideCode += Bytecode.iinc(DeclaredVariables.indexOf(Flag), 1);
				
				int Goto = (InsideCode.length() + 12) / 2; //iinc, goto��
				int Offset = ((Code + ForStatement).length() / 2) + Goto;
				int GoBackto = LoopIndex - Offset + 3; //3�� goto �ε��� ���� 3����Ʈ�� �ǹ�
				String GotoStatement = Bytecode.Goto(GoBackto);

				ForStatement += Bytecode.If_icmpge(Goto); //�׻� i�� �� ���� ���� �����̹Ƿ� ge
				
				StoreToStackMapTable(StackMap, CP, Offset);
				StoreToStackMapTable(StackMap, CP, LoopIndex);
				
				Code += ForStatement + InsideCode;
				LineNumTable.add(new LineNumberFrame(Code.length() / 2, ++LineNumber.value));
				Code += GotoStatement;
				
				if(isInsideOfStatement == false)
					StackMap.setFirstOffset(Offset);
			}
			
			else if(Util.isInfiniteLoop(identifier))
			{
				if(hasStackMapTable.value == false)
				{
					hasStackMapTable.value = true;
					int StackMapTableNameIndex = CP.size() + 1;
					CP.put(StackMapTableNameIndex, Constant.UTF8("StackMapTable"));
					StackMap.setNameIndex(StackMapTableNameIndex);
				}
				LineNumTable.add(new LineNumberFrame(Code.length() / 2, ++LineNumber.value));
				
				boolean isInsideOfStatement = isInsideOfStatement(tree);
				
				Tree Comparison = tree.getChild(i).getChild(1);
				Tree Statements = tree.getChild(i).getChild(3);
				
				Tree LeftExpression = Comparison.getChild(0);
				String Relationship = Comparison.getChild(1).toString();
				Tree RightExpression = Comparison.getChild(2);
				
				int LoopIndex = Code.length() / 2;
				String WhileStatement = "";
				Bool isRightZero = new Bool(false);

				if(Util.isElement(LeftExpression.getChild(0).toString())) //������ ������Ʈ�� ���
				{					
					Tree LeftElement = LeftExpression.getChild(0);
					WhileStatement += LoadIntegerForComparison(LeftElement.getChild(0).toString(), CP, true, isRightZero);
				}
				else //������Ʈ�� �ƴϸ� ����� �ؾ� �Ѵ�.
					WhileStatement += CalculateForComparison(LeftExpression, CP, LineNumTable, true, isRightZero);
				
				if(Util.isElement(RightExpression.getChild(0).toString())) //������ ������Ʈ�� ���
				{					
					Tree RightElement = RightExpression.getChild(0);
					WhileStatement += LoadIntegerForComparison(RightElement.getChild(0).toString(), CP, false, isRightZero);
				}
				else //����ؾ��Ѵ�.
					WhileStatement += CalculateForComparison(RightExpression, CP, LineNumTable, true, isRightZero);
				
				String IfCode = "";
				
				switch (Relationship) //�׻� ����Ʈ�ڵ�� ���� ���̽��� �ݴ��. 
				{ //�������� ��� 0�� ���, �ҷ����� �����Ƿ� ���� ���۷��带 �ϳ��� ����ϴ� ����Ʈ�ڵ带 ����. �� ���� ��� ���� ���۷��带 �ΰ� ���� ����Ʈ�ڵ带 ����.
				case ">": //Less than or Equal
					if(isRightZero.value == true)
						IfCode = Bytecode.Ifle(0);
					else IfCode = Bytecode.If_icmple(0);
					break;
				case ">=": //Less then
					if(isRightZero.value == true)
						IfCode = Bytecode.Iflt(0);
					else IfCode = Bytecode.If_icmplt(0);
					break;
				case "<": //grater then or equal
					if(isRightZero.value == true)
						IfCode = Bytecode.Ifge(0);
					else IfCode = Bytecode.If_icmpge(0);
					break;
				case "<=": //grater then
					if(isRightZero.value == true)
						IfCode = Bytecode.Ifgt(0);
					else IfCode = Bytecode.If_icmpgt(0);
					break;
				case "=": //Not Equal
					if(isRightZero.value == true)
						IfCode = Bytecode.Ifne(0);
					else IfCode = Bytecode.If_icmpne(0);
					break;
				case "<>": //Equal
					if(isRightZero.value == true)
						IfCode = Bytecode.Ifeq(0);
					else IfCode = Bytecode.If_icmpeq(0);
					break;
				default:
					throw new Exception("Unknown Exception at Comparison. Relationship : " + Relationship);
				}
				Parsertree2Bytecode p = new Parsertree2Bytecode(Code + WhileStatement + Util.StretchLenth("0", 6), DeclaredVariables, hasInputStatement, Statements, LineNumber, InsideCodeDepth, CP, MaxStack, MaxLocal, hasStackMapTable, LineNumTable, StackMap, InitMethodNameIndex);
				String InsideCode = p.getCode(true);
				
				int Goto = (InsideCode.length() + 12) / 2; //goto��
				int Offset = ((Code + WhileStatement).length() / 2) + Goto;
				//StackMapTable�� Offset�� Goto�� ������ ����Ų��. Definite���� iinc�� �־ 12�� ������ �ڵ����� Goto ������ ����Ű�µ�, ���⼱ iinc�� ��� StackMapTable�� ���� +12�� �Ѵ�.
				int GoBackto = LoopIndex - Offset + 3; //���� ���� ������ +3
				String GotoStatement = Bytecode.Goto(GoBackto);
				WhileStatement += IfCode.substring(0, 2) + Util.intToHex((Goto), 4);
				
				StoreToStackMapTable(StackMap, CP, Offset);
				StoreToStackMapTable(StackMap, CP, LoopIndex);
				
				Code += WhileStatement + InsideCode;
				LineNumTable.add(new LineNumberFrame(Code.length() / 2, ++LineNumber.value));
				Code += GotoStatement;
				
				if(isInsideOfStatement == false)
					StackMap.setFirstOffset(Offset);
			}
			
			else if(Util.isAssign(identifier))
			{
				String Variable = tree.getChild(i).getChild(0).toString();
				Tree Expression = tree.getChild(i).getChild(2);

				//���ԵǴ� ���� Ȥ�� ���� ���� ������ �Ѵ�.
				if(Expression.getChild(0).getChildCount() == 1) //�ͽ��������� �ڽ��� �׻� �ϳ���. �ڽ��� �ڽ��� �ϳ���� �̴� ����� ������ �����ϴ� ���̴�.
				{
					String AssignValue = Expression.getChild(0).getChild(0).toString();
					
					LoadInteger(AssignValue, CP, LineNumTable);
					StoreInteger(Variable, true, MaxStack, MaxLocal, LineNumTable);
				}
				else //�Ѱ��� �ƴ� ���. �׷��� ����� ���� �ؾ� �Ѵ�.
				{
					Calculate(Expression, CP, LineNumTable);
					StoreInteger(Variable, true, MaxStack, MaxLocal, LineNumTable);
				}
			}
			
			else if(Util.isInput(identifier))
			{
				Tree InputList = tree.getChild(i).getChild(1);

				for(int j = 0; j < InputList.getChildCount(); j++)
				{
					if(!InputList.getChild(j).toString().equals(",") && !InputList.getChild(j).toString().equals(";"))
					{
						String Variable = InputList.getChild(j).toString();
						
						LineNumTable.add(new LineNumberFrame(Code.length() / 2, ++LineNumber.value));
						Code += Bytecode.Aload_1; //������ aload_1�̴�.
						Code += Bytecode.Invokevirtual(CP.IndexofMethodref("java/util/Scanner", "nextInt", "()I")); //invokevirtual nextInt()
						//invokevirtual�� nextInt�� �ҷ�����. ���� istore�� ���ָ��.
						StoreInteger(Variable, false, MaxStack, MaxLocal, LineNumTable);
					}
				}
			}
			
			else if(Util.isOutput(identifier))
			{
				boolean Line = tree.getChild(i).getChild(0).toString().equals("writeline");
				Tree OutputList = tree.getChild(i).getChild(1);
				
				for(int j = 0; j < OutputList.getChildCount(); j++)
				{
					if(!OutputList.getChild(j).toString().equals(",") && !OutputList.getChild(j).toString().equals(";"))
					{
						if(CP.IndexofField("java/lang/System", "out", "Ljava/io/PrintStream;") == -1 || CP.IndexofClass("java/io/PrintStream") == -1)
							InitPrintData(CP);
						
						LineNumTable.add(new LineNumberFrame(Code.length() / 2, ++LineNumber.value));
						boolean isStr = false;
						Code += Bytecode.Getstatic(CP.IndexofField("java/lang/System", "out", "Ljava/io/PrintStream;")); //getstatic java/lang/System.out
						
						if(OutputList.getChild(j).toString().contains("\""))
						{
							String str = OutputList.getChild(j).toString();
							str = str.substring(1, str.length() - 1);
							str = str.replace("\\", "");
							int UTF8index = CP.IndexofUTF8(str);
							int StringrefIndex = CP.IndexofString(str);
							
							isStr = true;
							
							if(StringrefIndex == -1) //CP�� Stringref�� �־���� �ϴ� ���
							{
								StringrefIndex = CP.size() + 1;
								
								if(UTF8index == -1) //CP�� UTF8�� �־���� �ϴ� ���
								{
									UTF8index = StringrefIndex + 1;
									CP.put(UTF8index, Constant.UTF8(str));
								}
								CP.put(StringrefIndex, Constant.String(UTF8index)); //string ref info
							}
							
							Code += Bytecode.Ldc(StringrefIndex); //ldc �ε��� �ѹ���Ʈ
						}
						else if(Util.isVariable(OutputList.getChild(j).toString()))
						{
							String Variable = OutputList.getChild(j).toString();
							int index = DeclaredVariables.indexOf(Variable);
							
							if(index < 0)
								throw new Exception("�ʱ�ȭ ���� ���� �����Դϴ�." + Variable + " " + index);
							else LoadInteger(Variable, CP, LineNumTable);
						}
						else throw new Exception("���� ���ͷ��� ����� �� �����ϴ�.");
						
						if(j + 2 == OutputList.getChildCount()) //�������� ���. +2�� ������ �����ݷ� �����̴�.
						{
							if(isStr == true) 
							{
								if(Line == true)
									LoadPrintlnString(CP);
								else LoadPrintString(CP);
							}
							else
							{
								if(Line == true)
									LoadPrintlnInt(CP);
								else LoadPrintInt(CP);
							}
						}
						
						else //�������� �ƴ� ���
						{
							if(isStr == true) LoadPrintString(CP);
							else LoadPrintInt(CP);
							
							LineNumTable.add(new LineNumberFrame(Code.length() / 2, ++LineNumber.value));
							Code += Bytecode.Getstatic(CP.IndexofField("java/lang/System", "out", "Ljava/io/PrintStream;")); //getstatic System.out
							
							int Blankindex = CP.IndexofString(" ");
							
							if(Blankindex == -1) //CP�� �־���� �ϴ� ���
							{
								Blankindex = CP.size() + 1;
								
								CP.put(Blankindex, Constant.String(Blankindex + 1)); //string ref info
								CP.put(Blankindex + 1, Constant.UTF8(" "));
							}
							Code += Bytecode.Ldc(Blankindex); //ldc �ε��� �ѹ���Ʈ
							
							LoadPrintString(CP);
						}
					}
				}
			}
			else if(tree.getChild(i).getChildCount() > 0) Parse(tree.getChild(i), CP, MaxStack, MaxLocal, hasStackMapTable, LineNumTable, StackMap, InitMethodNameIndex);
		}
	}
	
	private void LoadInteger(String Value, ConstantPool CP, LineNumberTable LineNumTable) throws Exception
	{
		if(Util.isVariable(Value)) //������ ���
		{
			int index = DeclaredVariables.indexOf(Value);
			
			if(index > 3)
				Code += Bytecode.Iload(index); //iload + �ε��� �ѹ���Ʈ
			
			else switch(index)
			{
			case 1: Code += Bytecode.Iload_1; break; //iload_1
			case 2: Code += Bytecode.Iload_2; break; //iload_2
			case 3: Code += Bytecode.Iload_3; break; //iload_3
			default : throw new Exception("Unknown Exception at LoadInteger. Variable index : " + index);
			}
		}
		else if(Util.isConstant(Value)) //Constant�� ���
		{
			int Const = Integer.parseInt(Value);
			
			if(Const == -1)
				Code += Bytecode.Iconst_m1;
			
			else if(Const >= 0 && Const <= 5) //0~5�� iconst�� �ִ�.
			{
				LineNumTable.add(new LineNumberFrame(Code.length() / 2, ++LineNumber.value));
				String iconst = "";
				switch(Const)
				{
					case 0: iconst = Bytecode.Iconst_0; break;
					case 1: iconst = Bytecode.Iconst_1; break;
					case 2: iconst = Bytecode.Iconst_2; break;
					case 3: iconst = Bytecode.Iconst_3; break;
					case 4: iconst = Bytecode.Iconst_4; break;
					case 5: iconst = Bytecode.Iconst_5; break;
					default : throw new Exception("Unknown Exception at LoadInteger. Constant : " + Const);
				}
				Code += iconst;
			}
			//127������ bipush ���ѹ���Ʈ
			else if(Const >= Byte.MIN_VALUE && Const <= Short.MAX_VALUE) 
				Code += Bytecode.Bipush(Const);
			//32767������ sipush ���ι���Ʈ
			else if(Const >= Short.MIN_VALUE && Const <= Short.MAX_VALUE) 
				Code += Bytecode.Sipush(Const);
			//32768���ʹ� CP�� �־ ldc�� �ҷ��� �� istore�� �Ѵ�. int�� cp �±״� 03�̰�, ���� 16������ �ٲپ� 8�ڸ�(4����Ʈ)�� �ؾ� �Ѵ�.
			else
			{
				int intrefIndex = CP.IndexofInteger(Const);
				
				if(intrefIndex == -1) //���� ���
				{
					intrefIndex = CP.size() + 1;
					CP.put(intrefIndex, Constant.Integer(Const));
				}
				Code += Bytecode.Ldc(intrefIndex); //�ε����� �ѹ���Ʈ
			}
		}
	}
	
	private void StoreInteger(String Variable, boolean AddToLineNumberTable, Int MaxStack, Int MaxLocal, LineNumberTable LineNumTable) throws Exception
	{
		if(!DeclaredVariables.contains(Variable))
		{
			DeclaredVariables.add(Variable);
			MaxLocal.value++;
		}
		
		int index = DeclaredVariables.indexOf(Variable);
		
		if(index > 3)
			Code += Bytecode.Istore(index); //istore + �ε��� �ѹ���Ʈ
		
		else switch(index)
		{
		case 1: Code += Bytecode.Istore_1; break; //istore_1
		case 2: Code += Bytecode.Istore_2; break; //istore_2
		case 3: Code += Bytecode.Istore_3; break; //istore_3
		default : throw new Exception("Unknown Exception at StoreInteger. Variable index : " + index); //0�̰ų� 1�� ���� ����.
		}
		MaxStack.value++;
		
		if(AddToLineNumberTable == true)
			LineNumTable.add(new LineNumberFrame(Code.length() / 2, ++LineNumber.value));
	}
	
	private void Calculate(Tree Expression, ConstantPool CP, LineNumberTable LineNumTable) throws Exception
	{
		HashMap<Integer, String> WeakOP = new HashMap<Integer, String>(); //���� ������ ������ ��ȣ
		HashMap<Integer, String> StrongOP = new HashMap<Integer, String>(); //���� �������� ������ ��ȣ
		
		for(int i = 0; i < Expression.getChildCount(); i++)
			LoadStrongInteger(Expression.getChild(i), WeakOP, StrongOP, CP, LineNumTable);
		LoadStrongOperator(StrongOP);
		
		for(int i = 0; i < Expression.getChildCount(); i++)
			LoadWeakInteger(Expression.getChild(i), WeakOP, StrongOP, CP, LineNumTable);
		LoadWeakOperator(WeakOP);
	}
	
	
	private void LoadStrongInteger(Tree tree, HashMap<Integer, String> WeakOP, HashMap<Integer, String> StrongOP, ConstantPool CP, LineNumberTable LineNumTable) throws Exception
	{
		if(Util.isMultiplicative(tree.toString())) //tree ��ü�� Multiplicative�̴�.
		{
			for(int i = 0; i < tree.getChildCount(); i++)
			{
				if(Util.isAdditive(tree.getChild(i).toString())) //tree���� Addtive �ڽ��� i��°�� �ִ� ���
				{
					LoadStrongOperator(StrongOP);
					LoadWeakInteger(tree.getChild(i), WeakOP, StrongOP, CP, LineNumTable);
					LoadWeakOperator(WeakOP);
				}
				else if(Util.isMultiplicative(tree.getChild(i).toString())) //tree���� Multiplicative �ڽ��� �ִ� ���.
					LoadStrongInteger(tree.getChild(i), WeakOP, StrongOP, CP, LineNumTable);
				
				else if(tree.getChild(i).toString().equals("*"))
					StrongOP.put(StrongOP.size(), "*");
				else if(tree.getChild(i).toString().equals("/"))
					StrongOP.put(StrongOP.size(), "/");
				
				else if(Util.isElement(tree.getChild(i).toString())) //������Ʈ�϶�
					LoadInteger(tree.getChild(i).getChild(0).toString(), CP, LineNumTable);
			}
		}
	}
	
	private void LoadStrongOperator(HashMap<Integer, String> StrongOP) throws Exception
	{
		int loop = StrongOP.size();
		for(int i = 0; i < loop; i++)
		{
			if(StrongOP.get(i).equals("*"))
				Code += Bytecode.Imul;
			else if(StrongOP.get(i).equals("/"))
				Code += Bytecode.Idiv;
			else throw new Exception("Unknown Exception at StrongOP" + StrongOP.get(i));
			
			StrongOP.remove(i);
		}
	}
	
	private void LoadWeakInteger(Tree tree, HashMap<Integer, String> WeakOP, HashMap<Integer, String> StrongOP, ConstantPool CP, LineNumberTable LineNumTable) throws Exception
	{
		if(Util.isAdditive(tree.toString())) //tree ��ü�� Additive�̴�.
		{
			for(int i = 0; i < tree.getChildCount(); i++)
			{
				if(Util.isAdditive(tree.getChild(i).toString())) //tree���� Addtive �ڽ��� i��°�� �ִ� ���
					LoadWeakInteger(tree.getChild(i), WeakOP, StrongOP, CP, LineNumTable);
				else if(Util.isMultiplicative(tree.getChild(i).toString())) //tree���� Multiplicative �ڽ��� �ִ� ���.
				{
					LoadStrongInteger(tree.getChild(i), WeakOP, StrongOP, CP, LineNumTable);
					LoadStrongOperator(StrongOP);
					LoadWeakOperator(WeakOP);
				}
				
				else if(tree.getChild(i).toString().equals("+"))
					WeakOP.put(WeakOP.size(), "+");
				else if(tree.getChild(i).toString().equals("-"))
					WeakOP.put(WeakOP.size(), "-");
				
				else if(Util.isElement(tree.getChild(i).toString())) //������Ʈ�϶�
					LoadInteger(tree.getChild(i).getChild(0).toString(), CP, LineNumTable);
			}
		}
	}
	
	private void LoadWeakOperator(HashMap<Integer, String> WeakOP) throws Exception
	{
		int loop = WeakOP.size();
		for(int i = 0; i < loop; i++)
		{
			if(WeakOP.get(i).equals("+"))
				Code += Bytecode.Iadd;
			else if(WeakOP.get(i).equals("-"))
				Code += Bytecode.Isub;
			else throw new Exception("Unknown Exception at WeakOP" + WeakOP.get(i));
			
			WeakOP.remove(i);
		}
	}
	
	private String LoadIntegerForComparison(String Element, ConstantPool CP, boolean LoadEvenZero, Bool isRightZero) throws Exception
	{
		String str = "";
		
		if(Util.isVariable(Element))
		{
			int index = DeclaredVariables.indexOf(Element);
			
			if(index < 0)
				throw new Exception("�ʱ�ȭ ���� ���� �����Դϴ�. " + Element + " " + index);
			else
			{
				if(index > 3)
					str += Bytecode.Iload(index); //iload + �ε��� �ѹ���Ʈ
				
				else switch(index)
				{
				case 1: str += Bytecode.Iload_1; break; //iload_1
				case 2: str += Bytecode.Iload_2; break; //iload_2
				case 3: str += Bytecode.Iload_3; break; //iload_3
				default : throw new Exception("Unknown Load Exception at Comparison. Variable index : " + index); //0�̰ų� 1�� ���� ����.
				}
			}
		}
		else 
		{
			int Const = Integer.parseInt(Element);
			
			if(Const == -1)
				str += Bytecode.Iconst_m1;
			
			else if(Const >= 0 && Const <= 5) //0~5�� iconst�� �ִ�.
			{
				String iconst = "";
				switch(Const)
				{
					case 0: 
						if(LoadEvenZero == true)
							iconst = Bytecode.Iconst_0;
						else isRightZero.value = true;
						break;
					case 1: iconst = Bytecode.Iconst_1; break;
					case 2: iconst = Bytecode.Iconst_2; break;
					case 3: iconst = Bytecode.Iconst_3; break;
					case 4: iconst = Bytecode.Iconst_4; break;
					case 5: iconst = Bytecode.Iconst_5; break;
					default : throw new Exception("Unknown Exception at Comparison. Constant : " + Const);
				}
				str += iconst;
			}
			//127������ bipush ���ѹ���Ʈ
			else if(Const >= Byte.MIN_VALUE && Const <= Byte.MAX_VALUE) 
				str += Bytecode.Bipush(Const);
			//32767������ sipush ���ι���Ʈ
			else if(Const >= Short.MIN_VALUE && Const <= Short.MAX_VALUE) 
				str += Bytecode.Sipush(Const);
			//32768���ʹ� CP�� �־ ldc�� �ҷ��� �� istore�� �Ѵ�. int�� cp �±״� 03�̰�, ���� 16������ �ٲپ� 8�ڸ�(4����Ʈ)�� �ؾ� �Ѵ�.
			else
			{
				int intrefIndex = CP.IndexofInteger(Const);
				
				if(intrefIndex == -1) //���� ���
				{
					intrefIndex = CP.size() + 1;
					CP.put(intrefIndex, Constant.Integer(Const));
				}
				str += Bytecode.Ldc(intrefIndex); //�ε����� �ѹ���Ʈ
			}
		}
		return str;
	}
	
	private String CalculateForComparison(Tree Expression, ConstantPool CP, LineNumberTable LineNumTable, boolean LoadEvenZero, Bool isRightZero) throws Exception
	{
		String str = "";
		
		HashMap<Integer, String> WeakOP = new HashMap<Integer, String>(); //���� ������ ������ ��ȣ
		HashMap<Integer, String> StrongOP = new HashMap<Integer, String>(); //���� �������� ������ ��ȣ
		
		for(int i = 0; i < Expression.getChildCount(); i++)
			str += LoadStrongIntegerForComparison(Expression.getChild(i), WeakOP, StrongOP, CP, LineNumTable, LoadEvenZero, isRightZero);
		str += LoadStrongOperatorForComparison(StrongOP);
		
		for(int i = 0; i < Expression.getChildCount(); i++)
			str += LoadWeakIntegerForComparison(Expression.getChild(i), WeakOP, StrongOP, CP, LineNumTable, LoadEvenZero, isRightZero);
		str += LoadWeakOperatorForComparison(WeakOP);
		
		return str;
	}
	
	
	private String LoadStrongIntegerForComparison(Tree tree, HashMap<Integer, String> WeakOP, HashMap<Integer, String> StrongOP, ConstantPool CP, LineNumberTable LineNumTable, boolean LoadEvenZero, Bool isRightZero) throws Exception
	{
		String str = "";
		
		if(Util.isMultiplicative(tree.toString())) //tree ��ü�� Multiplicative�̴�.
		{
			for(int i = 0; i < tree.getChildCount(); i++)
			{
				if(Util.isAdditive(tree.getChild(i).toString())) //tree���� Addtive �ڽ��� i��°�� �ִ� ���
				{
					str += LoadStrongOperatorForComparison(StrongOP);
					str += LoadWeakIntegerForComparison(tree.getChild(i), WeakOP, StrongOP, CP, LineNumTable, LoadEvenZero, isRightZero);
					str += LoadWeakOperatorForComparison(WeakOP);
				}
				else if(Util.isMultiplicative(tree.getChild(i).toString())) //tree���� Multiplicative �ڽ��� �ִ� ���.
					str += LoadStrongIntegerForComparison(tree.getChild(i), WeakOP, StrongOP, CP, LineNumTable, LoadEvenZero, isRightZero);
				
				else if(tree.getChild(i).toString().equals("*"))
					StrongOP.put(StrongOP.size(), "*");
				else if(tree.getChild(i).toString().equals("/"))
					StrongOP.put(StrongOP.size(), "/");
				
				else if(Util.isElement(tree.getChild(i).toString())) //������Ʈ�϶�
					str += LoadIntegerForComparison(tree.getChild(i).getChild(0).toString(), CP, LoadEvenZero, isRightZero);
			}
		}
		return str;
	}
	
	private String LoadStrongOperatorForComparison(HashMap<Integer, String> StrongOP) throws Exception
	{
		String str = "";
		
		int loop = StrongOP.size();
		for(int i = 0; i < loop; i++)
		{
			if(StrongOP.get(i).equals("*"))
				str += Bytecode.Imul;
			else if(StrongOP.get(i).equals("/"))
				str += Bytecode.Idiv;
			else throw new Exception("Unknown Exception at StrongOP" + StrongOP.get(i));
			
			StrongOP.remove(i);
		}
		return str;
	}
	
	private String LoadWeakIntegerForComparison(Tree tree, HashMap<Integer, String> WeakOP, HashMap<Integer, String> StrongOP, ConstantPool CP, LineNumberTable LineNumTable, boolean LoadEvenZero, Bool isRightZero) throws Exception
	{
		String str = "";
		
		if(Util.isAdditive(tree.toString())) //tree ��ü�� Additive�̴�.
		{
			for(int i = 0; i < tree.getChildCount(); i++)
			{
				if(Util.isAdditive(tree.getChild(i).toString())) //tree���� Addtive �ڽ��� i��°�� �ִ� ���
					str += LoadWeakIntegerForComparison(tree.getChild(i), WeakOP, StrongOP, CP, LineNumTable, LoadEvenZero, isRightZero);
				else if(Util.isMultiplicative(tree.getChild(i).toString())) //tree���� Multiplicative �ڽ��� �ִ� ���.
				{
					str += LoadStrongIntegerForComparison(tree.getChild(i), WeakOP, StrongOP, CP, LineNumTable, LoadEvenZero, isRightZero);
					str += LoadStrongOperatorForComparison(StrongOP);
					str += LoadWeakOperatorForComparison(WeakOP);
				}
				
				else if(tree.getChild(i).toString().equals("+"))
					WeakOP.put(WeakOP.size(), "+");
				else if(tree.getChild(i).toString().equals("-"))
					WeakOP.put(WeakOP.size(), "-");
				
				else if(Util.isElement(tree.getChild(i).toString())) //������Ʈ�϶�
					str += LoadIntegerForComparison(tree.getChild(i).getChild(0).toString(), CP, LoadEvenZero, isRightZero);
			}
		}
		return str;
	}
	
	private String LoadWeakOperatorForComparison(HashMap<Integer, String> WeakOP) throws Exception
	{
		String str = "";
		
		int loop = WeakOP.size();
		for(int i = 0; i < loop; i++)
		{
			if(WeakOP.get(i).equals("+"))
				str += Bytecode.Iadd;
			else if(WeakOP.get(i).equals("-"))
				str += Bytecode.Isub;
			else throw new Exception("Unknown Exception at WeakOP" + WeakOP.get(i));
			
			WeakOP.remove(i);
		}
		
		return str;
	}
	
	private void StoreToStackMapTable(StackMapTable StackMap, ConstantPool CP, int Offset) throws Exception
	{
		if(StackMap.size() == 0)
			StackMap.add(new StackMapFrame(true, CP.IndexofClass("java/util/Scanner"), Offset, DeclaredVariables.size(), CP));
		
		else
		{
			StackMap.SortFrames();
			
			boolean isOverlapped = false;
			
			for(int j = 0; j < StackMap.size(); j++)
			{
				if(StackMap.get(j).getOffset() == Offset)
				{
					isOverlapped = true;
					break;
				}
			}
			
			if(isOverlapped == false)
				StackMap.add(new StackMapFrame(false, CP.IndexofClass("java/util/Scanner"), Offset, DeclaredVariables.size(), CP));
			else 
			{
				boolean isFirst = false;
				
				for(int j = 0; j < StackMap.size(); j++)
				{
					if(StackMap.get(j).getOffset() == Offset)
					{
						isFirst = StackMap.get(j).isFirst();
						break;
					}
				}
					
				StackMap.Modify(Offset, new StackMapFrame(isFirst, CP.IndexofClass("java/util/Scanner"), Offset, DeclaredVariables.size(), CP));
			}
		}
	}
	
	private boolean isInsideOfStatement(Tree tree)
	{
		if(Util.isConditional(tree.getParent().toString()) || Util.isDefiniteLoop(tree.getParent().toString()) || Util.isInfiniteLoop(tree.getParent().toString()))
			return true;
		else return false;
	}
	
	private void LoadPrintInt(ConstantPool CP)
	{
		if(CP.IndexofMethodref("java/io/PrintStream", "print", "(I)V") == -1)
			ReadyToPrintInt(CP);
			
		Code += Bytecode.Invokevirtual(CP.IndexofMethodref("java/io/PrintStream", "print", "(I)V")); //invokevirtual
	}
	
	private void LoadPrintString(ConstantPool CP)
	{
		if(CP.IndexofMethodref("java/io/PrintStream", "print", "(Ljava/lang/String;)V") == -1)
			ReadyToPrintString(CP);
			
		Code += Bytecode.Invokevirtual(CP.IndexofMethodref("java/io/PrintStream", "print", "(Ljava/lang/String;)V")); //invokevirtual
	}
	
	private void LoadPrintlnInt(ConstantPool CP)
	{
		if(CP.IndexofMethodref("java/io/PrintStream", "println", "(I)V") == -1)
			ReadyToPrintlnInt(CP);
		
		Code += Bytecode.Invokevirtual(CP.IndexofMethodref("java/io/PrintStream", "println", "(I)V")); //invokevirtual
	}
	
	private void LoadPrintlnString(ConstantPool CP)
	{
		if(CP.IndexofMethodref("java/io/PrintStream", "println", "(Ljava/lang/String;)V") == -1)
			ReadyToPrintlnString(CP);
		
		Code += Bytecode.Invokevirtual(CP.IndexofMethodref("java/io/PrintStream", "println", "(Ljava/lang/String;)V")); //invokevirtual
	}
	
	private void ReadyToPrintInt(ConstantPool CP)
	{
		if(CP.IndexofUTF8("(I)V") == -1)
			InitPrintData_IntType(CP);
		
		if(CP.IndexofUTF8("print") == -1)
		{
			int PrintNameIndex = CP.size() + 1;
			CP.put(PrintNameIndex, Constant.UTF8("print")); //print name
		}
		int PrintIntref = CP.size() + 1;
		
		CP.put(PrintIntref, Constant.Method(CP.IndexofClass("java/io/PrintStream"), PrintIntref + 1)); //print int ref
		CP.put(PrintIntref + 1, Constant.NameAndType(CP.IndexofUTF8("print"), CP.IndexofUTF8("(I)V"))); //print int name and type
	}
	
	private void ReadyToPrintString(ConstantPool CP)
	{
		if(CP.IndexofUTF8("(Ljava/lang/String;)V") == -1)
			InitPrintData_StringType(CP);
		
		if(CP.IndexofUTF8("print") == -1)
		{
			int PrintName = CP.size() + 1;
			CP.put(PrintName, Constant.UTF8("print")); //print name
		}
		int PrintStringref = CP.size() + 1;
		
		CP.put(PrintStringref, Constant.Method(CP.IndexofClass("java/io/PrintStream"), PrintStringref + 1)); //print int ref
		CP.put(PrintStringref + 1, Constant.NameAndType(CP.IndexofUTF8("print"), CP.IndexofUTF8("(Ljava/lang/String;)V"))); //print int name and type
	}
	
	private void ReadyToPrintlnInt(ConstantPool CP)
	{
		if(CP.IndexofUTF8("(I)V") == -1)
			InitPrintData_IntType(CP);
		
		if(CP.IndexofUTF8("println") == -1)
		{
			int PrintlnName = CP.size() + 1;
			CP.put(PrintlnName, Constant.UTF8("println")); //println Name
		}
		int PrintlnIntref = CP.size() + 1;
		
		CP.put(PrintlnIntref, Constant.Method(CP.IndexofClass("java/io/PrintStream"), PrintlnIntref + 1)); //println int ref
		CP.put(PrintlnIntref + 1, Constant.NameAndType(CP.IndexofUTF8("println"), CP.IndexofUTF8("(I)V"))); //println int name and type
	}
	
	private void ReadyToPrintlnString(ConstantPool CP)
	{
		if(CP.IndexofUTF8("(Ljava/lang/String;)V") == -1)
			InitPrintData_StringType(CP);
		
		if(CP.IndexofUTF8("println") == -1)
		{
			int PrintlnName = CP.size() + 1;
			CP.put(PrintlnName, Constant.UTF8("println")); //println Name
		}
		int PrintlnStringref = CP.size() + 1;
		
		CP.put(PrintlnStringref, Constant.Method(CP.IndexofClass("java/io/PrintStream"), PrintlnStringref + 1)); //println int ref
		CP.put(PrintlnStringref + 1, Constant.NameAndType(CP.IndexofUTF8("println"), CP.IndexofUTF8("(Ljava/lang/String;)V"))); //println int name and type
	}
	
	private void InitPrintData_IntType(ConstantPool CP)
	{
		int PrintData_IntType = CP.size() + 1;
		CP.put(PrintData_IntType, Constant.UTF8("(I)V")); //printdata int type
	}
	
	private void InitPrintData_StringType(ConstantPool CP)
	{
		int PrintData_StringType = CP.size() + 1;
		CP.put(PrintData_StringType, Constant.UTF8("(Ljava/lang/String;)V")); //printdata string type
	}
	
	private void InitPrintData(ConstantPool CP)
	{
		int SystemOutFieldref = CP.size() + 1;
		int SystemClassref = CP.IndexofClass("java/lang/System");
		int SystemClassName = CP.IndexofUTF8("java/lang/System");
		int PrintStreamClassref = CP.IndexofClass("java/io/PrintStream");
		int SystemOutFieldNameAndType;
		int SystemOutFieldName;
		int SystemOutFieldType;
		int PrintStreamName;
		boolean FisrtSystemClass = false;
		
		if(SystemClassref == -1)
		{
			SystemClassref = SystemOutFieldref + 1;
			SystemClassName = SystemOutFieldref + 2;
			SystemOutFieldNameAndType = SystemOutFieldref + 3;
			SystemOutFieldName = SystemOutFieldref + 4;
			SystemOutFieldType = SystemOutFieldref + 5;
			PrintStreamClassref = SystemOutFieldref + 6;
			PrintStreamName = SystemOutFieldref + 7;
			FisrtSystemClass = true;
		}
		else
		{
			SystemOutFieldNameAndType = SystemOutFieldref + 1;
			SystemOutFieldName = SystemOutFieldref + 2;
			SystemOutFieldType = SystemOutFieldref + 3;
			PrintStreamClassref =SystemOutFieldref + 4;
			PrintStreamName = SystemOutFieldref + 5;
		}			
		CP.put(SystemOutFieldref, Constant.Field(SystemClassref, SystemOutFieldNameAndType)); //System.out �ʵ� ref
		
		if(FisrtSystemClass == true)
		{
			CP.put(SystemOutFieldref + 1, Constant.Class(SystemClassName)); //System Ŭ���� ref
			CP.put(SystemOutFieldref + 2, Constant.UTF8("java/lang/System")); //System Ŭ���� �̸�
			CP.put(SystemOutFieldref + 3, Constant.NameAndType(SystemOutFieldName, SystemOutFieldType)); //System.out �ʵ��� Name and Type
			CP.put(SystemOutFieldref + 4, Constant.UTF8("out")); //System.out �ʵ��� �̸�
			CP.put(SystemOutFieldref + 5, Constant.UTF8("Ljava/io/PrintStream;")); //System.out �ʵ��� Ÿ��
			CP.put(SystemOutFieldref + 6, Constant.Class(PrintStreamName)); //PrintStream Ŭ����
			CP.put(SystemOutFieldref + 7, Constant.UTF8("java/io/PrintStream")); //Ŭ������ �̸�
		}
		else 
		{
			CP.put(SystemOutFieldref + 1, Constant.NameAndType(SystemOutFieldName, SystemOutFieldType)); //System.out �ʵ��� Name and Type
			CP.put(SystemOutFieldref + 2, Constant.UTF8("out")); //System.out �ʵ��� �̸�
			CP.put(SystemOutFieldref + 3, Constant.UTF8("Ljava/io/PrintStream;")); //System.out �ʵ��� Ÿ��
			CP.put(SystemOutFieldref + 4, Constant.Class(PrintStreamName)); //PrintStream Ŭ����
			CP.put(SystemOutFieldref + 5, Constant.UTF8("java/io/PrintStream")); //Ŭ������ �̸�
		}
	}
	
	private boolean hasInputStatement(Tree tree)
	{
		boolean hasInputStatement = false;
		for(int i = 0; i < tree.getChildCount(); i++)
		{
			if(Util.isInput(tree.getChild(i).toString()))
				return true;
			else hasInputStatement = hasInputStatement(tree.getChild(i));
		}
		return hasInputStatement;
	}
	
	private void CreateScanner(ConstantPool CP, LineNumberTable LineNumTable, int InitMethodNameIndex)
	{
		/*
		if(LineNumTable.containsStartPc(0)) //�̹� �ٸ� ������ �ִ� ���
		{
			LineNumberTable tempTable = new LineNumberTable();
			
			for(int i = 0; i <= 65535; i++)
			{
				if(LineNumTable.containsStartPc(i))
					tempTable.add(new LineNumberFrame(LineNumTable.get(i).getStartPc() + 11, LineNumTable.get(i).getLineNumber() + 1));
			}
			tempTable.add(new LineNumberFrame(0, 3));

			LineNumTable.CopyFrom(tempTable);
		}
		else */LineNumTable.add(new LineNumberFrame(0, ++LineNumber.value));
		
		int ScannerClassref = CP.size() + 1;
		int ScannerClassName = ScannerClassref + 1;
		int Scannerinitref = ScannerClassref + 2;
		int ScannerinitNameAndType = ScannerClassref + 3;
		int ScannerinitType = ScannerClassref + 4;
		int NextIntref = ScannerClassref + 5;
		int ScannerNextIntNameAndType = ScannerClassref + 6;
		int ScannerNextIntName = ScannerClassref + 7;
		int ScannerNextIntType = ScannerClassref + 8;
		
		CP.put(ScannerClassref, Constant.Class(ScannerClassName)); //Scanner Ŭ������ ����
		CP.put(ScannerClassref + 1, Constant.UTF8("java/util/Scanner")); //Scanner Ŭ������ �̸�
		CP.put(ScannerClassref + 2, Constant.Method(ScannerClassref, ScannerinitNameAndType)); //Scanner Ŭ������ init �޼ҵ��� ����
		CP.put(ScannerClassref + 3, Constant.NameAndType(InitMethodNameIndex, ScannerinitType)); //Scanner Ŭ������ init �޼ҵ��� Name and Type
		//0005�� �ٸ� Ŭ������ init�ε�, �̸��� ���� �����ε� ���� �ε����� ������
		CP.put(ScannerClassref + 4, Constant.UTF8("(Ljava/io/InputStream;)V")); //Scanner Ŭ������ init �޼ҵ��� �μ��� ��ȯŸ��
		CP.put(ScannerClassref + 5, Constant.Method(ScannerClassref, ScannerNextIntNameAndType)); //nextInt�� ����
		CP.put(ScannerClassref + 6, Constant.NameAndType(ScannerNextIntName, ScannerNextIntType)); //nextInt�� Name and Type
		CP.put(ScannerClassref + 7, Constant.UTF8("nextInt")); //nextInt�� Name
		CP.put(ScannerClassref + 8, Constant.UTF8("()I")); //nextInt�� �μ��� ��ȯŸ��
		
		int SystemInFieldref = CP.size() + 1;
		int SystemInFieldNameAndType;
		int SystemInFieldName;
		int SystemInFieldType;
		boolean FisrtSystemClass = false;
		
		int SystemClassref = CP.IndexofClass("java/lang/System");
		int SystemClassName = CP.IndexofUTF8("java/lang/System");
		
		if(SystemClassref == -1)
		{
			SystemClassref = SystemInFieldref + 1;
			SystemClassName = SystemInFieldref + 2;
			SystemInFieldNameAndType = SystemInFieldref + 3;
			SystemInFieldName = SystemInFieldref + 4;
			SystemInFieldType = SystemInFieldref + 5;
			FisrtSystemClass = true;
		}
		else
		{
			SystemInFieldNameAndType = SystemInFieldref + 1;
			SystemInFieldName = SystemInFieldref + 2;
			SystemInFieldType = SystemInFieldref + 3;
		}		
		CP.put(SystemInFieldref, Constant.Field(SystemClassref, SystemInFieldNameAndType)); //System.in �ʵ��� ����
		
		if(FisrtSystemClass == true)
		{
			CP.put(SystemInFieldref + 1, Constant.Class(SystemClassName)); //System Ŭ������ ����
			CP.put(SystemInFieldref + 2, Constant.UTF8("java/lang/System")); //System Ŭ������ �̸�
			CP.put(SystemInFieldref + 3, Constant.NameAndType(SystemInFieldName, SystemInFieldType)); //System.in �ʵ��� Name and Type
			CP.put(SystemInFieldref + 4, Constant.UTF8("in")); //System.in �ʵ��� �̸�
			CP.put(SystemInFieldref + 5, Constant.UTF8("Ljava/io/InputStream;")); //System.in �ʵ��� Ÿ��
		}
		else
		{
			CP.put(SystemInFieldref + 1, Constant.NameAndType(SystemInFieldName, SystemInFieldType)); //System.in �ʵ��� Name and Type
			CP.put(SystemInFieldref + 2, Constant.UTF8("in")); //System.in �ʵ��� �̸�
			CP.put(SystemInFieldref + 3, Constant.UTF8("Ljava/io/InputStream;")); //System.in �ʵ��� Ÿ��
		}
		
		String temp = Bytecode.New(ScannerClassref); //new java/util/Scanner
		temp += Bytecode.Dup; //dup
		temp += Bytecode.Getstatic(SystemInFieldref); //getstatic java/lang/System.in
		temp += Bytecode.Invokespecial(Scannerinitref); //invokespecial java/util/Scanner.<init>
		temp += Bytecode.Astore_1; //astore_1
		
		Code = temp + Code;
	}
}
