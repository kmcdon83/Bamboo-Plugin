package com.cx.plugin.utils;

import java.io.File;

/**
 * Created by galn
 * Date: 22/09/2016.
 */

public class CxParam {
    public static final String CX_REPORT_LOCATION = File.separator + "Checkmarx" + File.separator + "Reports";

    public static final String CX_ORIGIN = "Bamboo";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String SERVER_URL = "serverUrl";
    public static final String PROJECT_NAME = "projectName";
    public static final String PRESET_ID = "presetId";
    public static final String PRESET_NAME = "presetName";
    public static final String PRESET_LIST = "presetList";
    public static final String TEAM_PATH_ID = "teamPathId";
    public static final String TEAM_PATH_NAME = "teamPathName";
    public static final String TEAM_PATH_LIST = "teamPathList";
    public static final String FOLDER_EXCLUSION = "folderExclusions";
    public static final String FILTER_PATTERN = "filterPatterns";
    public static final String SCAN_TIMEOUT_IN_MINUTES = "scanTimeoutInMinutes";
    public static final String COMMENT = "comment";
    public static final String IS_INCREMENTAL = "isIncremental";
    public static final String IS_INTERVALS = "isIntervals";
    public static final String FORCE_FULL_SCAN = "forceFullScan";
    public static final String INTERVAL_BEGINS = "intervalBegins";
    public static final String INTERVAL_ENDS = "intervalEnds";
    public static final String INTERVAL_BEGINS_LIST = "intervalBeginsList";
    public static final String INTERVAL_ENDS_LIST = "intervalEndsList";
    public static final String GENERATE_PDF_REPORT = "generatePDFReport";
    public static final String IS_SYNCHRONOUS = "isSynchronous";
    public static final String THRESHOLDS_ENABLED = "thresholdsEnabled";
    public static final String HIGH_THRESHOLD = "highThreshold";
    public static final String MEDIUM_THRESHOLD = "mediumThreshold";
    public static final String LOW_THRESHOLD = "lowThreshold";
    public static final String POLICY_VIOLATION_ENABLED = "enablePolicyViolations";
    public static final String OSA_ENABLED = "osaEnabled";
    public static final String OSA_FILTER_PATTERNS = "cxOsaFilterPatterns";
    public static final String OSA_ARCHIVE_INCLUDE_PATTERNS = "cxOsaArchiveIncludePatterns";
    public static final String OSA_INSTALL_BEFORE_SCAN = "cxOsaInstallBeforeScan";
    public static final String OSA_THRESHOLDS_ENABLED = "osaThresholdsEnabled";
    public static final String OSA_HIGH_THRESHOLD = "osaHighThreshold";
    public static final String OSA_MEDIUM_THRESHOLD = "osaMediumThreshold";
    public static final String OSA_LOW_THRESHOLD = "osaLowThreshold";
    public static final String SERVER_CREDENTIALS_SECTION = "serverCredentialsSection";
    public static final String CXSAST_SECTION = "cxSastSection";
    public static final String SCAN_CONTROL_SECTION = "scanControlSection";
    public static final String ADMINISTRATION_CONFIGURATION = "administrationConfiguration";
    public static final String GLOBAL_CONFIGURATION_SERVER = "globalConfigurationServer";
    public static final String CUSTOM_CONFIGURATION_SERVER = "customConfigurationServer";
    public static final String GLOBAL_CONFIGURATION_CXSAST = "globalConfigurationCxSAST";
    public static final String CUSTOM_CONFIGURATION_CXSAST = "customConfigurationCxSAST";
    public static final String GLOBAL_CONFIGURATION_CONTROL = "globalConfigurationControl";
    public static final String CUSTOM_CONFIGURATION_CONTROL = "customConfigurationControl";
    public static final String NO_TEAM_PATH = "noTeamPath";
    public static final String NO_PRESET = "noPreset";
    public static final int NO_PRESET_ID = -1;
    public final static String NO_PRESET_MESSAGE = "Unable to connect to server. Make sure URL and Credentials are valid to see presets list";
    public final static String NO_TEAM_MESSAGE = "Unable to connect to server. Make sure URL and Credentials are valid to see teams list";
    public final static String ERROR_OCCURRED = "errorOccurred";
    public final static String HTML_REPORT = "htmlReport";

    public final static String ERROR_OCCURRED_MESSAGE = "Configuration settings were not saved. Please review your settings and try again";

    public static final String OPTION_TRUE = "true";
    public static final String OPTION_FALSE = "false";

