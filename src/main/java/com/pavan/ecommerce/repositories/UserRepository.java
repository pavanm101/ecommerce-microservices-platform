package com.pavan.ecommerce.repositories;

import com.pavan.ecommerce.models.Product;
import com.pavan.ecommerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

