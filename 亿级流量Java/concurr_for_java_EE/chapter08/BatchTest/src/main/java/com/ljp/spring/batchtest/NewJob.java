/*
 * Copyright 2006-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ljp.spring.batchtest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StartLimitExceededException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.AbstractJob;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.StepLocator;

/**
 * Implementation of the {@link Job} interface that allows for complex flows of
 * steps, rather than requiring sequential execution. In general, this job
 * implementation was designed to be used behind a parser, allowing for a
 * namespace to abstract away details.
 *
 * @author Dave Syer
 * @since 2.0
 */
public class NewJob extends SimpleJob implements Serializable {
	private List<Step> steps = new ArrayList<Step>();

	/**
	 * Default constructor for job with null name
	 */
	public NewJob() {
		this(null);
	}

	/**
	 * @param name
	 */
	public NewJob(String name) {
		super(name);
	}

	/**
	 * Public setter for the steps in this job. Overrides any calls to
	 * {@link #addStep(Step)}.
	 *
	 * @param steps the steps to execute
	 */
	public void setSteps(List<Step> steps) {
		this.steps.clear();
		this.steps.addAll(steps);
	}

	/**
	 * Convenience method for clients to inspect the steps for this job.
	 *
	 * @return the step names for this job
	 */
	@Override
	public Collection<String> getStepNames() {
		List<String> names = new ArrayList<String>();
		for (Step step : steps) {
			names.add(step.getName());

			if(step instanceof StepLocator) {
				names.addAll(((StepLocator)step).getStepNames());
			}
		}
		return names;
	}

	/**
	 * Convenience method for adding a single step to the job.
	 *
	 * @param step a {@link Step} to add
	 */
	public void addStep(Step step) {
		this.steps.add(step);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.batch.core.job.AbstractJob#getStep(java.lang.String)
	 */
	@Override
	public Step getStep(String stepName) {
		for (Step step : this.steps) {
			if (step.getName().equals(stepName)) {
				return step;
			} else if(step instanceof StepLocator) {
				Step result = ((StepLocator)step).getStep(stepName);
				if(result != null) {
					return result;
				}
			}
		}
		return null;
	}

	/**
	 * Handler of steps sequentially as provided, checking each one for success
	 * before moving to the next. Returns the last {@link StepExecution}
	 * successfully processed if it exists, and null if none were processed.
	 *
	 * @param execution the current {@link JobExecution}
	 *
	 * @see AbstractJob#handleStep(Step, JobExecution)
	 */
	@Override
	protected void doExecute(JobExecution execution) throws JobInterruptedException, JobRestartException,
	StartLimitExceededException {

		StepExecution stepExecution = null;
		for (Step step : steps) {
			stepExecution = handleStep(step, execution);
			if (stepExecution.getStatus() != BatchStatus.COMPLETED) {
				//
				// Terminate the job if a step fails
				//
				break;
			}
		}

		//
		// Update the job status to be the same as the last step
		//
		if (stepExecution != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Upgrading JobExecution status: " + stepExecution);
			}
			execution.upgradeStatus(stepExecution.getStatus());
			execution.setExitStatus(stepExecution.getExitStatus());
		}
	}

}
