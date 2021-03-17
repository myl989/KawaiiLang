package org.kawaiilang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Switch {

  private KawaiiLangRuntime runtime;
  private ArrayList<ArrayList<Token[]>> proc = new ArrayList<>();
  private ArrayList<Token[]> defaults = null;
  private ArrayList<Integer> nums = new ArrayList<>();
  
  //Unique blank switch object for handling default cases.
  public static final Switch BLANK_SWITCH = new Switch(null);
  
  static {
	Token[] ta = {TokenV1.NULLTOKEN};
	ArrayList<Token[]> a = new ArrayList<>();
	a.add(ta);
	BLANK_SWITCH.defaults = a;
  }
  
  public Switch(KawaiiLangRuntime runtime) {
	this.runtime = runtime;
  }
  
  public void addStatement(int num, ArrayList<Token[]> statements) {
	nums.add(num);
	proc.add(statements);
  }
  
  public void addDefaults(ArrayList<Token[]> statements) {
	defaults = statements;
  }
  
  public void doSwitch(int num) {
	int idx = nums.indexOf(num);
	
	//Item is not in switch
	if (idx < 0) {
	  if (defaults != null) {	//If there are default statements
		for (Token[] action : defaults) {
		  //Make sure that statements like nextcase are not interpreted.
		  if (action.length > 0 && !action[0].equals(TokenV1.C_NEXTCASEK)) {
			new Runner(runtime.getFileLocation(), runtime).interpret(action);
			return;
		  }
		}
	  }
	  return;
	}
	
	for (int i = idx; i < proc.size(); i++) {
	  ArrayList<Token[]> actions = proc.get(i);
	  if (!actions.isEmpty()) {
		for (Token[] action : actions) {
		  //Make sure that statements like nextcase are not interpreted.
		  if (action.length > 0 && !action[0].equals(TokenV1.C_NEXTCASEK)) {
		  	Object o = new Runner(runtime.getFileLocation(), runtime).interpret(action);
		  	if (o != null && o.equals(KawaiiLangRuntime.BREAK_OBJECT)) {	//If the code tells to break (no fall-thru)
				return;
		  	}
		  }
		}	//End actions loop
	  }
	}	//End procedure (fall-thru) loop
	//Fall-thru to default case
	if (defaults != null) {
		for (Token[] action : defaults) {
		  //Make sure that statements like nextcase are not interpreted.
		  if (action.length > 0 && !action[0].equals(TokenV1.C_NEXTCASEK)) {
			new Runner(runtime.getFileLocation(), runtime).interpret(action);
			return;
		  }
		}
	  }
  }	//End function
  
  public String toString() {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < nums.size(); i++) {
	  sb.append(nums.get(i)).append(":\n");
	  ArrayList<Token[]> actions = proc.get(i);
	  for (Token[] action : actions) {
		sb.append(Arrays.toString(action));
		sb.append('\n');
	  }
	}
	if (defaults != null) {
	  sb.append("default:\n");
	  for (Token[] action : defaults) {
		sb.append(Arrays.toString(action));
		sb.append('\n');
	  }
	}
	return sb.toString();
  }
  
  public boolean equals(Object o) {
	if (o instanceof Switch) {
	  Switch s = (Switch) o;
	  boolean a = (proc == null || s.proc == null) ? proc == s.proc : proc.equals(s.proc);
	  boolean b = (nums == null || s.nums == null) ? nums == s.nums : nums.equals(s.nums);
	  boolean c = (defaults == null || s.defaults == null) ? defaults == s.defaults : defaults.equals(s.defaults);
	  return a && b && c;
	} else {
	  return false;
	}
  }
  
  public int hashCode() {
	int hash = 7;
	hash = 31 * hash + (proc == null ? 0 : proc.hashCode());
	hash = 31 * hash + (defaults == null ? 0 : defaults.hashCode());
	hash = 31 * hash + (nums == null ? 0 : nums.hashCode());
	return hash;
  }
  
}