package ru.ivel.algo;

/**
 * QuickSelectTest
 * 
 * @author Игорь Елькин (ielkin@nvision-group.com)
 */
public class QuickSelectTest {

	@org.junit.Test
	public void testSelect() throws Exception {
		QuickSelect select = new QuickSelect();
		int[] a = { 5, 3, 1, 4, 6 };
        for (int i = 0; i < a.length; i++) {
            System.out.println(select.select(new int[] {5, 3, 1, 4, 6}, i));
        }
	}
}
