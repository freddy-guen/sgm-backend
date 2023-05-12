package fr.guen.dev.sgm.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SgmUtils {

    private SgmUtils() {

    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        return new ResponseEntity<>("{\"message\":\""+responseMessage+"\"}", httpStatus);
    }
}
