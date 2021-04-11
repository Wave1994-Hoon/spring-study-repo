package me.hoon.springrestapiwithtdd.config;

import me.hoon.springrestapiwithtdd.accounts.Account;
import me.hoon.springrestapiwithtdd.accounts.AccountRole;
import me.hoon.springrestapiwithtdd.accounts.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // Encoding 된 문자열 앞에 prefix 를 붙여 줌
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account test = Account.builder()
                    .email("test@test.com")
                    .password("test")
                    .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                    .build();
                accountService.saveAccount(test);
            }
        };
    }
}
