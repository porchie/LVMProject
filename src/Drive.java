import java.util.*;


public class Drive {
    private String name;
    private int storage;
    private boolean pvAssociated;

    public Drive(String name, int storage) {
        this.name = name;
        this.storage = storage;
    }

    public boolean isPvAssociated()
    {
        return pvAssociated;
    }
    public void assignPv()
    {
        pvAssociated = true;
    }

    public String getName() {
        return name;
    }

    public int getStorage() {
        return storage;
    }
}
