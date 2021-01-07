package org.kawaiilang;

class InvalidSyntaxError extends org.kawaiilang.Error {
  public InvalidSyntaxError(Position positionStart, Position positionEnd, String message) {
    super(positionStart, positionEnd, "InwawidSwntaxEwwor", message);
  }
}