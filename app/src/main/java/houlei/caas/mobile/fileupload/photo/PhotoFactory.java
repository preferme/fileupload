package houlei.caas.mobile.fileupload.photo;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class PhotoFactory {

    public static List<Photo> create(File workDir){
        File[] photos = workDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isFile()&&file.getName().endsWith(".jpg");
            }
        });
        List<Photo> result = new ArrayList<>(photos.length);

        for (File file : photos) {
            String path = file.getPath();
            String desc = file.getName();
            boolean selected = false;
            result.add(new Photo(path, desc, selected));
        }

        return result;
    }
}
