package auction.controller;

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
		} else {
			System.out.println("[기록 실패]");
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

	public void searchAuctionList(String search) {
		List<AuctionVO> list =	auctionService.getSearchAuctionList(search);
		if(list.size() == 0) {
			System.out.println("일치하는 경매 기록이 없습니다.");
		}
		System.out.println("[검색 결과]");
		for(AuctionVO auction : list) {
			System.out.println(auction);
		}
		
	}

	public void searchBidList(String search) {
		List<BidVO> list =	auctionService.getSearchBidList(search);
		if(list.size() == 0) {
			System.out.println("일치하는 입찰 기록이 없습니다.");
		}
		System.out.println("[검색 결과]");
		for(BidVO bid : list) {
			System.out.println("[" + bid.getAuction().getAu_date()+"][경매품: " + bid.getAuction().getAu_name() 
					+"][입찰가: " + bid.getBi_price() + "][입찰자ID: " + bid.getBi_me_id()+"]");
		}
	}

	public void getBidListById(String id) {
		List<BidVO> list =	auctionService.getSearchBidListById(id);
		if(list.size() == 0) {
			System.out.println("경매기록이 없습니다.");
		}
		System.out.println("[검색 결과]");
		for(BidVO bid : list) {
			System.out.println("[" + bid.getAuction().getAu_date()+"][경매품: " + bid.getAuction().getAu_name() 
					+"][입찰가: " + bid.getBi_price() + "][입찰자ID: " + bid.getBi_me_id()+"]");
		}
	}

}
