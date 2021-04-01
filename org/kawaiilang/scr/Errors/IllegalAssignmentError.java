package org.kawaiilang;

class IllegalAssignmentError extends org.kawaiilang.Error {
  public IllegalAssignmentError(Position positionEnd, String message) {
    super(positionEnd, "IwegalAsswignmwentEwwor", message);
  }
}