import java.util.*;


public class LogicalVolume extends Volume{
    private VolumeGroup vg;

    public LogicalVolume(int storage,String name, VolumeGroup vg) {
        super(storage,name);
        this.vg = vg;
    }

}
