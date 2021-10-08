//tag::securityConfigOuterClass[]
package tacos.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

//end::securityConfigOuterClass[]
//tag::baseBonesImports[]
//end::baseBonesImports[]

//tag::securityConfigOuterClass[]

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//end::securityConfigOuterClass[]

    //tag::customUserDetailsService[]
    @Autowired
    private UserDetailsService userDetailsService;

//end::customUserDetailsService[]



    //tag::configureHttpSecurity[]
    //tag::authorizeRequests[]
    //tag::customLoginPage[]
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/design", "/order**")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/", "/**").access("permitAll")
                //end::authorizeRequests[]

                .and()
                    .formLogin()
                        .loginPage("/login")
                        .defaultSuccessUrl("/design")
                //end::customLoginPage[]

                // tag::enableLogout[]
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                // end::enableLogout[]

                // Make H2-Console non-secured; for debug purposes
                // tag::csrfIgnore[]
                .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**")
                // end::csrfIgnore[]

                // Allow pages to be loaded in frames from the same origin; needed for H2-Console
                // tag::frameOptionsSameOrigin[]
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()
        // end::frameOptionsSameOrigin[]

        //tag::authorizeRequests[]
        //tag::customLoginPage[]
        ;
    }
//end::configureHttpSecurity[]
//end::authorizeRequests[]
//end::customLoginPage[]


    //tag::customUserDetailsService_withPasswordEncoder[]
    @Bean
    public PasswordEncoder encoder() {
        return new StandardPasswordEncoder("53cr3t");
    }

/*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(encoder());

    }
    //end::customUserDetailsService_withPasswordEncoder[]
*/
//
// IN MEMORY AUTHENTICATION EXAMPLE
//
/*
//tag::configureAuthentication_inMemory[]
  @Override
  protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {
    
    auth
      .inMemoryAuthentication()
        .withUser("buzz")
          .password(encoder().encode("infinity"))
          .authorities("ROLE_USER")
        .and()
        .withUser("woody")
          .password(encoder().encode("bullseye"))
          .authorities("ROLE_USER");
    
  }
//end::configureAuthentication_inMemory[]
*/

//
// JDBC Authentication example
//

//tag::configureAuthentication_jdbc[]
  @Autowired
  DataSource dataSource;
 /*
  @Override
  protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {
    
    auth
      .jdbcAuthentication()
        .dataSource(dataSource)
            .passwordEncoder(encoder());
    
  }
//end::configureAuthentication_jdbc[]
*/

/*
//tag::configureAuthentication_jdbc_withQueries[]
  @Override
  protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {
    
    auth
      .jdbcAuthentication()
        .dataSource(dataSource)
        .usersByUsernameQuery(
            "select username, password, enabled from Users " +
            "where username=?")
        .authoritiesByUsernameQuery(
            "select username, authority from UserAuthorities " +
            "where username=?");
    
  }
//end::configureAuthentication_jdbc_withQueries[]
*/


//tag::configureAuthentication_jdbc_passwordEncoder[]
  @Override
  protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {
    
    auth
      .jdbcAuthentication()
        .dataSource(dataSource)
        .usersByUsernameQuery(
            "select username, password, enabled from User " +
            "where username=?")
//        .authoritiesByUsernameQuery(
//            "select username, authority from UserAuthorities " +
//            "where username=?")
        .passwordEncoder(encoder());
    
  }
//end::configureAuthentication_jdbc_passwordEncoder[]



//
// LDAP Authentication example
//
/*
//tag::configureAuthentication_ldap[]
@Override
public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
            .ldapAuthentication()
            .userDnPatterns("uid={0},ou=people")
            .groupSearchBase("ou=groups")
            .contextSource()
            .url("ldap://localhost:8389/dc=springframework,dc=org")
.and()
            .passwordCompare()
            .passwordEncoder(new LdapShaPasswordEncoder())
            .passwordAttribute("adminpassword");
}
//end::configureAuthentication_ldap[]
*/

//tag::securityConfigOuterClass[]

}
//end::securityConfigOuterClass[]
