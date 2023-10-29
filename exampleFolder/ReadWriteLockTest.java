package threads;

/**
 * ReadWriteLockTest.java Test program for understanding ReadWriteLock
 * 
 * @author www.codejava.net
 */
public class ReadWriteLockTest {
	static final int READER_SIZE = 100;
	static final int WRITER_SIZE = 50;

	public static void main(String[] args) {
		Integer[] initialElements = { 33, 28, 86, 99 };

		ReadWriteList<Integer> sharedList = new ReadWriteList<Integer>(initialElements);

		for (int i = 0; i < WRITER_SIZE; i++) {
			new Writer(sharedList).start();
		}

		for (int i = 0; i < READER_SIZE; i++) {
			new Reader(sharedList).start();
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sharedList.getAll());
		System.out.println(sharedList.size());
	}
}