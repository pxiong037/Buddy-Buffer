package Assignment7;
/**
 * Prechar Xiong
 * 12/10/19
 * ICS 462-01
 * Assignment 7
 * 
 * This program creates a BuddyBufferManager class that manages buffers with 
 * a buddy system, where buddy buffers can be split to make room for incoming
 * blocks and recombine when returning blocks. Each memory block is a buffer
 * of size maxBlockSize, these buffers cannot be combined. All buffers contains blocks that are size of powers of 2
 * which contains 2 control words, the first indicating its size and the second indicating its assigned address.
 * Any block from a buffer that has been assigned will have a value of 1 following its control words up until
 * you reach the next control words of the next block or the end of the buffer.
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class BuddyBufferManager {
	int maxBlockSize; //this  int stores the maximum block size of a buffer
	int maxMemoryBlocks; //this int stores the maximum number of memory blocks
	int minBlockSize = 8; //this int stores the minimum block size
	ArrayList<ArrayList<Integer>> bufferBlocks; //this ArrayList of ArrayList of Integers stores all of the buffer Blocks
	HashMap<Integer,Integer> freeBlocks; //this HashMap of Integers stores all of the free buffer blocks. (The block size are the keys and the free quantity of those blocks are the values)
	ArrayList<Integer> blockSizes; //this ArrayList of Integers stores all of the possible block sizes
	HashMap<Integer,Integer> addressPairs; //this HashMap of Integers stores all of the buddy buffers that can combine. (The address of where the split occurred is the key and the original address is the value)
	
	/**
	 * This is the constructor for the buddy buffer manager
	 * @param maxBlockSize is the maximum block size of a buffer
	 * @param maxMemoryBlocks is the maximum number of memory blocks
	 */
	public BuddyBufferManager(int maxBlockSize, int maxMemoryBlocks) {
		this.maxBlockSize = maxBlockSize;
		this.maxMemoryBlocks = maxMemoryBlocks;
		bufferBlocks = new ArrayList<ArrayList<Integer>>();
		addressPairs = new HashMap<Integer,Integer>();
		initialize();
		freeBlocks();
		createFile();
	}
	
	/**
	 * This method initializes all of the buddy buffer blocks
	 */
	public void initialize() {
		ArrayList<Integer> blocks;
		
		for(int i = 0; i < maxMemoryBlocks; i++) {
			blocks = new ArrayList<Integer>();
			for(int j = 0; j < maxBlockSize; j++) {
				if(j == 0) {
					blocks.add(maxBlockSize-2);
				} else if( j == 1) {
					blocks.add((maxBlockSize*i)+2);
				} else {
					blocks.add(null);
				}
			}
			bufferBlocks.add(blocks);
		}
	}
	
	/**
	 * this method allows you to get the log of a number with a base other than 10
	 * @param x is the number you want to find the log of
	 * @param base is the base of the log
	 * @return the x log base of the parameter base
	 */
	public int log(int x, int base) {
		return (int) (Math.log(x) / Math.log(base));
	}
	
	/**
	 * this method gets the block index of the address
	 * @param address
	 * @return the block index of the address
	 */
	public int getBlockIndex(int address) {
		int blockIndex = address%maxBlockSize;
		return blockIndex;
	}
	
	/**
	 * this method gets the memory block index of the address
	 * @param address
	 * @return the memory block index of the address
	 */
	public int getMemoryBlockIndex(int address) {
		int memoryBlockIndex = address/maxBlockSize;
		return memoryBlockIndex;
	}
	
	/**
	 * this method gets the size associated with the address
	 * @param address
	 * @return the size associated with the address
	 */
	public int getSize(int address) {
		int memoryBlockIndex = getMemoryBlockIndex(address);
		int blockIndex = getBlockIndex(address);
		int size = bufferBlocks.get(memoryBlockIndex).get(blockIndex-2);
		return size;
	}
	
	/**
	 * this method checks to see if the address entered is currently used
	 * @param address 
	 * @return it returns false if the address is not in use and true if it is in use
	 */
	public boolean isAssigned(int address) {
		boolean assigned = false;
		int memoryBlockIndex = getMemoryBlockIndex(address);
		int blockIndex = getBlockIndex(address);
		int assignedValue = bufferBlocks.get(memoryBlockIndex).get(blockIndex);
		if(equals(assignedValue,1)) {
			assigned = true;
		}
		return assigned;
	}
	
	/**
	 * this method allocates the blocks in an address to mark it up as used
	 * @param address, the address to be allocated
	 */
	public void allocateBlocks(int address) {
		int memoryBlockIndex = getMemoryBlockIndex(address);
		int blockIndex = getBlockIndex(address);
		int size = getSize(address);
		int i = 0;
		
		while(i < size){
			bufferBlocks.get(memoryBlockIndex).set(blockIndex+i, 1);
			i++;
		}
	}
	
	/**
	 * this method deallocates the blocks in an address to mark it up as unused
	 * @param address, the address to be deallocated
	 */
	public void deallocateBlocks(int address) {
		int memoryBlockIndex = getMemoryBlockIndex(address);
		int blockIndex = getBlockIndex(address);
		int size = getSize(address);
		int i = 0;
		
		while(i < size){
			bufferBlocks.get(memoryBlockIndex).set(blockIndex+i, null);
			i++;
		}
	}
	
	/**
	 * this method takes in a block size and tries to fit it into the smallest
	 * possible block size in the buddy buffer.
	 * @param blockSize, the block size to be added into the buddy buffer
	 * @return -2 if the block size is too big, -1 if there's not enough space to fit 
	 * the block size into the buddy buffer because all available blocks that could fit
	 * the block size is taken, and any address from 2 to the (maxBlockSize*maxMemoryBlocks-minimumBlockSize+2)
	 */
	public int getBlock(int blockSize) {
		//if the block size requested is bigger than the maxBlockSize-2 then return -2
		if(blockSize > maxBlockSize - 2) {
			System.out.println(blockSize + " exceeds the max block size and therefore cannot be stored into the buffer.\n");
			printToFile(blockSize + " exceeds the max block size and therefore cannot be stored into the buffer.\n");
			return -2;
		}
		
		int min = log(minBlockSize,2);
		int max = log(maxBlockSize,2);
		
		//find the minimum block size that can fit the requested block size
		for (int i = min; i <= max; i++) {
			int temp = (int)Math.pow(2,i)-2;
			if (blockSize <= temp){
				blockSize = temp;
				break;
			}
		}
		
		//address is set to -1 so if the buffer does not find an available spot for the requested buffer -1 will be returned
		int address = -1;
		
		/**
		 * This loop starts at the last memory block and attempts to find the first available buffer block for the requested block.
		 * When it finds a buffer block that is big enough to fit the requested block it divides buffer block into the smallest possible 
		 * block size that can fit the requested buffer. If a block size that can fit it is found, it allocates it and returns the address
		 * of where the requested block was assigned.
		 */
		for(int i = maxMemoryBlocks-1; i >= 0; i--) {
			for(int j = 0; j < maxBlockSize-2; j++) {
				Integer size = bufferBlocks.get(i).get(j);
				Integer tempAddress = bufferBlocks.get(i).get(j+1);
				Integer assigned = bufferBlocks.get(i).get(j+2);
				if(size != null && tempAddress != null && assigned == null) {
					if(blockSize == size) {
						address = tempAddress;
						allocateBlocks(address);
						return address;
					} else{
						while(size > blockSize) {
							bufferBlocks.get(i).set(j, size/2-1);
							bufferBlocks.get(i).set((j+(size/2)+1), size/2-1);
							bufferBlocks.get(i).set((j+(size/2)+2), tempAddress+size/2+1);
							addressPairs.put(tempAddress+size/2+1, tempAddress);
							if(blockSize == bufferBlocks.get(i).get(j)) {
								address = tempAddress + blockSize + 2;
								allocateBlocks(address);
								return address;
							}
							size = bufferBlocks.get(i).get(j);
						}
					}
				}
			}
		}	
		
		/**
		 * if no such buffer block was found that could fit the requested block it returns the 
		 * initial -1 value to indicate that the buffer manager does not have sufficient space 
		 * for the requested block
		 */
		System.out.println("There's not enough space in the buffer to store another " + blockSize + " size block\n");
		printToFile("There's not enough space in the buffer to store another " + blockSize + " size block\n");
		return address;
	}
	
	/**
	 * this method returns the block at the address entered into the buddy buffer
	 * and deallocates it then combines any buddy buffers it can
	 * @param address to be returned and deallocated
	 */
	public void returnBlock(int address) {
		/**
		 * if the address is less than 0 or greater than or equal 
		 * to the maxBlockSize*maxMemoryBlocks then inform the user
		 * and exit the method
		 */
		if(address < 0 || maxBlockSize*maxMemoryBlocks <= address) {
			System.out.println("The address " + address + " does not exist in the buffer (Addresses Contained in Buffer: 0 - " + (maxBlockSize*maxMemoryBlocks-1) + ")\n");
			printToFile("The address " + address + " does not exist in the buffer (Addresses Contained in Buffer: 0 - " + (maxBlockSize*maxMemoryBlocks-1) + ")\n");
			return;
		} 
		
		/**
		 * if the address is equal to 0 or 1 it is not valid because 
		 * those are reserved for the control words; inform the user
		 * and exit the method
		 */
		if(equals(address,0) || equals(address,1)){
			System.out.println("The address " + address + " cannot be removed because it holds the size or index of a block. "
					+ "(Please enter the actual address that the bufferBlock is assigned to.)\n");
			printToFile("The address " + address + " cannot be removed because it holds the size or index of a block. "
					+ "(Please enter the actual address that the bufferBlock is assigned to.)\n");
			return;
		} 
		
		int memoryBlockIndex = getMemoryBlockIndex(address);
		int size = getSize(address);
		boolean assigned = isAssigned(address);
		

		 
		//if the address is not assigned, inform the user and exit the method
		if(!assigned){ 
			System.out.println("Cannot return address " + address + " because there is currently no block assigned there.\n");
			printToFile("Cannot return address " + address + " because there is currently no block assigned there.\n");
			return;
		} else if(blockSizes.contains(size)){ //if the size is a valid block size of base 2
			//if the size of the assigned address is the max size; deallocate it and exit the method
			if(size == maxBlockSize-2) {
				deallocateBlocks(address);
				return;
			} else {	
				//deallocated the address and recombine buddy buffers starting at index 0
				deallocateBlocks(address);
				combineFreeBlocks(memoryBlockIndex,0);
				return;
			}
		} else{ //inform the user that they must enter a valid address that is assigned
			System.out.println("The address "+ address + " is not the address of one of the assigned buffer blocks " 
					+ "(Please enter the actual address that the bufferBlock is assigned to.)\n");
			printToFile("The address "+ address + " is not the address of one of the assigned buffer blocks " 
					+ "(Please enter the actual address that the bufferBlock is assigned to.)\n");
			return;
		}
	}
	
	/**
	 * this method combines all free buddy buffers from a specific memoryBlockIndex using recursion
	 * @param memoryBlockIndex the specific memoryBlockIndex that will have its buddy buffers combined
	 * @param index to start combining
	 */
	public void combineFreeBlocks(int memoryBlockIndex, int index) {	
		//if the current index is greater than or equal to the maxBlockSize-1-minBlockSize then exit the method
		if(index >= maxBlockSize-1-minBlockSize){
			return;
		}
		
		//block1 is the block that will be checked to see if it can combine with block2
		Integer block1 = bufferBlocks.get(memoryBlockIndex).get(index);
		Integer block1Address = bufferBlocks.get(memoryBlockIndex).get(index+1);
		Integer assigned1 = bufferBlocks.get(memoryBlockIndex).get(index+2);
		
		//if the block at index is equal to the max possible index then exit the method
		if(block1 == maxBlockSize-2){
			return;
		} else if(index+block1+2 > maxBlockSize-1) { //if there is no next block then exit the method
			return;
		}
		
		//block2 is the block that may be able to combine with block1
		Integer block2 = bufferBlocks.get(memoryBlockIndex).get(index+block1+2);
		Integer block2Address = bufferBlocks.get(memoryBlockIndex).get(index+block1+2+1);
		Integer assigned2 = bufferBlocks.get(memoryBlockIndex).get(index+block1+2+2);
		

		//if block1 and block2 are equivalent and they are buddy buffers then continue
		if(equals(block1,block2) && addressPairs.get(block2Address).equals(block1Address)) {
			if(equals(assigned1,null) && equals(assigned2,null)){//if block1 is free and block2 is free then
				addressPairs.remove(block2Address);//remove the block2Address key from the address pairs
				bufferBlocks.get(memoryBlockIndex).set(index,(block1+block2+2));//set block1 to equal the newly combine block of block1 and block2
				deallocateBlocks(block1Address);//deallocate all blocks within the new block1
				freeBlocks();//update the free blocks list
				combineFreeBlocks(memoryBlockIndex,0);//combine all possible free blocks within the current memory block index starting at index 0
			} else{//if block1 or block2 are assigned then jump to block3 and attempt to combine it with block4 and so on
				combineFreeBlocks(memoryBlockIndex,((index+(block1+2)+(block2+2))));
			}
		} else { //if block1 and block2 are not equivalent and are not buddy buffers then move on to block2 and attempt to combine with block3 and so on
			combineFreeBlocks(memoryBlockIndex,(index+block1+2));
		}
	}
	
	/**
	 * this method prints OK if the buddy buffer has more than 2 max block size buffers and
	 * Tight if it has 2 or less max block size buffers.
	 */
	public void isTight() {
		int count = 0;//used to count the number of max sized blocks
		for(int i = 0; i < maxMemoryBlocks; i++) {//go through each memory block, if the block size is the maxBlockSize-2 and is not assigned then increment count
			if(equals(bufferBlocks.get(i).get(0),(maxBlockSize-2)) && equals(bufferBlocks.get(i).get(2),null)) {
				count++;
			}
		}
		
		if(count > 2) {//if count is greater than 2 then print the status as OK
			System.out.println("Status: \nOK\n");
			printToFile("Status: \nOK\n");
		} else {//if count is equal to or less than 2 then print the status as tight
			System.out.println("Status: \nTight\n");
			printToFile("Status: \nTight\n");
		}
	}
	
	/**
	 * This method initializes the freeblocks hash map and the block size array list
	 * to see which blocks are free and what size blocks are available in the buddy buffer
	 */
	public void freeBlocks() {
		freeBlocks = new HashMap<Integer,Integer>();
		blockSizes = new ArrayList<Integer>();

		int min = log(minBlockSize,2);
		int max = log(maxBlockSize,2);
		
		for (int i = min; i <= max; i++) {
			int temp = (int)Math.pow(2,i)-2;
			freeBlocks.put(temp,0);//assign all possible block sizes as keys to the free blocks hashmap
			blockSizes.add(temp);//add the possible block sizes to the block size array
		}
		
		for(int i = 0; i < maxMemoryBlocks; i++) {//memoryBlockIndex
			for(int j = 0; j < maxBlockSize; j++) {//blockIndex
				if(freeBlocks.containsKey(bufferBlocks.get(i).get(j)) && equals(bufferBlocks.get(i).get(j+2),null)) {//if the free blocks hashmap contains the size of the buffer blocks as its key and the buffer block is unassigned
					freeBlocks.put(bufferBlocks.get(i).get(j), ((freeBlocks.get(bufferBlocks.get(i).get(j))+1)));//increment the hashmap key of size by 1
				}
			}
		}
	}
	
	/**
	 * this method checks to see if two objects are equivalent 
	 * the isequals method properly compares two objects but is unable
	 * to compare any object with null
	 * the == operator can compare null objects with other objects
	 * but doesn't properly compare the values of objects
	 * the method tries to compare two objects with the equals method 
	 * if it fails it'll then use the == operator to compare the objects
	 * @param num1, first object to be compared
	 * @param num2, second object to be compared
	 * @return true if the two objects are equal and false if they are not
	 */
	public boolean equals(Integer num1, Integer num2){
		boolean isEqual; //whether num1 is equal to num2
		
		try{
			isEqual = (num1.equals(num2));//properly compares two objects but unable to compare null objects with other objects
		} catch(Exception e){
			isEqual = (num1 == num2);//does not properly compare two objects but is able to compare null objects with other objects
		}
		
		return isEqual;
	}
	
	/**
	 * This method converts a buddy buffer block into a string
	 * @param bufferBlock, the block to be converted into a string
	 * @return the buffer block as a string
	 */
	public String bufferBlockToString(Integer bufferBlock) {
		String s = "";
		if(equals(bufferBlock,null)) {
			s += "null";
		} else {
			s += bufferBlock;
		}
		return s;
	}
	
	/**
	 * This method prints all of the free blocks in the buddy buffer manager
	 */
	public void printFreeBlocks() {
		freeBlocks();
		System.out.println("Free Buffer Count: ");
		printToFile("Free Buffer Count: ");
		
		for(Integer blockSize: blockSizes) {
			System.out.println(freeBlocks.get(blockSize) + " " + blockSize + " size buffers");
			printToFile(freeBlocks.get(blockSize) + " " + blockSize + " size buffers");
		}
		
		System.out.println();
		printToFile("\n");
	}
	
	/**
	 * This method prints out all of the buddy buffer addresses. This method is great
	 * for testing purposes to see what buffers can be combined 
	 */
	public void printAddressPairs() {
		System.out.println(addressPairs.keySet());
		System.out.println(addressPairs.values());
		printToFile(addressPairs.keySet().toString());
		printToFile(addressPairs.values().toString());	
	}
	
	/**
	 * This method takes in a string and prints it to the output file
	 * @param msg is the String to be printed to the file
	 */
	public void printToFile(String msg) {
		try {
			File file = new File("Assignment7-Output.txt");
			FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println(msg);
			printWriter.close();
		}	catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This methods creates an output.txt file if it doesn't already exist, to output
	 * the numbers read by the consumer, output when the consumer waits and when the consumer
	 * finishes.
	 */
	public void createFile() {
		try {
			FileWriter fileWriter;
			File file = new File("Assignment7-Output.txt");
			PrintWriter printWriter;
			fileWriter = new FileWriter(file);
			printWriter = new PrintWriter(fileWriter);
			printWriter.println("Prechar Xiong \nICS 462 Assignment #7 \n12/10/19 \n \n");
			printWriter.close();
		}	catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method overrides the object toString method with its own. This toString method prints
	 * out the buffer manager so the user can visualize the buddy buffer. This method is awesome
	 * when testing and debugging the buffer manager.
	 */
	public String toString() {
		String s = "";
		s += "\n[";
		for(int i = 0; i < maxMemoryBlocks; i++) {
			s += "[";
			for(int j = 0; j < maxBlockSize; j++) {
				s += bufferBlockToString(bufferBlocks.get(i).get(j));
				if(j != maxBlockSize-1) {
					s += ",";
				}
			}
			s += "]";
			if(i != maxMemoryBlocks-1) {
				s += "\n";
			}
		}
		s += "]\n\n";
		return s;
	}
}
