<style type="text/css" xmlns="http://www.w3.org/1999/html">

    form.aui .description {
        width: 90%;
    }

    .cx.center {
        padding: 16px;
        border: solid 1px;
        width: 90%;
        margin-top: 10px;
        margin-bottom: 25px;
        min-width: 500px;
        max-width: 820px;
        box-sizing: border-box;
    }

    input#radioGroupcustomConfigurationServer, input#radioGroupglobalConfigurationServer, input#radioGroupglobalConfigurationCxSAST, input#radioGroupcustomConfigurationCxSAST, input#radioGroupglobalConfigurationControl, input#radioGroupcustomConfigurationControl {
        width: 14px;
    }

    form.aui.top-label .field-group > label {
        margin: 5px 0;
        font-weight: bold;
    }

    form.aui .radio {
        width: 43%;
        display: inline-block;
    }

    form.aui .checkbox > label, form.aui .radio > label {
        color: #333333;
        font-weight: bold;
    }

    .aui-page-focused .aui-page-panel-content h3:not(.collapsible-header) {
        margin-top: 0;
    }

    h3 {
        color: #3b73af;
        font-size: 17px;
    }

    form.aui .field-value {
        border-radius: 3.01px;
        max-width: 60%;
        max-height: 17px;
        width: 108%;
        border: 1px solid #cccccc;
        padding-bottom: 6px;
        display: inline-block;
        background-color: #f1f1f1;
        font-weight: bold;
        min-height: 13px;
        padding-top: 5px;
        padding-left: 10px;
    }

    form.aui .text{
        max-width: 60%;
    }


    #panel-editor-config form.aui .long-field {
        max-width: 600px;
        width: 90%;
        max-height: 374px;
        min-height: 12px;
    }

    .aui-button.test-connection {
        margin: 10px 0;
    }

    #spinner {
        display: none;
        position: absolute;
        left: 50%;
        top: 20%;
    }

    #errorOccurred {
        display: none;
    }

</style>
[@ww.textfield labelKey="" id="errorOccurred" name="errorOccurred"  required='false' /]
[@ui.bambooSection title='Checkmarx Server' cssClass="cx center"]
    [@ww.radio id = 'radioGroup' name='serverCredentialsSection' listKey='key' listValue='value' toggle='true' list=configurationModeTypesServer /]

    [@ui.bambooSection dependsOn='serverCredentialsSection' showOn='customConfigurationServer']
        [@ww.textfield labelKey="serverUrl.label" id="serverUrl" name="serverUrl"  required='true' /]
        [@ww.textfield labelKey="username.label"  id="username" name="username" required='true'/]
        [@ww.password labelKey="password.label"  id="password" name="password" showPassword='true' required='true'/]
    <button type="button" class="aui-button test-connection" id="test_connection">Connect to Server</button>
    <div id="testConnectionMessage" class="test-connection-message"></div>


    [/@ui.bambooSection]
    [@ui.bambooSection dependsOn='serverCredentialsSection' showOn='globalConfigurationServer']
        [@ww.label labelKey="serverUrl.label"  id="globalServerUrl" name="globalServerUrl"/]
        [@ww.label labelKey="username.label" id="globalUsername" name="globalUsername" /]
        [@ww.label type="password" labelKey="password.label"/]
    [/@ui.bambooSection]

    [@ww.textfield labelKey="projectName.label" name="projectName" required='true' descriptionKey='projectName.description' /]
    [@ww.select labelKey="preset.label" name="presetId" id="presetListId" list="presetList" listKey="key" listValue="value" multiple="false"  cssClass="long-field" descriptionKey="preset.description"/]
    [@ww.select labelKey="teamPath.label" name="teamPathId" id="teamPathListId" list="teamPathList" listKey="key" listValue="value" multiple="false"  cssClass="long-field" descriptionKey="teamPath.description"/]
[/@ui.bambooSection]


