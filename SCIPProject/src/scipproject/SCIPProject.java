import java.util.Stack;

public class SCIPProject {

    Node root;

    // Class to represent the nodes of syntax tree
    private static class Node {

        String data;
        Node left, right;

        Node(String d) {
            data = d;
            left = null;
            right = null;
        }
    }

    static Stack operators = new Stack<>();
    static Stack postfixExp = new Stack();
    static double result;

    public static Stack toPostfix(String infix) //converts an infix expression to postfix 
    {
        char symbol;
        String integer = "";

        for (int i = 0; i < infix.length(); ++i) {
            symbol = infix.charAt(i);
            //if it's an operand (either positive or negative), add it to the postfix string 
            if (integer.isEmpty() && infix.charAt(i) == '-' || isOperand(infix.charAt(i))) {
                integer = integer + symbol;

                //if it's an operator, add it to the postfix string 
            } else {
                postfixExp.push(integer);
                integer = ""; //clear String 
                if (symbol == '(') {//push '(' into stack  
                    operators.push(symbol);

                } else if (symbol == ')') { // keeps pop item from stack and add to postfix string until meet '('

                    while ((char) operators.peek() != '(') {
                        postfixExp.push(operators.pop());
                    }
                    operators.pop(); // pop '(' 
                } else { //print operators occurring before it that have greater precedence '()' and follow order of operation  

                    while (!operators.isEmpty() && !((char) operators.peek() == '(') && prec(symbol) <= prec((char) operators.peek())) {
                        postfixExp.push(operators.pop());
                    }
                    operators.push(symbol);
                }
            }

        }
        //if leave any operand 
        if (!integer.isEmpty()) {
            postfixExp.push(integer);
        }
        //if leave any operator
        while (!operators.isEmpty()) {
            postfixExp.push(operators.pop());
        }
        postfixExp.removeIf(n -> n == ""); //remove whitespace in stack 
        return postfixExp;
    }

    static int prec(char x) { //order of operation 
        if (x == '+' || x == '-') {
            return 1;
        }
        if (x == '*' || x == '/' || x == '%') {
            return 2;
        }
        return 0;
    }

    static boolean isOperand(char ch) {
        return (ch >= '0' && ch <= '9' || ch == '.');
    }

    static boolean isOperator(String ch) {
        return ch.equals("+") || ch.equals("-") || ch.equals("*") || ch.equals("/") || ch.equals("%");
    }

    //create expressionTree and evaluate 
    public static Node expressionTree(Stack postfixExp) {
        Stack<Node> st = new Stack<Node>();
        Node t1, t2, temp, res;

        for (int i = 0; i < postfixExp.size(); i++) {
            try {
                if (!isOperator(postfixExp.elementAt(i).toString())) { // when is operands
                    temp = new Node(postfixExp.elementAt(i).toString()); // 5  3 
                    st.push(temp);
                } else { // when is operator 
                    temp = new Node(postfixExp.elementAt(i).toString());
                    //  + 
                    // / \
                    //5   3
                    t1 = st.pop();
                    t2 = st.pop();
                    temp.left = t2;
                    temp.right = t1;
                    result = evalTree(temp);
                    String str = Double.toString(result);

                    res = new Node(str);
                    st.push(res);
                }
            } catch (Exception e) {
                System.out.println("Invalid Expression");
            }

        }
        temp = st.pop();
        return temp;
    }

    // convert string to double data type
    private static double toDigit(String number) {

        double num = Double.parseDouble(number);
        return num;
    }

    public static double evalTree(Node root) {
        // Empty tree
        if (root == null) {
            return 0;
        }
        if (root.left == null && root.right == null) {
            return toDigit(root.data);
        }

        // Evaluate left subtree
        double leftEval = evalTree(root.left);

        // Evaluate right subtree
        double rightEval = evalTree(root.right);

        // Check which operator to apply
        if (root.data.equals("+")) {
            return leftEval + rightEval;
        }

        if (root.data.equals("-")) {
            return leftEval - rightEval;
        }

        if (root.data.equals("*")) {
            return leftEval * rightEval;
        }
        if (root.data.equals("/")) {
            return leftEval / rightEval;
        }
        return leftEval % rightEval;
    }

    public static void main(String[] args) {
        String infix = "-2-(-15.5*6-3.5)/8";
        postfixExp = toPostfix(infix);
        System.out.println(postfixExp);
        Node r = expressionTree(postfixExp);
        System.out.println(r.data);
    }
}
