import java.util.*;
import java.io.*;


public class Manager {
    private Map<String, PhysicalVolume> pvList;
    private Map<String, LogicalVolume> lvList;
    private Map<String, VolumeGroup> vgList;
    private Map<String, Drive> drvList;
    private ArrayList<String> cmdList;


    public Manager(String fileName) {
        pvList = new HashMap<>();
        lvList = new HashMap<>();
        vgList = new HashMap<>();
        drvList = new HashMap<>();
        cmdList = new ArrayList<>();
        loadFromSave(fileName);
    }
    private void loadFromSave(String fileName)
    {
        try {
            File file = new File(fileName);
            Scanner sc = new Scanner(file);

            while(sc.hasNext())
            {
                String cmd = sc.next();
                String ins = sc.nextLine();
                ins = (ins.length()>0)? ins.substring(1): ins;
                process(cmd,ins);
            }
            sc.close();
        }
        catch(FileNotFoundException e) {
            System.out.println(e.getMessage() + " asfAfnkadgfsdgnj");
        }
    }

    public void saveToFile(String fileName)
    {
        try {
            File file = new File(fileName);
            FileWriter writer = new FileWriter(fileName);

            for(String s: cmdList)
            {
                writer.write(s + System.lineSeparator());
            }
            writer.close();
        }
        catch (Exception e){

        }
    }
    private void addCmd(String cmd, String params)
    {
        cmdList.add(cmd + " " + params);
    }
    public String process(String cmd, String params)
    {
        try {

            String[] cmdArr = params.split(" ");
            if (cmd.equals("install-drive")) {
                addCmd(cmd,params);
                String name = cmdArr[0];
                int size = Integer.parseInt(cmdArr[1].substring(0,cmdArr[1].length()-1));
                if (installDrv(name, size))
                {
                    return "drive "  + name + " installed";
                }
            }
            else if (cmd.equals("pvcreate")) {
                addCmd(cmd,params);
                String name = cmdArr[0];
                String dr = cmdArr[1];
                if(pvCreate(name,dr))
                {
                    return name + " created";
                }
            }
            else if (cmd.equals("lvcreate")) {
                addCmd(cmd,params);
                String name = cmdArr[0];
                int size = Integer.parseInt(cmdArr[1].substring(0,cmdArr[1].length()-1));
                String vgName = cmdArr[2];
                if(lvCreate(name,size,vgName))
                {
                    return name + " created";
                }
            }
            else if (cmd.equals("vgcreate")) {
                addCmd(cmd,params);
                String name = cmdArr[0];
                String pvName = cmdArr[1];
                if(vgCreate(name,pvName))
                {
                    return name + " created";
                }
            }
            else if(cmd.equals("vgextend"))
            {
                addCmd(cmd,params);
                String name = cmdArr[0];
                String pvName = cmdArr[1];
                if(vgExtend(name,pvName))
                {
                    return pvName + " added to " + name;
                }
            }
            else if (cmd.equals("list-drives")) {
                return drvList();
            }
            else if (cmd.equals("pvlist")) {
                return pvList();
            }
            else if (cmd.equals("vglist")) {
                return vgList();
            }
            else if (cmd.equals("lvlist")) {
                return lvList();
            }
            else if(cmd.equals("exit")) {
                return "program exited, volumes saved";
            }
            else {
                return "non-valid input";
            }
            return "Ran into an Error";
        }
        catch (IndexOutOfBoundsException e)
        {
            return "invalid input";
        }
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
        if (pvList.containsKey(name)) return false;
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
        if (lvList.containsKey(name)) return false;
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
        String notInVg = "not in a vg";
        //sorting
        Map<String, ArrayList<PhysicalVolume>> vgSortedPv =  new HashMap<>();
        for(Map.Entry<String,PhysicalVolume> entry:pvList.entrySet()){
           PhysicalVolume pv = entry.getValue();
           String vgName = pv.getVgName();
           if(vgName == null)  vgName = notInVg;
           ArrayList<PhysicalVolume> arr = vgSortedPv.get(vgName);
           if (arr == null) vgSortedPv.put(vgName, new ArrayList<PhysicalVolume>(Arrays.asList(pv)));
           else arr.add(pv);


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
