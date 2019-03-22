/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

grammar pam;

program : procedure* mainproc procedure*;
procedure : Proc Identifier Lparen (Identifier (Comma Identifier)*)? Rparen statements;
mainproc : Proc Main Lparen Rparen statements;

statements : (inputStatement | outputStatement | definiteLoop | indefiniteLoop | assignmentStatement | conditionalStatement | procSemicolonCallStatement | returnStatement)+;
inputStatement : Read inputList;
inputList : Identifier (Comma Identifier)* Semicolon;
outputStatement : (WriteLine | Write) outputList;
outputList : (String | expression | procNocolonCallStatement) (Comma (String | expression | procNocolonCallStatement))* Semicolon;
assignmentStatement : Identifier Assignment (expression | procNocolonCallStatement) Semicolon;
conditionalStatement : If comparison Then statements (Else statements)? Fi;
definiteLoop : To expression Do statements End;
indefiniteLoop : While comparison Do statements End;
comparison : expression Relation expression; 
returnStatement : Return expression? Semicolon;
procSemicolonCallStatement : Identifier Lparen (expression (Comma (expression))*)? Rparen Semicolon;
procNocolonCallStatement :  Identifier Lparen (expression (Comma (expression))*)? Rparen;

element : Identifier | Constant;
expression : element | paren_multiplicative_expression | paren_additive_expression | multiplicative_expression | additive_expression;
additive_expression : (element | paren_multiplicative_expression | paren_additive_expression | multiplicative_expression) (WeakOperator (element | paren_multiplicative_expression | paren_additive_expression | multiplicative_expression))*;
multiplicative_expression : (element | paren_multiplicative_expression | paren_additive_expression) (StrongOperator (element | paren_multiplicative_expression | paren_additive_expression))*;
paren_additive_expression : Lparen ((element | paren_multiplicative_expression) (WeakOperator (element | paren_multiplicative_expression))*)  Rparen;
paren_multiplicative_expression : Lparen ((element | paren_additive_expression) (StrongOperator (element | paren_additive_expression))*) Rparen;

WS : ('\r'? '\n' | '\t' | ' ')+ {skip();};

Semicolon : ';';
Read : 'read';
WriteLine : 'writeline';
Write : 'write';
Comma : ',';
Assignment : ':=';
If : 'if';
Then : 'then';
Fi : 'fi';
Else : 'else';
To : 'to';
Do : 'do';
While :  'while';
End : 'end';
Proc : 'proc';
Lparen : '(';
Rparen : ')';
Main : 'main';
Return : 'return';

String : '"' (Letter | WS | Digit | WeakOperator | StrongOperator | Assignment | Relation | Comma | '.' | ':' | '\\\'' | '\\"' | '!' | '?' | Lparen | Rparen)+ '"' ;
Identifier : Letter (Letter | Digit)*;
Constant : '-'? Digit+;

WeakOperator : '+' | '-';
StrongOperator : '*'| '/';
Relation : '='|'=<'|'<'|'>'|'>='|'<>';

fragment Letter : 'a'..'z' | 'A'..'Z';
fragment Digit : '0'..'9';
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */