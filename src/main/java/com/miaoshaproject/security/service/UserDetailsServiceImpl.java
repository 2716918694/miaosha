package com.miaoshaproject.security.service;

import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.dao.UserPasswordDOMapper;
import com.miaoshaproject.dataobject.UserDO;
import com.miaoshaproject.dataobject.UserPasswordDO;
import com.miaoshaproject.security.model.UserDetailsModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Override
    public UserDetails loadUserByUsername(String telephone) throws UsernameNotFoundException {
        //由于未对用户名设置唯一表示
        //此处我们选择通过以手机完成业务逻辑
        UserDO userDO = userDOMapper.selectByTelephone(telephone);
        if(userDO == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        return convertFromDO(userDO,userPasswordDO);
    }

    //通过Dao层获取用户相关信息
    private UserDetailsModel convertFromDO (UserDO userDO, UserPasswordDO userPasswordDO){
        if(userDO == null || userPasswordDO == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        UserDetailsModel userDetailsModel = new UserDetailsModel();
        BeanUtils.copyProperties(userDO,userDetailsModel);
        userDetailsModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        return userDetailsModel;
    }

}
