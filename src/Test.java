import javafx.application.Application;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        Test test = new Test();
        test.TestCalc();
    }
    List<String> infix = Arrays.asList(new String[]{"(","3", "+", "2", "/", "3","*","(", "4", "+","5",")", "-", "6",")"});
    String expression = "3*2+ 23 *(5/(3+2))";

    public void TestCalc ()
    {
        Calculator calc = new Calculator();
        System.out.println(calc.tokenize(expression));
        /*System.out.println(calc.infix2Postfix(infix));
        System.out.println(calc.evalPostfix(calc.infix2Postfix(infix)));*/
        System.out.println(calc.infix2Postfix(calc.tokenize(expression)));
        System.out.println(calc.evalPostfix(calc.infix2Postfix(calc.tokenize(expression))));

        System.out.println(calc.Calc(expression));
    }
}
