package org.kawaiilang;

public class Token {
  
  public static final String CHARS_ALLOWED_IN_IDENTIFIERS = "`@#$&^_?";

  public static final String TT_INT = "INT";
  public static final String TT_FLOAT = "FLOAT";
  public static final String TT_ADD = "+";
  public static final String TT_MINUS = "-";
  public static final String TT_MUL = "*";
  public static final String TT_DIV = "/";
  public static final String TT_MOD = "%";
  public static final String TT_LPAREN = "(";
  public static final String TT_RPAREN = ")";

  public static final String TT_ASSIGN = "ASSIGN";
  public static final String TT_VARNAME = "VARNAME";
  public static final String TT_VARTYPE = "VARTYPE";
  public static final String TT_KEYWORD = "KEYWORD";

  public static final String[] KEYWORDS = {"cwass", "waifu", "husbando", "cwassType", "waifuType", "husbandoType", "hasSensi", "imaginawy", "OwO", "notices", "^_^ewndNotice", "^_^ewndCwass", "^_^ewndWaifu", "^_^endHusbando", "canDo", "gibU", "canGibU", "ewlse", "bweak", "^_^letsStawt", "pwbwic", "pwotected", "pwiwate", "awnd", "orw", "nawt", "xwr", "eqwals", "uwse", "dewete"};

  public static final String[] DATA_TYPES = {"Numwer"};

  String type;
  Object value;

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
    return (type.equals(t.type) && value.equals(t.value));
  }

}