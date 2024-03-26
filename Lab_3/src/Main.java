import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private static final FolderSnapshot folderSnapshot = new FolderSnapshot("C:/Users/NickPC/Projects/lab3/MyFiles");

    public static void main(String[] args) {
        // Start the scheduled detection thread
        executorService.submit(() -> {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    folderSnapshot.compareSnapshot(); // Removed incorrect parameters
                }
            }, 0, 5000); // Detect changes every 5 seconds
        });

        // Start the console input thread
        executorService.submit(() -> {
            TaskSelector.consoleMenu();
        });
    }
}
