/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infinite;

/**
 *
 * A simple program with infinite loops for testing LTL verification
 *
 * @author Michele
 */
import gov.nasa.jpf.ltl.LTLSpec;
import java.util.Random;

@LTLSpec("<>\"done()\"")
public class Variables {
 
  static boolean bool;
  static int a = 0, b = 0;
  static Random random = new Random();
  static String str = "ciao";

  public static void main(String[] args) {
    System.out.println("Sono in main");
    int f = 5;
    boolean bool_1 = true;
    bool_1 = 3.2<4.0;
    if(bool_1){}
    String cst = "ciao";
    int i = Mic1();
    i = i + 1;
  }
  
  public static int Mic1() {
    System.out.println("sono in Mic1");
    
    a = random.nextInt(5);
    b = random.nextInt(3);
    str = "io";
    String str2 = str;
    System.out.println("a = "+a);
    System.out.println("b = "+b);
    bool = false;
    bool = a>1;
    if(a>1){
      System.out.println("a>1");
      int c = a+b;
      if(a<3){
        System.out.println("a<3");
      }
      return a;
    }else{
      return b;
    }
  }

  public Variables(int a, int b) {
    this.a = a;
    this.b = b;
  }

  public int getA() {
    return a;
  }

  public int getB() {
    return b;
  }

  public String string() {
    return "Stringa";
  }
  // public Prova 
}
