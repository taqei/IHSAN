package com.taqeiddine.ihsan.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Don {
    private Besoin besoin;
    private int Qte;

    public Don(Besoin besoin, int qte) {
        this.besoin = besoin;
        Qte = qte;
    }

    public Don() {
    }

    public Besoin getBesoin() {
        return besoin;
    }

    public void setBesoin(Besoin besoin) {
        this.besoin = besoin;
    }

    public int getQte() {
        return Qte;
    }

    public void setQte(int qte) {
        Qte = qte;
    }
    public static ArrayList<Don> donsfrombesoins(ArrayList<Besoin> besoins){
        ArrayList<Don> arrayList=new ArrayList<>();
        for (int i=0;i<besoins.size();i++){
            arrayList.add(new Don((besoins.get(i)),0));
        }
        return arrayList;
    }

    public static Don fromJSON(JSONObject s){

        try {
            Don don=new Don();
            don.setQte(s.getInt("qte"));
            don.setBesoin(Besoin.fromJson(s.getJSONObject("besoin")));
            return don;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
