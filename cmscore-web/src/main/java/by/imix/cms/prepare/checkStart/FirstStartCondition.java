package by.imix.cms.prepare.checkStart;

import by.imix.cms.database.DatabaseUtil;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by Mikhail_Kachanouski on 11/2/2017.
 */
public class FirstStartCondition implements Condition{
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata){
        return DatabaseUtil.isConnectWithReadProperty();
    }
}