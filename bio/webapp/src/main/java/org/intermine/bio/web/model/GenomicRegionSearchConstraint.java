package org.intermine.bio.web.model;

/*
 * Copyright (C) 2002-2021 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.util.List;
import java.util.Set;

/**
 * A class to represent the constraints a user selected including a list of features and genomic
 * regions, etc.
 *
 * @author Fengyuan Hu
 */
public class GenomicRegionSearchConstraint
{
    private String orgName = null;
    private String chrAssembly = null;
    private Set<Class<?>> featureTypes = null;
    private List<GenomicRegion> genomicRegionList = null;
    private int extendedRegionSize = 0;

    private boolean strandSpecific;

    // TODO add liftOver contraints: org, source, target

    /**
     * @return the orgName
     */
    public String getOrgName() {
        return orgName;
    }
    /**
     * @param orgName the orgName to set
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * @return the chrAssembly
     */
    public String getChrAssembly() {
        return chrAssembly;
    }
    /**
     * @param orgName the chrAssembly to set
     */
     public void setChrAssembly(String chrAssembly) {
        this.chrAssembly = chrAssembly;
    }
    /**
     * @return the feature types to search for
     */
    public Set<Class<?>> getFeatureTypes() {
        return featureTypes;
    }
    /**
     * @param featureTypes the feature types to search for
     */
    public void setFeatureTypes(Set<Class<?>> featureTypes) {
        this.featureTypes = featureTypes;
    }
    /**
     * @return the genomicRegionList
     */
    public List<GenomicRegion> getGenomicRegionList() {
        return genomicRegionList;
    }
    /**
     * @param genomicRegionList the genomicRegionList to set
     */
    public void setGenomicRegionList(List<GenomicRegion> genomicRegionList) {
        this.genomicRegionList = genomicRegionList;
    }

    /**
     * @return the extendedRegionSize
     */
    public int getExtendedRegionSize() {
        return extendedRegionSize;
    }
    /**
     * @param extendedRegionSize the extendedRegionSize to set
     */
    public void setExtendedRegionSize(int extendedRegionSize) {
        this.extendedRegionSize = extendedRegionSize;
    }

    /**
     * @param strandSpecific whether or not this search specifies a strand
     */
    public void setStrandSpecific(boolean strandSpecific) {
        this.strandSpecific = strandSpecific;
    }

    /**
     * @return strandSpecific value
     */
    public boolean getStrandSpecific() {
        return strandSpecific;
    }

    /**
     * @param obj a GenomicRegionSearchConstraint object
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GenomicRegionSearchConstraint) {
            GenomicRegionSearchConstraint c = (GenomicRegionSearchConstraint) obj;
            boolean retVal = (extendedRegionSize == c.getExtendedRegionSize()
                    && genomicRegionList.equals(c.getGenomicRegionList())
                    && featureTypes.equals(c.getFeatureTypes())
                    && orgName.equals(c.getOrgName())
                    && strandSpecific == c.getStrandSpecific());
            if (chrAssembly != null && c.getChrAssembly() != null) {
                retVal = retVal && chrAssembly.equals(c.getChrAssembly());
            }
            return retVal;
        }
        return false;
    }

    /**
     * @return hashCode
     */
    @Override
    public int hashCode() {
        int retVal = extendedRegionSize + genomicRegionList.hashCode() + featureTypes.hashCode()
            + orgName.hashCode();
        if (chrAssembly != null) {
            retVal += chrAssembly.hashCode();
        }
        return retVal;
    }
}
