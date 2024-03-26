import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FolderSnapshot {
    private static Path folderPath;
    private static Map<String, Long> fileSnapshots;

    public FolderSnapshot(String folderPath) {
        this.folderPath = Path.of(folderPath);
        this.fileSnapshots = new HashMap<>();
    }

    public static void createSnapshot() {
        Map<String, Long> newSnapshots = new HashMap<>();
        File[] files = folderPath.toFile().listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    newSnapshots.put(file.getName(), file.lastModified());
                }
            }
            String snapshotTime = getCurrentTimestamp();
            System.out.println("Created Snapshot at: " + snapshotTime);
            fileSnapshots = newSnapshots;
        }
    }

    private static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return sdf.format(new Date());
    }

    public static void info(String filename) {
        System.out.println("Info for file: " + filename);
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        printFileInfo(filename);
        switch (extension) {
            case "png":
            case "jpg":
                printImageInfo(filename);
                break;
            case "txt":
                printTextFileInfo(filename);
                break;
            case "java":
            case "py":
                printProgramFileInfo(filename);
                break;
            default:
                System.out.println("Unsupported file type.");
        }
    }

    public static void printFileInfo(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        String prettyPrintExtension = getPrettyPrintExtension(extension);

        Path filePath = Paths.get(folderPath.toString(), filename);
        File file = filePath.toFile();

        if (file.exists()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("File Name: " + filename);
            System.out.println("File Type: " + prettyPrintExtension);
            System.out.println("Created Date: " + sdf.format(new Date(file.lastModified())));
            System.out.println("Last Modified Date: " + sdf.format(new Date(file.lastModified())));
        } else {
            System.out.println("File does not exist.");
        }
    }

    private static String getPrettyPrintExtension(String extension) {
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "JPEG Image";
            case "png":
                return "PNG Image";
            case "txt":
                return "Text File";
            case "java":
                return "Java Source File";
            case "py":
                return "Python Source File";
            default:
                return extension.toUpperCase() + " File";
        }
    }

    public static void printImageInfo(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png")) {
            try {
                Process process = new ProcessBuilder("identify", "-format", "%wx%h", folderPath + File.separator + filename).start();
                InputStream is = process.getInputStream();
                Scanner scanner = new Scanner(is);
                String dimensions = scanner.hasNext() ? scanner.next() : "Unknown";
                System.out.println("Image Size: " + dimensions);
            } catch (IOException e) {
                System.out.println("Failed to get image dimensions.");
            }
        } else {
            System.out.println("File is not an image.");
        }
    }

    private static void printTextFileInfo(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(folderPath + File.separator + filename))) {
            int lineCount = 0;
            int wordCount = 0;
            int charCount = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                charCount += line.length();
                String[] words = line.split("\\s+");
                wordCount += words.length;
            }
            System.out.println("Line Count: " + lineCount);
            System.out.println("Word Count: " + wordCount);
            System.out.println("Character Count: " + charCount);
        } catch (IOException e) {
            System.out.println("Failed to read file.");
        }
    }

    private static void printProgramFileInfo(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(folderPath + File.separator + filename))) {
            int lineCount = 0;
            int classCount = 0;
            int methodCount = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                if (line.trim().contains("class ")) {
                    classCount++;
                } else if (line.trim().contains("public") || line.trim().contains("private") || line.trim().contains("protected")) {
                    methodCount++;
                }
            }
            System.out.println("Line Count: " + lineCount);
            System.out.println("Class Count: " + classCount);
            System.out.println("Method Count: " + methodCount);
        } catch (IOException e) {
            System.out.println("Failed to read file.");
        }
    }

    public static void compareSnapshot() {
        Map<String, Long> newSnapshots = new HashMap<>();
        File[] files = folderPath.toFile().listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    newSnapshots.put(file.getName(), file.lastModified());
                }
            }
            for (Map.Entry<String, Long> entry : fileSnapshots.entrySet()) {
                String filename = entry.getKey();
                long lastModified = entry.getValue();
                long currentModified = newSnapshots.getOrDefault(filename, 0L);
                if (lastModified == currentModified) {
                    System.out.println(filename + " - No Change");
                } else if (currentModified == 0L) {
                    System.out.println(filename + " - Deleted");
                } else if (currentModified > lastModified) {
                    // Add your logic for handling changes
                } else {
                    System.out.println(filename + " - Changed");
                }
                newSnapshots.remove(filename);
            }
            fileSnapshots = newSnapshots;
        }
    }

    public static void status() {
        for (Map.Entry<String, Long> entry : fileSnapshots.entrySet()) {
            String filename = entry.getKey();
            long lastModified = entry.getValue();
            File file = new File(folderPath + File.separator + filename);
            long currentModified = file.lastModified();

            if (lastModified == currentModified) {
                System.out.println(filename + " - No Change");
            } else if (currentModified == 0L) {
                System.out.println(filename + " - Deleted");
            } else {
                System.out.println(filename + " - Changed");
            }
        }
    }
}
