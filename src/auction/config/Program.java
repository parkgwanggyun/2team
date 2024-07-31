package auction.config;

public interface Program {
	void printMenu();
	void runMenu(int menu) throws Exception;
	void run();
	
	default void printBorder() {
		System.out.println("--------------");
	}
}