import org.junit.*;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * DequeTest
 *
 * @author Igor Elkin
 */
public class DequeTest {

    @org.junit.Test
    public void testEmpty() throws Exception {
        Deque<String> deque = new Deque<String>();
        assertTrue(deque.isEmpty());
    }

    @org.junit.Test
    public void testAddFirst() throws Exception {
        Deque<String> deque = new Deque<String>();
        deque.addFirst("first");
        assertFalse(deque.isEmpty());
        assertEquals(1, deque.size());
    }

    @org.junit.Test
    public void testAddFirstRemoveFirst() throws Exception {
        Deque<String> deque = new Deque<String>();
        deque.addFirst("first");
        String item = deque.removeFirst();
        assertTrue(deque.isEmpty());
        assertEquals(0, deque.size());
        assertEquals("first", item);
    }

    @org.junit.Test
    public void testAddFirstRemoveLast() throws Exception {
        Deque<String> deque = new Deque<String>();
        deque.addFirst("first");
        String item = deque.removeLast();
        assertTrue(deque.isEmpty());
        assertEquals(0, deque.size());
        assertEquals("first", item);
    }

    @org.junit.Test
    public void testAddLast() throws Exception {
        Deque<String> deque = new Deque<String>();
        deque.addLast("first");
        assertFalse(deque.isEmpty());
        assertEquals(1, deque.size());
    }

    @org.junit.Test
    public void testAddLastRemoveLast() throws Exception {
        Deque<String> deque = new Deque<String>();
        deque.addLast("first");
        String item = deque.removeLast();
        assertTrue(deque.isEmpty());
        assertEquals(0, deque.size());
        assertEquals("first", item);
    }

    @org.junit.Test
    public void testAddLastRemoveFirst() throws Exception {
        Deque<String> deque = new Deque<String>();
        deque.addLast("first");
        String item = deque.removeFirst();
        assertTrue(deque.isEmpty());
        assertEquals(0, deque.size());
        assertEquals("first", item);
    }

    @org.junit.Test
    public void testAddRemoveLastMore() throws Exception {
        Deque<String> deque = new Deque<String>();
        deque.addFirst("first");
        deque.addLast("last");
        String item = deque.removeLast();
        assertTrue(!deque.isEmpty());
        assertEquals(1, deque.size());
        assertEquals("last", item);
        item = deque.removeLast();
        assertTrue(deque.isEmpty());
        assertEquals(0, deque.size());
        assertEquals("first", item);
    }

    @org.junit.Test
    public void testAddRemoveFirstMore() throws Exception {
        Deque<String> deque = new Deque<String>();
        deque.addFirst("first");
        deque.addLast("last");
        String item = deque.removeFirst();
        assertTrue(!deque.isEmpty());
        assertEquals(1, deque.size());
        assertEquals("first", item);
        item = deque.removeFirst();
        assertTrue(deque.isEmpty());
        assertEquals(0, deque.size());
        assertEquals("last", item);
    }

    @org.junit.Test
    public void testIterator() throws Exception {
        Deque<String> deque = new Deque<String>();
        deque.addLast("1");
        deque.addLast("2");
        deque.addLast("3");
        deque.addLast("4");
        int i = 1;
        for (String s : deque) {
            assertEquals(String.valueOf(i), s);
            i++;
        }
        assertFalse(deque.isEmpty());
        assertEquals(4, deque.size());
    }

    @Test(expected = NoSuchElementException.class)
    public void testEmptyIterator() throws Exception {
        Deque<String> deque = new Deque<String>();
        Iterator it = deque.iterator();
        assertFalse(it.hasNext());
        it.next();
    }
}

