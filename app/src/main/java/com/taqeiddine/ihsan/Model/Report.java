package com.taqeiddine.ihsan.Model;

import com.taqeiddine.ihsan.Model.Profile.Utilisateur;
import com.taqeiddine.ihsan.Model.Publications.Publication;

public class Report {
    private Utilisateur utilisateur;
    private Publication publication;
    private String rapport;

    public Report(Utilisateur utilisateur, Publication publication, String rapport) {
        this.utilisateur = utilisateur;
        this.publication = publication;
        this.rapport = rapport;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public String getRapport() {
        return rapport;
    }

    public void setRapport(String rapport) {
        this.rapport = rapport;
    }
}
