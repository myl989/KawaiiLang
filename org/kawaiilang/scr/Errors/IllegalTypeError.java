package org.kawaiilang;

class IllegalTypeError extends org.kawaiilang.Error {
  public IllegalTypeError(Position positionStart, Position positionEnd, String message) {
    super(positionStart, positionEnd, "IwegalTwypeEwwor", message);
  }
}