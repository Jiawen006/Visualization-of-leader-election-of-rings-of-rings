package Task2;

public class Node {
	protected int id;
	protected int readId = 0;
	protected int inId = 0;
	protected boolean isAwake = false;
	protected boolean preAwake = false;
	protected boolean nextAwake = false;
	protected boolean sendItself = false;
	// 0 denote unknown, 1 denote not leader,
	// 2 denote is leader
	protected int isLeader = 0;
	// this one store the leader id in the ring
	public int leaderId = 0;
	protected Node next;
	protected Node previous;

	public Node() {

	}

	// this is non interface node in the main ring
	public Node(int id) {
		this.id = id;
	}

	protected boolean sendMessage(int sendId, SystemCounter counter) {
		// we only implement one direction here, dual direction send will be designed
		// later on.
		if (nextAwake == true) {
			// only transfer when the next node is awake
			next.setReadId(sendId);
			counter.addCounter();
			return true;
		} else {
			// do nothing
			return false;
		}
	}

	protected int lastReadId = 0;

	public void readId() {
		if (isAwake == true) {
			lastReadId = inId;
			inId = readId;
		} else {
			// did not wake, do nothing
		}
	}

	public boolean process(SystemCounter counter) {
		if (isAwake != true) {
			checkAwake(counter);
			return false;
		} else {
			// the processor is awake
			if (sendItself == false) {
				if (inId == 0) {
					sendItself = sendMessage(id, counter);
					return false;
				} else {
					int sendId = (int) (Math.max(inId, id));
					sendItself = sendMessage(sendId, counter);
					isLeader = (sendId == id) ? 0 : 1;
					return false;
				}
			} else {
				if (inId != 0) {
					if (id < inId) {
						if (lastReadId != inId) {
							// node is not the leader and send the larger id to next node
							isLeader = 1;
							sendMessage(inId, counter);
							return false;
						} else {
							// do nothing
							return false;
						}

					} else if (id == inId) {
						// this node is the leader
						isLeader = 2;
						leaderId = id;
						broadcastLeader(id, counter);
						return true;
					} else {
						return false;
						// do nothing
					}
				} else {
					return false;
					// do nothing
				}
			}
		}

	}

	protected void broadcastLeader(int leaderId, SystemCounter counter) {
		Node preNode = this.previous;
		Node nextNode = this.next;
		while (preNode.leaderId == 0 || nextNode.leaderId == 0) {
			preNode.leaderId = leaderId;
			nextNode.leaderId = leaderId;
			preNode = preNode.previous;
			nextNode = nextNode.next;
			counter.addExtraMessage();
			counter.addExtraMessage();
			counter.addExtraRound();
		}
		System.out.println("Leader selected. Leader ID is " + id);
	}

	protected void checkAwake(SystemCounter counter) {
//		calculate whether this round will awake
		isAwake = (id == -1) ? false : true;
		if (isAwake == true) {
//			broadccast to the neighbour that it is awake
			broadcastAwake(counter);
			// add two messages to counter
		} else {
//			do nothing
		}
	}

	public void broadcastAwake(SystemCounter counter) {
		this.previous.setnextAwake();
		this.next.setpreAwake();
		if (this.previous == this.next) {
			counter.addCounter();
		} else {
			counter.addCounter();
			counter.addCounter();
		}

	}

//	getter and setter
	public int getId() {
		return id;
	}

	public boolean getisAwake() {
		return isAwake;
	}

	public void setpreAwake() {
		preAwake = true;
	}

	public void setnextAwake() {
		nextAwake = true;
	}

	public int getisLeader() {
		return isLeader;
	}

	public void setReadId(int readId) {
		this.readId = readId;
	}

	public int getReadId() {
		return readId;
	}

	public void setInId(int inId) {
		this.inId = inId;
	}

	public int getInId() {
		return inId;
	}

	public boolean isAwake() {
		return isAwake;
	}

	public void setAwake(boolean isAwake) {
		this.isAwake = isAwake;
	}

	public boolean isPreAwake() {
		return preAwake;
	}

	public void setPreAwake(boolean preAwake) {
		this.preAwake = preAwake;
	}

	public boolean isNextAwake() {
		return nextAwake;
	}

	public void setNextAwake(boolean nextAwake) {
		this.nextAwake = nextAwake;
	}

	public boolean isSendItself() {
		return sendItself;
	}

	public void setSendItself(boolean sendItself) {
		this.sendItself = sendItself;
	}

	public int getIsLeader() {
		return isLeader;
	}

	public void setIsLeader(int isLeader) {
		this.isLeader = isLeader;
	}

	public int getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(int leaderId) {
		this.leaderId = leaderId;
	}

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}

	public Node getPrevious() {
		return previous;
	}

	public void setPrevious(Node previous) {
		this.previous = previous;
	}

	public int getLastReadId() {
		return lastReadId;
	}

	public void setLastReadId(int lastReadId) {
		this.lastReadId = lastReadId;
	}

	public void setLeader(int leaderId) {
		this.leaderId = leaderId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Ring getRing() {
		return null;
	}
}
