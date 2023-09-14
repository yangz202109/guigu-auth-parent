package com.study.config;

import com.study.custom.CustomMd5PasswordEncoder;
import com.study.filter.TokenAuthenticationFilter;
import com.study.handler.AccessDeniedHandlerImpl;
import com.study.handler.AuthenticationEntryPointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.annotation.Resource;

/**
 * @author yangz
 * @date 2022/12/9 - 15:05
 * springSecurity配置类
 */
@Configuration
@EnableWebSecurity //开启springSecurity的默认行为
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启注解功能，默认禁用注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private CustomMd5PasswordEncoder passwordEncoder;
    @Resource
    private AccessDeniedHandlerImpl accessDeniedHandler;
    @Resource
    private AuthenticationEntryPointImpl authenticationEntryPoint;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //禁止跨域攻击
        http.csrf().disable();

        //白名单
        String[] urls = {
                "/system/user/login",
                "/system/user/register"
        };
        http.authorizeRequests()
                .antMatchers(urls).permitAll() //指定该接口不需要认证
                .anyRequest().authenticated() //其余接口都需认证
                .and()
                //加入过到Security的滤器链 TokenAuthenticationFilter放到UsernamePasswordAuthenticationFilter的前面，
                //这样做就是为了除了登录的时候去查询数据库外，其他时候都用token进行认证。
                .addFilterBefore(new TokenAuthenticationFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class);

        //Spring Security默认使用的是Session机制,禁用session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 配置自定义异常处理器
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 指定UserDetailService和加密器
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    /**
     * 配置哪些请求不拦截
     * 排除swagger相关请求
     *
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/favicon.ico", "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/doc.html");
    }
}
