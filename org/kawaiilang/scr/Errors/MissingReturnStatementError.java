package org.kawaiilang;

class MissingReturnStatementError extends org.kawaiilang.Error {
  public MissingReturnStatementError(Position positionStart, Position positionEnd, String message) {
    super(positionStart, positionEnd, "MwissingWeturnStwatemwentEwwor", message);
  }
}