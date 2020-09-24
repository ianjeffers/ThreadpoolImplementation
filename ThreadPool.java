
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;

public class ThreadPool<T> {
	private int n;
	private Thread[] threads;
	private LinkedList<MyFuture> tasks; // Queue
	private Mutex lock;
	private ReentrantLock taskLock = new ReentrantLock();
	
	private int taskCount;

	public ThreadPool() {
		this((Runtime.getRuntime().availableProcessors()) - 1);
	}

	public ThreadPool(int n) {
		this.lock = new Mutex();
		lock.lock();
		this.n = n;
		threads = new Thread[n];
		tasks = new LinkedList<>();

		for (int i = 0; i < threads.length; i++) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					while (true) {
						lock.lock();
						taskLock.lock();
						MyFuture task = tasks.removeFirst();
						
						if (!tasks.isEmpty()) {
							//Change here V
							lock.unlock();
						}
						taskLock.unlock();
						if (task.getCallable() instanceof Callable) {
							try {
								Object x = task.getCallable().call();
								((MyFuture<T>) task).addResult((T)x);
								taskLock.lock();
								taskCount++;
								taskLock.unlock();
								// callable is done
							} catch (MyExecutionException e) {
								e.printStackTrace();
								Logger.getLogger(ThreadPool.class.getName()).log(Level.SEVERE, null, e);
							} catch (Exception e) {
								e.printStackTrace();
								Logger.getLogger(ThreadPool.class.getName()).log(Level.SEVERE, null, e);
							}
						} else if (task.getRunnable() instanceof Runnable) {
							task.getRunnable().run();
							((MyFuture<T>) task).addResult(null);
							taskLock.lock();
							taskCount++;
							taskLock.unlock();
							// runnable is done
						}

					}
				}
			});
			threads[i] = t;
			threads[i].start();
		}
	}

	public <T> MyFuture<T> submit(Runnable runnable) {
		MyFuture<T> future = new MyFuture<>(runnable);
		taskLock.lock();
		tasks.add(future);
		taskLock.unlock();
		lock.unlock();
		return future;
	}

	public <T> MyFuture<T> submit(Callable<T> callable) {
		MyFuture<T> future = new MyFuture<>(callable);
		taskLock.lock();
		tasks.add(future);
		taskLock.unlock();
		lock.unlock();
		return future;

	}

	void shutdown() throws MyExecutionException {
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (Exception e) {
				Logger.getLogger(ThreadPool.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	}

	public int getTaskCount() {
		return taskCount;
	}

	public void waitForThreads(Thread[] threads) {
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException ex) {
				Logger.getLogger(ThreadPool.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}