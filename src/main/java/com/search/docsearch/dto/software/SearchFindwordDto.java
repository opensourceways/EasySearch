package com.search.docsearch.dto.software;

import com.search.docsearch.utils.Trie;
import lombok.Data;

import java.util.List;

@Data
public class SearchFindwordDto {
    List<Trie.KeyCountResult> word;

    public SearchFindwordDto(List<Trie.KeyCountResult> word) {
        this.word = word;
    }
}
