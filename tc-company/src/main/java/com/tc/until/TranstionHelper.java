package com.tc.until;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tc.db.entity.Authority;
import com.tc.db.entity.AuthorityResource;
import com.tc.db.entity.Resource;
import com.tc.dto.Show;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TranstionHelper {

    /**
     * 获取树形结构的资源列表，树根已资源的类名做区分
     * @param resources
     * @return
     */
    public static List<Show> toShowByResources(List<Resource> resources){
        List<Show> result = new ArrayList<>();
        if (!resources.isEmpty()){
            resources.forEach(resource ->{
                boolean findParent = false;

                for (Show show : result) {
                    if (resource.getClassName().equals(show.getId())){
                        show.getChildren().add(new Show(resource.getId(),resource.getName()));
                        findParent = true;
                        break;
                    }
                }

                if (!findParent){
                    List<Show> current = new ArrayList<>();
                    current.add(new Show(resource.getId(),resource.getName()));
                    result.add(new Show(resource.getClassName(),filterClassName(resource.getClassName()),current));
                }
            });
        }
        return result;
    }

    public static List<Show> toShowByAuthorities(List<Authority> authorities){
        List<Show> show = new ArrayList<>();
        if (!authorities.isEmpty()){
            authorities.forEach(resource ->
                    show.add(new Show(resource.getId(),resource.getName()))
            );
        }
        return show;
    }

    public static List<AuthorityResource> toAuthorityResourceID(List<AuthorityResource> authorityResources){
        if (!authorityResources.isEmpty()){
            authorityResources.forEach(authorityResource -> {
                authorityResource.setAuthority(null);
                authorityResource.setResource(null);
                }
            );
        }
        return authorityResources;
    }

    public static String toGson(Object o){

        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(o);

    }

    private static String filterClassName(String className){

        String current = StringUtils.substringAfterLast(className,".");

        String name = "";

        switch (current){
            case "AdminController":
                name = "管理员管理";
                break;
            case "AppChatController":
                name = "APP用户聊天管理";
                break;
            case "AppCommentController":
                name = "APP评论管理";
                break;
            case "AppController":
                name = "APP首页管理";
                break;
            case "AppFinanceController":
                name = "APP用户财务管理";
                break;
            case "AppHunterController":
                name = "APP猎刃个人信息管理";
                break;
            case "AppHunterTaskController":
                name = "APP猎刃任务管理";
                break;
            case "AppMessageController":
                name = "APP用户公告管理";
                break;
            case "AppResourceController":
                name = "APP资源中心管理";
                break;
            case "AppTaskController":
                name = "APP用户个人任务管理";
                break;
            case "AppUserController":
                name = "APP用户个人信息管理";
                break;
            case "ASController":
                name = "分析与统计管理";
                break;
            case "AuditController":
                name = "后台审核管理";
                break;
            case "AuthorityController":
                name = "后台权限管理";
                break;
            case "AuthorizationController":
                name = "后台授权管理";
                break;
            case "BasicController":
                name = "其他";
                break;
            case "CommentController":
                name = "后台评论管理";
                break;
            case "FinanceController":
                name = "公司财务管理";
                break;
            case "HunterController":
                name = "猎刃信息管理";
                break;
            case "MessageController":
                name = "后台公告管理";
                break;
            case "PersonalController":
                name = "管理员个人信息管理";
                break;
            case "TaskClassifyController":
                name = "任务分类管理";
                break;
            case "TaskController":
                name = "用户任务管理";
                break;
            case "UrlResourceController":
                name = "路径资源管理";
                break;
            case "UserController":
                name = "用户信息管理";
                break;
            default:
                break;
        }

        return name;
    }
}
