import java.awt.event.*;
import javax.swing.*;
public class TextReader extends JFrame implements ActionListener {
  JTextField textbox;
  JButton button;
  JLabel errorMessage;
  String returnedText;

  // Constructor
  TextReader() {
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setSize(400, 400);
    this.setLayout(null);
    this.setVisible(true);
    
    textbox = new JTextField();
    textbox.setBounds(75, 100, 150, 20);
    
    button = new JButton("Submit");
    button.setBounds(225, 100, 100, 20);
    button.addActionListener(this);

    errorMessage = new JLabel("", SwingConstants.CENTER);
    errorMessage.setBounds(0, 300, 400, 20);
    errorMessage.setOpaque(true);

    this.add(textbox);
    this.add(button);
    this.add(errorMessage);
  }

  // Detects when the "submit" button has been pressed
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == button) {
      String text = textbox.getText();
      
      // displays error if field is empty
      if (text.equals("")) {
        errorMessage.setText("ERROR: NAME MUST NOT BE EMPTY");
      }

      // displays error if name is more than 20 characters
      else if (text.length() > 20) {
        errorMessage.setText("ERROR: NAME MUST NOT EXCEED 20 CHARACTERS");
      }

      // returns the text within the textbox if it is valid
      else {
        returnedText = text;
      }
    }
  }
}