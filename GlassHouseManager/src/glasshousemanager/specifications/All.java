package glasshousemanager.specifications;

import java.util.Iterator;

public class All<E> extends CompositeSpecification<E>{

    public boolean isSatisfiedBy(E e){
        boolean result = true;

        Iterator<SpecificationI<E>> it = this.specifications.iterator();
        while(it.hasNext()){
            if(!it.next().isSatisfiedBy(e))
                return false;
        }

        return true;
    }
}