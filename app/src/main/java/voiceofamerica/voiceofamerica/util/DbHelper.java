package voiceofamerica.voiceofamerica.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mwongela on 5/12/16.
 */
public class DbHelper {

    private static final String TAG = DbHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 19;
    private static final String DATABASE_NAME = "vamerica.voiceofamerica";

    //toukir.best.vamerica.voiceofamerica
    private static final String TABLE_WEBRADIO = "tbl";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_RADIO_STATION_NAME = "radio_station_name";
    public static final String KEY_RADIO_URL = "radio_url";
    public static final String KEY_MUSIC_GENRE = "music_genre";
    public static final String KEY_FAV = "favourite";
    public static final String KEY_ORDER = "rorder";

    private DatabaseOpenHelper openHelper;
    private SQLiteDatabase database;

    public DbHelper(Context aContext) {
        openHelper = new DatabaseOpenHelper(aContext);
        database = openHelper.getWritableDatabase();
    }

    public Cursor getAllWebradios(){
        String buildSQL = "SELECT * FROM " + TABLE_WEBRADIO + " ORDER BY "+ KEY_ORDER+" desc , "+KEY_RADIO_STATION_NAME+ " ASC";
        Cursor systems =  database.rawQuery(buildSQL, null);
        systems.moveToFirst();
        return systems;
    }

    public Cursor getAllWebradios(String query){
        String buildSQL = "SELECT * FROM " + TABLE_WEBRADIO + " WHERE "+KEY_RADIO_STATION_NAME+" LIKE '%"+query+"%' OR "+KEY_MUSIC_GENRE+" LIKE '%"+query+"%' ORDER BY "+ KEY_ORDER+" desc , "+KEY_RADIO_STATION_NAME+ " ASC";
        Log.e(TAG, buildSQL);
        Cursor systems =  database.rawQuery(buildSQL, null);
        systems.moveToFirst();
        return systems;
    }

    public Cursor getFavoriteWebradios(){
        String buildSQL = "SELECT * FROM " + TABLE_WEBRADIO + " WHERE "+KEY_FAV+" = 1 ORDER BY "+ KEY_ORDER+" desc , "+KEY_RADIO_STATION_NAME+ " ASC";
        Cursor systems =  database.rawQuery(buildSQL, null);
        systems.moveToFirst();

        return systems;
    }

    public int getRadioFavStatus (String rowId) {
        String buildSQL = "SELECT * FROM " + TABLE_WEBRADIO +" WHERE " + KEY_ROWID + " = '"+rowId+"'";
        Cursor c =  database.rawQuery(buildSQL, null);
        c.moveToFirst();
        return c.getInt(c.getColumnIndex(c.getColumnName(4)));
    }

    public int getRadioId(String rowId) {
        String buildSQL = "SELECT * FROM " + TABLE_WEBRADIO +" WHERE " + KEY_RADIO_STATION_NAME + " = '"+rowId+"'";
        Cursor c =  database.rawQuery(buildSQL, null);
        c.moveToFirst();
        return c.getInt(c.getColumnIndex(c.getColumnName(0)));
    }

    public String getRadioURL (String RadioName) {
        String buildSQL = "SELECT * FROM " + TABLE_WEBRADIO +" WHERE " + KEY_RADIO_STATION_NAME+ " = '"+RadioName+"'";
        Cursor c =  database.rawQuery(buildSQL, null);
        c.moveToFirst();
        return c.getString(c.getColumnIndex(c.getColumnName(2)));
    }

    public String getRadioName (String rowId) {
        String buildSQL = "SELECT * FROM " + TABLE_WEBRADIO +" WHERE " + KEY_ROWID + " = '"+rowId+"'";
        Cursor c =  database.rawQuery(buildSQL, null);
        c.moveToFirst();
        return c.getString(c.getColumnIndex(c.getColumnName(1)));
    }

