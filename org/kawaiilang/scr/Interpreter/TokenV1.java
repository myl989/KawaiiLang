package org.kawaiilang;

//A class for storing information of the code, 
//including but not limited to tokens of the code
//also including data such as variable names, types, variables, errors and data values
//However, in the future, new classes/enums for non-syntax data should be created
public class TokenV1 implements Token {
  
  public static final String CHARS_ALLOWED_IN_IDENTIFIERS = "^_?:>=<";

  //Types of tokens and other strings required for token creation
  public static final String TT_INT = "INT";
  public static final String TT_FLOAT = "FLOAT";
  public static final String TT_STR = "STR";
  public static final String TT_LIST = "LIST";
  public static final String TT_NOTHING = "NOTHING";

  public static final String TT_ADD = "+";
  public static final String TT_MINUS = "-";
  public static final String TT_MUL = "ᴹ";
  public static final String TT_DIV = "/";
  public static final String TT_MOD = "%";
  public static final String TT_LPAREN = "(";
  public static final String TT_RPAREN = ")";

  public static final String TT_EQUALS = "EQUALS";
  public static final String TT_LT = "LT";
  public static final String TT_GT = "GT";
  public static final String TT_LTE = "LTE";
  public static final String TT_GTE = "GTE";

  public static final String TT_ASSIGN = "ASSIGN";
  public static final String TT_TILDE = "TILDE";
  public static final String TT_VARNAME = "VARNAME";
  public static final String TT_VARTYPE = "VARTYPE";
  public static final String TT_KEYWORD = "KEYWORD";

  public static final String TT_STARTIF = "STARTIF";
  public static final String TT_ENDIF = "ENDIF";

  public static final String TT_PARAM = "UwU";
  public static final String TT_COMMA = "COMMA";

  public static final String TT_LSQU = "LSQU";
  public static final String TT_RSQU = "RSQU";
  
  public static final String TT_CLASS = "CLASS";
  
  public static final String TT_INCLUDE = "INCLUDE";

  public static final String[] KEYWORDS = {"OwO", "notices", "^_^ewndNotice", "^_^wepeatDat", "^_^nwextCase", "othwerwiseDo", "cwass", "^_^ewndCwass", "extwends", "hewesHwwUMake", "^_^ewndMake", "make", "do", "doWen", "tw", "twimes", "ewlse", "stawp", "awnd", "orw", "nawt", "xwr", "dewete", "canDo", "canGibU", "gibU", "finishMaking", "^_^ewndCanDo", "owerlwoad"};

  public static final String[] DATA_TYPES = {"Numwer", "Fwnctwion", "Stwing", "Lwist", "Anwy", "Cwass"};
  
  //Cached tokens, used for token comparisons
  //To save memory, some unused tokens are commented out.
  public static final Token NULLTOKEN = new TokenV1(null);	//Unique null token for internal operations
  
  public static final Token C_NOTHING = new TokenV1(TT_NOTHING);
  public static final Token C_TILDE = new TokenV1(TT_TILDE);
  
  public static final Token C_ANYVT = new TokenV1(TT_VARTYPE, "Anwy");
  public static final Token C_NUMVT = new TokenV1(TT_VARTYPE, "Numwer");
  public static final Token C_FUNCVT = new TokenV1(TT_VARTYPE, "Fwnctwion");
  public static final Token C_STRVT = new TokenV1(TT_VARTYPE, "String");
  public static final Token C_LISTVT = new TokenV1(TT_VARTYPE, "Lwist");
  public static final Token C_CLASSVT = new TokenV1(TT_VARTYPE, "Cwass");
  
