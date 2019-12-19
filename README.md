# Buddy-Buffer
This program creates a BuddyBufferManager class that manages buffers with a buddy system, where buddy buffers can be split to make room for incoming blocks and recombine when returning blocks. Each memory block is a buffer of size maxBlockSize, these buffers cannot be combined. All buffers contains blocks that are size of powers of 2 which contains 2 control words, the first indicating its size and the second indicating its assigned address. Any block from a buffer that has been assigned will have a value of 1 following its control words up until you reach the next control words of the next block or the end of the buffer.
