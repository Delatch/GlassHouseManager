package glasshousemanager.specifications;

public abstract class Binary<E> implements SpecificationI<E> {
    protected SpecificationI<E> spec1;
    protected SpecificationI<E> spec2;

    public Binary(SpecificationI<E> spec1, SpecificationI<E> spec2) {
        this.spec1 = spec1;
        this.spec2 = spec2;
    }

    public Binary() {
    }

    public void setSpec1(SpecificationI<E> spec) {
        this.spec1 = spec;
    }

    public void setSpec2(SpecificationI<E> spec) {
        this.spec2 = spec;
    }
}