package com.taqeiddine.ihsan.Model.Profile;

/**
 * Created by Taqei on 09/03/2018.
 */

public class ChefAssociation extends Utilisateur {
    Association association;

    public ChefAssociation(String idprofile) {
        super(idprofile);
    }

    public ChefAssociation(Utilisateur utilisateur,Association association){
        super(utilisateur.getIdprofile(),utilisateur.getNom(),utilisateur.getPrenom(),utilisateur.getPhone(),utilisateur.getEmail(),null);
        this.setPhotodeprofil(utilisateur.getPhotodeprofil());
        this.setConfiance(utilisateur.getConfiance());
        this.association=association;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }
}
