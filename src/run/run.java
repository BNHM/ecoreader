package run;

import org.apache.commons.digester3.CallMethodRule;
import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.Rule;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import digester.Bar;
import digester.Mods;
import digester.ErrorHandler;
import digester.Term;
import renderer.printer;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;


/**
 * Created by jdeck on 4/16/15.
 */
public class run {
    String xmlFile;

    public run() {
        //xmlFile = "file:////Users/jdeck/IdeaProjects/nature-reader/nature-reader/docs/mvz/mods/Grinnell_v1316.xml";
        xmlFile = "file:////Users/jdeck/IdeaProjects/nature-reader/nature-reader/docs/test.xml";

    }

    public void runAsDOM() {

        try {
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            System.out.println("foo");

            Element root = document.getDocumentElement();
            loopNode(root);
        } catch (FactoryConfigurationError e) {
            // unable to get a document builder factory
        } catch (ParserConfigurationException e) {
            // parser was unable to be configured
        } catch (SAXException e) {
            // parsing error
        } catch (IOException e) {
            // i/o error
        }
    }

    public static void loopNode(Node node) {
        // do something with the current node instead of System.out
        System.out.println(node.getNodeName() + "|" + node.getFirstChild().getNodeValue());


        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                //calls this method for all the children which is Element
                loopNode(currentNode);
            }
        }
    }

    public static void main(String[] args) {

        run r = new run();

        //r.runAsDOM();

        Mods mods = r.runAsApacheDigester();

        System.out.println(new printer(mods).getTabFormat());

    }

    private Mods runAsApacheDigester() {
        Mods mods = null;
        Digester digester = new Digester();
        digester.setNamespaceAware(true);
        digester.setRuleNamespaceURI("http://www.loc.gov/mods/v3");
        digester.setErrorHandler(new ErrorHandler());

        /**
         * Root MODS element
         */
        digester.addObjectCreate("mods", Mods.class);
        digester.addSetProperties("mods");

        // Set title
        digester.addCallMethod("mods/titleInfo/title", "setTitle", 0);

        // Language
        digester.addObjectCreate("mods/language/languageTerm", Term.class);
        digester.addSetProperties("mods/language/languageTerm");
        digester.addSetNext("mods/language/languageTerm", "setLanguage");
        digester.addCallMethod("mods/language/languageTerm", "setValue", 0);

        // Identifier
        digester.addCallMethod("mods/identifier", "setIdentifier", 0);

        // Name  (only extract family name here)
        digester.addObjectCreate("mods/name/namePart", Term.class);
        digester.addSetProperties("mods/name/namePart");
        digester.addSetNext("mods/name/namePart", "setName");
        digester.addCallMethod("mods/name/namePart", "setValue", 0);

        /**
         * Sub-elements
         */
        digester.addObjectCreate("mods/bar", Bar.class);
        digester.addSetProperties("mods/bar");
        digester.addSetNext("mods/bar", "addBar");
        digester.addCallMethod("mods/bar", "addDefinition", 0);


        try {
            mods = digester.parse(xmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return mods;
    }
}
