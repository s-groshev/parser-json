import java.io.BufferedReader;
import java.util.Scanner;

public class C {
    public static void main(String[] args) {
        StringBuilder input=new StringBuilder();
        Scanner in=new Scanner(System.in);
        String addLine=in.nextLine();
        for(int i=1;i<5;i++){
            input.append(addLine);
            input.append("\n");
            addLine=in.nextLine();
        }

    }
}
