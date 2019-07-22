import java.util.List;


interface Parser<T>
{
   Pair<T,List<Character>> parse(List<Character> str) throws Parser_excption;
}
