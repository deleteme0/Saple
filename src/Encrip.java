
public class Encrip{
    
    String key = "qewrtyuiopasdfghjkl;zxcvbn m,./879654231-=0;";

    String letters= "879546123 0abcdefghijklmnopqrstuvwxyz.,/-=";

    public String enc(String in){

        StringBuilder out= new StringBuilder("");
        
        char[] arr = in.toCharArray();

        for(char ch:arr){

            out.append(key.charAt(letters.indexOf(ch)));

        }


        return out.toString();
    }


    public String dec(String in){

        StringBuilder out = new StringBuilder("");

        char[] arr = in.toCharArray();

        for(char ch:arr){

            out.append(letters.charAt(key.indexOf(ch)));
        }

        return out.toString();
    }

}
