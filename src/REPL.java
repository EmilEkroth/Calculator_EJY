import java.util.Scanner;

public class REPL {
    public static void main(String[] args) {
        REPL repl = new REPL();
        repl.Run();
    }

    void Run ()
    {
        Scanner sc = new Scanner(System.in);
        Calculator calc = new Calculator();
        String in = "";
        System.out.println("Calculator_EJY\ninput 'exit' to exit program");

        while (!in.equals("exit"))
        {
            System.out.print("Write expression >");
            in = sc.nextLine();
            System.out.println(in + " = " + calc.Calc(in));
        }
    }
}
