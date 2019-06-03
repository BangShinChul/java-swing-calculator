import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;

/*

=============================================================================
[문제] Swing으로 계산기 구현
첨부파일로 올려둔 Java 프로그램(JCalculator.java)을 실행해보고,
아래와 같은 기능을 추가하여 계산기를 구현해보세요.

(1) 사칙연산(+, -, *, /)을 수행할 수 있도록 버튼을 추가하고, 계산 결과가 텍스트 필드에 나타나도록 합니다.
(2) 양의 정수에 대한 계산(사칙연산)은 반드시 수행되어야 합니다.
(3) 음의 정수 또는 소수점이 포함된 계산이 수행되면 가산점을 받을 수 있습니다.
(4) 그 밖에 여러 기능
(예: 루트 또는 제곱근 계산, 백스페이스 지우기, 키보드 입력 기능, 계산 도중 메모리 기능 등등)이 추가될수록
가산점을 받을 수 있습니다.
(5) 반드시 Swing 컴포넌트를 이용하도록 하며, 배치나 색상 또는 크기는 자유롭게 선택해도 좋습니다.
=============================================================================


제출하는 소스 코드(즉, JCalculator.java) 내부에는 반드시 프로그램에 대한 설명이 주석(comment)으로 포함되어야 하며,
컴파일과 실행을 거친 후 제출 바랍니다.
또한 실행결과도 캡처해서 파일(형식은 제한없음)로 제출 바랍니다(소스 코드와 zip으로 묶어서 제출).

java JCaculator.java -encoding UTF-8

 implements KeyListener
*/
public class JCalculator extends JFrame{
    private boolean isStart = true;
    private boolean formulaIsStart = true;
    private String lastOperand = ""; // 마지막예 계산했던 연산자
    private String lastCalculateNumber = ""; // 마지막에 계산했던 숫자
    JTextField display; // 입력한 값 보여주기용
    JTextField formula; // 계산식 보여주기용
    JPanel keyPanel; // 버튼 키 패널

