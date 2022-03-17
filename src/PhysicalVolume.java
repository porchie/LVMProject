import java.util.*;

public class PhysicalVolume extends Volume{
    private Drive hardDrive;
    private VolumeGroup vg;


    public PhysicalVolume(String name, Drive hardDrive) {
        super(hardDrive.getStorage(), name);
        this.hardDrive = hardDrive;
        hardDrive.assignPv();
    }

    public boolean isAssigned()
    {
        return vg!=null;
    }

    public void assign(VolumeGroup vg)
    {
        this.vg = vg;
    }
    public String getVgName()
    {
        if(vg==null) return null;
        return vg.getName();
    }
}
