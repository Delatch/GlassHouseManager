package glasshousemanager.specifications;


import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class CompositeSpecification<E> implements SpecificationI<E>{
    protected List<SpecificationI<E>> specifications;

    public CompositeSpecification(){
        this.specifications = new ArrayList<>();
    }

    public void setSpecifications(SpecificationI<E>[] specifications){
        this.specifications.addAll(Arrays.asList(specifications));
    }

    public CompositeSpecification<E> add(SpecificationI<E> spec){
        this.specifications.add(spec);
        return this;
    }
}