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
package com.search.infrastructure.support.converter;

import com.alibaba.fastjson.JSONObject;
import com.search.common.util.ObjectMapperUtil;
import com.search.domain.base.vo.CountVo;
import com.search.domain.base.vo.TagsVo;
import com.search.domain.mindspore.vo.MindSporeCourseVo;
import com.search.domain.mindspore.vo.MindSporeVo;
import com.search.infrastructure.search.mindspore.dataobject.MindsporeCourseDo;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class CommonConverter {

    // Private constructor to prevent instantiation of the CommonConverter class
    private CommonConverter() {
        // private constructor to hide the implicit public one
        throw new AssertionError("CommonConverter class cannot be instantiated.");
    }

    /**
     * Convert an list of map object to an CountVo list.
     *
     * @param numberList The list of map  to convert
     * @return An list of CountVo
     */
    public static List<CountVo> toCountVoList(List<Map<String, Object>> numberList) {
        List<CountVo> countVos = new ArrayList<>();
        for (Map<String, Object> map : numberList) {
            CountVo countVo = new CountVo();
            countVo.setDoc_count(Long.parseLong(map.get("doc_count") + ""));
            countVo.setKey(String.valueOf(map.get("key")));
            countVos.add(countVo);
        }
        return countVos;
    }

    /**
     * Convert an list of map object to an TagsVo list.
     *
     * @param numberList The list of map  to convert
     * @return An list of TagsVo
     */
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

    /**
     * Convert an list of map object to an Do list.
     *
     * @param dateMapList The list of map  to convert
     * @param clazz       to Convert type
     * @param <T>         to Convert type.
     * @return An list of clazz
     */
    public static <T> List<T> toDoList(final List<Map<String, Object>> dateMapList, Class<T> clazz) {
        List<T> doList = new ArrayList<>(dateMapList.size());
        for (Map<String, Object> map : dateMapList) {
            T t = ObjectMapperUtil.toObject(clazz, JSONObject.toJSONString(map));
            doList.add(t);
        }
        return doList;
    }
    /**
     * Convert a map object to a Do.
     *
     * @param dataMap The list of map  to convert
     * @param clazz       to Convert type
     * @param <T>    to Convert type.
     * @return a <T> clazz
     */
    public static <T> T toDo(final Map<String, Object> dataMap, Class<T> clazz) {
        T t = ObjectMapperUtil.toObject(clazz, JSONObject.toJSONString(dataMap));
        return t;
    }

    /**
     * Convert an list of map object to an vo list.
     *
     * @param doList The list of map  to convert.
     * @param clazz  to Convert type.
     * @param <T>    to Convert type.
     * @return An list of clazz
     */
    public static <T> List<T> toBaseVoList(final List doList, Class<T> clazz) {
        List<T> voList = new ArrayList<>(doList.size());
        for (Object dataObject : doList) {
            T t = ObjectMapperUtil.toObject(clazz, JSONObject.toJSONString(dataObject));
            voList.add(t);
        }
        return voList;
    }

    /**
     * Convert a Do object to an Base vo .
     *
     * @param dataObject The Object.
     * @param clazz  to Convert type.
     * @param <T>    to Convert type.
     * @return An list of clazz
     */
    public static <T> T toBaseVo(final Object dataObject, Class<T> clazz) {
        T t = ObjectMapperUtil.toObject(clazz, JSONObject.toJSONString(dataObject));
        return t;
    }

    /**
     * Convert a do to a vo entity.
     *
     * @param mindsporeCourseDo The MindsporeCourseDo entity.
     * @return An MindSporeVo entity
     */
    public static MindSporeVo toBaseCourseVo(MindsporeCourseDo mindsporeCourseDo) {
        MindSporeVo mindSporeVo = new MindSporeVo();
        MindSporeCourseVo mindSporeCourseVo = new MindSporeCourseVo();
        BeanUtils.copyProperties(mindsporeCourseDo, mindSporeCourseVo);
        BeanUtils.copyProperties(mindSporeCourseVo, mindSporeVo);
        mindSporeVo.setMindsporeCourseVo(mindSporeCourseVo);
        return mindSporeVo;
    }

}
