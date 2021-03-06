package nl.ssischaefer.savaragerow.workflow.model.action;

import nl.ssischaefer.savaragerow.workflow.util.StringPlaceholderTransformer;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Map;

public class APICallAction extends Action {
    private String url;
    private String jsonBody;

    @Override
    public void perform(Map<String, String> data) {
        String transformedUrl = StringPlaceholderTransformer.transformPlaceholders(url, data);
        String transformedJsonBody = StringPlaceholderTransformer.transformPlaceholdersJson(jsonBody, data);

        try {
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpPost httpPost = new HttpPost(transformedUrl);
                StringEntity entity = new StringEntity(transformedJsonBody);
                httpPost.setEntity(entity);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                client.execute(httpPost);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getUrl() {
        return url;
    }

    public APICallAction setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getJsonBody() {
        return jsonBody;
    }

    public APICallAction setJsonBody(String jsonBody) {
        this.jsonBody = jsonBody;
        return this;
    }
}
