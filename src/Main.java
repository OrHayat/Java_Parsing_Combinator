import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {


    // Driver program to test above functions
    public static void main(String[] args)
    {
        Parser c=PC.nt_char('a');
        Parser b=PC.nt_char('b');
        Parser ab=PC.nt_caten(c,b);
        List<Character> lst=new LinkedList<>();
        lst.add('a');
        lst.add('a');
        lst.add('a');
        lst.add('a');
        lst.add('a');
        lst.add('a');
        lst.add('a');
     //   ab.parse(lst);
      var q  =PC.nt_caten_list(new LinkedList<>());
        Pair res= PC.nt_display(PC.nt_pack((x)->x,q),"test parser").parse(lst);

        /*
        Parser cb=PC.nt_caten(c,b);
        c=PC.range_ci('A','B');
        ArrayList arr = new ArrayList();
        arr.add(c);
        arr.add(c);
        arr.add(c);
        Parser t=PC.nt_caten_list(arr);
        t.parse(lst);
        List<Character> lst2=new LinkedList<>();
*/
        //System.out.print(res.y.toString());
    }
}