    public static final String GLOBAL_USER_NAME = "globalUsername";
    public static final String GLOBAL_PWD = "globalPss";
    public static final String GLOBAL_SERVER_URL = "globalServerUrl";

    public static final String GLOBAL_FOLDER_EXCLUSION = "globalFolderExclusions";
    public static final String GLOBAL_FILTER_PATTERN = "globalFilterPatterns";
    public static final String GLOBAL_SCAN_TIMEOUT_IN_MINUTES = "globalScanTimeoutInMinutes";
    public static final String GLOBAL_IS_SYNCHRONOUS = "globalIsSynchronous";
    public static final String GLOBAL_THRESHOLDS_ENABLED = "globalThresholdsEnabled";
    public static final String GLOBAL_HIGH_THRESHOLD = "globalHighThreshold";
    public static final String GLOBAL_MEDIUM_THRESHOLD = "globalMediumThreshold";
    public static final String GLOBAL_LOW_THRESHOLD = "globalLowThreshold";
    public static final String GLOBAL_OSA_THRESHOLDS_ENABLED = "globalOsaThresholdsEnabled";
    public static final String GLOBAL_OSA_HIGH_THRESHOLD = "globalOsaHighThreshold";
    public static final String GLOBAL_OSA_MEDIUM_THRESHOLD = "globalOsaMediumThreshold";
    public static final String GLOBAL_OSA_LOW_THRESHOLD = "globalOsaLowThreshold";
    public static final String GLOBAL_DENY_PROJECT = "globalDenyProject";
    public static final String IS_GLOBAL_DENY_PROJECT = "isglobalDenyProject";
    public static final String IS_GLOBAL_HIDE_RESULTS = "isglobalHideResults";
    public static final String GLOBAL_HIDE_RESULTS = "globalHideResults";
    public static final String GLOBAL_POLICY_VIOLATION_ENABLED = "globalEnablePolicyViolations";
    public static final String DEFAULT_FILTER_PATTERNS = "!**/_cvs/**/*, !**/.svn/**/*,   !**/.hg/**/*,   !**/.git/**/*,  !**/.bzr/**/*, !**/bin/**/*," +
            "!**/obj/**/*,  !**/backup/**/*, !**/.idea/**/*, !**/*.DS_Store, !**/*.ipr,     !**/*.iws,   " +
            "!**/*.bak,     !**/*.tmp,       !**/*.aac,      !**/*.aif,      !**/*.iff,     !**/*.m3u,   !**/*.mid,   !**/*.mp3,  " +
            "!**/*.mpa,     !**/*.ra,        !**/*.wav,      !**/*.wma,      !**/*.3g2,     !**/*.3gp,   !**/*.asf,   !**/*.asx,  " +
            "!**/*.avi,     !**/*.flv,       !**/*.mov,      !**/*.mp4,      !**/*.mpg,     !**/*.rm,    !**/*.swf,   !**/*.vob,  " +
            "!**/*.wmv,     !**/*.bmp,       !**/*.gif,      !**/*.jpg,      !**/*.png,     !**/*.psd,   !**/*.tif,   !**/*.swf,  " +
            "!**/*.jar,     !**/*.zip,       !**/*.rar,      !**/*.exe,      !**/*.dll,     !**/*.pdb,   !**/*.7z,    !**/*.gz,   " +
            "!**/*.tar.gz,  !**/*.tar,       !**/*.gz,       !**/*.ahtm,     !**/*.ahtml,   !**/*.fhtml, !**/*.hdm,   " +
            "!**/*.hdml,    !**/*.hsql,      !**/*.ht,       !**/*.hta,      !**/*.htc,     !**/*.htd,   !**/*.war,   !**/*.ear,  " +
            "!**/*.htmls,   !**/*.ihtml,     !**/*.mht,      !**/*.mhtm,     !**/*.mhtml,   !**/*.ssi,   !**/*.stm,   " +
            "!**/*.stml,    !**/*.ttml,      !**/*.txn,      !**/*.xhtm,     !**/*.xhtml,   !**/*.class, !**/*.iml,   !**/Checkmarx/Reports/**/* , !**/node_modules/**/*";

    public static final String DEFAULT_OSA_ARCHIVE_INCLUDE_PATTERNS = "*.zip, *.tgz, *.war, *.ear";

    public static final String CONNECTION_FAILED_COMPATIBILITY = "Connection Failed.\n" +
            "Validate the provided login credentials and server URL are correct.\n" +
            "In addition, make sure the installed plugin version is compatible with the CxSAST version according to CxSAST release notes.";
}