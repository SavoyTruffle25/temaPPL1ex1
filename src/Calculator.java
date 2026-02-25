import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.*;


public class Calculator extends JFrame {
    JButton digits[] = {
            new JButton(" 0 "),
            new JButton(" 1 "),
            new JButton(" 2 "),
            new JButton(" 3 "),
            new JButton(" 4 "),
            new JButton(" 5 "),
            new JButton(" 6 "),
            new JButton(" 7 "),
            new JButton(" 8 "),
            new JButton(" 9 ")
            };

    JButton operators[] = {
            new JButton(" + "),
            new JButton(" - "),
            new JButton(" * "),
            new JButton(" / "),
            new JButton(" = "),
            new JButton(" C "),
            new JButton(" ( "),
            new JButton(" ) ")
    };

    String oper_values[] = {"+", "-", "*", "/", "=", "","(",")"};

    String value;
    char operator;

    JTextArea area = new JTextArea(3, 5);

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setSize(230, 250);
        calculator.setTitle(" Java-Calc, PP Lab1 ");
        calculator.setResizable(false);
        calculator.setVisible(true);
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Calculator() {
        add(new JScrollPane(area), BorderLayout.NORTH);
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new FlowLayout());

        for (int i = 0; i < 10; i++)
            buttonpanel.add(digits[i]);

        for (int i = 0; i < 8; i++)
            buttonpanel.add(operators[i]);

        add(buttonpanel, BorderLayout.CENTER);
        area.setForeground(Color.BLACK);
        area.setBackground(Color.WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            digits[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    area.append(Integer.toString(finalI));
                }
            });
        }

        for (int i = 0; i < 8; i++) {
            int finalI = i;
            operators[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (finalI == 5)
                        area.setText("");
                    else if (finalI == 4) {
                        try {
                            String expresie = area.getText();
                            double result = evaluareExpresie(expresie);
                            area.setText(Double.toString(result));
                        } catch (Exception e) {
                            area.setText(" !!!Probleme!!! ");
                        }
                    } else {
                        area.append(oper_values[finalI]);
                       // operator = oper_values[finalI].charAt(0);
                    }
                }
            });
        }
    }
       public double evaluareExpresie(String expresie)
    {
        List<String> lista=toList(expresie);
        List<String> rpn= convertireRPN(lista);
        return evaluateRPN(rpn);
    }

    private List<String> toList(String expresie) // converteste expresia intr-o lista
    {
        List<String> Lista = new ArrayList<>();
        StringBuffer nr= new StringBuffer();

        for(int i=0; i<expresie.length(); i++)
        {
            char c= expresie.charAt(i);
            if(Character.isDigit(c) || c=='.')
            {
                nr.append(c);
            }
            else{
                if (nr.length() > 0) {
                    Lista.add(nr.toString());
                    nr.setLength(0);
                }
                if (!Character.isWhitespace(c)) {
                    Lista.add(String.valueOf(c));
                }
            }
        }

        if(nr.length()>0)  // posibil numar ramas
        {
            Lista.add(nr.toString());
        }
        return Lista;
    }

    private List<String> convertireRPN(List<String> Lista)
    {
        List<String> RPN= new ArrayList<>();
        Stack<String> stack=new Stack<>();
        for(String elem : Lista)
        {
            if (isNumber(elem)) {
                RPN.add(elem);
            } else if (elem.equals("(")) {
                stack.push(elem);
            } else if (elem.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    RPN.add(stack.pop());
                }
                if (!stack.isEmpty()) stack.pop();
            } else if (isOperator(elem)) {
                while (!stack.isEmpty() && isOperator(stack.peek()) &&
                        getPrecedence(elem) <= getPrecedence(stack.peek())) {
                    RPN.add(stack.pop());
                }
                stack.push(elem);
            }
        }
        while (!stack.isEmpty()) RPN.add(stack.pop());
        return RPN;
    }

    private double evaluateRPN(List<String> RPN) {
        Stack<Double> stack = new Stack<>();

        for (String elem : RPN) {
            if (isNumber(elem)) {
                stack.push(Double.parseDouble(elem));
            } else {
                // Este operator, scoatem ultimele doua numere
                double val2 = stack.pop();
                double val1 = stack.pop();

                switch (elem) {
                    case "+": stack.push(val1 + val2); break;
                    case "-": stack.push(val1 - val2); break;
                    case "*": stack.push(val1 * val2); break;
                    case "/": stack.push(val1 / val2); break;
                }
            }
        }
        return stack.pop();
    }

    public boolean isNumber(String str)
    {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isOperator(String str)
    {
        String operatori="+-*/";
        return operatori.contains(str) && str.length() == 1;
    }



    public int getPrecedence(String op) {
        if (op.equals("*") || op.equals("/")) return 2;
        if (op.equals("+") || op.equals("-")) return 1;
        return 0;
    }
}
