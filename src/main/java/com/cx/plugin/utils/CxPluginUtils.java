package com.cx.plugin.utils;

import com.atlassian.bamboo.security.EncryptionException;
import com.atlassian.bamboo.security.EncryptionServiceImpl;
import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.dto.ScanResults;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 * Created by Galn on 24/10/2017.
 */

public abstract class CxPluginUtils {

    public static void printConfiguration(CxScanConfig config, CxConfigHelper configBFF, CxLoggerAdapter log) {
        log.info("---------------------------------------Configurations:------------------------------------");
        log.info("Bamboo plugin version: " + configBFF.getPluginVersion());
        log.info("Username: " + config.getUsername());
        log.info("URL: " + config.getUrl());
        log.info("Project name: " + config.getProjectName());
        log.info("Deny new project creation: " + config.getDenyProject());
        log.info("Hide scan results: " + config.getHideResults());
        log.info("Scan timeout in minutes: " + (config.getSastScanTimeoutInMinutes() <= 0 ? "" : config.getSastScanTimeoutInMinutes()));
        log.info("Full team path: " + config.getTeamPath());
        log.info("Preset: " + config.getPresetName());
        log.info("Is incremental scan: " + config.getIncremental());
        log.info("Is interval full scans enabled: " + configBFF.isIntervals());
        if (configBFF.isIntervals()) {
            log.info("Interval- begins: " + configBFF.getIntervalBegins());
            log.info("Interval- ends: " + configBFF.getIntervalEnds());
            String forceScan = config.getForceScan() ? "" : "NOT ";
            log.info("Override full scan: " + config.getForceScan() + " (Interval based full scan " + forceScan + "activated.)");
        }
        log.info("Folder exclusions: " + (config.getSastFolderExclusions()));
        log.info("Is synchronous scan: " + config.getSynchronous());
        log.info("Generate PDF report: " + config.getGeneratePDFReport());
        log.info("Policy violations enabled: " + config.getEnablePolicyViolations());
        log.info("CxSAST thresholds enabled: " + config.getSastThresholdsEnabled());
        if (config.getSastThresholdsEnabled()) {
            log.info("CxSAST high threshold: " + (config.getSastHighThreshold() == null ? "[No Threshold]" : config.getSastHighThreshold()));
            log.info("CxSAST medium threshold: " + (config.getSastMediumThreshold() == null ? "[No Threshold]" : config.getSastMediumThreshold()));
            log.info("CxSAST low threshold: " + (config.getSastLowThreshold() == null ? "[No Threshold]" : config.getSastLowThreshold()));
        }
        log.info("Policy violations enabled: " + config.getEnablePolicyViolations());
        log.info("CxOSA enabled: " + config.getOsaEnabled());
        if (config.getOsaEnabled()) {
            log.info("CxOSA filter patterns: " + config.getOsaFilterPattern());
            log.info("CxOSA archive extract patterns: " + config.getOsaArchiveIncludePatterns());
            log.info("Execute dependency managers 'install packages' command before Scan: " + config.getOsaRunInstall());

            log.info("CxOSA thresholds enabled: " + config.getOsaThresholdsEnabled());
            if (config.getOsaThresholdsEnabled()) {
                log.info("CxOSA high threshold: " + (config.getOsaHighThreshold() == null ? "[No Threshold]" : config.getOsaHighThreshold()));
                log.info("CxOSA medium threshold: " + (config.getOsaMediumThreshold() == null ? "[No Threshold]" : config.getOsaMediumThreshold()));
                log.info("CxOSA low threshold: " + (config.getOsaLowThreshold() == null ? "[No Threshold]" : config.getOsaLowThreshold()));
            }
        }
        log.info("------------------------------------------------------------------------------------------");
    }

    public static void printBuildFailure(String thDescription, ScanResults ret, CxLoggerAdapter log) {
        log.error("********************************************");
        log.error(" The Build Failed for the Following Reasons: ");
        log.error("********************************************");

        logError(ret.getGeneralException(), log);
        logError(ret.getSastCreateException(), log);
        logError(ret.getSastWaitException(), log);
        logError(ret.getOsaCreateException(), log);
        logError(ret.getOsaWaitException(), log);

        if (thDescription != null) {
            String[] lines = thDescription.split("\\n");
            for (String s : lines) {
                log.error(s);
            }

        }
        log.error("-----------------------------------------------------------------------------------------\n");
        log.error("");
    }

    private static void logError(Exception ex, Logger log){
        if (ex != null) {
            log.error(ex.getMessage());
        }
    }

    public static String decrypt(String str) {
        String encStr;
        if (isEncrypted(str)) {
            try {
                encStr = new EncryptionServiceImpl().decrypt(str);
            } catch (EncryptionException e) {
                encStr = "";
            }
            return encStr;
        } else {
            return str;
        }
    }

    public static String encrypt(String password) {
        String encPass;
        if (!isEncrypted(password)) {
            try {
                encPass = new EncryptionServiceImpl().encrypt(password);
            } catch (EncryptionException e) {
                encPass = "";
            }
            return encPass;
        } else {
            return password;
        }
    }

    public static boolean isEncrypted(String encryptStr) {
        try {
            new EncryptionServiceImpl().decrypt(encryptStr);
        } catch (EncryptionException e) {
            return false;
        }
        return true;
    }

    public static Integer resolveInt(String value, Logger log) {
        Integer inti = null;
        if (!StringUtils.isEmpty(value)) {
            try {
                inti = Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                log.warn("failed to parse integer value: " + value);
            }
        }
        return inti;
    }
}
