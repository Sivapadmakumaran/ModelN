package models;


import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;

public class MyNamespaceMapper extends NamespacePrefixMapper {

    private static final String V1_URI = "http://xmlns.modeln.com/ApplicationObjects/ManagedCareEstimatedFFSIFFAck/V1";

    private static final String V1_PREFIX = "ns3";
    
	@Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        if(V1_URI.equals(namespaceUri)) {
            return V1_PREFIX;
        } 
        return suggestion;
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return new String[] { V1_URI};
    }
}
