package fr.guen.dev.sgm.services.interfaces;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {

    ResponseEntity<String> signUp(Map<String, String> requestMap);
}
