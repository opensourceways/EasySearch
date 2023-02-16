package com.search.docsearch.service.impl;

import com.search.docsearch.config.MySystem;
import com.search.docsearch.service.DataImportService;
import com.search.docsearch.utils.IdUtil;
import com.search.docsearch.utils.ParseData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

@Service
@Slf4j
public class DataImportServiceImpl implements DataImportService {
    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    @Qualifier("setConfig")
    private MySystem s;

    @Autowired
    private ParseData parseData;


    @Value("${kafka.topic}")
    private String topic;


    @Override
    public void refreshDoc() throws IOException {
        File indexFile = new File(s.basePath);
        if (!indexFile.exists()) {
            log.error(String.format("%s 文件夹不存在", indexFile.getPath()));
            log.error("服务器开小差了");
            return;
        }
        log.info("开始更新es文档");
        String lang = "";
        String deleteType = "";
        File[] typeDir;
        File[] languageDir = indexFile.listFiles();
        assert languageDir != null;
        for (File languageFile : languageDir) {
            lang = languageFile.getName();
            String saveIndex = s.index + "_" + lang;
            try {
                makeIndex(saveIndex);
            } catch (Exception e) {
                log.error(e.getMessage());
                continue;
            }

            typeDir = languageFile.listFiles();
            assert typeDir != null;
            for (File typeFile : typeDir) {
                if (typeFile.isDirectory()) {
                    BulkRequest bulkRequest = new BulkRequest();
                    deleteType = typeFile.getName();
                    Collection<File> listFiles = FileUtils.listFiles(typeFile, new String[]{"md", "html"}, true);
                    System.out.println(lang + "/" + deleteType + " -- " + listFiles.size());
                    for (File mdFile : listFiles) {
                        if (!mdFile.getName().startsWith("_")) {
                            try {
                                Map<String, Object> map = parseData.parse(lang, deleteType, mdFile);
                                if (map != null) {
                                    IndexRequest indexRequest = new IndexRequest(saveIndex).id(IdUtil.getId()).source(map);
                                    bulkRequest.add(indexRequest);
                                }
                            } catch (Exception e) {
                                log.info(mdFile.getPath());
                                log.error(e.getMessage());
                            }
                        }
                    }
                    log.info(deleteType + " have " + bulkRequest.requests().size());
                    DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(saveIndex);
                    BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
                    boolQueryBuilder.must(new TermQueryBuilder("deleteType.keyword", deleteType));
                    deleteByQueryRequest.setQuery(boolQueryBuilder);
                    BulkByScrollResponse r = restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
                    if (bulkRequest.requests().size() > 0) {
                        BulkResponse q = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

                        log.info("wrong ? " + q.hasFailures());
                        log.info(lang + "/" + deleteType + "更新成功");
                    }

                }
            }
        }
        log.info("所有文档更新成功");
    }

    public void makeIndex(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest(index);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        if (exists) {
            return;
        }

        CreateIndexRequest request1 = new CreateIndexRequest(index);
        File mappingJson = FileUtils.getFile(s.mappingPath);
        String mapping = FileUtils.readFileToString(mappingJson, StandardCharsets.UTF_8);

        request1.mapping(mapping, XContentType.JSON);
        request1.setTimeout(TimeValue.timeValueMillis(1));

        restHighLevelClient.indices().create(request1, RequestOptions.DEFAULT);
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void asyncrefreshDoc() throws IOException {
        boolean success = false;
        try {
            log.info("===============开始更新仓库资源=================");
            ProcessBuilder pb = new ProcessBuilder(s.updateDoc);
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                log.info(line);
                if (line.contains("build complete in")) {
                    success = true;
                }
            }
            log.info("===============仓库资源更新成功=================");
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        if (success) {
            log.info("全局更新成功!");
        } else {
            log.info("全局更新失败，开始局部更新");
            ProcessBuilder pb = new ProcessBuilder(s.updateLocal);
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }
        }

        refreshDoc();
    }

    @Override
    public void sendKafka(String data, String parameter) {
    }


}
