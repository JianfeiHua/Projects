package Projects;
//https://app.devdraft.com/#!/workspace/6faabeea-e194-4d53-8864-382b8fc7c505
import java.util.*;
import java.lang.*;
import java.io.*;
public class DevDraftMinionZapper {

    public static void main (String[] args) throws java.lang.Exception
    {
        BufferedReader in = new BufferedReader (new InputStreamReader(System.in));                
        String[] parts = in.readLine().trim().split(" ");
        int N = Integer.parseInt(parts[0]);
        int X = Integer.parseInt(parts[1]);
        int Y = Integer.parseInt(parts[2]);
        parts = in.readLine().trim().split(" ");
        int count = 0;
        for (int i = 0; i < parts.length; i++) {
            int health = Integer.parseInt(parts[i]);
            if (health <= X) {
                X -= Y;
                count++;
            }
        }
        System.out.println(count);
    }

}
