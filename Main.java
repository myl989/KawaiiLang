import java.util.Scanner;
import org.kawaiilang.*;

class Main {
  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);
    while (true) {
      System.out.print("KawaiiLang> ");
      String input = s.nextLine();
      if (input.equals("Senpai.stawpp UwU UwU!")) {
        s.close();
        break;
      } else {
        Object result = Runner.run("<stdin>", input);
        if (result == null) {
          System.out.println("nwthin");
        } else {
          if (result instanceof Integer) {
            Integer i = (Integer) result;
            System.out.println(Integer.toString(i));
          } else {
            System.out.println(result);
          }
        }
      }
    }
  }
}