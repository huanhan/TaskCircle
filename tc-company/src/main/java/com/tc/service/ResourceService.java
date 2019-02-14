package com.tc.service;

import com.tc.db.entity.Resource;
import com.tc.dto.resource.QueryResource;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 资源服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 * @author Cyg
 */
public interface ResourceService extends BasicService<Resource> {


    /**
     * 根据查询条件获取数据库资源
     * @param queryResource
     * @return
     */
    Page<Resource> findByQuery(QueryResource queryResource);

}
