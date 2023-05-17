package fr.guen.dev.sgm.jwt;

import fr.guen.dev.sgm.common.constants.MessagesConstants;
import fr.guen.dev.sgm.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class CostumerUsersDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    private fr.guen.dev.sgm.models.User userDetail;

    /*public CostumerUsersDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }*/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}", username);
        userDetail = userRepository.findByEmailId(username);
        if(!Objects.isNull(userDetail)){
            return new User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException(MessagesConstants.USER_NOT_FOUND);
        }
    }

    public fr.guen.dev.sgm.models.User getUserDetail() {
        return userDetail;
    }
}
