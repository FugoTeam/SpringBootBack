package com.fugoworld.backend.repository;

import com.fugoworld.backend.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository
extends JpaRepository<User, Long> {
    @Query(value="select * from user where user.firstname=:firstname and user.lastname=:lastname", nativeQuery=true)
    public Optional<List<User>> getUsersByNames(@Param(value="firstname") String var1, @Param(value="lastname") String var2);

    public Optional<User> findUserByEmail(String var1);

    @Query(value="select * from user where user.city=:city", nativeQuery=true)
    public List<User> getUsersByCity(@Param(value="city") String var1);

    @Query(value="select * from user where  user.email=:email and user.password=:password", nativeQuery=true)
    public Optional<User> getUserByConnexion(@Param(value="email") String var1, @Param(value="password") String var2);

    @Query(value="select * from user where  user.email=:email", nativeQuery=true)
    public Optional<User> getUser(@Param(value="email") String var1);

    @Query(value="select * from user where  user.email=:email and user.isAdmin=true", nativeQuery=true)
    public Optional<User> getAdmin(@Param(value="email") String var1);

    @Query(value="select * from user where  user.isAdmin=true", nativeQuery=true)
    public List<User> getAdmins();

    @Query(value="select * from user where  user.isAdmin=false", nativeQuery=true)
    public List<User> getNonAdmins();
}
