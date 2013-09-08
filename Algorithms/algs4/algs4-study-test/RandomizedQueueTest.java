import org.junit.*;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * RandomizedQueueTest
 *
 * @author Igor Elkin
 */
public class RandomizedQueueTest {

    @Test
    public void testEmpty() throws Exception {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
    }


    @Test
    public void testNotEmpty() throws Exception {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        queue.enqueue("A");
        assertEquals(1, queue.size());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueueDequeue() throws Exception {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        queue.enqueue("A");
        String item = queue.dequeue();
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
        assertEquals("A", item);
    }

    @Test
    public void testSample() throws Exception {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        queue.enqueue("A");
        String item = queue.sample();
        assertEquals(1, queue.size());
        assertFalse(queue.isEmpty());
        assertEquals("A", item);
    }

    @Test
    public void testMultipleEnqueueDequeue() throws Exception {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        for (int i = 0; i < 10; i++) {
            queue.enqueue(String.valueOf(i));
        }
        Set results = new HashSet<String>();
        for (int i = 0; i < 10; i++) {
            String e = queue.dequeue();
            System.out.println(e);
            results.add(e);
        }

        for (int i = 0; i < 10; i++) {
            assertTrue(results.contains(String.valueOf(i)));
        }
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testIterator() throws Exception {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        for (int i = 0; i < 10; i++) {
            queue.enqueue(String.valueOf(i));
        }
        Set results1 = new HashSet<String>();
        for (String s : queue) {
            System.out.println(s);
            results1.add(s);
        }

        Set results2 = new HashSet<String>();
        for (String s : queue) {
            System.out.println(s);
            results2.add(s);
        }

        assertTrue(results1.containsAll(results2));
        for (int i = 0; i < 10; i++) {
            assertTrue(results1.contains(String.valueOf(i)));
        }
        assertEquals(10, queue.size());
    }
}
