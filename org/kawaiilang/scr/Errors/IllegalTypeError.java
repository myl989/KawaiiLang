package org.kawaiilang;

class IllegalTypeError extends org.kawaiilang.Error {
  public IllegalTypeError(Position positionEnd, String message) {
    super(positionEnd, "IwegalTwypeEwwor", message);
  }
}