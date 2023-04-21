package resources;

import base.BasePageAPI;
import java.io.File;

public class FolderUtil extends BasePageAPI {
    public static String createFolder(String nameFolder) {
        String pathFolder = System.getProperty("user.dir")
                + "\\src\\test\\java\\logs\\" + nameFolder;
        File logsFolder = new File(pathFolder);
        if (!logsFolder.exists()) {
            logsFolder.mkdirs();
            System.out.println("Carpeta creada: "+nameFolder);
        } else {
            System.out.println("Carpeta ya existente: " + nameFolder);
        }
        return logsFolder.getAbsolutePath();
    }

    public static String createFolder(String nameFolder, String pathFolder) {
        File logsFolder = new File(pathFolder);
        if (!logsFolder.exists()) {
            logsFolder.mkdirs();
            System.out.println("Carpeta creada: "+nameFolder);
        } else {
            System.out.println("Carpeta ya existente: " + nameFolder);
        }
        return logsFolder.getAbsolutePath();
    }
}
