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

        mediaActivityViewModel.addMedia(new Media("Name1", "Title1", 1, "Type1"));
        assertEquals(1, mediaActivityViewModel.getMediaDatabase().getValue().size());

        mediaActivityViewModel.addMedia(new Media("Name2", "Title2", 2, "Type2"));
        assertEquals(2, mediaActivityViewModel.getMediaDatabase().getValue().size());

        mediaActivityViewModel.addMedia(new Media("Name3", "Title3", 3, "Type3"));
        assertEquals(3, mediaActivityViewModel.getMediaDatabase().getValue().size());
    }
}