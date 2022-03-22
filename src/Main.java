import java.util.*;
import java.io.*;


public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Scanner sc = new Scanner(System.in);
        String cmd = "";
        String fileName = "src\\conf";
        Manager manager = new Manager(fileName);
        while(!cmd.equals("exit"))
        {
            System.out.print("cmd# ");
            cmd = sc.next();
            String ins = sc.nextLine();
            ins = (ins.length()>0)? ins.substring(1): ins;
            System.out.println(manager.process(cmd,ins));
        }
        manager.saveToFile(fileName);
        sc.close();

    }
}
