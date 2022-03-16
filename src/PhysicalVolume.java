import java.util.*;

public class PhysicalVolume extends Volume{
    private Drive hardDrive;


    public PhysicalVolume(String name, Drive hardDrive) {
        super(hardDrive.getStorage(), name);
        this.hardDrive = hardDrive;
    }
}
