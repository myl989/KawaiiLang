package org.kawaiilang;

public class Token implements Cloneable {
  
  public static final String CHARS_ALLOWED_IN_IDENTIFIERS = "&^_?:>=<";

  public static final String TT_INT = "INT";
  public static final String TT_FLOAT = "FLOAT";
  public static final String TT_STR = "STR";
  public static final String TT_LIST = "LIST";
  public static final String TT_NOTHING = "NOTHING";

  public static final String TT_ADD = "+";
  public static final String TT_MINUS = "-";
  public static final String TT_MUL = "ᴹ";
  public static final String TT_DIV = "/";
  public static final String TT_MOD = "%";
  public static final String TT_LPAREN = "(";
  public static final String TT_RPAREN = ")";

  public static final String TT_EQUALS = "EQUALS";
  public static final String TT_LT = "LT";
  public static final String TT_GT = "GT";
  public static final String TT_LTE = "LTE";
  public static final String TT_GTE = "GTE";

  public static final String TT_ASSIGN = "ASSIGN";
  public static final String TT_VARNAME = "VARNAME";
  public static final String TT_VARTYPE = "VARTYPE";
  public static final String TT_KEYWORD = "KEYWORD";

  public static final String TT_STARTIF = "STARTIF";
  public static final String TT_ENDIF = "ENDIF";

  public static final String TT_PARAM = "UwU";
  public static final String TT_COMMA = "COMMA";

  public static final String TT_LSQU = "LSQU";
  public static final String TT_RSQU = "RSQU";

  public static final String[] KEYWORDS = {"OwO", "notices", "^_^ewndNotice", "^_^wepeatDat", "do", "doWen", "tw", "twimes", "ewlse", "stawp", "awnd", "orw", "nawt", "xwr", "dewete", "canDo", "canGibU", "gibU", "^_^ewndCanDo"};

  public static final String[] DATA_TYPES = {"Numwer", "Fwnctwion", "Stwing", "Lwist"};

  String type;
  Object value;

  public String getType() {
    return type;
  }

  public Object getValue() {
    return value;
  }

  public Token(String type, Object value) {
    this.type = type;
    this.value = value;
  }

  public Token(String type) {
    this.type = type;
    this.value = null;
  }

  public String toString() {
    if (value != null) {
      return new StringBuilder(type).append(":").append(value.toString()).toString();
    } else {
      return type;
    }
  }

  public int hashCode() {
    int hash = 647;
    hash += 67 * type.hashCode();
    if (value == null) {
      return hash;
    } else {
      hash += 67 * value.hashCode();
      return hash;
    }
  }

  public boolean equals(Object o) {
    if (!(o instanceof Token)) {
      return false;
    }
    Token t = (Token) o;
    if (value != null) {
      return (type.equals(t.type) && value.equals(t.value));  
    } else {
      return type.equals(t.type);
    }
  }

  public Token clone() {
    return new Token(type, value);
  }

  public static Token[] cloneTokenArray(Token[] tokenArray) {
    Token[] clone = new Token[tokenArray.length];
    for (int i = 0; i < tokenArray.length; i++) {
      clone[i] = tokenArray[i].clone();
    }
    return clone;
  }

}