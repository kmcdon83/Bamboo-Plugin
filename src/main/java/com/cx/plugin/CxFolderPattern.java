package com.cx.plugin;

import com.atlassian.util.concurrent.NotNull;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;


/**
 * CxFolderPattern generates the patterns used for zipping the workspace folder
 */

class CxFolderPattern {
    static String generatePattern(String folderExclusions, String filterPattern, BuildLoggerAdapter buildLogger) throws IOException, InterruptedException {

        String excludeFoldersPattern = processExcludeFolders(folderExclusions, buildLogger);

        if (StringUtils.isEmpty(filterPattern) && StringUtils.isEmpty(excludeFoldersPattern)) {
            return "";
        } else if (!StringUtils.isEmpty(filterPattern) && StringUtils.isEmpty(excludeFoldersPattern)) {
            return filterPattern;
        } else if (StringUtils.isEmpty(filterPattern) && !StringUtils.isEmpty(excludeFoldersPattern)) {
            return excludeFoldersPattern;
        } else {
            return filterPattern + "," + excludeFoldersPattern;
        }
    }


    @NotNull
    private static String processExcludeFolders(String folderExclusions, BuildLoggerAdapter buildLogger) {
        if (StringUtils.isEmpty(folderExclusions)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        String[] patterns = StringUtils.split(folderExclusions, ",\n");
        for (String p : patterns) {
            p = p.trim();
            if (p.length() > 0) {
                result.append("!**/");
                result.append(p);
                result.append("/**/*, ");
            }
        }
        buildLogger.info("Exclude folders converted to: '" + result.toString() + "'");
        return result.toString();
    }
}
