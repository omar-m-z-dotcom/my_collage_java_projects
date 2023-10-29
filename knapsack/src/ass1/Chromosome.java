package ass1;

import java.io.*;
import java.util.Scanner;

public class Chromosome {
 	
	public static void main (String[] args) {
		
		
		File f=new File("in.txt");
		try {
			Scanner s =new Scanner(f);
			
			int num_of_test_cases=s.nextInt();
			System.out.println(num_of_test_cases);
			
			for (int i=0;i<num_of_test_cases;i++)
			{
				int size_of_Chromosome=s.nextInt();
				System.out.println(size_of_Chromosome);
				int arr_of_wights [] = new int [size_of_Chromosome];
				int arr_of_vaules [] = new int [size_of_Chromosome];
				int arr_of_knapsack [] =new int [size_of_Chromosome];
				
				int size_of_knapsack=s.nextInt();
				System.out.println(size_of_knapsack);
				
				for(int j= 0;j<size_of_Chromosome;j++)
				{
					int wight=s.nextInt();
					int value=s.nextInt();
					arr_of_wights[j]=wight;
					arr_of_vaules[j]=value;
					System.out.println(wight);
					System.out.println(value);
					
				}	
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	

}
