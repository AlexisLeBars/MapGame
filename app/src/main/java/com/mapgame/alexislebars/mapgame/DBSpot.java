package com.mapgame.alexislebars.mapgame;

import android.text.BoringLayout;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;
import java.util.ArrayList;
import java.lang.Integer;

/**
 * Created by aurelien on 24/01/18.
 */

public class DBSpot {
    public DBSpot(){
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
        curIdxs = new ArrayList(db.size());
    }

    public ArrayList<Spot> db = new ArrayList<>();
    public ArrayList<Integer> curIdxs;

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

    public LatLng getNewSpot(int level){
        int nb = getNbRowAvilableForLevel(level);

        if(nb == 0 ){
            return null;
        }
        int r = (new Random()).nextInt(nb+1);
        for(Spot s : db){
            if(r == 0 && (s.i == level && !s.b )){
                s.b = true;
                curIdxs.add(db.indexOf(s));
                return s.ll;
            }else{
                if( s.i == level && !s.b ){
                    r--;
                }
            }
        }
        return null;
    }
    public void setViewedSpots(ArrayList<Integer> idxs){
        for(Spot s : db){
            if(idxs.contains(db.indexOf(s)) && s.b == false){
                s.b = true;
                Log.d("Lieu","vu "+db.indexOf(s));
            }
        }
    }
    private int getNbRowAvilableForLevel(int level){
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
    }
}
