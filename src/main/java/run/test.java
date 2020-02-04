package run;

import java.net.URLEncoder;
public class test {
    public static void main(String[] args) {
        String str = "%u2";
        String str2 = URLEncoder.encode(str);
        System.out.print("encoded result: ");
        try {
            System.out.println(java.net.URLDecoder.decode(str2, "UTF-8"));
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        System.out.print("unencoded result: ");
        try {
            System.out.println(java.net.URLDecoder.decode(str, "UTF-8"));
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
