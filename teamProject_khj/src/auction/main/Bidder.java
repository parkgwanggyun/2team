package auction.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Scanner;

import auction.controller.AuctionController;
import auction.controller.MemberController;
import auction.controller.PrintController;
import auction.model.vo.MemberVO;


public class Bidder {

	private String SERVER_IP = "182.227.11.155";
	private int SERVER_PORT = 6006;
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	MemberVO member = null;
	int possibleMinBid; 
	boolean auctionState = false;
	boolean Bidding = false;
	boolean exitFlag = false;
	private Scanner scan = new Scanner(System.in);
	MemberController memberController = new MemberController(scan);
	AuctionController auctionController = new AuctionController(scan);

	public Bidder() {
		try {
			socket = new Socket(SERVER_IP, SERVER_PORT);
			System.out.println("- 서버에 연결되었습니다.");
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

		}  catch (Exception e) {
		}
	}
	public void start() {       
		while (true) {
			System.out.println("1. 로그인");
			System.out.println("2. 회원가입");
			System.out.println("3. 종료");
			System.out.print("선택: ");
			char choice = scan.next().charAt(0);

			if (choice == '1') {
				member = memberController.logIn();
				if(member != null) {
					bidderReceiver();
					out.println("LOGIN::"+ member.getMe_id()); // 서버에 로그인 알림
					auctionStart();
				}
			} else if (choice == '2') {
				MemberVO nMem = memberController.register();
				if(nMem == null) {
					continue;
				}
				out.println("REGISTER::"+nMem.getMe_id()+"::"+nMem.getMe_name()+"::"
						+ nMem.getMe_address() +"::"+ nMem.getMe_contact()); // 서버에 회원가입 알림
			} else if (choice == '3') {
				out.println("EXIT");
				System.out.println("[프로그램 종료]");
				exitFlag = true;
				break;
			} else {
				System.out.println("잘못된 선택입니다.");
			}
		}

	}
	private void auctionStart() {
		while (true) {
			System.out.println("1. 경매 참여");
			System.out.println("2. 경매기록조회");
			System.out.println("3. 나가기");
			System.out.print("선택: ");
			char choice = scan.next().charAt(0);

			if (choice == '1') {
				out.println("JOIN::");
				Bidding = true;
				bidStart();
				
			} else if (choice == '2') {		
				auctionController.getBidListById(member.getMe_id());
				PrintController.bar();
			} else if (choice == '3') {	
				break;
			} else {
				System.out.println("잘못된 선택입니다.");
			}
		}
	}

	private void bidStart() {
		while (true) {
			System.out.println("1. 입찰하기");
			System.out.println("2. 이전으로");
			System.out.print("선택: ");
			char choice = scan.next().charAt(0);
			if (choice == '1') {	
				if(!auctionState) {
					System.out.println("진행 중인 경매가 없습니다.");
					continue;
				}
				if(Bidding) {
					System.out.print("입찰가 입력 > ");
					int bid = scan.nextInt();
					if(bid >= possibleMinBid) {
						out.println("BID::" + member.getMe_id() + "::" + bid);
					} else {
						System.out.println(getFormatWon(possibleMinBid) + "원 이상만 입찰 가능합니다.");
					}
				}
			} else if (choice == '2') {	
				Bidding = false;
				break;
			} else {
				System.out.println("잘못된 선택입니다.");
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
								auctionState = true;
								String[] parts = response.split("::");
								String notify = parts[1];
								System.out.println(notify);
							}
							else if(response.startsWith("AUCTION_ON")) {
								auctionState = true;
								String[] parts = response.split("::");
								String notify = parts[1];
								System.out.println(notify);
							}
							else if (response.startsWith("PRESENT_CONDITION")) {
								printAuctionPc(response);
							} else if (response.startsWith("FINISH")) {
								String[] parts = response.split("::");
								String notify = parts[1];
								System.out.println(notify);
								break;
							} else if(response.startsWith("AUCTION_OFF")) {
								auctionState = false;
								String[] parts = response.split("::");
								String notify = parts[1];
								System.out.println(notify);
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
		String[] parts = response.split("::");
		String name = parts[1];
		String startPrice = parts[2];
		String highestPrice = parts[3];
		String endTime = parts[4];
		String increment = parts[5];
		String id = parts[6];
		if(id.equals(member.getMe_id())) {
			System.out.println("[입찰 성공]");
		}
		int highestPriceInt = Integer.parseInt(highestPrice);
		int incrementInt = Integer.parseInt(increment);		
		possibleMinBid = highestPriceInt + incrementInt; // 입찰 가능 금액
		System.out.println("진행중인 경매 [경매품:] " + name + " [시작가:] " + getFormatWon(startPrice) + " [최고입찰가:] " 
				+ getFormatWon(highestPrice) +"("+id+") [종료시간:] " + endTime + "\n[최소 입찰 가능액:] " + getFormatWon(possibleMinBid) + "");	
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


}
