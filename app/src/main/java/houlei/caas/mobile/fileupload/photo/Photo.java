package houlei.caas.mobile.fileupload.photo;

public class Photo {

    private final String path;
    private final String desc;
    private boolean selected = false;

    public Photo(String path, String desc, boolean selected) {
        this.path = path;
        this.desc = desc;
        this.selected = selected;
    }

    public String getPath() {
        return path;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
