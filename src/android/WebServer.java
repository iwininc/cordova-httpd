package com.rjfun.cordova.httpd;

import java.io.IOException;
import java.lang.String;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import android.util.Log;

public class WebServer extends NanoHTTPD
{
    private Map customPaths = null;
    private String[] customURIs = new String[0];
    private final String LOGTAG = "NanoHTTPD-Cordova";
    
	public WebServer(InetSocketAddress localAddr, AndroidFile wwwroot, Map customPaths ) throws IOException {
		super(localAddr, wwwroot);
        addCustomPaths(customPaths);
	}

	public WebServer(int port, AndroidFile wwwroot, Map customPaths ) throws IOException {
		super(port, wwwroot);
        addCustomPaths(customPaths);
	}
    
    private void addCustomPaths(Map customPaths) {
        this.customPaths = customPaths;
        customURIs = new String[customPaths.keySet().size()];
        int i = 0;
        Iterator keys = customPaths.keySet().iterator();
        while (keys.hasNext()) {
            customURIs[i++] = (String) keys.next();
        }
    }
    
    public Response serve( String uri, String method, Properties header, Properties parms, Properties files )
    {
        if (customURIs.length > 0) {
            for (int i = 0; i < customURIs.length; i++) {
                String testURI = customURIs[i];
                if (uri.startsWith(testURI)) {
                    Log.i( LOGTAG, method + " '" + uri + "' " );
                    String newURI = uri.substring(testURI.length());
                    return serveFile( newURI, header, (AndroidFile) customPaths.get(testURI), true );
                }
            }
            return super.serve( uri, method, header, parms, files );
            }
        } else {
            return super.serve( uri, method, header, parms, files );
        }
    }
}
