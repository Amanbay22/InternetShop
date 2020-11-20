package kz.yertayev.shop.repositiry;

import kz.yertayev.shop.models.Roles;
import kz.yertayev.shop.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    Users findByEmail(String email);
}
