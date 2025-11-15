package services;

import java.io.File;
import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;

@Service
public class ServiceTika {

    public String extractContent(File file) throws IOException, TikaException {
    	Tika tika = new Tika();
        try {
            return tika.parseToString(file);
        } catch (TikaException e) {

            throw e;
        }
    }
}
