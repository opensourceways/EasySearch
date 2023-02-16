package com.search.docsearch.service;

import com.search.docsearch.entity.vo.EulerForumPost;

import java.io.IOException;

public interface DataImportService {

    void refreshDoc() throws IOException;

    void asyncrefreshDoc() throws IOException;

    void sendKafka(String data, String parameter);
}
