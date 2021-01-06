package org.kawaiilang;

public class Variable {

  private Token type;
  private Object value;

  public Variable(Token type, Object value) {
    this.type = type;
    this.value = value;
  }

  public Token getType() {
    return type;
  }

  public Object getValue() {
    return value;
  }

}