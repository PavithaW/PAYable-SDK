package util;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

/**
 * Created by Dell on 12/7/2015.
 */
public abstract class ToStringConverter {


      public String fromBody(ResponseBody body) throws IOException {
        return body.string();
    }


    public RequestBody toBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }
}
