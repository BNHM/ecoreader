package digester;

import org.xml.sax.SAXParseException;

public class ErrorHandler implements org.xml.sax.ErrorHandler {
  public void warning(SAXParseException ex) throws SAXParseException {
     // stop processing on warnings
    throw ex;
  }

  public void error(SAXParseException ex) throws SAXParseException {
     // stop processing on errors
    throw ex;
  }

  public void fatalError(SAXParseException ex) throws SAXParseException {
     // stop processing on fatal errors
    throw ex;
  }
}
