
public class Responder implements Runnable {

	RoutingTable routingTable;
	
	public Responder(RoutingTable routingTable) {
		this.routingTable = routingTable;
	}
	
	@Override
	public void run() {
		System.out.println("In Responder");
		System.out.println("Do something here");
		
		// 
	}
	

}
