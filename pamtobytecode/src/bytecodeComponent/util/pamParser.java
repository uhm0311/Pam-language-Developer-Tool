package bytecodeComponent.util;

// Generated from C:\Users\Administrator\Google 드라이브\단국대\프로그래밍\ANTLR\workspace\pam to bytecode\pam.g4 by ANTLR 4.1
import java.util.List;

import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNSimulator;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class pamParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, Semicolon=2, Read=3, WriteLine=4, Write=5, Comma=6, Assignment=7, 
		If=8, Then=9, Fi=10, Else=11, To=12, Do=13, While=14, End=15, String=16, 
		Variable=17, Constant=18, WeakOperator=19, StrongOperator=20, Relation=21;
	public static final String[] tokenNames = {
		"<INVALID>", "WS", "';'", "'read'", "'writeline'", "'write'", "','", "':='", 
		"'if'", "'then'", "'fi'", "'else'", "'to'", "'do'", "'while'", "'end'", 
		"String", "Variable", "Constant", "WeakOperator", "StrongOperator", "Relation"
	};
	public static final int
		RULE_program = 0, RULE_statements = 1, RULE_inputStatement = 2, RULE_outputStatement = 3, 
		RULE_inputList = 4, RULE_outputList = 5, RULE_assignmentStatement = 6, 
		RULE_conditionalStatement = 7, RULE_definiteLoop = 8, RULE_indefiniteLoop = 9, 
		RULE_comparison = 10, RULE_element = 11, RULE_expression = 12, RULE_additive_expression = 13, 
		RULE_multiplicative_expression = 14;
	public static final String[] ruleNames = {
		"program", "statements", "inputStatement", "outputStatement", "inputList", 
		"outputList", "assignmentStatement", "conditionalStatement", "definiteLoop", 
		"indefiniteLoop", "comparison", "element", "expression", "additive_expression", 
		"multiplicative_expression"
	};

	@Override
	public String getGrammarFileName() { return "pam.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public pamParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgramContext extends ParserRuleContext {
		public StatementsContext statements() {
			return getRuleContext(StatementsContext.class,0);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(30); statements();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementsContext extends ParserRuleContext {
		public DefiniteLoopContext definiteLoop(int i) {
			return getRuleContext(DefiniteLoopContext.class,i);
		}
		public List<AssignmentStatementContext> assignmentStatement() {
			return getRuleContexts(AssignmentStatementContext.class);
		}
		public IndefiniteLoopContext indefiniteLoop(int i) {
			return getRuleContext(IndefiniteLoopContext.class,i);
		}
		public List<IndefiniteLoopContext> indefiniteLoop() {
			return getRuleContexts(IndefiniteLoopContext.class);
		}
		public ConditionalStatementContext conditionalStatement(int i) {
			return getRuleContext(ConditionalStatementContext.class,i);
		}
		public OutputStatementContext outputStatement(int i) {
			return getRuleContext(OutputStatementContext.class,i);
		}
		public AssignmentStatementContext assignmentStatement(int i) {
			return getRuleContext(AssignmentStatementContext.class,i);
		}
		public List<OutputStatementContext> outputStatement() {
			return getRuleContexts(OutputStatementContext.class);
		}
		public List<DefiniteLoopContext> definiteLoop() {
			return getRuleContexts(DefiniteLoopContext.class);
		}
		public List<ConditionalStatementContext> conditionalStatement() {
			return getRuleContexts(ConditionalStatementContext.class);
		}
		public InputStatementContext inputStatement(int i) {
			return getRuleContext(InputStatementContext.class,i);
		}
		public List<InputStatementContext> inputStatement() {
			return getRuleContexts(InputStatementContext.class);
		}
		public StatementsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statements; }
	}

	public final StatementsContext statements() throws RecognitionException {
		StatementsContext _localctx = new StatementsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statements);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(38);
				switch (_input.LA(1)) {
				case Read:
					{
					setState(32); inputStatement();
					}
					break;
				case WriteLine:
				case Write:
					{
					setState(33); outputStatement();
					}
					break;
				case To:
					{
					setState(34); definiteLoop();
					}
					break;
				case While:
					{
					setState(35); indefiniteLoop();
					}
					break;
				case Variable:
					{
					setState(36); assignmentStatement();
					}
					break;
				case If:
					{
					setState(37); conditionalStatement();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(40); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Read) | (1L << WriteLine) | (1L << Write) | (1L << If) | (1L << To) | (1L << While) | (1L << Variable))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InputStatementContext extends ParserRuleContext {
		public InputListContext inputList() {
			return getRuleContext(InputListContext.class,0);
		}
		public TerminalNode Read() { return getToken(pamParser.Read, 0); }
		public InputStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inputStatement; }
	}

	public final InputStatementContext inputStatement() throws RecognitionException {
		InputStatementContext _localctx = new InputStatementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_inputStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42); match(Read);
			setState(43); inputList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OutputStatementContext extends ParserRuleContext {
		public TerminalNode WriteLine() { return getToken(pamParser.WriteLine, 0); }
		public TerminalNode Write() { return getToken(pamParser.Write, 0); }
		public OutputListContext outputList() {
			return getRuleContext(OutputListContext.class,0);
		}
		public OutputStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_outputStatement; }
	}

	public final OutputStatementContext outputStatement() throws RecognitionException {
		OutputStatementContext _localctx = new OutputStatementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_outputStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45);
			_la = _input.LA(1);
			if ( !(_la==WriteLine || _la==Write) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(46); outputList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InputListContext extends ParserRuleContext {
		public TerminalNode Variable(int i) {
			return getToken(pamParser.Variable, i);
		}
		public TerminalNode Semicolon() { return getToken(pamParser.Semicolon, 0); }
		public List<TerminalNode> Variable() { return getTokens(pamParser.Variable); }
		public TerminalNode Comma(int i) {
			return getToken(pamParser.Comma, i);
		}
		public List<TerminalNode> Comma() { return getTokens(pamParser.Comma); }
		public InputListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inputList; }
	}

	public final InputListContext inputList() throws RecognitionException {
		InputListContext _localctx = new InputListContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_inputList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48); match(Variable);
			setState(53);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(49); match(Comma);
				setState(50); match(Variable);
				}
				}
				setState(55);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(56); match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OutputListContext extends ParserRuleContext {
		public TerminalNode Variable(int i) {
			return getToken(pamParser.Variable, i);
		}
		public List<TerminalNode> String() { return getTokens(pamParser.String); }
		public TerminalNode Semicolon() { return getToken(pamParser.Semicolon, 0); }
		public List<TerminalNode> Variable() { return getTokens(pamParser.Variable); }
		public TerminalNode String(int i) {
			return getToken(pamParser.String, i);
		}
		public TerminalNode Comma(int i) {
			return getToken(pamParser.Comma, i);
		}
		public List<TerminalNode> Comma() { return getTokens(pamParser.Comma); }
		public OutputListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_outputList; }
	}

	public final OutputListContext outputList() throws RecognitionException {
		OutputListContext _localctx = new OutputListContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_outputList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			_la = _input.LA(1);
			if ( !(_la==String || _la==Variable) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(63);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(59); match(Comma);
				setState(60);
				_la = _input.LA(1);
				if ( !(_la==String || _la==Variable) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
				}
				setState(65);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(66); match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssignmentStatementContext extends ParserRuleContext {
		public TerminalNode Semicolon() { return getToken(pamParser.Semicolon, 0); }
		public TerminalNode Variable() { return getToken(pamParser.Variable, 0); }
		public TerminalNode Assignment() { return getToken(pamParser.Assignment, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AssignmentStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignmentStatement; }
	}

	public final AssignmentStatementContext assignmentStatement() throws RecognitionException {
		AssignmentStatementContext _localctx = new AssignmentStatementContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_assignmentStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68); match(Variable);
			setState(69); match(Assignment);
			setState(70); expression();
			setState(71); match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConditionalStatementContext extends ParserRuleContext {
		public List<StatementsContext> statements() {
			return getRuleContexts(StatementsContext.class);
		}
		public TerminalNode Else() { return getToken(pamParser.Else, 0); }
		public ComparisonContext comparison() {
			return getRuleContext(ComparisonContext.class,0);
		}
		public TerminalNode If() { return getToken(pamParser.If, 0); }
		public TerminalNode Fi() { return getToken(pamParser.Fi, 0); }
		public StatementsContext statements(int i) {
			return getRuleContext(StatementsContext.class,i);
		}
		public TerminalNode Then() { return getToken(pamParser.Then, 0); }
		public ConditionalStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionalStatement; }
	}

	public final ConditionalStatementContext conditionalStatement() throws RecognitionException {
		ConditionalStatementContext _localctx = new ConditionalStatementContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_conditionalStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73); match(If);
			setState(74); comparison();
			setState(75); match(Then);
			setState(76); statements();
			setState(79);
			_la = _input.LA(1);
			if (_la==Else) {
				{
				setState(77); match(Else);
				setState(78); statements();
				}
			}

			setState(81); match(Fi);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DefiniteLoopContext extends ParserRuleContext {
		public TerminalNode Variable() { return getToken(pamParser.Variable, 0); }
		public StatementsContext statements() {
			return getRuleContext(StatementsContext.class,0);
		}
		public TerminalNode Do() { return getToken(pamParser.Do, 0); }
		public TerminalNode End() { return getToken(pamParser.End, 0); }
		public TerminalNode To() { return getToken(pamParser.To, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public DefiniteLoopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definiteLoop; }
	}

	public final DefiniteLoopContext definiteLoop() throws RecognitionException {
		DefiniteLoopContext _localctx = new DefiniteLoopContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_definiteLoop);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(83); match(To);
			setState(86);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(84); expression();
				}
				break;

			case 2:
				{
				setState(85); match(Variable);
				}
				break;
			}
			setState(88); match(Do);
			setState(89); statements();
			setState(90); match(End);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IndefiniteLoopContext extends ParserRuleContext {
		public TerminalNode While() { return getToken(pamParser.While, 0); }
		public StatementsContext statements() {
			return getRuleContext(StatementsContext.class,0);
		}
		public TerminalNode Do() { return getToken(pamParser.Do, 0); }
		public TerminalNode End() { return getToken(pamParser.End, 0); }
		public ComparisonContext comparison() {
			return getRuleContext(ComparisonContext.class,0);
		}
		public IndefiniteLoopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_indefiniteLoop; }
	}

	public final IndefiniteLoopContext indefiniteLoop() throws RecognitionException {
		IndefiniteLoopContext _localctx = new IndefiniteLoopContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_indefiniteLoop);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92); match(While);
			setState(93); comparison();
			setState(94); match(Do);
			setState(95); statements();
			setState(96); match(End);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComparisonContext extends ParserRuleContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public TerminalNode Relation() { return getToken(pamParser.Relation, 0); }
		public ComparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparison; }
	}

	public final ComparisonContext comparison() throws RecognitionException {
		ComparisonContext _localctx = new ComparisonContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_comparison);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98); expression();
			setState(99); match(Relation);
			setState(100); expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElementContext extends ParserRuleContext {
		public TerminalNode Variable() { return getToken(pamParser.Variable, 0); }
		public TerminalNode Constant() { return getToken(pamParser.Constant, 0); }
		public ElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_element; }
	}

	public final ElementContext element() throws RecognitionException {
		ElementContext _localctx = new ElementContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_element);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(102);
			_la = _input.LA(1);
			if ( !(_la==Variable || _la==Constant) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ElementContext element() {
			return getRuleContext(ElementContext.class,0);
		}
		public Additive_expressionContext additive_expression() {
			return getRuleContext(Additive_expressionContext.class,0);
		}
		public Multiplicative_expressionContext multiplicative_expression() {
			return getRuleContext(Multiplicative_expressionContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_expression);
		try {
			setState(107);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(104); element();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(105); multiplicative_expression();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(106); additive_expression();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Additive_expressionContext extends ParserRuleContext {
		public List<ElementContext> element() {
			return getRuleContexts(ElementContext.class);
		}
		public ElementContext element(int i) {
			return getRuleContext(ElementContext.class,i);
		}
		public List<TerminalNode> WeakOperator() { return getTokens(pamParser.WeakOperator); }
		public TerminalNode WeakOperator(int i) {
			return getToken(pamParser.WeakOperator, i);
		}
		public Multiplicative_expressionContext multiplicative_expression(int i) {
			return getRuleContext(Multiplicative_expressionContext.class,i);
		}
		public List<Multiplicative_expressionContext> multiplicative_expression() {
			return getRuleContexts(Multiplicative_expressionContext.class);
		}
		public Additive_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_additive_expression; }
	}

	public final Additive_expressionContext additive_expression() throws RecognitionException {
		Additive_expressionContext _localctx = new Additive_expressionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_additive_expression);
		int _la;
		try {
			setState(139);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(109); element();
				setState(114);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==WeakOperator) {
					{
					{
					setState(110); match(WeakOperator);
					setState(111); element();
					}
					}
					setState(116);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(117); element();
				setState(125);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==WeakOperator) {
					{
					{
					setState(118); match(WeakOperator);
					setState(121);
					switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
					case 1:
						{
						setState(119); element();
						}
						break;

					case 2:
						{
						setState(120); multiplicative_expression();
						}
						break;
					}
					}
					}
					setState(127);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(128); multiplicative_expression();
				setState(136);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==WeakOperator) {
					{
					{
					setState(129); match(WeakOperator);
					setState(132);
					switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
					case 1:
						{
						setState(130); element();
						}
						break;

					case 2:
						{
						setState(131); multiplicative_expression();
						}
						break;
					}
					}
					}
					setState(138);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Multiplicative_expressionContext extends ParserRuleContext {
		public List<ElementContext> element() {
			return getRuleContexts(ElementContext.class);
		}
		public ElementContext element(int i) {
			return getRuleContext(ElementContext.class,i);
		}
		public List<TerminalNode> StrongOperator() { return getTokens(pamParser.StrongOperator); }
		public TerminalNode StrongOperator(int i) {
			return getToken(pamParser.StrongOperator, i);
		}
		public Multiplicative_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicative_expression; }
	}

	public final Multiplicative_expressionContext multiplicative_expression() throws RecognitionException {
		Multiplicative_expressionContext _localctx = new Multiplicative_expressionContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_multiplicative_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141); element();
			setState(146);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==StrongOperator) {
				{
				{
				setState(142); match(StrongOperator);
				setState(143); element();
				}
				}
				setState(148);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\3\27\u0098\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\3\2\3\2\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\6\3)\n\3\r\3\16\3*\3\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6"+
		"\7\6\66\n\6\f\6\16\69\13\6\3\6\3\6\3\7\3\7\3\7\7\7@\n\7\f\7\16\7C\13\7"+
		"\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\5\tR\n\t\3\t\3\t"+
		"\3\n\3\n\3\n\5\nY\n\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3"+
		"\f\3\f\3\f\3\f\3\r\3\r\3\16\3\16\3\16\5\16n\n\16\3\17\3\17\3\17\7\17s"+
		"\n\17\f\17\16\17v\13\17\3\17\3\17\3\17\3\17\5\17|\n\17\7\17~\n\17\f\17"+
		"\16\17\u0081\13\17\3\17\3\17\3\17\3\17\5\17\u0087\n\17\7\17\u0089\n\17"+
		"\f\17\16\17\u008c\13\17\5\17\u008e\n\17\3\20\3\20\3\20\7\20\u0093\n\20"+
		"\f\20\16\20\u0096\13\20\3\20\2\21\2\4\6\b\n\f\16\20\22\24\26\30\32\34"+
		"\36\2\5\3\2\6\7\3\2\22\23\3\2\23\24\u009c\2 \3\2\2\2\4(\3\2\2\2\6,\3\2"+
		"\2\2\b/\3\2\2\2\n\62\3\2\2\2\f<\3\2\2\2\16F\3\2\2\2\20K\3\2\2\2\22U\3"+
		"\2\2\2\24^\3\2\2\2\26d\3\2\2\2\30h\3\2\2\2\32m\3\2\2\2\34\u008d\3\2\2"+
		"\2\36\u008f\3\2\2\2 !\5\4\3\2!\3\3\2\2\2\")\5\6\4\2#)\5\b\5\2$)\5\22\n"+
		"\2%)\5\24\13\2&)\5\16\b\2\')\5\20\t\2(\"\3\2\2\2(#\3\2\2\2($\3\2\2\2("+
		"%\3\2\2\2(&\3\2\2\2(\'\3\2\2\2)*\3\2\2\2*(\3\2\2\2*+\3\2\2\2+\5\3\2\2"+
		"\2,-\7\5\2\2-.\5\n\6\2.\7\3\2\2\2/\60\t\2\2\2\60\61\5\f\7\2\61\t\3\2\2"+
		"\2\62\67\7\23\2\2\63\64\7\b\2\2\64\66\7\23\2\2\65\63\3\2\2\2\669\3\2\2"+
		"\2\67\65\3\2\2\2\678\3\2\2\28:\3\2\2\29\67\3\2\2\2:;\7\4\2\2;\13\3\2\2"+
		"\2<A\t\3\2\2=>\7\b\2\2>@\t\3\2\2?=\3\2\2\2@C\3\2\2\2A?\3\2\2\2AB\3\2\2"+
		"\2BD\3\2\2\2CA\3\2\2\2DE\7\4\2\2E\r\3\2\2\2FG\7\23\2\2GH\7\t\2\2HI\5\32"+
		"\16\2IJ\7\4\2\2J\17\3\2\2\2KL\7\n\2\2LM\5\26\f\2MN\7\13\2\2NQ\5\4\3\2"+
		"OP\7\r\2\2PR\5\4\3\2QO\3\2\2\2QR\3\2\2\2RS\3\2\2\2ST\7\f\2\2T\21\3\2\2"+
		"\2UX\7\16\2\2VY\5\32\16\2WY\7\23\2\2XV\3\2\2\2XW\3\2\2\2YZ\3\2\2\2Z[\7"+
		"\17\2\2[\\\5\4\3\2\\]\7\21\2\2]\23\3\2\2\2^_\7\20\2\2_`\5\26\f\2`a\7\17"+
		"\2\2ab\5\4\3\2bc\7\21\2\2c\25\3\2\2\2de\5\32\16\2ef\7\27\2\2fg\5\32\16"+
		"\2g\27\3\2\2\2hi\t\4\2\2i\31\3\2\2\2jn\5\30\r\2kn\5\36\20\2ln\5\34\17"+
		"\2mj\3\2\2\2mk\3\2\2\2ml\3\2\2\2n\33\3\2\2\2ot\5\30\r\2pq\7\25\2\2qs\5"+
		"\30\r\2rp\3\2\2\2sv\3\2\2\2tr\3\2\2\2tu\3\2\2\2u\u008e\3\2\2\2vt\3\2\2"+
		"\2w\177\5\30\r\2x{\7\25\2\2y|\5\30\r\2z|\5\36\20\2{y\3\2\2\2{z\3\2\2\2"+
		"|~\3\2\2\2}x\3\2\2\2~\u0081\3\2\2\2\177}\3\2\2\2\177\u0080\3\2\2\2\u0080"+
		"\u008e\3\2\2\2\u0081\177\3\2\2\2\u0082\u008a\5\36\20\2\u0083\u0086\7\25"+
		"\2\2\u0084\u0087\5\30\r\2\u0085\u0087\5\36\20\2\u0086\u0084\3\2\2\2\u0086"+
		"\u0085\3\2\2\2\u0087\u0089\3\2\2\2\u0088\u0083\3\2\2\2\u0089\u008c\3\2"+
		"\2\2\u008a\u0088\3\2\2\2\u008a\u008b\3\2\2\2\u008b\u008e\3\2\2\2\u008c"+
		"\u008a\3\2\2\2\u008do\3\2\2\2\u008dw\3\2\2\2\u008d\u0082\3\2\2\2\u008e"+
		"\35\3\2\2\2\u008f\u0094\5\30\r\2\u0090\u0091\7\26\2\2\u0091\u0093\5\30"+
		"\r\2\u0092\u0090\3\2\2\2\u0093\u0096\3\2\2\2\u0094\u0092\3\2\2\2\u0094"+
		"\u0095\3\2\2\2\u0095\37\3\2\2\2\u0096\u0094\3\2\2\2\20(*\67AQXmt{\177"+
		"\u0086\u008a\u008d\u0094";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}