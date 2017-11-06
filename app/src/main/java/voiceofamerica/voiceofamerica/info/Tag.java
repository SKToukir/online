package voiceofamerica.voiceofamerica.info;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by vivek on 21/5/16.
 */
//==============================================
public class Tag {

    private final static  int tagSize = 128;
    private final static String META_INT = "icy-metaint";
    private final static String STREAM_TITLE = "StreamTitle";

    private String title;
    private String artist;
    private String album;
    private String year;
    private String comment;
    private byte genre;

    //===========================
    public static int getTagSize() { return tagSize; }
    public String getTitle() { return title;}
    public void setTitle(String title) { this.title = title.replaceAll("'","");}
    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist;}
    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album;}
    public String getYear() { return year;}
    public void setYear(String year) { this.year = year;}
    public String getComment() { return comment;}
    public void setComment(String comment) { this.comment = comment;}
    public byte getGenre() { return genre;}
    public void setGenre(byte genre) { this.genre = genre; }

    //===========================
    public static Tag fetch(String rurl) throws IOException {

        //=== Fetch ICY INT
        URL url = new URL(rurl);
        URLConnection conn = url.openConnection();
        conn.addRequestProperty("Icy-MetaData", "1");
        conn.connect();
        Map<String, List<String>> hList = conn.getHeaderFields();

        if (hList.containsKey(META_INT)) {
            int discard = Integer.parseInt(hList.get(META_INT).get(0) + "");
            InputStream is = conn.getInputStream();
            long read = 0;
            long skipped = is.skip(discard);
            while (skipped != discard) {
                skipped += is.skip(discard - skipped);
            }
            //=== Read 1st byte
            int fb = is.read();
            //=== Read metadata, first byte *16
            int mdl = fb * 16;
            if (mdl > 0) {
                read = 0;
                String md = "";
                do {
                    byte[] mdb = new byte[mdl - (int) read];
                    read = is.read(mdb);
                    md += new String(mdb, 0, mdb.length);
                } while (read != mdl);

                String metdata = new String(md.trim().getBytes(), "utf-8");
                //=== Extract
                String[] data = metdata.split(";");
                Tag t = new Tag();
                for (String tag:data){
                    if(tag.startsWith(STREAM_TITLE))
                        t.setTitle(tag.split("=")[1]);
                }
                return t;
            }
        }

        return null;
    }
}
