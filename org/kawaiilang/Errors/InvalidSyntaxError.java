package org.kawaiilang;

public class InvalidSyntaxError extends org.kawaiilang.Error {
  public InvalidSyntaxError(Position positionStart, Position positionEnd, String message) {
    super(positionStart, positionEnd, "InvalidSyntaxError", message);
  }
}