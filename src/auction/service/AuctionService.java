package auction.service;

import auction.model.vo.AuctionVO;

public interface AuctionService {

	boolean insertAuction(AuctionVO auction);

	boolean insertBid(String id, int intBid);

	boolean updateAuction();

}
