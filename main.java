package Task2;

import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;

public class main {

	public static void main(String[] args) {
		Ring res = new Ring();
		boolean visual = res.initialize();
		Scheduler solution = new Scheduler(res);
		if (visual == true) {
			UI visualize = new UI(res, solution.getSystemCounter());
			visualize.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			visualize.setSize(1000, 1000);
			visualize.show();
			solution.LCR(visualize);
		} else {
			solution.LCR();
		}
	}

}
