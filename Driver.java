package Assignment7;
/**
 * Prechar Xiong
 * 12/10/19
 * ICS 462-01
 * Assignment 7
 * This class is the Driver class which tests and debugs the methods contained within the buddy buffer manager,
 * to ensure that it is working properly.
 */
import java.util.ArrayList;
import java.util.Collections;

public class Driver {
	public static void main(String[] args) {
		ArrayList<Integer> bufferSizes;
		ArrayList<Integer> bufferAddresses;
		int bufferAddress;
		
		/**
		 * This initializes the buddy buffer manager and sets its max size to 512 and 
		 * max memory block to 10 and prints out its free blocks, status and how the 
		 * buddy buffer looks like.
		 */
		BuddyBufferManager bBM = new BuddyBufferManager(512,10);
		System.out.println("Initializing buffers");
		System.out.println("Expected values: 10 512 size buffers, Status Ok\n");
		bBM.printToFile("Initializing buffers");
		bBM.printToFile("Expected values: 10 512 size buffers, Status Ok\n");	
		bBM.printFreeBlocks();
		bBM.isTight();
		System.out.println("This a visualization of the buffers: \n" + bBM.toString());
		bBM.printToFile("This a visualization of the buffers: \n" + bBM.toString());
		
		/**
		 * This test attempts to request a 700 size block from the buddy buffer
		 * it then prints out the address of the 700 size block its free blocks, 
		 * status and how the buddy buffer looks like.
		 */
		System.out.println("Requesting 700 \nExpected values: \nAssigned address: -2\n");
		bBM.printToFile("Requesting 700 \nExpected values: \nAssigned address: -2\n");
		bufferAddress = bBM.getBlock(700);
		System.out.println("Actual = Assigned address: " + bufferAddress + "\n");
		bBM.printToFile("Actual = Assigned address: " + bufferAddress + "\n");
		bBM.printFreeBlocks();
		bBM.isTight();
		System.out.println("This a visualization of the buffers: \n" + bBM.toString());
		bBM.printToFile("This a visualization of the buffers: \n" + bBM.toString());
		
		/**
		 * This test requests for a 6 size block from the buddy buffer 
		 * it then prints out the address of where the 6 size block was assigned,
		 * its free blocks, status and how the buddy buffer looks like.
		 */
		System.out.println("Request buffer size 6\n "
				+ "Expected values: \n"
				+ "9 510 size buffers, 1 254 size buffer, 1 126 size buffer, \n"
				+ "1 62 size buffer, 1 30 size buffer, 1 14 size buffer and 1 6 size buffer, Status OK\n");
		bBM.printToFile("Request buffer size 6\n "
				+ "Expected values: \n"
				+ "9 510 size buffers, 1 254 size buffer, 1 126 size buffer, \n"
				+ "1 62 size buffer, 1 30 size buffer, 1 14 size buffer and 1 6 size buffer, Status OK\n");
		bufferAddress = bBM.getBlock(6);
		System.out.println("Actual = Assigned Address: " + bufferAddress + "\n");
		bBM.printToFile("Actual = Assigned Address: " + bufferAddress + "\n");
		bBM.printFreeBlocks();
		bBM.isTight();
		System.out.println("This a visualization of the buffers: \n" + bBM.toString());
		bBM.printToFile("This a visualization of the buffers: \n" + bBM.toString());
		
		/**
		 * This test returns the 6 size block using its address and prints out
		 * its free blocks, status and how the buddy buffer looks like.
		 */
		System.out.println("Return buffer size 6 \n"
				+ "Expected values: \n"
				+ "10 510 size buffers, Status OK\n");
		bBM.printToFile("Return buffer size 6 \n"
				+ "Expected values: \n"
				+ "10 510 size buffers, Status OK\n");
		bBM.returnBlock(4618);
		bBM.printFreeBlocks();
		bBM.isTight();
		System.out.println("This a visualization of the buffers: \n" + bBM.toString());
		bBM.printToFile("This a visualization of the buffers: \n" + bBM.toString());
		
		/**
		 * This test requests for 10, 510 size buffers one at a time and prints
		 * out their assigned address. It then prints out the free blocks, status and how 
		 * the buddy buffer looks like.
		 */
		System.out.println("Request 10 510 buffers \n"
				+ "Expected values: \n"
				+ "0 510 buffers, 0 for all buffers, Status Tight\n");
		bBM.printToFile("Request 10 510 buffers \n"
				+ "Expected values: \n"
				+ "0 510 buffers, 0 for all buffers, Status Tight\n");
		bufferAddresses = new ArrayList<Integer>();
		
		for(int i = 0; i < 10; i++) {
			bufferAddress = bBM.getBlock(510);
			bufferAddresses.add(bufferAddress);
			System.out.println("Actual = Assigned Address: " + bufferAddress + "\n");
			bBM.printToFile("Actual = Assigned Address: " + bufferAddress + "\n");
		}
		bBM.printFreeBlocks();
		bBM.isTight();	
		System.out.println("This a visualization of the buffers: \n" + bBM.toString());
		bBM.printToFile("This a visualization of the buffers: \n" + bBM.toString());
		
		/**
		 * This test attempts to request another 510 size buffer, prints out its assigned address,
		 * the free blocks and how the buddy buffer looks like.
		 */
		System.out.println("Request additional buffer\n" + 
				" Expected values:\n" + 
				" Assigned address: -1,\n" + 
				" 0 510 buffers, Status Tight\n");
		bBM.printToFile("Request additional buffer\n" + 
				" Expected values:\n" + 
				" Assigned address: -1,\n" + 
				" 0 510 buffers, Status Tight\n");
		bufferAddress = bBM.getBlock(510);
		System.out.println("Actual = Assigned Address: " + bufferAddress + "\n");
		bBM.printToFile("Actual = Assigned Address: " + bufferAddress + "\n");
		bBM.printFreeBlocks();
		bBM.isTight();
		System.out.println("This a visualization of the buffers: \n" + bBM.toString());
		bBM.printToFile("This a visualization of the buffers: \n" + bBM.toString());
		
		/**
		 * This test returns all 510 buffers to the buffer manager and prints
		 * the free blocks and how the buddy buffer looks like. 
		 */
		System.out.println("Return 10 510 buffers\n" + 
				" Expected values:\n" + 
				" 10 510 buffers, Status OK\n");
		bBM.printToFile("Return 10 510 buffers\n" + 
				" Expected values:\n" + 
				" 10 510 buffers, Status OK\n");
		for(Integer x : bufferAddresses){
			bBM.returnBlock(x);
		}
		bBM.printFreeBlocks();
		bBM.isTight();
		System.out.println("This a visualization of the buffers: \n" + bBM.toString());
		bBM.printToFile("This a visualization of the buffers: \n" + bBM.toString());
		
		/**
		 * This test requests for 19, 254 size buffers one at a time and prints
		 * out their assigned address. It then prints out the free blocks, status and how 
		 * the buddy buffer looks like.
		 */
		System.out.println("Request 19 254 size buffers\n" + 
				" Expected values:\n" + 
				" 0 510 buffers, 1 254 size buffers, 0 126 size buffers,\n" + 
				" 0 62 size buffers, 0 30 size buffers, 0 14 size buffers,  0 6 size buffers Status Tight\n");
		bBM.printToFile("Request 19 254 size buffers\n" + 
				" Expected values:\n" + 
				" 0 510 buffers, 1 254 size buffers, 0 126 size buffers,\n" + 
				" 0 62 size buffers, 0 30 size buffers, 0 14 size buffers,  0 6 size buffers Status Tight\n");
		bufferAddresses = new ArrayList<Integer>();
		
		for(int i = 0; i < 19; i++) {
			bufferAddress = bBM.getBlock(254);
			bufferAddresses.add(bufferAddress);
			System.out.println("Actual = Assigned Address: " + bufferAddress + "\n");
			bBM.printToFile("Actual = Assigned Address: " + bufferAddress + "\n");
		}
		bBM.printFreeBlocks();
		bBM.isTight();
		System.out.println("This a visualization of the buffers: \n" + bBM.toString());
		bBM.printToFile("This a visualization of the buffers: \n" + bBM.toString());
		
		/**
		 * This test attempts to request another 510 size buffer, prints out its assigned address,
		 * the free blocks and how the buddy buffer looks like.
		 */
		System.out.println("Request 1 510 Buffer after there is only 1 254 left.\n" + 
				"Expected values: -1\n");
		bBM.printToFile("Request 1 510 Buffer after there is only 1 254 left.\n" + 
				"Expected values: -1\n");
		bufferAddress = bBM.getBlock(510);
		System.out.println("Actual = Assigned Address: " + bufferAddress + "\n");
		bBM.printToFile("Actual = Assigned Address: " + bufferAddress + "\n");
		bBM.printFreeBlocks();
		bBM.isTight();
		System.out.println("This a visualization of the buffers: \n" + bBM.toString());
		bBM.printToFile("This a visualization of the buffers: \n" + bBM.toString());
		
		/**
		 * This test returns all 254 size buffers to the buffer manager and prints
		 * the free blocks and how the buddy buffer looks like. 
		 */
		System.out.println("Return 19 254 size buffers\n" + 
				" Expected values:\n" + 
				" 10 510 buffers, 0 254 size buffers, 0 126 size buffers,\n" + 
				" 0 62 size buffers, 0 30 size buffers, 0 14 size buffers,  0 6 size buffers Status OK\n");
		bBM.printToFile("Return 19 254 size buffers\n" + 
				" Expected values:\n" + 
				" 10 510 buffers, 0 254 size buffers, 0 126 size buffers,\n" + 
				" 0 62 size buffers, 0 30 size buffers, 0 14 size buffers,  0 6 size buffers Status OK\n");
				
		for(Integer x : bufferAddresses){
			bBM.returnBlock(x);
		}
		bBM.printFreeBlocks();
		bBM.isTight();
		System.out.println("This a visualization of the buffers: \n" + bBM.toString());
		bBM.printToFile("This a visualization of the buffers: \n" + bBM.toString());
		
		/**
		 * This test, tests multiple requests and proper combining of buddy buffers, it does so by adding
		 * different size buffers to the buffer manager and saving the addresses of the assigned buffers. It then
		 * shuffles the addresses of the assigned buffers and returns each address one at a time, the user should be able to see
		 * the buffers recombine with their buddy buffer. Each test result could result in different outcomes throughout
		 * the loop, but the overall outcome will be the same with 10 free 510 size buffers. This test ensures that everything is
		 * working properly. Running this test multiple times without the program crashing is confirmation that the program is running
		 * properly, bug free.
		 */
		System.out.println("Step 9 testing multiple requests: 5 size 6, 2 size 254, 2 size 126, 7 510 size\n" + 
				" Expected values:\n" + 
				" 1 510 size buffers,  0 254 size buffer, 1 126 size buffer,\n" + 
				" 1 62 size buffer, 0 30 size buffer, 1 14 size buffer 	and 1 6 size buffer, Status tight\n");
		bBM.printToFile("Step 9 testing multiple requests: 5 size 6, 2 size 254, 2 size 126, 7 510 size\n" + 
				" Expected values:\n" + 
				" 1 510 size buffers,  0 254 size buffer, 1 126 size buffer,\n" + 
				" 1 62 size buffer, 0 30 size buffer, 1 14 size buffer 	and 1 6 size buffer, Status tight\n");
		bufferSizes = new ArrayList<Integer>();
		bufferAddresses = new ArrayList<Integer>();
		
		for(int i = 0; i < 16; i++) {
			if(i < 7 ) {
				bufferSizes.add(510);
			} else if(i < 9) {
				bufferSizes.add(254);
			} else if(i < 10) {
				bufferSizes.add(126);
			} else if(i < 15){
				bufferSizes.add(6);
			} else {
				bufferSizes.add(126);
			}
		}
		
		for(Integer x : bufferSizes) {
			bufferAddress = bBM.getBlock(x);
			bufferAddresses.add(bufferAddress);
			System.out.println("Assigned Address for size " + x);
			bBM.printToFile("Assigned Address for size " + x);
			System.out.println("Actual = Assigned Address: " + bufferAddress + "\n");
			bBM.printToFile("Actual = Assigned Address: " + bufferAddress + "\n");
		}
		bBM.printFreeBlocks();
		bBM.isTight();
		System.out.println("This a visualization of the buffers: \n" + bBM.toString());
		bBM.printToFile("This a visualization of the buffers: \n" + bBM.toString());
		
		
		System.out.println("Return buffers in a different order. We should see buddies eventually combine\n");
		bBM.printToFile("Return buffers in a different order. We should see buddies eventually combine\n");
		
		Collections.shuffle(bufferAddresses);
		for(Integer x : bufferAddresses) {
			System.out.println("Returning address " + x + " of size " + bBM.getSize(x) + "\n");
			bBM.printToFile("Returning address " + x + " of size " + bBM.getSize(x) + "\n");
			bBM.returnBlock(x);
			bBM.printFreeBlocks();
			bBM.isTight();
			System.out.println("This a visualization of the buffers: \n" + bBM.toString());
			bBM.printToFile("This a visualization of the buffers: \n" + bBM.toString());
		}
	}
}
