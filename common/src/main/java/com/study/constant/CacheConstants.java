package com.study.constant;

/**
 * 缓存的key 常量
 * 
 * @author yangz
 */
public class CacheConstants
{
    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * 登录账户密码错误限制次数
     */
    public static final Integer PWD_ERR_NUM = 3;

    /**
     * 登录账户密码错误限制有效时间(秒) 30分钟
     */
    public static final Long PWD_ERR_EXPIRATION = 1800L;

    /** token有效时间(秒)  3天 */
    public static final Long TOKEN_EXPIRATION = 259200L;
}
