import java.util.*;


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String cmd = "";
        Manager manager = new Manager();
        while(!cmd.equals("exit"))
        {
            System.out.print("cmd# ");
            cmd = sc.next();
            String ins = sc.nextLine();
            ins = (ins.length()>0)? ins.substring(1): ins;
            System.out.println(manager.process(cmd,ins));
        }




    }
}
