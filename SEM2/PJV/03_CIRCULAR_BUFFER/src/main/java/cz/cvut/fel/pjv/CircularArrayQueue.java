package cz.cvut.fel.pjv;

/**
 * Implementation of the {@link Queue} backed by fixed size array.
 */
public class CircularArrayQueue implements Queue {

    private final String[] data;
    private int size;
    private int read;
    private int write;
    
    /**
     * Creates the queue with capacity set to the value of 5.
     */
    public CircularArrayQueue() {
        data = new String[5];
        size = read = write = 0;
    }
    
    /**
     * Creates the queue with given {@code capacity}. The capacity represents maximal number of elements that the
     * queue is able to store.
     * @param capacity of the queue
     */
    public CircularArrayQueue(int capacity) {
        data = new String[capacity];
        size = read = write = 0;
    }

    
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean isFull() {
        return size == data.length;
    }

    @Override
    public boolean enqueue(String obj) {
        if (isFull()) {
            return false;
        }
        
        data[write] = obj;
        write = (write + 1) % data.length;
        size++;
        
        return true;
    }

    @Override
    public String dequeue() {
        if (isEmpty()) {
            return null;
        }
        
        String ret = data[read];
        read = (read + 1) % data.length;
        size--;
        
        return ret;
    }

    @Override
    public void printAllElements() {
        for (int i = 0; i <= size; i++) {
            System.out.println(data[i]);
        }
    }
}
