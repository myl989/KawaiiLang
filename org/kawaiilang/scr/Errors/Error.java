package org.kawaiilang;

public class Error extends Token {

  private Position positionStart;
  private Position positionEnd;

  Error(Position positionStart, Position positionEnd, String type, String message) {
    super(type, message);
    this.positionStart = positionStart;
    this.positionEnd = positionEnd;
  }

  public Error(Position positionStart, Position positionEnd, String message) {
    this(positionStart, positionEnd, "Ewwor", message);
  }

  public String toString() {
    return new StringBuilder("File ").append(positionStart.getFileName()).append(", line ").append(positionStart.getLine() + 1).append(":").append(type).append(":").append(value.toString()).toString();
  }
}