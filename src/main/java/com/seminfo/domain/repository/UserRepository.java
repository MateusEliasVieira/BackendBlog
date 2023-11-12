package com.seminfo.domain.repository;

import com.seminfo.domain.model.User;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long>
{
    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User findAccountGoogleByEmail(@Param("email") String email);
    @Modifying
    @Query("UPDATE User u SET u.status = true WHERE u.token = :token")
    public Integer updateStatusUserByToken(@Param("token") String token);
    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User findUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    public User findUserByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.token = :token")
    public User findUserByToken(@Param("token") String token);
}
