/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geoproject;

import geoproject.Coverage;
import geoproject.CoverageLongs;
import geoproject.Direction;
import geoproject.GeoHash;
import geoproject.LatLong;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author KGJQ6312
 */
public class GeoHashTest {
    
    public GeoHashTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of adjacentHash method, of class GeoHash.
     */
    @Test
    public void testAdjacentHash_String_Direction() {
        System.out.println("adjacentHash");
        String hash = "";
        Direction direction = null;
        String expResult = "";
        String result = GeoHash.adjacentHash(hash, direction);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of right method, of class GeoHash.
     */
    @Test
    public void testRight() {
        System.out.println("right");
        String hash = "";
        String expResult = "";
        String result = GeoHash.right(hash);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of left method, of class GeoHash.
     */
    @Test
    public void testLeft() {
        System.out.println("left");
        String hash = "";
        String expResult = "";
        String result = GeoHash.left(hash);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of top method, of class GeoHash.
     */
    @Test
    public void testTop() {
        System.out.println("top");
        String hash = "";
        String expResult = "";
        String result = GeoHash.top(hash);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of bottom method, of class GeoHash.
     */
    @Test
    public void testBottom() {
        System.out.println("bottom");
        String hash = "";
        String expResult = "";
        String result = GeoHash.bottom(hash);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of adjacentHash method, of class GeoHash.
     */
    @Test
    public void testAdjacentHash_3args() {
        System.out.println("adjacentHash");
        String hash = "";
        Direction direction = null;
        int steps = 0;
        String expResult = "";
        String result = GeoHash.adjacentHash(hash, direction, steps);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of neighbours method, of class GeoHash.
     */
    @Test
    public void testNeighbours() {
        System.out.println("neighbours");
        String hash = "";
        List<String> expResult = null;
        List<String> result = GeoHash.neighbours(hash);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of encodeHash method, of class GeoHash.
     */
    @Test
    public void testEncodeHash_double_double() {
        System.out.println("encodeHash");
        double latitude = 0.0;
        double longitude = 0.0;
        String expResult = "";
        String result = GeoHash.encodeHash(latitude, longitude);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of encodeHash method, of class GeoHash.
     */
    @Test
    public void testEncodeHash_LatLong_int() {
        System.out.println("encodeHash");
        LatLong p = null;
        int length = 0;
        String expResult = "";
        String result = GeoHash.encodeHash(p, length);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of encodeHash method, of class GeoHash.
     */
    @Test
    public void testEncodeHash_LatLong() {
        System.out.println("encodeHash");
        LatLong p = null;
        String expResult = "";
        String result = GeoHash.encodeHash(p);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of encodeHash method, of class GeoHash.
     */
    @Test
    public void testEncodeHash_3args() {
        System.out.println("encodeHash");
        double latitude = 0.0;
        double longitude = 0.0;
        int length = 0;
        String expResult = "";
        String result = GeoHash.encodeHash(latitude, longitude, length);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fromLongToString method, of class GeoHash.
     */
    @Test
    public void testFromLongToString() {
        System.out.println("fromLongToString");
        long hash = 0L;
        String expResult = "";
        String result = GeoHash.fromLongToString(hash);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of encodeHashToLong method, of class GeoHash.
     */
    @Test
    public void testEncodeHashToLong() {
        System.out.println("encodeHashToLong");
        double latitude = 0.0;
        double longitude = 0.0;
        int length = 0;
        long expResult = 0L;
        long result = GeoHash.encodeHashToLong(latitude, longitude, length);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of decodeHash method, of class GeoHash.
     */
    @Test
    public void testDecodeHash() {
        System.out.println("decodeHash");
        String geohash = "";
        LatLong expResult = null;
        LatLong result = GeoHash.decodeHash(geohash);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashLengthToCoverBoundingBox method, of class GeoHash.
     */
    @Test
    public void testHashLengthToCoverBoundingBox() {
        System.out.println("hashLengthToCoverBoundingBox");
        double topLeftLat = 0.0;
        double topLeftLon = 0.0;
        double bottomRightLat = 0.0;
        double bottomRightLon = 0.0;
        int expResult = 0;
        int result = GeoHash.hashLengthToCoverBoundingBox(topLeftLat, topLeftLon, bottomRightLat, bottomRightLon);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashContains method, of class GeoHash.
     */
    @Test
    public void testHashContains() {
        System.out.println("hashContains");
        String hash = "";
        double lat = 0.0;
        double lon = 0.0;
        boolean expResult = false;
        boolean result = GeoHash.hashContains(hash, lat, lon);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of coverBoundingBox method, of class GeoHash.
     */
    @Test
    public void testCoverBoundingBox_4args() {
        System.out.println("coverBoundingBox");
        double topLeftLat = 0.0;
        double topLeftLon = 0.0;
        double bottomRightLat = 0.0;
        double bottomRightLon = 0.0;
        Coverage expResult = null;
        Coverage result = GeoHash.coverBoundingBox(topLeftLat, topLeftLon, bottomRightLat, bottomRightLon);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of coverBoundingBoxMaxHashes method, of class GeoHash.
     */
    @Test
    public void testCoverBoundingBoxMaxHashes() {
        System.out.println("coverBoundingBoxMaxHashes");
        double topLeftLat = 0.0;
        double topLeftLon = 0.0;
        double bottomRightLat = 0.0;
        double bottomRightLon = 0.0;
        int maxHashes = 0;
        Coverage expResult = null;
        Coverage result = GeoHash.coverBoundingBoxMaxHashes(topLeftLat, topLeftLon, bottomRightLat, bottomRightLon, maxHashes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of coverBoundingBox method, of class GeoHash.
     */
    @Test
    public void testCoverBoundingBox_5args() {
        System.out.println("coverBoundingBox");
        double topLeftLat = 0.0;
        double topLeftLon = 0.0;
        double bottomRightLat = 0.0;
        double bottomRightLon = 0.0;
        int length = 0;
        Coverage expResult = null;
        Coverage result = GeoHash.coverBoundingBox(topLeftLat, topLeftLon, bottomRightLat, bottomRightLon, length);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of coverBoundingBoxLongs method, of class GeoHash.
     */
    @Test
    public void testCoverBoundingBoxLongs() {
        System.out.println("coverBoundingBoxLongs");
        double topLeftLat = 0.0;
        double topLeftLon = 0.0;
        double bottomRightLat = 0.0;
        double bottomRightLon = 0.0;
        int length = 0;
        CoverageLongs expResult = null;
        CoverageLongs result = GeoHash.coverBoundingBoxLongs(topLeftLat, topLeftLon, bottomRightLat, bottomRightLon, length);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of heightDegrees method, of class GeoHash.
     */
    @Test
    public void testHeightDegrees() {
        System.out.println("heightDegrees");
        int n = 0;
        double expResult = 0.0;
        double result = GeoHash.heightDegrees(n);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of widthDegrees method, of class GeoHash.
     */
    @Test
    public void testWidthDegrees() {
        System.out.println("widthDegrees");
        int n = 0;
        double expResult = 0.0;
        double result = GeoHash.widthDegrees(n);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of gridAsString method, of class GeoHash.
     */
    @Test
    public void testGridAsString_3args() {
        System.out.println("gridAsString");
        String hash = "";
        int size = 0;
        Set<String> highlightThese = null;
        String expResult = "";
        String result = GeoHash.gridAsString(hash, size, highlightThese);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of gridAsString method, of class GeoHash.
     */
    @Test
    public void testGridAsString_5args() {
        System.out.println("gridAsString");
        String hash = "";
        int fromRight = 0;
        int fromBottom = 0;
        int toRight = 0;
        int toBottom = 0;
        String expResult = "";
        String result = GeoHash.gridAsString(hash, fromRight, fromBottom, toRight, toBottom);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of gridAsString method, of class GeoHash.
     */
    @Test
    public void testGridAsString_6args() {
        System.out.println("gridAsString");
        String hash = "";
        int fromRight = 0;
        int fromBottom = 0;
        int toRight = 0;
        int toBottom = 0;
        Set<String> highlightThese = null;
        String expResult = "";
        String result = GeoHash.gridAsString(hash, fromRight, fromBottom, toRight, toBottom, highlightThese);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
