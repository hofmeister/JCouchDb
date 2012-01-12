package com.vonhofmeister.jcouchdb.documents;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Henrik Hofmeister (@vonhofdk)
 */
public class UUIDBatch {
    private Set<String> uuids = new HashSet<String>();

    public Set<String> getUuids() {
        return uuids;
    }
}
