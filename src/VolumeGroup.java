
import java.util.*;

public class VolumeGroup {
    private int storage;
    private String uuid;
    private String name;
    private ArrayList<PhysicalVolume> pvList;
    private ArrayList<LogicalVolume> lvList;
    private int storageUsed;



    public VolumeGroup(String name, PhysicalVolume pv) {

        this.name = name;
        UUID u = UUID.randomUUID();
        this.uuid = u.toString();
        storageUsed = 0;
        this.pvList = new ArrayList<PhysicalVolume>();
        this.lvList = new ArrayList<LogicalVolume>();
        addNewPv(pv);
    }

    public boolean addNewLv(LogicalVolume lv)
    {
        if(storageUsed+lv.getStorage() > storage) return false;
        else {
            lvList.add(lv);
            storageUsed += lv.getStorage();
            return true;
        }
    }

    public void addNewPv(PhysicalVolume pv)
    {
        pvList.add(pv);
        pv.assign(this);
        storage += pv.getStorage();
    }

    public int getStorage() {
        return storage;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public ArrayList<PhysicalVolume> getPvList() {
        return pvList;
    }

    public ArrayList<LogicalVolume> getLvList() {
        return lvList;
    }

    public int getStorageUsed() {
        return storageUsed;
    }
}
