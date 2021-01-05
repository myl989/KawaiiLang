import java.util.Arrays;
import java.util.ArrayList;
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
        Token[] result = Lexer.run("<stdin>", input);
        System.out.println(Arrays.toString(result));
      }
    }
  }
}