package org.kawaiilang;

abstract class Error extends TokenV1 {

    private Position positionStart;
    private Position positionEnd;
  
    //internal use only
    Error(Position positionStart, Position positionEnd, String type, String message) {
      super(type, message);
      this.positionStart = positionStart;
      this.positionEnd = positionEnd;
    }
  
    public String toString() {
      return new StringBuilder("file ").append(positionStart.getFileName()).append(": ").append(getType()).append(": ").append(getValue().toString()).toString();
    }
  }