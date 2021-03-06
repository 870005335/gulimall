package com.liubin.gulimall.member.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.liubin.gulimall.member.vo.MemberLevelListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.liubin.gulimall.member.entity.MemberLevelEntity;
import com.liubin.gulimall.member.service.MemberLevelService;
import com.liubin.common.utils.PageUtils;
import com.liubin.common.utils.R;



/**
 * 会员等级
 *
 * @author liubin
 * @email 870005335@qq.com
 * @date 2021-02-26 12:08:24
 */
@RestController
@RequestMapping("member/memberlevel")
public class MemberLevelController {

    @Autowired
    private MemberLevelService memberLevelService;

    @GetMapping("/getMemberLevelList")
    public R getMemberLevelList() {
        List<MemberLevelListVo> memberLevelList = new ArrayList<>();
        List<MemberLevelEntity> levelList = memberLevelService.list();
        if (!CollectionUtils.isEmpty(levelList)) {
            memberLevelList = levelList.stream().map(level -> {
                MemberLevelListVo levelVo = new MemberLevelListVo();
                levelVo.setLevelId(level.getId());
                levelVo.setName(level.getName());
                return levelVo;
            }).collect(Collectors.toList());
        }
        return R.ok().put("resultList", memberLevelList);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("member:memberlevel:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberLevelService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("member:memberlevel:info")
    public R info(@PathVariable("id") Long id){
		MemberLevelEntity memberLevel = memberLevelService.getById(id);

        return R.ok().put("memberLevel", memberLevel);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("member:memberlevel:save")
    public R save(@RequestBody MemberLevelEntity memberLevel){
		memberLevelService.save(memberLevel);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("member:memberlevel:update")
    public R update(@RequestBody MemberLevelEntity memberLevel){
		memberLevelService.updateById(memberLevel);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("member:memberlevel:delete")
    public R delete(@RequestBody Long[] ids){
		memberLevelService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
