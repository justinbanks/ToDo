package com.todo.group1.todo.data;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * Created by Justin Banks on 7/25/16.
 */
public class ToDoDbHelperTest {

    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.Test
    public void testOnCreate() throws Exception {
        assertThat(4, is(4));
    }
}