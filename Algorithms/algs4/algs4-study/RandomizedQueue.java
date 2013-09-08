import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * RandomizedQueue
 *
 * @author Igor Elkin
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;            // queue elements
    private int N = 0;           // number of elements on queue

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

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        // double size of array if necessary
        if (N == q.length) resize(2 * q.length);
        // add item
        q[N++] = item;
    }

    // delete and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();

        // select random element
        int randomIdx = StdRandom.uniform(N);
        Item item = q[randomIdx];
        // moving last item in the queue to the place of dequeued element
        q[randomIdx] = q[N - 1];
        // decrease queue from the end
        // to avoid loitering
        q[N - 1] = null;
        N--;
        // shrink size of array if necessary
        if (N > 0 && N == q.length / 4) resize(q.length / 2);
        return item;
    }

    // return (but do not delete) a random item
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        return q[StdRandom.uniform(N)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomArrayIterator();
    }


    // resize the underlying array
    private void resize(int max) {
        assert max >= N;
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < N; i++) {
            temp[i] = q[i];
        }
        q = temp;
    }

    private class RandomArrayIterator implements Iterator<Item> {
        private int next = 0;
        private Item[] randomData;

        private RandomArrayIterator() {
            // copy array data and shuffle it
            randomData = (Item[]) new Object[N];
            for (int i = 0; i < N; i++) {
                randomData[i] = q[i];
            }
            StdRandom.shuffle(randomData);
        }

        public boolean hasNext() {
            return next < N;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = randomData[next];
            next++;
            return item;
        }
    }

}