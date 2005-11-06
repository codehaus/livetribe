header {
/**
 *
 * Copyright 2005 (C) The original author or authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.livetribe.snmp.mib.parser;

import java.util.*;


import org.livetribe.snmp.mib.ast.*;
}

class MIBParser extends Parser;
options {
    k=5;
}

tokens {
    REAL_NUMBER;
    INTEGER_NUMBER;
}

compile returns [Environment environment]
{
    Module module;
    environment = new Environment();
}
    : (module = module_definition { environment.addModule(module); })+ ;

module_definition returns [Module module]
{
    module = new Module();
    ModuleIdentifier id;
}
    : id=module_identifier { module.setModuleIdentifier(id); }
      DEFINITIONS
      (tag_default[module])?
      (extension_default[module])?
      ASSIGNMENT BEGIN module_body[module] END
    ;

module_identifier returns [ModuleIdentifier id]
{
    id = new ModuleIdentifier();
}
    : LEFT_CURLY
    ;

module_body[Module module]
    : LEFT_CURLY 
    ;

tag_default[Module module]
    : LEFT_CURLY 
    ;

extension_default[Module module]
    : LEFT_CURLY 
    ;


{
import org.livetribe.snmp.mib.ast.*;
}
class MIBCompiler extends TreeParser;
options {
    defaultErrorHandler=false;
}

modules : (  );

class ASN1Lexer extends Lexer;
options {
    k=3;
    charVocabulary = '\3'..'\377' | '\u1000'..'\u1fff';
}

tokens {
    ACCESS = "ACCESS";
    MANDITORY = "manditory";
    NOT_ACCESSABLE = "not-accessable";
    OBSOLETE = "obsolete";
    OPTIONAL = "optional";
    READ_ONLY = "read-only";
    READ_WRITE = "read-write";
    STATUS = "STATUS";
    SYNTAX = "SYNTAX";
    WRITE_ONLY = "write-only";
}

NEWLINE
    : ((('\r''\n') => '\r''\n') | '\r' | '\n')
        { newline(); $setType(Token.SKIP); }
    ;

WS : ( ' ' | '\t' | '\f' ) { $setType(Token.SKIP); } ;

COMMENT : "--" ;

SL_COMMENT
	: COMMENT
	  ({ LA(2)!='-' }? '-' | ~('-'|'\n'|'\r'))*
	  (((('\r''\n') => '\r''\n') | '\r' | '\n') {newline();} | COMMENT)
        { $setType(Token.SKIP); }
	;

ASSIGNMENT : "::=";

IDENTIFIER : SMALL_LETTER ( (HYPHEN)? (SMALL_LETTER | CAPITAL_LETTER | DIGIT) )* ;
TYPEREFERENCE : CAPITAL_LETTER ( (HYPHEN)? (SMALL_LETTER | CAPITAL_LETTER | DIGIT) )* ;

NUMBER : DIGITS (DOT (DIGIT)* (EXPONENT)? { $setType(REAL_NUMBER); } | { $setType(INTEGER_NUMBER); } ) ;


protected DIGITS : ( ('1'..'9') (DIGIT)* | '0' );

protected EXPONENT
    : ('e'|'E') ('+'|'-')? (DIGIT)+
    ;

protected CAPITAL_LETTER : 'A'..'Z';
protected SMALL_LETTER : 'a'..'z';
protected DIGIT : '0'..'9';
protected HEX_DIGIT : ('0'..'9' | 'A'..'F');
EXCLAMATION_MARK : '!';
QUOTATION_MARK : '"';
AMPERSAND : '&';
APOSTROPHE : '\'';
LEFT_PAREN : '(';
RIGHT_PAREN : ')';
ASTERISK : '*';
COMMA : ',';
HYPHEN : '-';
DOT : '.';
SOLIDUS : '/';
COLON : ':';
SEMICOLON : ';';
LESS_THAN : '<';
EQUALS : '=';
GREATER_THAN : '>';
AT : '@';
LEFT_SQUARE : '[';
RIGHT_SQUARE : ']';
CIRCUMFLEX : '^';
UNDERSCORE : '_';
LEFT_CURLY : '{';
BAR : '|';
RIGHT_CURLY : '}';
RANGE : "..";
ELLIPSIS : "...";

