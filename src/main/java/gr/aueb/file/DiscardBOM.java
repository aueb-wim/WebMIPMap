package gr.aueb.file;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;


public class DiscardBOM {
    
    public static InputStream checkForUtf8BOMAndDiscardIfAny(InputStream inputStream) throws IOException {
    PushbackInputStream pushbackInputStream = new PushbackInputStream(new BufferedInputStream(inputStream), 3);
    byte[] bom = new byte[3];
    if (pushbackInputStream.read(bom) != -1) {
        if (!(bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF)) {
            pushbackInputStream.unread(bom);
        }
    }
    return pushbackInputStream; 
}

}
