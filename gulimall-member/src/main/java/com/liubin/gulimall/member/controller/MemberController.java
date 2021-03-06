package com.liubin.gulimall.member.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.liubin.common.enums.BizCodeEnum;
import com.liubin.gulimall.member.exception.PhoneExistException;
import com.liubin.gulimall.member.exception.UserNameExistException;
import com.liubin.gulimall.member.vo.MemberLoginVo;
import com.liubin.gulimall.member.vo.MemberRegisterVo;
import com.liubin.gulimall.member.vo.MemberStatusVo;
import com.liubin.gulimall.member.vo.SocialUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.liubin.gulimall.member.entity.MemberEntity;
import com.liubin.gulimall.member.service.MemberService;
import com.liubin.common.utils.PageUtils;
import com.liubin.common.utils.R;



/**
 * 会员
 *
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-26 12:08:24
 */
@RestController
@RequestMapping("member/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("oauth2/login")
    public R oauthLogin(@RequestBody SocialUser socialUser) {
        MemberEntity member = memberService.login(socialUser);
        return R.ok().put("member", member);
    }

    @PostMapping("login")
    public R login(@RequestBody MemberLoginVo loginVo) {
        MemberEntity member = memberService.login(loginVo);
        return R.ok().put("member", member);
    }

    @PostMapping("register")
    public R register(@RequestBody MemberRegisterVo registerVo) {
        try {
            memberService.register(registerVo);
        }catch (PhoneExistException e){
            return R.error(BizCodeEnum.PHONE_EXIST_EXCEPTION.getCode(), BizCodeEnum.PHONE_EXIST_EXCEPTION.getMsg());
        }catch (UserNameExistException e){
            return R.error(BizCodeEnum.USER_EXIST_EXCEPTION.getCode(), BizCodeEnum.USER_EXIST_EXCEPTION.getMsg());
        }
        return R.ok();
    }

    @PostMapping("update/status")
    public R updateMemberStatus(@RequestBody MemberStatusVo statusVo) {
        MemberEntity member = new MemberEntity();
        member.setId(statusVo.getId());
        member.setStatus(statusVo.getStatus());
        this.memberService.updateById(member);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
        member.setCreateTime(new Date());
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
