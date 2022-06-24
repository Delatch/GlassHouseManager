package glasshousemanager.rules;

import java.util.*;

public class Rules<E,R> implements RuleI<E,R>{
    private List<RuleI<E,R>> rules;

    public Rules(){
        this.rules = new ArrayList<>();
    }

    public void setRules(RuleI<E,R>[] rules){
        this.rules.addAll(Arrays.asList(rules));
    }

    public Rules<E,R> add(RuleI<E,R> rule){
        this.rules.add(rule);
        return this;
    }

    public R execute(E e,R r) throws Exception {
        for(RuleI rule : rules){
            r = (R)rule.execute(e, r);
        }
        return r;
    }

}
