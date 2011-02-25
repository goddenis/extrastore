package ru.extrastore;

import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;
import ru.extrastore.model.Price;
import ru.extrastore.model.Product;

import java.util.HashSet;

import static org.testng.AssertJUnit.assertEquals;

/**
 * http://extrastore.ru
 * Created by Ксюха on 25.02.11 at 18:26
 */
public class PriceTest extends SeamTest {
    @Test
    public void testPrice() throws Exception {
        final Product p = new Product();
        p.setId(1);
        p.setPrice(120);
        p.setPrices(new HashSet<Price>());

        final Price main = new Price();
        main.setMain(true);
        main.setOld(false);
        main.setValue(100);
        main.setId(1);
        main.setProduct(p);
        p.getPrices().add(main);

        final Price old = new Price();
        old.setMain(false);
        old.setOld(true);
        old.setValue(130);
        old.setId(2);
        old.setProduct(p);
        p.getPrices().add(old);

        final Price club = new Price();
        club.setMain(false);
        club.setOld(false);
        club.setValue(80);
        club.setDescription("для членов клуба");
        club.setId(3);
        club.setProduct(p);
        p.getPrices().add(club);

        new ComponentTest() {
            @Override
            protected void testComponents() throws Exception {
                assertEquals("main price", main.getValue(), p.getMainPrice());
                assertEquals("old price", old.getValue(), p.getOldPrice());
                assertEquals("club price", club.getValue(), p.getExtraPrices().iterator().next().getValue());
            }
        }.run();
    }
}
