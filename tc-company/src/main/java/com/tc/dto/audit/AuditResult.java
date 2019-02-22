package com.tc.dto.audit;

import com.tc.db.entity.Audit;
import com.tc.dto.trans.Trans;
import com.tc.dto.trans.TransEnum;

import java.util.List;

public class AuditResult {
    private Trans admin;
    private List<TransEnum> results;
    private List<TransEnum> types;
    private List<Audit> audits;

    public AuditResult() {
    }

    public AuditResult(List<TransEnum> results, List<TransEnum> types, List<Audit> audits,Trans admin) {
        this.results = results;
        this.types = types;
        this.audits = audits;
        this.admin = admin;
    }

    public List<TransEnum> getResults() {
        return results;
    }

    public void setResults(List<TransEnum> results) {
        this.results = results;
    }

    public List<TransEnum> getTypes() {
        return types;
    }

    public void setTypes(List<TransEnum> types) {
        this.types = types;
    }

    public List<Audit> getAudits() {
        return audits;
    }

    public void setAudits(List<Audit> audits) {
        this.audits = audits;
    }

    public Trans getAdmin() {
        return admin;
    }

    public void setAdmin(Trans admin) {
        this.admin = admin;
    }
}
