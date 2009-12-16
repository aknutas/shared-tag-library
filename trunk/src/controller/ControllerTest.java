package controller;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Vector;

import network.ControlImpl;

import org.junit.Test;

import butler.HeadButler;

import data.*;
import database.QueryBuilderImpl;

public class ControllerTest {

    @Test
    public void testController() {
	Controller cont;
	cont = new Controller();
	assertTrue(cont.qb != null);
	assertTrue(cont.myLib != null);
	assertTrue(cont.cntrl != null);
	assertTrue(cont.modifiedBs != null);
	assertTrue(cont.connections != null);
	assertTrue(cont.HB != null);
    }

    @Test
    public void testreadInLibrary() {
	Controller cont = new Controller();
	cont.readInLibrary("0110output.txt");
    }

    @Test
    public void testwriteOut() {
	Controller cont = new Controller();
	cont.writeOut("0110testwriteout.txt");
    }
}
