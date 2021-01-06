package org.kawaiilang;

public class IllegalCharacterError extends org.kawaiilang.Error {
  public IllegalCharacterError(Position positionStart, Position positionEnd, String message) {
    super(positionStart, positionEnd, "IllegalCharacterError", message);
  }
}