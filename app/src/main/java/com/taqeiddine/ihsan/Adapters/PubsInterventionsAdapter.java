package com.taqeiddine.ihsan.Adapters;

import android.app.Activity;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.taqeiddine.ihsan.Model.Don;
import com.taqeiddine.ihsan.Model.Intervention;
import com.taqeiddine.ihsan.Model.Publications.Publication;
import com.taqeiddine.ihsan.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PubsInterventionsAdapter extends BaseExpandableListAdapter {
    private Publication publication;
    private ArrayList<Intervention> interventions;
    public LayoutInflater inflater;
    public Activity activity;

    public PubsInterventionsAdapter(Publication publication,Activity activity) {
        this.publication = publication;
        this.interventions= publication.getInterventions();
        this.activity=activity;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getGroupCount() {
        return interventions.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Log.i("lenovoo",""+interventions.get(groupPosition).getDonsInterventions().size());
        return getGroup(groupPosition).getDonsInterventions().size();
    }

    @Override
    public Intervention getGroup(int groupPosition) {
        return interventions.get(groupPosition);
    }

    @Override
    public Don getChild(int groupPosition, int childPosition) {
        return interventions.get(groupPosition).getDonsInterventions().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    public ArrayList<Intervention> getInterventions(){
        return interventions;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_interventiondetail, null);
        }

        Intervention intervention= (Intervention) interventions.get(groupPosition);
        //ImageView photodeprofil=(ImageView)convertView.findViewById(R.id.row_interventiondetail_photodeprofil),delete=(ImageView) convertView.findViewById(R.id.row_interventiondetail_delete);

        TextView nom=(TextView) convertView.findViewById(R.id.row_interventiondetail_nom);
        TextView date=(TextView) convertView.findViewById(R.id.row_interventiondetail_date);
        TextView etat=(TextView) convertView.findViewById(R.id.row_interventiondetail_etat);
        //Switch switche=(Switch) convertView.findViewById(R.id.row_interventiondetail_switch);

        nom.setText(intervention.getUtilisateur().getNom()+"  "+intervention.getUtilisateur().getPrenom());
        date.setText("hhhh");
        if(intervention.isInterventionvalidee())
            etat.setText("Approuvée");
        else
            etat.setText("En attente");

        Log.i("lenovoo","hilo"+getChildrenCount(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_interventiondon, null);
        }
        Don don=getChild(groupPosition,childPosition);

        ImageView iconarticle;
        TextView nomarticle=(TextView) convertView.findViewById(R.id.row_interventiondon_nom),qte=(TextView) convertView.findViewById(R.id.row_interventiondon_qte);
        nomarticle.setText(don.getBesoin().getArticle().getNomArticle());
        qte.setText(don.getQte()+" "+don.getBesoin().getArticle().getUniteArticle());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
