package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.entity.Message;
import com.example.entity.Account;
import com.example.service.AccountService;
import com.example.service.MessageService;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;

    /**
     * Register an account
     * @param account
     * @return Status 200 if successful and new Account object status 409 if not
     * 
     */
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        if(account.getUsername().isBlank() || account.getPassword().length()<4)
           return ResponseEntity.status(400).build();

        List<Account> allAccounts = accountService.getAllAccounts();
        for(Account a : allAccounts){
            if(a.getUsername().equals(account.getUsername())){
               return ResponseEntity.status(409).build();
            }
        }
        Account newAccount = accountService.register(account);
        if(newAccount!= null) {
            return ResponseEntity.status(200).body(newAccount);
        }
        else {
            return ResponseEntity.status(400).build();
        }
    }

    /**
     * Login to an account (Verifies login credentials)
     * @param account
     * @return 200 if successful with the account object found, 401 if not 
     */
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account accountFound = accountService.login(account);
        if(accountFound != null){
            return ResponseEntity.status(200).body(accountFound);
        }
        return ResponseEntity.status(401).build();
    }
  /**
   * add a new message to database if data verification checks are successful
   * @param message
   * @return 400 if data verification fails otherwise 200 and new message that was created
   */
    @PostMapping("/messages")
    public ResponseEntity<Message> addNewMessage (@RequestBody Message message) {
        if(message.getMessageText() == null || message.getMessageText().isBlank() || message.getMessageText().length() > 255 || accountService.getAccountById(message.getPostedBy()) == null) {
            return ResponseEntity.status(400).body(null);          
        }
        Message newMessage = messageService.createMessage(message);
        System.out.println(newMessage);
        return ResponseEntity.status(200).body(newMessage);
    }

    /**
     * Retreieve all messages from database
     * @return List containing all messages
     */
    @GetMapping("/messages")
    @ResponseBody
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    /**
     * Retrieve all messages for a given messageId
     * @param messageId
     * @return Message object retrieved
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageByMessageId (@PathVariable Integer messageId) {
        Message messageRetrieved = messageService.getMessagebyId(messageId);
        if(messageRetrieved != null){
            return ResponseEntity.status(200).body(messageRetrieved);
        }

        return ResponseEntity.status(200).build();  
    }

    /**
     * Delete a message for a given messageId
     * @param messageId
     * @return Number of rows (1) affected if successful, nothing if no message was found for messageId
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageByMessageId (@PathVariable Integer messageId) {
        Message messageToDelete = messageService.getMessagebyId(messageId);
        if(messageToDelete != null){
            messageService.deleteMessage(messageToDelete);
            return ResponseEntity.status(200).body(1);
        }

        return ResponseEntity.status(200).build();
        
    }

    /**
     * Updates message based on messageId and messageTest passed in
     * Verifies message text is fit to propogate and message for the given messageId is found, 400 returned if not
     * calls updateMessage to update the message (set new value and save to database)
     * @param messageId
     * @param messageUpdate
     * @return 400 if any issues, 200 and number of rows affected (1) if no issues
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageByMessageId (@PathVariable Integer messageId, @RequestBody Message messageUpdate) {
        if(messageUpdate.getMessageText().isBlank() || messageUpdate.getMessageText().length() > 255) {
            return ResponseEntity.status(400).build();            
        }
        Message messageToUpdate = messageService.getMessagebyId(messageId);

        if(messageToUpdate != null){
            messageService.updateMessage(messageToUpdate, messageUpdate.getMessageText());
            return ResponseEntity.status(200).body(1);
        }
        else {
            return ResponseEntity.status(400).build();            
        }        
    }

    /**
     * Retrieve all messages for a given accountId
     * @param accountId
     * @return List of messages found for accountId (can be empty)
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessageByAccountId (@PathVariable Integer accountId) {
        return ResponseEntity.status(200).body(messageService.getMessageByAccountId(accountId));        
    }

}
