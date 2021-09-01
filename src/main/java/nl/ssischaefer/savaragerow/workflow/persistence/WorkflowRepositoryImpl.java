package nl.ssischaefer.savaragerow.workflow.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import nl.ssischaefer.savaragerow.common.schema.WorkflowSchema;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WorkflowRepositoryImpl implements WorkflowRepository {
    private static final String WORKFLOW_FILENAME = "workflow.json";

    private final String workflowRootDirectory;

    public WorkflowRepositoryImpl(String workflowRootDirectory) {
        this.workflowRootDirectory = workflowRootDirectory;
    }

    @Override
    public boolean save(WorkflowSchema workflowSchema) {
        try {
            var objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            var path = Paths.get(workflowRootDirectory, workflowSchema.getId(), WORKFLOW_FILENAME);
            if(!Files.isDirectory(path.getParent())) Files.createDirectories(path.getParent());
            var json = objectMapper.writeValueAsString(workflowSchema);
            Files.write(path, json.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public Optional<WorkflowSchema> get(String id) {
        try {
            var workflow = new ObjectMapper().readValue(Paths.get(workflowRootDirectory, id, WORKFLOW_FILENAME).toFile(), WorkflowSchema.class);
            return Optional.ofNullable(workflow);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(String id) {
        try {
            var path = Paths.get(workflowRootDirectory, id, WORKFLOW_FILENAME);
            FileUtils.deleteDirectory(path.toFile());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<WorkflowSchema> getAll() {
        var workflowDirectories = new File(workflowRootDirectory).list(new FileFileFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        if(workflowDirectories == null) return Collections.emptyList();

        return Arrays.stream(workflowDirectories).map(this::get).filter(Optional::isEmpty).map(Optional::get).collect(Collectors.toList());
    }
}
