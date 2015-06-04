package org.richfaces.tests.metamer.ftest.richColumnGroup;

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.ColumnGroupDT;
import org.testng.annotations.Test;

public class TestColumnGroupHeaderFooterDT extends TestColumnGroupHeaderFooter {

    @FindBy(css = ".rf-dt[id$=richDataTable]")
    private ColumnGroupDT table;

    @Override
    public String getComponentTestPagePath() {
        return "richColumnGroup/headerFooter.xhtml";
    }

    @Test
    @Templates("plain")
    public void testRendered() {
        super.testRendered();
    }

    @Override
    protected ColumnGroupDT getTable() {
        return table;
    }
}