  public static final Token C_CLASSK = new TokenV1(TT_KEYWORD, "cwass");
  public static final Token C_ENDCLASSK = new TokenV1(TT_KEYWORD, "^_^ewndCwass");
  public static final Token C_EXTENDSK = new TokenV1(TT_KEYWORD, "extwends");
  public static final Token C_CONSTRUCTOR = new TokenV1(TT_KEYWORD, "hewesHwwUMake");
  public static final Token C_ENDCONSTRUCTOR = new TokenV1(TT_KEYWORD, "^_^ewndMake");
  public static final Token C_OWOK = new TokenV1(TT_KEYWORD, "OwO");
  public static final Token C_IFK = new TokenV1(TT_KEYWORD, "notices");	//Not to be confused with C_STARTIF
  public static final Token C_ENDIFK = new TokenV1(TT_KEYWORD, "^_^ewndNotice");	//Not to be confused with C_ENDIF
  public static final Token C_NEXTCASEK = new TokenV1(TT_KEYWORD, "^_^nwextCase");
  public static final Token C_OTHERWISEDOK = new TokenV1(TT_KEYWORD, "othwerwiseDo");
  public static final Token C_ENDLOOPK = new TokenV1(TT_KEYWORD, "^_^wepeatDat");
  public static final Token C_FORK = new TokenV1(TT_KEYWORD, "do");
  public static final Token C_DOK = C_FORK;	//Alias to C_FORK
  public static final Token C_WHILEK = new TokenV1(TT_KEYWORD, "doWen");
  public static final Token C_TOK = new TokenV1(TT_KEYWORD, "tw");
  public static final Token C_TIMESK = new TokenV1(TT_KEYWORD, "twimes");
  public static final Token C_ELSEK = new TokenV1(TT_KEYWORD, "ewlse");
  public static final Token C_BREAKK = new TokenV1(TT_KEYWORD, "stawp");
  public static final Token C_DELETEK = new TokenV1(TT_KEYWORD, "dewete");
  public static final Token C_FUNCK = new TokenV1(TT_KEYWORD, "canDo");
  public static final Token C_CANRETURNK = new TokenV1(TT_KEYWORD, "canGibU");
  public static final Token C_RETURNK = new TokenV1(TT_KEYWORD, "gibU");
  public static final Token C_RETURNCONSTRUCTEDK = new TokenV1(TT_KEYWORD, "finishMake");
  public static final Token C_ENDFUNCK = new TokenV1(TT_KEYWORD, "^_^ewndCanDo");
  public static final Token C_OVERLOADK = new TokenV1(TT_KEYWORD, "owerlwoad");
  
  public static final Token C_ANDK = new TokenV1(TT_KEYWORD, "awnd");
  public static final Token C_ORK = new TokenV1(TT_KEYWORD, "orw");
  public static final Token C_NOTK = new TokenV1(TT_KEYWORD, "nawt");
  public static final Token C_XORK = new TokenV1(TT_KEYWORD, "xwr");
  
  //public static final Token C_ASSIGN = new TokenV1(TT_ASSIGN, "iws");
  public static final Token C_EQUALS = new TokenV1(TT_EQUALS, "eqwals");
  public static final Token C_LT = new TokenV1(TT_LT);
  public static final Token C_GT = new TokenV1(TT_GT);
  public static final Token C_LTE = new TokenV1(TT_LTE);
  public static final Token C_GTE = new TokenV1(TT_GTE);

  /*public static final Token C_ADD = new TokenV1(TT_ADD);
  public static final Token C_MINUS = new TokenV1(TT_MINUS);
  public static final Token C_MUL = new TokenV1(TT_MUL);
  public static final Token C_DIV = new TokenV1(TT_DIV);
  public static final Token C_MOD = new TokenV1(TT_MOD);
  public static final Token C_LPAREN = new TokenV1(TT_LPAREN);
  public static final Token C_RPAREN = new TokenV1(TT_RPAREN);*/
  
  public static final Token C_STARTIF = new TokenV1(TT_STARTIF);
  public static final Token C_ENDIF = new TokenV1(TT_ENDIF);
  /*public static final Token C_LSQU = new TokenV1(TT_LSQU);
  public static final Token C_RSQU = new TokenV1(TT_RSQU);*/
  public static final Token C_PARAM = new TokenV1(TT_PARAM);
  public static final Token C_COMMA = new TokenV1(TT_COMMA);
  
  public static final Token C_INCLUDE = new TokenV1(TT_INCLUDE);
  
  private String type;
  private Object value;

  public String getType() {
    return type;
  }

  public Object getValue() {
    return value;
  }

  public TokenV1(String type, Object value) {
    this.type = type;
    this.value = value;
  }

  public TokenV1(String type) {
    this.type = type;
    this.value = null;
  }

  public String toString() {
    if (value != null) {
      return new StringBuilder(type).append(":").append(value.toString()).toString();
    } else {
      return type;
    }
  }

  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + type.hashCode();
    if (value == null) {
      return hash;
    } else {
      hash = 31 * hash + value.hashCode();
      return hash;
    }
  }

  public boolean equals(Object o) {
    if (!(o instanceof Token)) {
      return false;
    }
    Token t = (Token) o;
    if (value != null) {
      return (type.equals(t.getType()) && value.equals(t.getValue()));  
    } else {
      return type.equals(t.getType());
    }
  }

  public Token clone() {
    return new TokenV1(type, value);
  }

  public static Token[] cloneTokenArray(Token[] tokenArray) {
    Token[] clone = new TokenV1[tokenArray.length];
    for (int i = 0; i < tokenArray.length; i++) {
      clone[i] = tokenArray[i].clone();
    }
    return clone;
  }

}