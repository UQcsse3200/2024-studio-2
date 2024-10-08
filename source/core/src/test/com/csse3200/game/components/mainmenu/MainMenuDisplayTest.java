package com.csse3200.game.components.mainmenu;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.extensions.GameExtension;

@ExtendWith(GameExtension.class)
public class MainMenuDisplayTest {
    private String[] owlFacts;

    // Simulates the method to set up owl facts
    private void setupOwlFacts() {
        owlFacts = new String[]{
                "A dogs nose print is as unique as a human fingerprint.",
                "Crocodiles have been around for over 200 million years!",
                "Some birds, like the Arctic Tern, migrate over 40,000 miles a year.",
                "Dogs can understand up to 250 words and gestures.",
                "Crocs can gallop on land like a horse for short bursts!",
                "The owl can rotate its head 270 degrees without moving its body.",
                "Dogs can smell diseases like cancer and diabetes!",
                "A crocodiles bite is the strongest in the animal kingdom.",
                "Parrots can mimic human speech better than any other animal.",
                "A Greyhound can reach speeds of 45 mph!",
                "The heart of a hummingbird beats over 1,200 times per minute!"
        };
    }

    @Before
    public void setUp() {
        setupOwlFacts();
    }

    @Test
    public void testOwlFactsArrayIsNotNull() {
        assertNotNull("Owl facts array should not be null", owlFacts);
    }

    @Test
    public void testOwlFactsArrayHasCorrectLength() {
        int expectedLength = 11;
        assertEquals("Owl facts array should have 11 entries", expectedLength, owlFacts.length);
    }

    @Test
    public void testOwlFactsArrayContainsExpectedFact() {
        String expectedFact = "The owl can rotate its head 270 degrees without moving its body.";
        boolean containsExpectedFact = false;
        for (String fact : owlFacts) {
            if (fact.equals(expectedFact)) {
                containsExpectedFact = true;
                break;
            }
        }
        assertTrue("Owl facts array should contain the expected fact", containsExpectedFact);
    }

    @Test
    public void testOwlFactsAreUnique() {
        for (int i = 0; i < owlFacts.length; i++) {
            for (int j = i + 1; j < owlFacts.length; j++) {
                assertNotEquals("Each owl fact should be unique", owlFacts[i], owlFacts[j]);
            }
        }
    }

}
