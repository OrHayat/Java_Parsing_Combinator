import java.util.List;



class Pair<T,U>
{
    public  T x;
    public U y;
    Pair(T x,U y)
    {
        this.x=x;
        this.y=y;
    }

    @Override
    public String toString() {
    return ("("+x.toString()+","+y.toString()+")");
    }
}