package org.kawaiilang.api;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.kawaiilang.*;

public abstract class BuiltInFunction extends AbstractFunction {

  private String api;
  
  public BuiltInFunction(KawaiiLangRuntime runtime, String apiName, String name, Token canGibU) {
	  super(runtime, name, canGibU);
	  api = apiName;
  }
  
  public BuiltInFunction(KawaiiLangRuntime runtime, String apiName, String name, LinkedHashMap<String, String> parameters, Token canGibU) {
	  super(runtime, name, parameters, canGibU);
	  api = apiName;
  }
  
  @Override
  public String toString() {
	  return new StringBuilder("<Function ").append(getName()).append(" from built-in API \"").append(api).append("\">").toString();
  }
  
}