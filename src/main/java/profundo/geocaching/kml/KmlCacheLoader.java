package profundo.geocaching.kml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.xml.sax.SAXException;

import profundo.kml.GcKml;
import profundo.kml.GcKmlHandler;
import profundo.pushparser.XmlPushParser;

public class KmlCacheLoader {
	private String url;

	public KmlCacheLoader(String url) {
		try {
			this.url = searchURL(url);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public KmlCacheLoader() {
		this(getKmlUrl());
	}

	public GcKml load(float bboxWest, float bboxSouth, float bboxEast,
			float bboxNorth) {
		try {
			String url = search(bboxWest, bboxSouth, bboxEast, bboxNorth);
			GcKmlHandler kml = new GcKmlHandler();
			XmlPushParser parser = new XmlPushParser(kml);
			parser.parse(new URL(url).openStream());
			return kml.getGckml();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	

	private String search(float bboxWest, float bboxSouth, float bboxEast,
			float bboxNorth) {
		return url + "?" + viewFormat(bboxWest, bboxSouth, bboxEast, bboxNorth);
	}

	protected String viewFormat(float bboxWest, float bboxSouth,
			float bboxEast, float bboxNorth) {
		return "BBOX=" + bboxWest + "," + bboxSouth + "," + bboxEast + ","
				+ bboxNorth;
	}

	public String searchURL(String url) throws MalformedURLException {
		if (url.endsWith("/create")) {
			url = url.substring(0, url.length() - "/create".length())
					+ "/search";
		}
		return url;
	}

	public static void main(String[] args) throws SAXException, IOException {
		KmlCacheLoader kml = new KmlCacheLoader();
		GcKml load = kml.load(10.1762f, 56.0631f, 10.2081f, 56.0729f);

		System.out.println(load.placemarks);
	}

	private static String getKmlUrl()  {
		try {
			URL url = KmlCacheLoader.class.getResource("/META-INF/kml.link");
			BufferedReader r = new BufferedReader(new InputStreamReader(url
					.openStream(), "UTF-8"));
			String kmllink = r.readLine();
			r.close();
			return kmllink;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
}
