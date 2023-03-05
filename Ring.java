package Task2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;

public class Ring {
	private Node head = null;
	private Node tail = null;
	private InterfaceNode interfaceNode;

	// this is the main ring
	public Ring() {

	}

	// this is the subring
	public Ring(InterfaceNode interfaceNode) {
		this.interfaceNode = interfaceNode;
	}

//    this method add the non-interface node
	public void addNode(int id) {
		Node newNode = new Node(id);

		// if list is empty, head and tail points to newNode
		if (head == null) {
			head = tail = newNode;
			// head's previous will be null
			head.previous = tail;
			// tail's next will be null
			tail.next = head;
		} else {
			// add newNode to the end of list. tail->next set to newNode
			tail.next = newNode;
			// newNode->previous set to tail
			newNode.previous = tail;
			// newNode becomes new tail
			tail = newNode;
			// tail's next point to null
			tail.next = head;
			head.previous = tail;
		}
	}

	// this method add the interface node
	public void addNode(Node newNode) {
		// if list is empty, head and tail points to newNode
		if (head == null) {
			head = tail = newNode;
			// head's previous will be null
			head.previous = tail;
			// tail's next will be null
			tail.next = head;
		} else {
			// add newNode to the end of list. tail->next set to newNode
			tail.next = newNode;
			// newNode->previous set to tail
			newNode.previous = tail;
			// newNode becomes new tail
			tail = newNode;
			// tail's next point to null
			tail.next = head;
			head.previous = tail;
		}
	}

	public boolean containELe(int searchValue) {
		Node currentNode = head;
		if (head == null) {
			return false;
		} else {
			do {
				if (currentNode.getId() == searchValue) {
					return true;
				}
				currentNode = currentNode.next;
			} while (currentNode != head);
			return false;
		}
	}

	public Node findEle(int searchValue) {
		Node currentNode = head;
		if (head == null) {
			return null;
		} else {
			do {
				if (currentNode.getId() == searchValue) {
					return currentNode;
				}
				currentNode = currentNode.next;
			} while (currentNode != head);
			return null;
		}

	}

	public void display(String input) {
		Node current = head;
		if (head == null) {
			System.out.println("List is empty");
		} else {
			System.out.println(input + " format the circular linked list: ");
			do {
				// Prints each node by incrementing pointer.
				int id = current.getId();
				if (id != -1) {
					// this is not the interface node
					System.out.print(" " + id + "-->");
				} else {
					// this is the interface node
					System.out.print(" " + id + "( ");
					Node sub_current = current.getRing().getHead();
					do {
						System.out.print(sub_current.getId() + " ");
						sub_current = sub_current.next;
					} while (sub_current != current.getRing().getHead());
					System.out.print(") -->");
				}
				current = current.next;
			} while (current != head);
			System.out.println();
		}
	}