    public void updateRadioFavStatus(String rowId, String status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FAV, status);
        database.update(TABLE_WEBRADIO, contentValues, KEY_ROWID + " = " + rowId, null);
        Log.e(TAG, "Set "+getRadioName(rowId)+" to "+status);
    }

    private class DatabaseOpenHelper extends SQLiteOpenHelper {

        public DatabaseOpenHelper(Context aContext) {
            super(aContext, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            String buildWebradioSQL ="create table " + TABLE_WEBRADIO + " ("
                    + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_RADIO_STATION_NAME + " text, "
                    + KEY_RADIO_URL + " text, "
                    + KEY_MUSIC_GENRE + " text, "
                    + KEY_FAV + " text DEFAULT 0,"
                    + KEY_ORDER+" int default 0);";
            Log.d(TAG, "onCreate SQL: " + buildWebradioSQL);
            sqLiteDatabase.execSQL(buildWebradioSQL);

            //Insert webradios
            String insertWebradioSQL1 ="INSERT INTO " + TABLE_WEBRADIO + " ("
                    + KEY_RADIO_STATION_NAME + ", "
                    + KEY_RADIO_URL + ", "
                    + KEY_MUSIC_GENRE +") VALUES "+


                    //old radio stations

//                    "(\"Ado Club\", \"http://adoclub.bcast.infomaniak.ch/adoclub-128.mp3\", \"Club, Electro\"),"+
//                    "(\"Ado FM\", \"http://start-adofm.ice.infomaniak.ch/start-adofm-high.mp3\", \"RnB, Rap\"),"+
//                    "(\"Ado RnB US\", \"http://adornbus.bcast.infomaniak.ch/adornbus-128.mp3\",\"Hits, RnB\"),"+
//                    "(\"Ado Rap FR\", \"http://adorapfr.bcast.infomaniak.ch/adorapfr-128.mp3\", \"Rap\"),"+
//                    "(\"Ado Rap US\", \"http://adorapus.bcast.infomaniak.ch/adorapus-128.mp3\", \"Rap\"),"+
//                    "(\"Beur FM\", \"http://broadcast.infomaniak.ch/beurfm-high.mp3\", \"Oriental\"),"+
//                    "(\"Beur FM 100% Kabyle\", \"http://broadcast.infomaniak.ch/beurfm-kabyle-high.mp3\", \"Oriental\"),"+
//                    "(\"Beur FM 100% Maroc\", \"http://broadcast.infomaniak.ch/beurfm-marocain-high.mp3\", \"Oriental\"),"+
//                    "(\"Beur FM 100% Oriental\", \"http://broadcast.infomaniak.ch/beurfm-annonces-high.mp3\", \"Oriental\"),"+
//                    "(\"Beur FM 100% Raï\", \"http://broadcast.infomaniak.ch/beurfm-rai-high.mp3\", \"Oriental\"),"+
//                    "(\"BFM Business\", \"http://bfmbusiness.scdn.arkena.com/bfmbusiness.mp3\", \"Business, Finance, Information, News\"),"+
//                    "(\"Chérie FM\", \"http://adwzg3.tdf-cdn.com/8473/nrj_178499.mp3\", \"Pop\"),"+
//                    "(\"Classic & Jazz Radio\", \"http://classicandjazzaac.ice.infomaniak.ch/classicandjazzaac.mp3\", \"Classique, Jazz, Classical\"),"+
//                    "(\"Classic & Jazz Radio Chic & Frenchy\", \"http://chicetfrenchy.ice.infomaniak.ch/chicetfrenchy-128.mp3\", \"Chansons française, Classique, Jazz, Classical, French songs\"),"+
//                    "(\"Classic & Jazz Radio Smooth & Relax\", \"http://smoothandrelax.ice.infomaniak.ch/smoothandrelax-128.mp3\", \"Classique, Jazz, Relax, Classical\"),"+
//                    "(\"Classic & Jazz Radio Soul & Funk\", \"http://soulandfunkaac.ice.infomaniak.ch/soulandfunkaac-0.mp3\", \"Classique, Funk, Jazz, Classical\"),"+
//                    "(\"Contact FM\", \"http://radio-contact.ice.infomaniak.ch/radio-contact-high.mp3\", \"Club, Dance, Electro, Hits\"),"+
//                    "(\"Europe 1\", \"http://e1-live-mp3-128.scdn.arkena.com/europe1.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"FIP\", \"http://audio.scdn.arkena.com/11016/fip-midfi128.mp3\", \"Hits, Jazz, Musique du monde, Rock, World music\"),"+
//                    "(\"France Bleu 107.1\", \"http://audio.scdn.arkena.com/11719/fb1071-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Culture\", \"http://audio.scdn.arkena.com/11010/franceculture-midfi128.mp3\", \"Actualité, Culture, Nexs\"),"+
//                    "(\"France Info\", \"http://audio.scdn.arkena.com/11006/franceinfo-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Inter\", \"http://audiots.scdn.arkena.com/11591/franceinter-midfi128TS.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Maghreb 2\", \"http://francemaghreb2.ice.infomaniak.ch/francemaghreb2-high.mp3\", \"Oriental\"),"+
//                    "(\"France Musique\", \"http://audio.scdn.arkena.com/11012/francemusique-midfi128.mp3\", \"classique, Classical\"),"+
//                    "(\"Fun Radio\", \"http://streaming.radio.funradio.fr/fun-1-48-192\", \"Dance, Hits, House, Club\"),"+
//                    "(\"Generations 88.2\", \"http://generationfm.ice.infomaniak.ch/generationfm-high.mp3\", \"Rap\"),"+
//                    "(\"Guadeloupe 1ère\", \"http://guadeloupe-la1ere.scdn.arkena.com/live.mp3\", \"Antilles, Radio d'Outre-mer, Tropiques, West Indies\"),"+
//                    "(\"Guyane 1ère\", \"http://guyane-la1ere.scdn.arkena.com/live.mp3\", \"Radio d'Outre-mer, Tropical music\"),"+
//                    "(\"Martinique 1ère\", \"http://martinique-la1ere.scdn.arkena.com/live.mp3\", \"Antilles, Radio d'Outre-mer, West Indies\"),"+
//                    "(\"Mayotte 1ère\", \"http://mayotte-la1ere.scdn.arkena.com/live.mp3\", \"Radio d'Outre-mer, Tropical music\"),"+
//                    "(\"MFM Radio\", \"http://mfm.ice.infomaniak.ch/mfm-128.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"Nostalgie\", \"http://95.81.155.24/8472/nrj_172793.mp3\", \"Années 60, Années 70, Années 80, Chansons françaises, French songs, Oldies\"),"+
//                    "(\"NRJ\", \"http://95.81.147.24/8470/nrj_165631.mp3\", \"Club, Dance, Hits, House, Pop, Rock\"),"+
//                    "(\"Radio FG\", \"http://radiofg.impek.com/fg\", \"Electro, House\"),"+
//                    "(\"Radio Latina\", \"http://broadcast.infomaniak.net/start-latina-high.mp3\", \"Latino, Latin music, Latin music\"),"+
//                    "(\"Radio Nova\", \"http://novazz.ice.infomaniak.ch/novazz-128.mp3\", \"Musique du monde, World music\"),"+
//                    "(\"RFM\", \"http://rfm-live-mp3-128.scdn.arkena.com/rfm.mp3\", \"Actualité, Années 80, Hits, Information, News\"),"+
//                    "(\"Rire et Chansons\", \"http://adwzg5.scdn.arkena.com/10832/nrj_173735.mp3\", \"Humour\"),"+
//                    "(\"RTL\", \"http://icecast.rtl.fr/rtl-1-48-72.aac\", \"Actualité, Information, News\"),"+
//                    "(\"Skyrock\", \"http://mp3lg2.tdf-cdn.com/4603/sky_120728.mp3\", \"RnB, Rap\"),"+
//                    "(\"Voltage\", \"http://start-voltage.bcast.infomaniak.ch/start-voltage-high.mp3\", \"Hits\"),"+

                    //new radio stations
//                    "(\"Radio Accordéon\", \"http://streaming.radionomy.com/radio-accordeon\", \"Accordéon, Oldies\"),"+
//                    "(\"Chérie Acoustic\", \"http://adwzg3.scdn.arkena.com/8644/nrj_172974.mp3\", \"Acoustic\"),"+
//                    "(\"Alta Frequenza\", \"http://www.streamakaci.com/radios/player/playerstreamakV1.3.swf?serveur=altafrequenza&port=80&file=a\", \"Actualité,  Corse, Information, News\"),"+
//                    "(\"Africa N°1\", \"http://african1paris.ice.infomaniak.ch/african1paris-128.mp3\", \"Actualité, Afrique, Information, News\"),"+
//                    "(\"RFI Afrique\", \"http://rfi-afrique-96k.scdn.arkena.com/rfiafrique-96k.mp3\", \"Actualité, Afrique, Information, News\"),"+
//                    "(\"RFM\", \"http://rfm-live-mp3-128.scdn.arkena.com/rfm.mp3\", \"Actualité, Années 80, Hits, Information, News\"),"+
//                    "(\"France Culture\", \"http://audio.scdn.arkena.com/11010/franceculture-midfi128.mp3\", \"Actualité, Culture,Nexs\"),"+
//                    "(\"RFI Musique\", \"http://rfi-afrique-96k.scdn.arkena.com/rfiafrique-96k.mp3\", \"Actualité, Culture,Nexs\"),"+
//                    "(\"77 FM\", \"http://91.121.150.196:8000/;stream.mp3\", \"Actualité, Hits, News\"),"+
//                    "(\"Delta FM Boulogne 100.7\", \"http://deltafmboulogne.ice.infomaniak.ch:80/deltafmboulogne-high\", \"Actualité, Hits, News\"),"+
//                    "(\"Delta FM Dunkerque 100.7\", \"http://deltafmsaintomer.ice.infomaniak.ch:80/deltafmsaintomer-high\", \"Actualité, Hits, News\"),"+
//                    "(\"Delta FM Saint Omer 98.8\", \"http://deltafmsaintomer.ice.infomaniak.ch:80/deltafmsaintomer-high\", \"Actualité, Hits, News\"),"+
//                    "(\"Tendance Ouest\", \"http://streaming.tendanceouest.com/tomanche.mp3\", \"Actualité, Hits, News\"),"+
//                    "(\"Tendance Ouest Calvados\", \"http://streaming.tendanceouest.com/tocalvados.mp3\", \"Actualité, Hits, News\"),"+
//                    "(\"Tendance Ouest Manche\", \"http://streaming.tendanceouest.com/tomanche.mp3\", \"Actualité, Hits, News\"),"+
//                    "(\"Tendance Ouest Normandie\", \"http://streaming.tendanceouest.com/toorne.mp3\", \"Actualité, Hits, News\"),"+
//                    "(\"ARL (Aquitaine Radio Live)\", \"http://arlaquitaineradiolive.ice.infomaniak.ch/arlaquitaineradiolive-128.mp3\", \"Actualité, Hits, Sport, News\"),"+
//                    "(\"SUN Radio\", \"http://80.82.229.202/sunhd.mp3\", \"Actualité, Hits, Sport, News\"),"+
//                    "(\"SUN Radio Franco Sun\", \"http://80.82.229.202/francosun.mp3\", \"Actualité, Hits, Sport, News\"),"+
//                    "(\"SUN Radio Sun Sport\", \"http://80.82.229.202/sunsports.mp3\", \"Actualité, Hits, Sport, News\"),"+
//                    "(\"Europe 1\", \"http://e1-live-mp3-128.scdn.arkena.com/europe1.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu 107.1\", \"http://audio.scdn.arkena.com/11719/fb1071-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Alsace\", \"http://audio.scdn.arkena.com/11356/fbalsace-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Armorique\", \"http://audio.scdn.arkena.com/11358/fbarmorique-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Auxerre\", \"http://audio.scdn.arkena.com/11360/fbauxerre-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Azur\", \"http://audio.scdn.arkena.com/11361/fbazur-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Béarn \", \"http://audio.scdn.arkena.com/11364/fbbearn-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Belfort-Montbéliard\", \"http://audio.scdn.arkena.com/11365/fbbelfort-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Berry\", \"http://audio.scdn.arkena.com/11366/fbberry-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Besançon\", \"http://audio.scdn.arkena.com/11367/fbbesancon-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Bourgogne\", \"http://audio.scdn.arkena.com/11368/fbbourgogne-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Breizh Izel\", \"http://audio.scdn.arkena.com/11369/fbbreizizel-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Champagne-Ardenne\", \"http://audio.scdn.arkena.com/11370/fbchampagne-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Cotentin\", \"http://audio.scdn.arkena.com/11372/fbcotentin-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Creuse\", \"http://audio.scdn.arkena.com/11373/fbcreuse-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Drôme Ardèche\", \"http://audio.scdn.arkena.com/11357/fbdromeardeche-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Elsass\", \"http://audio.scdn.arkena.com/11607/Fbelsass-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Gard Lozère\", \"http://audio.scdn.arkena.com/11374/fbgardlozere-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Gascogne\", \"http://audio.scdn.arkena.com/11375/fbgascogne-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Gironde\", \"http://audio.scdn.arkena.com/11376/fbgironde-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Haute Normandie\", \"http://audio.scdn.arkena.com/11377/fbhautenorm&ie-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Hérault\", \"http://audio.scdn.arkena.com/11378/fbherault-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Isère\", \"http://audio.scdn.arkena.com/11379/fbisere-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu La Rochelle\", \"http://audio.scdn.arkena.com/11391/fblarochelle-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Limousin\", \"http://audio.scdn.arkena.com/11380/fblimousin-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Loire Océan\", \"http://audio.scdn.arkena.com/11381/fbloireocean-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Lorraine Nord\", \"http://audio.scdn.arkena.com/11382/fblorrainenord-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Maine\", \"http://audio.scdn.arkena.com/11384/fbmaine-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Mayenne\", \"http://audio.scdn.arkena.com/11383/fbmayenne-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Nord\", \"http://audio.scdn.arkena.com/11385/fbnord-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Normandie (Calvados - Orne)\", \"http://audio.scdn.arkena.com/11363/fbbassenormandie-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Orléans\", \"http://audio.scdn.arkena.com/11386/fborleans-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Pays Basque\", \"http://audio.scdn.arkena.com/11362/fbpaysbasque-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Pays d’Auvergne\", \"http://audio.scdn.arkena.com/11359/fbpaysdauvergne-midfi128.mp3\", \"Actualité, Information, News\"),"+
                    "(\"SHEI\", \"http://188.166.176.244:8919/index.html?sid=1\", \"ABC, ABC\"),"+
                    "(\"France Bleu Perigord\", \"http://audio.scdn.arkena.com/11387/fbperigord-midfi128.mp3\", \"Actualité, Information, News\");";

            //break

            String insertWebradioSQL2 ="INSERT INTO " + TABLE_WEBRADIO + " ("
                    + KEY_RADIO_STATION_NAME + ", "
                    + KEY_RADIO_URL + ", "
                    + KEY_MUSIC_GENRE +") VALUES "+

//                    "(\"France Bleu Picardie\", \"http://audio.scdn.arkena.com/11388/fbpicardie-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Poitou\", \"http://audio.scdn.arkena.com/11389/fbpoitou-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Provence\", \"http://audio.scdn.arkena.com/11390/fbprovence-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu RCFM Frequenza Mora\", \"http://audio.scdn.arkena.com/11371/fbfrequenzamora-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Roussillon\", \"http://audio.scdn.arkena.com/11393/fbroussillon-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Saint-Étienne-Loire\", \"http://audio.scdn.arkena.com/11392/fbstetienne-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Sud Lorraine\", \"http://audio.scdn.arkena.com/11395/fbsudlorraine-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Toulouse\", \"http://audio.scdn.arkena.com/11396/fbtoulouse-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Touraine\", \"http://audio.scdn.arkena.com/11397/fbtouraine-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Bleu Vaucluse\", \"http://audio.scdn.arkena.com/11398/fbvaucluse-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Info\", \"http://audio.scdn.arkena.com/11006/franceinfo-midfi128.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"France Inter\", \"http://audiots.scdn.arkena.com/11591/franceinter-midfi128TS.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"RFI Monde\", \"http://rfi-monde-96k.scdn.arkena.com/rfimonde-96k.mp3\", \"Actualité, Information, News\"),"+
//                    "(\"RTL\", \"http://icecast.rtl.fr/rtl-1-48-72.aac\", \"Actualité, Information, News\"),"+
//                    "(\"Sud Radio\", \"http://sudradioplus.ice.infomaniak.ch/sudradioplus.mp3\", \"Actualité, Information, Sport, News\"),"+
//                    "(\"Alpes 1 Grenoble\", \"http://alpes1grenoble.ice.infomaniak.ch/alpes1grenoble-high.mp3\", \"Actualité, Radio locale, News\"),"+
//                    "(\"RMC\", \"http://rmc.scdn.arkena.com/rmc.mp3\", \"Actualité, Sport, News\"),"+
//                    "(\"NRJ Africa\", \"http://triton.scdn.arkena.com/13279/live.mp3\", \"Afrique, Africa\"),"+
//                    "(\"Radio Pulse\", \"http://bendelz.net:8000//;stream.nsv\", \"Alençon, Electro, Musique alternative, Radio locale, Local service\"),"+
//                    "(\"Radio G!\", \"http://80.82.229.202/radiog.mp3\", \"Angers, Radio associative, Community radio\"),"+
//                    "(\"HotmixRadio 2000\", \"http://streamingads.hotmixradio.fm/hotmixradio-2k-128.mp3\", \"Années 2000, Hits, 00s music\"),"+
//                    "(\"MFM Radio Années 2000\", \"http://mfm-wr12.ice.infomaniak.ch/mfm-wr12.mp3\", \"Années 2000, Hits, 00s music\"),"+
//                    "(\"NRJ Hits 2000\", \"http://triton.scdn.arkena.com/14782/live.mp3\", \"Années 2000, Hits, 00s music\"),"+
//                    "(\"Voltage 2000\", \"http://voltage2000.bcast.infomaniak.ch/voltage2000-128.mp3\", \"Années 2000, Hits, 00s music\"),"+
//                    "(\"Wit 2000\", \"http://wit2000.ice.infomaniak.ch/wit2000.mp3\", \"Années 2000, Hits, 00s music\"),"+
//                    "(\"HotmixRadio Golds\", \"http://streamingads.hotmixradio.fm/hotmixradio-golds-128.mp3\", \"Années 60, Année 70, Oldies\"),"+
//                    "(\"Nostalgie\", \"http://95.81.155.24/8472/nrj_172793.mp3\", \"Années 60, Années 70, Années 80, Chansons françaises, French songs, Oldies\"),"+
//                    "(\"Radio Emotion\", \"http://radioemotion.ice.infomaniak.ch/radioemotion-128.mp3\", \"Années 60, Années 70, Années 80, Hits, Oldies\"),"+
//                    "(\"RFM Collector\", \"http://rfm-wr1-mp3-128.scdn.arkena.com/rfm.mp3\", \"Années 60, Années 70, Années 80, Hits, Oldies\"),"+
//                    "(\"Radiocéan\", \"http://stream.rvm.fr:8004/;radiocean.mp3\", \"Années 60, Années 70, Années 80, Local service, Oldies\"),"+
//                    "(\"MFM Radio Culte 60/70\", \"http://mfm-wr15.ice.infomaniak.ch/mfm-wr15.mp3\", \"Années 60, Années 70, Chansons françaises, French songs, Hits, Oldies\"),"+
//                    "(\"Fip autour de Gainsbourg\", \"http://webradiofip6.scdn.arkena.com/webradiofip6.mp3\", \"Années 70, Année 80, Chansons françaises, French songs, Oldies\"),"+
//                    "(\"4U 70s Flowers\", \"http://streaming.radionomy.com/4U-70s\", \"Années 70, Rock, Oldies\"),"+
//                    "(\"Fun Radio 80-90\", \"http://stream.funradio.sk:8000/80-90-128.mp3\", \"Années 80, Années 90, Hits, Oldies\"),"+
//                    "(\"Allzic Radio Années 80\", \"http://allzic08.ice.infomaniak.ch/allzic08.mp3\", \"Années 80, Hits, 80s music\"),"+
//                    "(\"Chérie 80s\", \"http://adwzg3.scdn.arkena.com/8600/nrj_168310.mp3\", \"Années 80, Hits, 80s music\"),"+
//                    "(\"HotmixRadio 80\"\"s\", \"http://streamingads.hotmixradio.fm/hotmixradio-80-128.mp3\", \"Années 80, Hits, 80s music\"),"+
//                    "(\"M2 80\", \"http://live.m2stream.fr/m280-128.mp3\", \"Années 80, Hits, 80s music\"),"+
//                    "(\"Magic Radio 80\"\"s\", \"http://icecast.pulsradio.com/magicradioHD.mp3\", \"Années 80, Hits, 80s music\"),"+
//                    "(\"RFM 80\"\"s\", \"http://rfm-wr4-mp3-128.scdn.arkena.com/rfm.mp3\", \"Années 80, Hits, 80s music\"),"+
//                    "(\"Tendance Ouest 80\"\"s\", \"http://streaming.tendanceouest.com/quatrevingt.mp3\", \"Années 80, Hits, 80s music\"),"+
//                    "(\"Voltage 80\"\"S\", \"http://voltage80s.bcast.infomaniak.ch/voltage80s-128.mp3\", \"Années 80, Hits, 80s music\"),"+
//                    "(\"Wit 80\"\"s\", \"http://wit80s.ice.infomaniak.ch/wit80s.mp3\", \"Années 80, Hits, 80s music\"),"+
//                    "(\"4U 80s\", \"http://streaming.radionomy.com/4u-80s\", \"Années 80, Hits, Rock, 80s music\"),"+
//                    "(\"Allzic Radio Années 90\", \"http://allzic24.ice.infomaniak.ch/allzic24.mp3\", \"Années 90, Hits, 90s music\"),"+
//                    "(\"Chérie 90s\", \"http://adwzg3.scdn.arkena.com/8601/nrj_172773.mp3\", \"Années 90, Hits, 90s music\"),"+
//                    "(\"HotmixRadio 90\"\"s\", \"http://streamingads.hotmixradio.fm/hotmixradio-90-128.mp3\", \"Années 90, Hits, 90s music\"),"+
//                    "(\"M2 90\", \"http://live.m2stream.fr/m290-128.mp3\", \"Années 90, Hits, 90s music\"),"+
//                    "(\"NRJ Hits 90\", \"http://triton.scdn.arkena.com/14782/live.mp3\", \"Années 90, Hits, 90s music\"),"+
//                    "(\"Puls\"\" Radio 90\"\"S\", \"http://icecast.pulsradio.com/puls90HD.mp3\", \"Années 90, Hits, 90s music\"),"+
//                    "(\"Radio Espace Dance 90\", \"http://espace-disco.ice.infomaniak.ch/espace-disco-high.mp3\", \"Années 90, Hits, 90s music\"),"+
//                    "(\"RFM Party 90\", \"http://rfm-wr5-mp3-128.scdn.arkena.com/rfm.mp3\", \"Années 90, Hits, 90s music\"),"+
//                    "(\"Voltage 90\"\"S\", \"http://voltage90s.bcast.infomaniak.ch/voltage90s-128.mp3\", \"Années 90, Hits, 90s music\"),"+
//                    "(\"Wit 90\"\"s\", \"http://wit90s.ice.infomaniak.ch/wit90s.mp3\", \"Années 90, Hits, 90s music\"),"+
//                    "(\"RCI Dancehall\", \"http://ns347811.ip-5-196-91.eu:8000/rci-dancehall.mp3\", \"Antilles, Dancehall, Radio d\"\"Outre-mer, West Indies\"),"+
//                    "(\"RCI Latino\", \"http://ns347811.ip-5-196-91.eu:8000/rci-latino.mp3\", \"Antilles, Latino, Radio d\"\"Outre-mer, West Indies\"),"+
//                    "(\"Kréol FM\", \"http://91.121.107.206:8700/kreol.mp3\", \"Antilles, Radio d\"\"Outre-mer, Tropiques, West Indies\"),"+
//                    "(\"Guadeloupe 1ère\", \"http://guadeloupe-la1ere.scdn.arkena.com/live.mp3\", \"Antilles, Radio d\"\"Outre-mer, Tropiques, West Indies\"),"+
//                    "(\"Martinique 1ère\", \"http://martinique-la1ere.scdn.arkena.com/live.mp3\", \"Antilles, Radio d\"\"Outre-mer, West Indies\"),"+
//                    "(\"Ouepa FM\", \"http://streaming101.radionomy.com/RadionoMiX\", \"Antilles, Radio d\"\"Outre-mer, West Indies\"),"+
//                    "(\"RCI Compas\", \"http://ns347811.ip-5-196-91.eu:8000/rci-compas.mp3\", \"Antilles, Radio d\"\"Outre-mer, West Indies\"),"+
//                    "(\"RCI Guadeloupe\", \"http://91.121.24.209:8000/rcigp.mp3\", \"Antilles, Radio d\"\"Outre-mer, West Indies\"),"+
//                    "(\"RCI Guadeloupe\", \"http://91.121.24.209:8000/rcigp.mp3\", \"Antilles, Radio d\"\"Outre-mer, West Indies\"),"+
//                    "(\"RCI Kassav\"\"\", \"http://ns347811.ip-5-196-91.eu:8000/rci-kassav.mp3\", \"Antilles, Radio d\"\"Outre-mer, West Indies\"),"+
//                    "(\"RCI Martinique\", \"http://94.23.9.13:8000/stream\", \"Antilles, Radio d\"\"Outre-mer, West Indies\"),"+
//                    "(\"RCI Martinique\", \"http://94.23.9.13:8000/stream\", \"Antilles, Radio d\"\"Outre-mer, West Indies\"),"+
//                    "(\"RCI Tradition\", \"http://ns347811.ip-5-196-91.eu:8000/rci-tradition.mp3\", \"Antilles, Radio d\"\"Outre-mer, West Indies\"),"+
//                    "(\"RCI Zouk\", \"http://ns347811.ip-5-196-91.eu:8000/rci-zouk.mp3\", \"Antilles, Radio d\"\"Outre-mer, West Indies\"),"+
//                    "(\"Zouk FM Guadeloupe\", \"http://sv2.vestaradio.com:4890/;stream.mp3\", \"Antilles, Radio d\"\"Outre-mer, West Indies, Zouk\"),"+
//                    "(\"Zouk FM Martinique\", \"http://sv3.vestaradio.com:6540/;stream.mp3\", \"Antilles, Radio d\"\"Outre-mer, West Indies, Zouk\"),"+
//                    "(\"Ayp FM\", \"http://stric6.streamakaci.com/radioayp.mp3\", \"Arménie, Armenia\"),"+
//                    "(\"Allzic Radio Blues\", \"http://allzic09.ice.infomaniak.ch/allzic09.mp3\", \"Blues\"),"+
//                    "(\"OUÏ FM Blues\"\"N\"\"rock\", \"http://ouifmbluesnrock.ice.infomaniak.ch/ouifmbluesnrock-128.mp3\", \"Blues\"),"+
//                    "(\"W3 blue & jazz Radio\", \"http://w3bluesradio.com:8000/live\", \"Blues, Funk, Jazz, Soul\"),"+
//                    "(\"Jazz Radio Blues\", \"http://jazzblues.ice.infomaniak.ch/jazzblues-high.mp3\", \"Blues, Jazz\"),"+
//                    "(\"Allzic Radio Black music\", \"http://allzic04.ice.infomaniak.ch/allzic04.mp3\", \"Blues, Jazz, Rap, RnB, Zouk\"),"+
//                    "(\"O2 Radio\", \"http://91.121.176.26:7900/;\", \"Bordeaux, Radio associative, Community radio\"),"+
//                    "(\"Radio Bro Gwened\", \"http://rbg.online.radiobreizh.bzh/live.mp3\", \"Bretagne, Radio associative, Breton language service, Community radio\"),"+
//                    "(\"Radio Laser\", \"http://217.128.133.215:8000/stream.mp3\", \"Bretagne, Radio associative, Breton language service, Community radio\"),"+
//                    "(\"Radio Sing Sing\", \"http://stream.sing-sing.org:8000/singsing128?type=.mp3\", \"Bretagne, Radio associative, Breton language service, Community radio\"),"+
//                    "(\"Arvorig FM\", \"http://arvorig-fm.online.stalig.net/live.mp3\", \"Bretagne, Radio locale, Local service\"),"+
//                    "(\"Mélody FM\", \"http://stream.melodiefm.net:8000/;\", \"Bretagne, Radio locale, Breton language service, Local service\"),"+
//                    "(\"Radio Caroline\", \"http://195.154.113.150:8000/caroline.mp3\", \"Bretagne, Radio locale, Breton language service, Local service\"),"+
//                    "(\"Radio Kerne\", \"http://bhs-partage.online.radiobreizh.net:10028/live-1.mp3\", \"Bretagne, Radio locale, Breton language service, Local service\"),"+
//                    "(\"Radio Nord Bretagne\", \"http://radionordbretagne.ice.infomaniak.ch:80/radionordbretagne\", \"Bretagne, Radio locale, Breton language service, Local service\"),"+
//                    "(\"BFM Business\", \"http://bfmbusiness.scdn.arkena.com/bfmbusiness.mp3\", \"Business, Finance, Information, News\"),"+
//                    "(\"Radio Occitania\", \"http://radio-occitania.tetaneutral.net:8000/occitania.mp3\", \"Catalan,Occitane, Radio locale, Local service\"),"+
//                    "(\"Classic & Jazz Radio Chic & Frenchy\", \"http://chicetfrenchy.ice.infomaniak.ch/chicetfrenchy-128.mp3\", \"Chansons française, Classique, Jazz, Classical, French songs\"),"+
//                    "(\"Chante France\", \"http://stream1.chantefrance.com/Chante_France\", \"Chansons françaises, French songs\"),"+
//                    "(\"Chante France 60\"\"s\", \"http://stream1.chantefrance.com/cf-60\", \"Chansons françaises, French songs\"),"+
//                    "(\"Chante France 70\"\"s\", \"http://stream1.chantefrance.com/cf-70\", \"Chansons françaises, French songs\"),"+
//                    "(\"Chante France 80\"\"s\", \"http://stream1.chantefrance.com/cf-80\", \"Chansons françaises, French songs\"),"+
//                    "(\"Chante France Emotion\", \"http://stream1.chantefrance.com/cf-emotion\", \"Chansons françaises, French songs\"),"+
//                    "(\"HotmixRadio Frenchy\", \"http://streamingads.hotmixradio.fm/hotmixradio-frenchy-128.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"Impact FM\", \"http://impactfm.ice.infomaniak.ch:80/impactfm-64.aac\", \"Chansons françaises, French songs\"),"+
//                    "(\"MFM Radio\", \"http://mfm.ice.infomaniak.ch/mfm-128.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"MFM Radio 100% Céline Dion\", \"http://mfm-wr16.ice.infomaniak.ch/mfm-wr16.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"MFM Radio 100% Comédies musicales\", \"http://mfm-wr05.ice.infomaniak.ch/mfm-wr05.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"MFM Radio 100% Enfoirés\", \"http://mfm-wr09.ice.infomaniak.ch/mfm-wr09.mp3\", \"Chansons françaises, French songs\"),"+
                    "(\"Voot\", \"http://dl.bhoot-fm.com/Bhoot-FM_2017-10-31_(Bhoot-FM.com).mp3\", \"ABC, ABC\"),"+
                    "(\"MFM Radio 100% Goldman\", \"http://dl.bhoot-fm.com/Bhoot-FM_2017-10-31_(Bhoot-FM.com).mp3\", \"Chansons françaises, French songs\");";

            String insertWebradioSQL3 ="INSERT INTO " + TABLE_WEBRADIO + " ("
                    + KEY_RADIO_STATION_NAME + ", "
                    + KEY_RADIO_URL + ", "
                    + KEY_MUSIC_GENRE +") VALUES "+
//                    "(\"MFM Radio Culte 80/90\", \"http://mfm-wr2.ice.infomaniak.ch/mfm-wr2-128.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"MFM Radio Culte Talents de la télé\", \"http://mfm-talents-tv.ice.infomaniak.ch/mfm-talents-tv.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"MFM Radio Duos\", \"http://mfm-wr06.ice.infomaniak.ch/mfm-wr06.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"MFM Radio Francophonie\", \"http://mfm-wr10.ice.infomaniak.ch/mfm-wr10.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"MFM Radio Génériques TV\", \"http://mfm-wr14.ice.infomaniak.ch/mfm-wr14.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"MFM Radio Nouvelle scène\", \"http://mfm-wr4.ice.infomaniak.ch/mfm-wr4-64.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"MFM Radio Romantique\", \"http://mfm-thema.ice.infomaniak.ch/mfm-thema.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"MFM Radio Séducteurs\", \"http://mfm-wr08.ice.infomaniak.ch/mfm-wr08.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"MFM Radio Tube du grenier\", \"http://mfm-wr3.ice.infomaniak.ch/mfm-wr3-128.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"MFM Radio Voix du Sud\", \"http://voix-du-sud.ice.infomaniak.ch/voix-du-sud.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"MFM Radio Voix féminines\", \"http://mfm-wr1.ice.infomaniak.ch/mfm-wr1-128.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"NRJ Made in France\", \"http://triton.scdn.arkena.com/13051/live.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"Radio Bonheur\", \"http://radiobonheur.ice.infomaniak.ch/radiobonheur-128-1.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"RFM spéciale Balavoine\", \"http://rfm-wr8-mp3-128.scdn.arkena.com/rfm.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"RFM spéciale Goldman\", \"http://rfm-wr12-mp3-128.scdn.arkena.com/rfm.mp3\", \"Chansons françaises, French songs\"),"+
//                    "(\"Paris Chanson\", \"http://streaming.radionomy.com/Paris-Chanson\", \"Chansons françaises, French songs, Oldies\"),"+
//                    "(\"Allzic Radio Années 50\", \"http://allzic29.ice.infomaniak.ch/allzic29.mp3\", \"Chansons françaises, French songs, Hits\"),"+
//                    "(\"Allzic Radio Enfoirés\", \"http://allzic12.ice.infomaniak.ch/allzic12.mp3\", \"Chansons françaises, French songs, Hits\"),"+
//                    "(\"Allzic Radio Golds français\", \"http://allzic30.ice.infomaniak.ch/allzic30.mp3\", \"Chansons françaises, French songs, Hits\"),"+
//                    "(\"Chérie Frenchy\", \"http://adwzg3.scdn.arkena.com/8569/nrj_177648.mp3\", \"Chansons françaises, French songs, Hits\"),"+
//                    "(\"Allzic Radio Nouveautés françaises\", \"http://allzic23.ice.infomaniak.ch/allzic23.mp3\", \"Chansons françaises, Nouveautés, French songs, New\"),"+
//                    "(\"MFM Radio Nouveautés\", \"http://mfm-wr13.ice.infomaniak.ch/mfm-wr13.mp3\", \"Chansons françaises, Nouveautés, French songs, New\"),"+
//                    "(\"ABC Stars Français\", \"http://streaming.radionomy.com/ABCStarsFrancais\", \"Chansons françaises, Rock, French songs\"),"+
//                    "(\"Johnny Hallyday le Web\", \"http://streaming.radionomy.com/Johnny-Hallyday-Le-Web\", \"Chansons françaises, Rock, French songs\"),"+
//                    "(\"Accent 4\", \"http://str0.creacast.com/accent4\", \"classique, Classical\"),"+
//                    "(\"Allzic Radio Classic\", \"http://allzic06.ice.infomaniak.ch/allzic06.mp3\", \"classique, Classical\"),"+
//                    "(\"France Musique\", \"http://audio.scdn.arkena.com/11012/francemusique-midfi128.mp3\", \"classique, Classical\"),"+
//                    "(\"M2 Classic\", \"http://live.m2stream.fr/m2classic-128.mp3\", \"classique, Classical\"),"+
//                    "(\"Radio Classique\", \"http://radioclassique.ice.infomaniak.ch/radioclassique-high.mp3\", \"classique, Classical\"),"+
//                    "(\"Radio Neptune\", \"http://radios.infini.fr:8000/neptune\", \"classique, Classical\"),"+
//                    "(\"Classic & Jazz Radio Soul & Funk\", \"http://soulandfunkaac.ice.infomaniak.ch/soulandfunkaac-0.mp3\", \"Classique, Funk, Jazz, Classical\"),"+
//                    "(\"Classic & Jazz Radio\", \"http://classicandjazzaac.ice.infomaniak.ch/classicandjazzaac.mp3\", \"Classique, Jazz, Classical\"),"+
//                    "(\"Classic & Jazz Radio Smooth & Relax\", \"http://smoothandrelax.ice.infomaniak.ch/smoothandrelax-128.mp3\", \"Classique, Jazz, Relax, Classical\"),"+
//                    "(\"Elium Radio Club & Dance\", \"http://streaming.radionomy.com/Elium-ClubDance\", \"Club, Dance\"),"+
//                    "(\"HotmixRadio Game\", \"http://streamingads.hotmixradio.fm/hotmixradio-game-128.mp3\", \"Club, Dance, Electro\"),"+
//                    "(\"Contact FM\", \"http://radio-contact.ice.infomaniak.ch/radio-contact-high.mp3\", \"Club, Dance, Electro, Hits\"),"+
//                    "(\"NRJ\", \"http://95.81.147.24/8470/nrj_165631.mp3\", \"Club, Dance, Hits, House, Pop, Rock\"),"+
//                    "(\"Ado Club\", \"http://adoclub.bcast.infomaniak.ch/adoclub-128.mp3\", \"Club, Electro\"),"+
//                    "(\"M2 Club\", \"http://live.m2stream.fr/m2club-128.mp3\", \"Club, Electro\"),"+
//                    "(\"MX Radio Club\", \"http://icecast.pulsradio.com/mxHD.mp3\", \"Club, Electro\"),"+
//                    "(\"NRJ Clubbin\"\"\", \"http://triton.scdn.arkena.com/13133/live.mp3\", \"Club, Electro\"),"+
//                    "(\"Paris-One Club\", \"http://streaming.radionomy.com/parisoneclub\", \"Club, Electro\"),"+
//                    "(\"Paris-One Dance\", \"http://streaming.radionomy.com/parisonedance\", \"Club, Electro\"),"+
//                    "(\"Paris-One Deeper\", \"http://streaming.radionomy.com/parisonedeeper\", \"Club, Electro\"),"+
//                    "(\"Paris-One Reverse\", \"http://streaming.radionomy.com/parisonereverse\", \"Club, Electro\"),"+
//                    "(\"Paris-One Trance\", \"http://streaming.radionomy.com/parisonetrance\", \"Club, Electro\"),"+
//                    "(\"Radio Espace Club\", \"http://espace100.ice.infomaniak.ch/espace100-high.mp3\", \"Club, Electro\"),"+
//                    "(\"Tendance Ouest Club\", \"http://streaming.tendanceouest.com/club.mp3\", \"Club, Electro\"),"+
//                    "(\"Voltage CLUB\", \"http://voltageclub.bcast.infomaniak.ch/voltageclub-128.mp3\", \"Club, Electro\"),"+
//                    "(\"NRJ Club Hits\", \"http://triton.scdn.arkena.com/13097/live.mp3\", \"Club, Hits\"),"+
//                    "(\"Wit Club\", \"http://witclub.ice.infomaniak.ch/witclub.mp3\", \"Club, Hits\"),"+
//                    "(\"Allzic Radio Comptines\", \"http://allzic05.ice.infomaniak.ch/allzic05.mp3\", \"Comptines, Enfants, Young persons programming\"),"+
//                    "(\"Chante France Comptines\", \"http://stream1.chantefrance.com/cf-comptines\", \"Comptines, Enfants, Young persons programming\"),"+
//                    "(\"MFM Radio Comptines\", \"http://mfm-wr11.ice.infomaniak.ch/mfm-wr11.mp3\", \"Comptines, Enfants, Young persons programming\"),"+
//                    "(\"Corsica Radio\", \"http://92.243.25.139:9000/;stream.mp3\", \"Corse, Radio locale, Local service\"),"+
//                    "(\"Allzic Radio Country\", \"http://allzic32.ice.infomaniak.ch/allzic32.mp3\", \"Corse, Radio locale, Local service\"),"+
//                    "(\"Music Box\", \"http://www.musicboxtv.com:8000/live\", \"Country, Rock\"),"+
//                    "(\"NRJ Cover Hits\", \"http://triton.scdn.arkena.com/13031/live.mp3\", \"Cover, Hits\"),"+
//                    "(\"HotmixRadio Dance\", \"http://streamingads.hotmixradio.fm/hotmixradio-dance-128.mp3\", \"Dance\"),"+
//                    "(\"NRJ Dance\", \"http://triton.scdn.arkena.com/13095/live.mp3\", \"Dance\"),"+
//                    "(\"NRJ Dance 2000\", \"http://triton.scdn.arkena.com/14786/live.mp3\", \"Dance\"),"+
//                    "(\"NRJ Dance 90\", \"http://triton.scdn.arkena.com/13275/live.mp3\", \"Dance\"),"+
//                    "(\"NRJ Extravadance\", \"http://triton.scdn.arkena.com/13195/live.mp3\", \"Dance\"),"+
//                    "(\"Radio Espace Dance Floor\", \"http://espace-dancefloor.ice.infomaniak.ch/espace-dancefloor-high.mp3\", \"Dance\"),"+
//                    "(\"Blackbox Club\", \"http://blackboxclub.bcast.infomaniak.ch/blackboxclub-128.mp3\", \"Dance, Club, House\"),"+
//                    "(\"Allzic Radio Dancefloor\", \"http://allzic07.ice.infomaniak.ch/allzic07.mp3\", \"Dance, Electro\"),"+
//                    "(\"Fun Radio Dance\", \"http://stream.funradio.sk:8000/dance128.mp3\", \"Dance, Electro\"),"+
//                    "(\"Only 1 Radio\", \"http://195.154.115.97/point_de_montage_stream_1\", \"Dance, Electro\"),"+
//                    "(\"Puls\"\" Radio Dance\", \"http://193.200.42.208/;\", \"Dance, Electro\"),"+
//                    "(\"Radio FG Deep Dance\", \"http://radiofg.impek.com/fgd\", \"Dance, Electro\"),"+
//                    "(\"Flash FM\", \"http://flashfm.ice.infomaniak.ch/flashfm-128.mp3\", \"Dance, Hits\"),"+
//                    "(\"Hit Party Dance\", \"http://icecast.pulsradio.com/hitpartyHD.mp3\", \"Dance, Hits\"),"+
//                    "(\"La La Radio\", \"http://lalaradio.ice.infomaniak.ch/lalaradio-high.mp3\", \"Dance, Hits\"),"+
//                    "(\"M2 Hit\", \"http://live.m2stream.fr/m2hit-128.mp3\", \"Dance, Hits\"),"+
//                    "(\"MTI\", \"http://radiomti.ice.infomaniak.ch/radiomti.mp3\", \"Dance, Hits\"),"+
//                    "(\"NRJ Dance Hits\", \"http://triton.scdn.arkena.com/13093/live.mp3\", \"Dance, Hits\"),"+
//                    "(\"Planète FM\", \"http://planetefm.ice.infomaniak.ch/planetefm-64.aac\", \"Dance, Hits\"),"+
//                    "(\"Radio Attitude\", \"http://178.32.222.164:8002/;stream.mp3\", \"Dance, Hits\"),"+
//                    "(\"Radio Sensations Bernay\", \"http://sensationsnormandie.ice.infomaniak.ch/sensations-normandie.mp3\", \"Dance, Hits\"),"+
//                    "(\"RTS FM\", \"http://str0.creacast.com/rts-montpellier\", \"Dance, Hits\"),"+
//                    "(\"Toulouse FM\", \"http://toulousefm.scdn.arkena.com/toulousefm.mp3\", \"Dance, Hits\"),"+
//                    "(\"Fun Radio\", \"http://streaming.radio.funradio.fr/fun-1-48-192\", \"Dance, Hits, House, Club\"),"+
//                    "(\"NRJ Mad Mag\", \"http://triton.scdn.arkena.com/15361/live_aac.mp3\", \"Dance, Hits, Pop\"),"+
//                    "(\"NRJ Music Tour\", \"http://triton.scdn.arkena.com/15085/live_aac.mp3\", \"Dance, Hits, Pop\"),"+
//                    "(\"NRJ Broken Heart\", \"http://triton.scdn.arkena.com/13145/live.mp3\", \"Dance, Hits, Pop, Rap, RnB\"),"+
//                    "(\"Voltage @WORK\", \"http://voltagework.bcast.infomaniak.ch/voltagework-128.mp3\", \"Dance, Hits, Pop, Rap, RnB\"),"+
//                    "(\"Wit @Work\", \"http://witwork.ice.infomaniak.ch/witwork.mp3\", \"Dance, Hits, Pop, Rap, RnB\"),"+
//                    "(\"Métropolys\", \"http://str0.creacast.com/metropolys\", \"Dance, Pop\"),"+
//                    "(\"Atomic Radio\", \"http://188.165.196.212:8032/;stream/1\", \"Dance, Pop, Rock\"),"+
//                    "(\"Enjoy 33\", \"http://streaming.streamonomy.com/enjoy.mp3\", \"Dance, RnB\"),"+
//                    "(\"NRJ Pop R\"\"n\"\"B Dance\", \"http://triton.scdn.arkena.com/13033/live.mp3\", \"Dance, RnB\"),"+
//                    "(\"HotmixRadio Sunny\", \"http://streamingads.hotmixradio.fm/hotmixradio-sunny-128.mp3\", \"Dancehall, Ragga\"),"+
//                    "(\"NRJ Ragga Dancehall\", \"http://triton.scdn.arkena.com/13287/live.mp3\", \"Dancehall, Ragga\"),"+
//                    "(\"Ado Dancehall\", \"http://adodancehall.bcast.infomaniak.ch/adodancehall-128.mp3\", \"Dancehall, Reggae\"),"+
//                    "(\"Blackbox Dancehall\", \"http://blackboxdancehall.bcast.infomaniak.ch/blackboxdancehall-128.mp3\", \"Dancehall, Reggae\"),"+
//                    "(\"M2 Sunshine\", \"http://live.m2stream.fr/m2sunshine-128.mp3\", \"Dancehall, Reggae\"),"+
//                    "(\"Allzic Radio Disco\", \"http://allzic36.ice.infomaniak.ch/allzic36.mp3\", \"Disco\"),"+
//                    "(\"RFM Night Fever\", \"http://audio.scdn.arkena.com/9134/lag_101525.mp3\", \"Disco\"),"+
//                    "(\"M2 Funk\", \"http://live.m2stream.fr/m2funk-128.mp3\", \"Disco, Funk\"),"+
                    "(\"ABC\", \"http://st2.zenorad.io:14424\", \"ABC, ABC\"),"+
                    "(\"NRJ Disney\", \"http://triton.scdn.arkena.com/13209/live.mp3\", \"Disney Channel\");";

            String insertWebradioSQL4 ="INSERT INTO " + TABLE_WEBRADIO + " ("
                    + KEY_RADIO_STATION_NAME + ", "
                    + KEY_RADIO_URL + ", "
                    + KEY_MUSIC_GENRE +") VALUES "+

//                    "(\"NRJ Dubstep\", \"http://triton.scdn.arkena.com/13271/live.mp3\", \"Dubstep, Electro\"),"+
//                    "(\"Allzic Radio Deep Disco\", \"http://allzic31.ice.infomaniak.ch/allzic31.mp3\", \"Electro\"),"+
//                    "(\"Allzic Radio Hipster\", \"http://allzic42.ice.infomaniak.ch/allzic42.mp3\", \"Electro\"),"+
//                    "(\"Galaxie FM 95.3\", \"http://91.121.22.39:8010/;stream.nsv\", \"Electro\"),"+
//                    "(\"NRJ DJ Awards 2015\", \"http://triton.scdn.arkena.com/13111/live.mp3\", \"Electro\"),"+
//                    "(\"NRJ Edm\", \"http://triton.scdn.arkena.com/13227/live.mp3\", \"Electro\"),"+
//                    "(\"NRJ Electro\", \"http://triton.scdn.arkena.com/13309/live.mp3\", \"Electro\"),"+
//                    "(\"Puls\"\" Radio Trance\", \"http://icecast.pulsradio.com/pulstranceHD.mp3\", \"Electro\"),"+
//                    "(\"NRJ Deep House\", \"http://triton.scdn.arkena.com/13239/live.mp3\", \"Electro, House\"),"+
//                    "(\"NRJ Future House\", \"http://triton.scdn.arkena.com/15020/live.mp3\", \"Electro, House\"),"+
//                    "(\"Radio FG\", \"http://radiofg.impek.com/fg\", \"Electro, House\"),"+
//                    "(\"Radio FG Belgique\", \"http://radiofg.impek.com/fga\", \"Electro, House\"),"+
//                    "(\"Radio FG Chic\", \"http://radiofg.impek.com/fgc\", \"Electro, House\"),"+
//                    "(\"Radio FG Club FG\", \"http://radiofg.impek.com/fg6\", \"Electro, House\"),"+
//                    "(\"Radio FG Maxximum 90\"\"s\", \"http://37.59.25.179:8405/Maxximum\", \"Electro, House\"),"+
//                    "(\"Radio FG Non Stop\", \"http://radiofg.impek.com/fge\", \"Electro, House\"),"+
//                    "(\"Radio FG Starter FG by Hakimakli\", \"http://radiofg.impek.com/fgv\", \"Electro, House\"),"+
//                    "(\"Radio FG Underground\", \"http://radiofg.impek.com/ufg\", \"Electro, House\"),"+
//                    "(\"Radio NTI\", \"http://streaming.radionti.com/nti-hd.mp3\", \"Electro, House\"),"+
//                    "(\"Virgin Radio ElectroShock\", \"http://vr-wr3-mp3-128.scdn.arkena.com/virginradio.mp3\", \"Electro, House\"),"+
//                    "(\"Virgin Radio New\", \"http://vr-wr1-mp3-128.scdn.arkena.com/virginradio.mp3\", \"Electro, Nouveautés, Pop\"),"+
//                    "(\"Mouv’\", \"http://audio.scdn.arkena.com/11014/mouv-midfi128.mp3\", \"Electro, Rap, RnB\"),"+
//                    "(\"Allzic Radio Enfant 0-4 ans\", \"http://allzic14.ice.infomaniak.ch/allzic14.mp3\", \"Enfants, Young persons programming\"),"+
//                    "(\"Allzic Radio Enfant 4-7 ans\", \"http://allzic014.ice.infomaniak.ch/allzic15.mp3\", \"Enfants, Young persons programming\"),"+
//                    "(\"Allzic Radio Enfant 7-12 ans\", \"http://allzic16.ice.infomaniak.ch/allzic16.mp3\", \"Enfants, Young persons programming\"),"+
//                    "(\"HotmixRadio BabymixRADIO\", \"http://streamingads.hotmixradio.fm/hotmixradio-baby-128.mp3\", \"Enfants, Young persons programming\"),"+
//                    "(\"Fréquence Terre Radionomy\", \"http://streaming.radionomy.com/PopNature\", \"Environnement, Nature\"),"+
//                    "(\"Allzic Radio Faire la fête\", \"http://allzic26.ice.infomaniak.ch/allzic26.mp3\", \"Fiesta\"),"+
//                    "(\"NRJ Fiesta\", \"http://triton.scdn.arkena.com/13165/live.mp3\", \"Fiesta\"),"+
//                    "(\"Chérie Party\", \"http://adwzg3.scdn.arkena.com/8639/nrj_172411.mp3\", \"Fiesta, Pop\"),"+
//                    "(\"Allzic Radio Fitness\", \"http://allzic13.ice.infomaniak.ch/allzic13.mp3\", \"Fitness\"),"+
//                    "(\"NRJ Hits for Running\", \"http://triton.scdn.arkena.com/13189/live.mp3\", \"Fitness, Hits, Running\"),"+
//                    "(\"Chérie Fitness\", \"http://adwzg3.scdn.arkena.com/8678/nrj_176534.mp3\", \"Fitness, Pop\"),"+
//                    "(\"Fun Radio Running\", \"http://stream.funradio.sk:8000/running128.mp3\", \"Fitness, Running\"),"+
//                    "(\"NRJ Fitness\", \"http://triton.scdn.arkena.com/13221/live.mp3\", \"Fitness, Running\"),"+
//                    "(\"NRJ Pour le Sport\", \"http://triton.scdn.arkena.com/15024/live.mp3\", \"Fitness, Running\"),"+
//                    "(\"RFM Run & Fit\", \"http://rfm-wr9-mp3-128.scdn.arkena.com/rfm.mp3\", \"Fitness, Running\"),"+
//                    "(\"4U Funky\", \"http://streaming.radionomy.com/4U-Funky-Classics\", \"Funk\"),"+
//                    "(\"Allzic Radio Funk\", \"http://allzic08.ice.infomaniak.ch/allzic11.mp3\", \"Funk\"),"+
//                    "(\"Generations Funk\", \"http://gene-wr05.ice.infomaniak.ch/gene-wr05.mp3\", \"Funk\"),"+
//                    "(\"NRJ Funky\", \"http://triton.scdn.arkena.com/13291/live.mp3\", \"Funk\"),"+
//                    "(\"Fip autour du groove\", \"http://webradiofip3.scdn.arkena.com/webradiofip3.mp3\", \"Funk, Groove, Soul\"),"+
//                    "(\"HotmixRadio Funky\", \"http://streamingads.hotmixradio.fm/hotmixradio-funky-128.mp3\", \"Funk, Pop\"),"+
//                    "(\"Gold FM\", \"http://goldfm.scdn.arkena.com/goldfm.mp3\", \"Girondins de Bordeaux, Radio locale, Sport, Local service\"),"+
//                    "(\"Allzic Radio Gothique\", \"http://allzic27.ice.infomaniak.ch/allzic27.mp3\", \"Gothique, Metal, Rock, Gothic\"),"+
//                    "(\"Vivre FM\", \"http://vivrefm.ice.infomaniak.ch/vivrefm-128.mp3\", \"Handicap, Information, News\"),"+
//                    "(\"Allzic Radio Hard & Heavy\", \"http://allzic18.ice.infomaniak.ch/allzic18.mp3\", \"Hard Roch, Metal, Rock\"),"+
//                    "(\"HotmixRadio Metal\", \"http://streamingads.hotmixradio.fm/hotmixradio-metal-128.mp3\", \"Hard Rock, Metal, Rock\"),"+
//                    "(\"4U Hard FM\", \"http://streaming.radionomy.com/4U-Hard-FM\", \"Hard rock, Rock\"),"+
//                    "(\"Activ Radio (Roanne)\", \"http://stream.activradio.com/roanne.mp3\", \"Hits\"),"+
//                    "(\"Activ Radio (Saint Etienne)\", \"http://stream.activradio.com/stetienne.mp3\", \"Hits\"),"+
//                    "(\"Ado Classic\", \"http://adoclassic.bcast.infomaniak.ch/adoclassic-128.mp3\", \"Hits\"),"+
//                    "(\"Allzic Radio TOP 10\", \"http://allzic22.ice.infomaniak.ch/allzic22.mp3\", \"Hits\"),"+
//                    "(\"Allzic Radio TOP 20\", \"http://allzic41.ice.infomaniak.ch/allzic41.mp3\", \"Hits\"),"+
//                    "(\"Azur FM\", \"http://37.58.75.166:8318/stream\", \"Hits\"),"+
//                    "(\"Cannes Radio\", \"http://cannesradio.ice.infomaniak.ch/cannesradio-128.mp3\", \"Hits\"),"+
//                    "(\"Champagne FM\", \"http://champagnefm.ice.infomaniak.ch/champagnefm-128-marne.mp3\", \"Hits\"),"+
//                    "(\"Chérie Ballads\", \"http://adwzg3.tdf-cdn.com/8716/nrj_166913.mp3\", \"Hits\"),"+
//                    "(\"Cocktail FM\", \"http://91.121.62.121:7450/;stream.nsv\", \"Hits\"),"+
//                    "(\"Direct FM\", \"http://stream.devclic.net:8200/;stream.nsv\", \"Hits\"),"+
//                    "(\"Evasion FM Oise\", \"http://stream1.evasionfm.com/Oise\", \"Hits\"),"+
//                    "(\"Evasion FM Paris\", \"http://stream1.evasionfm.com/Paris\", \"Hits\"),"+
//                    "(\"Flor FM\", \"http://stream.florfm.com:8000/florfm\", \"Hits\"),"+
//                    "(\"Forum\", \"http://broadcast.infomaniak.net/start-forum-high.mp3\", \"Hits\"),"+
//                    "(\"Fréquence 3\", \"http://hd.stream.frequence3.net/frequence3-256.mp3\", \"Hits\"),"+
//                    "(\"Fréquence 3 Gold\", \"http://hd.stream.frequence3.net/frequence3ac-256.mp3\", \"Hits\"),"+
//                    "(\"Fréquence Plus\", \"http://frequenceplus71.ice.infomaniak.ch/frequenceplus71-128.mp3\", \"Hits\"),"+
//                    "(\"Fun Radio Top 20\", \"http://stream.funradio.sk:8000/top20128.mp3\", \"hits\"),"+
//                    "(\"Hit West\", \"http://broadcast.infomaniak.ch/hitwest-high.mp3\", \"Hits\"),"+
//                    "(\"Hot Radio Bourgoin-Jallieu\", \"http://st8.hotradio.fr/stream82?1460306696061.aac\", \"Hits\"),"+
//                    "(\"Hot Radio Chambéry\", \"http://st8.hotradio.fr/stream62?1460306863861.aac\", \"Hits\"),"+
//                    "(\"Hot Radio Grenoble\", \"http://st8.hotradio.fr/stream72?1460306582319.aac\", \"Hits\"),"+
//                    "(\"Hot Radio Pontcharra - Allevard\", \"http://st8.hotradio.fr/stream32?1460306914721.aac\", \"Hits\"),"+
//                    "(\"Kiss FM Saint-Tropez/Monaco\", \"http://kissfm-mp3.scdn.arkena.com/live.mp3\", \"Hits\"),"+
//                    "(\"LOR\"\"FM\", \"http://broadcast.infomaniak.net/lorfm-128.mp3\", \"Hits\"),"+
//                    "(\"Magnum La Radio\", \"http://str0.creacast.com/magnum\", \"Hits\"),"+
//                    "(\"Mistral FM\", \"http://mistralfm.bcast.infomaniak.ch:8000/mistralfm-high.mp3\", \"Hits\"),"+
//                    "(\"Mona FM\", \"http://broadcast.infomaniak.net/monafm-high.mp3\", \"Hits\"),"+
//                    "(\"Montagne FM\", \"http://montagnefm.ice.infomaniak.ch/montagnefm-96.mp3 \", \"Hits\"),"+
//                    "(\"NRH Hits for School Break\", \"http://triton.scdn.arkena.com/13067/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Acoustic Hits\", \"http://triton.scdn.arkena.com/13169/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ All Hits All Styles\", \"http://triton.scdn.arkena.com/13021/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ At Home\", \"http://triton.scdn.arkena.com/13251/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ At Work\", \"http://triton.scdn.arkena.com/13263/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Back 2 School\", \"http://triton.scdn.arkena.com/13061/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Best Hits Ever\", \"http://triton.scdn.arkena.com/13047/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Beyonce\"\"s Hits\", \"http://triton.scdn.arkena.com/13125/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Big Hits 2015\", \"http://triton.scdn.arkena.com/14943/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Collège Hits\", \"http://triton.scdn.arkena.com/13201/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ EuroHot 30\", \"http://triton.scdn.arkena.com/13177/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Fall Hits 2015\", \"http://triton.scdn.arkena.com/14706/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Foot 2016\", \"http://triton.scdn.arkena.com/15249/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ French Hits\", \"http://triton.scdn.arkena.com/13051/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Futurs Hits\", \"http://triton.scdn.arkena.com/13191/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Good Night\", \"http://triton.scdn.arkena.com/13207/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Happy Hits\", \"http://triton.scdn.arkena.com/13313/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Hit of the Day\", \"http://triton.scdn.arkena.com/13035/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Hits\", \"http://triton.scdn.arkena.com/12681/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Hits for Girls\", \"http://triton.scdn.arkena.com/13149/live.mp3\", \"Hits\"),"+
                    "(\"ABC\", \"http://st2.zenorad.io:14424\", \"ABC, ABC\"),"+
                    "(\"NRJ Hits of the Month\", \"http://triton.scdn.arkena.com/13037/live.mp3\", \"Hits\");";

            String insertWebradioSQL5 ="INSERT INTO " + TABLE_WEBRADIO + " ("
                    + KEY_RADIO_STATION_NAME + ", "
                    + KEY_RADIO_URL + ", "
                    + KEY_MUSIC_GENRE +") VALUES "+

//                    "(\"NRJ Hits of the Week\", \"http://triton.scdn.arkena.com/13027/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Hits Preview\", \"http://triton.scdn.arkena.com/13115/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Hits Remix\", \"http://triton.scdn.arkena.com/13137/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ In Bed\", \"http://triton.scdn.arkena.com/13193/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Itunes\", \"http://triton.scdn.arkena.com/13071/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Millions of Hits\", \"http://triton.scdn.arkena.com/13073/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Most Played\", \"http://triton.scdn.arkena.com/13049/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Most Wanted\", \"http://triton.scdn.arkena.com/13171/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Mpokora\"\"s Hits\", \"http://triton.scdn.arkena.com/13301/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Music Awards\", \"http://triton.scdn.arkena.com/13023/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ No Repeat\", \"http://triton.scdn.arkena.com/13205/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Party Hits\", \"http://triton.scdn.arkena.com/13167/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Punchy Hits\", \"http://triton.scdn.arkena.com/13303/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ PURE Hits Only\", \"http://adwzg5.scdn.arkena.com/10738/nrj_172920.mp3\", \"Hits\"),"+
//                    "(\"NRJ Sentimental\", \"http://triton.scdn.arkena.com/13117/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Sexy\", \"http://triton.scdn.arkena.com/13311/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Snow Hits\", \"http://triton.scdn.arkena.com/14851/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Summer Hits 2015\", \"http://triton.scdn.arkena.com/13045/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Sunny Hits\", \"http://triton.scdn.arkena.com/13305/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Teen Hits\", \"http://triton.scdn.arkena.com/13299/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Teen Party\", \"http://triton.scdn.arkena.com/13069/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Twerk Hits\", \"http://triton.scdn.arkena.com/15016/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ UK Top 40\", \"http://triton.scdn.arkena.com/13187/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Urban Hits\", \"http://triton.scdn.arkena.com/13143/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ US Top 40\", \"http://triton.scdn.arkena.com/13101/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Wake Up\", \"http://triton.scdn.arkena.com/13203/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Winter Hits 2015\", \"http://triton.scdn.arkena.com/15002/live.mp3\", \"Hits\"),"+
//                    "(\"NRJ Youtube\", \"http://adwzg3.scdn.arkena.com/8737/nrj_164766.mp3\", \"Hits\"),"+
//                    "(\"NRJ Zayn vs One Direction\", \"http://triton.scdn.arkena.com/15357/live_aac.mp3\", \"Hits\"),"+
//                    "(\"Oceane FM\", \"http://oceanefm.ice.infomaniak.ch/oceanefm-128.mp3\", \"Hits\"),"+
//                    "(\"Phare FM Hits\", \"http://str0.creacast.com/pharefmhits\", \"hits\"),"+
//                    "(\"Puls\"\" Radio 2000\", \"http://icecast.pulsradio.com/puls00HD.mp3\", \"Hits\"),"+
//                    "(\"Radio 8\", \"http://radio8.ice.infomaniak.ch/radio8-128.mp3\", \"Hits\"),"+
//                    "(\"Radio Atlantis\", \"http://83.143.18.8:8000/;?1457190942162.mp3\", \"Hits\"),"+
//                    "(\"Radio Cristal\", \"http://radiocristal.ice.infomaniak.ch/radiocristal-high.mp3\", \"Hits\"),"+
//                    "(\"Radio Espace\", \"http://radioespace.ice.infomaniak.ch/radioespace-high.mp3\", \"Hits\"),"+
//                    "(\"Radio Espace Hot 30\", \"http://espace-vintage.ice.infomaniak.ch/espace-vintage-high.mp3\", \"Hits\"),"+
//                    "(\"Radio Inside\", \"http://serveur-toukir.best.vamerica.voiceofamerica.com:9070/;stream\", \"Hits\"),"+
//                    "(\"Radio Maritima\", \"http://radiomaritima.streamakaci.com/radiomaritima.mp3\", \"Hits\"),"+
//                    "(\"Radio Menergy\", \"http://menergy.scdn.arkena.com/live.mp3\", \"Hits\"),"+
//                    "(\"Radio Numéro 1\", \"http://radiono1.ice.infomaniak.ch/radiono1-128.mp3\", \"Hits\"),"+
//                    "(\"Radio Oxygène\", \"http://live.radiohorizon.fr:7710/;stream.mp3\", \"Hits\"),"+
//                    "(\"Radio RVA\", \"http://rva.ice.infomaniak.ch/rva-high.mp3\", \"Hits\"),"+
//                    "(\"Radio Scoop 91.3\", \"http://radioscoopsaintetienne.ice.infomaniak.ch/radioscoop-stetienne-128.mp3\", \"Hits\"),"+
//                    "(\"Radio Scoop 92.0\", \"http://radioscooplyon.ice.infomaniak.ch/radioscoop-lyon-128.mp3\", \"Hits\"),"+
//                    "(\"Radio Star\", \"http://radiostar.ice.infomaniak.ch/radiostar-128.mp3\", \"Hits\"),"+
//                    "(\"RVM\", \"http://stream.rvm.fr:8002/;stream/1\", \"Hits\"),"+
//                    "(\"Sweet FM\", \"http://62.210.214.169:8000/alc\", \"Hits\"),"+
//                    "(\"Top FM\", \"http://str30.creacast.com/top_fm\", \"Hits\"),"+
//                    "(\"Totem\", \"http://aratotem.ice.infomaniak.ch/aveyron.mp3\", \"Hits\"),"+
//                    "(\"Virgin Radio Classics\", \"http://vr-wr2-mp3-128.scdn.arkena.com/virginradio.mp3\", \"Hits\"),"+
//                    "(\"Virgin Radio Hits\", \"http://vr-wr4-mp3-128.scdn.arkena.com/virginradio.mp3\", \"Hits\"),"+
//                    "(\"Voltage\", \"http://start-voltage.bcast.infomaniak.ch/start-voltage-high.mp3\", \"Hits\"),"+
//                    "(\"NRJ Hits 2010\", \"http://triton.scdn.arkena.com/13171/live.mp3\", \"Hits 2010\"),"+
//                    "(\"NRJ Hits 2016\", \"http://triton.scdn.arkena.com/13049/live.mp3\", \"Hits 2016\"),"+
//                    "(\"NRJ Spring Hits 2016\", \"http://triton.scdn.arkena.com/15307/live_aac.mp3\", \"Hits 2016\"),"+
//                    "(\"Allzic Radio Route 66\", \"http://allzic37.ice.infomaniak.ch/allzic37.mp3\", \"Hits US\"),"+
//                    "(\"NRJ David Guetta\"\"s Hits\", \"http://triton.scdn.arkena.com/13231/live.mp3\", \"Hits, House\"),"+
//                    "(\"Autoroute Info nord\", \"http://media.autorouteinfo.fr:8000/direct_nord.mp3\", \"Hits, Information, News\"),"+
//                    "(\"Autoroute Info sud\", \"http://media.autorouteinfo.fr:8000/direct_sud.mp3\", \"Hits, Information, News\"),"+
//                    "(\"Ouest FM\", \"http://start-forum.ice.infomaniak.ch/start-forum-high.mp3\", \"Hits, Information, News\"),"+
//                    "(\"Radio 6\", \"http://radio6-montreuilsurmer.ice.infomaniak.ch/radio6-montreuilsurmer-128.mp3\", \"Hits, Information, News\"),"+
//                    "(\"Radio Vinci Autoroutes Alpes Provence\", \"http://str0.creacast.com/radio_vinci_autoroutes_6\", \"Hits, Information, News\"),"+
//                    "(\"Radio Vinci Autoroutes Auvergne Vallée du Rhône\", \"http://str0.creacast.com/radio_vinci_autoroutes_5\", \"Hits, Information, News\"),"+
//                    "(\"Radio Vinci Autoroutes Côte d\"\"Azur\", \"Radio Vinci Autoroutes Alpes Provence\", \"Hits, Information, News\"),"+
//                    "(\"Radio Vinci Autoroutes Languedoc Roussillon\", \"http://str0.creacast.com/radio_vinci_autoroutes_4\", \"Hits, Information, News\"),"+
//                    "(\"Radio Vinci Autoroutes Ouest Centre\", \"http://str0.creacast.com/radio_vinci_autoroutes_1\", \"Hits, Information, News\"),"+
//                    "(\"Radio Vinci Autoroutes Sud Ouest\", \"http://str0.creacast.com/radio_vinci_autoroutes_3\", \"Hits, Information, News\"),"+
//                    "(\"VFM\", \"http://str0.creacast.com/vfm\", \"Hits, Information, News\"),"+
//                    "(\"Wit FM\", \"http://start-witfm.ice.infomaniak.ch/start-witfm-high.mp3\", \"Hits, Information, News\"),"+
//                    "(\"Radio Mont-Blanc\", \"http://radiomontblanc1.ice.infomaniak.ch/radiomontblanc1-128.mp3\", \"Hits, Information, Radio locale, Local Service, News\"),"+
//                    "(\"FIP\", \"http://audio.scdn.arkena.com/11016/fip-midfi128.mp3\", \"Hits, Jazz, Musique du monde, Rock, World music\"),"+
//                    "(\"Allzic Radio Alternative\", \"http://allzic46.ice.infomaniak.ch/allzic46.mp3\", \"Hits, Latino, Latin music\"),"+
//                    "(\"Allzic Radio Love song\", \"http://allzic34.ice.infomaniak.ch/allzic34.mp3\", \"Hits, Love songs\"),"+
//                    "(\"Alouette\", \"http://broadcast.infomaniak.net/alouette-high.mp3\", \"Hits, News\"),"+
//                    "(\"Happy FM\", \"http://happyfm.streaming-pro.com:8038/;357663778644823stream.nsv\", \"Hits, Nouveautés\"),"+
//                    "(\"HotmixRadio Hits\", \"http://streamingads.hotmixradio.fm/hotmixradio-hits-128.mp3\", \"Hits, Nouveautés\"),"+
//                    "(\"NRJ Michael Jackson\"\"s Hits\", \"http://triton.scdn.arkena.com/13283/live.mp3\", \"Hits, Pop\"),"+
//                    "(\"NRJ Pop Hits\", \"http://triton.scdn.arkena.com/13173/live.mp3\", \"Hits, Pop\"),"+
//                    "(\"Tonic radio Lyon & Vienne\", \"http://broadcast.infomaniak.net/hit-sport-high.mp3\", \"Hits, Pop\"),"+
//                    "(\"Tonic radio Villefranche\", \"http://broadcast.infomaniak.ch/tonicradiovillefranche.mp3\", \"Hits, Pop\"),"+
//                    "(\"Ado R\"\"n\"\"B US\", \"http://adornbus.bcast.infomaniak.ch/adornbus-128.mp3\", \"Hits, RnB\"),"+
//                    "(\"La Radio Plus\", \"http://broadcast.infomaniak.ch/laradioplus-high.mp3\", \"Hits, RnB\"),"+
//                    "(\"NRJ R\"\"n\"\"B Hits\", \"http://triton.scdn.arkena.com/13099/live.mp3\", \"Hits, RnB\"),"+
//                    "(\"Hélène FM\", \"http://helenefm.ice.infomaniak.ch/helenefm-high.mp3\", \"Hits, Radio locale, Local service\"),"+
//                    "(\"Radio Evasion\", \"http://radioevasion.fr:8020/;stream.nsv\", \"Hits, Radio locale, Local service\"),"+
//                    "(\"Blackbox\", \"http://start-blackbox.bcast.infomaniak.ch/start-blackbox-high.mp3\", \"Hits, Rap, RnB\"),"+
//                    "(\"NRJ Hip Hop Hits\", \"http://triton.scdn.arkena.com/13131/live.mp3\", \"Hits, Rap, RnB\"),"+
//                    "(\"NRJ Hip Hop R\"\"n\"\"B Hits\", \"http://triton.scdn.arkena.com/13259/live.mp3\", \"Hits, Rap, RnB\"),"+
//                    "(\"NRJ Relax\", \"http://triton.scdn.arkena.com/13197/live.mp3\", \"Hits, Relax\"),"+
//                    "(\"4U Classic Rock\", \"http://str4uice.streamakaci.com/4uclassicrock.mp3\", \"Hits, Rock\"),"+
//                    "(\"Live9\", \"http://stream8.addictradio.net/addictrock\", \"Hits, Rock\"),"+
//                    "(\"Hit UK Station\", \"http://icecast.pulsradio.com/hitukHD.mp3\", \"Hits, Royaume-Uni, UK\"),"+
//                    "(\"Nice Radio 102.3\", \"http://niceradio.stream.ideocast.fr:8000/niceradio.mp3?time=9961\", \"Hits, Russie, Russia\"),"+
//                    "(\"Tonic radio Bourgoin\", \"http://broadcast.infomaniak.ch/tonicradiobourgoin.mp3\", \"Hits, Sport\"),"+
//                    "(\"Allzic Radio Humour\", \"http://allzic25.ice.infomaniak.ch/allzic25.mp3\", \"Humour\"),"+
//                    "(\"Rire et Chansons\", \"http://adwzg5.scdn.arkena.com/10832/nrj_173735.mp3\", \"Humour\"),"+
//                    "(\"Rire et Chansons Blagues\", \"http://adwzg3.tdf-cdn.com/8574/nrj_165638.mp3\", \"Humour\"),"+
//                    "(\"Rire et Chansons Canulars\", \"http://adwzg3.tdf-cdn.com/8645/nrj_172621.mp3\", \"Humour\"),"+
                    "(\"ABC\", \"http://st2.zenorad.io:14424\", \"ABC, ABC\"),"+
                    "(\"Rire et Chansons Collectors\", \"http://adwzg3.tdf-cdn.com/8648/nrj_166789.mp3\", \"Humour\");";

            String insertWebradioSQL6 ="INSERT INTO " + TABLE_WEBRADIO + " ("
                    + KEY_RADIO_STATION_NAME + ", "
                    + KEY_RADIO_URL + ", "
                    + KEY_MUSIC_GENRE +") VALUES "+

//                    "(\"Rire et Chansons Gad Elmaleh\", \"http://adwzg3.tdf-cdn.com/8751/nrj_175644.mp3\", \"Humour\"),"+
//                    "(\"Rire et Chansons Live\", \"http://adwzg3.tdf-cdn.com/8575/nrj_176026.mp3\", \"Humour\"),"+
//                    "(\"Rire et Chansons Nouveaux Génération\", \"http://adwzg3.tdf-cdn.com/8573/nrj_177371.mp3\", \"Humour\"),"+
//                    "(\"Rire et Chansons Open du Rire\", \"http://adwzg5.tdf-cdn.com/10747/nrj_173546.mp3\", \"Humour\"),"+
//                    "(\"Rire et Chansons Semoun\", \"http://adwzg3.tdf-cdn.com/8715/nrj_166957.mp3\", \"Humour\"),"+
//                    "(\"Rire et Chansons Sketches\", \"http://95.81.155.24/8572/nrj_175797.mp3\", \"Humour\"),"+
//                    "(\"Rire et Chansons Stand Up\", \"http://adwzg3.tdf-cdn.com/8646/nrj_170283.mp3\", \"Humour\"),"+
//                    "(\"Radio 74\", \"http://radio74.ice.infomaniak.ch/radio74-high.aac\", \"Information, News\"),"+
//                    "(\"Radio Courtoisie\", \"http://direct.radiocourtoisie.fr\", \"Information, débats, politique, News, Talk\"),"+
//                    "(\"Radio Libertaire\", \"http://ecoutez.radio-libertaire.org:8080/radiolib\", \"Information, débats, politique, News, Talk\"),"+
//                    "(\"Kernews\", \"http://stric6.streamakaci.com/kernews.mp3\", \"information, Radio locale, Local service, News\"),"+
//                    "(\"Radio Ici & Maintenant!\", \"http://radio.rim952.fr:8000/stream.mp3\", \"information, Radio locale, Local service, News\"),"+
//                    "(\"RJL Info\", \"http://rjl-info.ice.infomaniak.ch/rjl-info-128.mp3\", \"Information, Religion, News\"),"+
//                    "(\"Allzic Radio Jazz\", \"http://allzic40.ice.infomaniak.ch/allzic40.mp3\", \"Jazz\"),"+
//                    "(\"Fip autour du jazz\", \"http://webradiofip2.scdn.arkena.com/webradiofip2.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Classic Jazz\", \"http://jazz-wr01.ice.infomaniak.ch/jazz-wr01-128.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Confluence\", \"http://jzr-com-01.ice.infomaniak.ch/jzr-com-01.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Contemporary Jazz\", \"http://jazz-wr05.ice.infomaniak.ch/jazz-wr05-128.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Electro Swing\", \"http://jazz-wr04.ice.infomaniak.ch/jazz-wr04-128.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Funk\", \"http://jazz-wr06.ice.infomaniak.ch/jazz-wr06-128.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Groov’up DJ Master Phil\", \"http://jazz-wr13.ice.infomaniak.ch/jazz-wr13-128.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Groove\", \"http://jazz-wr08.ice.infomaniak.ch/jazz-wr08-128.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Happy Hour Bart&Baker\", \"http://jazz-wr14.ice.infomaniak.ch/jazz-wr14-128.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Jazz & Cinéma\", \"http://jzr-wr20.ice.infomaniak.ch/jzr-wr20.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Jazz & Classique\", \"http://jazz-wr17.ice.infomaniak.ch/jazz-wr17-128.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Jazz Gospel\", \"http://jazz-wr07.ice.infomaniak.ch/jazz-wr07-128.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Jazzy French\", \"http://jazz-wr18.ice.infomaniak.ch/jazz-wr18-128.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Ladies & Crooners\", \"http://jazzladiescrooners.ice.infomaniak.ch/jazzladiescrooners-high.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio New Orleans\", \"http://jazz-wr03.ice.infomaniak.ch/jazz-wr03-128.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Only Women\", \"http://jazz-wr16.ice.infomaniak.ch/jazz-wr16-128.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Piano Jazz\", \"http://jzr-piano.ice.infomaniak.ch/jzr-piano.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Reprises\", \"http://reprises.ice.infomaniak.ch/reprises-high.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Saxo Jazz\", \"http://jzr-saxo.ice.infomaniak.ch/jzr-saxo.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Soul Food DJ Philgood\", \"http://jazz-wr12.ice.infomaniak.ch/jazz-wr12-128.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Sunset\", \"http://jzr-sunset.ice.infomaniak.ch/jzr-sunset.mp3\", \"Jazz\"),"+
//                    "(\"Jazz Radio Valentine\"\"s Day\", \"http://jzr-thema.ice.infomaniak.ch/jzr-thema.mp3\", \"Jazz\"),"+
//                    "(\"LinasJazz\", \"http://stric6.streamakaci.com/4uclassicrock.mp3\", \"Jazz\"),"+
//                    "(\"M2 Jazz\", \"http://live.m2stream.fr/m2jazz-128.mp3\", \"Jazz\"),"+
//                    "(\"Swing FM\", \"http://swingfm.ice.infomaniak.ch/swingfm-128.mp3\", \"Jazz\"),"+
//                    "(\"Tendance Ouest Jazz\", \"http://streaming.tendanceouest.com/jazz.mp3\", \"jazz\"),"+
//                    "(\"TSF Jazz\", \"http://tsfjazz.ice.infomaniak.ch:80/tsfjazz-high\", \"Jazz\"),"+
//                    "(\"Jazz Radio Jazz Manouche\", \"http://jazz-wr02.ice.infomaniak.ch/jazz-wr02-128.mp3\", \"Jazz Manouche\"),"+
//                    "(\"Jazz Radio Gospel\", \"http://jazz-wr07.ice.infomaniak.ch/jazz-wr07-128.mp3\", \"Jazz, Gospel\"),"+
//                    "(\"Jazz Radio Latin Jazz\", \"http://jazz-wr09.ice.infomaniak.ch/jazz-wr09-128.mp3\", \"Jazz, Latino, Latin music\"),"+
//                    "(\"Allzic Radio Jazz lounge\", \"http://allzic03.ice.infomaniak.ch/allzic03.mp3\", \"Jazz, Lounge\"),"+
//                    "(\"Jazz Radio Lounge\", \"http://jazzlounge.ice.infomaniak.ch/jazzlounge-high.mp3\", \"Jazz, Lounge\"),"+
//                    "(\"Jazz Radio Nouveautés Jazz\", \"http://jzr-wr99.ice.infomaniak.ch/jzr-wr99.mp3\", \"Jazz, Nouveautés\"),"+
//                    "(\"Jazz Radio Nouveautés Soul\", \"http://jzr-wr19.ice.infomaniak.ch/jzr-wr19-128.mp3\", \"Jazz, Nouveautés, Soul\"),"+
//                    "(\"Chérie Jazzy\", \"http://adwzg3.scdn.arkena.com/8602/nrj_167655.mp3\", \"Jazz, Pop\"),"+
//                    "(\"Chérie Zen\", \"http://adwzg3.scdn.arkena.com/8568/nrj_163374.mp3\", \"Jazz, Pop\"),"+
//                    "(\"4U Smooth Jazz\", \"http://streaming.radionomy.com/4u-smooth-jazz\", \"Jazz, Relax\"),"+
//                    "(\"Jazz Radio Zen Attitude\", \"http://zen-attitude.ice.infomaniak.ch/zen-attitude.mp3\", \"Jazz, Relax\"),"+
//                    "(\"Jazz Radio Black Music\", \"http://jazzblackmusic.ice.infomaniak.ch/jazzblackmusic-high.mp3\", \"jazz, Soul\"),"+
//                    "(\"Jazz Radio Jazz & Soul\", \"http://jazzradio.ice.infomaniak.ch/jazzradio-high.mp3\", \"Jazz, Soul\"),"+
//                    "(\"Ado Kizomba\", \"http://adokizomba.bcast.infomaniak.ch/adokizomba-128.mp3\", \"Kizomba, Latino, Latin music\"),"+
//                    "(\"Blackbox Kizomba\", \"http://blackboxkizomba.bcast.infomaniak.ch/blackboxkizomba-128.mp3\", \"Kizomba, Latino, Latin music\"),"+
//                    "(\"NRJ Kizomba\", \"http://triton.scdn.arkena.com/13237/live.mp3\", \"Kizomba, Latino, Latin music\"),"+
//                    "(\"Allzic Radio Brazil\", \"http://allzic19.ice.infomaniak.ch/allzic19.mp3\", \"Latino, Latin music, Latin music\"),"+
//                    "(\"Allzic Radio Italia\", \"http://allzic35.ice.infomaniak.ch/allzic35.mp3\", \"Latino, Latin music, Latin music\"),"+
//                    "(\"Allzic Radio Latino\", \"http://allzic39.ice.infomaniak.ch/allzic39.mp3\", \"Latino, Latin music, Latin music\"),"+
//                    "(\"Ambiance Brasil\", \"http://streaming.radionomy.com/Ambiance-Brasil\", \"Latino, Latin music, Latin music\"),"+
//                    "(\"Blackbox Latina\", \"http://blackboxlatina.bcast.infomaniak.ch/blackboxlatina-128.mp3\", \"Latino, Latin music, Latin music\"),"+
//                    "(\"M2 Caliente\", \"http://live.m2stream.fr/m2caliente-128.mp3\", \"Latino, Latin music, Latin music\"),"+
//                    "(\"NRJ Fiesta Latina\", \"http://triton.scdn.arkena.com/14698/live.mp3\", \"Latino, Latin music, Latin music\"),"+
//                    "(\"NRJ Latino\", \"http://triton.scdn.arkena.com/13109/live.mp3\", \"Latino, Latin music, Latin music\"),"+
//                    "(\"Radio CapSao\", \"http://str2.creacast.com/capsao\", \"Latino, Latin music, Latin music\"),"+
//                    "(\"Radio Capsao\", \"http://str0.creacast.com/capsao\", \"Latino, Latin music, Latin music\"),"+
//                    "(\"Radio Latina\", \"http://broadcast.infomaniak.net/start-latina-high.mp3\", \"Latino, Latin music, Latin music\"),"+
//                    "(\"Wit Latina\", \"http://witlatina.ice.infomaniak.ch/witlatina.mp3\", \"Latino, Latin music, Latin music\"),"+
//                    "(\"NRJ Portugal\", \"http://triton.scdn.arkena.com/15246/live_aac.mp3\", \"Latino, Latin music, Latin music \"),"+
//                    "(\"NRJ Latino Hits\", \"http://triton.scdn.arkena.com/13119/live.mp3\", \"Latino, Hits, Latin music \"),"+
//                    "(\"Chérie Latino\", \"http://adwzg3.scdn.arkena.com/8599/nrj_171454.mp3\", \"Latino, Pop, Latin music \"),"+
//                    "(\"Ado Reggaeton\", \"http://adoreggaeton.bcast.infomaniak.ch/adoreggaeton-128.mp3\", \"Latino, Reggaeton, Latin music\"),"+
//                    "(\"Blackbox Reggaeton\", \"http://blackboxreggaeton.bcast.infomaniak.ch/blackboxreggaeton-128.mp3\", \"Latino, Reggaeton, Latin music\"),"+
//                    "(\"Generations Reggaeton\", \"http://gene-rgton.ice.infomaniak.ch/generations-rgton.mp3\", \"Latino, Reggaeton, Latin music\"),"+
//                    "(\"NRJ Reggaeton\", \"http://triton.scdn.arkena.com/13183/live.mp3\", \"Latino, Reggaeton, Latin music\"),"+
//                    "(\"M2 Chillout\", \"http://live.m2stream.fr/m2chillout-128.mp3\", \"Lounge\"),"+
//                    "(\"NRJ Lounge\", \"http://triton.scdn.arkena.com/12715/live.mp3\", \"Lounge\"),"+
//                    "(\"Puls\"\" Radio Lounge\", \"http://icecast.pulsradio.com/relaxHD.mp3\", \"Lounge\"),"+
//                    "(\"RFM Lounge\", \"http://rfm-wr6-mp3-128.scdn.arkena.com/rfm.mp3\", \"Lounge\"),"+
//                    "(\"Voltage LOUNGE\", \"http://voltagelounge.bcast.infomaniak.ch/voltagelounge-128.mp3\", \"Lounge\"),"+
//                    "(\"Wit Lounge\", \"http://witlounge.ice.infomaniak.ch/witlounge.mp3\", \"Lounge\"),"+
//                    "(\"Chérie Lounge\", \"http://adwzg3.tdf-cdn.com/8638/nrj_164024.mp3\", \"Lounge, Pop\"),"+
//                    "(\"La Radio de la Mer\", \"http://radiomer.ice.infomaniak.ch/radiomer-128.mp3\", \"Lounge, Pop\"),"+
//                    "(\"Allzic Radio Lounge\", \"http://allzic21.ice.infomaniak.ch/allzic21.mp3\", \"Lounge, Relax\"),"+
//                    "(\"Allzic Radio Zen\", \"http://allzic20.ice.infomaniak.ch/allzic20.mp3\", \"Lounge, Relax\"),"+
//                    "(\"Chérie Relax\", \"http://adwzg4.scdn.arkena.com/9892/nrj_103712.mp3\", \"Lounge, Relax\"),"+
//                    "(\"Chérie Spa\", \"http://adwzg3.scdn.arkena.com/8746/nrj_167735.mp3\", \"Lounge, Relax\"),"+
//                    "(\"HotmixRadio Lounge\", \"http://streamingads.hotmixradio.fm/hotmixradio-lounge-128.mp3\", \"Lounge, Relax\"),"+
//                    "(\"Fun Radio Love\", \"http://stream.funradio.sk:8000/slow128.mp3\", \"Love\"),"+
//                    "(\"M2 Love\", \"http://live.m2stream.fr/m2love-128.mp3\", \"Love\"),"+
//                    "(\"NRJ Love\", \"http://triton.scdn.arkena.com/13085/live.mp3\", \"Love\"),"+
//                    "(\"NRJ Romantic\", \"http://triton.scdn.arkena.com/13211/live.mp3\", \"Love\"),"+
//                    "(\"Voltage LOVE\", \"http://voltagelove.bcast.infomaniak.ch/voltagelove-128.mp3\", \"Love\"),"+
//                    "(\"Wit Love\", \"http://witlove.ice.infomaniak.ch/witlove.mp3\", \"Love\"),"+
//                    "(\"Chérie Love songs\", \"http://adwzg3.scdn.arkena.com/8570/nrj_170997.mp3\", \"Love songs, Pop\"),"+
//                    "(\"Chérie Romantic\", \"http://adwzg3.tdf-cdn.com/8679/nrj_164792.mp3\", \"Love songs, Pop\"),"+
//                    "(\"Ado Love\", \"http://adolove.bcast.infomaniak.ch/adolove-128.mp3\", \"Love, Pop, RnB\"),"+
                    "(\"ABC\", \"http://st2.zenorad.io:14424\", \"ABC, ABC\"),"+
                    "(\"Blackbox Love\", \"http://blackboxlove.bcast.infomaniak.ch/blackboxlove-128.mp3\", \"Love, RnB\"),"+
                    "(\"HotmixRadio Hot\", \"http://streamingads.hotmixradio.fm/hotmixradio-hot-128.mp3\", \"Love, RnB, Soul\");";

            String insertWebradioSQL7 ="INSERT INTO " + TABLE_WEBRADIO + " ("
                    + KEY_RADIO_STATION_NAME + ", "
                    + KEY_RADIO_URL + ", "
                    + KEY_MUSIC_GENRE +") VALUES "+

//                    "(\"BPM\", \"http://str20.creacast.com/bpm\", \"Mantois, Radio locale, Local service\"),"+
//                    "(\"Radio Grenouille\", \"http://live.radiogrenouille.com/live\", \"Marseille, Radio associative, Community radio\"),"+
//                    "(\"La Grosse Radio Metal\", \"http://hd.lagrosseradio.info:8200/;stream.nsv\", \"Metal, Rock\"),"+
//                    "(\"Metal War\", \"http://streaming.radionomy.com/Metal-War\", \"Metal, Rock\"),"+
//                    "(\"Divergence FM 93.9\", \"http://ridibundus.divergence-fm.org:8000/divergence.mp3\", \"Monptellier, Radio locale, Local service\"),"+
//                    "(\"Fip autour du monde\", \"http://webradiofip4.scdn.arkena.com/webradiofip4.mp3\", \"Musique du monde, World music\"),"+
//                    "(\"Radio Nova\", \"http://novazz.ice.infomaniak.ch/novazz-128.mp3\", \"Musique du monde, World music\"),"+
//                    "(\"Chérie Nouveautés\", \"http://adwzg3.scdn.arkena.com/8635/nrj_175012.mp3\", \"Nouveauté, Pop, New\"),"+
//                    "(\"Fip Tout nouveau, tout Fip\", \"http://webradiofip5.scdn.arkena.com/webradiofip5.mp3\", \"Nouveauté, New\"),"+
//                    "(\"HotmixRadio New\", \"http://streamingads.hotmixradio.fm/hotmixradio-new-128.mp3\", \"Nouveauté, New\"),"+
//                    "(\"NRJ Discover\", \"http://triton.scdn.arkena.com/13025/live.mp3\", \"Nouveauté, New\"),"+
//                    "(\"NRJ First Listen\", \"http://triton.scdn.arkena.com/13105/live.mp3\", \"Nouveauté, New\"),"+
//                    "(\"NRJ New Releases\", \"http://triton.scdn.arkena.com/13155/live.mp3\", \"Nouveauté, New\"),"+
//                    "(\"NRJ Nouveautés\", \"http://triton.scdn.arkena.com/13039/live.mp3\", \"Nouveauté, New\"),"+
//                    "(\"Radio Néo\", \"http://stream.radioneo.org:8000/;chat.mp3\", \"Nouveauté, New\"),"+
//                    "(\"RFM Fresh\", \"http://rfm-wr7-mp3-128.scdn.arkena.com/rfm.mp3\", \"Nouveauté, New\"),"+
//                    "(\"Allzic Radio Musique orientale\", \"http://allzic33.ice.infomaniak.ch/allzic33.mp3\", \"Oriental\"),"+
//                    "(\"Beur FM\", \"http://broadcast.infomaniak.ch/beurfm-high.mp3\", \"Oriental\"),"+
//                    "(\"France Maghreb 2\", \"http://francemaghreb2.ice.infomaniak.ch/francemaghreb2-high.mp3\", \"Oriental\"),"+
//                    "(\"NRJ Oriental\", \"http://triton.scdn.arkena.com/13159/live.mp3\", \"Oriental\"),"+
//                    "(\"NRJ Raï\", \"http://triton.scdn.arkena.com/13147/live.mp3\", \"Oriental\"),"+
//                    "(\"Radio Orient\", \"http://stream3.broadcast-associes.com:8405/Radio-Orient\", \"Oriental\"),"+
//                    "(\"Radio Salam\", \"http://radiosalam.scdn.arkena.com/radiosalam.mp3\", \"Oriental, Radio associative, Community radio\"),"+
//                    "(\"Radio Soleil\", \"http://radiosoleil.ice.infomaniak.ch/radiosoleil-96.mp3\", \"Oriental, Radio associative, Community radio\"),"+
//                    "(\"Chérie At work\", \"http://adwzg3.scdn.arkena.com/8571/nrj_176201.mp3\", \"Pop\"),"+
//                    "(\"Chérie Emotions\", \"http://adwzg4.tdf-cdn.com/8830/nrj_164825.mp3\", \"Pop\"),"+
//                    "(\"Chérie été\", \"http://adwzg4.tdf-cdn.com/10124/nrj_110839.mp3\", \"Pop\"),"+
//                    "(\"Chérie FM\", \"http://adwzg3.tdf-cdn.com/8473/nrj_178499.mp3\", \"Pop\"),"+
//                    "(\"Chérie Happy\", \"http://adwzg4.scdn.arkena.com/9894/nrj_103849.mp3\", \"Pop\"),"+
//                    "(\"Chérie Les plus belles voix\", \"http://adwzg4.scdn.arkena.com/8866/nrj_172778.mp3\", \"Pop\"),"+
//                    "(\"Chérie No repeat\", \"http://adwzg3.tdf-cdn.com/8708/nrj_177509.mp3\", \"Pop\"),"+
//                    "(\"Chérie Pop\", \"http://adwzg3.scdn.arkena.com/8587/nrj_175489.mp3\", \"Pop\"),"+
//                    "(\"Chérie Pop ballads\", \"http://adwzg5.scdn.arkena.com/10896/nrj_110459.mp3\", \"Pop\"),"+
//                    "(\"Gold Fréquence 3\", \"http://hd.stream.frequence3.net/frequence3ac-256.mp3\", \"Pop\"),"+
//                    "(\"NRJ Adele\", \"http://triton.scdn.arkena.com/14762/live.mp3\", \"Pop\"),"+
//                    "(\"NRJ Pop\", \"http://triton.scdn.arkena.com/13233/live.mp3\", \"Pop\"),"+
//                    "(\"Chérie R\"\"n\"\"B\", \"http://adwzg3.scdn.arkena.com/8660/nrj_166610.mp3\", \"Pop, RnB\"),"+
//                    "(\"NRJ Ariana Grande\", \"http://triton.scdn.arkena.com/13153/live.mp3\", \"Pop, RnB\"),"+
//                    "(\"NRJ Justin Bieber\", \"http://triton.scdn.arkena.com/13241/live.mp3\", \"Pop, RnB\"),"+
//                    "(\"Mega FM\", \"http://megafm.ice.infomaniak.ch/megafm-high.mp3\", \"Pop, Radio locale, Rock, Local service\"),"+
//                    "(\"RTF 95.4\", \"http://live140.impek.com:9962/;flash.mp3\", \"Pop, Radio locale, Rock, Local service\"),"+
//                    "(\"Chérie Sweet home\", \"http://adwzg3.scdn.arkena.com/8680/nrj_171678.mp3\", \"Pop, Relax\"),"+
//                    "(\"Elium Radio Rock & Pop\", \"http://streaming.radionomy.com/Elium-Rock\", \"Pop, Rock\"),"+
//                    "(\"Fip autour du rock\", \"http://webradiofip1.scdn.arkena.com/webradiofip1.mp3\", \"Pop, Rock\"),"+
//                    "(\"ODS Radio\", \"http://ods.ice.infomaniak.ch/ods-high.mp3\", \"Pop, Rock\"),"+
//                    "(\"RFM Pop Rock\", \"http://rfm-wr10-mp3-128.scdn.arkena.com/rfm.mp3\", \"Pop, Rock\"),"+
//                    "(\"RMN-FM\", \"http://shoutcast.rmnfm.fr:8000/livermn.mp3\", \"Pop, Rock\"),"+
//                    "(\"RTL2\", \"http://icecast.rtl2.fr/rtl2-1-48-72.aac\", \"Pop, Rock\"),"+
//                    "(\"Tendance Ouest Pop Rock\", \"http://streaming.tendanceouest.com/poprock.mp3\", \"Pop, Rock\"),"+
//                    "(\"Top Music\", \"http://str0.creacast.com/topmusic1\", \"Pop, Rock\"),"+
//                    "(\"Virgin Radio\", \"http://vr-live-mp3-128.scdn.arkena.com/virginradio.mp3\", \"Pop, Rock\"),"+
//                    "(\"Radio Alfa\", \"http://5.39.87.10:8050/;\", \"Portugal, Radio communautaire, Community radio\"),"+
//                    "(\"Ado @Work\", \"http://adowork.bcast.infomaniak.ch/adowork-128.mp3\", \"RnB\"),"+
//                    "(\"Allzic Radio R&B\", \"http://allzic08.ice.infomaniak.ch/allzic10.mp3\", \"RnB\"),"+
//                    "(\"Blackbox R\"\"n\"\"B US\", \"http://blackboxrnbus.bcast.infomaniak.ch/blackboxrnbus-128.mp3\", \"RnB\"),"+
//                    "(\"Generations R&B\", \"http://generationfm-slowjam.ice.infomaniak.ch/generationfm-slowjam-high.mp3\", \"RnB\"),"+
//                    "(\"Generations R&B Gold\", \"http://generations-rnb-gold.ice.infomaniak.ch/generations-rnb-gold.mp3\", \"RnB\"),"+
//                    "(\"Generations Slow jam\", \"http://gene-wr11.ice.infomaniak.ch/gene-wr11.mp3\", \"RnB\"),"+
//                    "(\"NRJ Classic R\"\"n\"\"B\", \"http://triton.scdn.arkena.com/13199/live.mp3\", \"RnB\"),"+
//                    "(\"NRJ R\"\"n\"\"B\", \"http://triton.scdn.arkena.com/13129/live.mp3\", \"RnB\"),"+
//                    "(\"NRJ R\"\"n\"\"B FR\", \"http://triton.scdn.arkena.com/13157/live.mp3\", \"RnB\"),"+
//                    "(\"NRJ Rihanna\"\"s Hits\", \"http://triton.scdn.arkena.com/13107/live.mp3\", \"RnB\"),"+
//                    "(\"Ado FM\", \"http://start-adofm.ice.infomaniak.ch/start-adofm-high.mp3\", \"RnB, Rap\"),"+
//                    "(\"Agora FM\", \"http://37.187.146.140:8519/stream\", \"RnB, Rap\"),"+
//                    "(\"Skyrock\", \"http://mp3lg2.tdf-cdn.com/4603/sky_120728.mp3\", \"RnB, Rap\"),"+
//                    "(\"AlterNantes\", \"http://diffusion.lafrap.fr/alternantes.mp3\", \"Radio associative, Community radio\"),"+
//                    "(\"Fréquence Paris Plurielle (FPP)\", \"http://radio.toile-libre.org:8000/fpp.mp3\", \"Radio associative, Community radio\"),"+
//                    "(\"La Clé des Ondes\", \"http://radio.lacdo.org:80/stream\", \"Radio associative, Community radio\"),"+
//                    "(\"Radio Alpine Meilleure\", \"http://91.121.62.121:8310/;stream.nsv\", \"Radio associative, Community radio\"),"+
//                    "(\"Radio Béton\", \"http://51.254.220.198:8000/stream.mp3\", \"Radio associative, Community radio\"),"+
//                    "(\"Radio FMR\", \"http://giss.tv:8001/radio-fmr.mp3\", \"Radio associative, Community radio\"),"+
//                    "(\"Radio Galère\", \"http://www.radiogalere.org:8080/galere.mp3\", \"Radio associative, Community radio\"),"+
//                    "(\"Studio ZEF\", \"http://192.99.13.42:8000/zef.mp3\", \"Radio associative, Community radio\"),"+
//                    "(\"Beaub FM\", \"http://beaubfm2.ice.infomaniak.ch/beaubfm2-96.mp3\", \"Radio associative, Rock, Community radio\"),"+
//                    "(\"Ràdio Arrels\", \"http://stream.franclr.fr:8000/radioarrels\", \"Radio catalane, Local service\"),"+
//                    "(\"Guyane 1ère\", \"http://guyane-la1ere.scdn.arkena.com/live.mp3\", \"Radio d\"\"Outre-mer, Tropical music\"),"+
//                    "(\"Mayotte 1ère\", \"http://mayotte-la1ere.scdn.arkena.com/live.mp3\", \"Radio d\"\"Outre-mer, Tropical music\"),"+
//                    "(\"Nouvelle Calédonie 1ère\", \"http://nouvellecaledonie-la1ere.scdn.arkena.com/live.mp3\", \"Radio d\"\"Outre-mer, Tropical music\"),"+
//                    "(\"Polynésie 1ère\", \"http://polynesie-la1ere.scdn.arkena.com/live.mp3\", \"Radio d\"\"Outre-mer, Tropical music\"),"+
//                    "(\"RCOM FM\", \"http://str30.creacast.com/rcom\", \"Radio d\"\"Outre-mer, Tropical music\"),"+
//                    "(\"Réunion 1ère\", \"http://reunion-la1ere.scdn.arkena.com/live.mp3\", \"Radio d\"\"Outre-mer, Tropical music\"),"+
//                    "(\"Saint Pierre et Miquelon 1ère\", \"http://saintpierremiquelon-la1ere.scdn.arkena.com/live.mp3\", \"Radio d\"\"Outre-mer, Tropical music\"),"+
//                    "(\"Wallis et Futuna 1ère\", \"http://wallisfutuna-la1ere.scdn.arkena.com/live.mp3\", \"Radio d\"\"Outre-mer, Tropical music\"),"+
//                    "(\"Tropiques FM\", \"http://tropiquesfm.scdn.arkena.com/live.mp3\", \"Radio d\"\"Outre-mer, Tropical music\"),"+
//                    "(\"Outre-Mer 1ère\", \"http://outremer-la1ere.scdn.arkena.com/live.mp3\", \"Radio d\"\"Outre-mer, Tropical music\"),"+
//                    "(\"Tropik FM\", \"http://streamantille.com:8027/;?1459713359833.mp3\", \"Radio d\"\"Outre-mer, Zouk, Tropical music\"),"+
//                    "(\"Campus FM\", \"http://live.radio-campus.org:8000/toulouse\", \"Radio étudiante, Radio locale, local service\"),"+
//                    "(\"Radio Brume\", \"http://ice1.impek.com/radiobrume?1457191613898.mp3\", \"Radio étudiante, Radio locale, local service\"),"+
//                    "(\"Radio Campus Amiens\", \"http://www.radiocampusamiens.com:8000/streamrcamp3\", \"Radio étudiante, Radio locale, local service\"),"+
//                    "(\"Radio Campus Angers\", \"http://80.82.229.202/rca\", \"Radio étudiante, Radio locale, local service\"),"+
//                    "(\"Radio Campus Bordeaux\", \"http://www.radiocampus.fr:8000/campus-bordeaux\", \"Radio étudiante, Radio locale, local service\"),"+
//                    "(\"Radio Campus Grenoble\", \"http://live.campusgrenoble.org:9000/rcg112?type=.mp3\", \"Radio étudiante, Radio locale, local service\"),"+
//                    "(\"Radio Campus Lille\", \"http://radiocampuslille.bcast.infomaniak.ch:8000/radiocampuslille-128.mp3\", \"Radio étudiante, Radio locale, local service\"),"+
//                    "(\"Radio Campus Orleans\", \"http://orleans.radiocampus.org:8000/stream_rco\", \"Radio étudiante, Radio locale, local service\"),"+
//                    "(\"Radio Campus Paris\", \"http://www.radiocampusparis.org:8000/stream_rcp\", \"Radio étudiante, Radio locale, local service\"),"+
//                    "(\"Radio Campus Rennes\", \"http://stream.radiocampusrennes.fr:8001/rcr\", \"Radio étudiante, Radio locale, local service\"),"+
//                    "(\"Radio U\", \"http://live.radio-campus.fr:8000/radio-u.mp3\", \"Radio étudiante, Radio locale, local service\"),"+
//                    "(\"100% Radio\", \"http://100radio-albi.ice.infomaniak.ch/100radio-albi-128.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Alpes 1 Gap\", \"http://alpes1gap.ice.infomaniak.ch/alpes1gap-high.mp3\", \"Radio locale, Local service\"),"+
                    "(\"ABC\", \"http://st2.zenorad.io:14424\", \"ABC, ABC\"),"+
                    "(\"Canal FM\", \"http://51.255.162.41:8000/canalfm.mp3\", \"Radio locale, Local service\");";


            String insertWebradioSQL8 ="INSERT INTO " + TABLE_WEBRADIO + " ("
                    + KEY_RADIO_STATION_NAME + ", "
                    + KEY_RADIO_URL + ", "
                    + KEY_MUSIC_GENRE +") VALUES "+

//                    "(\"Cristal FM\", \"http://cristalfm.no-ip.biz:8000/;stream.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Décibel\", \"http://195.154.113.150:8004/decibel.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Demoiselle FM\", \"http://demoisellefm.ice.infomaniak.ch/demoisellefm-128.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Est FM\", \"http://str0.creacast.com/estfm1\", \"Radio locale, Local service\"),"+
//                    "(\"FC Radio l\"\"Essentiel\", \"http://fcradio.ice.infomaniak.ch/fcradio-128.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"FGL Radio\", \"http://str0.creacast.com/fgl\", \"Radio locale, Local service\"),"+
//                    "(\"Fréquence Horizon\", \"http://horizonradio-arras.ice.infomaniak.ch/horizonradio-arras-128\", \"Radio locale, Local service\"),"+
//                    "(\"Fréquence Luz\", \"http://www.frequenceluz.com:8000/mobile?type=flash\", \"Radio locale, Local service\"),"+
//                    "(\"Fréquence Nautique\", \"http://str0.creacast.com/frequence_nautique\", \"Radio locale, Local service\"),"+
//                    "(\"Fréquence Sillé\", \"http://str45.streamakaci.com:6890\", \"Radio locale, Local service\"),"+
//                    "(\"Fugi FM\", \"http://92.175.228.15:8000/stream.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Fusion FM\", \"http://fusionfm.ice.infomaniak.ch:8000/fusionfm-192.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Grand Sud FM\", \"http://gr&sudfm-hd.scdn.arkena.com/live.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Grap\"\"hit\", \"http://213.246.53.78:8000/grafhit.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Hag\"\"FM\", \"http://213.246.51.78:9290/;stream.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Isabelle FM\", \"http://80.13.146.243:8000/;stream.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Jordanne FM\", \"http://live.jordannefm.com:8000/JFMCantalMD.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"K6FM Radio\", \"http://k6fm.ice.infomaniak.ch/k6fm-128.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Kiss FM Toulon/Marseille\", \"http://rga.scdn.arkena.com/rga.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Lyon 1ère\", \"http://broadcast.infomaniak.net:80/lyon1ere-high.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Perrine FM\", \"http://perrinefm.ice.infomaniak.ch/perrinefm-high.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Pic FM\", \"http://picfm.ice.infomaniak.ch/picfm-128.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Pyrénées Fm\", \"http://antenne.pyreneesfm.com/direct.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"R\"\"Courchevel 93.2\", \"http://live76.laradiostation.fr:8888/;stream.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Radio Albatros\", \"http://stream.inovacast.fr:7601/stream\", \"Radio locale, Local service\"),"+
//                    "(\"Radio Arverne\", \"http://radioarverne.ice.infomaniak.ch/arverne.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Radio Aviva\", \"http://stream.franclr.fr:8000/radioaviva\", \"Radio locale, Local service\"),"+
//                    "(\"Radio Boomerang\", \"http://109.190.81.136:8000/radioboomerang.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Radio Cicogne\", \"http://cerisefm.ice.infomaniak.ch/cerisefm-128.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Radio Craponne\", \"http://radio-shoutcast.cyber-streaming.com:8010/;\", \"Radio locale, Local service\"),"+
//                    "(\"Radio Cultures Dijon\", \"http://radiocristal.ice.infomaniak.ch/radiocristal-high.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Radio d\"\"ici\", \"http://live.francra.org:8000/Radiodici?1457217923170.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Radio Dijon Campus\", \"http://frequence.u-bourgogne.fr:8082/dijoncampus\", \"Radio locale, Local service\"),"+
//                    "(\"Radio FM43\", \"http://broadcast.infomaniak.net/fm43-128.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Radio Lodeve\", \"http://stream.franclr.fr:8000/radiolodeve?type=flash\", \"Radio locale, Local service\"),"+
//                    "(\"Radio Marseillette\", \"http://151.80.42.124:8000/marseillette.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Radio Melodie\", \"http://str0.creacast.com/melodie\", \"Radio locale, Local service\"),"+
//                    "(\"Radio Pays d\"\"Aurillac\", \"http://sv3.vestaradio.com:4420/;stream.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Radio Prévert\", \"http://195.154.81.167:8012/;?nocache=1457220153661\", \"Radio locale, Local service\"),"+
//                    "(\"Radio Puisaleine\", \"http://radiopuisaleine.ice.infomaniak.ch/radiopuisaleine-128.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Radio Transparence\", \"http://5.39.79.229:8000/;stream.nsv&type=mp3\", \"Radio locale, Local service\"),"+
//                    "(\"RDL Radio\", \"http://91.121.62.121:8300/;stream.nsv?time=21233\", \"Radio locale, Local service\"),"+
//                    "(\"RJR\", \"http://stream.rjrradio.fr/rjr.ogg\", \"Radio locale, Local service\"),"+
//                    "(\"RPL Radio\", \"http://91.121.62.121:9800/;stream.nsv?time=94256\", \"Radio locale, Local service\"),"+
//                    "(\"RVE 103.7\", \"http://str0.creacast.com/rve\", \"Radio locale, Local service\"),"+
//                    "(\"Sea FM\", \"http://94.23.209.107:8092/;stream.nsv\", \"Radio locale, Local service\"),"+
//                    "(\"Souvenirs FM\", \"http://souvenirsfm.bcast.infomaniak.ch/souvenirsfm-128.mp3\", \"Radio locale, Local service\"),"+
//                    "(\"Canal B\", \"http://stream.levillage.org/canalb?1362308951917.mp3?1457086124182.mp3\", \"Radio locale, Pop, Rennes, Rock, Underground, Ska, Ragga, Local service\"),"+
//                    "(\"Collines FM\", \"http://collineslaradio.bcast.infomaniak.ch/collineslaradio-128.mp3?1457099617475.mp3\", \"Radio locale, Sport, local service\"),"+
//                    "(\"Canal Sud\", \"http://stream.giss.tv:8000/canalsudtoulouse.mp3\", \"Radio locale, Toulouse, Local service\"),"+
//                    "(\"ABC French Rap\", \"http://streaming.radionomy.com/ABC-French-Rap\", \"Rap\"),"+
//                    "(\"Ado Rap FR\", \"http://adorapfr.bcast.infomaniak.ch/adorapfr-128.mp3\", \"Rap\"),"+
//                    "(\"Ado Rap US\", \"http://adorapus.bcast.infomaniak.ch/adorapus-128.mp3\", \"Rap\"),"+
//                    "(\"Allzic Radio Rap FR\", \"http://allzic02.ice.infomaniak.ch/allzic02.mp3\", \"Rap\"),"+
//                    "(\"Allzic Radio Rap US\", \"http://allzic01.ice.infomaniak.ch/allzic01.mp3\", \"Rap\"),"+
//                    "(\"Blackbox Rap FR\", \"http://blackboxrapfr.bcast.infomaniak.ch/blackboxrapfr-128.mp3\", \"Rap\"),"+
//                    "(\"Blackbox Rap US\", \"http://blackboxrapus.bcast.infomaniak.ch/blackboxrapus-128.mp3\", \"Rap\"),"+
//                    "(\"Generations 2015\", \"http://generations-thema.ice.infomaniak.ch/generations-thema.mp3\", \"Rap\"),"+
//                    "(\"Generations 88.2\", \"http://generationfm.ice.infomaniak.ch/generationfm-high.mp3\", \"Rap\"),"+
//                    "(\"Generations Booba\", \"http://generations-booba.ice.infomaniak.ch/generations-booba.mp3\", \"Rap\"),"+
//                    "(\"Generations En mode brand new\", \"http://gene-wr10.ice.infomaniak.ch/gene-wr10.mp3\", \"Rap\"),"+
//                    "(\"Generations Freestyle\", \"http://gene-wr09.ice.infomaniak.ch/gene-wr09.mp3\", \"Rap\"),"+
//                    "(\"Generations La Fouine\", \"http://generations-la-fouine.ice.infomaniak.ch/generations-la-fouine.mp3\", \"Rap\"),"+
//                    "(\"Generations Rap FR\", \"http://generationfm-rap.ice.infomaniak.ch/generationfm-rap-high.mp3\", \"Rap\"),"+
//                    "(\"Generations Rap FR Gold\", \"http://gene-wr06.ice.infomaniak.ch/gene-wr06.mp3\", \"Rap\"),"+
//                    "(\"Generations Rap US\", \"http://generationfm-underground.ice.infomaniak.ch/generationfm-underground-high.mp3\", \"Rap\"),"+
//                    "(\"Generations Rap US Gold\", \"http://gene-wr08.ice.infomaniak.ch/gene-wr08.mp3\", \"Rap\"),"+
//                    "(\"Generations Rohff\", \"http://generation-rohff.ice.infomaniak.ch/generation-rohff.mp3\", \"Rap\"),"+
//                    "(\"Generations Wati B\", \"http://generations-wati-b.ice.infomaniak.ch/generations-wati-b.mp3\", \"Rap\"),"+
//                    "(\"M2 Hip-hop\", \"http://live.m2stream.fr/m2hip-hop-128.mp3\", \"Rap\"),"+
//                    "(\"NRJ Black M\", \"http://triton.scdn.arkena.com/13083/live.mp3\", \"Rap\"),"+
//                    "(\"NRJ Classic Rap FR\", \"http://triton.scdn.arkena.com/14690/live.mp3\", \"Rap\"),"+
//                    "(\"NRJ Classic Rap US\", \"http://triton.scdn.arkena.com/13277/live.mp3\", \"Rap\"),"+
//                    "(\"NRJ Gangsta Rap\", \"http://triton.scdn.arkena.com/13223/live.mp3\", \"Rap\"),"+
//                    "(\"NRJ Gradur\", \"http://triton.scdn.arkena.com/14855/live.mp3\", \"Rap\"),"+
//                    "(\"NRJ Jul\", \"http://triton.scdn.arkena.com/13057/live.mp3\", \"Rap\"),"+
//                    "(\"NRJ La Fouine\", \"http://triton.scdn.arkena.com/13219/live.mp3\", \"Rap\"),"+
//                    "(\"NRJ Lacrim\", \"http://triton.scdn.arkena.com/15257/live.mp3\", \"Rap\"),"+
//                    "(\"NRJ Maître Gims\", \"http://triton.scdn.arkena.com/13163/live.mp3\", \"Rap\"),"+
//                    "(\"NRJ Nekfeu\", \"http://triton.scdn.arkena.com/14678/live.mp3\", \"Rap\"),"+
//                    "(\"NRJ Oreslan Casseurs Flowters\", \"http://triton.scdn.arkena.com/15130/live.mp3\", \"Rap\"),"+
//                    "(\"NRJ Rap FR\", \"http://triton.scdn.arkena.com/13091/live.mp3\", \"Rap\"),"+
//                    "(\"NRJ Rap US\", \"http://triton.scdn.arkena.com/13121/live.mp3\", \"Rap\"),"+
//                    "(\"NRJ SCH\", \"http://triton.scdn.arkena.com/15006/live.mp3\", \"Rap\"),"+
//                    "(\"NRJ Sexion d\"\"Assaut\", \"http://triton.scdn.arkena.com/13185/live.mp3\", \"Rap\"),"+
//                    "(\"NRJ Soprano\", \"http://triton.scdn.arkena.com/13127/live.mp3\", \"Rap\"),"+
//                    "(\"NRJ Wati B\", \"http://triton.scdn.arkena.com/13161/live.mp3\", \"Rap\"),"+
//                    "(\"OKLM\", \"http://163.172.6.199/radio/3093\", \"Rap\"),"+
//                    "(\"Blackbox @Work\", \"http://blackboxwork.bcast.infomaniak.ch/blackboxwork-128.mp3\", \"Rap, RnB\"),"+
//                    "(\"Blackbox Classic\", \"http://blackboxclassic.bcast.infomaniak.ch/blackboxclassic-128.mp3\", \"Rap, RnB\"),"+
//                    "(\"Fun Radio Hip-hop\", \"http://stream.funradio.sk:8000/hiphop128.mp3\", \"Rap, RnB\"),"+
//                    "(\"Generations Girl\", \"http://generations-girls.ice.infomaniak.ch/generations-girls.mp3\", \"Rap, RnB\"),"+
//                    "(\"Generations Newjack\", \"http://generations-njs.ice.infomaniak.ch/generations-njs.mp3\", \"Rap, RnB\"),"+
//                    "(\"HotmixRadio Hip-Hop\", \"http://streamingads.hotmixradio.fm/hotmixradio-hiphop-128.mp3\", \"Rap, RnB\"),"+
//                    "(\"MKM Urban\", \"http://mkmradio.com:8050/;file.mp3\", \"Rap, RnB\"),"+
//                    "(\"Mouv’ Xtra\", \"http://audio.scdn.arkena.com/11609/Mouvxtra-midfi128.mp3\", \"Rap, RnB\"),"+
//                    "(\"Tendance Ouest Urban\", \"http://streaming.tendanceouest.com/urban.mp3\", \"Rap, RnB\"),"+
//                    "(\"Urban 3\", \"http://hd.stream.frequence3.net/frequence3urban-256.mp3\", \"Rap, RnB\"),"+
//                    "(\"Urban Hit\", \"http://onlyrai.ice.infomaniak.ch/onlyrai-high.mp3\", \"Rap, RnB\"),"+
                    "(\"ABC\", \"http://st2.zenorad.io:14424\", \"ABC, ABC\"),"+
                    "(\"Radio Sun\", \"http://broadcast.infomaniak.net/radiosun-high.mp3\", \"Rap, RnB, Reggae\");";

            String insertWebradioSQL9 ="INSERT INTO " + TABLE_WEBRADIO + " ("
                    + KEY_RADIO_STATION_NAME + ", "
                    + KEY_RADIO_URL + ", "
                    + KEY_MUSIC_GENRE +") VALUES "+

//                    "(\"Generations Reggae\", \"http://generations-caraibes.ice.infomaniak.ch/generations-caraibes-high.mp3\", \"Reggae\"),"+
//                    "(\"La Grosse Radio Reggae\", \"http://hd.lagrosseradio.info:8300/;stream.nsv\", \"Reggae\"),"+
//                    "(\"NRJ Reggae\", \"http://triton.scdn.arkena.com/13247/live.mp3\", \"Reggae\"),"+
//                    "(\"Reggae Connection\", \"http://streaming.radionomy.com/Reggae-Connection\", \"Reggae\"),"+
//                    "(\"Allzic Radio Nationale 7\", \"http://allzic38.ice.infomaniak.ch/allzic38.mp3\", \"Relax\"),"+
//                    "(\"Elium Radio Urban Smooth\", \"http://streaming.radionomy.com/Elium\", \"Relax\"),"+
//                    "(\"Phare FM\", \"http://str0.creacast.com/pharefm\", \"Religion\"),"+
//                    "(\"Phare FM Worship\", \"http://str30.creacast.com/pharefm_worship\", \"Religion\"),"+
//                    "(\"Radio Arc en Ciel\", \"http://str0.creacast.com/arcencielweb\", \"Religion\"),"+
//                    "(\"Radio Bonne Nouvelle\", \"http://dn10215.hostncast.net:8001/;stream.mp3\", \"Religion\"),"+
//                    "(\"Radio Espérance\", \"http://stream.radio-esperance.net/esperance.mp3\", \"Religion\"),"+
//                    "(\"Radio Maria France\", \"http://217.114.200.126/;\", \"Religion\"),"+
//                    "(\"Radio Notre Dame\", \"http://windu.radionotredame.net/RadioNotreDame-Fm.mp3\", \"Religion\"),"+
//                    "(\"Radio Notre Dame Foi et Raison\", \"http://windu.radionotredame.net/RadioNotreDame-FoiEtRaison.mp3\", \"Religion\"),"+
//                    "(\"Radio Notre Dame Musique Sacrée\", \"http://windu.radionotredame.net/RadioNotreDame-MusiqueSacree.mp3\", \"Religion\"),"+
//                    "(\"Radio Présence\", \"http://str10.streamakaci.com:9540/;streamakaci\", \"Religion\"),"+
//                    "(\"Radio RCJ\", \"http://arj-france.ice.infomaniak.ch/arj-france-high.mp3\", \"Religion\"),"+
//                    "(\"RCF\", \"http://rcf.streamakaci.com/rcf.mp3\", \"Religion\"),"+
//                    "(\"RJL\", \"http://judaica.ice.infomaniak.ch/judaica-high.mp3\", \"Religion\"),"+
//                    "(\"Allzic Radio Rock FM\", \"http://allzic17.ice.infomaniak.ch/allzic17.mp3\", \"Rock\"),"+
//                    "(\"Bac FM\", \"http://streaming.bacfm.fr:8000/bacfm\", \"Rock\"),"+
//                    "(\"Fun Radio Rock\", \"http://stream.funradio.sk:8000/rock128.mp3\", \"Rock\"),"+
//                    "(\"HotmixRadio Rock\", \"http://streamingads.hotmixradio.fm/hotmixradio-rock-128.mp3\", \"Rock\"),"+
//                    "(\"La Grosse Radio Rock\", \"http://hd.lagrosseradio.info:8500/;stream.nsv\", \"Rock\"),"+
//                    "(\"M2 Rock\", \"http://live.m2stream.fr/m2rock-128.mp3\", \"Rock\"),"+
//                    "(\"Nostalgie Rock\", \"http://adwzg3.tdf-cdn.com/8561/nrj_171397.mp3\", \"Rock\"),"+
//                    "(\"NRJ Classic Rock\", \"http://triton.scdn.arkena.com/13293/live.mp3\", \"Rock\"),"+
//                    "(\"NRJ Metal\", \"http://triton.scdn.arkena.com/13297/live.mp3\", \"Rock\"),"+
//                    "(\"NRJ Rock\", \"http://triton.scdn.arkena.com/13289/live.mp3\", \"Rock\"),"+
//                    "(\"Oui FM\", \"http://ouifm.ice.infomaniak.ch/ouifm-high.mp3\", \"Rock\"),"+
//                    "(\"OUÏ FM - Alternatif\", \"http://ouifm2.ice.infomaniak.ch/ouifm2.mp3\", \"Rock\"),"+
//                    "(\"OUÏ FM Classic Rock\", \"http://ouifm3.ice.infomaniak.ch/ouifm3.mp3\", \"Rock\"),"+
//                    "(\"OUÏ FM Rock Indé\", \"http://ouifm5.ice.infomaniak.ch/ouifm5.mp3\", \"Rock\"),"+
//                    "(\"SOL FM 100.7\", \"http://stric6.streamakaci.com/solfm64.mp3\", \"Rock\"),"+
//                    "(\"Virage Radio\", \"http://virageradio.ice.infomaniak.ch/virageradio-high.mp3\", \"Rock\"),"+
//                    "(\"Virgin Radio Rock\", \"http://vr-wr5-mp3-128.scdn.arkena.com/virginradio.mp3\", \"Rock\"),"+
//                    "(\"Nostalgie Slow\", \"http://adwzg3.tdf-cdn.com/8563/nrj_178223.mp3\", \"Slow\"),"+
//                    "(\"Generations Soul\", \"http://gene-wr07.ice.infomaniak.ch/gene-wr07.mp3\", \"Soul\"),"+
//                    "(\"Jazz Radio Soul\", \"http://jazz-wr10.ice.infomaniak.ch/jazz-wr10-128.mp3\", \"Soul\"),"+
//                    "(\"Jazz Radio Stax & Motown\", \"http://jazz-wr15.ice.infomaniak.ch/jazz-wr15-128.mp3\", \"Soul\"),"+
//                    "(\"RBS\", \"http://www6.radiorbs.com:8000/RBSkleber\", \"Strasbourg, Radio locale, Local service\"),"+
//                    "(\"Allzic Radio Zouk\", \"http://allzic28.ice.infomaniak.ch/allzic28.mp3\", \"Zouk, Tropical music\"),"+
//                    "(\"NRJ Zouk\", \"http://triton.scdn.arkena.com/13253/live.mp3\", \"Zouk, Tropical music\"),"+
//                    "(\"MKM Caraïbes\", \"http://mkmradio.com:8000/;file.mp3\", \"Zouk, Tropical music\"),"+
//                    "(\"MKM Gold\", \"http://mkmradio.com:8020/;file.mp3\", \"Zouk, Tropical music\"),"+
//                    "(\"HotmixRadio Japan\", \"http://streamingads.hotmixradio.fm/hotmixradio-japan-128.mp3\", \"\"),"+
//                    "(\"HotmixRadio VIP\", \"http://streamingads.hotmixradio.fm/hotmixradio-vip-128.mp3\", \"\"),"+
//                    "(\"NRJ C\"\"Cauet\", \"http://triton.scdn.arkena.com/13089/live.mp3\", \"\"),"+
//                    "(\"NRJ Games\", \"http://triton.scdn.arkena.com/14702/live.mp3\", \"\"),"+
//                    "(\"NRJ Glee\", \"http://triton.scdn.arkena.com/13181/live.mp3\", \"\"),"+
//                    "(\"NRJ Guillaume Radio 2.0\", \"http://triton.scdn.arkena.com/13213/live.mp3\", \"\"),"+
//                    "(\"NRJ Keen\"\"V\", \"http://triton.scdn.arkena.com/13135/live.mp3\", \"\"),"+
//                    "(\"NRJ Kendji\", \"http://triton.scdn.arkena.com/13079/live.mp3\", \"\"),"+
//                    "(\"NRJ Les Anges 8\", \"http://triton.scdn.arkena.com/15254/live_aac.mp3\", \"\"),"+
//                    "(\"NRJ Louane\", \"http://triton.scdn.arkena.com/13229/live.mp3\", \"\"),"+
//                    "(\"NRJ Manu le 6-9\", \"http://triton.scdn.arkena.com/13175/live.mp3\", \"\"),"+
//                    "(\"NRJ Marseille\", \"http://triton.scdn.arkena.com/15126/live.mp3\", \"\"),"+
//                    "(\"NRJ MiKL\", \"http://triton.scdn.arkena.com/14682/live.mp3\", \"\"),"+
//                    "(\"NRJ Music Tour\", \"http://triton.scdn.arkena.com/15084/live.mp3\", \"\"),"+
//                    "(\"NRJ One Direction\", \"http://triton.scdn.arkena.com/13103/live.mp3\", \"\"),"+
//                    "(\"NRJ Selena Gomez\", \"http://triton.scdn.arkena.com/13249/live.mp3\", \"\"),"+
//                    "(\"NRJ Sextape\", \"http://triton.scdn.arkena.com/15158/live.mp3\", \"\"),"+
//                    "(\"NRJ Shawn Mendes\", \"http://triton.scdn.arkena.com/15171/live.mp3\", \"\"),"+
//                    "(\"NRJ Shazam\", \"http://triton.scdn.arkena.com/13053/live.mp3\", \"\"),"+
//                    "(\"NRJ Taylor Swift\", \"http://triton.scdn.arkena.com/13217/live.mp3\", \"\"),"+
//                    "(\"NRJ Trap Music\", \"http://triton.scdn.arkena.com/14694/live.mp3\", \"\"),"+
//                    "(\"RFM les Divas de RFM\", \"http://rfm-wr3-mp3-128.scdn.arkena.com/rfm.mp3\", \"\"),"+
//                    "(\"Virgin Tonic\", \"http://vr-wr6-mp3-128.scdn.arkena.com/virginradio.mp3\", \"\"),"+
//                    "(\"NRJ Les Anges 8\", \"http://triton.scdn.arkena.com/15254/live_aac.mp3\", \"Hits\"),"+
//                    "(\"NRJ TALENT\", \"http://triton.scdn.arkena.com/15424/live_aac.mp3\", \"Hits\"),"+
//                    "(\"NRJ Mad Mag\", \"http://triton.scdn.arkena.com/15361/live_aac.mp3\", \"Hits\"),"+
//                    "(\"NRJ MHD\", \"http://triton.scdn.arkena.com/15428/live_aac.mp3\", \"Rap\"),"+
//                    "(\"NRJ PLN\", \"http://triton.scdn.arkena.com/15470/live_aac.mp3\", \"Rap\"),"+
//                    "(\"NRJ Hits Summer 2016 Preview\", \"http://triton.scdn.arkena.com/15392/live_aac.mp3\", \"Hits 2016\"),"+
//                    "(\"NRJ Hits 2016\", \"http://triton.scdn.arkena.com/15242/live_aac.mp3\", \"Hits 2016\"),"+
                    "(\"ABC\", \"http://st2.zenorad.io:14424\", \"ABC, ABC\"),"+
                    "(\"NRJ Dubstep\", \"http://triton.scdn.arkena.com/15474/live_aac.mp3\", \"Dubstep\");";

            sqLiteDatabase.execSQL(insertWebradioSQL1);
            sqLiteDatabase.execSQL(insertWebradioSQL2);
            sqLiteDatabase.execSQL(insertWebradioSQL3);
            sqLiteDatabase.execSQL(insertWebradioSQL4);
            sqLiteDatabase.execSQL(insertWebradioSQL5);
            sqLiteDatabase.execSQL(insertWebradioSQL6);
            sqLiteDatabase.execSQL(insertWebradioSQL7);
            sqLiteDatabase.execSQL(insertWebradioSQL8);
            sqLiteDatabase.execSQL(insertWebradioSQL9);

            updateOrder(sqLiteDatabase);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            String buildWebradioSQL = "DROP TABLE IF EXISTS " + TABLE_WEBRADIO;
            Log.d(TAG, "onUpgrade SQL: " + buildWebradioSQL);

            sqLiteDatabase.execSQL(buildWebradioSQL);

            onCreate(sqLiteDatabase);
        }
    }

    public DbHelper open() throws SQLException {
        database = openHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        openHelper.close();
    }

    private void updateOrder(SQLiteDatabase database){
        String where = KEY_RADIO_STATION_NAME+" in (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String[] whereVals = {
                "RTL", "NRJ", "RMC", "Fun Radio", "Skyrock", "Europe 1", "Radio Fg", "RTL2", "France Info", "Virgin Radio",
                "Chérie FM", "Le Mouv", "ADO FM", "RFM", "Latina", "Hit West", "France Inter", "Nova",
                "Voltage", "Radio Classique", "Alouette", "MFM", "Jazz Radio", "France Culture"
        };
        ContentValues vals = new ContentValues();
        vals.put(KEY_ORDER,1);
        database.update(TABLE_WEBRADIO, vals, where, whereVals);
    }
}