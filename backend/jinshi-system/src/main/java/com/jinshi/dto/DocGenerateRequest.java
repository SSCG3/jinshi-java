// src/main/java/com/jinshi/controller/DocGenerateRequest.java

package com.jinshi.dto;

import java.util.List;

public class DocGenerateRequest {
    private String docType;
    private String requirements;
    private List<String> referenceMaterials;

    // Getters and Setters
    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public List<String> getReferenceMaterials() {
        return referenceMaterials;
    }

    public void setReferenceMaterials(List<String> referenceMaterials) {
        this.referenceMaterials = referenceMaterials;
    }
}