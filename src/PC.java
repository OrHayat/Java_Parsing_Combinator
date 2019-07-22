import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class PC {

    static  Parser<String> nt_char(char c)
    {

        return (List<Character> w) -> {
            if (w.get(0) == c) {
                return new Pair<>(String.valueOf(w.get(0)), w.subList(1, w.size()));
            }
            throw new Parser_excption("X not match");
        };
    }


    static <T> Parser<T> nt_caten(Parser nt_1, Parser nt_2)
    {
        return (List<Character> w)->{
            Pair<T,List<Character>> tmp=nt_1.parse(w);
            Pair tmp2=nt_2.parse(tmp.y);
            tmp2.x=new Pair<>(tmp.x,tmp2.x);
            return  tmp2;
        };
    }


    public static<T>  Parser<T> nt_disj(Parser nt_1,Parser nt_2)
    {
        return (List<Character> w)->{
            try
            {
                Pair tmp=nt_1.parse(w);
                return tmp;
            }
            catch (Parser_excption ex)
            {
                return nt_2.parse(w);
            }
        };
    }

    static <T,U> Parser<T> nt_pack(Function<T, U> op, Parser p)
    {
        return (List<Character> w)->
        {
            Pair tmp= p.parse(w);

            return  new Pair(((U)(op.apply((T)tmp.x))),tmp.y);
        };
    }


    static <T> Parser<T> nt_display(Parser<T> nt, String name)
    {
        return (List<Character> w)->{

            try {
                Pair tmp=nt.parse(w);
                System.out.println(name+" succeded to parse"+w.toString()+"  result is "+tmp.x.toString()+" remaineder is "+ tmp.y.toString());
                return  tmp;
            }
            catch ( Parser_excption ex)
            {
                System.out.println(name+" failled to parse "+w.toString());
                throw  ex;
            }
        };
    }



    public static  Parser<String> nt_word(String word)
    {
        return (List<Character> w)->{

            if(w.size()<word.length())
            {
                throw new Parser_excption("X dont match");
            }
            char[] arr=word.toCharArray();
            for(int i=0;i<arr.length;i++)
            {
                if(arr[i]!=w.get(i))
                {
                    throw new Parser_excption("X dont match");
                }
            }
            return new Pair<>(word,w.subList(word.length(),w.size()));
        };
    }





    public static  Parser<String> nt_one_of_chars(String chars)
    {
        return (List<Character> w)->
        {
            if(w.size()<=0)
            {
                throw  new Parser_excption("x dont match");
            }
            List<Character> rest_tokens= w.subList(1,w.size());
            char c=w.get(0);
            for(int i=0;i<chars.length();i++)
            {
                if(chars.charAt(i)==c)
                {
                    return  new Pair<String,List<Character>>(String.valueOf(c),rest_tokens);
                }
            }
            throw  new Parser_excption("x dont match");
        };
    }


     static Parser<LinkedList<String>> nt_end_of_input=(List<Character> w)->
    {
        if(w.size()>0)
        {
            throw  new Parser_excption("x dont match");
        }
        else {
            return new Pair<>(new LinkedList<String>(), new LinkedList<Character>());
        }
    };
     static Parser<Object> nt_none =(List<Character> w)->
    {
        throw  new Parser_excption("x dont match");

    };

     static Parser<?> nt_epsilon =(List<Character> w)->
    {
        return new Pair<>(new LinkedList<>(),w);

    };




    private static <T> Parser nt_star(Parser nt)
    {
        return (Parser) (w)->
        {

            try {
                Pair tmp=nt.parse(w);
                Pair rest=nt_star(nt).parse((List<Character>) tmp.y);
                //rest.x is list
                LinkedList result=(LinkedList)rest.x;
                result.addFirst(tmp.x);
                return new Pair(result,rest.y);
            }
            catch (Exception ex)
            {
                return  new Pair<LinkedList<String>, List<Character>>(new LinkedList<String>(),w);
            }
        };
    }




    public  static <T> Parser<T> nt_plus(Parser nt) {

        return  nt_pack((T x)->{
                    Pair p = (Pair) x;
                    LinkedList res = (LinkedList) p.y;
                    res.addFirst(p.x);
                    return res;
                },nt_caten(nt,nt_star(nt)));
    }



    public  static  Parser<String> range(char min,char max)
    {
        return (w)->
        {
            if(w.size()<=0)
            {
                throw new Parser_excption("x dont match");
            }
            char c=w.get(0);
            if((int)c>=min&&(int)c<=max)
            {
                return  new Pair(String.valueOf(c),w.subList(1,w.size()));
            }
            throw new Parser_excption("x dont match");
        };
    }




    public  static  Parser<String> range_ci(char min,char max)
    {
        return (w)->
        {
            if(w.size()<=0)
            {
                throw new Parser_excption("x dont match");
            }
            Character c=Character.toLowerCase(w.get(0));
            char min_=Character.toLowerCase(min);
            char max_=Character.toLowerCase(max);
            if((int)c>=min_&&(int)c<=max_)
            {
                return  new Pair(String.valueOf(w.get(0)),w.subList(1,w.size()));
            }
            throw new Parser_excption("x dont match");
        };
    }




    static <T> Parser<T>nt_caten_list(Iterable<Parser> parsers)
    {
        Parser res;
        var it=parsers.iterator();
        if(it.hasNext())
        {
            res=nt_pack((x)->
            {
                LinkedList acc = new LinkedList();
                acc.add(x);
                return  acc;
            },it.next());
        }
        else
        {
            return (Parser<T>) nt_epsilon;
        }
        while(it.hasNext())
        {
            res=nt_pack((x)->
            {
                Pair p=(Pair) x;
                LinkedList acc=(LinkedList)  p.x;
                acc.addLast(p.y);
                return  acc;
            },nt_caten(res,it.next()));
        }
        return res;
    }





    static <T> Parser<T>nt_disj_list(Iterable<Parser> parsers)
    {
        Parser res;
        var it=parsers.iterator();
        if(it.hasNext())
        {
            res=it.next();
        }
        else
        {
            return (Parser<T>) nt_none;
        }
        while(it.hasNext())
        {
            res=nt_disj(res,it.next());
        }
        return res;
    }


}
