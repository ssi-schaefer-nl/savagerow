package nl.ssischaefer.savaragerow.v1.util.sql;

import nl.ssischaefer.savaragerow.v1.workflow.RowCriterion;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class SQLDQLGeneratorTest {
    private final String expectedBaseQuery = "select rowid as rowid, * from test";

    @Test
    public void shouldGenerateSelectQueryWithRowId() {
        Long rowid = 5L;
        String expected = expectedBaseQuery + " where rowid="+rowid.toString();
        String result = SQLDQLGenerator.generateSelectQuery("test", rowid);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldGenerateSelectQuery() {
        String result = SQLDQLGenerator.generateSelectQuery("test");
        Assert.assertEquals(expectedBaseQuery, result);
    }

    @Test
    public void shouldGenerateSelectQueryWithSelectCriteria() {
        List<RowCriterion> criteria = new ArrayList<>();
        String expected = expectedBaseQuery + " WHERE one == ? AND two > ? AND three < ? AND four != ?";
        criteria.add(new RowCriterion().setColumn("one").setComparator(SQLComparator.EQUALS).setRequired("1"));
        criteria.add(new RowCriterion().setColumn("two").setComparator(SQLComparator.GREATER).setRequired("2"));
        criteria.add(new RowCriterion().setColumn("three").setComparator(SQLComparator.SMALLER).setRequired("3"));
        criteria.add(new RowCriterion().setColumn("four").setComparator(SQLComparator.NOT).setRequired("4"));

        String result = SQLDQLGenerator.generateSelectQuery("test", criteria);
        Assert.assertEquals(expected, result);
    }
}
