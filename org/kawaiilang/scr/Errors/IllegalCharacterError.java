package org.kawaiilang;

class IllegalCharacterError extends org.kawaiilang.Error {
  public IllegalCharacterError(Position positionEnd, String message) {
    super(positionEnd, "IwegalChawacterEwwor", message);
  }
}