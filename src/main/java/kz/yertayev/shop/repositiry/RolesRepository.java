package kz.yertayev.shop.repositiry;

import kz.yertayev.shop.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles,Long> {
}
