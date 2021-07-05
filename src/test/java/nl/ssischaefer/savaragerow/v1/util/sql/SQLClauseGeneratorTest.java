package nl.ssischaefer.savaragerow.v1.util.sql;
import nl.ssischaefer.savaragerow.v1.workflow.FieldUpdate;
import nl.ssischaefer.savaragerow.v1.workflow.RowCriterion;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class SQLClauseGeneratorTest {

    @Test
    public void shouldGenerateWhereClause() {
        List<RowCriterion> criteria = new ArrayList<>();
        String expected = "WHERE one == ? AND two > ? AND three < ? AND four != ?";
        criteria.add(new RowCriterion().setColumn("one").setComparator(SQLComparator.EQUALS).setRequired("1"));
        criteria.add(new RowCriterion().setColumn("two").setComparator(SQLComparator.GREATER).setRequired("2"));
        criteria.add(new RowCriterion().setColumn("three").setComparator(SQLComparator.SMALLER).setRequired("3"));
        criteria.add(new RowCriterion().setColumn("four").setComparator(SQLComparator.NOT).setRequired("4"));

        String result = SQLClauseGenerator.generateWhereClause(criteria);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldGenerateSetClause() {
        List<FieldUpdate> updates = new ArrayList<>();
        String expected = "SET one = ?, two = two * ?, three = three + ?, four = four - ?";
        updates.add(new FieldUpdate().setColumn("one").setAction(SQLSetAction.SET).setValue("1"));
        updates.add(new FieldUpdate().setColumn("two").setAction(SQLSetAction.MULTIPLY).setValue("2"));
        updates.add(new FieldUpdate().setColumn("three").setAction(SQLSetAction.ADD).setValue("3"));
        updates.add(new FieldUpdate().setColumn("four").setAction(SQLSetAction.SUBTRACT).setValue("4"));

        String result = SQLClauseGenerator.generateSetClause(updates);
        Assert.assertEquals(expected, result);
    }
}
