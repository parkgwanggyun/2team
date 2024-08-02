package auction.service;

import java.util.List;

import auction.model.vo.AuctionVO;
import auction.model.vo.BidVO;

public interface AuctionService {

	boolean insertAuction(AuctionVO auction);

	boolean insertBid(String id, int intBid);

	boolean updateAuction();

	List<AuctionVO> getAuctionList();

	List<AuctionVO> getSearchAuctionList(String search);

	List<BidVO> getSearchBidList(String search);

	List<BidVO> getSearchBidListById(String id);

}
