package org.kawaiilang;

//A token class for determining the instance type of an Object
class InstanceToken implements Token {

  private InstanceToken parent;
  private OwObject instanceOf;	//Points to the master class
  
  public static final String TYPE = TokenV1.TT_VARTYPE;	//This class actually is treated like any other vartype
  public static final InstanceToken BASE_CLASS = new InstanceToken(null, null);
  
  public InstanceToken(OwObject instanceOf) {
	this.parent = BASE_CLASS;
	this.instanceOf = instanceOf;
  }
  
  public InstanceToken(InstanceToken parent, OwObject instanceOf) {
	this.parent = parent;
	this.instanceOf = instanceOf;
  }
  
  public String getType() {
	return TYPE;
  }

  public Object getValue() {
	//In this case, the full name of the class
	return instanceOf.className();
  }

  public Token clone() {
	return new InstanceToken(parent, instanceOf);
  }
  
  public boolean equals(Object o) {
	if (o instanceof InstanceToken) {
	  InstanceToken t = (InstanceToken) o;
	  return this.instanceOf.equals(t.instanceOf);
	} else {
	  return false;
	}
  }
  
  public int hashCode() {
    return instanceOf.hashCode();
  }

}