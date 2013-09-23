/**
 * MaxPQTest
 *
 * @author Игорь Елькин (ielkin@nvision-group.com)
 */
public class MaxPQTest {

    @org.junit.Test
    public void testInsert() throws Exception {
        MaxPQ<Integer> pq = new MaxPQ<Integer>(new Integer[] {88, 86, 55, 85, 84, 46, 23, 31, 53, 16});
        pq.insert(11);
        pq.insert(63);
        pq.insert(32);
        for (Integer integer : pq) {
            System.out.print(integer + " ");
        }
    }

    @org.junit.Test
    public void testDelete() throws Exception {
        MaxPQ<Integer> pq = new MaxPQ<Integer>(new Integer[] {74, 71, 41, 57, 60, 13, 24, 12, 51, 45});
        pq.delMax();
        pq.delMax();
        pq.delMax();
        for (Integer integer : pq) {
            System.out.print(integer + " ");
        }
    }

}
