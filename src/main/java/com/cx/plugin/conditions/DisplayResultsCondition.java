package com.cx.plugin.conditions;

/**
 * Created by Galn on 07/06/2017.
 */

import com.atlassian.bamboo.plan.cache.ImmutableBuildable;
import com.atlassian.bamboo.plan.cache.ImmutableChain;
import com.atlassian.bamboo.plan.cache.ImmutableJob;
import com.atlassian.bamboo.plan.cache.ImmutablePlan;
import com.atlassian.bamboo.resultsummary.AbstractResultsSummary;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.task.TaskIdentifier;
import com.atlassian.plugin.web.Condition;
import com.atlassian.util.concurrent.Nullable;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.Map;

public class DisplayResultsCondition implements Condition {

    public DisplayResultsCondition() {
    }

    @Override
    public void init(final Map<String, String> configParams) {
    }

    @Override
    public boolean shouldDisplay(final Map<String, Object> context) {

        AbstractResultsSummary a = (AbstractResultsSummary) context.get("resultSummary");
        boolean buildFinished = "Finished".equals(a.getLifeCycleState().toString());

        return buildFinished && hasCxTask(((ImmutablePlan) context.get("plan")), new IsCxTaskPredicate<TaskDefinition>());
    }


    public static boolean hasCxTask(ImmutablePlan plan, Predicate<TaskDefinition> predicate) {
        if (plan instanceof ImmutableChain) {
            ImmutableJob job = ((ImmutableChain) plan).getAllJobs().get(0);
            if (Iterables.any(job.getBuildDefinition().getTaskDefinitions(), predicate)) {
                return true;
            }
        } else if (plan instanceof ImmutableBuildable) {
            ImmutableJob job = (ImmutableJob) plan;
            if (Iterables.any(job.getBuildDefinition().getTaskDefinitions(), predicate)) {
                return true;
            }
        }
        return false;
    }

    private static class IsCxTaskPredicate<TASKDEF extends TaskIdentifier> implements Predicate<TASKDEF> {
        @Override
        public boolean apply(@Nullable TASKDEF taskIdentifier) {
            return (Preconditions.checkNotNull(taskIdentifier)).getPluginKey().startsWith("com.cx.checkmarx-bamboo-plugin:checkmarx");
        }

        @Override
        public boolean test(@javax.annotation.Nullable TASKDEF input) {
            return this.apply(input);
        }

    }
}