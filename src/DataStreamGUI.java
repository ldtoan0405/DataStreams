import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DataStreamGUI extends JFrame {
    private JTextArea originalTextArea;
    private JTextArea filteredTextArea;
    private JTextField searchField;

    public DataStreamGUI() {
        // Set up the JFrame
        setTitle("Data Stream Processing");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        originalTextArea = new JTextArea();
        filteredTextArea = new JTextArea();
        searchField = new JTextField(20);
        JButton loadButton = new JButton("Load File");
        JButton searchButton = new JButton("Search");
        JButton quitButton = new JButton("Quit");

        // Set layout
        setLayout(new BorderLayout());

        // Create panels for buttons and search field
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(quitButton);
        add(buttonPanel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search String: "));
        searchPanel.add(searchField);
        add(searchPanel, BorderLayout.SOUTH);

        // Create scroll panes for text areas
        JScrollPane originalScrollPane = new JScrollPane(originalTextArea);
        JScrollPane filteredScrollPane = new JScrollPane(filteredTextArea);

        add(originalScrollPane, BorderLayout.WEST);
        add(filteredScrollPane, BorderLayout.EAST);

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(new File("."));
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    try {
                        Path filePath = fileChooser.getSelectedFile().toPath();
                        Stream<String> lines = Files.lines(filePath);
                        lines.forEach(line -> originalTextArea.append(line + "\n"));
                    } catch (IOException exception) {
                        JOptionPane.showMessageDialog(null, "Error loading file: " + exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchString = searchField.getText();
                if (!searchString.isEmpty()) {
                    String originalText = originalTextArea.getText();
                    Stream<String> filteredLines = Stream.of(originalText.split("\n"))
                            .filter(line -> line.contains(searchString));
                    filteredLines.forEach(line -> filteredTextArea.append(line + "\n"));
                }
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DataStreamGUI().setVisible(true);
            }
        });
    }
}
