package glasshousemanager.specifications;

public class Or<E> extends Binary<E>{

    public Or(SpecificationI<E> spec1, SpecificationI<E> spec2){
        super(spec1,spec2);
    }

    public Or(){}

    public boolean isSatisfiedBy(E e){
        return spec1.isSatisfiedBy(e) || spec2.isSatisfiedBy(e); /* TODO */
    }
}