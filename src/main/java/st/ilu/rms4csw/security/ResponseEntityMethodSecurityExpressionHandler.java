package st.ilu.rms4csw.security;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;

/**
 * @author Mischa Holz
 */
public class ResponseEntityMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    @Override
    public Object filter(Object filterTarget, Expression filterExpression, EvaluationContext ctx) {
        if(filterTarget instanceof ResponseEntity) {
            Object ret = super.filter(((ResponseEntity) filterTarget).getBody(), filterExpression, ctx);

            return new ResponseEntity<>(ret, ((ResponseEntity) filterTarget).getStatusCode());
        }
        return super.filter(filterTarget, filterExpression, ctx);
    }
}
