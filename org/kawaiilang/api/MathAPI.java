package org.kawaiilang.api;

import org.kawaiilang.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MathAPI {

  // The math API contains several OwO functions, as well as Java wrappers around
  // some functions.

  private MathAPI() {
  }

  // Pi senpai
  static class Pi extends BuiltInFunction {
     Pi(KawaiiLangRuntime interpreter) {
      super(interpreter, "Mwath", "pi", TokenV1.C_NUMVT);
    }

    public Object call() {
      return Double.valueOf(Math.PI);
    }
  }

  // Euler senpai
  static class E extends BuiltInFunction {
     E(KawaiiLangRuntime interpreter) {
      super(interpreter, "Mwath", "e", TokenV1.C_NUMVT);
    }

    public Object call() {
      return Double.valueOf(Math.E);
    }
  }

  static class Acos extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
    }

     Acos(KawaiiLangRuntime runtime) {
      super(runtime, "Mwath", "acos", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        return Math.acos((Double) inputs.get(0));
      } else {
        return ci;
      }
    }
  }

  static class Asin extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
    }

     Asin(KawaiiLangRuntime runtime) {
      super(runtime, "Mwath", "asin", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        return Math.acos((Double) inputs.get(0));
      } else {
        return ci;
      }
    }
  }

  static class Atan extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
    }

     Atan(KawaiiLangRuntime runtime) {
      super(runtime, "Mwath", "atan", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        return Math.acos((Double) inputs.get(0));
      } else {
        return ci;
      }
    }
  }

  static class Atan2 extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
    }

     Atan2(KawaiiLangRuntime runtime) {
      super(runtime, "Mwath", "atan2", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        return Math.acos((Double) inputs.get(0));
      } else {
        return ci;
      }
    }
  }

  static class Cos extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
    }

     Cos(KawaiiLangRuntime runtime) {
      super(runtime, "Mwath", "cos", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        return Math.cos((Double) inputs.get(0));
      } else {
        return ci;
      }
    }
  }

  static class Sin extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
    }

     Sin(KawaiiLangRuntime runtime) {
      super(runtime, "Mwath", "sin", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        return Math.sin((Double) inputs.get(0));
      } else {
        return ci;
      }
    }
  }

  static class Tan extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
    }

     Tan(KawaiiLangRuntime runtime) {
      super(runtime, "Mwath", "tan", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        return Math.tan((Double) inputs.get(0));
      } else {
        return ci;
      }
    }
  }

  static class Cosh extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
    }

     Cosh(KawaiiLangRuntime runtime) {
      super(runtime, "Mwath", "cosh", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        return Math.cosh((Double) inputs.get(0));
      } else {
        return ci;
      }
    }
  }

  static class Sinh extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
    }

     Sinh(KawaiiLangRuntime runtime) {
      super(runtime, "Mwath", "sinh", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        return Math.sinh((Double) inputs.get(0));
      } else {
        return ci;
      }
    }
  }

  static class Tanh extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
    }

     Tanh(KawaiiLangRuntime runtime) {
      super(runtime, "Mwath", "tanh", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        return Math.tanh((Double) inputs.get(0));
      } else {
        return ci;
      }
    }
  }

  static class Sqrt extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
    }

     Sqrt(KawaiiLangRuntime runtime) {
      super(runtime, "Mwath", "sqrt", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        return Math.sqrt((Double) inputs.get(0));
      } else {
        return ci;
      }
    }
  }

  static class Cbrt extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
    }

     Cbrt(KawaiiLangRuntime runtime) {
      super(runtime, "Mwath", "cbrt", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        return Math.cbrt((Double) inputs.get(0));
      } else {
        return ci;
      }
    }
  }

  static class Pow extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
      parameters.put("a", "Numwer");
    }

     Pow(KawaiiLangRuntime runtime) {
      super(runtime, "Mwath", "expowonwent", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        return Math.pow(((Double) inputs.get(0)), ((Double) inputs.get(1)));
      } else {
        return ci;
      }
    }
  }

  static class Log10 extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
    }

     Log10(KawaiiLangRuntime runtime) {
      super(runtime, "Mwath", "lwg", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        return Math.log10((Double) inputs.get(0));
      } else {
        return ci;
      }
    }
  }

  static class Log extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
    }

     Log(KawaiiLangRuntime runtime) {
      super(runtime, "Mwath", "lwn", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        return Math.log((Double) inputs.get(0));
      } else {
        return ci;
      }
    }
  }

  static class Round extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
    }

     Round(KawaiiLangRuntime runtime) {
      super(runtime, "Mwath", "ruwund", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        return Math.round((Double) inputs.get(0));
      } else {
        return ci;
      }
    }
  }

  static class Floor extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
    }

     Floor(KawaiiLangRuntime runtime) {
      super(runtime, "Mwath", "ruwundDwwn", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        return Math.floor((Double) inputs.get(0));
      } else {
        return ci;
      }
    }
  }

  static class Ceil extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
    }

     Ceil(KawaiiLangRuntime runtime) {
      super(runtime, "Mwath", "ruwundUwup", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        return Math.ceil((Double) inputs.get(0));
      } else {
        return ci;
      }
    }
  }

}