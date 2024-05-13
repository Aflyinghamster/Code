// import event listeners 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Calculator  implements ActionListener{
    //create the frame a text field and the buttons, initalize variables
    JFrame frame;
    JTextField textfield;
    // create anarray of numbers and function buttons
    JButton[] numbered_buttons = new JButton[10];
    JButton[] func_button = new JButton[9];
    JButton add_button, sub_button, mult_button, div_button;
    JButton dec_button, eq_button, del_button, clr_button, neg_button; 
    JPanel panel;
     
    Font myFont = new Font("Comic Sans", Font.BOLD,30);
    double num1 = 0, num2 =0, result = 0; 
    char operator;

    //create calculator function
    Calculator(){
        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,550);
        frame.setLayout(null); 

        textfield = new JTextField();
        textfield.setBounds(50, 25, 300, 50);
        textfield.setFont(myFont);
        textfield.setEditable(false); 

        add_button = new JButton("+");
        sub_button = new JButton("-");
        mult_button = new JButton("*");
        div_button = new JButton("/");
        dec_button = new JButton(".");
        eq_button = new JButton("=");
        del_button = new JButton("DEL");
        clr_button = new JButton("CLR"); 
        neg_button = new JButton("(-)");
// store the buttons in an array
        func_button[0] = add_button;
        func_button[1] = sub_button;
        func_button[2] = mult_button;
        func_button[3] = div_button;
        func_button[4] = dec_button;
        func_button[5] = eq_button;
        func_button[6] = del_button;
        func_button[7] = clr_button; 
        func_button[8] = neg_button;
        //for each button in the array set listers 
        for( int i = 0; i < func_button.length; i++){
            func_button[i].addActionListener(this);
            func_button[i].setFont(myFont);
            func_button[i].setFocusable(false);
        }
//same as before but for the numbers
        for(int i = 0; i < 10; i++){
            numbered_buttons[i] = new JButton(String.valueOf(i)); 
            numbered_buttons[i].addActionListener(this);
            numbered_buttons[i].setFont(myFont);
            numbered_buttons[i].setFocusable(false);

        }  
//set size of button area
        neg_button.setBounds(50, 430, 100, 50);
        del_button.setBounds(150,430,145,50);
        clr_button.setBounds(250,430,145,50);



// draw everything onto panel
        panel = new JPanel();
        panel.setBounds(50,100,300,300);
        panel.setLayout(new GridLayout(4,4,10,10));
        // panel.setBackground(Color.BLACK); 

        panel.add(numbered_buttons[1]);
        panel.add(numbered_buttons[2]);
        panel.add(numbered_buttons[3]);
        panel.add(add_button);

        panel.add(numbered_buttons[4]);
        panel.add(numbered_buttons[5]);
        panel.add(numbered_buttons[6]);
        panel.add(sub_button); 

        panel.add(numbered_buttons[7]);
        panel.add(numbered_buttons[8]);
        panel.add(numbered_buttons[9]);
        panel.add(mult_button);
        
        panel.add(dec_button);
        panel.add(numbered_buttons[0]);
        panel.add(eq_button);
        panel.add(div_button);
//ad panel to frame and draw
        frame.add(panel);
        frame.add(neg_button);
        frame.add(del_button);
        frame.add(clr_button);
        frame.add(textfield);
        frame.setVisible(true);

    }
//run calaculator()
    public static void main(String[] args){
        Calculator calc = new Calculator();

    }
    //logic for button presses 
    @Override
    public void actionPerformed(ActionEvent e){ 

        for(int i = 0; i< 10; i++){
            if (e.getSource() == numbered_buttons[i]) {
                textfield.setText(textfield.getText().concat(String.valueOf(i))); 
                //for each of the numbers in the array of number buttons and set the text to the string associated with that index
            }
        }
        if(e.getSource() == dec_button){
            textfield.setText(textfield.getText().concat("."));
        } 
// set an operator value to be used in switch statement set textfeild to blank on func press 
        if(e.getSource() == add_button){
            num1 = Double.parseDouble(textfield.getText());
            operator = '+' ; 
            textfield.setText("");
        } 

        if(e.getSource() == sub_button){
            num1 = Double.parseDouble(textfield.getText());
            operator = '-' ; 
            textfield.setText("");
        } 

        if(e.getSource() == mult_button){
            num1 = Double.parseDouble(textfield.getText());
            operator = '*' ; 
            textfield.setText("");
        } 

        if(e.getSource() == div_button){
            num1 = Double.parseDouble(textfield.getText());
            operator = '/' ; 
            textfield.setText("");
        } 
// switch statements for the operators based on which function key was pressed
        if(e.getSource()== eq_button){
            num2 = Double.parseDouble(textfield.getText());

            switch (operator) {
            case'+':
                result = num1 + num2;
                break; 
            case'-':
                result = num1 - num2;
                break;
            case'*':
                result = num1 * num2;
                break;
            case'/':
                result = num1 / num2;
                break;   
            }
            textfield.setText(String.valueOf(result));
            num1 = result;
        }
        if(e.getSource()== clr_button){
            textfield.setText(String.valueOf(""));

        } 

        if(e.getSource()== del_button){
            String string = textfield.getText();
            textfield.setText(String.valueOf(""));
            for(int i = 0; i <string.length()-1; i++)
            textfield.setText(textfield.getText()+string.charAt(i));
        } 
        if(e.getSource()== neg_button){
           double temp = Double.parseDouble(textfield.getText()); // turn string to double
           temp*=-1;
           textfield.setText(String.valueOf(temp));
        }

    }

}
