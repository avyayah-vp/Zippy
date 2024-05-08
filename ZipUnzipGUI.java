import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ZipUnzipGUI extends JFrame {

    // Panels for zip and unzip operations
    private JPanel zipPanel;
    private JPanel unzipPanel;

    private File selectedFile;

    // constructor to initialize the GUI components
    public ZipUnzipGUI() {
        setTitle("Zippy");

        // Initialize panels
        zipPanel = createPanel("Zip Files");
        unzipPanel = createPanel("Unzip Files");

        // Add panels to tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Zip", zipPanel);
        tabbedPane.addTab("UnZip", unzipPanel);
        add(tabbedPane);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null); // Center the frame
        setResizable(false);
        setVisible(true);
    }

    // function to create the specefic panels and return the panel
    private JPanel createPanel(String operation) {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("ZIPPY");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(200, 20, 200, 50);
        panel.add(titleLabel);

        JLabel filesLabel = new JLabel(operation + ":");
        filesLabel.setBounds(200, 100, 100, 30);
        panel.add(filesLabel);

        JButton selectFileButton = new JButton("Select File");
        selectFileButton.setBounds(300, 100, 150, 30);
        panel.add(selectFileButton);

        JLabel selectedFileTitleLabel = new JLabel("Selected File:");
        selectedFileTitleLabel.setBounds(200, 150, 150, 30);
        panel.add(selectedFileTitleLabel);

        JLabel progressTitleLabel = new JLabel("Progress:");
        progressTitleLabel.setBounds(200, 200, 100, 30);
        panel.add(progressTitleLabel);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(200, 250, 100, 30);
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBounds(300, 250, 100, 30);
        panel.add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(175, 300, 150, 30);
        panel.add(confirmPasswordLabel);

        JPasswordField confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setBounds(300, 300, 100, 30);
        panel.add(confirmPasswordField);

        JButton actionButton = new JButton(operation.equals("Zip Files") ? "Zip File" : "Unzip File");
        actionButton.setBounds(300, 350, 100, 30);
        panel.add(actionButton);

        JLabel selectedFileLabel = new JLabel("~~~No file selected~~~");
        selectedFileLabel.setBounds(350, 150, 200, 30);
        selectedFileLabel.setForeground(Color.BLUE);
        panel.add(selectedFileLabel);

        JLabel progressLabel = new JLabel("0%");
        progressLabel.setBounds(300, 200, 100, 30);
        panel.add(progressLabel);

        // Add action listeners
        selectFileButton.addActionListener(e -> selectFile(selectedFileLabel, progressLabel));
        actionButton.addActionListener(
                e -> performAction(operation, passwordField, confirmPasswordField, selectedFile, progressLabel));

        return panel;
    }

    // logic of selecting files
    private void selectFile(JLabel selectedFileLabel, JLabel progressLabel) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            selectedFileLabel.setText(selectedFile.getName());
            progressLabel.setText("0%");
        }
    }

    // zip or unzip action
    private void performAction(String operation, JPasswordField passwordField, JPasswordField confirmPasswordField,
            File selectedFile, JLabel progressLabel) {
        if (passwordField.getPassword().length > 0 && confirmPasswordField.getPassword().length > 0) {
            if (new String(passwordField.getPassword()).equals(new String(confirmPasswordField.getPassword()))) {
                if (selectedFile != null) {
                    // open the JFileChooser to select the destination folder
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int returnValue = fileChooser.showSaveDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File destination = fileChooser.getSelectedFile();
                        try {
                            // Call zip method
                            if (operation.equals("Zip Files")) {
                                FileZipper.zipFile(selectedFile, new File(destination, selectedFile.getName() + ".zip"),
                                        new String(passwordField.getPassword()), progressLabel);
                            }
                            // Call unzip method
                            else {
                                FileUnzipper.unzipFile(selectedFile,
                                        new File(destination, selectedFile.getName().replace(".zip", "")),
                                        new String(passwordField.getPassword()), progressLabel);
                                JOptionPane.showMessageDialog(this, "File successfully unzipped!");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(this, "An error occurred during operation.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please select any file or folder.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Passwords do not match. Please try again.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a password.");
        }
    }
}
