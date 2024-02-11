package ru.goryacheva.tacocloud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.goryacheva.tacocloud.models.User;
import ru.goryacheva.tacocloud.repository.jpa.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableGlobalMethodSecurity  //включение глобальной защиты методов
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeRequests()
                .antMatchers("/design", "/orders").access("hasRole('USER')")
                .antMatchers("/", "/**").access("permitAll()")
                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .oauth2Login()
                .loginPage("/login")
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .csrf()
                .and()
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository){
        return username -> {
            User user = userRepository.findByUsername(username);
            if (user != null)
                return user;
            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

    /* IN MEMORY AUTHENTICATION EXAMPLE
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder){
        List<UserDetails> userDetailsList = new ArrayList<>();
        userDetailsList.add(new User(
                "buzz", passwordEncoder.encode("password"),
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
        userDetailsList.add(new User(
                "woody", passwordEncoder.encode("password"),
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
        return new InMemoryUserDetailsManager(userDetailsList);
    }
     */
}

/*
* .logout() // выйдет и перенаправит на страницу аутентификации
* .logoutSuccessUrl("/") // выйдет и перенаправится на увазанную страницу
* .oauth2Login().loginPage("/login") //задаем страницу входа, что бы можно было аутентифицироваться обычно (логин + пароль) и через стороннюю службу
*
* */

/*
//запрос на вход будет иметь путь /authenticate и имя пользователя и пароль будут передаваться в полях user и pwd
.and()
.formLogin()
.loginPage("/login")
.loginProcessingUrl("/authenticate")
.usernameParameter("user")
.passwordParameter("pwd")

//если полозователь напрямую открыл страницу входа после успешной аутентификации пользователь будет перенаправлен на страницу /design
// .defaultSuccessUrl("/design", true) параметр true принудительно отправляет на страницу /design
.and()
.formLogin()
.loginPage("/login")
.defaultSuccessUrl("/design")
 */

/*
    //только по вторникам User может сделать новый taco
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeRequests()
                .antMatchers("/design", "/orders")
                .access("hasRole('USER') && T(java.util.Calendar).getInstance().get(" +
                        "T(java.util.Calendar).DAY_OF_WEEK) == T(java.util.Calendar).TUESDAY")
                .antMatchers("/", "/**").access("permitAll()")
                .and()
                .build();
    }
*/