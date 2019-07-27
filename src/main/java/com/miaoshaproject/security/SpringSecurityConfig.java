package com.miaoshaproject.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAuthenticationProvider authenticationProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://192.168.1.23:8080"));
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "OPTIONS", "DELETE"));
        configuration.setMaxAge(3600L);
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .exceptionHandling()
                .authenticationEntryPoint((request,response,authException) -> {
                    response.setContentType("application/json;charset=utf-8");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    PrintWriter out = response.getWriter();
                    out.write(objectMapper.writeValueAsString(CommonReturnType.create("fail","未登陆")));
                    out.flush();
                    out.close();
                }) //未登录时返回JSON数据
                .accessDeniedHandler((request,response,ex) -> {
                    response.setContentType("application/json;charset=utf-8");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    PrintWriter out = response.getWriter();
                    out.write(objectMapper.writeValueAsString(CommonReturnType.create("fail","权限不足")));
                    out.flush();
                    out.close();
                })//用户权限不足，禁止访问时
                .and()
                .csrf().disable() //关闭csrf验证
                .cors()//跨域请求
                .and()
                //.authenticationProvider(authenticationProvider)
                .httpBasic()
                // 定制请求的授权规则
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/",
                        "/user/getOtp",
                        //"/user/postOtp",//测试登录验证
                        "/user/putUserByPhone",
                        "/accesstoken").permitAll() //无条件允许访问
//              .antMatchers("/security/user/**").hasRole("ADMIN") //需要ADMIN角色才可以访问
                .anyRequest()
                .authenticated() //其他url需要身份认证
//              .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                    @Override
//                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
//                        o.setSecurityMetadataSource(filterInvocationSecurityMetadataSource); //动态获取url权限配置
//                        o.setAccessDecisionManager(accessDecisionManager); //权限判断
//                        return o;
//                    }
//                })

                // 开启自动配置的登录功能
                .and()
                .formLogin() //开启登录
//                .loginPage("/login") //登录页面(前后端不分离)
                .loginProcessingUrl("/user/login") //自定义登录请求路径(post)
//                .successForwardUrl("/index") //登录成功后的url(post,前后端不分离)
//                .failureForwardUrl("/error") //登录失败后的url(post,前后端不分离)
                .successHandler((request,response,authentication) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.write(objectMapper.writeValueAsString(CommonReturnType.create(authentication.getPrincipal(),"登录成功")));
                    out.flush();
                    out.close();
                }) //验证成功处理器(前后端分离)
                .failureHandler((request,response,ex) -> {
                    response.setContentType("application/json;charset=utf-8");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//返回通用对象，在此无需设置异常状态码
                    PrintWriter out = response.getWriter();
                    if (ex instanceof UsernameNotFoundException || ex instanceof BadCredentialsException) {
                        out.write(objectMapper.writeValueAsString(CommonReturnType.create("fail","用户名或密码错误")));
                    } else if (ex instanceof DisabledException) {
                        out.write(objectMapper.writeValueAsString(CommonReturnType.create("fail","用户被禁用")));
                    } else {
                        out.write(objectMapper.writeValueAsString(CommonReturnType.create("fail","登录失败")));
                    }
                    out.flush();
                    out.close();
                }) //验证失败处理器(前后端分离)
                .permitAll()

                // 开启自动配置的注销功能
                .and()
                .logout() //用户注销, 清空session
                .logoutUrl("/logout") //自定义注销请求路径
//                .logoutSuccessUrl("/bye") //注销成功后的url(前后端不分离)
                .logoutSuccessHandler((request,response,authentication) -> {

                }) //注销成功处理器(前后端分离)
                .permitAll();

//        http.sessionManagement().invalidSessionUrl("/session/invalid"); //session失效时间
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider); //自定义登录认证
    }


//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/js/**","/css/**","/images/**");
//    }

}
