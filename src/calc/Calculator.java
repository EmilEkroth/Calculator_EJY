package calc;

import java.lang.reflect.Array;
import java.util.*;

import static java.lang.Double.NaN;
import static java.lang.Double.doubleToLongBits;
import static java.lang.Math.*;

public class Calculator {

    // Here are the only allowed instance variables!
    // Error messages (more on static later)
    final static String MISSING_OPERAND = "Missing or bad operand";
    final static String DIV_BY_ZERO = "Division with 0";
    final static String MISSING_OPERATOR = "Missing operator or parenthesis";
    final static String OP_NOT_FOUND = "Operator not found";

    // Definition of operators
    final static String OPERATORS = "+-*/^";

    // Method used in calc.REPL
    double eval(String expr) {
        if (expr.length() == 0) {
            return NaN;
        }
        List<String> tokens = tokenize(expr);
        List<String> postfix = infix2Postfix(tokens);
        return evalPostfix(postfix);
    }

    public double Calc (String input) {
        String equ = "("+input+")";
        List<String> tokens = tokenize(equ);
        List<String> postfix = infix2Postfix(tokens);

        double out = evalPostfix(postfix);
        return out;
    }

    // ------  Evaluate RPN expression -------------------

    public double evalPostfix(List<String> postfix) {
        Stack<Double> stack = new Stack<Double>();
        for(int i = 0; i < postfix.size(); i++){
            if(isNumber(postfix.get(i))) {
                double nmb = Double.parseDouble(postfix.get(i));
                stack.add(nmb);
            } else if (postfix.get(i).equals("ln")) {
                double a = stack.pop();
                stack.push(log(a));
            } else{
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

    // sorts list from infix to postfix
    public List<String> infix2Postfix(List<String> tokens) {
        Stack<String> stack = new Stack<String>();
        ArrayList<String> postfix = new ArrayList<String>();

        for (int i = 0; i < tokens.size(); i++) {
            if(isNumber(tokens.get(i))) {
                postfix.add(tokens.get(i));
            } else{
                //System.out.println("debug: " + tokens.get(i));

                if (tokens.get(i).equals("(")) {
                    stack.push(tokens.get(i));
                } else if (tokens.get(i).equals(")")) {
                    // pops elements inside parenthesis to prefix;
                    while (!stack.peek().equals("(")) {
                        if(stack.size() == 0) throw new RuntimeException(MISSING_OPERATOR);
                        postfix.add(stack.pop());
                    }
                    stack.pop();
                } else{
                    while (stack.size() != 0 && !stack.peek().equals("(") &&  getPrecedence(stack.peek()) >= getPrecedence(tokens.get(i))) {
                        //check if procedure in stack has higher value than current procedure
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
        } else if ("ln".contains(op)){
            return 5;
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

    public List<String> tokenize(String expr) {
        List<String> strList = new ArrayList<>();

        String strAsNumber = "";    // used to save numbers with multiple digits
        int strSize = expr.length();
        for (int i = 0; i < strSize; i++){
            String token = Character.toString(expr.charAt(i));

            if (!isEmpty(token)) {
                if (isNumber(token) || token.equals(".")) {
                    // adds the digit to the number
                    strAsNumber += token;
                }
                else{
                    if (!isEmpty(strAsNumber)) {
                        // adds the number to the list and resets
                        strList.add(strAsNumber);
                        strAsNumber = "";
                    }

                    if ((strList.size() == 0 || !isNumber(strList.get(strList.size()-1))) && token.equals("-")) {
                        // handles the negative numbers
                        strList.add("-1");
                        strList.add("*");
                    } else if (token.equals("(") && strList.size() != 0 && (isNumber(strList.get(strList.size()-1)) || strList.get(strList.size()-1).equals(")"))) {
                        // Parenthesis as multiplication
                        System.out.println("debug");
                        strList.add("*");
                        strList.add(token);
                    } else if (token.equals("l")) {
                        // adds the ln function
                        strList.add("ln");
                        i++;
                    } else{
                        // adds the operator or parenthesis
                        strList.add(token);
                    }
                }
            } else if (!isEmpty(strAsNumber)) {
                // preventing space between two digits messing up
                // ex. 4 5 + 3 = 48 ==> ["45", "+", "3"] instead of ["4", "5", "+", "3"]
                strList.add(strAsNumber);
                strAsNumber = "";
            }

            // finishing up by adding the number at the end of expression to the list
            if (i == strSize-1 && !isEmpty(strAsNumber)) {
                strList.add(strAsNumber);
            }
        }

        return strList;
    }

    boolean isEmpty(String str) {
        return str.trim().length() == 0;
    }

    /*boolean isInteger(String str) { //TODO check if decimals exist
        try{
            int num = Integer.parseInt(str);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }
*/
/*
    public List<String> tokenize(String expr) {
        //break string up into tokens: number, operand or parenthesis
        List<String> list = new ArrayList<String>();
        String[] in = expr.split("");
        for (int i = 0; i < in.length; i++) {
            if(!in[i].equals(" ")) {
                if(isInteger(in[i])) {
                    String c = in[i];
                    int n = i + 1;
                    while (true) {
                        if(isInteger(in[n])) {
                            c += in[n];
                            n++;
                        } else {
                            break;
                        }
                    }
                    i = n-1;
                    list.add(c);
                } else if (in[i].equals("l")) {
                    list.add("ln");
                    i++;
                } else {
                    list.add(in[i]);
                }
            }
        }
        return list;
    }
*/
    boolean isNumber (String string)
    {
        boolean r = false;
        try {
            Double.parseDouble(string);
            r = true;
        } catch (NumberFormatException nfe){}

        return r;
    }
}
