package auction.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import auction.dao.AuctionDAO;
import auction.model.vo.AuctionVO;
import auction.model.vo.BidVO;

public class AuctionServiceImp implements AuctionService{
	
	AuctionDAO auctionDao;
	AuctionVO auction;
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
		this.auction = auction; // 추가된 레코드에서 경매번호 가져옴
		return res;
	}

	@Override
	public boolean insertBid(String id, int bid) {
		if(id == null) {
			return false;
		}
		winBidder = new BidVO(bid, id);
		return auctionDao.insertBid(auction.getAu_num(), id, bid);
	}

	@Override
	public boolean updateAuction() {
		
		return auctionDao.updateAuction(auction.getAu_num(), winBidder);
	}

	@Override
	public List<AuctionVO> getAuctionList() {
		return auctionDao.selectAuctionList();
	}

	@Override
	public List<AuctionVO> getSearchAuctionList(String search) {
		if(search == null) {
			return null;
		}
		return auctionDao.selectSearchAuctionList(search);
	}

	@Override
	public List<BidVO> getSearchBidList(String search) {
		if(search == null) {
			return null;
		}
		return auctionDao.getBidWithAuction(search);
	}

	@Override
	public List<BidVO> getSearchBidListById(String id, String search) {
		if(id == null) {
			return null;
		}
		return auctionDao.getBidWithAuctionById(id, search);
	}
}
