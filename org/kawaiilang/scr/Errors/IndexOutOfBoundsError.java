package org.kawaiilang;

class IndexOutOfBoundsError extends org.kawaiilang.Error {
  public IndexOutOfBoundsError(Position positionStart, Position positionEnd, String message) {
    super(positionStart, positionEnd, "InwexOwtOfwBwowndsEwwor", message);
  }
}