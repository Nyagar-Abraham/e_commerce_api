package org.abraham.e_commerce_api.repositories;

import org.abraham.e_commerce_api.entities.Role;
import org.abraham.e_commerce_api.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "cart")
    @Query("SELECT u FROM User u WHERE u.id =:userId")
    Optional<User> findWithCart(@Param("userId") Long userId);

    List<User> findByRole(Role role);
}
