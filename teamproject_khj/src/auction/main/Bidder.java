package auction.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

import auction.controller.AuctionController;
import auction.controller.MemberController;
import auction.controller.PrintController;
import auction.model.vo.MemberVO;


public class Bidder {

	private String SERVER_IP = "localhost";
	private int SERVER_PORT = 6006;
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	MemberVO member = null;
	int possibleMinBid; 
	boolean auctionState = false;
	boolean bidding = false;
	boolean exitFlag = false;
	boolean logIn = false;
	private Scanner scan = new Scanner(System.in);
	MemberController memberController = new MemberController(scan);
	AuctionController auctionController = new AuctionController(scan);

	public Bidder() {
		try {
			System.out.println(">>> 서버와 연결을 시도하는 중입니다.");
			socket = new Socket(SERVER_IP, SERVER_PORT);
			System.out.println(">>> 서버에 연결되었습니다.");
			PrintController.bar();
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

		}  catch (Exception e) {
			System.out.println(">>> 서버와 연결 할수 없습니다.");
			exitFlag = true;
			return;
		}
	}
	public void start() {       
		while (true) {
			if(exitFlag) {
				break;
			}
			System.out.println("1. 로그인");
			System.out.println("2. 회원가입");
			System.out.println("3. 종료");
			System.out.print("선택: ");
			int choice = nextInt();

			if (choice == 1) {
				PrintController.bar();
				member = memberController.logIn();
				if(member != null) {
					bidderReceiver();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}
					out.println("LOGIN::"+ member.getMe_id()); // 서버에 로그인 알림
					auctionStart();
				}
			} else if (choice == 2) {
				PrintController.bar();
				MemberVO nMem = memberController.register();
				PrintController.bar();
				if(nMem == null) {
					continue;
				}
				out.println("REGISTER::"+nMem.getMe_id()+"::"+nMem.getMe_name()+"::"
						+ nMem.getMe_address() +"::"+ nMem.getMe_contact()); // 서버에 회원가입 알림
			} else if (choice == 3) {
				if(out != null) {
					out.println("EXIT");
				}
				System.out.println("[프로그램 종료]");
				exitFlag = true;
				break;
			} else {
				System.out.println(">>> 잘못된 선택입니다.");
				PrintController.bar();
			}
		}

	}
	private void auctionStart() {
		logIn = true;
		while (true) {
			System.out.println("1. 경매 참여");
			System.out.println("2. 경매기록조회");
			System.out.println("3. 나가기");
			System.out.print("선택: ");
			int choice = nextInt();

			if (choice == 1) {
				PrintController.bar();
				if(!auctionState) {
					System.out.println(">>> 진행 중인 경매가 없습니다.");
					PrintController.bar();
					continue;
				}
				out.println("JOIN::");
				bidding = true;
				bidStart();
			} else if (choice == 2) {	
				PrintController.bar();
				auctionController.getBidListById(member.getMe_id());
				PrintController.bar();
			} else if (choice == 3) {	
				logIn = false;
				out.println("LOGOUT::"+ member.getMe_id());
				break;
			} else {
				System.out.println(">>> 잘못된 선택입니다.");
				PrintController.bar();
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}

	private void bidStart() {
		while (auctionState) {
			System.out.println("1. 입찰하기");
			System.out.println("2. 이전으로");
			System.out.print("선택: ");
			int choice = nextInt();
			if (choice == 1) {	
				PrintController.bar();
				if(!auctionState) {
					System.out.println(">>> 진행 중인 경매가 없습니다.");
					PrintController.bar();
					continue;
				}
				out.println("JOIN::");
				if(bidding) {
					System.out.print("입찰가 입력 > ");
					try {
						int bid = scan.nextInt();
						if(bid >= possibleMinBid) {
							out.println("BID::" + member.getMe_id() + "::" + bid);
						} else {
							System.out.println(">>> " + getFormatWon(possibleMinBid) + "원 이상만 입찰 가능합니다.");
							PrintController.bar();
						}
					} catch (InputMismatchException e) {
						System.out.println("[입력이 올바르지 않음]");
						PrintController.bar();
						scan.nextLine();
						continue;
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else if (choice == 2) {
				PrintController.bar();
				bidding = false;
				break;
			} else {
				System.out.println(">>> 잘못된 선택입니다.");
				PrintController.bar();
			}
		}
	}

	// 경매현황 수신
	private void bidderReceiver() {
		Thread thread = new Thread(()->{
			while(!exitFlag) {
				try {
					synchronized(in) {
						String response;
						try {
							response = in.readLine();
							if(response.startsWith("AUCTION_START")) {
								if(auctionState) {
									System.out.println(">>> 경매가 진행 중입니다.");
								} else {
									if(logIn) {
										System.out.println(">>> 경매를 시작합니다.");
									}
									auctionState = true;
								}
								PrintController.bar();
//								printStartAuctionPc(response);
							}
							else if(response.startsWith("AUCTION_ON")) {
								auctionState = true;
								String[] parts = response.split("::");
								String notify = parts[1];
								System.out.println(notify);
								PrintController.bar();
							}
							else if (response.startsWith("PRESENT_CONDITION")) {
								auctionState = true;
								printAuctionPc(response);
								PrintController.bar();
							} else if(response.startsWith("AUCTION_OFF")) {
								auctionState = false;
								String[] parts = response.split("::");
								String notify = parts[1];
								System.out.println(notify);
								PrintController.bar();
							} else if(response.startsWith("AUCTION_OUT")) {
								auctionState = false;
								PrintController.bar();
								break;
							} else if (response.startsWith("FINISH")) {
								auctionState = false;
								String[] parts = response.split("::");
								String notify = parts[1];
								System.out.println(notify);
								response = in.readLine();
								System.out.println(response);
								out.println("FINISH::");
								PrintController.bar();
							} else {
								System.out.println(response);
							}
						}catch(NullPointerException e) {
						}
					}
				} catch (IOException e) {
				}
			}
		});
		thread.start();

	}
	
	// 전송받은 경매현황을 출력해주는 기능
	private void printAuctionPc(String response) {
		if(!bidding) {
			return;
		}
		String[] parts = response.split("::");
		String name = parts[1];
		String startPrice = parts[2];
		String highestPrice = parts[3];
		String endTime = parts[4];
		String increment = parts[5];
		String id = parts[6];
		int highestPriceInt = Integer.parseInt(highestPrice);
		int incrementInt = Integer.parseInt(increment);		
		possibleMinBid = highestPriceInt + incrementInt; // 입찰 가능 금액
		if(!id.equals("아이디")) {	
			if(id.equals(member.getMe_id())) {
				System.out.println("[입찰 성공]");
			} else {
				System.out.println(">>> " + id + "님이 " + getFormatWon(highestPrice) + "원에 입찰!");
			}
			System.out.println("경매 현황 > 경매품: " + name + "  |  최고입찰가: " + getFormatWon(highestPrice) +"원 ("+id+")"
					+"  |  종료시간: " + endTime + "\n[최소 입찰 가능액: " + getFormatWon(possibleMinBid) + "원]");	
		} else {
			System.out.println("경매 현황 > 경매품: " + name + "  |  시작가: " + getFormatWon(startPrice) +"원 ("+id+")"
					+"  |  종료시간: " + endTime + "\n[최소 입찰 가능액: " + getFormatWon(possibleMinBid) + "원]");				
		}
		
	}

	// 세자리마다 , 넣어주는 기능
	public String getFormatWon(int price) {
		DecimalFormat format = new DecimalFormat("###,###,###,###");
		return format.format(price);
	}
	public String getFormatWon(String price) {
		int priceInt = Integer.parseInt(price);
		DecimalFormat format = new DecimalFormat("###,###,###,###");
		return format.format(priceInt);
	}
	public int nextInt() {
		try {
			return scan.nextInt();
		} catch (InputMismatchException e) {
			scan.nextLine();
			System.out.println(">>> 잘못 입력하셨습니다.");
			return Integer.MIN_VALUE;
		}
	}

}
