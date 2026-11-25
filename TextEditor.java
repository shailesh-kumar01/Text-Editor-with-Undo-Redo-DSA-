import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Stack;
import javax.swing.*;

public class TextEditor extends JFrame {

    JTextArea textArea;
    Stack<String> undoStack = new Stack<>();
    Stack<String> redoStack = new Stack<>();

    public TextEditor() {

        setTitle("Text Editor with Undo and Redo");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 18));

        JScrollPane scrollPane =
                new JScrollPane(textArea);

        JButton undo = new JButton("Undo");
        JButton redo = new JButton("Redo");
        JButton clear = new JButton("Clear");
        JButton save = new JButton("Save");
        JButton open = new JButton("Open");

        JPanel panel = new JPanel();

        panel.add(undo);
        panel.add(redo);
        panel.add(clear);
        panel.add(save);
        panel.add(open);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        undoStack.push("");

        textArea.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                undoStack.push(textArea.getText());
                redoStack.clear();
            }
        });

        undo.addActionListener(e -> undo());

        redo.addActionListener(e -> redo());

        clear.addActionListener(e -> {
            undoStack.push(textArea.getText());
            textArea.setText("");
            redoStack.clear();
        });

        save.addActionListener(e -> saveFile());

        open.addActionListener(e -> openFile());

        setVisible(true);
    }

    void undo() {
        if (undoStack.size() > 1) {
            redoStack.push(undoStack.pop());
            textArea.setText(undoStack.peek());
        }
    }

    void redo() {
        if (!redoStack.isEmpty()) {
            String text = redoStack.pop();
            undoStack.push(text);
            textArea.setText(text);
        }
    }

    void saveFile() {
        JFileChooser chooser = new JFileChooser();

        if (chooser.showSaveDialog(this)
                == JFileChooser.APPROVE_OPTION) {
            try {
                FileWriter writer =
                        new FileWriter(chooser.getSelectedFile());

                writer.write(textArea.getText());
                writer.close();

                JOptionPane.showMessageDialog(
                        this, "File Saved Successfully!");

            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                        this, "Error Saving File!");
            }
        }
    }

    void openFile() {
        JFileChooser chooser = new JFileChooser();

        if (chooser.showOpenDialog(this)
                == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedReader reader = new BufferedReader(
                        new FileReader(chooser.getSelectedFile()));

                textArea.read(reader, null);
                reader.close();

                undoStack.clear();
                redoStack.clear();
                undoStack.push(textArea.getText());

            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                        this, "Error Opening File!");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TextEditor());
    }
}
