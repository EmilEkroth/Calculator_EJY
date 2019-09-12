import java.util.*;

import static java.lang.Double.NaN;
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

    // ------  Evaluate RPN expression -------------------

    public double evalPostfix(List<String> postfix) {
        return 0;  // TODO
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
                if (tokens.get(i) == ")") // pops elements inside parenthesis to prefix;
                {
                    while (stack.peek() != "(")
                    {
                        postfix.add(stack.pop());
                    }
                    stack.pop();
                } else {
                    while (getPrecedence(stack.peek()) >= getPrecedence(tokens.get(i))) //check if procedure in stack has higher value than current procedure
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
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }

    Assoc getAssociativity(String op) {
        if ("+-*/".contains(op)) {
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

    // ---------- Tokenize -----------------------

    public List<String> tokenize(String expr)
    {
        //break string up into tokens: number, operand or parenthesis
        return null;   // TODO
    }

    // TODO Possibly more methods
}
