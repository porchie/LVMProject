
import java.util.*;

public class VolumeGroup extends Volume{
    private ArrayList<PhysicalVolume> pvList;
    private ArrayList<LogicalVolume> lvList;


    public VolumeGroup(String name, PhysicalVolume pv) {
        super(pv.getStorage(), name);
        this.pvList = pvList;
    }

    public boolean addNewLv(LogicalVolume lv)
    {
        int storageUsed = sumVolumeStorage(lvList);
        if(storageUsed+lv.getStorage() > getStorage()) return false;
        else {
            lvList.add(lv);
            return true;
        }
    }

    public void addNewPv(PhysicalVolume pv)
    {
        pvList.add(pv);
        setStorage(sumVolumeStorage(pvList));
    }


}
