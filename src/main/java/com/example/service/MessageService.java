package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    MessageRepository messageRepository;

    @Autowired
    public MessageService (MessageRepository messageRepository) {
        this.messageRepository=messageRepository;
    }

    /**
     * Persist a new message
     */

    public Message createMessage (Message message){
      return messageRepository.save(message);
    }

    /**
     * 
     * @param message
     * Delete the message passed in from database using its messageId
     */
    public void deleteMessage (Message message) {
      messageRepository.deleteById(message.getMessageId());
    }
     /**
     * Find an existing message by messageId
     */

    public Message getMessagebyId (Integer messageId){
      Optional<Message> messageOptional = messageRepository.findById(messageId);
      if(messageOptional.isPresent()) {   
          return messageOptional.get();
      }
      return null;
    }

 
     /**
      * @return all messages
      */  

    public List<Message> getAllMessages () {
        return messageRepository.findAll();
    }

    /**
     * updated message passed in with new message text and save
     * @param messageToUpdate
     * @param messageText
     */
    public void updateMessage(Message messageToUpdate, String messageText) {
      messageToUpdate.setMessageText(messageText);
      messageRepository.save(messageToUpdate);
    }

    /**
     * get all messages by given message id using named query
     * @param accountId
     * @return List of messages found for given accountId
     */
    public List<Message> getMessageByAccountId(Integer accountId) {
      return messageRepository.findMessagesByPostedBy(accountId);
    }
}
