// Generated from C:\Users\Administrator\Google 드라이브\단국대\프로그래밍\ANTLR\workspace\pam to bytecode\pam.g4 by ANTLR 4.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class pamLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, Semicolon=2, Read=3, WriteLine=4, Write=5, Comma=6, Assignment=7, 
		If=8, Then=9, Fi=10, Else=11, To=12, Do=13, While=14, End=15, String=16, 
		Variable=17, Constant=18, WeakOperator=19, StrongOperator=20, Relation=21;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"WS", "';'", "'read'", "'writeline'", "'write'", "','", "':='", "'if'", 
		"'then'", "'fi'", "'else'", "'to'", "'do'", "'while'", "'end'", "String", 
		"Variable", "Constant", "WeakOperator", "StrongOperator", "Relation"
	};
	public static final String[] ruleNames = {
		"WS", "Semicolon", "Read", "WriteLine", "Write", "Comma", "Assignment", 
		"If", "Then", "Fi", "Else", "To", "Do", "While", "End", "String", "Variable", 
		"Constant", "WeakOperator", "StrongOperator", "Relation", "Letter", "Digit"
	};


	public pamLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "pam.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 0: WS_action((RuleContext)_localctx, actionIndex); break;
		}
	}
	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0: skip(); break;
		}
	}

	public static final String _serializedATN =
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\2\27\u00ad\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\3\2"+
		"\5\2\63\n\2\3\2\3\2\6\2\67\n\2\r\2\16\28\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3"+
		"\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3"+
		"\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\6\21\u0086\n\21\r\21\16\21\u0087\3\21\3\21\3\22\3"+
		"\22\3\22\7\22\u008f\n\22\f\22\16\22\u0092\13\22\3\23\5\23\u0095\n\23\3"+
		"\23\6\23\u0098\n\23\r\23\16\23\u0099\3\24\3\24\3\25\3\25\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\5\26\u00a8\n\26\3\27\3\27\3\30\3\30\2\31\3\3"+
		"\2\5\4\1\7\5\1\t\6\1\13\7\1\r\b\1\17\t\1\21\n\1\23\13\1\25\f\1\27\r\1"+
		"\31\16\1\33\17\1\35\20\1\37\21\1!\22\1#\23\1%\24\1\'\25\1)\26\1+\27\1"+
		"-\2\1/\2\1\3\2\b\4\2\13\13\"\"\4\2\60\60<<\4\2--//\4\2,,\61\61\4\2>>@"+
		"@\4\2C\\c|\u00bf\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13"+
		"\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2"+
		"\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2"+
		"!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\3\66"+
		"\3\2\2\2\5<\3\2\2\2\7>\3\2\2\2\tC\3\2\2\2\13M\3\2\2\2\rS\3\2\2\2\17U\3"+
		"\2\2\2\21X\3\2\2\2\23[\3\2\2\2\25`\3\2\2\2\27c\3\2\2\2\31h\3\2\2\2\33"+
		"k\3\2\2\2\35n\3\2\2\2\37t\3\2\2\2!x\3\2\2\2#\u008b\3\2\2\2%\u0094\3\2"+
		"\2\2\'\u009b\3\2\2\2)\u009d\3\2\2\2+\u00a7\3\2\2\2-\u00a9\3\2\2\2/\u00ab"+
		"\3\2\2\2\61\63\7\17\2\2\62\61\3\2\2\2\62\63\3\2\2\2\63\64\3\2\2\2\64\67"+
		"\7\f\2\2\65\67\t\2\2\2\66\62\3\2\2\2\66\65\3\2\2\2\678\3\2\2\28\66\3\2"+
		"\2\289\3\2\2\29:\3\2\2\2:;\b\2\2\2;\4\3\2\2\2<=\7=\2\2=\6\3\2\2\2>?\7"+
		"t\2\2?@\7g\2\2@A\7c\2\2AB\7f\2\2B\b\3\2\2\2CD\7y\2\2DE\7t\2\2EF\7k\2\2"+
		"FG\7v\2\2GH\7g\2\2HI\7n\2\2IJ\7k\2\2JK\7p\2\2KL\7g\2\2L\n\3\2\2\2MN\7"+
		"y\2\2NO\7t\2\2OP\7k\2\2PQ\7v\2\2QR\7g\2\2R\f\3\2\2\2ST\7.\2\2T\16\3\2"+
		"\2\2UV\7<\2\2VW\7?\2\2W\20\3\2\2\2XY\7k\2\2YZ\7h\2\2Z\22\3\2\2\2[\\\7"+
		"v\2\2\\]\7j\2\2]^\7g\2\2^_\7p\2\2_\24\3\2\2\2`a\7h\2\2ab\7k\2\2b\26\3"+
		"\2\2\2cd\7g\2\2de\7n\2\2ef\7u\2\2fg\7g\2\2g\30\3\2\2\2hi\7v\2\2ij\7q\2"+
		"\2j\32\3\2\2\2kl\7f\2\2lm\7q\2\2m\34\3\2\2\2no\7y\2\2op\7j\2\2pq\7k\2"+
		"\2qr\7n\2\2rs\7g\2\2s\36\3\2\2\2tu\7g\2\2uv\7p\2\2vw\7f\2\2w \3\2\2\2"+
		"x\u0085\7$\2\2y\u0086\5-\27\2z\u0086\5\3\2\2{\u0086\5/\30\2|\u0086\5\'"+
		"\24\2}\u0086\5)\25\2~\u0086\5+\26\2\177\u0086\5\r\7\2\u0080\u0086\t\3"+
		"\2\2\u0081\u0082\7^\2\2\u0082\u0086\7)\2\2\u0083\u0084\7^\2\2\u0084\u0086"+
		"\7$\2\2\u0085y\3\2\2\2\u0085z\3\2\2\2\u0085{\3\2\2\2\u0085|\3\2\2\2\u0085"+
		"}\3\2\2\2\u0085~\3\2\2\2\u0085\177\3\2\2\2\u0085\u0080\3\2\2\2\u0085\u0081"+
		"\3\2\2\2\u0085\u0083\3\2\2\2\u0086\u0087\3\2\2\2\u0087\u0085\3\2\2\2\u0087"+
		"\u0088\3\2\2\2\u0088\u0089\3\2\2\2\u0089\u008a\7$\2\2\u008a\"\3\2\2\2"+
		"\u008b\u0090\5-\27\2\u008c\u008f\5-\27\2\u008d\u008f\5/\30\2\u008e\u008c"+
		"\3\2\2\2\u008e\u008d\3\2\2\2\u008f\u0092\3\2\2\2\u0090\u008e\3\2\2\2\u0090"+
		"\u0091\3\2\2\2\u0091$\3\2\2\2\u0092\u0090\3\2\2\2\u0093\u0095\7/\2\2\u0094"+
		"\u0093\3\2\2\2\u0094\u0095\3\2\2\2\u0095\u0097\3\2\2\2\u0096\u0098\5/"+
		"\30\2\u0097\u0096\3\2\2\2\u0098\u0099\3\2\2\2\u0099\u0097\3\2\2\2\u0099"+
		"\u009a\3\2\2\2\u009a&\3\2\2\2\u009b\u009c\t\4\2\2\u009c(\3\2\2\2\u009d"+
		"\u009e\t\5\2\2\u009e*\3\2\2\2\u009f\u00a8\7?\2\2\u00a0\u00a1\7?\2\2\u00a1"+
		"\u00a8\7>\2\2\u00a2\u00a8\t\6\2\2\u00a3\u00a4\7@\2\2\u00a4\u00a8\7?\2"+
		"\2\u00a5\u00a6\7>\2\2\u00a6\u00a8\7@\2\2\u00a7\u009f\3\2\2\2\u00a7\u00a0"+
		"\3\2\2\2\u00a7\u00a2\3\2\2\2\u00a7\u00a3\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a8"+
		",\3\2\2\2\u00a9\u00aa\t\7\2\2\u00aa.\3\2\2\2\u00ab\u00ac\4\62;\2\u00ac"+
		"\60\3\2\2\2\r\2\62\668\u0085\u0087\u008e\u0090\u0094\u0099\u00a7";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}