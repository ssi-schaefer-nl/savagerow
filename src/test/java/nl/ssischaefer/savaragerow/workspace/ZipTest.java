package nl.ssischaefer.savaragerow.workspace;

import net.lingala.zip4j.ZipFile;
import nl.ssischaefer.savaragerow.workspace.exception.WorkspaceNotSetException;
import nl.ssischaefer.savaragerow.workspace.util.ZipUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ZipTest {
    @Test
    public void shouldZipFile() throws IOException, SQLException, WorkspaceNotSetException {
        WorkspaceService.setCurrentWorkspace("testdb");
        String dir = WorkspaceService.getCurrentWorkspace();
        new ZipFile("test.zip").addFolder(new File(dir));
    }
}
