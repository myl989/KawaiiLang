package org.kawaiilang;

class InvalidParameterError extends org.kawaiilang.Error {
  public InvalidParameterError(Position positionEnd, String message) {
    super(positionEnd, "InwawidPawamweterEwwor", message);
  }
}