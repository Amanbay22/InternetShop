package kz.yertayev.shop.repositiry;

import kz.yertayev.shop.models.Country;
import kz.yertayev.shop.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CountryRepository extends JpaRepository<Country,Long> {

}
