package Test;

import geoPackage.Coverage;
import geoPackage.GeoHash;

public class GeoHashTestRunner {

	public static void main(String[] args) {
		//Coverage x = null;
		//System.out.println(x);
		//System.out.println(GeoHash.encodeHash(23, -23,1));
		//LatLong l = new LatLong(42.8, -24.43);
		//System.out.println(GeoHash.encodeHash(l,4));
		//System.out.println(GeoHash.encodeHashToLong(42.8, -24.43, 4));
		//long x = 7924312242776965124L;
		//System.out.println(GeoHash.fromLongToString(x));
		//System.out.println(GeoHash.decodeHash("erwe").toString());
		System.out.println(GeoHash.widthDegrees(-410));
		/*Coverage x = GeoHash.coverBoundingBoxMaxHashes(90, 180, -90, -180,1);
		System.out.println(x);
		x = GeoHash.coverBoundingBox(90, 180, -90, -180,4);
		System.out.println(x);
		/*System.out.println(GeoHash.hashLengthToCoverBoundingBox(23.5, 1.5, 23.0, 1.0));
		System.out.println(GeoHash.coverBoundingBox(23.5, 1.5, 23.0, 1.0,2).getHashes().size());
		System.out.println(GeoHash.coverBoundingBox(23.5, 1.5, 23.0, 1.0,2).getRatio());
		System.out.println(GeoHash.coverBoundingBox(23.5, 1.5, 23.0, 1.0,2));
		System.out.println(GeoHash.coverBoundingBox(23.5, 1.5, 23.0, 1.0,3).getHashes().size());
		System.out.println(GeoHash.coverBoundingBox(23.5, 1.5, 23.0, 1.0,3).getRatio());
		System.out.println(GeoHash.coverBoundingBox(23.5, 1.5, 23.0, 1.0,3));
		System.out.println(GeoHash.coverBoundingBox(23.5, 1.5, 23.0, 1.0,4).getHashes().size());
		System.out.println(GeoHash.coverBoundingBox(23.5, 1.5, 23.0, 1.0,4).getRatio());
		System.out.println(GeoHash.coverBoundingBox(23.5, 1.5, 23.0, 1.0,4));
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Coverage x = GeoHash.coverBoundingBox(23.5, 1.5, 23.0, 1.0,5);
		for (String geohash : x.getHashes()) {
			System.out.println(geohash);
		}
		System.out.println(x.getRatio());
		System.out.println(x.getHashes().size());
		System.out.println();
		
		/*Coverage y = GeoHash.coverBoundingBox(23.5, 1.5, 23.0, 1.0,6);
		for (String geohash : y.getHashes()) {
			System.out.println(geohash);
		}
		System.out.println(y.getRatio());
		System.out.println(y.getHashes().size());
		System.out.println();
		
		/*x = GeoHash.coverBoundingBox(23.5, 1.5, 23.0, 1.0,7);
		for (String geohash : x.getHashes()) {
			System.out.println(geohash);
		}
		System.out.println(x.getRatio());
		System.out.println(x.getHashes().size());
		System.out.println();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//System.out.println(GeoHash.hashContains("ss", 22.337673, 31.624296));
		//System.out.println(GeoHash.right("-1"));
	}

}
