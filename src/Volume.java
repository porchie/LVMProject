import java.util.*;


public class Volume {
    private int storage;
    private String uuid;
    private String name;


    public Volume(int storage, String name) {
        this.storage = storage;

        UUID u = UUID.randomUUID();
        this.uuid = u.toString();

        this.name = name;
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

    public static int sumVolumeStorage(ArrayList<? extends Volume> v)
    {
        return v.stream().mapToInt(l -> l.getStorage()).sum();
    }
}
