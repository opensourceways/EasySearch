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
package com.search.infrastructure.search.softcenter.converter;


import com.alibaba.fastjson.JSONObject;
import com.search.domain.softcenter.vo.*;
import com.search.infrastructure.search.softcenter.dataobject.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
public final class SoftcenterConverter {

    private SoftcenterConverter() {

    }

    /**
     * convert SoftCenterDo  To ApplicationVersion.
     *
     * @param applicationVersionDoList The list of SoftCenterDo.
     * @return A list of ApplicationVersionVo.
     */
    public static List<ApplicationVersionVo> convertToApplicationVersion(List<SoftCenterDo> applicationVersionDoList) {
        List<ApplicationVersionVo> softwareAppVersionDtoList = new ArrayList<>();
        applicationVersionDoList.stream().forEach(m -> {
                    try {
                        ApplicationVersionVo applicationVersionVo = new ApplicationVersionVo();
                        BeanUtils.copyProperties(m, applicationVersionVo);
                        softwareAppVersionDtoList.add(applicationVersionVo);
                    } catch (Exception e) {
                        log.error("error happens in convertToApplicationVersion");
                    }
                }

        );
        return softwareAppVersionDtoList;
    }

    /**
     * convert SoftCenterDo  To FieldApplicationVo.
     *
     * @param fieldApplicationDoList The list of SoftCenterDo.
     * @return A list of FieldApplicationVo.
     */
    public static List<FieldApplicationVo> convertToFieldApplication(List<SoftCenterDo> fieldApplicationDoList) {
        List<FieldApplicationVo> fieldApplicationVoList = new ArrayList<>();
        fieldApplicationDoList.stream().forEach(m -> {
                    try {
                        FieldApplicationVo fieldApplicationVo = new FieldApplicationVo();
                        BeanUtils.copyProperties(m, fieldApplicationVo);
                        if (m.getPkgIds() != null) {
                            fieldApplicationVo.setPkgIds(JSONObject.parseObject(m.getPkgIds() + ""));
                        }
                        if (m.getTags() != null) {
                            fieldApplicationVo.setTags(JSONObject.parseArray(m.getTags() + ""));
                        }
                        fieldApplicationVoList.add(fieldApplicationVo);
                    } catch (Exception e) {
                        log.error("error happens in convertAllMapToSoftwareDto");
                    }
                }

        );
        return fieldApplicationVoList;
    }

    /**
     * convert SoftCenterDo  To ApplicationPackageVo.
     *
     * @param applicationPackageDoList The list of SoftCenterDo.
     * @return A list of ApplicationPackageVo.
     */
    public static List<ApplicationPackageVo> convertToApplicationPackage(List<SoftCenterDo> applicationPackageDoList) {
        List<ApplicationPackageVo> applicationPackageVoList = new ArrayList<>();
        applicationPackageDoList.stream().forEach(m -> {
                    try {
                        ApplicationPackageVo applicationPackageVo = new ApplicationPackageVo();
                        BeanUtils.copyProperties(m, applicationPackageVo);
                        if (m.getTags() != null) {
                            String tagsText = String.valueOf(m.getTags());
                            applicationPackageVo.setTags(Arrays.asList(tagsText.split(",")));
                        }
                        applicationPackageVo.getPkgIds().setEPKG(m.getEPKG() == null ? "" : m.getEPKG());

                        applicationPackageVo.getPkgIds().setIMAGE(m.getIMAGE() == null ? "" : m.getIMAGE());

                        applicationPackageVo.getPkgIds().setRPM(m.getRPM() == null ? "" : m.getRPM());

                        applicationPackageVoList.add(applicationPackageVo);
                    } catch (Exception e) {
                        log.error("error happens in convertAppMapToSoftwareDto");
                    }
                }

        );
        return applicationPackageVoList;
    }

    /**
     * convert SoftCenterDo  To EPKGPackageVo.
     *
     * @param epkgPackageDoList The list of SoftCenterDo.
     * @return A list of EPKGPackageVo.
     */
    public static List<EPKGPackageVo> convertToEPKGPackage(List<SoftCenterDo> epkgPackageDoList) {
        List<EPKGPackageVo> softwareEpkgVoList = new ArrayList<>();

        epkgPackageDoList.stream().forEach(m -> {
            try {
                EPKGPackageVo epkgPackageVo = new EPKGPackageVo();
                BeanUtils.copyProperties(m, epkgPackageVo);
                epkgPackageVo.setEpkgUpdateAt(m.getUpdatetime());
                epkgPackageVo.setEpkgCategory(m.getCategory());
                epkgPackageVo.setEpkgSize(m.getSize());
                softwareEpkgVoList.add(epkgPackageVo);
            } catch (Exception e) {
                log.error("error happens in convertAppMapToSoftwareEpkgDto");
            }
        });
        return softwareEpkgVoList;
    }

    /**
     * convert SoftCenterDo  To RPMPackageVo.
     *
     * @param rpmPackageDoList The list of SoftCenterDo.
     * @return A list of RPMPackageVo.
     */
    public static List<RPMPackageVo> convertAppMapToSoftwareRpmDto(List<SoftCenterDo> rpmPackageDoList) {
        List<RPMPackageVo> rpmPackageVoList = new ArrayList<>();
        rpmPackageDoList.stream().forEach(m -> {
            try {
                RPMPackageVo rpmPackageVo = new RPMPackageVo();
                BeanUtils.copyProperties(m, rpmPackageVo);
                rpmPackageVo.setRpmCategory(m.getCategory());

                rpmPackageVo.setRpmUpdateAt(m.getUpdatetime());
                rpmPackageVo.setRpmSize(m.getSize());
                rpmPackageVoList.add(rpmPackageVo);
            } catch (Exception e) {
                log.error("error happens inconvertAppMapToSoftwareRpmDto");
            }
        });
        return rpmPackageVoList;
    }

}
