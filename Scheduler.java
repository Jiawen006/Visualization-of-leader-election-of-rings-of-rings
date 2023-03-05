package Task2;

public class Scheduler {
	Ring ring;
	SystemCounter counter;
	private Node head = null;
	private Node tail = null;
	public Scheduler(Ring input) {
		this.ring = input;
		head = ring.getHead();
		tail = ring.getTail();
		counter = new SystemCounter();
	}
	public void LCR() {
		selectLeader();
		printResult();
	}
	
	private void printResult() {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("Round: "+ (counter.getRoundCounter()));
		System.out.println("Message Sent: " + (counter.getMessage()));
		System.out.println("Termination extra round: "+ counter.getExtraRound());
		System.out.println("Termination extra message: " + counter.getExtraMessage());
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("Total Round: " + ((counter.getRoundCounter())+ counter.getExtraRound()));
		System.out.println("Total Message Sent: "+ (counter.getMessage()+ counter.getExtraMessage()));
	}
	private void selectLeader() {
		boolean terminate = false;
		while (terminate == false) {
			counter.addRound();
			readAllId();
			terminate = updateAllId();
		}
		
	}
	
	private void readAllId() {
		Node current = head;
		do {
			int current_id = current.getId();
			if (current_id !=-1) {
				// this is non interface node
				current.readId();
			}else {
				// this is interface node that have not participated in the main ring
				SubringNode sub_current = (SubringNode) current.getRing().getHead();
				do {
					sub_current.readId();
					sub_current = (SubringNode) sub_current.next;
				}while(sub_current != current.getRing().getHead());
			}
			current = current.next;
		}while (current != head);
	}
	
	private boolean updateAllId(){
		Node current = head;
		boolean terminate = false;
		do {
			if (current.getId() != -1) {
				// this is not an interface node
				terminate= current.process(this.counter);
			}else {
				SubringNode sub_current = (SubringNode) current.getRing().getHead();
				boolean findLeader = false;
				do {
					findLeader = sub_current.process(this.counter);
					sub_current = (SubringNode) sub_current.next;
				}while(sub_current != current.getRing().getHead() && findLeader==false);
			}
			
			current = current.next;
			counter.setLeaderFlag(terminate);
		}while(current!=head && terminate == false);
		return terminate;
	}
	
	public void LCR(UI visualize) {
		selectLeader(visualize);
		printResult();
	}
	
	private void selectLeader(UI visualize) {
		boolean terminate = false;
		while (terminate == false) {
			counter.addRound();
			readAllId(visualize);
			terminate = updateAllId(visualize);
		}
	}
	
	private void readAllId(UI visualize) {
		Node current = head;
		do {
			int current_id = current.getId();
			if (current_id !=-1) {
				// this is non interface node
				current.readId();
			}else {
				// this is interface node that have not participated in the main ring
				SubringNode sub_current = (SubringNode) current.getRing().getHead();
				do {
					sub_current.readId();
					sub_current = (SubringNode) sub_current.next;
				}while(sub_current != current.getRing().getHead());
			}
			current = current.next;
		}while (current != head);
		
		visualize.updateInId(this.head);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
		try {
			Thread.sleep(1000);
		}catch(InterruptedException ex){
			Thread.currentThread().interrupt();
		}
	}

	
	private boolean updateAllId(UI visualize){
		Node current = head;
		boolean terminate = false;
		do {
			if (current.getId() != -1) {
				// this is not an interface node
				terminate= current.process(this.counter);
			}else {
				SubringNode sub_current = (SubringNode) current.getRing().getHead();
				boolean findLeader = false;
				do {
					findLeader = sub_current.process(this.counter);
					sub_current = (SubringNode) sub_current.next;
				}while(sub_current != current.getRing().getHead() && findLeader==false);
			}
			
			current = current.next;
			counter.setLeaderFlag(terminate);
		}while(current!=head && terminate == false);
		
		visualize.updateInfo(this.head,this.counter);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
		try {
			Thread.sleep(1000);
		}catch(InterruptedException ex){
			Thread.currentThread().interrupt();
		}
		return terminate;
	}
	
	
	public SystemCounter getSystemCounter() {
		return this.counter;
	}
}
