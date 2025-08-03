package com.craft.tmanager.configuration;


public class SecurityConfig {
    // @Autowired
    // private UserDetailsService userDetailsService;

    // @Autowired
    // private JwtRequestFilter jwtRequestFilter;

    // @Autowired
    // public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.userDetailsService(userDetailsService);
    // }

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }

    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    //     http.csrf().disable()
    //             .authorizeRequests()
    //             .anyRequest().permitAll()
    //             .and().sessionManagement()
    //             .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    //     http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    // }
    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    //     http.csrf().disable()
    //             .authorizeRequests().antMatchers("/authenticate", "/register").permitAll()
    //             .anyRequest().authenticated()
    //             .and().sessionManagement()
    //             .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    //     http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    // }

}
