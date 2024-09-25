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
package com.search.adapter.condition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class NpsConditon {
    /**
     * condition field of feedbackPageUrl.
     */
    @NotBlank(message = "PageUrl can not be null")
    @Size(max = 200)
    private String feedbackPageUrl;
    /**
     * condition field of feedbackValue.
     */
    @Range(min = 1, max = 10, message = "score must be greater than 0 and less than 10 ")
    private int feedbackValue;
    /**
     * condition field of feedbackText.
     */
    @Size(max = 500)
    private String feedbackText;
}
