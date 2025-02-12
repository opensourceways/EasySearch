
/* Copyright (c) 2024 openEuler Community
 EasySoftware is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 See the Mulan PSL v2 for more details.
*/
package com.search.docsearch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.search.docsearch.recognition.SynonymMapper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Map;
import org.springframework.test.util.ReflectionTestUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.mockito.Mock;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class SynonymMapperTest {
    /**
     * es search strategy
     * 
     */
    @InjectMocks
    private SynonymMapper synonymMapper;
    
    /**
     * es search strategy
     * 
     */
    @Mock
    private Logger logger;
    
    /**
     * es search strategy
     * 
     */
    @Value("${synonym.xmlPath}")
    private String xmlPath;
    
    /**
     * es search strategy
     * 
     */
    @Value("${synonym.threshold}")
    private int threshold;

    @BeforeEach
    public void setUp() {
        // Initialize mocks and set up necessary fields
        ReflectionTestUtils.setField(synonymMapper, "xmlPath", "classpath:test-synonyms.xml");
        ReflectionTestUtils.setField(synonymMapper, "threshold", 2);
        Logger testlogger = LoggerFactory.getLogger(SynonymMapper.class);
        ReflectionTestUtils.setField(synonymMapper, "LOGGER", testlogger);
    }
 
    /**
      * 测试: 同义词测试
      */
      @Test
      public void testGetSynonyms() {
          // Setup the synonyms map for testing
          Map<String, Set<String>> testSynonymsMap = new HashMap<>();
          testSynonymsMap.put("apple", new HashSet<>(Set.of("fruit", "red fruit")));
          ReflectionTestUtils.setField(synonymMapper, "synonymsMap", testSynonymsMap);
   
          // Test exact match
          Set<String> synonyms = synonymMapper.getSynonyms("apple");
          assertEquals(new HashSet<>(Set.of("fruit", "red fruit")), synonyms);
   
          // Test non-existing term
          synonyms = synonymMapper.getSynonyms("orange");
          assertTrue(synonyms.isEmpty());
      }

    /**
      * 测试: 不存在的同义词
      */
    @Test
    public void testGetSynonymsWithNon() {
        // 测试一个不存在的词
        Set<String> nonExistentSynonyms = synonymMapper.getSynonyms("nonexistent");
        assertTrue(nonExistentSynonyms.isEmpty());
    }

    /**
      * 测试: fuzzy同义词测试
      */
    @Test
    public void testGetSynonymsFuzzy() {
        // Setup the synonyms map for testing
        Map<String, Set<String>> testSynonymsMap = new HashMap<>();
        testSynonymsMap.put("apple", new HashSet<>(Set.of("fruit", "red fruit")));
        testSynonymsMap.put("apricot", new HashSet<>(Set.of("stone fruit")));
        ReflectionTestUtils.setField(synonymMapper, "synonymsMap", testSynonymsMap);
 
        // Test fuzzy match
        Set<String> synonyms = synonymMapper.getSynonymsFuzzy("appl");
        assertEquals(new HashSet<>(Set.of("fruit", "red fruit")), synonyms);
 
        // Test fuzzy match with threshold
        synonyms = synonymMapper.getSynonymsFuzzy("apric");
        assertEquals(new HashSet<>(Set.of("stone fruit")), synonyms);
 
        // Test no fuzzy match
        synonyms = synonymMapper.getSynonymsFuzzy("grape");
        assertTrue(synonyms.isEmpty());
    }

}