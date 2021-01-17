import java.io.IOException;
import org.kawaiilang.Runner;
import org.kawaiilang.Shell;

class Main {
  public static void main(String[] args) {
    if (args.length > 0) {
      Runner r = new Runner(args[0]);
      try {
        r.run();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    } else {
      Shell.main(args);
    }
  }
  
}