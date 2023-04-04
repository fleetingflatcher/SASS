package xyz.sched.ques;



public abstract class AbstractProcessQueue
{
    abstract boolean step();
    protected java.util.LinkedList
            <xyz.proc.Process>
                                queue;
}
