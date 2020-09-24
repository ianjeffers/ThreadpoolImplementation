
public class MyRunnable implements Runnable{
	private int num;
	
	public MyRunnable(int num){
		this.num = num;
	}
	public void run() {
		System.out.print(num);
//		System.out.flush();
	}

}
