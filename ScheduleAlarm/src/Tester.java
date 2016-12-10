public class Tester {
	public static void main(String[] args){
		TimePeriod p = new TimePeriod();
		p.start();
		try{
			Thread.sleep(3333);
		}catch(Exception e){
		}
		p.stop();
		System.out.println("before: " + p);
		System.out.println("after:  " + TimePeriod.fromString(p.toString()));
	}
}
