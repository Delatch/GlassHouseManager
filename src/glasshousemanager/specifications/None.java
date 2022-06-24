package glasshousemanager.specifications;

import java.util.Iterator;

public class None<E> extends CompositeSpecification<E>{
    @Override
    public boolean isSatisfiedBy(E e){
        Iterator<SpecificationI<E>> it = this.specifications.iterator();
        boolean result;

        while(it.hasNext()){
            SpecificationI spec;
            spec = it.next();

            result = spec.isSatisfiedBy(e);
            result = (spec instanceof None) != result;

            if(result){
                return false;
            }
        }

        return true;
    }
}