// package algos;

//Java implementation of Best - Fit algorithm
import java.util.Scanner;
// import java.io.*;
public class App
{
 Scanner in = new Scanner(System.in);


 int m= in.nextInt();//number of blocks
 int n= in.nextInt();//number of processes
 
//  System.out.println();

    int blockSize[] = new int[m];
	int processSize[] = new int[n];
	//static size
	int BS[] = new int[m];
	int PS[] = new int[n];
	int allocation[] = new int[n];//alocation indexes
	void inputs()
	{
	    for(int i=0;i<m;i++)
     {
         int x=in.nextInt();
         blockSize[i]=x;
         BS[i]=x;
     } 
     for(int i=0;i<n;i++)
     {
         int x=in.nextInt();
         processSize[i]=x;
         PS[i]=x;
     } 
	}
	void initialize()
	{
	    for (int i = 0; i < allocation.length; i++)
         allocation[i] = -1;
     for(int i=0;i<m;i++)
         blockSize[i]=BS[i];
     for(int i=0;i<n;i++)
         processSize[i]=PS[i];
	}
 void bestFit()
	{
	    initialize();
		for (int i=0; i<n; i++)
		{
			int index = -1;
			for (int j=0; j<m; j++)
			{
				if (blockSize[j] >= processSize[i])
				{
					if (index == -1)
						index = j;
					else if (blockSize[index] > blockSize[j])
						index = j;
				}
			}
			if (index != -1)
			{
				allocation[i] = index;
				blockSize[index] -= processSize[i];
			}
		}
		System.out.println("Best Fit");
		show();
	}
	void firstFit()
 {
     initialize();
     for (int i = 0; i < n; i++)
     {
         for (int j = 0; j < m; j++)
         {
             if (blockSize[j] >= processSize[i])
             {
                 allocation[i] = j;
                 blockSize[j] -= processSize[i];
   
                 break;
             }
         }
     }
     System.out.println("First Fit");
     show();
 }
 void worstFit()
 {
     initialize();
     for (int i=0; i<n; i++)
     {
         int index = -1;
         for (int j=0; j<m; j++)
         {
             if (blockSize[j] >= processSize[i])
             {
                 if (index == -1)
                     index = j;
                 else if (blockSize[index] < blockSize[j])
                     index = j;
             }
         }
         if (index != -1)
         {
             allocation[i] = index;
             blockSize[index] -= processSize[i];
         }
     }
     System.out.println("Worst Fit");
     show();
 }
 void NextFit() 
 {
     initialize();
     int j=0;
     for (int i = 0; i < n; i++) 
     {
         int count =0;
         while (j < m) 
         {
             count++;
             if (blockSize[j] >= processSize[i]) 
             {
                 allocation[i] = j;
                 blockSize[j] -= processSize[i];
                 break;
             }
             if(count==n+1)
				    break;
             j = (j + 1) % m;
         }
     }
     System.out.println("Next Fit");
     show();
 }
 void show()
 {
     
		System.out.println("Process No.\t\tProcess Size\t\t\tBlock no.");
		for (int i = 0; i < n; i++)
		{
			System.out.print( (i+1) + "\t\t\t" + processSize[i] + "\t\t\t\t");
			if (allocation[i] != -1)
				System.out.print(allocation[i] + 1);
			else
				System.out.print("Not Allocated");
			System.out.println();
		}
		System.out.println("\n");
 }
	public static void main(String[] args)
	{   
        System.out.println("Welcome");
	    App obj = new App();
	    obj.inputs();
		obj.bestFit();
		obj.firstFit();
		obj.worstFit();
		obj.NextFit();
	}
}

// 4
// 5
// 212 417 112 426
// 100 500 200 300 600