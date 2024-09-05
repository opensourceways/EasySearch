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

package com.search.common.aop.log;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagementLog {
    /**
     * Type of the log event.
     */
    private String type;

    /**
     * Time of the log event.
     */
    private String time;

    /**
     * Function associated with the log event.
     */
    private String func;

    /**
     * Details of the event.
     */
    private String eventDetails;

    /**
     * URL of the request.
     */
    private String requestUrl;

    /**
     * HTTP method used in the request.
     */
    private String method;

    /**
     * IP address of the application.
     */
    private String appIP;

    /**
     * HTTP status code of the response.
     */
    private int status;

    /**
     * Message related to the event.
     */
    private String message;

    /**
     * Error log information.
     */
    private String errorLog;

    /**
     * Operator associated with the event.
     */
    private String operator;

}

