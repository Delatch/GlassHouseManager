package glasshousemanager.commands;

public class Command<E,R> implements CommandI<E,R>{
    private static final boolean T = false;
    protected CommandI<E,R> command;

    public void setCommand(CommandI<E,R> command){
        this.command = command;
    }

    public CommandI<E,R> getCommand(){
        return this.command;
    }

    public R execute(E entity, R result)throws Exception{
        try{
            return command.execute(entity, result);
        }catch(StackOverflowError e){
            throw new RuntimeException(e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }

    }

}