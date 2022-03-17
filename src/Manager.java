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

    /*
    cmds


    install-drive [drive_name] [size] x
    list-drives x
    pvcreate [pv_name] [drive_name] x
    pvlist x
    vgcreate [vg_name] [pv_name] x
    vglist x
    lvcreate [lv_name] [size] [vg_name] x
    lvlist x


     */

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
    private boolean pvCreate(String name, String drive)
    {
        Drive dr = drvList.get(drive);
        if(dr==null) return false;
        if(dr.isPvAssociated()) return false;
        else
        {
            PhysicalVolume pv = new PhysicalVolume(name,dr);
            pvList.put(name,pv);
            return true;
        }
    }

    private boolean vgCreate(String name, String pvName)
    {
        PhysicalVolume pv = pvList.get(pvName);
        if(vgList.containsKey(name)) return false;
        if(pv==null) return false;
        if(pv.isAssigned()) return false;
        else
        {
            vgList.put(name,new VolumeGroup(name,pv));
            return true;
        }
    }

    private boolean vgExtend(String vgName, String pvName)
    {
        PhysicalVolume pv = pvList.get(pvName);
        VolumeGroup vg = vgList.get(vgName);
        if(pv==null||vg==null) return false;
        if(pv.isAssigned()) return false;
        else
        {
            vg.addNewPv(pv);
            return true;
        }
    }

    private boolean lvCreate(String name, int size,String vgName)
    {
        VolumeGroup vg = vgList.get(vgName);
        if(vg==null) return false;

        LogicalVolume lv = new LogicalVolume(size,name,vg);
        if(vg.addNewLv(lv))
        {
            lvList.put(name,lv);
            return true;
        }
        else
        {
            return false;
        }
    }

    private String drvList()
    {
        String str = "";
        for (Map.Entry<String, Drive> entry : drvList.entrySet()) {
            String name = entry.getKey();
            Drive drv = entry.getValue();
            str += name + " " + "[" + drv.getStorage() + "G]\n";
        }
        return str;
    }

    private String pvList()
    {
        String str = "";

        //sorting
        Map<String, ArrayList<PhysicalVolume>> vgSortedPv =  new HashMap<>();
        for(Map.Entry<String,PhysicalVolume> entry:pvList.entrySet()){
           PhysicalVolume pv = entry.getValue();
           String vgName = pv.getVgName();
           ArrayList<PhysicalVolume> arr = vgSortedPv.get(vgName);
           if(arr==null) vgSortedPv.put(vgName,new ArrayList<PhysicalVolume>(Arrays.asList(pv)));
           else
           {
               arr.add(pv);
           }
        }


        // adding str;
        for(Map.Entry<String,ArrayList<PhysicalVolume>> entry: vgSortedPv.entrySet())
        {
            String vgName = entry.getKey();
            ArrayList<PhysicalVolume> arr = entry.getValue();
            for(PhysicalVolume pv: arr)
            {
                str+= pv.getName() + ": " + colonIt(pv.getStorage() + "G") + " ";
                str+= (pv.isAssigned()) ? colonIt(vgName) + " " :"";
                str+= colonIt(pv.getUuid()) + "\n";
            }
        }
        return str;
    }

    private String vgList()
    {
        String str = "";
        for(Map.Entry<String,VolumeGroup> entry:vgList.entrySet()){
            String name = entry.getKey();
            VolumeGroup vg = entry.getValue();
            str+= name + ": total:" + colonIt(vg.getStorage()+"G") + " avaliable:"  + colonIt(vg.getStorage()-vg.getStorageUsed() + "G");
            str+= " " + vg.getPvList().toString();
            str+= " " + colonIt(vg.getUuid()) + "\n";
        }
        return str;
    }

    private String lvList()
    {
        String str = "";
        for(Map.Entry<String,VolumeGroup> entry:vgList.entrySet()){
            VolumeGroup vg = entry.getValue();
            for(LogicalVolume lv:vg.getLvList())
            {
                str+= lv.getName() + ": " + colonIt(lv.getStorage()+"G") + " " + colonIt(vg.getName()) + " "  + colonIt(lv.getUuid()) + "\n";
            }
        }
        return str;
    }


    private String colonIt(String in)
    {
        return "[" + in + "]";
    }






}
