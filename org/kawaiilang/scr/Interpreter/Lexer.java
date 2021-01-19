package org.kawaiilang;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

class Lexer {

  private String text;
  private String fn;
  private Position pos;
  private char currentChar = Character.MIN_VALUE;
  private boolean firstChar = true;

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
    while (true) { //handel nullType maybe?
    //System.out.println(currentChar);
      if (Character.isWhitespace(currentChar)) {
        advance();
      } else if (Character.isLetter(currentChar) || Token.CHARS_ALLOWED_IN_IDENTIFIERS.indexOf(currentChar) > -1) {
        tokens.add(makeIdentifier());
      } else if (Character.isDigit(currentChar)) {
        tokens.add(makeNum());
      } else if (currentChar == '\'') {
        tokens.add(makeStr());
      } else if (currentChar == '+') {
        tokens.add(new Token(Token.TT_ADD));
        advance();
      } else if (currentChar == '-') {
        tokens.add(new Token(Token.TT_MINUS));
        advance();
      } else if (currentChar == '*') {
        //If there is a start if then another * must be end if
        if (tokens.contains(new Token(Token.TT_STARTIF))) {
          tokens.add(new Token(Token.TT_ENDIF));
          advance();
          if (currentChar == '?') {
            break;
          } else {
            Position start = pos.clone();
            Token[] errorArray = new Token[1];
            errorArray[0] = new InvalidSyntaxError(start, pos, "Naooo uwu if statement must end with \"?\" ._.");
            return errorArray;
          }
        } else {
          tokens.add(new Token(Token.TT_MUL));
          advance();
        }
      } else if (currentChar == '/') {
        tokens.add(new Token(Token.TT_DIV));
        advance();
      } else if (currentChar == '(') {
        tokens.add(new Token(Token.TT_LPAREN));
        advance();
      } else if (currentChar == ')') {
        tokens.add(new Token(Token.TT_RPAREN));
        advance();
      } else if (currentChar == '[') {
        tokens.add(new Token(Token.TT_LSQU));
        advance();
      } else if (currentChar == ']') {
        tokens.add(new Token(Token.TT_RSQU));
        advance();
      } else if (currentChar == '%') {
        tokens.add(new Token(Token.TT_MOD));
        advance();
      } else if (currentChar == ',') {
        tokens.add(new Token(Token.TT_COMMA));
        advance();
      } else if (currentChar == '!') {
        break;
      } else {
        char badC = currentChar;
        boolean tmp = firstChar;
        advance();
        if (!tmp) {
          Position start = pos.clone();
          Token[] errorArray = new Token[1];
          if (badC == Character.MIN_VALUE) {
            errorArray[0] = new InvalidSyntaxError(start, pos, "Naooo uwu line must end with \"!\" ._.");
          } else {
            errorArray[0] = new IllegalCharacterError(start, pos, "\"" + badC + "\"");
          }
          return errorArray;
        }
      }
    }
    return tokens.toArray(new Token[0]);
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
      return new Token(Token.TT_INT, Integer.parseInt(numStr.toString()));
    } else {
      return new Token(Token.TT_FLOAT, Double.parseDouble(numStr.toString()));
    }
  }

  private Token makeStr() {
    StringBuilder sb = new StringBuilder();
    Position start = pos.clone();
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
    return new Token(Token.TT_STR, sb.toString());
  }

  private Token makeIdentifier() {
    StringBuilder idSB = new StringBuilder();
    Position start = pos.clone();
    while (currentChar != Character.MIN_VALUE && (Character.isLetter(currentChar) || Character.isDigit(currentChar) || Token.CHARS_ALLOWED_IN_IDENTIFIERS.indexOf(currentChar) > -1)) {
      idSB.append(currentChar);
      advance();
    }
    String idStr = idSB.toString();
    if (Arrays.asList(Token.KEYWORDS).contains(idStr)) {
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
          return new Token(Token.TT_STARTIF);
        } else {
          //just assume it's a normal OwO
          pos = before;
          return new Token(Token.TT_KEYWORD, "OwO");
        }
      }
      return new Token(Token.TT_KEYWORD, idStr);
    } else if (idStr.equals("iws")) {
      return new Token(Token.TT_ASSIGN, idStr);
    } else if (idStr.equals("eqwals")) {
      return new Token(Token.TT_EQUALS, idStr);
    } else if (idStr.equals("nwthin")) {
      return new Token(Token.TT_NOTHING);
    } else if (idStr.equals("UwU")) {
      return new Token(Token.TT_PARAM);
    } else if (idStr.equals(">_<")) {
      return new Token(Token.TT_GT, idStr);
    } else if (idStr.equals("<_<")) {
      return new Token(Token.TT_LT, idStr);
    } else if (idStr.equals(">=<")) {
      return new Token(Token.TT_GTE, idStr);
    } else if (idStr.equals("<=<")) {
      return new Token(Token.TT_LTE, idStr);
    } else if (Arrays.asList(Token.DATA_TYPES).contains(idStr)) {
      return new Token(Token.TT_VARTYPE, idStr);
    } else {
      return new Token(Token.TT_VARNAME, idStr);
    }
  }

}