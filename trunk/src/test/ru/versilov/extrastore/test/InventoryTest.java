package ru.versilov.extrastore.test;

import org.testng.annotations.Test;
import ru.versilov.extrastore.model.Inventory;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 14.10.2009
 * Time: 23:50:47
 * To change this template use File | Settings | File Templates.
 */
public class InventoryTest {

    @Test
    public void testInventory() {
        Inventory inv = new Inventory();

        inv.setInventoryId(3);
        assert inv.getInventoryId() == 3;

        inv.setQuantity(10);

        boolean success = inv.order(3);
        assert success;

        assert inv.getSales() == 3;
        assert inv.getQuantity() == (10-3);
    }
}
