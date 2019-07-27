package com.miaoshaproject.controller;


import com.alibaba.druid.util.StringUtils;
import com.miaoshaproject.controller.viewobject.UserVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.Random;

@RestController("user")
@RequestMapping("/user")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 用户获取otp短信接口
     * @param telephone
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value ="/getOtp",method = RequestMethod.POST,consumes = CONTENT_TYPE_FORMED)
    public CommonReturnType getOtp(@RequestParam(value = "telephone") String telephone) throws BusinessException {

        if (telephone == null || telephone == ""){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(89999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        //将OTP验证码和对应用户的手机号关联，使用HttpSession的方式绑定手机号
        httpServletRequest.getSession().setAttribute(telephone, otpCode);

        //将OTP验证码通过短信发送给用户
        System.out.println("otp send to telephone:"+telephone+",otpCode:"+otpCode);

        return CommonReturnType.create(CommonReturnType.create("success",telephone+"已发送短信"));
    }

    /**
     * 手机注册验证码验证
     * @param telephone
     * @param otpCode
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/postOtp",method = RequestMethod.POST,consumes = CONTENT_TYPE_FORMED)
    public CommonReturnType verifyOtp(@RequestParam(value = "telephone")String telephone,
                                      @RequestParam(value = "otpCode")String otpCode) throws BusinessException {

        if (telephone == null || telephone == "") {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号不能为空");
        }
        String inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute(telephone);
        if (StringUtils.equals(inSessionOtpCode, otpCode))
            return CommonReturnType.create("success","验证通过");
        else
            return CommonReturnType.create("fail","验证码错误");
    }

    /**
     * 用户注册
     * @param name
     * @param telephone
     * @param password
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/putUserByPhone",method = RequestMethod.POST,consumes = CONTENT_TYPE_FORMED)
    public CommonReturnType register(@RequestParam(value = "name")String name,
                                     @RequestParam(value = "telephone")String telephone,
                                     @RequestParam(value = "password")String password) throws BusinessException {

        if(name.isEmpty()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入正确的用户名");
        }
        if(telephone.isEmpty()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入正确的手机号");
        }
        if(password.isEmpty()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入正确的密码");
        }
        String encrptPassword = encoder.encode(password.trim());

        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setTelephone(telephone);
        userModel.setEncrptPassword(encrptPassword);
        userModel.setRegisterMode("by phone");
        userService.register(userModel);
        return CommonReturnType.create("OK","注册成功");
    }

    /**
     * 通过id获取用户信息
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/getUserById")
    public CommonReturnType getUser(@RequestParam(value = "id") Integer id) throws BusinessException {
        //调用service服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);
        //若获取对象不存在
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        //将核心领域模型转换为可供前端使用的viewobject
        UserVO userVO = convertFromModel(userModel);
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }

}
