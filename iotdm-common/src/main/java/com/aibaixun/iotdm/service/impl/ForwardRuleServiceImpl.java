package com.aibaixun.iotdm.service.impl;

import com.aibaixun.iotdm.entity.ForwardRule;
import com.aibaixun.iotdm.mapper.ForwardRuleMapper;
import com.aibaixun.iotdm.service.IForwardRuleService;
import com.aibaixun.iotdm.util.UserInfoUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 转发规则 服务实现类
 * </p>
 *
 * @author baixun
 * @since 2022-03-03
 */
@Service
public class ForwardRuleServiceImpl extends ServiceImpl<ForwardRuleMapper, ForwardRule> implements IForwardRuleService {


    @Override
    public Page<ForwardRule> pageQueryForwardRule(Integer page, Integer pageSize, String ruleLabel) {

        LambdaQueryWrapper<ForwardRule> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ForwardRule::getTenantId, UserInfoUtil.getTenantIdOfNull());
        if (StringUtils.isNotBlank(ruleLabel)){
            queryWrapper.likeRight(ForwardRule::getRuleLabel,ruleLabel);
        }
        queryWrapper.orderByDesc(ForwardRule::getCreateTime);
        return page(Page.of(page,pageSize),queryWrapper);
    }


    @Override
    public Boolean updateRuleStatus(String id, Boolean status) {
        return update(Wrappers.<ForwardRule>lambdaUpdate().set(ForwardRule::getRuleStatus,status).eq(ForwardRule::getId,id));
    }
}
