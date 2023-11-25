package com.seminfo.domain.repository;

import com.seminfo.domain.enums.Roles;
import com.seminfo.domain.model.User;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

    @Modifying
    @Query("UPDATE User u SET u.attempts = :attempts WHERE u.username = :username")
    public void updateAttemptsUser(@Param("attempts") int attempts, @Param("username") String username);

    @Query("SELECT u.attempts FROM User u WHERE u.username = :username")
    public int attemptsUser(@Param("username") String username);

    @Query("SELECT u.role FROM User u WHERE u.username = :username")
    public Roles findRoleByUsername(@Param("username") String username);

    @Modifying
    @Query("UPDATE User u SET u.releaseLogin = :releaseDate WHERE u.username = :username")
    public void updateReleaseDate(@Param("releaseDate") Date releaseDate, @Param("username") String username);

    @Query("SELECT u.releaseLogin FROM User u WHERE u.username = :username")
    public Date getDateReleaseLogin(@Param("username") String username);
    @Modifying
    @Query("UPDATE User u SET u.releaseLogin = null, u.attempts = 0 WHERE u.username = :username")
    public void resetAttemptsAndReleaseLogin(@Param("username") String username);

}
