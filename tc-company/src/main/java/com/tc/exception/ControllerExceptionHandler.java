package com.tc.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Cyg
 * 统一异常处理中心
 */
@RestController
@ControllerAdvice
public class ControllerExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(ValidException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public MyResponse handlerValidException(ValidException ex){

        List<Message> messages = new ArrayList<>();

        if (!ex.getErrors().isEmpty()) {

            for (FieldError error :
                    ex.getErrors()) {
                String message = error.getField() + error.getDefaultMessage();
                messages.add(new Message(message));
                logger.info(error.getField() + ":" + error.getDefaultMessage());
            }

            return new MyResponse(messages);
        }


        messages.add(new Message(ex.getMessage()));
        return new MyResponse(messages);
    }

    @ExceptionHandler(DBException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public MyResponse handlerDBException(DBException ex){
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(ex.getMessage()));
        return new MyResponse(messages);
    }


    private class MyResponse implements Serializable {

        private List<Message> messages;

        public MyResponse(List<Message> messages) {
            this.messages = messages;
        }

        public List<Message> getMessages() {
            return messages;
        }

        public void setMessages(List<Message> messages) {
            this.messages = messages;
        }
    }
}
