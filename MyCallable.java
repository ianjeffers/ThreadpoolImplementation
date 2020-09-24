import java.util.concurrent.Callable;

public class MyCallable implements Callable {
	private int num;

	public MyCallable(int num) {
		this.num = num;
	}

	public Object call() throws Exception {
		System.out.print(num);
		return num;
	}
}
