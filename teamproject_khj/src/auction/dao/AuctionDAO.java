package auction.dao;


import org.apache.ibatis.annotations.Param;

import auction.model.vo.AuctionVO;
import auction.model.vo.BidVO;

public interface AuctionDAO {

	boolean insertAuctionStart(@Param("auction")AuctionVO auction);

	boolean insertBid(@Param("bi_au_num")int au_num, @Param("bi_me_id")String id, @Param("bi_price")int bid);

	boolean updateAuction(@Param("au_num")int au_num, @Param("wb")BidVO winBidder);

}
