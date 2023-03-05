package Task2;

public class SubringNode extends Node {
	// each subring node should know its interface node
	Node interfaceNode;

	// subring node constructor
	public SubringNode(int id, Node interfaceNode) {
		super();
		this.setId(id);
		this.interfaceNode = interfaceNode;
		super.setAwake(true);
	}

	@Override
	public void readId() {
		this.inId = this.readId;
		this.readId = 0;
	}

	@Override
	protected boolean sendMessage(int sendId, SystemCounter counter) {
		next.readId = sendId;
		counter.addCounter();
		return true;
	}

	@Override
	public boolean process(SystemCounter counter) {
		// use the synchronous version of process

		if (this.sendItself == false) {
//					this is the first round for the node
			sendMessage(id, counter);
			this.sendItself = true;
		} else {
			if (id < inId) {
//						node is not the leader and send the larger id to next node
				this.isLeader = 1;
				sendMessage(inId, counter);
				this.inId = 0;
			} else if (id == inId) {
				// this is the leader in the subring, send back to the interface node
				this.isLeader = 2;
				this.interfaceNode.setId(id);
				return true;
			} else {
				// do nothing
			}
		}
		return false;
	}

	public int getisLeader() {
		return this.isLeader;
	}

}
