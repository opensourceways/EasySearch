package com.search.docsearch.init;


import com.search.docsearch.constant.Constants;
import com.search.docsearch.dto.software.SearchTagsDto;
import com.search.docsearch.entity.software.SoftwareSearchTags;
import com.search.docsearch.enums.SoftwareTypeEnum;
import com.search.docsearch.except.ServiceException;
import com.search.docsearch.service.ISoftwareEsSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SoftwareInit implements ApplicationRunner {

    private final ISoftwareEsSearchService searchService;


    @Override
    public void run(ApplicationArguments args) {
        List<SearchTagsDto> alltypeTags = new ArrayList<>();
        for (SoftwareTypeEnum value : SoftwareTypeEnum.values()) {
            if(SoftwareTypeEnum.RPMPKG.equals(value))
                continue;
            SoftwareSearchTags softwareSearchTags = new SoftwareSearchTags(value.getType(), "name", null);
            try {
                List<SearchTagsDto> typeTags = searchService.getTags(softwareSearchTags);
                if (!CollectionUtils.isEmpty(typeTags)) {
                    for (SearchTagsDto tag : typeTags) {
                        value.getTrie().insert(tag.getKey(), tag.getCount());
                    }
                    alltypeTags.addAll(typeTags);
                }
                log.info("{}初始化完成trie,size:{}", value.getType(), typeTags.size());
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }

        for (SearchTagsDto alltypeTag : alltypeTags) {
            Constants.softwareTrie.insert(alltypeTag.getKey(), alltypeTag.getCount());
        }

    }
}