    public JCalculator() {
        super("Java Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        display = new JTextField("0", 12);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false); // 직접 입력창을 수정하지 못하도록 설정

        // 계산식 보여주기용 JTextField 생성
        formula = new JTextField("0", 12);
        formula.setHorizontalAlignment(JTextField.RIGHT);
        formula.setEditable(false); // 입력된 계산식은 수정하지 못하도록 설정

        keyPanel = new JPanel();
        JButton button;
        /*
        ce : 현재 값만 0으로 리셋, 하지만 = 을 누르면 이전의 계산값을 반복함
        c : 현재값과 이전의 계산값 모두 리셋. = 을 눌러도 이전의 계산값을 반복하지않음
        ± : 현재값에 NOT 처리를함. 예) 10일때 ±를 클릭하면 -10이 됨. 반대로 -10일때 ±를 클릭하면 10 이됨.
        * */
        String[] buttons = {
                "％","√","x^2","1/x",
                "ce","c","←","÷",
                "7","8","9","×",
                "4","5","6","－",
                "1","2","3","＋",
                "±","0",".","＝"
        };
        keyPanel.setLayout(new GridLayout(7, 4));
        for (int i = 0; i < buttons.length; i++) {
            String key = buttons[i];

            if (
                    key.equals("％") || key.equals("√") || key.equals("x^2") || key.equals("1/x") ||
                    key.equals("ce") || key.equals("c") || key.equals("←") || key.equals("÷") ||
                    key.equals("×") || key.equals("－") || key.equals("＋") || key.equals("＝") ||
                    key.equals("±")
            ) {
                button = new JButton(key);
                button.addActionListener(new OpListener());
                keyPanel.add(button);
            } else {
                button = new JButton(key);
                button.addActionListener(new NumListener());
                keyPanel.add(button);
            }
        }
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add("North", formula);
        pane.add("Center", display);
        pane.add("South", keyPanel);
    }

//    @Override
//    public void keyTyped(KeyEvent e) {
//        System.out.println(e);
//    }
//
//    @Override
//    public void keyPressed(KeyEvent e) {
//        System.out.println(e);
//    }
//
//    @Override
//    public void keyReleased(KeyEvent e) {
//        System.out.println(e);
//    }

    // 숫자 액션리스너
    class NumListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String digit = e.getActionCommand();
            if (isStart) {
                display.setText(digit);
                isStart = false;
            } else {
                display.setText(display.getText() + digit);
            }
        }
    }

    // 연산자 액션리스너
    class OpListener implements ActionListener {

        // 계산식 필드를 보여주기 위한 메서드
        private void setFormulaField(String op) {

            if(!op.isEmpty()) {
                lastOperand = op;
                lastCalculateNumber = display.getText();
            }

            if (formulaIsStart) { // 첫 입력이라면
                formula.setText(display.getText() + op); // display 필드에 있는 값 + operand를 setText한다.
                formulaIsStart = false; // 첫 입력이 아니라고 표시하기 위해 formulaIsStart값을 false로 변경한다.
            } else { // 첫 입력이 아니라면
                formula.setText(formula.getText() + display.getText() + op); // formula필드에 있는 값 + display필드에 있는 값 + operand를 setText한다.
            }
            isStart = true; // display 필드의 값을 새로 입력하기위해 isStart 값을 true로 변경한다.
        }

        // 최종적으로 formula의 수식을 계산하여 결과를 도출해내는 메서드
        private String doCalculate() {
            BigDecimal rst = null; // 결과로 리턴할 변수 초기화. BigDecimal 타입으로 선언한 이유는 소수점 처리도 가능하도록 하기 위함.
            String formularData = formula.getText(); // formula 필드에 있는 수식을 가져온다.
            ScriptEngineManager mgr = new ScriptEngineManager(); // JDK 1.6 이상에서 사용할 수 있는 자바스크립트 엔진 객체를 불러오기 위해 ScriptEngineManager 객체 생성
            ScriptEngine engine = mgr.getEngineByName("js"); // js 엔진을 가져온다
            try {
                rst = new BigDecimal(engine.eval(formularData).toString()); // js 엔진을 사용하여 계산하고 결과값을 rst에 저장
            }catch (Exception e) {
                e.printStackTrace();
            }
            formulaIsStart = true;
            return rst.toString(); // 수식을 계산한 결과를 리턴한다.

        }

        public void actionPerformed(ActionEvent e) {
            String op = e.getActionCommand();
            if (op.equals("c") || op.equals("ce")) {
                // 계산식 초기화 로직
                display.setText("0");
                formula.setText("0");
                isStart = true;
                formulaIsStart = true;
            } else if (op.equals("＋") || op.equals("－") || op.equals("×") || op.equals("÷")) {
                // 사칙연산 로직
                String operand = "";
                if (op.equals("＋")) {
                    operand = "+";
                } else if (op.equals("－")) {
                    operand = "-";
                } else if (op.equals("×")) {
                    operand = "*";
                } else if (op.equals("÷")) {
                    operand = "/";
                }

                setFormulaField(operand);
            } else if (op.equals("←")) {
                // 백스페이스 지우기 로직
                String s = display.getText(); // display의 데이터를 가져와서 s에 넣고
                display.setText(""); // display의 데이터는 지운다음에
                for (int i=0;i<s.length()-1;i++) {
                    // s의 데이터를 한글자씩 가져와 기존 display의 데이터의 -1 인덱스만큼의 데이터만 다시 세팅한다.
                    display.setText(display.getText() + s.charAt(i));
                }
            } else if (op.equals("±")) {

            } else if (op.equals("％")) {

            } else if (op.equals("√")) {

            } else if (op.equals("x^2")) {

            } else if (op.equals("1/x")) {

            } else if (op.equals("＝")) {
                // 계산식 실행 로직
                setFormulaField("");
                display.setText(doCalculate()); // 계산결과를 display에 세팅
                isStart = true;
            } else {
                // 잘못된 연산자를 입력했을 시 (예외처리)
                formula.setText("");
                display.setText("정확한 연산자를 넣어주세요.");
                isStart = true;
            }
        }
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JCalculator calculator = new JCalculator();
        calculator.pack();
        calculator.setVisible(true);
    }
}
