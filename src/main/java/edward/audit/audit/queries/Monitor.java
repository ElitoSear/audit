package edward.audit.audit.queries;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class Monitor extends Thread {
    private final File file;
    public String oldContent = "";
    public String content = "";

    public Monitor(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        try {
            Path dir = file.getParentFile().toPath();
            String targetFileName = file.getName();

            WatchService watchService = FileSystems.getDefault().newWatchService();
            dir.register(watchService,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_CREATE);

            System.out.println("Watching " + file.getAbsolutePath());

            while (true) {
                WatchKey key = watchService.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    Path changed = ((WatchEvent<Path>) event).context();
                    if (changed.getFileName().toString().equals(targetFileName)) {
                        onChange();
                    }
                }

                if (!key.reset()) break;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onChange(){
        System.out.println("File changed: " + System.currentTimeMillis());
        content = getContent();
        System.out.println(content);

    }

    public String getContent() {
        oldContent = content;

        try {
            content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        } catch (IOException error) {
            try {
                content = Files.readString(file.toPath(), StandardCharsets.ISO_8859_1);
            } catch (IOException fallbackError) {
                fallbackError.printStackTrace();
            }
        }

        if (content.equals(oldContent)) {
            System.out.println("Both are equal, returning old content");
            return oldContent;
        }

        if (content.startsWith(oldContent)) {
            System.out.println("Content starts with old content, appending new part");
            return oldContent + content.substring(oldContent.length());
        }

        if(oldContent.endsWith(content)) {
            return oldContent + content.substring(oldContent.length());
        }

        return oldContent + content;
    }
}