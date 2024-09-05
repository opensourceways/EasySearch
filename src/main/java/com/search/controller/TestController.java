package com.search.controller;

import com.search.common.constant.SearchConstant;
import com.search.common.entity.ResponceResult;
import com.search.common.thread.ThreadLocalCache;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    RestHighLevelClient restHighLevelClient;

    @PostMapping("/docs")
    public ResponceResult checkEsIndexExist() {


        String dataSource = ThreadLocalCache.getDataSource();
        String index = dataSource + SearchConstant.INDEX_CONNECT + "zh";
        System.out.println(index);
        org.elasticsearch.client.indices.GetIndexRequest request = new GetIndexRequest(index);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        try {
            boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
            return ResponceResult.ok(exists);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponceResult.fail();
    }


}
