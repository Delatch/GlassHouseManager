package glasshousemanager.commands;

/** La commande à exécuter.
 * r est mutable, dont le contenu est éventuellement modifié par
 * l'exécution de la commande
 * @param <E> l'entité métier
 * @param <R> le type de la donnée et du résultat
 */
public interface CommandI<E,R>{
    /** L'exécution de la commande.
     * @param r l'entité transmise
     * @return le résultat retourné
     */
    public R execute(E e, R r) throws Exception;
}