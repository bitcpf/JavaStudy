import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Deque implementation
 *
 * @author Igor Elkin
 */
public class Deque<Item> implements Iterable<Item> {
    // size of the queue
    private int N;
    // start of the queue
    private Node first;
    // end of the queue
    private Node last;

    // helper linked list class
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    // construct an empty deque
    public Deque() {
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
        Node newNode = createNewNode(item);
        if (isEmpty()) {
            last = newNode;
        } else {
            newNode.next = first;
            first.prev = newNode;
        }
        first = newNode;
        N++;
    }

    // insert the item at the end
    public void addLast(Item item) {
        Node newNode = createNewNode(item);
        if (isEmpty()) {
            first = newNode;
        } else {
            newNode.prev = last;
            last.next = newNode;
        }
        last = newNode;
        N++;
    }

    private Node createNewNode(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        Node newNode = new Node();
        newNode.item = item;
        return newNode;
    }

    // delete and return the item at the front
    public Item removeFirst() {
        checkNotEmpty();
        Item item = first.item;
        if (first == last) {
            first = null;
            last = null;
        } else {
            first.next.prev = null;
            first = first.next;
        }
        N--;
        return item;
    }

    // delete and return the item at the end
    public Item removeLast() {
        checkNotEmpty();
        Item item = last.item;
        if (first == last) {
            first = null;
            last = null;
        } else {
            last.prev.next = null;
            last = last.prev;
        }
        N--;
        return item;
    }

    private void checkNotEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Node next = first;

            public boolean hasNext() {
                return next != null;
            }

            public Item next() {
                if (next == null) {
                    throw new NoSuchElementException();
                }
                Item item = next.item;
                next = next.next;
                return item;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}