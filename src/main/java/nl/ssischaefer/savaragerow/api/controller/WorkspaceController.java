package nl.ssischaefer.savaragerow.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.api.dto.Base64File;
import nl.ssischaefer.savaragerow.api.util.RequestParams;
import nl.ssischaefer.savaragerow.data.common.exception.DatabaseException;
import nl.ssischaefer.savaragerow.workspace.WorkspaceService;
import org.apache.commons.codec.binary.Base64;
import spark.Request;
import spark.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.zip.ZipInputStream;

import static spark.Spark.*;

public class WorkspaceController {

    public void setup(String prefix) {
        get(prefix + "/workspace", this::getAllDatabases);
        post(prefix + "/workspace", this::importDatabase);
        get(prefix + "/workspace/:database", this::exportDatabase);
        post(prefix + "/workspace/:database", this::createDatabase);
        delete(prefix + "/workspace/:database", this::deleteDatabase);

    }

    private String exportDatabase(Request request, Response response) throws IOException, SQLException {
        String db = request.params(RequestParams.Parameter.Database);
        WorkspaceService.export(db, response.raw().getOutputStream());
        response.raw().setContentType("application/zip");
        response.raw().setHeader("Content-Disposition","attachment; filename="+db+".zip");
        return "";
    }

    public String createDatabase(Request request, Response response) throws DatabaseException, IOException, SQLException {
        String database = request.params(RequestParams.Parameter.Database);
        WorkspaceService.createDatabase(database);
        return "";
    }

    private String importDatabase(Request request, Response response) throws IOException {
        var base64File = new ObjectMapper().readValue(request.body(), Base64File.class);
        byte[] decodedFile = Base64.decodeBase64(base64File.getFile());
        InputStream ba = new ByteArrayInputStream(decodedFile);
        WorkspaceService.importDatabase(new ZipInputStream(ba));
        return "";
    }


    public String deleteDatabase(Request request, Response response) throws IOException {
        WorkspaceService.removeDatabase(request.params(RequestParams.Parameter.Database));
        return "";
    }

    public String getAllDatabases(Request request, Response response) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(WorkspaceService.listDatabases());
    }

}
