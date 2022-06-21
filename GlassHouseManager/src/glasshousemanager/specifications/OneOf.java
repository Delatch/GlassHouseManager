package glasshousemanager.specifications;

import java.util.Iterator;

public class OneOf<E> extends CompositeSpecification<E>{

    public boolean isSatisfiedBy(E e){
        Iterator<SpecificationI<E>> it = this.specifications.iterator();
        while(it.hasNext()){
            if(it.next().isSatisfiedBy(e))
                return true;
        }

        return false;
    }
}