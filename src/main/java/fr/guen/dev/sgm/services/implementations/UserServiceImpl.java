package fr.guen.dev.sgm.services.implementations;

import fr.guen.dev.sgm.constants.SgmConstants;
import fr.guen.dev.sgm.models.User;
import fr.guen.dev.sgm.repository.UserRepository;
import fr.guen.dev.sgm.services.interfaces.UserService;
import fr.guen.dev.sgm.utils.SgmUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap);
        try {
            if (validateSignupMap(requestMap)) {
                User user = userRepository.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userRepository.save(getUserFromMap(requestMap));
                    return SgmUtils.getResponseEntity(SgmConstants.SUCCESSFUL_REGISTERED_USER, HttpStatus.OK);
                } else {
                    return SgmUtils.getResponseEntity(SgmConstants.EMAIL_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
                }
            } else {
                return SgmUtils.getResponseEntity(SgmConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return SgmUtils.getResponseEntity(SgmConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignupMap(Map<String, String> requestMap) {
        if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")){
            return true;
        } else {
            return false;
        }
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus(requestMap.get("status"));
        user.setRole(requestMap.get("role"));
        return user;
    }

}
