package auction.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import auction.vo.Item;

public interface ItemDAO {
	boolean insertItem(@Param("item")Item item);

	List<Item> selectItemList();

	Item selectItem(@Param("it_num")int num);

	boolean updateAuctionNow(@Param("item")Item item);

	List<Item> getNowAuctionItemList();
}