[@ui.bambooSection title='Checkmarx Scan CxSAST' cssClass="cx center"]
    [@ww.radio id = 'radioGroup' name='cxSastSection' listKey='key' listValue='value' toggle='true' list=configurationModeTypesCxSAST /]

    [@ui.bambooSection dependsOn='cxSastSection' showOn='customConfigurationCxSAST']
        [@ww.textfield labelKey="folderExclusions.label" name="folderExclusions" descriptionKey="folderExclusions.description" cssClass="long-field"/]
        [@ww.textarea labelKey="filterPatterns.label" name="filterPatterns" rows="4" descriptionKey='Comma separated list of include or exclude wildcard patterns. Exclude patterns start with exclamation mark "!". Example: **/*.java, **/*.html, !**\\test\\**\\XYZ*"' cssClass="long-field"/]
        [@ww.textfield labelKey="scanTimeoutInMinutes.label"  name="scanTimeoutInMinutes" descriptionKey="scanTimeoutInMinutes.description"/]
    [/@ui.bambooSection]

    [@ui.bambooSection dependsOn='cxSastSection' showOn='globalConfigurationCxSAST']
        [@ww.label labelKey="folderExclusions.label" name="globalFolderExclusions" descriptionKey="folderExclusions.description" cssClass="long-field"/]
        [@ww.label labelKey="filterPatterns.label" name="globalFilterPatterns" rows="4" cssClass="long-field"/]
        [@ww.label labelKey="scanTimeoutInMinutes.label" name="globalScanTimeoutInMinutes"/]

    [/@ui.bambooSection]

    [@ww.textarea labelKey="comment.label"  name="comment" rows="3" descriptionKey="comment.description" cssClass="long-field"/]

    [@ww.checkbox labelKey="isIncremental.label" name="isIncremental" descriptionKey="isIncremental.description" toggle='true' /]
    [@ui.bambooSection dependsOn="isIncremental" showOn="true"]
        [@ww.checkbox labelKey="isIntervals.label" name="isIntervals" descriptionKey="isIntervals.description" toggle="true"/]
        [@ui.bambooSection dependsOn="isIntervals" showOn="true"]
            [@ww.select labelKey="intervalBegins.label" name="intervalBegins" list="intervalBeginsList" listKey="key" listValue="value" multiple="false"/]
            [@ww.select labelKey="intervalEnds.label" name="intervalEnds" list="intervalEndsList" listKey="key" listValue="value" multiple="false"/]
        [/@ui.bambooSection]
    [/@ui.bambooSection]


    [@ww.checkbox labelKey="generatePDFReport.label" name="generatePDFReport" toggle='false' descriptionKey='generatePDFReport.description'/]
[/@ui.bambooSection]


[@ui.bambooSection title='Checkmarx Scan CxOSA' cssClass="cx center"]
<p class="description">
    <small>
        Open Source Analysis (OSA) helps you manage the security risk involved in using open
        source libraries in your applications
    </small>
</p>
    [@ww.checkbox labelKey="osaEnabled.label" name="osaEnabled" descriptionKey="osaEnabled.description" toggle='true' /]
    [@ui.bambooSection dependsOn="osaEnabled" showOn="true"]
        [@ww.textarea labelKey="cxOsaFilterPatterns.label" name="cxOsaFilterPatterns" descriptionKey="cxOsaFilterPatterns.description" rows="4" cssClass="long-field"/]
        [@ww.textfield labelKey="cxOsaArchiveIncludePatterns.label" name="cxOsaArchiveIncludePatterns" descriptionKey="cxOsaArchiveIncludePatterns.description"/]
        [@ww.checkbox labelKey="cxOsaInstallBeforeScan.label" name="cxOsaInstallBeforeScan" descriptionKey="cxOsaInstallBeforeScan.description" toggle='true' /]
    [/@ui.bambooSection]
[/@ui.bambooSection]


[@ui.bambooSection title='Control Checkmarx Scan' cssClass="cx center"]
<p class="description">
    <small>Controls the scan mode (synchrnous or asynchronous) and the build results threshold.
        The thresholds will define the minimal criteria to fail the build.
    </small>
