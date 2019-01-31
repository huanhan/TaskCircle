package com.tc.until;


public class StringResourceCenter {
    public static final String SERVICE_INIT_FAILED = "初始化服务失败";
    public static final String DB_QUERY_FAILED = "数据库查询失败";
    public static final String DB_QUERY_ABNORMAL = "数据库查询失败,查询结果不存在";
    public static final String DB_INSERT_FAILED = "数据库插入失败";
    public static final String DB_INSERT_ABNORMAL = "数据库插入异常，未全部成功插入";
    public static final String DB_DELETE_FAILED = "数据库删除失败";
    public static final String DB_DELETE_ABNORMAL = "数据库删除异常，没有删掉任何数据";
    public static final String DB_UPDATE_ABNORMAL = "数据库修改异常";
    public static final String DB_RESOURCE_ABNORMAL = "数据库资源异常";
    public static final String VALIDATOR_INSERT_FAILED = "请不要添加异常数据";
    public static final String VALIDATOR_INSERT_ABNORMAL = "要添加的数据在数据库中已存在";
    public static final String VALIDATOR_AUTHORITY_FAILED = "你没有权限";
    public static final String VALIDATOR_UPDATE_ABNORMAL = "没有需要更新的内容";
    public static final String VALIDATOR_QUERY_FAILED = "请输入正确的查询内容";

    public static final String VALIDATOR_QUERY_ADMIN_FAILED = VALIDATOR_QUERY_FAILED + "，管理员编号不能为空";
    public static final String VALIDATOR_ADD_TITLE_FAILED = "已存在相同标题";
    public static final String VALIDATOR_ADD_ID_FAILED = "已存在相同编号";
    public static final String VALIDATOR_STATE_FAILED = "用户状态异常";
    public static final String VALIDATOR_TASK_STATE_FAILED = "任务状态有误";

    public static final String VALIDATOR_OSS_TOKEN_FAILED = "验证资源上传权限失败";
    public static final String VALIDATOR_CONNECTION_ERROR = "连接推送服务器请求失败，请重试";
    public static final String VALIDATOR_REQUEST_ERROR = "请求参数错误，请重试";

    public static final String CONTEXT_NOT_NULL = "内容不能为空";



}
