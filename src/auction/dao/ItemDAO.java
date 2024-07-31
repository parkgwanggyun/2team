package auction.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import auction.vo.Item;

public interface ItemDAO {
	boolean insertItem(@Param("item")Item item);

	Item selectItem(@Param("name")String name);

	List<Item> selectItemList();
}