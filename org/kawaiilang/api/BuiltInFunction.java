package org.kawaiilang.api;
import java.util.LinkedHashMap;

import org.kawaiilang.*;

public class BuiltInFunction extends Function {

  private String api;
  
  public BuiltInFunction(KawaiiLangRuntime runtime, String apiName, String name, Token canGibU) {
	super(runtime, name, canGibU);
	api = apiName;
  }
  
  public BuiltInFunction(KawaiiLangRuntime runtime, String apiName, String name, LinkedHashMap<String, String> parameters, Token canGibU) {
	super(runtime, name, parameters, canGibU);
	api = apiName;
  }
  
  public String toString() {
	return new StringBuilder("<Function ").append(getName()).append(" from built-in API \"CoreAPI").append(api).append("\">").toString();
  }
  
}