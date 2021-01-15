package org.kawaiilang;

class IllegalAssignmentError extends org.kawaiilang.Error {
  public IllegalAssignmentError(Position positionStart, Position positionEnd, String message) {
    super(positionStart, positionEnd, "IwegalAsswignmwentEwwor", message);
  }
}