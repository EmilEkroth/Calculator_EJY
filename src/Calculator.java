import java.lang.reflect.Array;
import java.util.*;

import static java.lang.Double.NaN;
import static java.lang.Double.doubleToLongBits;
import static java.lang.Math.pow;
import static java.lang.Math.toIntExact;

public class Calculator {

    // Here are the only allowed instance variables!
    // Error messages (more on static later)
    final static String MISSING_OPERAND = "Missing or bad operand";
    final static String DIV_BY_ZERO = "Division with 0";
    final static String MISSING_OPERATOR = "Missing operator or parenthesis";
    final static String OP_NOT_FOUND = "Operator not found";

    // Definition of operators
    final static String OPERATORS = "+-*/^";

    // Method used in REPL
    double eval(String expr) {
        if (expr.length() == 0) {
            return NaN;
        }
        List<String> tokens = tokenize(expr);
        List<String> postfix = infix2Postfix(tokens);
        return evalPostfix(postfix);
    }

    public double Calc (String input)
    {
        String equ = "("+input+")";
        List<String> tokens = tokenize(equ);
        List<String> postfix = infix2Postfix(tokens);
        double out = evalPostfix(postfix);
        return out;
    }

    // ------  Evaluate RPN expression -------------------

    public double evalPostfix(List<String> postfix)
    {
        Stack<Double> stack = new Stack<Double>();
        for(int i = 0; i < postfix.size(); i++)
        {
            try {
                double nmb = Double.parseDouble(postfix.get(i));
                stack.add(nmb);
            } catch(Exception e)
            {
                double a = stack.pop();
                double b = stack.pop();

                stack.push(applyOperator(postfix.get(i), a, b));
            }
        }
        return stack.pop();
    }


    double applyOperator(String op, double d1, double d2) {
        switch (op) {
            case "+":
                return d1 + d2;
            case "-":
                return d2 - d1;
            case "*":
                return d1 * d2;
            case "/":
                if (d1 == 0) {
                    throw new IllegalArgumentException(DIV_BY_ZERO);
                }
                return d2 / d1;
            case "^":
                return pow(d2, d1);
        }
        throw new RuntimeException(OP_NOT_FOUND);
    }

    // ------- Infix 2 Postfix ------------------------

    public List<String> infix2Postfix(List<String> tokens) // sorts list from infix to postfix
    {
        Stack<String> stack = new Stack<String>();
        ArrayList<String> postfix = new ArrayList<String>();
        for (int i = 0; i < tokens.size(); i++)
        {
            try {
                int test = Integer.parseInt(tokens.get(i));
                postfix.add(tokens.get(i));
            } catch(Exception e)
            {
                //System.out.println("debug: " + tokens.get(i));

                if (tokens.get(i).equals("(")) {
                    stack.push(tokens.get(i));
                }
                else if (tokens.get(i).equals(")")) // pops elements inside parenthesis to prefix;
                {
                    while (!stack.peek().equals("("))
                    {
                        if(stack.size() == 0) throw new RuntimeException(MISSING_OPERATOR);
                        postfix.add(stack.pop());
                    }
                    stack.pop();
                } else {
                    while (stack.size() != 0 && !stack.peek().equals("(") &&  getPrecedence(stack.peek()) >= getPrecedence(tokens.get(i)))
                        //check if procedure in stack has higher value than current procedure
                    {
                        postfix.add(stack.pop());
                    }
                    stack.push(tokens.get(i));
                }
            }
        }
        return postfix;
    }


    int getPrecedence(String op) {
        if ("+-".contains(op)) {
            return 2;
        } else if ("*/".contains(op)) {
            return 3;
        } else if ("^".contains(op)) {
            return 4;
        } else {
            throw new RuntimeException(OP_NOT_FOUND + " : [" + op + "]");
        }
    }
    /*
    Assoc getAssociativity(String op) {
        if ("+-/*".contains(op)) {
            return Assoc.LEFT;
        } else if ("^".contains(op)) {
            return Assoc.RIGHT;
        } else {
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }

    enum Assoc {
        LEFT,
        RIGHT
    }
*/
    // ---------- Tokenize -----------------------

    public List<String> tokenize(String expr)
    {
        //break string up into tokens: number, operand or parenthesis
        List<String> list = new ArrayList<String>();
        String[] in = expr.split("");
        for (int i = 0; i < in.length; i++)
        {
            if(!Character.isWhitespace(in[i].charAt(0)))
            {
                try {
                    int test = Integer.parseInt(in[i]);
                    String c = in[i];
                    int n = i + 1;
                    while (true) {
                        try {
                            int test2 = Integer.parseInt(in[n]);
                            c += in[n];
                            n++;
                        } catch (Exception e) {
                            break;
                        }
                    }
                    i = n-1;
                    list.add(c);
                } catch (Exception e)
                {
                    list.add(in[i]);
                }
            }
        }
        return list;
    }

    // TODO Possibly more methods
}
