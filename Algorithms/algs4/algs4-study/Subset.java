/**
 * Subset
 *
 * @author Igor Elkin
 */
public class Subset {

    private static RandomizedQueue<String> queue = new RandomizedQueue<String>();

    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }
        for (int i = 0; i < k; i++) {
              StdOut.println(queue.dequeue());
        }
    }
}
