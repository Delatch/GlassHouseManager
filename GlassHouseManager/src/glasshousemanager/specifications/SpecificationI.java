package glasshousemanager.specifications;

/** L'interface SpecificationI.
 * @param <E> L'entité métier, sur laquelle porte la condition.
 */
public interface SpecificationI<E>{
    /** La condition à satisfaire.
     * @param e le paramètre de la méthode
     */
    public abstract boolean isSatisfiedBy(E e);
}