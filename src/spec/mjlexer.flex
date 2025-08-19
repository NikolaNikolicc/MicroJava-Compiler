package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}
// indicates that the lexer will work with Java CUP, a parser generator
%cup
// enable tracking of line(yyline) and column (yycolumn) numbers for tokens
%line
%column

// declares a state names COMMENT for managing comment parsing
%xstate COMMENT

// specifies the action to perform when the end of the file is reached
%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

"program"   { return new_symbol(sym.PROG, yytext()); }
"break" 	{ return new_symbol(sym.BREAK, yytext()); }
"class" 	{ return new_symbol(sym.CLASS, yytext()); }
"else" 		{ return new_symbol(sym.ELSE, yytext()); }
"const" 	{ return new_symbol(sym.CONST, yytext()); }
"if" 		{ return new_symbol(sym.IF, yytext()); }
"new" 		{ return new_symbol(sym.NEW, yytext()); }
"print" 	{ return new_symbol(sym.PRINT, yytext()); }
"read" 		{ return new_symbol(sym.READ, yytext()); }
"return" 	{ return new_symbol(sym.RETURN, yytext()); }
"void" 		{ return new_symbol(sym.VOID, yytext()); }
"extends" 	{ return new_symbol(sym.EXTENDS, yytext()); }
"continue" 	{ return new_symbol(sym.CONTINUE, yytext()); }
"union" 	{ return new_symbol(sym.UNION, yytext()); }
"intersect" { return new_symbol(sym.INTERSECTION, yytext()); }
"do" 		{ return new_symbol(sym.DO, yytext()); }
"while" 	{ return new_symbol(sym.WHILE, yytext()); }
"map" 		{ return new_symbol(sym.MAP, yytext()); }
"interface" { return new_symbol(sym.INTERFACE, yytext()); }

"+" 		{ return new_symbol(sym.PLUS, yytext()); }
"-" 		{ return new_symbol(sym.MINUS, yytext()); }
"*" 		{ return new_symbol(sym.MUL, yytext()); }
"/" 		{ return new_symbol(sym.DIV, yytext()); }
"%" 		{ return new_symbol(sym.MOD, yytext()); }
"==" 		{ return new_symbol(sym.EQEQ, yytext()); }
"!=" 		{ return new_symbol(sym.NOTEQ, yytext()); }
">" 		{ return new_symbol(sym.GR, yytext()); }
">=" 		{ return new_symbol(sym.GREQ, yytext()); }
"<" 		{ return new_symbol(sym.LE, yytext()); }
"<=" 		{ return new_symbol(sym.LEQ, yytext()); }
"&&" 		{ return new_symbol(sym.AND, yytext()); }
"||" 		{ return new_symbol(sym.OR, yytext()); }
"=" 		{ return new_symbol(sym.EQ, yytext()); }
"++" 		{ return new_symbol(sym.INC, yytext()); }
"--" 		{ return new_symbol(sym.DEC, yytext()); }
";" 		{ return new_symbol(sym.SEMI, yytext()); }
":" 		{ return new_symbol(sym.COLON, yytext()); }
"," 		{ return new_symbol(sym.COMMA, yytext()); }
"." 		{ return new_symbol(sym.DOT, yytext()); }
"(" 		{ return new_symbol(sym.LPAREN, yytext()); }
")" 		{ return new_symbol(sym.RPAREN, yytext()); }
"[" 		{ return new_symbol(sym.LBRACKET, yytext()); }
"]"			{ return new_symbol(sym.RBRACKET, yytext()); }
"{" 		{ return new_symbol(sym.LBRACE, yytext()); }
"}"			{ return new_symbol(sym.RBRACE, yytext()); }

<YYINITIAL> "//" 		     { yybegin(COMMENT); }
<COMMENT> .      { yybegin(COMMENT); }
<COMMENT> "\r\n" { yybegin(YYINITIAL); }

[0-9]+  { return new_symbol(sym.NUMBER, new Integer (yytext())); }

"'".{1}"'" {return new_symbol (sym.CHAR, new Character(yytext().charAt(1))); }

"true" {return new_symbol (sym.BOOL, 1); }
"false" {return new_symbol (sym.BOOL, 0); }

"main" {return new_symbol (sym.MAIN, yytext()); }

// rule for tokenizing identifiers from input should be last because otherwise we wouldn't be able to recognise char, bool and num types
[a-zA-Z][a-zA-Z0-9A_]* 	{return new_symbol (sym.IDENT, yytext()); }

// rule for identifying tokens starting with a number but not consisting solely of numbers
[0-9]+[a-zA-Z][^\b\t\r\n\f" "]* { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1) + ", kolona " + (yycolumn + 1)); }

// rule for identifying char tokens which are longer than 1
"'".{1}[^\b\t\r\n\f" "]+"'" { System.err.println("Leksicka greska (" + yytext() + ") u liniji " + (yyline + 1) + ", kolona " + (yycolumn + 1)); }

// rule for identifying empty char
"'""'" { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1) + ", kolona " + (yycolumn + 1)); }

. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1) + ", kolona " + (yycolumn + 1)); }