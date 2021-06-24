package com.liubin.gulimall.authserver.web;

import com.alibaba.fastjson.JSON;
import com.liubin.common.constant.AuthServerConstant;
import com.liubin.common.enums.BizCodeEnum;
import com.liubin.common.utils.R;
import com.liubin.gulimall.authserver.feign.MemberFeignService;
import com.liubin.gulimall.authserver.feign.ThirdPartyFeignService;
import com.liubin.common.vo.MemberRespVo;
import com.liubin.gulimall.authserver.vo.UserLoginVo;
import com.liubin.gulimall.authserver.vo.UserRegisterVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Author liubin
 * @Date 2021/5/27 14:28
 * @Version 1.0
 */
@Controller
public class LoginController {

    @Autowired
    private ThirdPartyFeignService thirdPartyFeignService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MemberFeignService memberFeignService;

    @GetMapping("sms/sendCode")
    @ResponseBody
    public R sendCode(@RequestParam("phone") String phone) {
        //TODO 接口防刷
        // redis 缓存验证码 防止同一个手机号在倒计时内多次发送验证码请求
        String cacheKey = AuthServerConstant.SMS_CODE_CACHE_KEY_PREFIX + phone;
        String redisCode = redisTemplate.opsForValue().get(cacheKey);
        if (StringUtils.isNotBlank(redisCode)) {
            long time = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() - time < 60000) {
                return R.error(BizCodeEnum.SMS_CODE_EXCEPTION.getCode(), BizCodeEnum.SMS_CODE_EXCEPTION.getMsg());
            }
        }
        String code = UUID.randomUUID().toString().substring(0, 6);
        redisTemplate.opsForValue().set(cacheKey, code + "_" + System.currentTimeMillis(), 10, TimeUnit.MINUTES);
        thirdPartyFeignService.sendCode(phone, code);
        return R.ok();
    }

    @PostMapping("register")
    public String register(@Valid UserRegisterVo registerVo, BindingResult result, RedirectAttributes model) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream().collect(
                    Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (v1, v2) -> v1));
            model.addFlashAttribute("errors", errors);
            // 校验出错，转发到注册页
            return "redirect:http://auth.gulimall.com/register.html";
        }
        // 调用注册服务之前 校验验证码
        String code = registerVo.getCode();
        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_KEY_PREFIX + registerVo.getPhone());
        if (StringUtils.isNotBlank(redisCode) &&  StringUtils.equals(code, redisCode.split("_")[1])) {
            //校验成功 删除验证码，令牌机制
            redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_KEY_PREFIX + registerVo.getPhone());
            // 调用远程服务进行注册
            R response = memberFeignService.register(registerVo);
            if (response.getCode() == 0) {
                // 注册成功 回到登录页面
                return "redirect:http://auth.gulimall.com/login.html";
            } else {
                Map<String, String> errors = new HashMap<>();
                errors.put("msg", response.get("msg").toString());
                model.addFlashAttribute("errors",errors);
                return "redirect:http://auth.gulimall.com/register.html";
            }
        } else {
            //校验不成功
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "验证码错误");
            model.addFlashAttribute("errors", errors);
            return "redirect:http://auth.gulimall.com/register.html";
        }
    }

    @PostMapping("login")
    public String login(UserLoginVo loginVo, RedirectAttributes model, HttpSession session) {
        // 调用远程接口
        R response = memberFeignService.login(loginVo);
        if (response.getCode() == 0) {
            MemberRespVo memberResp = JSON.parseObject(JSON.toJSONString(response.get("member")), MemberRespVo.class);
            if (memberResp != null) {
                // 将返回对象放入spring session
                session.setAttribute(AuthServerConstant.LOGIN_USER, memberResp);
                return "redirect:http://www.gulimall.com";
            } else {
                //校验出错
                Map<String, String> errors =new HashMap<>();
                errors.put("msg","用户名或密码不正确");
                model.addFlashAttribute("errors", errors);
                return "redirect:http://auth.gulimall.com/login.html";
            }
        } else {
            //校验出错
            Map<String, String> errors =new HashMap<>();
            errors.put("msg","用户名或密码不正确");
            model.addFlashAttribute("errors", errors);
            return "redirect:http://auth.gulimall.com/login.html";
        }
    }

//    @GetMapping("/login.html")
//    public String loginPage(HttpSession session) {
//        Object attribute = session.getAttribute(AuthServerConstant.LOGIN_USER);
//        if (attribute != null) {
//            return "redirect:http://www.gulimall.com";
//        }
//        return "redirect:http://auth.gulimall.com/login.html";
//    }
}
