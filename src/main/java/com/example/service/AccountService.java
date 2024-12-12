package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.Account;
import com.example.repository.AccountRepository;
import java.util.Optional;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccountById(Integer id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if(accountOptional.isPresent()){
            Account account = accountOptional.get();
            return account;
        }
        return null;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account register(Account account) {
        return accountRepository.save(account);
    }

    public Account login(Account account) {
        List<Account> accounts = getAllAccounts();
        for(Account a : accounts) {
            if(a.getUsername().equals(account.getUsername()) ){
                if(a.getPassword().equals(account.getPassword())) {
                    return a;
                }
            }
        }
        return null;
    }
}
