package com.search.infrastructure.support.converter;

import com.alibaba.fastjson.JSONObject;
import com.search.common.util.ObjectMapperUtil;
import com.search.domain.base.vo.CountVo;
import com.search.domain.base.vo.TagsVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonConverter {

    public static List<CountVo> toCountVoList(List<Map<String, Object>> numberList) {
        List<CountVo> countVos = new ArrayList<>();
        for (Map<String, Object> map : numberList) {
            CountVo countVo = new CountVo();
            BeanUtils.copyProperties(map, countVo);
            countVos.add(countVo);
        }
        return countVos;
    }


    public static List<TagsVo> toTagsVoList(List<Map<String, Object>> numberList) {
        List<TagsVo> tagsVos = new ArrayList<>();
        for (Map<String, Object> map : numberList) {
            TagsVo tagsVo = new TagsVo();
            BeanUtils.copyProperties(map, tagsVo);
            tagsVos.add(tagsVo);

            tagsVo.setKey((String) map.get("key"));
            tagsVo.setCount((Long) map.get("doc_count"));
        }
        return tagsVos;
    }


    public static <T> List<T> toDoList(final List<Map<String, Object>> dateMapList, Class<T> clazz) {
        List<T> doList = new ArrayList<>(dateMapList.size());
        for (Map<String, Object> map : dateMapList) {
            T t = ObjectMapperUtil.toObject(clazz, JSONObject.toJSONString(map));
            doList.add(t);
        }
        return doList;
    }


    public static <T> List<T> toBaseVoList(final List doList, Class<T> clazz) {
        List<T> voList = new ArrayList<>(doList.size());
        for (Object dataObject : doList) {
            T t = ObjectMapperUtil.toObject(clazz, JSONObject.toJSONString(dataObject));
            voList.add(t);
        }
        return voList;
    }

}
