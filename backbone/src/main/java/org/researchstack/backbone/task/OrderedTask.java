package org.researchstack.backbone.task;

import org.researchstack.backbone.result.TaskResult;
import org.researchstack.backbone.step.Step;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class OrderedTask extends Task implements Serializable
{

    private List<Step> steps;

    public OrderedTask(String identifier)
    {
        super(identifier);
        this.steps = new ArrayList<>();
    }

    public OrderedTask(String identifier, List<Step> steps)
    {
        super(identifier);
        this.steps = new ArrayList<>(steps);
    }

    public OrderedTask(String identifier, Step... steps)
    {
        super(identifier);
        this.steps = Arrays.asList(steps);
    }


    @Override
    public Step getStepAfterStep(Step step, TaskResult result)
    {
        if(step == null)
        {
            return steps.get(0);
        }

        int nextIndex = steps.indexOf(step) + 1;

        if(nextIndex < steps.size())
        {
            return steps.get(nextIndex);
        }

        return null;
    }


    @Override
    public Step getStepBeforeStep(Step step, TaskResult result)
    {
        int nextIndex = steps.indexOf(step) - 1;

        if(nextIndex >= 0)
        {
            return steps.get(nextIndex);
        }

        return null;
    }

    @Override
    public Step getStepWithIdentifier(String identifier)
    {
        for(Step step : steps)
        {
            if(identifier.equals(step.getIdentifier()))
            {
                return step;
            }
        }
        return null;
    }

    @Override
    public TaskProgress getProgressOfCurrentStep(Step step, TaskResult result)
    {
        int current = step == null ? - 1 : steps.indexOf(step);
        return new TaskProgress(current, steps.size());
    }

    @Override
    public void validateParameters()
    {
        Set<String> uniqueIds = new HashSet<>();
        for(Step step : steps)
        {
            uniqueIds.add(step.getIdentifier());
        }

        if(uniqueIds.size() != steps.size())
        {
            throw new InvalidTaskException("OrderedTask has steps with duplicate ids");
        }
    }

    public void addStep(Step step)
    {
        if(steps == null)
        {
            steps = new ArrayList<>();
        }

        steps.add(step);
    }

    public List<Step> getSteps()
    {
        return steps;
    }

    @Override
    public boolean equals(Object o)
    {
        if(o instanceof OrderedTask)
        {
            OrderedTask orderedTask = (OrderedTask) o;
            return getIdentifier().equals(orderedTask.getIdentifier()) &&
                    steps.equals(orderedTask.getSteps());
        }
        return false;
    }
}