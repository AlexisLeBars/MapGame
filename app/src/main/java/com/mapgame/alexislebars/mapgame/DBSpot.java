package com.mapgame.alexislebars.mapgame;

import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Integer;


public class DBSpot {
    public DBSpot(){
        db = new ArrayList<>();
        this.db.add(new Spot(new LatLng(48.53839 , 2.347287),1,false));//Paris
        this.db.add(new Spot(new LatLng(51.500835, -0.126125),1,false));//Londre
        this.db.add(new Spot(new LatLng(-22.9522546,-43.2104932),1,false));//Rio Jesus
        this.db.add(new Spot(new LatLng(22.463119, 113.999668),2,false));//Hong Kong
        this.db.add(new Spot(new LatLng(27.1739386,78.0421101),1,false));//Taj mahal
        this.db.add(new Spot(new LatLng(50.8935589,4.3428836),1,false));//atomium
        this.db.add(new Spot(new LatLng(52.5144934,13.3484954),1,false));//allemagne
        this.db.add(new Spot(new LatLng(40.420025,-3.6881135),1,false));//espagne
        this.db.add(new Spot(new LatLng(38.6922679,-9.2157719),1,false));//portugal
        this.db.add(new Spot(new LatLng(55.5869039,-3.0191189),3,false));// Coin paumé (royaume uni)
        this.db.add(new Spot(new LatLng(28.683333,83.856667),1,false));// himalaya
        this.db.add(new Spot(new LatLng(43.0782094,-79.074204),2,false));// niagara
        this.db.add(new Spot(new LatLng(43.0828164,-79.0741631),2,false));// niagara2
        this.db.add(new Spot(new LatLng(36.122505,-112.12134),1,false));//grand canyon 1
        this.db.add(new Spot(new LatLng(42.6959086,23.331986),3,false));// hagia sophia
        this.db.add(new Spot(new LatLng(30.3284544,35.4443622),2,false));// pétra jordani
        this.db.add(new Spot(new LatLng(30.33054,35.4423383),3,false));// pétra 2
        this.db.add(new Spot(new LatLng(20.6842848,-88.5677826),3,false));// chichen itza
        this.db.add(new Spot(new LatLng(41.8912216,12.4916112),3,false));
        this.db.add(new Spot(new LatLng(14.3558196,100.5590247),3,false));
        this.db.add(new Spot(new LatLng(-27.122067,-109.2890096),3,false));
        this.db.add(new Spot(new LatLng(-27.1151265,-109.3954123),2,false));
        this.db.add(new Spot(new LatLng(13.4125244,103.8651291),3,false));
        this.db.add(new Spot(new LatLng(48.8055984,2.1174569),3,false));
        this.db.add(new Spot(new LatLng(55.6928117,12.599225),2,false));
        this.db.add(new Spot(new LatLng(51.8851585,4.6399012),2,false));
        this.db.add(new Spot(new LatLng(57.3228575,-4.4243817),3,false));
        this.db.add(new Spot(new LatLng(48.6318479,-1.5091184),3,false));
        this.db.add(new Spot(new LatLng(52.516245,13.3769667),1,false));
        this.db.add(new Spot(new LatLng(41.4030319,2.1735623),3,false));
        this.db.add(new Spot(new LatLng(-25.3448384,131.032539),2,false));
        this.db.add(new Spot(new LatLng(-25.3423968,131.0089774),1,false));
        this.db.add(new Spot(new LatLng(35.3656282,138.7324052),3,false));
        this.db.add(new Spot(new LatLng(51.1786423,-1.8262766),1,false));

        this.db.add(new Spot(new LatLng(-17.9247153,25.8514839),2,false));
        this.db.add(new Spot(new LatLng(71.1709152,25.7834537),1,false));
        this.db.add(new Spot(new LatLng(-25.695376,-54.4379301),3,false));
        this.db.add(new Spot(new LatLng(44.0915517,3.0218092),2,false));
        this.db.add(new Spot(new LatLng(52.5185078,13.399629),2,false));
        this.db.add(new Spot(new LatLng(50.6789177,4.4053262),2,false));
        this.db.add(new Spot(new LatLng(28.683333,83.856667),3,false));
        this.db.add(new Spot(new LatLng(-8.3749036,115.451302),2,false));
        this.db.add(new Spot(new LatLng(40.747067,14.5013783),3,false));
        this.db.add(new Spot(new LatLng(-0.0021378,-78.4554744),1,false));
        this.db.add(new Spot(new LatLng(48.885961,2.3432133),1,false));
        this.db.add(new Spot(new LatLng(49.3604639,-0.8555708),2,false));
        this.db.add(new Spot(new LatLng(29.6554942,91.1185792),2,false));
        this.db.add(new Spot(new LatLng(5.9701254,-62.5362199),1,false));
        this.db.add(new Spot(new LatLng(36.1484641,-5.3396137),3,false));
        this.db.add(new Spot(new LatLng(-79.75,-82.5),3,false));


        curIdxs = new ArrayList<>(db.size());
        cur = -1;
    }

    public ArrayList<Spot> db;
    public ArrayList<Integer> curIdxs;
    public int cur;

    private class Spot{
        public LatLng ll;
        public Integer i;
        public Boolean b;

        public Spot(LatLng l,Integer i,Boolean b){
            this.ll = l;
            this.i = i;
            this.b = b;
        }
    }

    public LatLng getCurPos(){
        return db.get(cur).ll;
    }

    public LatLng getNewSpot(int level){
        int nb = getNbRowAvailableForLevel(level);

        if(nb == 0 ){
            Log.d("Lieu","0 available");
            return null;
        }
        int r = (new Random()).nextInt(nb+1);
        for(Spot s : db){
            if(r == 0 && (s.i == level && !s.b )){
                s.b = true;
                Log.d("Lieu","vu "+db.indexOf(s));
                cur = db.indexOf(s);
                return s.ll;
            }else{
                if( s.i == level && !s.b ){
                    r--;
                }
            }
        }
        Log.d("Lieu","ret null");
        return null;
    }
    public void setViewedSpots(){
        for(Spot s : db){
            if(curIdxs.contains(db.indexOf(s)) && !s.b){
                s.b = true;
                Log.d("Lieu r","vu "+db.indexOf(s));
            }
        }
    }
    private int getNbRowAvailableForLevel(int level){
        int ret = 0;
        for(Spot s : db){
            if(s.i == level && !s.b){
                ret++;
            }
        }
        return ret;
    }
    public void reset(){
        for( Spot s: db){
            s.b = false;
        }
        cur = -1;
        curIdxs.clear();
    }
}