	public boolean initialize() {
		int totalSize = 0;
		int type = 0;
		int subringSize = 0;
		int random_options = 0;
		int visualOrConsole = 0;
		boolean visualize = false;
		ArrayList<Integer> listId = new ArrayList<Integer>();
		Scanner sc = new Scanner(System.in);
		// Step 0: visualize result or not
		System.out.println("Visualize result or Console Display?");
		System.out.println("Visualize will use the default structure while console display can constuct by your own.");
		System.out.println("1. Visualize\t2. Console Display");
		try {
			visualOrConsole = sc.nextInt();
		} catch (InputMismatchException e) {
			System.out.println(e.getMessage());
		}
		if (visualOrConsole > 2 || visualOrConsole <= 0) {
			throw new IllegalArgumentException("size should be in the range of 1 to 2");
		} else if (visualOrConsole == 1) {
			visualize = true;
		}
		if (visualize == false) {
			// for console version, ask user to input the size
			// Step 1: user inputs the size of the network
			System.out.println("Enter the total size of the network(1 to 100):");
			try {
				totalSize = sc.nextInt();
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
			}
			if (totalSize > 100 || totalSize <= 0) {
				throw new IllegalArgumentException("size should be in the range of 1 to 1100");
			}
		} else {
			totalSize = 13;
		}

		// Step 2: user inputs the type of the network
		System.out.println("Enter the type of the network: (ID sequence)");
		System.out.println("1. Random\t2.Ascending\t3.Descending");
		try {
			type = sc.nextInt();
		} catch (InputMismatchException e) {
			System.out.println(e.getMessage());
		}

		if (type > 3 || type <= 0) {
			throw new IllegalArgumentException("the number should be in the range of 1 to 3");
		} else if (type == 1) {
			listId = randomlist(totalSize);
		} else if (type == 2) {
			listId = ascendinglist(totalSize);
		} else {
			listId = descendinglist(totalSize);
		}

		if (visualize == true) {
			defaultConstruct(listId);
			return true;
		}

		// Step 3: user input the number of subnetworks
		System.out.println("Enter the number of subrings. (1 to " + totalSize + ")");
		try {
			subringSize = sc.nextInt();
		} catch (InputMismatchException e) {
			System.out.println(e.getMessage());
		}
		if (subringSize > totalSize) {
			throw new IllegalArgumentException("Subring Size should not exceed the total number of processors.");
		}

		// Step 4: How to construct the sequence
		System.out.println("How to construct the sequence?");
		System.out.println("1. Manual \t 2. Random");
		try {
			random_options = sc.nextInt();
		} catch (InputMismatchException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		if (random_options > 2 || random_options <= 0) {
			throw new IllegalArgumentException("the number should be in the range of 1 to 2");
		} else if (random_options == 1) {
			constructManualRing(totalSize, subringSize, listId, sc);
		} else {
			constructRandomRing(totalSize, subringSize, listId);
		}
		return false;
	}

	// generate random rings
	private ArrayList<Integer> randomlist(int totalsize) {
		ArrayList<Integer> listId = new ArrayList<Integer>();
		int id = 0;
		// generate ascending ids
		for (int i = 0; i < totalsize; i++) {
			id = id + (int) (Math.random() * 3) + 1;
			listId.add(id);
		}
		Collections.shuffle(listId);
		return listId;
	}

	// generate ascending clockwise rings
	private ArrayList<Integer> ascendinglist(int totalsize) {
		int id = 0;
		ArrayList<Integer> listId = new ArrayList<Integer>();
		for (int i = 0; i < totalsize; i++) {
			id += 1;
			listId.add(id);
		}
		return listId;
	}

	// generate descending clockwise rings
	private ArrayList<Integer> descendinglist(int totalsize) {
		int id = 0;
		ArrayList<Integer> listId = new ArrayList<Integer>();
		for (int i = 0; i < totalsize; i++) {
			id += 1;
			listId.add(id);
		}
		Collections.reverse(listId);
		return listId;
	}

	private void defaultConstruct(ArrayList<Integer> listid) {

		this.addNode(listid.get(0));
		InterfaceNode node1 = new InterfaceNode(new Ring());
		this.addNode(node1);
		// construct the subring of the interface node
		for (int i = 0; i < 4; i++) {
			Node sub_node = new SubringNode(listid.get(i + 1), node1);
			node1.getRing().addNode(sub_node);
		}
		this.addNode(listid.get(5));
		this.addNode(listid.get(6));
		this.addNode(listid.get(7));
		InterfaceNode node2 = new InterfaceNode(new Ring());
		this.addNode(node2);
		for (int i = 0; i < 5; i++) {
			Node sub_node = new SubringNode(listid.get(i + 8), node2);
			node2.getRing().addNode(sub_node);
		}

	}

	private void constructManualRing(int totalsize, int subringSize, ArrayList<Integer> listId, Scanner sc) {
		// Step 4: How to construct the sequence
		// Step 5: How to construct a sub ring
		int nodeComplete = 0;
		int subRingComplete = 0;
		int sizeofMainRing = 0;
		int subRingposition = 0;

		while (subRingComplete < subringSize) {
			System.out.println("Choose the position to place the subring.");
			System.out.println("Completed subrings: " + subRingComplete + " Total sub ring: " + subringSize);
			// print place available
			System.out.println(
					"Available position (notice the previous will automatically assign to non-interface nodes)");
			System.out.println();
			int position = sizeofMainRing + (totalsize - nodeComplete) - (subringSize - subRingComplete) + 1;
			if (nodeComplete == totalsize && subRingComplete < subringSize) {
				throw new IllegalArgumentException("No processor can be contructed, try again!");
			}
			for (int i = sizeofMainRing + 1; i <= position; i++) {
				System.out.print(i + " ");
			}

			System.out.println();
			try {
				subRingposition = sc.nextInt();
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
			}
			if (subRingposition > position || subRingposition <= sizeofMainRing) {
				throw new IllegalArgumentException("the number should be a valid number");
			}
			// construct the processor till this interface node
			// add non interface node
			int nonInterfaceId = nodeComplete;
			for (int i = sizeofMainRing; i < subRingposition - 1; i++) {
				this.addNode(listId.get(nonInterfaceId));
				nonInterfaceId++;
			}
			// at subRingposition, add interface node
			InterfaceNode newNode = new InterfaceNode(new Ring());
			this.addNode(newNode);
			// update the main ring size
			nodeComplete += (subRingposition - sizeofMainRing - 1);
			sizeofMainRing = subRingposition;
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			System.out.println("Enter the size of the subring");
			System.out.println("Range: 1 to " + (totalsize - nodeComplete - (subringSize - subRingComplete) + 1));
			int sizeofSubring = 0;
			try {
				sizeofSubring = sc.nextInt();
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
			}
			if (sizeofSubring > (totalsize - nodeComplete) || sizeofSubring < 1) {
				throw new IllegalArgumentException("the number should between 1 to " + (totalsize - nodeComplete));
			}
			// construct the subring of the interface node
			for (int i = 0; i < sizeofSubring; i++) {
				Node sub_node = new SubringNode(listId.get(i + nodeComplete), newNode);
				newNode.getRing().addNode(sub_node);
			}
			nodeComplete += sizeofSubring;
			subRingComplete++;
			System.out.println();
			display("Current");
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
		// complement the rest ones
		while (nodeComplete < totalsize) {
			this.addNode(listId.get(nodeComplete));
			nodeComplete++;
		}
		display("Initial final");
		sc.close();

	}

	private void constructRandomRing(int totalsize, int subsize, ArrayList<Integer> listId) {
		int num_iter = 0;
		int interfaceRemain = subsize;
		double avgProbability = (subsize * 1.0) / totalsize;
		while (num_iter < totalsize) {
			double InterfaceProbability = Math.random();
			if (interfaceRemain > 0
					&& (InterfaceProbability < avgProbability || interfaceRemain >= (totalsize - num_iter - 1))) {
				// this is the interface node, default id is -1
				Ring sub_ring = new Ring();
				InterfaceNode newNode = new InterfaceNode(sub_ring);
				// determine the size of subring
				int subSize = (int) (((totalsize - num_iter) * 0.8) * (Math.random())) + 1;
				// construct the subring according to the subSize
				for (int i = 0; i < subSize; i++) {
					Node sub_node = new SubringNode(listId.get(num_iter + i), newNode);
					sub_ring.addNode(sub_node);
				}

				this.addNode(newNode);
				num_iter += subSize;
				interfaceRemain -= 1;
			} else {
				// this is non-interface node
				Node sub_node = new Node(listId.get(num_iter));
				this.addNode(sub_node);
				num_iter += 1;
			}
		}
		display("Initial final");
	}

	public void setHead(Node head) {
		this.head = head;
	}

	public void setTail(Node tail) {
		this.tail = tail;
	}

	public Node getHead() {
		return head;
	}

	public Node getTail() {
		return tail;
	}

}
