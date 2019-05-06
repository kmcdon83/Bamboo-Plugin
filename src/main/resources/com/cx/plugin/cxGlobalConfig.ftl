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
        max-width: 225px;
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


</style>



[@ww.form action="checkmarxDefaultConfiguration!save.action" method="post" submitLabelKey="cxDefaultConfigSubmit.label" titleKey="cxDefaultConfigTitle.label" cssClass="top-label"]
    [@ui.bambooSection title='Checkmarx Server' cssClass="cx center"]
        [@ww.textfield labelKey="serverUrl.label" name="globalServerUrl"/]
        [@ww.textfield labelKey="username.label" name="globalUsername"/]
        [@ww.password labelKey="password.label" name="globalPss" showPassword='true' /]

    <button type="button" class="aui-button test-connection" id="g_test_connection" onclick="connectToServer()">Connect to Server</button>
    <div id="gtestConnectionMessage" class="test-connection-message"></div>
    [/@ui.bambooSection]

    [@ui.bambooSection title='Checkmarx Scan CxSAST' cssClass="cx center"]
        [@ww.textfield labelKey="folderExclusions.label" name="globalFolderExclusions" descriptionKey="folderExclusions.description"  cssClass="long-field"/]
        [@ww.textarea labelKey="filterPatterns.label" name="globalFilterPatterns" rows="4"  cssClass="long-field"/]
        [@ww.textfield labelKey="scanTimeoutInMinutes.label" name="globalScanTimeoutInMinutes" required='false'/]
    [/@ui.bambooSection]

    [@ui.bambooSection title='Control Checkmarx Scan' cssClass="cx center"]

        [@ww.checkbox labelKey="isSynchronous.label" name="globalIsSynchronous" descriptionKey="isSynchronous.description" toggle='true' /]

        [@ui.bambooSection dependsOn='globalIsSynchronous' showOn='true']
            [@ww.checkbox labelKey="enablePolicyViolations.label" name="globalEnablePolicyViolations" descriptionKey="enablePolicyViolations.description" toggle='true' /]
            [@ww.checkbox labelKey="thresholdsEnabled.label" name="globalThresholdsEnabled" descriptionKey="thresholdsEnabled.description" toggle='true' /]
            [@ui.bambooSection dependsOn='globalThresholdsEnabled' showOn='true']
                [@ww.textfield labelKey="sastHighThreshold.label" name="globalHighThreshold" required='false'/]
                [@ww.textfield labelKey="sastMediumThreshold.label" name="globalMediumThreshold" required='false'/]
                [@ww.textfield labelKey="sastLowThreshold.label" name="globalLowThreshold" required='false'/]
            [/@ui.bambooSection]

            [@ww.checkbox labelKey="osaThresholdsEnabled.label" name="globalOsaThresholdsEnabled" descriptionKey="thresholdsEnabled.description" toggle='true' /]
            [@ui.bambooSection dependsOn='globalOsaThresholdsEnabled' showOn='true']
                [@ww.textfield labelKey="osaHighThreshold.label" name="globalOsaHighThreshold" required='false'/]
                [@ww.textfield labelKey="osaMediumThreshold.label" name="globalOsaMediumThreshold" required='false'/]
                [@ww.textfield labelKey="osaLowThreshold.label" name="globalOsaLowThreshold" required='false'/]
            [/@ui.bambooSection]
        [/@ui.bambooSection]

        [@ww.checkbox labelKey="globalDenyProject.label" name="globalDenyProject" descriptionKey="globalDenyProject.description" /]
        [@ww.checkbox labelKey="globalHideResults.label" name="globalHideResults" descriptionKey="globalHideResults.description" toggle='true' /]

    [/@ui.bambooSection]
[/@ww.form]

<script>
    function connectToServer() {
        document.getElementById("gtestConnectionMessage").innerHTML = "";
            restRequest();
    }

        function restRequest() {
            var request;

            var url = document.getElementById("checkmarxDefaultConfiguration_globalServerUrl").value;
            var username = document.getElementById("checkmarxDefaultConfiguration_globalUsername").value;
            var pas = document.getElementById("checkmarxDefaultConfiguration_globalPss").value;

            if (!validateGlobalFields()) {
                return;
            }
            request = JSON.stringify(getGlobalInputData());

            function createRestRequest(method, url) {

                var resolvedUrl = AJS.contextPath() + url;

                var xhr = new XMLHttpRequest();
                if ("withCredentials" in xhr) {
                    xhr.open(method, resolvedUrl, true);

                } else if (typeof XDomainRequest != "undefined") {
                    xhr = new XDomainRequest();
                    xhr.open(method, resolvedUrl);
                } else {
                    xhr = null;
                }
                return xhr;
            }

            var xhr = createRestRequest("POST", "/rest/checkmarx/1.0/test/connection");
            if (!xhr) {
                console.log("Request Failed");
                return;
            }

            xhr.onload = function () {
                var parsed = JSON.parse(xhr.responseText);
                var message = document.getElementById("gtestConnectionMessage");
                if (xhr.status == 200) {
                    message.style.color = "green";
                }
                else {
                    message.style.color = "#d22020"
                }
                message.innerHTML = parsed.loginResponse;
            };


            xhr.onerror = function () {
                console.log('There was an error!');
            };

            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.send(request);


            function validateGlobalFields() {

                var messageElement = document.getElementById("gtestConnectionMessage");
                if (url.length < 1) {
                    messageElement.textContent = 'URL must not be empty';
                    messageElement.style.color = "#d22020";
                    return false;
                } else if (username.length < 1) {
                    messageElement.textContent = "Username must not be empty";
                    messageElement.style.color = "#d22020";
                    return false;
                } else if (pas.length < 1) {
                    messageElement.textContent = "Password must not be empty";
                    messageElement.style.color = "#d22020";
                    return false;
                }
                return true;
            }

            function getGlobalInputData() {
                return {
                    "url": url,
                    "username": username,
                    "pas": pas
                };
            }

        }
</script>

