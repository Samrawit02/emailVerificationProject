package com.samrit.emailverificationprojject.appUser;

import com.samrit.emailverificationprojject.appUser.registration.token.ConfirmationToken;
import com.samrit.emailverificationprojject.appUser.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private  final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final AppUserRepo appUserRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return appUserRepo.findByEmail(email)
                .orElseThrow(()->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG,email)));
    }
    public  String signUpUser(AppUser appUser){
        boolean userExits = appUserRepo.findByEmail(appUser.getEmail())
                .isPresent();
        if(userExits){
            // Todo check of attributes are the same and
            //Todo if email not confirmed send confirmation email

            throw  new IllegalStateException("email already taken");
        }

        String encodePassword = bCryptPasswordEncoder
                .encode(appUser.getPassword());
        appUser.setPassword(encodePassword);
        appUserRepo.save(appUser);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
// TODO: SEND EMAIL
        return token;
    }

    public int enableAppUser(String email) {
        return appUserRepo.enableAppUser(email);
    }
}
