package nl.ssischaefer.savaragerow.workspace;

import nl.ssischaefer.savaragerow.workspace.exception.WorkspaceNotSetException;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

class WorkspaceTest {

    @Test
    public void testShouldDetectWorkspace()  {

        Path path = Paths.get("c:\\test", "test.json");
        System.out.println(path);

        try{
            String currentWorkspace = WorkspaceService.getCurrentWorkspace();
            System.out.println(currentWorkspace);
        }catch (WorkspaceNotSetException e) {
            System.out.println(e);
        }
    }

}