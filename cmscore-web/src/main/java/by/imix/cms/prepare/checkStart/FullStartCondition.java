package by.imix.cms.prepare.checkstart;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by miha on 02.11.2017.
 */
public class FullStartCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata)
    {
        return !FirstStartCondition.isConnect();
    }
}
