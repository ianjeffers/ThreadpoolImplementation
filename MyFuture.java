import java.util.concurrent.Callable;

public class MyFuture<V> {
	private V thingToReturn;
	private boolean doneCheck = false;
	private Callable MyCall;
	private Runnable MyRun;

	public MyFuture(Callable call) {
		this.MyCall = call;
	}

	public MyFuture(Runnable runner) {
		this.MyRun = runner;
	}

	public V get() throws MyExecutionException {
//inefficient
		while (!isDone()) {
			continue;
		}
		// return actual value here
		return thingToReturn;
	}

	public boolean isDone() {

		return doneCheck;

	}
	public Callable getCallable() {
		return MyCall;
	}
	public Runnable getRunnable() {
		return MyRun;
	}
	public void setDone() {
		doneCheck = true;
	}

	public void addResult(V x) {
		thingToReturn = x;
		setDone();
	}
	public void fowaiejfoiewjoi() {
		
	}
	public void f2389uf9832f(int b) {
		
	}
	public String strppppppddfd(){
		return "New";
	}
}
