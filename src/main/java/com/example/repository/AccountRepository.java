package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Account;

public interface AccountRepository extends JpaRepository <Account, Integer>{
    /* 
    *
    * @param username the username of the account.
    * @return Accoun matching the given username.
    */
   @Query("FROM Account WHERE username = :usernameVar")
   Account register(@Param("usernameVar") String username);

   /**
    * 
    *
    * @param username the username of the account.
    * @param password the password of the user.
    * @return Account that matches given username and password.
    */
   @Query("FROM Account WHERE username = :usernameVar AND password = :passwordVar")
   Account login(@Param("usernameVar") String username, @Param("passwordVar") String password);

   /**
    * Get all Accounts
    */
   @Query("FROM Account")
   List<Account> getAllAccounts();

}
