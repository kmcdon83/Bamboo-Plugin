package com.cx.plugin.utils;

import com.atlassian.bamboo.configuration.AdministrationConfiguration;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.spring.container.ContainerManager;
import com.cx.restclient.configuration.CxScanConfig;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import static com.cx.plugin.utils.CxParam.*;
import static com.cx.plugin.utils.CxPluginUtils.decrypt;
import static com.cx.plugin.utils.CxPluginUtils.resolveInt;

/**
 * Created by Galn on 25/10/2017.
 */
public class CxConfigHelper {
    private CxScanConfig scanConfig;
    private AdministrationConfiguration adminConfig;
    private boolean isIntervals;
    private String intervalBegins;
    private String intervalEnds;
    private CxLoggerAdapter log;

    public CxConfigHelper(CxLoggerAdapter log) {
        this.log = log;
    }

    public CxScanConfig resolveConfigurationMap(ConfigurationMap configMap, File workDir) throws TaskException {
        log.info("Resolving Cx configuration");

        Object a = ContainerManager.getComponent("administrationConfigurationAccessor");
        try {
            Method getAdminConfig = a.getClass().getDeclaredMethod("getAdministrationConfiguration");
            adminConfig = (AdministrationConfiguration) getAdminConfig.invoke(a);
        } catch (Exception e) {
            throw new TaskException("Failed to resolve global configuration", e);
        }

        scanConfig = new CxScanConfig();
        scanConfig.setCxOrigin(CX_ORIGIN);
        scanConfig.setSourceDir(workDir.getAbsolutePath());
        scanConfig.setReportsDir(new File(workDir + CX_REPORT_LOCATION));
        scanConfig.setSastEnabled(true);
        if (CUSTOM_CONFIGURATION_SERVER.equals(configMap.get(SERVER_CREDENTIALS_SECTION))) {
            scanConfig.setUrl(configMap.get(SERVER_URL));
            scanConfig.setUsername(configMap.get(USER_NAME));
            scanConfig.setPassword(decrypt(configMap.get(PASSWORD)));
        } else {
            scanConfig.setUrl(getAdminConfig(GLOBAL_SERVER_URL));
            scanConfig.setUsername(getAdminConfig(GLOBAL_USER_NAME));
            scanConfig.setPassword(decrypt(getAdminConfig(GLOBAL_PWD)));
        }

        scanConfig.setProjectName(configMap.get(PROJECT_NAME).trim());

        String presetId = configMap.get(PRESET_ID);
        if (!StringUtils.isNumeric(presetId)) {
            throw new TaskException("Invalid preset Id");
        }

        String teamName = configMap.get(TEAM_PATH_NAME);
        if (StringUtils.isEmpty(teamName)) {
            throw new TaskException("Invalid team path");
        }

        scanConfig.setPresetId(Integer.parseInt(presetId));
        scanConfig.setPresetName(StringUtils.defaultString(configMap.get(PRESET_NAME)));
        scanConfig.setTeamId(StringUtils.defaultString(configMap.get(TEAM_PATH_ID)));
        scanConfig.setTeamPath(teamName);

        if (CUSTOM_CONFIGURATION_CXSAST.equals(configMap.get(CXSAST_SECTION))) {
            scanConfig.setSastFolderExclusions(configMap.get(FOLDER_EXCLUSION));
            scanConfig.setSastFilterPattern(configMap.get(FILTER_PATTERN));
            scanConfig.setSastScanTimeoutInMinutes(resolveInt(configMap.get(SCAN_TIMEOUT_IN_MINUTES), log));

        } else {
            scanConfig.setSastFolderExclusions(getAdminConfig(GLOBAL_FOLDER_EXCLUSION));
            scanConfig.setSastFilterPattern(getAdminConfig(GLOBAL_FILTER_PATTERN));
            scanConfig.setSastScanTimeoutInMinutes(resolveInt(getAdminConfig(GLOBAL_SCAN_TIMEOUT_IN_MINUTES), log));
        }

        scanConfig.setScanComment(configMap.get(COMMENT));
        scanConfig.setIncremental(resolveBool(configMap, IS_INCREMENTAL));

        if (scanConfig.getIncremental()) {
            isIntervals = resolveBool(configMap, IS_INTERVALS);
            if (isIntervals) {
                intervalBegins = configMap.get(INTERVAL_BEGINS);
                intervalEnds = configMap.get(INTERVAL_ENDS);
                scanConfig = resolveIntervalFullScan(scanConfig);
            }
        }
        scanConfig.setGeneratePDFReport(resolveBool(configMap, GENERATE_PDF_REPORT));
        scanConfig.setOsaEnabled(resolveBool(configMap, OSA_ENABLED));
        scanConfig.setOsaArchiveIncludePatterns(configMap.get(OSA_ARCHIVE_INCLUDE_PATTERNS));
        scanConfig.setOsaFilterPattern(configMap.get(OSA_FILTER_PATTERNS));
        scanConfig.setOsaRunInstall(resolveBool(configMap, OSA_INSTALL_BEFORE_SCAN));

        if (CUSTOM_CONFIGURATION_CONTROL.equals(configMap.get(SCAN_CONTROL_SECTION))) {
            scanConfig.setSynchronous(resolveBool(configMap, IS_SYNCHRONOUS));
            scanConfig.setEnablePolicyViolations(resolveBool(configMap, POLICY_VIOLATION_ENABLED));
            scanConfig.setSastThresholdsEnabled(resolveBool(configMap, THRESHOLDS_ENABLED));
            scanConfig.setSastHighThreshold(resolveInt(configMap.get(HIGH_THRESHOLD), log));
            scanConfig.setSastMediumThreshold(resolveInt(configMap.get(MEDIUM_THRESHOLD), log));
            scanConfig.setSastLowThreshold(resolveInt(configMap.get(LOW_THRESHOLD), log));
            scanConfig.setOsaThresholdsEnabled(resolveBool(configMap, OSA_THRESHOLDS_ENABLED));
            scanConfig.setOsaHighThreshold(resolveInt(configMap.get(OSA_HIGH_THRESHOLD), log));
            scanConfig.setOsaMediumThreshold(resolveInt(configMap.get(OSA_MEDIUM_THRESHOLD), log));
            scanConfig.setOsaLowThreshold(resolveInt(configMap.get(OSA_LOW_THRESHOLD), log));
        } else {
            scanConfig.setSynchronous(resolveGlobalBool(GLOBAL_IS_SYNCHRONOUS));
            scanConfig.setEnablePolicyViolations(resolveGlobalBool(GLOBAL_POLICY_VIOLATION_ENABLED));
            scanConfig.setSastThresholdsEnabled(resolveGlobalBool(GLOBAL_THRESHOLDS_ENABLED));
            scanConfig.setSastHighThreshold(resolveInt(getAdminConfig(GLOBAL_HIGH_THRESHOLD), log));
            scanConfig.setSastMediumThreshold(resolveInt(getAdminConfig(GLOBAL_MEDIUM_THRESHOLD), log));
            scanConfig.setSastLowThreshold(resolveInt(getAdminConfig(GLOBAL_LOW_THRESHOLD), log));
            scanConfig.setOsaThresholdsEnabled(resolveGlobalBool(GLOBAL_OSA_THRESHOLDS_ENABLED));
            scanConfig.setOsaHighThreshold(resolveInt(getAdminConfig(GLOBAL_OSA_HIGH_THRESHOLD), log));
            scanConfig.setOsaMediumThreshold(resolveInt(getAdminConfig(GLOBAL_OSA_MEDIUM_THRESHOLD), log));
            scanConfig.setOsaLowThreshold(resolveInt(getAdminConfig(GLOBAL_OSA_LOW_THRESHOLD), log));
        }
        scanConfig.setDenyProject(resolveGlobalBool(GLOBAL_DENY_PROJECT));
        scanConfig.setHideResults(resolveGlobalBool(GLOBAL_HIDE_RESULTS));
        return scanConfig;
    }

