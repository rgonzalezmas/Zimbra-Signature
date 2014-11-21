

import java.util.Map;
import java.util.HashMap;

/**
 * A helper class provided out of the box to encode characters that HTML can't support
 * natively like &lt;, &gt;, &amp;, or &quot;.  This will scan the value passed to the transform
 * method and replace any of these special characters with the HTML encoded equivalent.  This
 * method will NOT work for HTML text because it will blindly encode all characters it sees which
 * means it will strip out any HTML tags.
 */
public class HtmlEncoder  {

    private static final Map<Integer,String> htmlEntities = new HashMap<Integer,String>();

    public HtmlEncoder() {
        if( htmlEntities.isEmpty() ) {
            htmlEntities.put( 34, "&quot;" );       // " - double-quote
           // htmlEntities.put( 38, "&amp;" );        // & - ampersand
//            htmlEntities.put( 39, "&apos;");        // ' - apostrophe
           // htmlEntities.put( 60, "&lt;" );         // < - less-than
            //htmlEntities.put( 62, "&gt;" );         // > - greater-than
            htmlEntities.put( 160, "&nbsp;" );      // non-breaking space
            htmlEntities.put( 169, "&copy;" );      // © - copyright
            htmlEntities.put( 174, "&reg;" );       // Æ - registered trademark
            htmlEntities.put( 192, "&Agrave;" );    // ¿ - uppercase A, grave accent
            htmlEntities.put( 193, "&Aacute;" );    // ¡ - uppercase A, acute accent
            htmlEntities.put( 194, "&Acirc;");      // ¬ - uppercase A, circumflex accent
            htmlEntities.put( 195, "&Atilde;" );    // √ - uppercase A, tilde
            htmlEntities.put( 196, "&Auml;" );      // ƒ - uppercase A, umlaut
            htmlEntities.put( 197, "&Aring;" );     // ≈ - uppercase A, ring
            htmlEntities.put( 198, "&AElig;" );     // Δ - uppercase AE
            htmlEntities.put( 199, "&Ccedil;" );    // « - uppercase C, cedilla
            htmlEntities.put( 200, "&Egrave;");     // » - uppercase E, grave accent
            htmlEntities.put( 201, "&Eacute;");     // … - uppercase E, acute accent
            htmlEntities.put( 202, "&Ecirc;" );     //   - uppercase E, circumflex accent
            htmlEntities.put( 203, "&Euml;" );      // À - uppercase E, umlaut
            htmlEntities.put( 204, "&Igrave;" );    // Ã - uppercase I, grave accent
            htmlEntities.put( 205, "&Iacute;" );    // Õ - uppercase I, acute accent
            htmlEntities.put( 206, "&Icirc;" );     // Œ - uppercase I, circumflex accent
            htmlEntities.put( 207, "&Iuml;" );      // œ - uppercase I, umlaut
            htmlEntities.put( 208, "&ETH;" );       // – - uppercase Eth, Icelandic
            htmlEntities.put( 209, "&Ntilde;");     // — - uppercase N, tilde
            htmlEntities.put( 210, "&Ograve;");     // “ - uppercase O, grave accent
            htmlEntities.put( 211, "&Oacute;");     // ” - uppercase O, acute accent
            htmlEntities.put( 212, "&Ocirc;" );     // ‘ - uppercase O, circumflex accent
            htmlEntities.put( 213, "&Otilde;");     // ’ - uppercase O, tilde
            htmlEntities.put( 214, "&Ouml;");       // ÷ - uppercase O, umlaut
            htmlEntities.put( 216, "&Oslash;");     // ÿ - uppercase O, slash
            htmlEntities.put( 217, "&Ugrave;");     // Ÿ - uppercase U, grave accent
            htmlEntities.put( 218, "&Uacute;");     // ⁄ - uppercase U, acute accent
            htmlEntities.put( 219, "&Ucirc;" );     // € - uppercase U, circumflex accent
            htmlEntities.put( 220, "&Uuml;" );      // ‹ - uppercase U, umlaut
            htmlEntities.put( 221, "&Yacute;");     // › - uppercase Y, acute accent
            htmlEntities.put( 222, "&THORN;");      // ﬁ - uppercase THORN, Icelandic
            htmlEntities.put( 223, "&szlig;");      // ﬂ - lowercase sharps, German
            htmlEntities.put( 224, "&agrave;");     // ‡ - lowercase a, grave accent
            htmlEntities.put( 225, "&aacute;");     // · - lowercase a, acute accent
            htmlEntities.put( 226, "&acirc;");      // ‚ - lowercase a, circumflex accent
            htmlEntities.put( 227, "&atilde;");     // „ - lowercase a, tilde
            htmlEntities.put( 228, "&auml;");       // ‰ - lowercase a, umlaut
            htmlEntities.put( 229, "&aring;");      // Â - lowercase a, ring
            htmlEntities.put( 230, "&aelig;");      // Ê - lowercase ae
            htmlEntities.put( 231, "&ccedil;" );    // Á - lowercase c, cedilla
            htmlEntities.put( 232, "&egrave;");     // Ë - lowercase e, grave accent
            htmlEntities.put( 233, "&eacute;");     // È - lowercase e, acute accent
            htmlEntities.put( 234, "&ecirc;");      // Í - lowercase e, circumflex accent
            htmlEntities.put( 235, "&euml;");       // Î - lowercase e, umlaut
            htmlEntities.put( 236, "&igrave;" );    // Ï - lowercase i, grave accent
            htmlEntities.put( 237, "&iacute;");     // Ì - lowercase i, acute accent
            htmlEntities.put( 238, "&icirc;");      // Ó - lowercase i, circumflex accent
            htmlEntities.put( 239, "&iuml;");       // Ô - lowercase i, umlaut
            htmlEntities.put( 240, "&eth;");        //  - lowercase eth, Icelandic
            htmlEntities.put( 241, "&ntilde;");     // Ò - lowercase n, tilde
            htmlEntities.put( 242, "&ograve;");     // Ú - lowercase o, grave accent
            htmlEntities.put( 243, "&oacute;");     // Û - lowercase o, acute accent
            htmlEntities.put( 244, "&ocirc;");      // Ù - lowercase o, circumflex accent
            htmlEntities.put( 245, "&otilde;");     // ı - lowercase o, tilde
            htmlEntities.put( 246, "&ouml;");       // ˆ - lowercase o, umlaut
            htmlEntities.put( 248, "&oslash;");     // ¯ - lowercase o, slash
            htmlEntities.put( 249, "&ugrave;");     // ˘ - lowercase u, grave accent
            htmlEntities.put( 250, "&uacute;");     // ˙ - lowercase u, acute accent
            htmlEntities.put( 251, "&ucirc;");      // ˚ - lowercase u, circumflex accent
            htmlEntities.put( 252, "&uuml;");       // ¸ - lowercase u, umlaut
            htmlEntities.put( 253, "&yacute;");     // ˝ - lowercase y, acute accent
            htmlEntities.put( 254, "&thorn;");      // ˛ - lowercase thorn, Icelandic
            htmlEntities.put( 255, "&yuml;");       // ˇ - lowercase y, umlaut
            htmlEntities.put( 8364, "&euro;");      // Euro symbol
        }
    }

    public String transform(Object value) {
        String val = value.toString();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < val.length(); ++i) {
            int intVal = (int)val.charAt(i);
           if( htmlEntities.containsKey( intVal ) ) {
                builder.append( htmlEntities.get( intVal ) );
            } else if ( intVal > 128 ) {
                    builder.append( "&#" );
                    builder.append( intVal );
                    builder.append( ";" );
            } else {
                builder.append( val.charAt(i) );
            }
        }
        return builder.toString();
    }
}