package auction.config;

import java.util.InputMismatchException;

public interface Program {
	void printMenu();
	void runMenu(int menu) throws Exception;
	void run();
	
	default void printBorder() {
		System.out.println("--------------");
	}
	
	default public int nextInt() {
		try {
			return Config.scan.nextInt();
		} catch (InputMismatchException e) {
			Config.scan.nextLine();
			return Integer.MIN_VALUE;
		}
	}
}