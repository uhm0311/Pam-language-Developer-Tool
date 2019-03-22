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
		DeclaredVariables.add("첫번째"); //의미없음. 인덱스 맞춰주기 위해 넣음
		
		hasInputStatement.value = hasInputStatement(tree);
		if(hasInputStatement.value == true)
		{
			DeclaredVariables.add("스캐너"); //1번 인덱스에 scanner를 넣는다.
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
			
			else if(Util.isConditional(identifier)) //코드 생성은 제대로 되는것같다. StackMapTable 좀더 지켜봐야 한다.
			{
				boolean hasElse = false; //else가 있는가
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
				
				Bool isRightZero = new Bool(false); //으론쪽이 상수 0인가. 계산의 결과로 0인 경우는 제외.
				
				//왼쪽은 무조건 불러온다. 오른쪾은 0일경우 불러오지 않는다.
				if(Util.isElement(LeftExpression.getChild(0).toString())) //왼쪽이 엘리먼트일 경우
				{					
					Tree LeftElement = LeftExpression.getChild(0);
					IfStatement += LoadIntegerForComparison(LeftElement.getChild(0).toString(), CP, true, isRightZero);
				}
				else //엘리먼트가 아니면 계산을 해야 한다.
					IfStatement += CalculateForComparison(LeftExpression, CP, LineNumTable, true, isRightZero);
				
				if(Util.isElement(RightExpression.getChild(0).toString())) //왼쪽이 엘리먼트일 경우
				{					
					Tree RightElement = RightExpression.getChild(0);
					IfStatement += LoadIntegerForComparison(RightElement.getChild(0).toString(), CP, false, isRightZero);
				}
				else //계산해야한다.
					IfStatement += CalculateForComparison(RightExpression, CP, LineNumTable, false, isRightZero);
				
				switch (Relationship) //항상 바이트코드와 실제 케이스는 반대다. 
				{ //오른쪽이 상수 0인 경우, 불러오지 않으므로 스택 오퍼랜드를 하나만 사용하는 바이트코드를 쓴다. 그 외의 경우 스택 오퍼랜드를 두개 쓰는 바이트코드를 쓴다.
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
				//If의 Goto는 (IfStatement의 예상 크기 + OffsetCode의 크기) / 2가 된다.
				//Else의 Goto는 (GotoStatement의 크기(3바이트) + ElseOffsetCode의 크기) / 2가 된다.
				//If의 Offset은 (코드 길이 + Ifstatement 길이) / 2 + Goto가 된다.
				//Else의 Offset은 If의 Offset + (ElseInsideCode 길이) / 2가 된다.
				
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
				
				ForStatement += LoadIntegerForComparison(Flag, CP, true, new Bool(false)); //Flag를 불러온다.
				
				if(Util.isElement(Expression.getChild(0).toString())) //수식이 아닌 경우
				{
					Tree Element = Expression.getChild(0);
					ForStatement += LoadIntegerForComparison(Element.getChild(0).toString(), CP, false, new Bool(false));
				}
				else
					ForStatement += CalculateForComparison(Expression, CP, LineNumTable, true, new Bool(false));
				
				Parsertree2Bytecode p = new Parsertree2Bytecode(Code + ForStatement + Util.StretchLenth("0", 6), DeclaredVariables, hasInputStatement, Statements, LineNumber, InsideCodeDepth, CP, MaxStack, MaxLocal, hasStackMapTable, LineNumTable, StackMap, InitMethodNameIndex);
				String InsideCode = p.getCode(true);
				InsideCode += Bytecode.iinc(DeclaredVariables.indexOf(Flag), 1);
				
				int Goto = (InsideCode.length() + 12) / 2; //iinc, goto문
				int Offset = ((Code + ForStatement).length() / 2) + Goto;
				int GoBackto = LoopIndex - Offset + 3; //3은 goto 인덱스 총합 3바이트를 의미
				String GotoStatement = Bytecode.Goto(GoBackto);

				ForStatement += Bytecode.If_icmpge(Goto); //항상 i가 더 작은 것이 조건이므로 ge
				
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

				if(Util.isElement(LeftExpression.getChild(0).toString())) //왼쪽이 엘리먼트일 경우
				{					
					Tree LeftElement = LeftExpression.getChild(0);
					WhileStatement += LoadIntegerForComparison(LeftElement.getChild(0).toString(), CP, true, isRightZero);
				}
				else //엘리먼트가 아니면 계산을 해야 한다.
					WhileStatement += CalculateForComparison(LeftExpression, CP, LineNumTable, true, isRightZero);
				
				if(Util.isElement(RightExpression.getChild(0).toString())) //왼쪽이 엘리먼트일 경우
				{					
					Tree RightElement = RightExpression.getChild(0);
					WhileStatement += LoadIntegerForComparison(RightElement.getChild(0).toString(), CP, false, isRightZero);
				}
				else //계산해야한다.
					WhileStatement += CalculateForComparison(RightExpression, CP, LineNumTable, true, isRightZero);
				
				String IfCode = "";
				
				switch (Relationship) //항상 바이트코드와 실제 케이스는 반대다. 
				{ //오른쪽이 상수 0인 경우, 불러오지 않으므로 스택 오퍼랜드를 하나만 사용하는 바이트코드를 쓴다. 그 외의 경우 스택 오퍼랜드를 두개 쓰는 바이트코드를 쓴다.
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
				
				int Goto = (InsideCode.length() + 12) / 2; //goto문
				int Offset = ((Code + WhileStatement).length() / 2) + Goto;
				//StackMapTable의 Offset은 Goto의 다음을 가리킨다. Definite에선 iinc가 있어서 12로 했을때 자동으로 Goto 다음을 가리키는데, 여기선 iinc가 없어도 StackMapTable을 위해 +12로 한다.
				int GoBackto = LoopIndex - Offset + 3; //위와 같은 이유로 +3
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

				//대입되는 숫자 혹은 식을 먼저 만들어야 한다.
				if(Expression.getChild(0).getChildCount() == 1) //익스프레션의 자식은 항상 하나뿐. 자식의 자식이 하나라면 이는 상수나 변수를 대입하는 것이다.
				{
					String AssignValue = Expression.getChild(0).getChild(0).toString();
					
					LoadInteger(AssignValue, CP, LineNumTable);
					StoreInteger(Variable, true, MaxStack, MaxLocal, LineNumTable);
				}
				else //한개가 아닌 경우. 그러면 계산을 먼저 해야 한다.
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
						Code += Bytecode.Aload_1; //무조건 aload_1이다.
						Code += Bytecode.Invokevirtual(CP.IndexofMethodref("java/util/Scanner", "nextInt", "()I")); //invokevirtual nextInt()
						//invokevirtual로 nextInt를 불러왔음. 이제 istore를 해주면됨.
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
							
							if(StringrefIndex == -1) //CP에 Stringref를 넣어줘야 하는 경우
							{
								StringrefIndex = CP.size() + 1;
								
								if(UTF8index == -1) //CP에 UTF8을 넣어줘야 하는 경우
								{
									UTF8index = StringrefIndex + 1;
									CP.put(UTF8index, Constant.UTF8(str));
								}
								CP.put(StringrefIndex, Constant.String(UTF8index)); //string ref info
							}
							
							Code += Bytecode.Ldc(StringrefIndex); //ldc 인덱스 한바이트
						}
						else if(Util.isVariable(OutputList.getChild(j).toString()))
						{
							String Variable = OutputList.getChild(j).toString();
							int index = DeclaredVariables.indexOf(Variable);
							
							if(index < 0)
								throw new Exception("초기화 되지 않은 변수입니다." + Variable + " " + index);
							else LoadInteger(Variable, CP, LineNumTable);
						}
						else throw new Exception("정수 리터럴은 출력할 수 없습니다.");
						
						if(j + 2 == OutputList.getChildCount()) //마지막일 경우. +2인 이유는 세미콜론 때문이다.
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
						
						else //마지막이 아닐 경우
						{
							if(isStr == true) LoadPrintString(CP);
							else LoadPrintInt(CP);
							
							LineNumTable.add(new LineNumberFrame(Code.length() / 2, ++LineNumber.value));
							Code += Bytecode.Getstatic(CP.IndexofField("java/lang/System", "out", "Ljava/io/PrintStream;")); //getstatic System.out
							
							int Blankindex = CP.IndexofString(" ");
							
							if(Blankindex == -1) //CP에 넣어줘야 하는 경우
							{
								Blankindex = CP.size() + 1;
								
								CP.put(Blankindex, Constant.String(Blankindex + 1)); //string ref info
								CP.put(Blankindex + 1, Constant.UTF8(" "));
							}
							Code += Bytecode.Ldc(Blankindex); //ldc 인덱스 한바이트
							
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
		if(Util.isVariable(Value)) //변수일 경우
		{
			int index = DeclaredVariables.indexOf(Value);
			
			if(index > 3)
				Code += Bytecode.Iload(index); //iload + 인덱스 한바이트
			
			else switch(index)
			{
			case 1: Code += Bytecode.Iload_1; break; //iload_1
			case 2: Code += Bytecode.Iload_2; break; //iload_2
			case 3: Code += Bytecode.Iload_3; break; //iload_3
			default : throw new Exception("Unknown Exception at LoadInteger. Variable index : " + index);
			}
		}
		else if(Util.isConstant(Value)) //Constant인 경우
		{
			int Const = Integer.parseInt(Value);
			
			if(Const == -1)
				Code += Bytecode.Iconst_m1;
			
			else if(Const >= 0 && Const <= 5) //0~5는 iconst가 있다.
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
			//127까지는 bipush 값한바이트
			else if(Const >= Byte.MIN_VALUE && Const <= Short.MAX_VALUE) 
				Code += Bytecode.Bipush(Const);
			//32767까지는 sipush 값두바이트
			else if(Const >= Short.MIN_VALUE && Const <= Short.MAX_VALUE) 
				Code += Bytecode.Sipush(Const);
			//32768부터는 CP에 넣어서 ldc로 불러온 뒤 istore를 한다. int의 cp 태그는 03이고, 값을 16진수로 바꾸어 8자리(4바이트)로 해야 한다.
			else
			{
				int intrefIndex = CP.IndexofInteger(Const);
				
				if(intrefIndex == -1) //없는 경우
				{
					intrefIndex = CP.size() + 1;
					CP.put(intrefIndex, Constant.Integer(Const));
				}
				Code += Bytecode.Ldc(intrefIndex); //인덱스값 한바이트
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
			Code += Bytecode.Istore(index); //istore + 인덱스 한바이트
		
		else switch(index)
		{
		case 1: Code += Bytecode.Istore_1; break; //istore_1
		case 2: Code += Bytecode.Istore_2; break; //istore_2
		case 3: Code += Bytecode.Istore_3; break; //istore_3
		default : throw new Exception("Unknown Exception at StoreInteger. Variable index : " + index); //0이거나 1일 수가 없다.
		}
		MaxStack.value++;
		
		if(AddToLineNumberTable == true)
			LineNumTable.add(new LineNumberFrame(Code.length() / 2, ++LineNumber.value));
	}
	
	private void Calculate(Tree Expression, ConstantPool CP, LineNumberTable LineNumTable) throws Exception
	{
		HashMap<Integer, String> WeakOP = new HashMap<Integer, String>(); //덧셈 뺄셈의 순서와 기호
		HashMap<Integer, String> StrongOP = new HashMap<Integer, String>(); //곱셈 나눗셈의 순서와 기호
		
		for(int i = 0; i < Expression.getChildCount(); i++)
			LoadStrongInteger(Expression.getChild(i), WeakOP, StrongOP, CP, LineNumTable);
		LoadStrongOperator(StrongOP);
		
		for(int i = 0; i < Expression.getChildCount(); i++)
			LoadWeakInteger(Expression.getChild(i), WeakOP, StrongOP, CP, LineNumTable);
		LoadWeakOperator(WeakOP);
	}
	
	
	private void LoadStrongInteger(Tree tree, HashMap<Integer, String> WeakOP, HashMap<Integer, String> StrongOP, ConstantPool CP, LineNumberTable LineNumTable) throws Exception
	{
		if(Util.isMultiplicative(tree.toString())) //tree 자체가 Multiplicative이다.
		{
			for(int i = 0; i < tree.getChildCount(); i++)
			{
				if(Util.isAdditive(tree.getChild(i).toString())) //tree에게 Addtive 자식이 i번째에 있는 경우
				{
					LoadStrongOperator(StrongOP);
					LoadWeakInteger(tree.getChild(i), WeakOP, StrongOP, CP, LineNumTable);
					LoadWeakOperator(WeakOP);
				}
				else if(Util.isMultiplicative(tree.getChild(i).toString())) //tree에게 Multiplicative 자식이 있는 경우.
					LoadStrongInteger(tree.getChild(i), WeakOP, StrongOP, CP, LineNumTable);
				
				else if(tree.getChild(i).toString().equals("*"))
					StrongOP.put(StrongOP.size(), "*");
				else if(tree.getChild(i).toString().equals("/"))
					StrongOP.put(StrongOP.size(), "/");
				
				else if(Util.isElement(tree.getChild(i).toString())) //엘리먼트일때
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
		if(Util.isAdditive(tree.toString())) //tree 자체가 Additive이다.
		{
			for(int i = 0; i < tree.getChildCount(); i++)
			{
				if(Util.isAdditive(tree.getChild(i).toString())) //tree에게 Addtive 자식이 i번째에 있는 경우
					LoadWeakInteger(tree.getChild(i), WeakOP, StrongOP, CP, LineNumTable);
				else if(Util.isMultiplicative(tree.getChild(i).toString())) //tree에게 Multiplicative 자식이 있는 경우.
				{
					LoadStrongInteger(tree.getChild(i), WeakOP, StrongOP, CP, LineNumTable);
					LoadStrongOperator(StrongOP);
					LoadWeakOperator(WeakOP);
				}
				
				else if(tree.getChild(i).toString().equals("+"))
					WeakOP.put(WeakOP.size(), "+");
				else if(tree.getChild(i).toString().equals("-"))
					WeakOP.put(WeakOP.size(), "-");
				
				else if(Util.isElement(tree.getChild(i).toString())) //엘리먼트일때
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
				throw new Exception("초기화 되지 않은 변수입니다. " + Element + " " + index);
			else
			{
				if(index > 3)
					str += Bytecode.Iload(index); //iload + 인덱스 한바이트
				
				else switch(index)
				{
				case 1: str += Bytecode.Iload_1; break; //iload_1
				case 2: str += Bytecode.Iload_2; break; //iload_2
				case 3: str += Bytecode.Iload_3; break; //iload_3
				default : throw new Exception("Unknown Load Exception at Comparison. Variable index : " + index); //0이거나 1일 수가 없다.
				}
			}
		}
		else 
		{
			int Const = Integer.parseInt(Element);
			
			if(Const == -1)
				str += Bytecode.Iconst_m1;
			
			else if(Const >= 0 && Const <= 5) //0~5는 iconst가 있다.
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
			//127까지는 bipush 값한바이트
			else if(Const >= Byte.MIN_VALUE && Const <= Byte.MAX_VALUE) 
				str += Bytecode.Bipush(Const);
			//32767까지는 sipush 값두바이트
			else if(Const >= Short.MIN_VALUE && Const <= Short.MAX_VALUE) 
				str += Bytecode.Sipush(Const);
			//32768부터는 CP에 넣어서 ldc로 불러온 뒤 istore를 한다. int의 cp 태그는 03이고, 값을 16진수로 바꾸어 8자리(4바이트)로 해야 한다.
			else
			{
				int intrefIndex = CP.IndexofInteger(Const);
				
				if(intrefIndex == -1) //없는 경우
				{
					intrefIndex = CP.size() + 1;
					CP.put(intrefIndex, Constant.Integer(Const));
				}
				str += Bytecode.Ldc(intrefIndex); //인덱스값 한바이트
			}
		}
		return str;
	}
	
	private String CalculateForComparison(Tree Expression, ConstantPool CP, LineNumberTable LineNumTable, boolean LoadEvenZero, Bool isRightZero) throws Exception
	{
		String str = "";
		
		HashMap<Integer, String> WeakOP = new HashMap<Integer, String>(); //덧셈 뺄셈의 순서와 기호
		HashMap<Integer, String> StrongOP = new HashMap<Integer, String>(); //곱셈 나눗셈의 순서와 기호
		
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
		
		if(Util.isMultiplicative(tree.toString())) //tree 자체가 Multiplicative이다.
		{
			for(int i = 0; i < tree.getChildCount(); i++)
			{
				if(Util.isAdditive(tree.getChild(i).toString())) //tree에게 Addtive 자식이 i번째에 있는 경우
				{
					str += LoadStrongOperatorForComparison(StrongOP);
					str += LoadWeakIntegerForComparison(tree.getChild(i), WeakOP, StrongOP, CP, LineNumTable, LoadEvenZero, isRightZero);
					str += LoadWeakOperatorForComparison(WeakOP);
				}
				else if(Util.isMultiplicative(tree.getChild(i).toString())) //tree에게 Multiplicative 자식이 있는 경우.
					str += LoadStrongIntegerForComparison(tree.getChild(i), WeakOP, StrongOP, CP, LineNumTable, LoadEvenZero, isRightZero);
				
				else if(tree.getChild(i).toString().equals("*"))
					StrongOP.put(StrongOP.size(), "*");
				else if(tree.getChild(i).toString().equals("/"))
					StrongOP.put(StrongOP.size(), "/");
				
				else if(Util.isElement(tree.getChild(i).toString())) //엘리먼트일때
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
		
		if(Util.isAdditive(tree.toString())) //tree 자체가 Additive이다.
		{
			for(int i = 0; i < tree.getChildCount(); i++)
			{
				if(Util.isAdditive(tree.getChild(i).toString())) //tree에게 Addtive 자식이 i번째에 있는 경우
					str += LoadWeakIntegerForComparison(tree.getChild(i), WeakOP, StrongOP, CP, LineNumTable, LoadEvenZero, isRightZero);
				else if(Util.isMultiplicative(tree.getChild(i).toString())) //tree에게 Multiplicative 자식이 있는 경우.
				{
					str += LoadStrongIntegerForComparison(tree.getChild(i), WeakOP, StrongOP, CP, LineNumTable, LoadEvenZero, isRightZero);
					str += LoadStrongOperatorForComparison(StrongOP);
					str += LoadWeakOperatorForComparison(WeakOP);
				}
				
				else if(tree.getChild(i).toString().equals("+"))
					WeakOP.put(WeakOP.size(), "+");
				else if(tree.getChild(i).toString().equals("-"))
					WeakOP.put(WeakOP.size(), "-");
				
				else if(Util.isElement(tree.getChild(i).toString())) //엘리먼트일때
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
		CP.put(SystemOutFieldref, Constant.Field(SystemClassref, SystemOutFieldNameAndType)); //System.out 필드 ref
		
		if(FisrtSystemClass == true)
		{
			CP.put(SystemOutFieldref + 1, Constant.Class(SystemClassName)); //System 클래스 ref
			CP.put(SystemOutFieldref + 2, Constant.UTF8("java/lang/System")); //System 클래스 이름
			CP.put(SystemOutFieldref + 3, Constant.NameAndType(SystemOutFieldName, SystemOutFieldType)); //System.out 필드의 Name and Type
			CP.put(SystemOutFieldref + 4, Constant.UTF8("out")); //System.out 필드의 이름
			CP.put(SystemOutFieldref + 5, Constant.UTF8("Ljava/io/PrintStream;")); //System.out 필드의 타입
			CP.put(SystemOutFieldref + 6, Constant.Class(PrintStreamName)); //PrintStream 클래스
			CP.put(SystemOutFieldref + 7, Constant.UTF8("java/io/PrintStream")); //클래스의 이름
		}
		else 
		{
			CP.put(SystemOutFieldref + 1, Constant.NameAndType(SystemOutFieldName, SystemOutFieldType)); //System.out 필드의 Name and Type
			CP.put(SystemOutFieldref + 2, Constant.UTF8("out")); //System.out 필드의 이름
			CP.put(SystemOutFieldref + 3, Constant.UTF8("Ljava/io/PrintStream;")); //System.out 필드의 타입
			CP.put(SystemOutFieldref + 4, Constant.Class(PrintStreamName)); //PrintStream 클래스
			CP.put(SystemOutFieldref + 5, Constant.UTF8("java/io/PrintStream")); //클래스의 이름
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
		if(LineNumTable.containsStartPc(0)) //이미 다른 변수가 있는 경우
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
		
		CP.put(ScannerClassref, Constant.Class(ScannerClassName)); //Scanner 클래스의 정보
		CP.put(ScannerClassref + 1, Constant.UTF8("java/util/Scanner")); //Scanner 클래스의 이름
		CP.put(ScannerClassref + 2, Constant.Method(ScannerClassref, ScannerinitNameAndType)); //Scanner 클래스의 init 메소드의 정보
		CP.put(ScannerClassref + 3, Constant.NameAndType(InitMethodNameIndex, ScannerinitType)); //Scanner 클래스의 init 메소드의 Name and Type
		//0005는 다른 클래스의 init인데, 이름이 같고 실제로도 같은 인덱스르 참조함
		CP.put(ScannerClassref + 4, Constant.UTF8("(Ljava/io/InputStream;)V")); //Scanner 클래스의 init 메소드의 인수와 반환타입
		CP.put(ScannerClassref + 5, Constant.Method(ScannerClassref, ScannerNextIntNameAndType)); //nextInt의 정보
		CP.put(ScannerClassref + 6, Constant.NameAndType(ScannerNextIntName, ScannerNextIntType)); //nextInt의 Name and Type
		CP.put(ScannerClassref + 7, Constant.UTF8("nextInt")); //nextInt의 Name
		CP.put(ScannerClassref + 8, Constant.UTF8("()I")); //nextInt의 인수와 반환타입
		
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
		CP.put(SystemInFieldref, Constant.Field(SystemClassref, SystemInFieldNameAndType)); //System.in 필드의 정보
		
		if(FisrtSystemClass == true)
		{
			CP.put(SystemInFieldref + 1, Constant.Class(SystemClassName)); //System 클래스의 정보
			CP.put(SystemInFieldref + 2, Constant.UTF8("java/lang/System")); //System 클래스의 이름
			CP.put(SystemInFieldref + 3, Constant.NameAndType(SystemInFieldName, SystemInFieldType)); //System.in 필드의 Name and Type
			CP.put(SystemInFieldref + 4, Constant.UTF8("in")); //System.in 필드의 이름
			CP.put(SystemInFieldref + 5, Constant.UTF8("Ljava/io/InputStream;")); //System.in 필드의 타입
		}
		else
		{
			CP.put(SystemInFieldref + 1, Constant.NameAndType(SystemInFieldName, SystemInFieldType)); //System.in 필드의 Name and Type
			CP.put(SystemInFieldref + 2, Constant.UTF8("in")); //System.in 필드의 이름
			CP.put(SystemInFieldref + 3, Constant.UTF8("Ljava/io/InputStream;")); //System.in 필드의 타입
		}
		
		String temp = Bytecode.New(ScannerClassref); //new java/util/Scanner
		temp += Bytecode.Dup; //dup
		temp += Bytecode.Getstatic(SystemInFieldref); //getstatic java/lang/System.in
		temp += Bytecode.Invokespecial(Scannerinitref); //invokespecial java/util/Scanner.<init>
		temp += Bytecode.Astore_1; //astore_1
		
		Code = temp + Code;
	}
}
