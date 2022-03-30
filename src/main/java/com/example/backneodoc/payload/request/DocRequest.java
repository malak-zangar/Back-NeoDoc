package com.example.backneodoc.payload.request;

import com.example.backneodoc.models.Departement;
import com.example.backneodoc.models.Tag;

import javax.validation.constraints.NotBlank;
import java.util.Set;

public class DocRequest {

    @NotBlank
    private String titre;

    @NotBlank
    private Set<Tag> tag;

    @NotBlank
    private Set<Departement> dep;

    public String getTitre() {return titre;}

    public void setTitre(String titre) {this.titre = titre;}

    public Set<Tag> getTag() {return tag;}

    public void setTag(Set<Tag> tag) {this.tag = tag;}

    public Set<Departement> getDep() {return dep;}

    public void setDep(Set<Departement> dep) {this.dep = dep;}
}
