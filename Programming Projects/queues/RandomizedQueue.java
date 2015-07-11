/***************************
 * Algorithms, Part I, Programming Assignment 2
 * RandomizedQueue
 * Jianfei Hua 2/17/2014
 ****************************/
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int N = 0;
    private int first = 0;
    private int last = 0;
    private Item[] q;
    // construct an empty randomized queue
    public RandomizedQueue() {
        q = (Item[]) new Object[2];
    }
    // is the queue empty?    
    public boolean isEmpty() {
        return N == 0;
    }
    // return the number of items on the queue
    public int size() {
        return N;
    }
    // resize array q
    private void resize(int M) {
        Item[] qq = (Item[]) new Object[M];
        for (int i = 0; i < N; i++) {
            qq[i] = q[(first + i) % q.length];
        }
        q = qq;
        last = N;
        first = 0;
    }
    // add the item
    public void enqueue(Item item) {
        if (item == null) { 
            throw new NullPointerException("null enqueued");
        }
        if (N == q.length) resize(2*q.length);
        q[last++] = item;
        if (last == q.length) last = 0;
        N++;
    }
    // delete and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        int pick = StdRandom.uniform(N);
        int location = (first + pick) % q.length;
        Item item = q[location]; 
        q[location] = q[first];  // transfer the frist to the deleted
        q[first] = null;
        N--;
        first++;
        if (first == q.length) first = 0;
        if (N > 0 && N == q.length/4) resize(q.length/2); 
        return item;
    }
        
    // return (but do not delete) a random item
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        int pick = StdRandom.uniform(N);
        int location = (first + pick) % q.length;
        Item item = q[location]; 
        return item;
    }
    
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RQIterator();
    }
    
    private class RQIterator implements Iterator<Item> {
        private int i = 0;
        private int[] arrange = new int[N];
        private RQIterator() {
            for (int j = 0; j < N; j++) {
                arrange[j] = j;
            }
            StdRandom.shuffle(arrange);
        }
        public boolean hasNext() { return i < N; }
        public void remove() { throw new UnsupportedOperationException("No remove()"); }
        public Item next() {
             if (!hasNext()) throw new NoSuchElementException();
             Item item = q[(first + arrange[i]) % q.length];
             i++;
             return item;
        }
    }
    // unit testing
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            rq.enqueue(item);
        }
        for (String s : rq) {
            StdOut.println(s);
        }
        StdOut.println("RandomizedQueue size = " + rq.size());
        
        while (!rq.isEmpty()) {
            StdOut.println(rq.dequeue());
        }
        StdOut.println("RandomizedQueue size = " + rq.size());
    }
}

