import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileZipper {

    /**
     * Zips the specified input file or directory into the output zip file with optional encryption.
     *
     * inputFile     The file or directory to zip
     * outputFile    The output zip file
     * password      The password for encryption (can be null or empty for no encryption)
     * progressLabel The JLabel used to display zipping progress
     */
    public static void zipFile(File inputFile, File outputFile, String password, JLabel progressLabel) {
        // Create and start a new thread to perform the zipping process
        Thread zipThread = new Thread(() -> {
            try {
                // Create a new ZipFile object with the output zip file and password (if provided)
                ZipFile zipFile = new ZipFile(outputFile, password.toCharArray());

                // Configure zip parameters
                ZipParameters parameters = new ZipParameters();
                parameters.setCompressionMethod(CompressionMethod.DEFLATE);
                parameters.setEncryptFiles(true); // Enable encryption
                parameters.setEncryptionMethod(EncryptionMethod.AES); // AES encryption
                parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);

                // Collect regular files within the input directory
                Path inputPath = inputFile.toPath();
                List<Path> regularFiles = Files.walk(inputPath)
                        .filter(Files::isRegularFile)
                        .collect(Collectors.toList());

                // Calculate total size of files to be zipped
                long totalBytes = regularFiles.stream()
                        .mapToLong(p -> p.toFile().length())
                        .sum();

                // Walk through each regular file in the input directory to add to the zip file
                long bytesCopied = 0;
                for (Path file : regularFiles) {
                    zipFile.addFile(file.toFile(), parameters);

                    // Update progress and publish intermediate progress updates
                    bytesCopied += file.toFile().length();
                    int progress = (int) ((double) bytesCopied / totalBytes * 100);// calcuating the ratio then * 100 to get percentage
                    SwingUtilities.invokeLater(() -> progressLabel.setText(progress + "%"));
                }

                // Update progress to 100% after zipping is complete
                SwingUtilities.invokeLater(() -> progressLabel.setText("100%"));

                // Show success message once zipping is complete
                JOptionPane.showMessageDialog(null, "File successfully zipped!");
            } catch (ZipException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred while zipping the file.");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred while handling the file.");
            }
        });

        // Start the zipThread to execute the zipping process
        zipThread.start();
    }
}
