
public class Runner {
	public static void main(String[]args) throws Exception {
		ThreadPool pool = new ThreadPool();
		System.out.println("Runnable: ");
		for(int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
			pool.submit(new MyRunnable(i));
		}
		Thread.sleep(2000);
		System.out.println();
		System.out.println("Callable: ");
		for(int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
			pool.submit(new MyCallable(i));
		}
	}
}
