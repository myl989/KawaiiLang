package org.kawaiilang;

public class IllegalCharacterError extends Error {
  public IllegalCharacterError(Position positionStart, Position positionEnd, String message) {
    super(positionStart, positionEnd, "IllegalCharacterError", message);
  }
}