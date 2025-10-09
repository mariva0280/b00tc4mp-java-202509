import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

class HelloWindow {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Hello, World!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Hello, World!", JLabel.CENTER);

        frame.getContentPane().add(label);
        frame.setSize(300, 100);
        frame.setVisible(true);
    }
}
