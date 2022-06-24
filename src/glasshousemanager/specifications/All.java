package glasshousemanager.specifications;

import java.util.Iterator;

public class All<E> extends CompositeSpecification<E>{

    public boolean isSatisfiedBy(E e){
        boolean result = true;

        for (SpecificationI<E> specification : this.specifications) {
            if (!specification.isSatisfiedBy(e))
                return false;
        }

        return true;
    }
}