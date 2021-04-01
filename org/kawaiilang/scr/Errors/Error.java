package org.kawaiilang;

abstract class Error extends TokenV1 {

    private Position positionEnd;
  
    //internal use only
    Error(Position positionEnd, String type, String message) {
      super(type, message);
      this.positionEnd = positionEnd;
    }
  
    public String toString() {
      return new StringBuilder("file ").append(positionEnd.getFileName()).append(": ").append(getType()).append(": ").append(getValue().toString()).toString();
    }
  }