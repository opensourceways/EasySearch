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

package com.search.common.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ApplicationRunnerStart implements ApplicationRunner {

    /**
     * Logger for ApplicationRunnerStart.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationRunnerStart.class);

    /**
     * ApplicationRunner method for the Java application.
     *
     * @param args Command-line arguments
     */
    @Override
    public void run(ApplicationArguments args) {
        String rawSecretsDirStr = System.getenv("APPLICATION_PATH");
        if (rawSecretsDirStr == null || rawSecretsDirStr.length() == 0) {
            LOGGER.info("deletefail, env not found");
            return;
        }

        String secretsDirStr = rawSecretsDirStr.replace("/application.yaml", "");

        File secretsDir = new File(secretsDirStr);

        if (!secretsDir.isDirectory()) {
            LOGGER.info("delete fail, not a dir");
            return;
        }

        File[] listFiles = secretsDir.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.delete()) {
                    LOGGER.info(
                            "ID:search " + "Client Ip: localhost " + "Type: Delete " + " Resource:"
                                    + file.getAbsolutePath()
                                    + " Result: success.");
                } else {
                    LOGGER.error(
                            "ID:search " + "Client Ip: localhost " + "Type: Delete " + " Resource:"
                                    + file.getAbsolutePath()
                                    + " Result: failed.");
                }
            }
        } else {
            LOGGER.warn("No files found in the directory or an error occurred while listing files.");
        }
    }
}
