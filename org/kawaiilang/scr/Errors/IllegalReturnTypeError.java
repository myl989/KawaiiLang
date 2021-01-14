package org.kawaiilang;

class IllegalReturnTypeError extends org.kawaiilang.Error {
  public IllegalReturnTypeError(Position positionStart, Position positionEnd, String message) {
    super(positionStart, positionEnd, "IwegalGibUTwypeEwwor", message);
  }
}