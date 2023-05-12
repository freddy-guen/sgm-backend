package fr.guen.dev.sgm.rest;

import fr.guen.dev.sgm.constants.SgmConstants;
import fr.guen.dev.sgm.services.interfaces.UserService;
import fr.guen.dev.sgm.utils.SgmUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(
        value = "/api/v1/sgm/user"
)
@Slf4j
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<String> signUp(
            @RequestBody(required = true) Map<String, String> requestMap
    ) {
        try{
            return userService.signUp(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return SgmUtils.getResponseEntity(SgmConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
