import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class HelloLogin {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Hello, Login!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
        frame.setSize(300, 200);

        JPanel panel = new JPanel();
        frame.add(panel);

        JLabel emaiLabel = new JLabel("Email:");
        panel.add(emaiLabel);   
        JTextField emailField = new JTextField(20);
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel);   
        JTextField passwordField = new JTextField(20);
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        panel.add(loginButton);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
