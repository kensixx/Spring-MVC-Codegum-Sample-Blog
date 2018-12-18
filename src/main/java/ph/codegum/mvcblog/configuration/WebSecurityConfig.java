package ph.codegum.mvcblog.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

/**
 * Securing a Web Application
 * @link https://spring.io/guides/gs/securing-web/
 *
 * @author Ken on 12/16/2018
 * @project Spring-MVC-Blog
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Read query statement from application.properties file
     */
    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    /**
     * Configure HTTP permissions
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/home", "/error/**", "/posts", "/posts/view/**", "/users/logout", "/users/register", "/users/login").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/users/login")
                .failureUrl("/users/login?error=true")
                .defaultSuccessUrl("/")
                .usernameParameter("username")
                .passwordParameter("passwordHash")
                .and()
            .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/users/logout"))
                .logoutSuccessUrl("/").and().exceptionHandling()
                .accessDeniedPage("/error/403");
    }


    /**
     * Configure Web permissions (images, css, js, etc.)
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/img/**", "/js/**");
    }
}