</p>
    [@ww.radio id  = 'radioGroup' name='scanControlSection' listKey='key' listValue='value' toggle='true' list=configurationModeTypesControl /]
    [@ui.bambooSection dependsOn='scanControlSection' showOn='customConfigurationControl']
        [@ww.checkbox labelKey="isSynchronous.label" name="isSynchronous" descriptionKey="isSynchronous.description" toggle='true' /]

        [@ui.bambooSection dependsOn='isSynchronous' showOn='true']

            [@ww.checkbox labelKey="enablePolicyViolations.label" name="enablePolicyViolations" descriptionKey="enablePolicyViolations.description" toggle='true' /]

            [@ww.checkbox labelKey="thresholdsEnabled.label" name="thresholdsEnabled" descriptionKey="thresholdsEnabled.description" toggle='true' /]
            [@ui.bambooSection dependsOn='thresholdsEnabled' showOn='true']

                [@ww.textfield labelKey="sastHighThreshold.label" name="highThreshold"/]
                [@ww.textfield labelKey="sastMediumThreshold.label" name="mediumThreshold"/]
                [@ww.textfield labelKey="sastLowThreshold.label" name="lowThreshold" /]
            [/@ui.bambooSection]

            [@ui.bambooSection dependsOn='thresholdsEnabled' showOn='false']
                [@ww.label labelKey="sastHighThreshold.label"/]
                [@ww.label labelKey="sastMediumThreshold.label" /]
                [@ww.label labelKey="sastLowThreshold.label"/]
            [/@ui.bambooSection]

            [@ui.bambooSection dependsOn='osaEnabled' showOn='true']
                [@ww.checkbox labelKey="osaThresholdsEnabled.label" name="osaThresholdsEnabled"  descriptionKey="thresholdsEnabled.description"toggle='true' /]

                [@ui.bambooSection dependsOn='osaThresholdsEnabled' showOn='true']
                    [@ww.textfield labelKey="osaHighThreshold.label" name="osaHighThreshold"/]
                    [@ww.textfield labelKey="osaMediumThreshold.label" name="osaMediumThreshold"/]
                    [@ww.textfield labelKey="osaLowThreshold.label" name="osaLowThreshold" /]
                [/@ui.bambooSection]

                [@ui.bambooSection dependsOn='osaThresholdsEnabled' showOn='false']
                    [@ww.label labelKey="osaHighThreshold.label" /]
                    [@ww.label labelKey="osaMediumThreshold.label" /]
                    [@ww.label labelKey="osaLowThreshold.label"  /]
                [/@ui.bambooSection]

            [/@ui.bambooSection]

            [@ui.bambooSection dependsOn='osaEnabled' showOn='false']
                [@ww.checkbox labelKey="osaThresholdsEnabled.label" name="osaThresholdsEnabled"  descriptionKey="thresholdsEnabled.description"toggle='true' disabled="true" checked='false'  /]

                [@ww.label labelKey="osaHighThreshold.label"/]
                [@ww.label labelKey="osaMediumThreshold.label"/]
                [@ww.label labelKey="osaLowThreshold.label"  /]

            [/@ui.bambooSection]

        [/@ui.bambooSection]

        [@ui.bambooSection dependsOn='isSynchronous' showOn='false']
            [@ww.checkbox labelKey="thresholdsEnabled.label" name="thresholdsEnabled" descriptionKey="thresholdsEnabled.description" toggle='true' disabled="true" checked='false' /]
            [@ww.label labelKey="sastHighThreshold.label"/]
            [@ww.label labelKey="sastMediumThreshold.label"  /]
            [@ww.label labelKey="sastLowThreshold.label" /]
            [@ww.checkbox labelKey="osaThresholdsEnabled.label" name="osaThresholdsEnabled"  descriptionKey="thresholdsEnabled.description"toggle='true' disabled="true" checked='false' /]
            [@ww.label labelKey="osaHighThreshold.label" /]
            [@ww.label labelKey="osaMediumThreshold.label" /]
            [@ww.label labelKey="osaLowThreshold.label" /]
        [/@ui.bambooSection]

    [/@ui.bambooSection]

    [@ui.bambooSection dependsOn='scanControlSection' showOn='globalConfigurationControl']
        [#if (globalIsSynchronous.attribute)??]
            [@ww.checkbox labelKey="isSynchronous.label" name="globalIsSynchronous" descriptionKey="isSynchronous.description" toggle='true' disabled="true" checked='true' /]
            [#if (globalEnablePolicyViolations.attribute)??]
                [@ww.checkbox labelKey="enablePolicyViolations.label" name="globalEnablePolicyViolations" descriptionKey="enablePolicyViolations.description" toggle='true' disabled="true" checked='true' /]

            [#else]
                [@ww.checkbox labelKey="enablePolicyViolations.label" name="globalEnablePolicyViolations" descriptionKey="enablePolicyViolations.description" toggle='true' disabled="true" /]
            [/#if]
            [#if (globalThresholdsEnabled.attribute)??]
                [@ww.checkbox labelKey="thresholdsEnabled.label" name="globalThresholdsEnabled" descriptionKey="thresholdsEnabled.description" toggle='true' disabled="true" checked='true' /]
                [@ww.label labelKey="sastHighThreshold.label" name="globalHighThreshold" /]
                [@ww.label labelKey="sastMediumThreshold.label" name="globalMediumThreshold" /]
                [@ww.label labelKey="sastLowThreshold.label" name="globalLowThreshold" /]
            [#else]
                [@ww.checkbox labelKey="thresholdsEnabled.label" name="globalThresholdsEnabled" descriptionKey="thresholdsEnabled.description" toggle='true' disabled="true" /]
                [@ww.label labelKey="sastHighThreshold.label"/]
                [@ww.label labelKey="sastMediumThreshold.label"/]
                [@ww.label labelKey="sastLowThreshold.label"/]
            [/#if]
            [#if (globalOsaThresholdsEnabled.attribute)??]
                [@ww.checkbox labelKey="osaThresholdsEnabled.label" name="globalOsaThresholdsEnabled"  descriptionKey="thresholdsEnabled.description"toggle='true' disabled="true" checked='true' /]
                [@ww.label labelKey="osaHighThreshold.label" name="globalOsaHighThreshold"/]
                [@ww.label labelKey="osaMediumThreshold.label" name="globalOsaMediumThreshold" /]
                [@ww.label labelKey="osaLowThreshold.label" name="globalOsaLowThreshold" /]
            [#else]
                [@ww.checkbox labelKey="osaThresholdsEnabled.label" name="globalOsaThresholdsEnabled" descriptionKey="thresholdsEnabled.description" toggle='true' disabled="true" /]
                [@ww.label labelKey="osaHighThreshold.label" /]
                [@ww.label labelKey="osaMediumThreshold.label" /]
                [@ww.label labelKey="osaLowThreshold.label"/]
            [/#if]
        [#else]
            [@ww.checkbox labelKey="isSynchronous.label" name="globalIsSynchronous" descriptionKey="isSynchronous.description" toggle='true' disabled="true" checked='false'/]
            [@ww.checkbox labelKey="enablePolicyViolations.label" name="globalEnablePolicyViolations" descriptionKey="enablePolicyViolations.description" toggle='true' disabled="true" checked='false'/]
            [@ww.checkbox labelKey="thresholdsEnabled.label" name="globalThresholdsEnabled" descriptionKey="thresholdsEnabled.description" toggle='true' disabled="true" checked='false'/]
            [@ww.checkbox labelKey="osaThresholdsEnabled.label" name="globalThresholdsEnabled" descriptionKey="thresholdsEnabled.description" toggle='true' disabled="true" checked='false'/]
        [/#if]
        [#if (globalDenyProject.attribute)??]
            [@ww.checkbox labelKey="isglobalDenyProject.label" name="globalDenyProject" descriptionKey="isglobalDenyProject.description" toggle='true' disabled="true" checked='true' /]
        [#else]
            [@ww.checkbox labelKey="isglobalDenyProject.label" name="globalDenyProject" descriptionKey="isglobalDenyProject.description" toggle='true' disabled="true" checked='false'/]
        [/#if]
        [#if (globalHideResults.attribute)??]
            [@ww.checkbox labelKey="isglobalHideResults.label" name="globalHideResults" descriptionKey="isglobalHideResults.description" toggle='true' disabled="true" checked='true'/]
        [#else]
            [@ww.checkbox labelKey="isglobalHideResults.label" name="globalHideResults" descriptionKey="isglobalHideResults.description" toggle='true' disabled="true" checked='false' /]
        [/#if]
    [/@ui.bambooSection]

[/@ui.bambooSection]

