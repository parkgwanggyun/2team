package auction.service;

import java.util.List;

import auction.model.vo.AuctionVO;

public interface AuctionService {

	boolean insertAuction(AuctionVO auction);

	boolean insertBid(String id, int intBid);

	boolean updateAuction();

	List<AuctionVO> getAuctionList(String me_id);

}
