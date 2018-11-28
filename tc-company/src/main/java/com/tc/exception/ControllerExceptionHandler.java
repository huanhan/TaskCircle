package com.tc.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
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

        Map<String,String> messages = new HashMap<>();

        for (FieldError error :
                ex.getErrors()) {
            messages.put(error.getField(),error.getDefaultMessage());
            logger.info(error.getField() + ":" + error.getDefaultMessage());
        }

        return new MyResponse(messages);
    }


    private class MyResponse implements Serializable {

        private Map<String,String> messages;

        public MyResponse(Map<String, String> messages) {
            this.messages = messages;
        }

        public Map<String, String> getMessages() {
            return messages;
        }

        public void setMessages(Map<String, String> messages) {
            this.messages = messages;
        }
    }
}
