import java.util.Scanner;
import org.kawaiilang.*;

class Main {
  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);
    while (true) {
      System.out.print("KawaiiLang> ");
      String input = s.nextLine();
      if (input.equals("Senpai.stawpp UwU UwU!")) {
        /*long startTime = System.nanoTime();
        int i = 1 + 1;
        long endTime = System.nanoTime();
        System.out.print("Time taken: ");
        System.out.print(i);
        System.out.println(endTime - startTime);*/
        s.close();
        break;
      } else {
        //long startTime = System.nanoTime();
        Object result = Runner.run(input);
        //long endTime = System.nanoTime();
        //System.out.print("Time taken: ");
        //System.out.println(endTime - startTime);
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