package org.kawaiilang;

public class InvalidSyntaxError extends Error {
  public InvalidSyntaxError(Position positionStart, Position positionEnd, String message) {
    super(positionStart, positionEnd, "InvalidSyntaxError", message);
  }
}