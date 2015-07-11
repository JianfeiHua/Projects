package Projects;
import java.util.*;
import java.lang.*;
import java.io.*;
public class DevDraftDominoes {
    public static long[] getRightCascade(int[] heights) {
        Stack<Long> stack = new Stack<Long>();
        long[] cascade = new long[heights.length];
        for (int i = heights.length-1; i >= 0; i--) {
            if (heights[i] == 0) continue;
            long reach = i + heights[i];
            while (!stack.isEmpty() && reach >= stack.peek()) {
                reach = Math.max(reach, stack.pop() + stack.pop());
            }
            stack.push(reach - i);
            stack.push((long) i);
            cascade[i] = reach - i;
        }
        return cascade;
    }
    
    
    public static long[] getLeftCascade(int[] heights) {
        Stack<Long> stack = new Stack<Long>();
        long[] cascade = new long[heights.length];
        for (int i = 0; i < heights.length; i++) {
            if (heights[i] == 0) continue;
            long reach = i - heights[i];
            while (!stack.isEmpty() && reach <= stack.peek()) {
                reach = Math.min(reach,  stack.pop() - stack.pop());
            }
            stack.push(i - reach);
            stack.push((long) i);
            cascade[i] = i - reach;
        }
        return cascade;
    }
    public static void main (String[] args) throws java.lang.Exception
    {
        // parse input data
        BufferedReader in = new BufferedReader (new InputStreamReader(System.in)); 
        int N = Integer.parseInt(in.readLine().trim());
        String[] parts = in.readLine().trim().split(" ");
        int[] heights = new int[N];
        for (int i = 0; i < N; i++) {
            heights[i] = Integer.parseInt(parts[i]);
        }
        // get and print cascades
        long[] rightCascade = getRightCascade(heights);
        for (int i = 0; i < N; i++) {
            System.out.print(rightCascade[i]);
            if (i != N-1) {
                System.out.print(" ");
            }
        }
        System.out.println();
        long[] leftCascade = getLeftCascade(heights);
        for (int i = 0; i < N; i++) {
            System.out.print(leftCascade[i]);
            if (i != N-1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

}
