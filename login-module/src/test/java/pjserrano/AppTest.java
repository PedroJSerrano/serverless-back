package pjserrano;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit test for simple App (updated for JUnit 5).
 */
public class AppTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testApp()
    {
        assertTrue( true ); // Una aserción simple para que el test pase
        assertNotNull("Some object", "This is an example assertion."); // Ejemplo de otra aserción
    }
}