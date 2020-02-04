package modsDigester;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * A set of Utilities for working with MODS files, generally
 */
public class modsUtils {
    /**
         * Generic method for fetching values for Terms in the MODS file.
         * This class is a work-around for MODS data files which store various
         * data in repeating elements with attributes defining entity types.  Since
         * Apache Digester cannot parse this data, we instead build a linkedList
         * of "terms" and pass in an attribute type, which we can search for to
         * output the appropriate value.
         *
         * @param terms
         * @param attributeKey
         * @param attributeValue
         *
         * @return
         */
        public static String getTermValue(LinkedList<Term> terms, String attributeKey, String attributeValue) {
            Iterator termsIterator = terms.iterator();
            while (termsIterator.hasNext()) {
                Term t = (Term) termsIterator.next();

                if (attributeKey.equals("type") &&
                        t.getType().equals(attributeValue)) {
                    return t.getValue();
                } else if (attributeKey.equals("point") &&
                        t.getPoint() != null &&
                        t.getPoint().equals(attributeValue)) {
                    return t.getValue();
                }
            }
            return null;
        }


}
