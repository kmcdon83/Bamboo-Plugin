package com.cx.plugin.task;

/**
 * Created by galn on 18/12/2016.
 */

import com.atlassian.bamboo.task.*;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.cx.plugin.dto.ScanResults;
import com.cx.plugin.utils.CxAppender;
import com.cx.plugin.utils.CxConfigHelper;
import com.cx.plugin.utils.CxLoggerAdapter;
import com.cx.restclient.CxShragaClient;
import com.cx.restclient.common.ShragaUtils;
import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.exception.CxClientException;
import com.cx.restclient.osa.dto.OSAResults;
import com.cx.restclient.sast.dto.SASTResults;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static com.cx.plugin.utils.CxParam.HTML_REPORT;
import static com.cx.plugin.utils.CxPluginUtils.printBuildFailure;
import static com.cx.plugin.utils.CxPluginUtils.printConfiguration;

public class CheckmarxTask implements TaskType {

    @NotNull
    public TaskResult execute(@NotNull final TaskContext taskContext) throws TaskException {
        CxLoggerAdapter log;
        CxShragaClient shraga = null;
        boolean sastCreated = false;
        boolean osaCreated = false;
        BuildContext buildContext = taskContext.getBuildContext();
        final TaskResultBuilder taskResultBuilder = TaskResultBuilder.newBuilder(taskContext);
        log = new CxLoggerAdapter(taskContext.getBuildLogger());

        try {
            //resolve configuration
            CxConfigHelper configHelper = new CxConfigHelper(log);
            CxScanConfig config = configHelper.resolveConfigurationMap(taskContext.getConfigurationMap(), taskContext.getWorkingDirectory());

            //print configuration
            printConfiguration(config, configHelper, log);

            if (!config.getSastEnabled() && !config.getOsaEnabled()) {
                log.error("Both SAST and OSA are disabled. exiting");
                //TODO run.setResult(Result.FAILURE);
            }
            //create scans and retrieve results (in jenkins agent)
            ScanResults ret = new ScanResults(new SASTResults(), new OSAResults());


            //initialize cx client
            try {
                shraga = new CxShragaClient(config, log);
                shraga.init();
            } catch (Exception ex) {
                if (ex.getMessage().contains("Server is unavailable")) {
                    try {
                        shraga.login();
                    } catch (CxClientException e) {
                        throw new TaskException(e.getMessage());
                    }
                    String errorMsg = "Connection Failed.\n" +
                            "Validate the provided login credentials and server URL are correct.\n" +
                            "In addition, make sure the installed plugin version is compatible with the CxSAST version according to CxSAST release notes.\n" +
                            "Error: " + ex.getMessage();
                    throw new TaskException(ex.getMessage() + ": " + errorMsg);
                }
                throw new TaskException(ex.getMessage(), ex);
            }

            if (config.getSastEnabled()) {
                try {
                    shraga.createSASTScan();
                    sastCreated = true;
                } catch (IOException | CxClientException e) {
                    ret.setSastCreateException(e);
                    log.error(e.getMessage());
                }
            }

            if (config.getOsaEnabled()) {
                //---------------------------
                //we do this in order to redirect the logs from the filesystem agent component to the build console
                String appenderName = "cxAppender_" + buildContext.getBuildKey().getKey();
                Logger.getRootLogger().addAppender(new CxAppender(taskContext.getBuildLogger(), appenderName));
                //---------------------------

                try {
                    shraga.createOSAScan();
                    osaCreated = true;
                } catch (CxClientException | IOException e) {
                    ret.setOsaCreateException(e);
                    log.error(e.getMessage());
                } finally {
                    Logger.getRootLogger().removeAppender(appenderName);
                }
            }

            //Asynchronous MODE
            if (!config.getSynchronous()) {
                log.info("Running in Asynchronous mode. Not waiting for scan to finish");
                String summaryStr = shraga.generateHTMLSummary();
                ret.getSummary().put(HTML_REPORT, summaryStr);

                if (ret.getSastCreateException() != null || ret.getOsaCreateException() != null) {
                    printBuildFailure(null, ret, log);
                    return taskResultBuilder.failed().build();
                }

                return taskResultBuilder.success().build();
            }

            if (sastCreated) {
                try {
                    SASTResults sastResults = shraga.waitForSASTResults();
                    ret.setSastResults(sastResults);
                } catch (CxClientException | IOException e) {
                    ret.setSastWaitException(e);
                    log.error(e.getMessage());
                }
            }

            if (osaCreated) {
                try {
                    OSAResults osaResults = shraga.waitForOSAResults();
                    ret.setOsaResults(osaResults);
                } catch (CxClientException | IOException e) {
                    ret.setOsaWaitException(e);
                    log.error(e.getMessage());
                }
            }

            if (config.getEnablePolicyViolations()) {
                shraga.printIsProjectViolated();
            }
            if (!config.getHideResults()) {
                String summaryStr = shraga.generateHTMLSummary();
                ret.getSummary().put(HTML_REPORT, summaryStr);
                buildContext.getBuildResult().getCustomBuildData().putAll(ret.getSummary());
            }

            //assert if expected exception is thrown  OR when vulnerabilities under threshold OR when policy violated
            String buildFailureResult = ShragaUtils.getBuildFailureResult(config, ret.getSastResults(), ret.getOsaResults());
            if (!StringUtils.isEmpty(buildFailureResult) || ret.getSastWaitException() != null || ret.getSastCreateException() != null ||
                    ret.getOsaCreateException() != null || ret.getOsaWaitException() != null)
            {
                printBuildFailure(buildFailureResult, ret, log);
                return taskResultBuilder.failed().build();
            }
            return taskResultBuilder.success().build();
        } catch (InterruptedException e) {
            log.error("Interrupted exception: " + e.getMessage(), e);

            if (shraga != null && sastCreated) {
                log.error("Canceling scan on the Checkmarx server...");
                cancelScan(shraga);
            }
            throw new TaskException(e.getMessage());

        } catch (Exception e) {
            log.error("Unexpected exception: " + e.getMessage(), e);
            throw new TaskException(e.getMessage());
        } finally {
            if (shraga != null) {
                shraga.close();
            }
        }
    }

    private void cancelScan(CxShragaClient shraga) {
        try {
            shraga.cancelSASTScan();
        } catch (Exception ignored) {
        }
    }
}