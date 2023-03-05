package Task2;

import java.util.ArrayList;
import java.util.Objects;

import javax.swing.*;
import java.awt.*;

public class UI extends JFrame {
	private int radius = 150;
	private int ringSize = 0;
	private int centerX = 200;
	private int centerY = 200;
	private int processorSize = 30;
	private final double PI = Math.PI;
	JFrame frame = new JFrame();
	JPanel panel = (JPanel) frame.getContentPane();

	private JLabel roundDisplay;
	private JLabel messageSent;
	private JLabel broadcastMessage;
	private JLabel colorInfo;
	private JLabel hint;

	private ArrayList<JLabel> label = new ArrayList<JLabel>();
	private ArrayList<JLabel> inID = new ArrayList<JLabel>();
	Ring ring;

	public UI(Ring input, SystemCounter counter) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setLayout(null);
		this.ringSize = 6;
		radius = ringSize * 25;
		this.ring = input;

		InitializeBasic(counter);
		InitializeNode(input);
		getContentPane().add(panel);
	}

	public void paint(Graphics g) {
		super.paint(g);
		double x;
		double y;
		double subX = 400;
		double subY = 400;
		int mainidx = 0;
		int globalidx = 0;

		Node current = this.ring.getHead();
		do {
			x = centerX + radius + Math.cos((float) mainidx / ringSize * 2 * PI) * radius;
			y = centerY + radius + Math.sin((float) mainidx / ringSize * 2 * PI) * radius;

			JLabel labelId = label.get(globalidx);
			Dimension size = labelId.getPreferredSize();
			panel.add(labelId);
			labelId.setBounds((int) (x - 1.3 * processorSize), (int) (y + 0.2 * processorSize), 2 * size.width,
					size.height);
			JLabel inIDs = inID.get(globalidx);
			panel.add(inIDs);
			size = inIDs.getPreferredSize();
			inIDs.setBounds((int) (x - 0.4 * processorSize), (int) (y - 1.5 * processorSize), 2 * size.width,
					size.height);
			globalidx++;
			if (current.getId() != -1) {
				// non interface node
				g.setColor(Color.BLACK);
			} else {
				// interface node
				g.setColor(Color.GREEN);
				SubringNode sub_current = (SubringNode) current.getRing().getHead();
				int subidx = -3;
				do {
					JLabel subLabel = label.get(globalidx);
					size = subLabel.getPreferredSize();
					panel.add(subLabel);
					subLabel.setBounds((int) (x + 150), (int) (y + 1.5 * size.height * subidx), 2 * size.width,
							size.height);
					JLabel subInID = inID.get(globalidx);
					panel.add(subInID);
					size = subInID.getPreferredSize();
					subInID.setBounds((int) (x + 80), (int) (y + 1.5 * size.height * subidx), 2 * size.width,
							size.height);
					sub_current = (SubringNode) sub_current.next;
					subidx++;
					globalidx++;
				} while (sub_current != current.getRing().getHead());
			}
			g.drawOval((int) x, (int) y, this.processorSize, this.processorSize);
			current = current.next;
			mainidx++;

		} while (current != this.ring.getHead());
	}

	public void paintComponent(Graphics g) {
		super.paintComponents(g);

	}

	private void addString(Graphics g, String id, int centerX, int centerY) {
		g.drawString(id, centerX, centerY);
	}

	private void updateFrame(Graphics g) {
		repaint();
	}

	private void InitializeBasic(SystemCounter counter) {
		this.roundDisplay = new JLabel(roundFormat(counter.getRoundCounter()));
		roundDisplay.setFont(new Font("Serif", Font.PLAIN, 20));
		panel.add(roundDisplay);
		Dimension size = roundDisplay.getPreferredSize();
		roundDisplay.setBounds(20, 20, 2 * size.width, size.height);

		this.messageSent = new JLabel(messageFormat(counter.getMessage()));
		messageSent.setFont(new Font("Serif", Font.PLAIN, 20));
		panel.add(messageSent);
		size = messageSent.getPreferredSize();
		messageSent.setBounds(20, 40, 2 * size.width, size.height);

		this.broadcastMessage = new JLabel(broadcastFormat(counter.getExtraRound(), counter.getExtraMessage()));
		panel.add(broadcastMessage);
		size = broadcastMessage.getPreferredSize();
		broadcastMessage.setBounds(20, 60, 2 * size.width, size.height);

		this.hint = new JLabel(
				"Hint: ID denote processor ID. T means awake while F means asleep. This is a clockwise version."
						+ " See terminal for final result.");
		panel.add(hint);
		size = hint.getPreferredSize();
		hint.setBounds(20, 80, 2 * size.width, size.height);
		this.colorInfo = new JLabel(
				"<html> </font> <font color='Green'>Interface Node </font> </font> <font color='black'>Non Interface Node </font> <font color='Blue'>Is not the leader </font><font color='Red'>Is Leader </font></html>");
		// this.colorInfo = new JLabel("Label Color: Black(Processor sleep),
		// Brown(Processor is executing), Blue(Processor know it is not the leader),
		// Red(Processor is the leader)");
		panel.add(colorInfo);
		size = colorInfo.getPreferredSize();
		colorInfo.setBounds(20, 100, 2 * size.width, size.height);
	}

	private void InitializeNode(Ring ring) {
		Node current = ring.getHead();
		do {
			this.label.add(new JLabel(processorFormat(current)));
			this.inID.add(new JLabel(inIDFormat(current)));
			if (current.getId() == -1) {
				// interface node
				SubringNode sub_current = (SubringNode) current.getRing().getHead();
				do {

					this.label.add(new JLabel(processorFormat(sub_current)));
					this.inID.add(new JLabel(inIDFormat(sub_current)));
					sub_current = (SubringNode) sub_current.next;
				} while (sub_current != current.getRing().getHead());
			}
			current = current.next;
		} while (current != ring.getHead());
	}

	private String roundFormat(int round) {
		return "Round: " + round;
	}

	private String messageFormat(int message) {
		return "Message sent: " + message;
	}

	private String broadcastFormat(int round, int message) {
		return "Broadcast Round: " + round + " Broadcast message: " + message;
	}

	private String processorFormat(Node node) {
		String awake = node.getisAwake() ? "T" : "F";
		return "ID:" + node.getId() + " Awake:" + awake;
	}

	private String inIDFormat(Node node) {
		int inID = node.getInId();
		if (inID == 0) {
			return "In ID: null";
		} else {
			return "In ID: " + inID;
		}
	}

	public void updateInId(Node head) {
		Node current = head;
		int idx = 0;
		do {
			inID.get(idx).setText(inIDFormat(current));
			System.out.println("idx " + idx + "\t" + inID.get(idx).getText());
			idx += 1;
			if (Objects.equals(current.getClass().getName(), "Task2.InterfaceNode")) {
				// this is interface node
				SubringNode sub_current = (SubringNode) current.getRing().getHead();
				do {
					inID.get(idx).setText(inIDFormat(sub_current));
					System.out.println("idx " + idx + inID.get(idx).getText());
					idx++;
					sub_current = (SubringNode) sub_current.next;
				} while (sub_current != current.getRing().getHead());
			} else {
				// this is non interface node
			}
			current = current.next;
		} while (current != head);
	}

	public void updateInfo(Node head, SystemCounter counter) {
		Node current = head;
		int idx = 0;
		do {
			label.get(idx).setText(processorFormat(current));
			if (current.getisLeader() == 1) {
				label.get(idx).setForeground(Color.BLUE);
			}
			if (current.getisLeader() == 2) {
				label.get(idx).setForeground(Color.RED);
			}
			System.out.println("idx " + idx + "\t" + label.get(idx).getText());
			idx += 1;
			if (Objects.equals(current.getClass().getName(), "Task2.InterfaceNode")) {
				SubringNode sub_current = (SubringNode) current.getRing().getHead();
				do {

					if (sub_current.getisLeader() == 1) {
						label.get(idx).setForeground(Color.BLUE);
					}
					if (sub_current.getisLeader() == 2) {
						label.get(idx).setForeground(Color.RED);
					}
					sub_current = (SubringNode) sub_current.next;
					idx++;

				} while (sub_current != (SubringNode) current.getRing().getHead());
			}
			current = current.next;
		} while (current != head);
		roundDisplay.setText(roundFormat(counter.getRoundCounter()));
		messageSent.setText(messageFormat(counter.getMessage()));
		broadcastMessage.setText(broadcastFormat(counter.getExtraRound(), counter.getExtraMessage()));
	}

	public int getringSize() {
		return this.ringSize;
	}

}
