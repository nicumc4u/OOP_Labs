import java.util.Scanner;

public class TaskSelector {
    public static void consoleMenu(){
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("|---------------------------------------------------------------|");
            System.out.println("|                           Commands                            |");
            System.out.println("|---------------------------------------------------------------|");
            System.out.println("| commit - Simply update the snapshot time                      |");
            System.out.println("| info <filename> - prints general information about the file   |");
            System.out.println("| status - shows program changes                                |");
            System.out.println("| exit                                                          |");
            System.out.println("|---------------------------------------------------------------|");
            String input = scanner.nextLine();

            switch (input) {
                case "commit":
                    FolderSnapshot.createSnapshot();
                    break;
                case "info":
                    System.out.println("Enter filename: ");
                    String filename = scanner.nextLine();
                    FolderSnapshot.info(filename);
                    break;
                case "status":
                    FolderSnapshot.status();
                    break;
                case "exit":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        }
    }
}
