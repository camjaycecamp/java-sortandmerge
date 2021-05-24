// cameron campbell
// advanced java
// occc spring 2021
// sort and merge program

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * 
 * 	"So the selection algorithm of natural merge is, of the two integers you see, take the smaller of
 * 	the two that is equal to or larger than what you just wrote. That is not always the smaller of the
 * 	two that you see. Eventually neither number will satisfy that criteria, so choose the smaller and
 * 	start the process over again. After one of the sublists has been consumed, just append the other
 * 	one. For natural split, we will write to the sublists as far as we can in order."
 * 
 * 
 */

public class SortAndMerge {
	
	/*
	 * the main method handles both the command line arguments and normal console input. the user passes
	 * two file names to the program, each a file with a set number of unsorted integers. the method then 
	 * outsources the handling and sorting of these files to the FileHandler method, and once these files
	 * are successfully sorted the user is prompted for a third file name, where a file will be created
	 * under said name. the data of the previous two files is sorted via passing the arrays containing
	 * each file's data directly to the NaturalMerge method, before finally passing that merged data to
	 * the StoreMergedData method as an array.
	 */
	
	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);
		String firstFileName, secondFileName, mergedFileName;
		if (args.length == 1) 
		{
			firstFileName = args[0];
			System.out.println("Next, enter the name of second file with a list of numbers you'd like to sort: ");
			secondFileName = sc.nextLine();
			
			int firstArray[] = FileHandler(firstFileName);
			int secondArray[] = FileHandler(secondFileName);
			
			System.out.println("Finally, enter a name for the file that will store the merged data of these two files: ");
			mergedFileName = sc.nextLine();
			System.out.println("Merging data...");
			int mergedArray[] = NaturalMerge(firstArray, secondArray, (firstArray.length + secondArray.length));
			System.out.println("Data merged. Creating file and storing data...");
			StoreMergedData(mergedFileName, mergedArray);
			
			FileHandler(args[0]);
		}
		else if (args.length == 2) 
		{
			firstFileName = args[0];
			secondFileName = args[1];
			
			int firstArray[] = FileHandler(firstFileName);
			int secondArray[] = FileHandler(secondFileName);
			
			System.out.println("Finally, enter a name for the file that will store the merged data of these two files: ");
			mergedFileName = sc.nextLine();
			System.out.println("Merging data...");
			int mergedArray[] = NaturalMerge(firstArray, secondArray, (firstArray.length + secondArray.length));
			System.out.println("Data merged. Creating file and storing data...");
			StoreMergedData(mergedFileName, mergedArray);
		}
		else if (args.length == 3) 
		{
			firstFileName = args[0];
			secondFileName = args[1];
			mergedFileName = args[2];
			
			int firstArray[] = FileHandler(firstFileName);
			int secondArray[] = FileHandler(secondFileName);
			
			System.out.println("Merging data...");
			int mergedArray[] = NaturalMerge(firstArray, secondArray, (firstArray.length + secondArray.length));
			System.out.println("Data merged. Creating file and storing data...");
			StoreMergedData(mergedFileName, mergedArray);
		}
		else 
		{
			System.out.println("Welcome to the Sort and Merge Program!");
			System.out.println("Please enter the name of a file with a list of numbers you'd like to sort: ");
			firstFileName = sc.nextLine();
			System.out.println("Next, enter the name of second file with a list of numbers you'd like to sort: ");
			secondFileName = sc.nextLine();
			
			int firstArray[] = FileHandler(firstFileName);
			int secondArray[] = FileHandler(secondFileName);
			
			System.out.println("Finally, enter a name for the file that will store the merged data of these two files: ");
			mergedFileName = sc.nextLine();
			System.out.println("Merging data...");
			int mergedArray[] = NaturalMerge(firstArray, secondArray, (firstArray.length + secondArray.length));
			System.out.println("Data merged. Creating file and storing data...");
			StoreMergedData(mergedFileName, mergedArray);
		}
	}
	
	/*
	 * the FileHandler method reads the passed file name with java.io.FileReader, creates an array of 
	 * the file's integers of a size dictated by the file's format, and stores each integer into a string
	 * array. The string array is then converted into the integer array via a for-loop, and that integer
	 * array is then passed to NaturalSort.
	 */
	
	static int[] FileHandler(String fileLocation) 
	{
		System.out.println("Reading file...");
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(fileLocation));
			String line = reader.readLine();
			int fileArray [] = new int[Integer.parseInt(line)];
			line = reader.readLine();
			String [] fileValues = line.trim().split("\\s+");
			
			for(int i = 0; i < fileArray.length; i++) 
			{
				fileArray[i] = Integer.parseInt(fileValues[i]);
			}
			reader.close();
			System.out.println("File successfully read. Sorting file...");
			int sortedArray[] = NaturalSort(fileArray);
			return sortedArray;
		} catch (IOException e) {
			System.out.println("File not found.");
			e.printStackTrace();
			int errorArray[] = new int[0];
			return errorArray;
		}
	}
	
	/*
	 * the NaturalSort method is a general handler for the NaturalSplit and NaturalMerge methods. As long as
	 * the length of the returned array is not 0, the condition will loop until the array does return a length
	 * of 0. once this happens, the method recognizes that the array is fully sorted, and passes it back to 
	 * the FileHandler method, which in turn passes it back to the main method.
	 */
	
	static int[] NaturalSort(int unsortedArray[]) 
	{
		while (NaturalSplit(unsortedArray).length != 0)
		{
			unsortedArray = NaturalSplit(unsortedArray);
		}
		System.out.println("File successfully sorted.");
		return unsortedArray;
	}
	
	/*
	 * the NaturalSplit method handles the split portion of the natural sort algorithm. two arrays representing 
	 * the "lists" that the passed array is split into are created and initialized at 0. Additionally, a variable
	 * representing the "list" that is currently in focus during the split process is created and initialized at 1
	 * for compatibility with the following for-loop. To start off the split process, the first element of the 
	 * passed array is inserted into the first "list" via invocation of the FillNextIndex method. the for-loop then
	 * picks up from that argument's endpoint, comparing every subsequent element in the passed array. Depending on 
	 * which of the if statements each element satisfies, they will either be placed in the same "list" as the previous
	 * element or will be placed in the opposite list, alternating the "list" currently in focus. Once all elements have been
	 * split into two "lists", the for-loop ends, and an if statement is quickly checked. If the if statement's condition,
	 * that the length of the second "list" would be zero, is satisfied, the passed array would be considered fully sorted,
	 * and the length of the second list(which would be zero) is returned to the NaturalSort method, and the sorting process
	 * is ended. Otherwise, the split "lists" are passed to the NaturalMerge method as arrays.
	 */
	
	static int[] NaturalSplit(int unsplitList[]) 
	{
		int splitList1[] = new int[0];
		int splitList2[] = new int[0];
		int currentList = 1;
		
		splitList1 = FillNextIndex(splitList1, unsplitList[0]);						// initialize the value of the array's first index to list 1 for compatibility with for-loop
		
		for (int i = 1; i < unsplitList.length; i++) 
		{
				if((unsplitList[i] > unsplitList[i-1]) && currentList == 1) 		// (1>) if current index value is greater than previous index value and list 1 is in focus
				{
					splitList1 = FillNextIndex(splitList1, unsplitList[i]);
					currentList = 1;
				}
				else if((unsplitList[i] > unsplitList[i-1]) && currentList == 2) 	// (2>) if current index value is greater than previous index value and list 2 is in focus
				{
					splitList2 = FillNextIndex(splitList2, unsplitList[i]);
					currentList = 2;
				}
				else if((unsplitList[i] < unsplitList[i-1]) && currentList == 1) 	// (2<) if current index value is less than previous index value and list 1 is in focus
				{
					splitList2 = FillNextIndex(splitList2, unsplitList[i]);
					currentList = 2;
				}
				else if((unsplitList[i] < unsplitList[i-1]) && currentList == 2) 	// (1<) if current index value is less than previous index value and list 2 is in focus
				{
					splitList1 = FillNextIndex(splitList1, unsplitList[i]);
					currentList = 1;
				}
				else if((unsplitList[i] == unsplitList[i-1]) && currentList == 1) 	// (1=) if current index value is equal to previous index value and list 1 is in focus
				{
					splitList1 = FillNextIndex(splitList1, unsplitList[i]);
					currentList = 1;
				}
				else if((unsplitList[i] == unsplitList[i-1]) && currentList == 2) 	// (1=) if current index value is equal to previous index value and list 2 is in focus
				{
					splitList2 = FillNextIndex(splitList2, unsplitList[i]);
					currentList = 2;
				}
		}
		if(splitList2.length == 0) 
		{
			return splitList2;
		}
		
		int mergedArray[] = NaturalMerge(splitList1, splitList2, unsplitList.length);
		
		return mergedArray;
	}
	
	
	/*
	 * the NaturalMerge method handles the merging portion of the natural sort algorithm. An array representing
	 * the final, merged array is created and initialized with the passed array size. For compatibility with the 
	 * method's main for-loop, the first element of the merged array is initialized to zero, and is subjected to an 
	 * initial if statement block so that it might find its first merged value. Additionally, integers for the index
	 * values of each array present in the method are created and initialized to zero. the method's main for-loop
	 * is encapsulated in a try-catch block, which will come in handy later. the for-loop employs several large
	 * if and if-else statement blocks; each compares the value at the relevant indexes of the first "list", second
	 * "list", and merged array. When a value is moved from either "list" to the merged array, the index integers
	 * of both that "list" and the merged array are incremented. The incrementation of the "list" indexes allows
	 * the functionality of the natural merge. Eventually, the for-loop will reach an error due to the index value of 
	 * either compared "list" reaching out of bounds, meaning that one of the "lists" has reached the end of its size.
	 * This will redirect the focus into the catch block. Due to the constant incrementation of the "list" and merged
	 * array indexes via integers, the progress in the for-loop is "saved", and these index values are used to
	 * initialize a for-loop depending on which "list" still hasn't reached the end of its size. The rest of that
	 * "list's" values are added as elements to the end of the array, before the array is finally returned to the
	 * invoker.
	 */
	
	static int[] NaturalMerge(int unmergedList1[], int unmergedList2[], int arraySize) 
	{
		int mergedArray[] = new int[arraySize];
		mergedArray[0] = 0;
		int list1Index = 0;
		int list2Index = 0;
		int mergedArrayIndex = 0;
		
		
		if((unmergedList1[0] >= mergedArray[0]) && (unmergedList1[0] < unmergedList2[0]))
		{
			list1Index++;
			mergedArrayIndex++;
			mergedArray[0] = unmergedList1[0];
		}
		else if((unmergedList2[0] >= mergedArray[0]) && (unmergedList2[0] < unmergedList1[0]))
		{
			list2Index++;
			mergedArrayIndex++;
			mergedArray[0] = unmergedList2[0];
		}
		else if((unmergedList2[0] >= mergedArray[0]) && (unmergedList2[0] == unmergedList1[0]))
		{
			list1Index++;
			mergedArrayIndex++;
			mergedArray[0] = unmergedList1[0];
		}
		
		
		try 
		{
			for(int i = 1; i < mergedArray.length; i++) 
			{
				if(unmergedList1[list1Index] >= mergedArray[i-1] && unmergedList2[list2Index] >= mergedArray[i-1])						// if list 1 AND list 2's values are greater than or equal to array's previous value
				{
					if(unmergedList1[list1Index] < unmergedList2[list2Index] ||
							unmergedList1[list1Index] == unmergedList2[list2Index] || 													// and if list 1 is less than list 2, equal to list 2, or equal to array's previous value
							unmergedList1[list1Index] == mergedArray[i-1])
					{
						mergedArray[i] = unmergedList1[list1Index];
						mergedArrayIndex++;
						list1Index++;
					}
					else if(unmergedList1[list1Index] > unmergedList2[list2Index] ||													// and if list 2 is less than list 1 or list 2 is equal to array's previous value
							unmergedList2[list2Index] == mergedArray[i-1])											
					{
						mergedArray[i] = unmergedList2[list2Index];
						mergedArrayIndex++;
						list2Index++;
					}
				}
				else if(unmergedList1[list1Index] >= mergedArray[i-1] && unmergedList2[list2Index] < mergedArray[i-1])					// else if list 1's value is greater than or equal to array and list 2's value is less than array	
				{
					mergedArray[i] = unmergedList1[list1Index];
					mergedArrayIndex++;
					list1Index++;
				}
				else if(unmergedList2[list2Index] >= mergedArray[i-1] && unmergedList1[list1Index] < mergedArray[i-1])					// else if list 2's value is greater than or equal to array and list 1's value is less than array	
				{
					mergedArray[i] = unmergedList2[list2Index];
					mergedArrayIndex++;
					list2Index++;
				}
				else if((unmergedList2[list2Index] < mergedArray[i-1]) && (unmergedList1[list1Index] < mergedArray[i-1]))				// else if array's previous value is greater than both lists
				{
					if(unmergedList2[list2Index] < unmergedList1[list1Index]) 															// and if list 2's value is less than list 1
					{
						mergedArray[i] = unmergedList2[list2Index];
						mergedArrayIndex++;
						list2Index++;
					}
					else if(unmergedList2[list2Index] > unmergedList1[list1Index]) 														// and if list 2's value is greater than list 1
					{
						mergedArray[i] = unmergedList1[list1Index];
						mergedArrayIndex++;
						list1Index++;
					}
					else if(unmergedList2[list2Index] == unmergedList1[list1Index]) 													// and if list 2's value is equal to list 1
					{
						mergedArray[i] = unmergedList1[list1Index];
						mergedArrayIndex++;
						list1Index++;
					}
				}
			}
		}
		catch (Exception arraySizeSurpassed)
		{
			
			if(unmergedList1.length == list1Index) 
			{
				for(int i = mergedArrayIndex; i < mergedArray.length; i++) 
				{
					mergedArray[i] = unmergedList2[list2Index];
					list2Index++;
				}
			}
			else if(unmergedList2.length == list2Index) 
			{
				for(int i = mergedArrayIndex; i < mergedArray.length; i++) 
				{
					mergedArray[i] = unmergedList1[list1Index];
					list1Index++;
				}
			}
		}
		
		return mergedArray;
	}
	
	/*
	 * the FillNextIndex method resizes the passed array by [arraySize + 1]. The values
	 * of the old array are ported into the new array, and the loose value passed alongside
	 * the old array is inserted into the new array's extra index. This method is essentially
	 * an extremely convenient way to have a self-resizing array for variable sizes, like the
	 * "lists" in the NaturalSplit method.
	 */
	
	static int[] FillNextIndex(int targetArray[], int looseValue) 
	{
		int expandedArray[] = new int[targetArray.length + 1];
		
		for(int i = 0; i < targetArray.length; i++)
		{
			expandedArray[i] = targetArray[i];
		}
		
		expandedArray[targetArray.length] = looseValue;
		return expandedArray;
	}
	
	/*
	 * the StoreMergedData method handles the storage of the merged data files and
	 * third file name passed from the main method. java.io.PrintWriter is used to
	 * read the passed file name, create a file from said file name, and store the 
	 * contents of the merged data files onto the created file.
	 */
	
	static void StoreMergedData(String fileLocation, int data[]) 
	{
		PrintWriter output;
		System.out.println("Storing data at '" + fileLocation + "'...");
		try 
		{
			output = new PrintWriter(fileLocation);
			output.write(data.length + " ");
			for(int i = 0; i < data.length; i++) 
			{
				output.write(data[i] + " ");
			}
			System.out.println("Data successfully stored. Thank you for using the Sort and Merge Program!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}



