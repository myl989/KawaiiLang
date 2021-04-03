package org.kawaiilang;

class Variable {

  private Token type;
  private Object value;
  private int scopeDepth;

  public Variable(Token type, Object value, int scopeDepth) {
    this.type = type;
    this.value = value;
    this.scopeDepth = scopeDepth;
  }

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

  public int scopeDepth() {
    return scopeDepth;
  }

}