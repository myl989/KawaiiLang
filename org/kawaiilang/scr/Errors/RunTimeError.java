package org.kawaiilang;

class RunTimeError extends org.kawaiilang.Error {
  public RunTimeError(Position positionStart, Position positionEnd, String message) {
    super(positionStart, positionEnd, "RwnTwimeEwwor", message);
  }
}