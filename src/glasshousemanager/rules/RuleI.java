package glasshousemanager.rules;

/** La classe Règle.
 *  @param <E> La classe de l'entité sur laquelle porte la condition/specification
 *  @param <R> la classe de la donnée et du résultat
 */
public interface RuleI<E,R>{

    /**
     * Exécution d'une règle de type if/then/else.
     * @param e l'entité sur laquelle porte la condition
     * @param r la donnée transmise
     * @return si la condition n'est pas satisfaite r est retourné,
     *         sinon le résultat de l'exécution de la commande est retourné
     */
    public R execute(E e,R r) throws Exception;

}