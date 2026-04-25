import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class RecursionActivity {

    private final JFrame frame;
    private final CardLayout cards;
    private final JPanel container;

    public RecursionActivity() {
        frame = new JFrame("Finals Lab Activity #4 - Recursion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 560);
        frame.setLocationRelativeTo(null);

        cards = new CardLayout();
        container = new JPanel(cards);

        container.add(buildMainMenu(), "MENU");
        container.add(new SequencePanel(
                "Fibonacci",
                "Fibonacci.png",
                "This program will find all the terms of the Fibonacci numbers.",
                3,
                Sequences::fibonacci
        ), "FIB");
        container.add(new SequencePanel(
                "Lucas",
                "lucas.png",
                "This program will find all the terms of the Lucas numbers.",
                3,
                Sequences::lucas
        ), "LUC");
        container.add(new SequencePanel(
                "Tribonacci",
                "tribonacci.png",
                "This program will find all the terms of the Tribonacci numbers.",
                4,
                Sequences::tribonacci
        ), "TRI");

        frame.add(container);
        frame.setVisible(true);
    }

    private JPanel buildMainMenu() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("RECURSIVE SEQUENCES", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(0x1F4E79));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        JLabel image = new JLabel("", SwingConstants.CENTER);
        ImageIcon icon = loadImage("recursive sequence.png", 560, 280);
        if (icon != null) image.setIcon(icon);
        else image.setText("(recursive sequence.png not found)");
        panel.add(image, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 4, 12, 0));
        buttons.setBorder(BorderFactory.createEmptyBorder(15, 25, 25, 25));
        buttons.setBackground(Color.WHITE);

        buttons.add(menuButton("1) Fibonacci", e -> cards.show(container, "FIB")));
        buttons.add(menuButton("2) Lucas",      e -> cards.show(container, "LUC")));
        buttons.add(menuButton("3) Tribonacci", e -> cards.show(container, "TRI")));
        buttons.add(menuButton("4) Exit",       e -> System.exit(0)));

        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private JButton menuButton(String text, java.awt.event.ActionListener a) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.addActionListener(a);
        return b;
    }

    private ImageIcon loadImage(String name, int w, int h) {
        java.io.File f = new java.io.File(name);
        if (!f.exists()) return null;
        ImageIcon raw = new ImageIcon(f.getAbsolutePath());
        Image scaled = raw.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private class SequencePanel extends JPanel {
        private final JTextField input = new JTextField(8);
        private final JTextArea output = new JTextArea(5, 50);
        private final java.util.function.IntFunction<java.util.List<BigInteger>> generator;
        private final int minTerms;
        private final String introLine;
        private final String sequenceName;

        SequencePanel(String name, String imageFile, String introLine, int minTerms,
                      java.util.function.IntFunction<java.util.List<BigInteger>> generator) {
            this.generator = generator;
            this.minTerms = minTerms;
            this.introLine = introLine;
            this.sequenceName = name;

            setLayout(new BorderLayout());
            setBackground(Color.WHITE);

            JLabel title = new JLabel(name.toUpperCase() + " NUMBERS", SwingConstants.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 22));
            title.setForeground(new Color(0x1F4E79));
            title.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
            add(title, BorderLayout.NORTH);

            JPanel center = new JPanel();
            center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
            center.setBackground(Color.WHITE);

            JLabel image = new JLabel("", SwingConstants.CENTER);
            image.setAlignmentX(Component.CENTER_ALIGNMENT);
            ImageIcon icon = loadImage(imageFile, 520, 240);
            if (icon != null) image.setIcon(icon);
            else image.setText("(" + imageFile + " not found)");
            center.add(image);

            JLabel intro = new JLabel(introLine);
            intro.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            intro.setAlignmentX(Component.CENTER_ALIGNMENT);
            intro.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
            center.add(intro);

            JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
            inputRow.setBackground(Color.WHITE);
            inputRow.add(new JLabel("Input the number of terms:"));
            inputRow.add(input);
            JButton compute = new JButton("Compute");
            compute.addActionListener(this::onCompute);
            inputRow.add(compute);
            center.add(inputRow);

            output.setEditable(false);
            output.setLineWrap(true);
            output.setWrapStyleWord(true);
            output.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            output.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY),
                    BorderFactory.createEmptyBorder(6, 8, 6, 8)));
            JScrollPane scroll = new JScrollPane(output);
            scroll.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25));
            center.add(scroll);

            add(center, BorderLayout.CENTER);

            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
            bottom.setBackground(Color.WHITE);
            JButton again = new JButton("Compute Again");
            again.addActionListener(e -> {
                input.setText("");
                output.setText("");
                input.requestFocus();
            });
            JButton back = new JButton("Back to Main Menu");
            back.addActionListener(e -> {
                input.setText("");
                output.setText("");
                cards.show(container, "MENU");
            });
            bottom.add(again);
            bottom.add(back);
            add(bottom, BorderLayout.SOUTH);
        }

        private void onCompute(ActionEvent e) {
            String raw = input.getText().trim();
            int n;
            try {
                n = Integer.parseInt(raw);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Please enter a valid integer.",
                        "Invalid input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (n <= minTerms - 1) {
                JOptionPane.showMessageDialog(frame,
                        "The number of terms must be greater than " + (minTerms - 1) + ".",
                        "Invalid input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            java.util.List<BigInteger> terms = generator.apply(n);
            StringBuilder sb = new StringBuilder();
            sb.append(introLine).append('\n');
            sb.append("Input the number of terms: ").append(n).append('\n');
            sb.append("The ").append(sequenceName).append(" numbers are: ");
            for (int i = 0; i < terms.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(terms.get(i));
            }
            output.setText(sb.toString());
        }
    }

    static class Sequences {
        private static final Map<Integer, BigInteger> FIB = new HashMap<>();
        private static final Map<Integer, BigInteger> LUC = new HashMap<>();
        private static final Map<Integer, BigInteger> TRI = new HashMap<>();

        static BigInteger fib(int n) {
            if (n == 0) return BigInteger.ZERO;
            if (n == 1) return BigInteger.ONE;
            BigInteger cached = FIB.get(n);
            if (cached != null) return cached;
            BigInteger v = fib(n - 1).add(fib(n - 2));
            FIB.put(n, v);
            return v;
        }

        static BigInteger luc(int n) {
            if (n == 0) return BigInteger.valueOf(2);
            if (n == 1) return BigInteger.ONE;
            BigInteger cached = LUC.get(n);
            if (cached != null) return cached;
            BigInteger v = luc(n - 1).add(luc(n - 2));
            LUC.put(n, v);
            return v;
        }

        static BigInteger tri(int n) {
            if (n == 0) return BigInteger.ZERO;
            if (n == 1) return BigInteger.ZERO;
            if (n == 2) return BigInteger.ONE;
            BigInteger cached = TRI.get(n);
            if (cached != null) return cached;
            BigInteger v = tri(n - 1).add(tri(n - 2)).add(tri(n - 3));
            TRI.put(n, v);
            return v;
        }

        static java.util.List<BigInteger> fibonacci(int count) {
            java.util.List<BigInteger> out = new java.util.ArrayList<>();
            for (int i = 0; i < count; i++) out.add(fib(i));
            return out;
        }

        static java.util.List<BigInteger> lucas(int count) {
            java.util.List<BigInteger> out = new java.util.ArrayList<>();
            for (int i = 0; i < count; i++) out.add(luc(i));
            return out;
        }

        static java.util.List<BigInteger> tribonacci(int count) {
            java.util.List<BigInteger> out = new java.util.ArrayList<>();
            for (int i = 0; i < count; i++) out.add(tri(i));
            return out;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RecursionActivity::new);
    }
}
