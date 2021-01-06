package org.kawaiilang;

public class Token {
  
  public static final String DIGITS = "0123456789";
  public static final String TT_INT = "INT";
  public static final String TT_FLOAT = "FLOAT";
  public static final String TT_ADD = "+";
  public static final String TT_MINUS = "-";
  public static final String TT_MUL = "*";
  public static final String TT_DIV = "/";
  public static final String TT_LPAREN = "(";
  public static final String TT_RPAREN = ")";

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
    return (type == t.type && value == t.value);
  }

}