package glasshousemanager.rules;

import glasshousemanager.commands.*;
import glasshousemanager.specifications.*;

/** Une Régle de la forme, if(spec)then(command).
 * Si la specification est satisfaite alors la commande est exécutée
 *
 * @param <E> l'entité métier
 * @param <R> le résultat
 */
public  class Rule<E,R> implements RuleI<E,R>{
    private static boolean T = false;
    protected SpecificationI<E> specification;
    protected CommandI<E,R>     command;

    public Rule(SpecificationI<E> specification, CommandI<E,R> command){
        this.specification = specification;
        this.command = command;
    }

    public Rule(){}

    public R execute(E e,R r) throws Exception {
        if(T)System.out.print("if(" +this.specification.isSatisfiedBy(e)+")");
        if(T)System.out.print("then(" +this.command+")");

        if(this.specification.isSatisfiedBy(e)){

            R r1 = this.command.execute(e,r);

            if(T)System.out.println(" res=" + r1);
            return r1;
        }
        return r;
    }
    // mutateurs
    public void setSpecification(SpecificationI<E> specification){
        this.specification = specification;
    }

    public void setCommand(CommandI<E,R> command){
        this.command = command;
    }
    // accesseurs
    public SpecificationI<E> getSpecification(){
        return this.specification;
    }

    public CommandI<E,R> getCommand(){
        return this.command;
    }
}