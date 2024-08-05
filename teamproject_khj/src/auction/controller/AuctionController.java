package auction.controller;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

import auction.main.PresentCondition;
import auction.model.vo.AuctionVO;
import auction.model.vo.BidVO;
import auction.service.AuctionService;
import auction.service.AuctionServiceImp;

public class AuctionController {
	private Scanner scan;
	private AuctionService auctionService = new AuctionServiceImp();
	public AuctionController(Scanner scan) {
		this.scan = scan;
	}
	
	//경매기록에 날짜, 경매품명, 시작가를 저장한다.
	public void startAuction(PresentCondition presentCondition) {
		if(presentCondition == null) {
			return;
		}
		String au_name = presentCondition.getName();
		int au_start_price = presentCondition.getStartPrice();
		AuctionVO auction = new AuctionVO(au_name, au_start_price);
		if(auctionService.insertAuction(auction)) {
			System.out.println("[경매 시작]");
			PrintController.bar();
		} else {
			System.out.println("[경매기록 실패]");
			PrintController.bar();
		}
	}

	public boolean insertBid(String id, String bid) {
		int intBid = Integer.parseInt(bid);
		return auctionService.insertBid(id, intBid);
	}
	
	//경매가 종료되면 낙찰가와 낙찰자를 등록하는 기능
	public void finishAuction() {
		 if(auctionService.updateAuction()) {
			 System.out.println("\n[경매 종료]");
		 }
	}
	//전체 경매기록을 조회해서 출력하는 기능
	public void printAuctionList() {
		List<AuctionVO> list =	auctionService.getAuctionList();
		if(list.size() == 0) {
			System.out.println("경매 기록이 없습니다.");
		}
		System.out.println("[전체 경매기록]");
		for(AuctionVO auction : list) {
			System.out.println(auction);
		}
	}

	public void searchAuctionList() {
		System.out.print("날짜|경매품|아이디 입력 >");
		scan.nextLine();
		String search = scan.nextLine();	
		List<AuctionVO> list =	auctionService.getSearchAuctionList(search);
		if(list.size() == 0) {
			System.out.println("일치하는 경매 기록이 없습니다.");
		}
		if(search.equals("")) {
			search = "전체";
		}
		System.out.println("<'"+search+"' 검색 결과>");
		for(AuctionVO auction : list) {
			System.out.println(auction);
		}
		
	}

	public void searchBidList() {
		System.out.print("날짜|경매품|아이디 입력 >");
		scan.nextLine();
		String search = scan.nextLine();
		List<BidVO> list =	auctionService.getSearchBidList(search);
		if(list.size() == 0) {
			System.out.println("일치하는 입찰 기록이 없습니다.");
		}
		if(search.equals("")) {
			search = "전체";
		}
		System.out.println("<'"+search+"' 검색 결과>");
		for(BidVO bid : list) {
			String au_name = bid.getAuction().getAu_name();
			int bi_price = bid.getBi_price();
			System.out.println("" + bid.getAuction().getAu_date()+"  |  경매품: " + au_name 
					+"  |  입찰가: " + getFormatWon(bi_price) + "  |  입찰ID: " + bid.getBi_me_id()+"");
		}
	}

	public void getBidListById(String id) {
		System.out.print("검색(날짜|경매품) > ");
		scan.nextLine();
		String search = scan.nextLine();
		List<BidVO> list =	auctionService.getSearchBidListById(id, search);
		if(list.size() == 0) {
			System.out.println("> 일치하는 경매기록이 없습니다.");
		}
		if(search.equals("")) {
			search = "전체";
		}
		System.out.println("<'"+search+"' 검색 결과>");
		for(BidVO bid : list) {
			String au_name = bid.getAuction().getAu_name();
			int bi_price = bid.getBi_price();
			System.out.println("" + bid.getAuction().getAu_date()+"  |  경매품: " + au_name 
					+"  |  입찰가: " + getFormatWon(bi_price) + "  |  입찰ID: " + bid.getBi_me_id()+"");
		}
	}
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
