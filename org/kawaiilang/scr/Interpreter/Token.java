package org.kawaiilang;

public interface Token extends Cloneable {

  String getType();
  Object getValue();
  Token clone();
  
}