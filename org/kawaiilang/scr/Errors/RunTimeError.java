package org.kawaiilang;

class RunTimeError extends org.kawaiilang.Error {
  public RunTimeError(Position positionEnd, String message) {
    super(positionEnd, "RwnTwimeEwwor", message);
  }
}