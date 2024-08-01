package auction.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import auction.dao.AuctionDAO;
import auction.model.vo.AuctionVO;
import auction.model.vo.BidVO;

public class AuctionServiceImp implements AuctionService{
	
	AuctionDAO auctionDao;
	int au_num;
	BidVO winBidder;
	
	public AuctionServiceImp() {
		String resource = "auction/config/mybatis-config.xml";
		InputStream inputStream;
		SqlSession session;
		try {
			inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			session = sessionFactory.openSession(true);
			auctionDao = session.getMapper(AuctionDAO.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean insertAuction(AuctionVO auction) {
		if(auction == null) {
			return false;
		}
		boolean res = auctionDao.insertAuctionStart(auction); //진행중인 경매정보를 db에 추가
		au_num = auction.getAu_num(); // 추가된 레코드에서 경매번호 가져옴
		return res;
	}

	@Override
	public boolean insertBid(String id, int bid) {
		if(id == null) {
			return false;
		}
		winBidder = new BidVO(bid, id);
		return auctionDao.insertBid(au_num, id, bid);
	}

	@Override
	public boolean updateAuction() {
		
		return auctionDao.updateAuction(au_num, winBidder);
	}
}
