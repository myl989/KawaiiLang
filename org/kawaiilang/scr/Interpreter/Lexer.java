package org.kawaiilang;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

final class Lexer {

  private String text;
  private String fn;
  private Position pos;
  private char currentChar = Character.MIN_VALUE;
  private boolean firstChar = true;

  private static HashMap<String, String> classNames = new HashMap<>();	//Short name (if first), long name
  
  public static final HashMap<Character, Character> escapeCharsList = new HashMap<>();

  static {
    escapeCharsList.put('n','\n');
    escapeCharsList.put('t','\t');
  }

  public Lexer(String fileName, String text) {
    this.text = text;
    this.fn = fileName;
    pos = new Position(-1, 0, -1, fn, text);
  }
  
  public static void addClassName(String longName, String shortName) {
	if (classNames.containsKey(shortName)) {
	  classNames.put(longName, longName);
	} else {
	  classNames.put(shortName, longName);
	}
  }

  private void advance() {
    if (firstChar) {
      firstChar = false;
    }
    pos.advance(currentChar);
    if (pos.getIdx() < text.length()) {
      currentChar = text.charAt(pos.getIdx());
    } else {
      currentChar = Character.MIN_VALUE;
    }
  }

  public Token[] makeTokens() {	
    ArrayList<Token> tokens = new ArrayList<>();
    while (true) {
      if (Character.isWhitespace(currentChar)) {
        advance();
      } else if (Character.isLetter(currentChar) || TokenV1.CHARS_ALLOWED_IN_IDENTIFIERS.indexOf(currentChar) > -1) {
    	Token t = makeIdentifier();
        tokens.add(t);
        if (t.getType().equals(TokenV1.TT_INCLUDE)) {
          break;
        }
      } else if (Character.isDigit(currentChar)) {
        tokens.add(makeNum());
      } else if (currentChar == '~') {
    	tokens.add(new TokenV1(TokenV1.TT_TILDE));
    	advance();
      } else if (currentChar == '\'') {
        tokens.add(makeStr());
      } else if (currentChar == '+') {
        tokens.add(new TokenV1(TokenV1.TT_ADD));
        advance();
      } else if (currentChar == '-') {
        tokens.add(new TokenV1(TokenV1.TT_MINUS));
        advance();
      } else if (currentChar == '*') {
        //If there is a start if then another * must be end if
        if (tokens.contains(new TokenV1(TokenV1.TT_STARTIF))) {
          tokens.add(new TokenV1(TokenV1.TT_ENDIF));
          advance();
          if (currentChar == '?') {
            break;
          } else if (currentChar == '~') {
        	tokens.add(new TokenV1(TokenV1.TT_TILDE));
        	break;
          } else {
            Position start = pos.clone();
            Token[] errorArray = new TokenV1[1];
            errorArray[0] = new InvalidSyntaxError(start, pos, "Naooo uwu if statement must end with \"?\" ._.");
            return errorArray;
          }
        } else {
          tokens.add(new TokenV1(TokenV1.TT_MUL));
          advance();
        }
      } else if (currentChar == '/') {
        tokens.add(new TokenV1(TokenV1.TT_DIV));
        advance();
      } else if (currentChar == '(') {
        tokens.add(new TokenV1(TokenV1.TT_LPAREN));
        advance();
      } else if (currentChar == ')') {
        tokens.add(new TokenV1(TokenV1.TT_RPAREN));
        advance();
      } else if (currentChar == '[') {
        tokens.add(new TokenV1(TokenV1.TT_LSQU));
        advance();
      } else if (currentChar == ']') {
        tokens.add(new TokenV1(TokenV1.TT_RSQU));
        advance();
      } else if (currentChar == '%') {
        tokens.add(new TokenV1(TokenV1.TT_MOD));
        advance();
      } else if (currentChar == ',') {
        tokens.add(new TokenV1(TokenV1.TT_COMMA));
        advance();
      } else if (currentChar == '!') {
        break;
      } else {
        char badC = currentChar;
        boolean tmp = firstChar;
        advance();
        if (!tmp) {
          Position start = pos.clone();
          Token[] errorArray = new TokenV1[1];
          if (badC == Character.MIN_VALUE) {
            errorArray[0] = new InvalidSyntaxError(start, pos, "Naooo uwu line must end with \"!\" ._.");
          } else {
            errorArray[0] = new IllegalCharacterError(start, pos, "\"" + badC + "\"");
          }
          return errorArray;
        }
      }
    }
    return tokens.toArray(new TokenV1[0]);
  }

