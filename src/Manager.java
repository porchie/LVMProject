import java.util.*;

public class Manager {
    private Map<String, PhysicalVolume> pvList;
    private Map<String, LogicalVolume> lvList;
    private Map<String, VolumeGroup> vgList;
    private Map<String, Drive> drvList;


    public Manager() {
        pvList = new HashMap<>();
        lvList = new HashMap<>();
        vgList = new HashMap<>();
        drvList = new HashMap<>();
    }

    private boolean installDrv(String name, int size)
    {
        Drive dr = new Drive(name,size);
        if(drvList.containsKey(name)) return false;
        else
        {
            drvList.put(name,dr);
            return true;
        }
    }
    private boolean createPv(String name, String drive)
    {
        Drive dr = drvList.get(drive);
        if(dr==null) return false;
        else
        {
            drvList.put(name,dr);//do check for assigned another pv
            return true;
        }
    }
}
