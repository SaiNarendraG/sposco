
//Java implementation of Best - Fit algorithm
import java.util.Scanner;

public class BestFit {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter number of memory blocks");
        int m = in.nextInt();// number of blocks

        System.out.println("Enter number of processes");
        int n = in.nextInt();// number of processes

        int blockSize[] = new int[m];
        int processSize[] = new int[n];
        int allocation[] = new int[n];

        for (int i = 0; i < m; i++) {
            int x = in.nextInt();
            blockSize[i] = x;

        }
        for (int i = 0; i < n; i++) {
            int x = in.nextInt();
            processSize[i] = x;

        }

        for (int i = 0; i < n; i++)
            allocation[i] = -1;

        for (int i = 0; i < n; i++) {
            int index = -1;
            for (int j = 0; j < m; j++) {
                if (blockSize[j] >= processSize[i]) {
                    if (index == -1)
                        index = j;
                    else if (blockSize[index] > blockSize[j])
                        index = j;
                }
            }
            if (index != -1) {
                allocation[i] = index;
                blockSize[index] -= processSize[i];
            }
        }
        System.out.println("Best Fit");

        System.out.println("Process No.\t\tProcess Size\t\t\tBlock no.");
        for (int i = 0; i < n; i++) {
            System.out.print((i + 1) + "\t\t\t" + processSize[i] + "\t\t\t\t");
            if (allocation[i] != -1)
                System.out.print(allocation[i] + 1);
            else
                System.out.print("Not Allocated");
            System.out.println();
        }
        System.out.println("\n");
        in.close();
    }

}

// 4
// 5
// 212 417 112 426
// 100 500 200 300 600