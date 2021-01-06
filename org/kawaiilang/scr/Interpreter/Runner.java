package org.kawaiilang;
import java.util.HashMap;

public class Runner {

  private Runner() {

  }

  public static final HashMap<String, String> UWU_KEY;

  static {

    HashMap<String, String> uwuKi = new HashMap<>();
    uwuKi.put("awdd", "+");
    uwuKi.put("mwinws", "-");
    uwuKi.put("mwltipwy", "*");
    uwuKi.put("diwide", "/");
    UWU_KEY = uwuKi;

  }

  public static String unUwUfy(String uwufiedText) {
    String[] lines = uwufiedText.split("\n");
    for (int i = 0; i < lines.length; i++) {
      for (String s : UWU_KEY.keySet()) {
        String beginning = new StringBuilder(s).append(" ").toString();
        if (lines[i].startsWith(beginning)) {
          String subLine = lines[i].substring(s.length() - 1);
          lines[i] = new StringBuilder(UWU_KEY.get(s)).append(subLine).toString();
        }
        String toReplace = new StringBuilder(" ").append(s).append(" ").toString();
        String replaceWith = new StringBuilder(" ").append(UWU_KEY.get(s)).append(" ").toString();
        lines[i] = lines[i].replaceAll(toReplace, replaceWith);
      }
    }
    StringBuilder strBuilder = new StringBuilder();
    for (String line : lines) {
      strBuilder.append(line).append("\n");
    }
    return strBuilder.toString();
  }

  public static Object run(String fileName, String text) {
    Lexer lexer = new Lexer(fileName, unUwUfy(text));
    Token[] tokens = lexer.makeTokens();
    Interpreter interpreter = new Interpreter(fileName, tokens);
    Object result = interpreter.interpret();
    return result;
  }

}