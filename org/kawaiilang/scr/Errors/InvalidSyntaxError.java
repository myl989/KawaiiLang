package org.kawaiilang;

class InvalidSyntaxError extends org.kawaiilang.Error {
  public InvalidSyntaxError(Position positionEnd, String message) {
    super(positionEnd, "InwawidSwntaxEwwor", message);
  }
}