    private boolean resolveBool(ConfigurationMap configMap, String value) {
        return OPTION_TRUE.equals(configMap.get(value));
    }

    private boolean resolveGlobalBool(String value) {
        return OPTION_TRUE.equals(getAdminConfig(value));
    }


    private CxScanConfig resolveIntervalFullScan(CxScanConfig scanConfig) {

        try {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

            final Calendar calendarBeginsHourMinute = Calendar.getInstance();
            calendarBeginsHourMinute.setTime(dateFormat.parse(intervalBegins));
            final Calendar calendarBegins = Calendar.getInstance();
            calendarBegins.set(Calendar.HOUR_OF_DAY, calendarBeginsHourMinute.get(Calendar.HOUR_OF_DAY));
            calendarBegins.set(Calendar.MINUTE, calendarBeginsHourMinute.get(Calendar.MINUTE));
            calendarBegins.set(Calendar.SECOND, 0);
            Date dateBegins = calendarBegins.getTime();

            final Calendar calendarEndsHourMinute = Calendar.getInstance();
            calendarEndsHourMinute.setTime(dateFormat.parse(intervalEnds));
            final Calendar calendarEnds = Calendar.getInstance();
            calendarEnds.set(Calendar.HOUR_OF_DAY, calendarEndsHourMinute.get(Calendar.HOUR_OF_DAY));
            calendarEnds.set(Calendar.MINUTE, calendarEndsHourMinute.get(Calendar.MINUTE));
            calendarEnds.set(Calendar.SECOND, 0);
            Date dateEnds = calendarEnds.getTime();

            final Date dateNow = Calendar.getInstance().getTime();

            if (dateBegins.after(dateEnds)) {
                if (dateBegins.after(dateNow)) {
                    calendarBegins.add(Calendar.DATE, -1);
                    dateBegins = calendarBegins.getTime();
                } else {
                    calendarEnds.add(Calendar.DATE, 1);
                    dateEnds = calendarEnds.getTime();
                }
            }

            if (dateNow.after(dateBegins) && dateNow.before(dateEnds)) {
                scanConfig.setForceScan(true);
            }

        } catch (final ParseException e) {
            log.error("Full scan interval parse exception");
        }
        return scanConfig;
    }


    private String getAdminConfig(String key) {
        return StringUtils.defaultString(adminConfig.getSystemProperty(key));
    }

    public String getPluginVersion() {
        String version = "";
        try {
            Properties properties = new Properties();
            java.io.InputStream is = getClass().getClassLoader().getResourceAsStream("english.properties");
            if (is != null) {
                properties.load(is);
                version = properties.getProperty("version");
            }
        } catch (Exception e) {

        }
        return version;
    }

    public boolean isIntervals() {
        return isIntervals;
    }

    public void setIntervals(boolean intervals) {
        isIntervals = intervals;
    }

    public String getIntervalBegins() {
        return intervalBegins;
    }

    public void setIntervalBegins(String intervalBegins) {
        this.intervalBegins = intervalBegins;
    }

    public String getIntervalEnds() {
        return intervalEnds;
    }

    public void setIntervalEnds(String intervalEnds) {
        this.intervalEnds = intervalEnds;
    }
}
