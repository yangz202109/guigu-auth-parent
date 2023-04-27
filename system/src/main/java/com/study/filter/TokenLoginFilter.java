//package com.study.filter;
//
//import cn.hutool.core.convert.Convert;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.study.custom.CustomUser;
//import com.study.domain.ResultData;
//import com.study.domain.system.SysUser;
//import com.study.domain.vo.LoginVo;
//import com.study.utils.JwtUtil;
//import com.study.utils.ResponseUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import javax.servlet.FilterChain;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author yangz
// * @date 2022/12/9 - 17:05
// * 自定义登录拦截过滤器
// */
//public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
//
//    private static final Logger log = LoggerFactory.getLogger(TokenLoginFilter.class);
//
//    //重写构造函数
//    public TokenLoginFilter(AuthenticationManager authenticationManager) {
//        this.setAuthenticationManager(authenticationManager);
//        this.setPostOnly(false);
//        //指定登录接口(我们自定义的)及提交方式,可以指定任意路径(替换掉springSecurity默认的登录url)
//        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/user/login", "POST"));
//    }
//
//    /**
//     * 登录认证方法:获取用户名和密码进行认证
//     * AuthenticationManager的校验逻辑：
//     *   根据用户名先查询出用户对象(没有查到则抛出异常)将用户对象的密码和传递过来的密码进行校验,密码不匹配则抛出异常
//     */
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        try {
//            log.info("进入TokenLoginFilter:");
//            //获取请求中的登录对象
//            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
//            //将登录参数封装到Authentication对象中
//            Authentication auth = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
//            //委派AuthenticationManager做认证
//            return this.getAuthenticationManager().authenticate(auth);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * 认证成功调用方法
//     */
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
//        //获取认证对象
//        CustomUser customUser = (CustomUser) authResult.getPrincipal();
//        //生成token
//        SysUser sysUser = customUser.getSysUser();
//        String token = JwtUtil.createToken(Convert.toLong(sysUser.getId()), sysUser.getUsername());
//        //将token放入response
//        Map<String, String> map = new HashMap<>(1);
//        map.put("token", token);
//        ResponseUtil.out(response, ResultData.ok(map));
//    }
//
//    /**
//     * 认证失败调用方法
//     */
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
//        if (failed.getCause() instanceof RuntimeException) {
//            ResponseUtil.out(response, ResultData.error(failed.getMessage()));
//        } else {
//            ResponseUtil.out(response, ResultData.error("认证失败!"));
//        }
//    }
//}
