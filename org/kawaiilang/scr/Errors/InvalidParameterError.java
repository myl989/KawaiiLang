package org.kawaiilang;

class InvalidParameterError extends org.kawaiilang.Error {
  public InvalidParameterError(Position positionStart, Position positionEnd, String message) {
    super(positionStart, positionEnd, "InwawidPawamweterEwwor", message);
  }
}