  private Token makeNum() {
    StringBuilder numStr = new StringBuilder();
    byte dotCount = 0;
    while (currentChar != Character.MIN_VALUE && (Character.isDigit(currentChar) || currentChar == '.')) {
      if (currentChar == '.') {
        if (dotCount == 1) {
          break;
        }
        dotCount++;
        numStr.append('.');
      }
      else {
        numStr.append(currentChar);
      }
      advance();
    }
    if (dotCount == 0) {
      return new TokenV1(TokenV1.TT_INT, Integer.parseInt(numStr.toString()));
    } else {
      return new TokenV1(TokenV1.TT_FLOAT, Double.parseDouble(numStr.toString()));
    }
  }

  private Token makeStr() {
    StringBuilder sb = new StringBuilder();
    boolean esc = false;
    advance();
    while (currentChar != '\'' || esc) {
      if (esc) {
        if (escapeCharsList.containsKey(currentChar)) {
          sb.append(escapeCharsList.get(currentChar));
        } else {
          sb.append(currentChar);
        }
        esc = false;
      } else if (currentChar == '\\') {
        esc = true;
      } else {
        sb.append(currentChar);
      }
      advance();
    }
    advance();
    return new TokenV1(TokenV1.TT_STR, sb.toString());
  }

  private Token makeIdentifier() {
    StringBuilder idSB = new StringBuilder();
    while (currentChar != Character.MIN_VALUE && (Character.isLetter(currentChar) || Character.isDigit(currentChar) || TokenV1.CHARS_ALLOWED_IN_IDENTIFIERS.indexOf(currentChar) > -1)) {
      idSB.append(currentChar);
      advance();
    }
    String idStr = idSB.toString();
    if (Arrays.asList(TokenV1.KEYWORDS).contains(idStr)) {
      if (idStr.equals("OwO")) {
        //OwO has a different meaning in classes!
        //The following code detects OwO *notices
        Position before = pos.clone();
        advance();
        StringBuilder noticesSB = new StringBuilder();
        while (currentChar != Character.MIN_VALUE && (Character.isLetter(currentChar) || currentChar == '*')) {
          noticesSB.append(currentChar);
          advance();
        }
        String noticesResults = noticesSB.toString();
        if (noticesResults.equals("*notices")) {
          return new TokenV1(TokenV1.TT_STARTIF);
        } else {
          //just assume it's a normal OwO
          pos = before;
          return new TokenV1(TokenV1.TT_KEYWORD, "OwO");
        }
      }
      return new TokenV1(TokenV1.TT_KEYWORD, idStr);
    } else if (idStr.equals("iws")) {
      return new TokenV1(TokenV1.TT_ASSIGN, idStr);
    } else if (idStr.equals("eqwals")) {
      return new TokenV1(TokenV1.TT_EQUALS, idStr);
    } else if (idStr.equals("nwthin")) {
      return new TokenV1(TokenV1.TT_NOTHING);
    } else if (idStr.equals("UwU")) {
      return new TokenV1(TokenV1.TT_PARAM);
    } else if (idStr.equals(">_<")) {
      return new TokenV1(TokenV1.TT_GT, idStr);
    } else if (idStr.equals("<_<")) {
      return new TokenV1(TokenV1.TT_LT, idStr);
    } else if (idStr.equals(">=<")) {
      return new TokenV1(TokenV1.TT_GTE, idStr);
    } else if (idStr.equals("<=<")) {
      return new TokenV1(TokenV1.TT_LTE, idStr);
    } else if (idStr.equals("isForMe?")) {
      return new TokenV1(TokenV1.TT_INCLUDE);
    } else if (Arrays.asList(TokenV1.DATA_TYPES).contains(idStr)) {
      return new TokenV1(TokenV1.TT_VARTYPE, idStr);
    } else if (classNames.containsKey(idStr)) {
      return new TokenV1(TokenV1.TT_CLASS, classNames.get(idStr));
    } else if (classNames.containsValue(idStr)) {
      return new TokenV1(TokenV1.TT_CLASS, idStr);
    } else {
      return new TokenV1(TokenV1.TT_VARNAME, idStr);
    }
  }

}