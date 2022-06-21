package glasshousemanager.specifications;

public class Not<E> implements SpecificationI<E>{
    protected SpecificationI<E> spec;

    public Not(SpecificationI<E> spec){
        this.spec = spec;
    }

    public Not(){}

    public void setSpec(SpecificationI<E> spec){
        this.spec = spec;
    }

    public SpecificationI<E> getSpec(){
        return this.spec;
    }

    public boolean isSatisfiedBy(E e){
        return !spec.isSatisfiedBy(e);
    }
}