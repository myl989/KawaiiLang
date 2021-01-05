package org.kawaiilang;
import java.util.ArrayList;
import java.util.HashMap;

public class Lexer {

  private String text;
  private String fn;
  Position pos;
  char currentChar = Character.MIN_VALUE;
  boolean firstChar = true;

  public Lexer(String fileName, String text) {
    this.text = text;
    this.fn = fileName;
    pos = new Position(-1, 0, -1, fn, text);
  }

  public void advance() {
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
      } else if (Token.DIGITS.indexOf(currentChar) > -1) {
        tokens.add(makeNum());
      } else if (currentChar == '+') {
        tokens.add(new Token(Token.TT_ADD));
        advance();
      } else if (currentChar == '-') {
        tokens.add(new Token(Token.TT_MINUS));
        advance();
      } else if (currentChar == '*') {
        tokens.add(new Token(Token.TT_MUL));
        advance();
      } else if (currentChar == '/') {
        tokens.add(new Token(Token.TT_DIV));
        advance();
      } else if (currentChar == '(') {
        tokens.add(new Token(Token.TT_LPAREN));
        advance();
      } else if (currentChar == ')') {
        tokens.add(new Token(Token.TT_RPAREN));
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
            errorArray[0] = new InvalidSyntaxError(start, pos, "Line must end with \"!\"");
          } else {
            errorArray[0] = new IllegalCharacterError(start, pos, "\"" + badC + "\"");
          }
          return errorArray;
        }
      }
    }
    return tokens.toArray(new Token[0]);
  }

  public Token makeNum() {
    StringBuilder numStr = new StringBuilder();
    byte dotCount = 0;
    while (currentChar != Character.MIN_VALUE && (Token.DIGITS.indexOf(currentChar) > -1 || currentChar == '.')) {
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

  public static final HashMap<String, String> UWU_KEY;

  static {

    HashMap<String, String> uwuKi = new HashMap<>();
    uwuKi.put("awdd", "+");
    uwuKi.put("mwinws", "-");
    uwuKi.put("mwltipwy", "*");
    uwuKi.put("diwide", "/");
    UWU_KEY = uwuKi;

  }

  public static String unUwUfy(String uwufiedText) {
    String[] lines = uwufiedText.split("\n");
    for (int i = 0; i < lines.length; i++) {
      for (String s : UWU_KEY.keySet()) {
        String beginning = new StringBuilder(s).append(" ").toString();
        if (lines[i].startsWith(beginning)) {
          String subLine = lines[i].substring(s.length() - 1);
          lines[i] = new StringBuilder(UWU_KEY.get(s)).append(subLine).toString();
        }
        String toReplace = new StringBuilder(" ").append(s).append(" ").toString();
        String replaceWith = new StringBuilder(" ").append(UWU_KEY.get(s)).append(" ").toString();
        lines[i] = lines[i].replaceAll(toReplace, replaceWith);
      }
    }
    StringBuilder strBuilder = new StringBuilder();
    for (String line : lines) {
      strBuilder.append(line).append("\n");
    }
    return strBuilder.toString();
  }

  public static Token[] run(String fileName, String text) {
    Lexer lexer = new Lexer(fileName, unUwUfy(text));
    Token[] tokens = lexer.makeTokens();
    return tokens;
  }

}