package com.aibaixun.iotdm.service.impl;

import com.aibaixun.iotdm.entity.RuleResourceEntity;
import com.aibaixun.iotdm.mapper.RuleResourceMapper;
import com.aibaixun.iotdm.service.IRuleResourceService;
import com.aibaixun.iotdm.util.UserInfoUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 规则资源 服务实现类
 * </p>
 *
 * @author baixun
 * @since 2022-03-03
 */
@Service
public class RuleResourceServiceImpl extends ServiceImpl<RuleResourceMapper, RuleResourceEntity> implements IRuleResourceService {


    @Override
    public Page<RuleResourceEntity> pageQueryRuleResource(Integer page, Integer pageSize, String resourceLabel) {

        LambdaQueryWrapper<RuleResourceEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(RuleResourceEntity::getTenantId, UserInfoUtil.getTenantIdOfNull());
        if (Objects.nonNull(resourceLabel)){
            queryWrapper.likeRight(RuleResourceEntity::getResourceLabel,resourceLabel);
        }
        queryWrapper.orderByDesc(RuleResourceEntity::getCreateTime);
        return page(Page.of(page,pageSize),queryWrapper);
    }


    @Override
    public List<RuleResourceEntity> listQueryRuleResource(Integer limit, String resourceLabel) {
        LambdaQueryWrapper<RuleResourceEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(RuleResourceEntity::getTenantId, UserInfoUtil.getTenantIdOfNull());
        if (Objects.nonNull(resourceLabel)){
            queryWrapper.likeRight(RuleResourceEntity::getResourceLabel,resourceLabel);
        }
        queryWrapper.orderByDesc(RuleResourceEntity::getCreateTime);
        queryWrapper.last(" LIMIT "+ limit);
        return list(queryWrapper);
    }
}