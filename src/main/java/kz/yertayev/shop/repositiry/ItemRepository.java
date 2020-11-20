package kz.yertayev.shop.repositiry;

import kz.yertayev.shop.models.Brand;
import kz.yertayev.shop.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
    List<Item> findAllByNameContainingAndBrandNameContainingAndPriceBetweenOrderByPriceAsc(String name, String brand_name, double price, double price2);
    List<Item> findAllByNameContainingAndBrandNameContainingAndPriceBetweenOrderByPriceDesc(String name, String brand_name, double price, double price2);
    List<Item> findAllByInTopPage(boolean inTopPage);
}
