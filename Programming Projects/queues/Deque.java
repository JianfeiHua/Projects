/* ********************
 * Algorithms, Part 1, Programming Assignments 2
 * Deque
 * Jianfei Hua 2/16/2014
 ***********************/
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int N;
    private Node first;
    private Node last;
    // a double linked list
    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }
    // construct an empty deque
    public Deque() {
        first = null;
        last  = null;
        N     = 0;
    }
    // is the deque empty?
    public boolean isEmpty() {
        return N == 0;
    }
    // return the number of items on the deque
    public int size() {
        return N;
    }
    // insert the item at the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException("null added");
        }
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.previous = null;
        if (isEmpty()) {
            first.next = null;
            last = first;
        }
        else {
            first.next = oldFirst;
            oldFirst.previous = first;
        }
        N++;
    }
    // insert the item at the end    
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException("null added");
        }
        if (isEmpty()) addFirst(item);
        else {
            Node oldLast = last;
            last = new Node();
            last.item = item;
            last.next = null;
            last.previous = oldLast;
            oldLast.next = last;
            N++;
        }
    }
    // delete and return the item at the front        
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = first.item;
        first = first.next;
        N--;
        if (!isEmpty()) first.previous = null;
        else last = null;
        return (item);
    } 
    // delete and return the item at the end
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = last.item;
        last = last.previous;
        N--;
        if (!isEmpty()) last.next = null;
        else first = null;
        return (item);
    }
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator();
    }
    
    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() { 
            return current != null;
        }
        public void remove() {
            throw new UnsupportedOperationException("No remove()");
        }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("Deque underflow");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    // unit testing    
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        int count = 0;
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (count % 2 == 0) deque.addFirst(item);
            else deque.addLast(item);
            count++;
        }
        for (String s : deque) {
            StdOut.println(s);
        }
        StdOut.println("deque size = " + deque.size());
        
        while (!deque.isEmpty()) {
            if (count % 2 == 0) StdOut.println(deque.removeFirst());
            else StdOut.println(deque.removeLast());
            count--;
        }
        StdOut.println("deque size = " + deque.size());
    }
}