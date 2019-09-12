import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

    List<String> infix = Arrays.asList(new String[]{"1", "+", "2", "*", "(", "3", "/", "4", ")", "-", "5", "+", "6"});

    public void TestInfix2Postfix ()
    {
        Calculator calc = new Calculator();
        System.out.println(calc.infix2Postfix(infix));
    }
}
