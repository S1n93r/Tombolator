package com.example.tombolator.media;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.junit.Assert.assertEquals;

public class MediaActivityViewModelTest {

    private boolean changeRegistered = false;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Test
    public void testAddMedia() {

        MediaActivityViewModel mediaActivityViewModel = new MediaActivityViewModel();

        mediaActivityViewModel.addMedia(new Media(1, "Test"));
        assertEquals(1, mediaActivityViewModel.getMediaDatabase().getValue().size());

        mediaActivityViewModel.addMedia(new Media(3, "Testorino"));
        assertEquals(2, mediaActivityViewModel.getMediaDatabase().getValue().size());

        mediaActivityViewModel.addMedia(new Media(7, "Testamento"));
        assertEquals(3, mediaActivityViewModel.getMediaDatabase().getValue().size());
    }
}