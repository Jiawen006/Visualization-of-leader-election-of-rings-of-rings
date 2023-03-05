package Task2;

public class InterfaceNode extends Node {
	Ring ring;
	public InterfaceNode(Ring ring) {
		super();
		this.ring = ring;
		this.setId(-1);
	}
	
	@Override
	public Ring getRing() {
		return ring;
	}
	
	@Override
	public void readId() {
		if (isAwake == true) {
			lastReadId = inId;
			inId = readId;
		} else {
			
		}
	}
}
