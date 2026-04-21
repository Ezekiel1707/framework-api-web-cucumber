package resources;

import base.basePageUI;
import java.io.File;
import java.nio.file.Paths;

public class folderUtil extends basePageUI {

    public static String createFolder(String nameFolder) {
        String pathFolder = Paths.get(System.getProperty("user.dir"), "src", "test", "java", "logs", nameFolder)
                .toString();
        File logsFolder = new File(pathFolder);
        if (!logsFolder.exists()) {
            logsFolder.mkdirs();
            System.out.println("Carpeta creada: " + nameFolder);
        } else {
            System.out.println("Carpeta ya existente: " + nameFolder);
        }
        return logsFolder.getAbsolutePath();
    }

    public static String createFolder(String nameFolder, String pathFolder) {
        File logsFolder = new File(pathFolder);
        if (!logsFolder.exists()) {
            logsFolder.mkdirs();
            System.out.println("Carpeta creada: " + nameFolder);
        } else {
            System.out.println("Carpeta ya existente: " + nameFolder);
        }
        return logsFolder.getAbsolutePath();
    }
}
