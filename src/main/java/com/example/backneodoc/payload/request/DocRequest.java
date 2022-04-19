package com.example.backneodoc.payload.request;

import com.example.backneodoc.models.Tag;

import javax.validation.constraints.NotBlank;
import java.util.Set;

public class DocRequest {

    @NotBlank
    private String titre;

    @NotBlank
    private Set<String> tag;

    @NotBlank
    private String dep;

    public String getTitre() {return titre;}

    public void setTitre(String titre) {this.titre = titre;}

    public Set<String> getTag() {return tag;}

    public void setTag(Set<String> tag) {this.tag = tag;}

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }
}
