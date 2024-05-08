import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import javax.swing.*;
import java.io.File;

public class FileUnzipper {
    public static void unzipFile(File inputFile, File outputFile, String password, JLabel progressLabel) {
        try {
            ZipFile zipFile = new ZipFile(inputFile, password.toCharArray());
            zipFile.extractAll(outputFile.getAbsolutePath());
            SwingUtilities.invokeLater(() -> progressLabel.setText("Unzip successful!"));
        } catch (ZipException e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "An error occurred while unzipping the file."));
        }
    }
}


