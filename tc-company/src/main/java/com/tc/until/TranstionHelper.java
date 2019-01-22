package com.tc.until;

import com.google.common.graph.ElementOrder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tc.db.entity.Authority;
import com.tc.db.entity.AuthorityResource;
import com.tc.db.entity.Resource;
import com.tc.db.entity.UserAuthority;
import com.tc.dto.Show;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TranstionHelper {
    public static List<Show> toShowByResources(List<Resource> resources){
        List<Show> show = new ArrayList<>();
        if (!resources.isEmpty()){
            resources.forEach(resource ->
                    show.add(new Show(resource.getId(),resource.getName()))
            );
        }
        return show;
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
        List<AuthorityResource> result = new ArrayList<>();
        if (!authorityResources.isEmpty()){
            authorityResources.forEach(authorityResource -> {
                authorityResource.setAuthority(null);
                authorityResource.setResource(null);
                }
            );
        }
        return result;
    }

    public static String toGson(Object o){

        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(o);

    }
}
