package kz.yertayev.shop.repositiry;


import kz.yertayev.shop.models.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories,Long> {
}
